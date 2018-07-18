import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
// Animasjon:
import javafx.scene.shape.*;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.util.ArrayList;

public class UIMaster extends Application{
  private static StackPane[][] feltene = new StackPane[8][8];
  // X - akse: trekk fra 1. Y-akse: 0 = A, 7 = H

  private static Text statusFelt, hoyreTekstFelt;
  private static Administrator administrator;
  private static Parti partiet;
  private static StillingReell stilling;
  private static ArrayList<ImageView> hviteBrikkerIV = new ArrayList<ImageView>(16);
  private static ArrayList<ImageView> svarteBrikkerIV = new ArrayList<ImageView>(16);

  public void startUI(String[] args){
    launch(args);
  }


  @Override
  public void start(Stage teater){
    // Oppretter spillbrett med Rektangler:
    GridPane brettGP = new GridPane();
    boolean hvittFelt = false;
    for(int y = 0; y < 10; y ++){
      for(int x = 0; x < 10; x++){
        if(x == 0 || x == 9){
          if(y == 0 || y == 9){
            Rectangle r = new Rectangle(Settinger.RUTE_BREDDE / 2,
                                        Settinger.RUTE_BREDDE / 2);
            r.setFill(Color.DARKGREY);
            brettGP.add(r, x, y);
          }
          else{
            Rectangle r = new Rectangle(Settinger.RUTE_BREDDE / 2,
                                        Settinger.RUTE_BREDDE);
            r.setFill(Color.DARKGREY);
            StackPane stack = new StackPane();
            stack.getChildren().add(r);
            Text tekst = new Text(Integer.toString(9 - y));
            tekst.setFont(new Font(12));
            tekst.setTextAlignment(TextAlignment.CENTER);
            stack.getChildren().add(tekst);
            brettGP.add(stack, x, y);
          }
        }
        else{
          if(y == 0 || y == 9){
            Rectangle r = new Rectangle(Settinger.RUTE_BREDDE,
                                        Settinger.RUTE_BREDDE / 2);
            r.setFill(Color.DARKGREY);
            StackPane stack = new StackPane();
            stack.getChildren().add(r);
            Text tekst = new Text(Character.toString((char)(x + 64)));
            tekst.setFont(new Font(12));
            tekst.setTextAlignment(TextAlignment.CENTER);
            stack.getChildren().add(tekst);
            brettGP.add(stack, x, y);
          }
          else{
            Rectangle varselFargeR = new Rectangle(Settinger.RUTE_BREDDE,
                                                   Settinger.RUTE_BREDDE);
            varselFargeR.setFill(Color.RED);
            Rectangle r = new Rectangle(Settinger.RUTE_BREDDE,
                                        Settinger.RUTE_BREDDE);
            if(hvittFelt){ r.setFill(Color.WHITE); }
            else{ r.setFill(Color.GREY); }
            StackPane stack = new StackPane();
            feltene[x - 1][8 - y] = stack;
            stack.getChildren().add(varselFargeR);
            stack.getChildren().add(r);
            brettGP.add(stack, x, y);
            hvittFelt = !hvittFelt;
          }

        }
      }
      hvittFelt = !hvittFelt;
    }

    statusFelt = new Text("Tekstfeltet");
    statusFelt.setWrappingWidth(Settinger.BRETT_BREDDE + Settinger.RUTE_BREDDE);
    //statusFelt.setY(Settinger.BRETT_BREDDE + 100);
    statusFelt.setFont(new Font(16));
    statusFelt.setTextAlignment(TextAlignment.CENTER);

    hoyreTekstFelt = new Text("Høyre");
    hoyreTekstFelt.setWrappingWidth(Settinger.RUTE_BREDDE * 6);
    hoyreTekstFelt.setFont(new Font(12));




    Pane hovedKulisse = new Pane();
    GridPane layoutGP = new GridPane();
    layoutGP.add(brettGP, 0, 0);
    layoutGP.add(hoyreTekstFelt, 1, 0);
    layoutGP.add(statusFelt, 0, 1);

    hovedKulisse.getChildren().add(layoutGP);


    Scene scene = new Scene(hovedKulisse);
    teater.setScene(scene);
    teater.setTitle("Java-sjakk");
    teater.show();

    // STARTER OPP SJAKK LOGIKK:
    administrator = new Administrator(this);
    partiet = administrator.hentParti();
    stilling = administrator.hentStilling();
    for(int i = 0; i < 100000000; i++){}
    hoyreTekstFelt.setText(stilling.toString());
    //hoyreTekstFelt.setText(stilling.hentEvalStreng());
    // OPPSETT FERDIG, STARTER AUTOSJAKK:
    if(partiet.hentAutomatisk(0)){ autoTrekk(); }

  }

  public void brikkeFlyttetMedMus(ImageView iv, double fraX, double fraY, double tilX, double tilY){
    int offset = Settinger.RUTE_BREDDE / 2;
    int fraFeltX = (int)((fraX - offset)/Settinger.RUTE_BREDDE);
    int fraFeltY = 7 - (int)((fraY - offset)/Settinger.RUTE_BREDDE);
    int tilFeltX = (int)((tilX - offset)/Settinger.RUTE_BREDDE);
    int tilFeltY = 7 - (int)((tilY - offset)/Settinger.RUTE_BREDDE);
    //statusFelt.setText(s);
    //hoyreTekstFelt.setText(stilling.hentEvalStreng());

    if(tilFeltX < 0 || tilFeltX > 7 || tilFeltY < 0 || tilFeltY > 7) {
      tilFeltX = fraFeltX;
      tilFeltY = fraFeltY;
    }
    int trekkType = stilling.manueltTrekk(fraFeltX, fraFeltY, tilFeltX, tilFeltY);

    visTrekk(false, trekkType, fraFeltX, fraFeltY, tilFeltX, tilFeltY);
    if(partiet.hentAutomatisk(stilling.hentNesteTrekkFarge())){ autoTrekk(); }
  }

  private void autoTrekk(){
    Trekk trekket = stilling.autoTrekk();
    visTrekk(true, trekket.hentTrekkType(), trekket.hentFraX(), trekket.hentFraY(),
             trekket.hentTilX(), trekket.hentTilY());
  }

  // Metode som brukes av både manuelt og auto trekk, viser trekket:
  private void visTrekk(boolean auto, int trekkType, int fraFeltX, int fraFeltY, int tilFeltX, int tilFeltY){
    int farge = 1 - stilling.hentNesteTrekkFarge();
    boolean nesteAuto = partiet.hentAutomatisk(1- farge);
    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];
    ImageView iv = (ImageView)fraSP.getChildren().get(2);
    hoyreTekstFelt.setText(stilling.toString());
    //hoyreTekstFelt.setText(stilling.hentEvalStreng());
    String s = "Fra: " + fraFeltX + ", " + fraFeltY + " til: " + tilFeltX + ", " + tilFeltY;
    statusFelt.setText(s + " trekkType: " + trekkType);

    if(auto){ animerFlyttAvBrikke(fraFeltX, fraFeltY, tilFeltX, tilFeltY, trekkType, nesteAuto); }
    else{
      // Kun ved manuelt trekk:
      if( trekkType >= 0 && trekkType <= 3){
        fraSP.getChildren().remove(iv);
        iv.setTranslateX(0);
        iv.setTranslateY(0);
        tilSP.getChildren().add(iv);
      }
      if(trekkType == -1){
        // Ikke lovlig trekk, brikke flyttes tilbake:
        animerReturAvBrikke(iv);
      }
      else if(trekkType == 0){
        // Trekk til tomt felt:
      }
      else if(trekkType == 1){
        // Trekk med utslag av motstanders brikker:
        animerUtslagAvBrikke(tilFeltX, tilFeltY, nesteAuto);
      }
      else if(trekkType == 2){
        // Lang rokade:
        // Flytter tårnet:
        animerFlyttAvBrikke(0, fraFeltY, 3, fraFeltY, trekkType, nesteAuto);
      }
      else if(trekkType == 3){
        // Kort rokade:
        // Flytter tårnet:
        animerFlyttAvBrikke(7, fraFeltY, 5, fraFeltY, trekkType, nesteAuto);
      }
      else if(trekkType == 4){
        // bondeforvandling uten utslag av brikke:
        animerBondeForvandling(tilFeltX, tilFeltY, 0, nesteAuto);
        fraSP.getChildren().remove(iv);
        if(hviteBrikkerIV.contains(iv)){
          hviteBrikkerIV.remove(iv);
          UIHvitDronning nyDronning = new UIHvitDronning(this, tilFeltX, tilFeltY);
          ImageView nyDronningIV = nyDronning.bildeViser();
          tilSP.getChildren().add(nyDronningIV);
          hviteBrikkerIV.add(nyDronningIV);
        }
        else{
          svarteBrikkerIV.remove(iv);
          UISvartDronning nyDronning = new UISvartDronning(this, tilFeltX, tilFeltY);
          ImageView nyDronningIV = nyDronning.bildeViser();
          tilSP.getChildren().add(nyDronningIV);
          svarteBrikkerIV.add(nyDronningIV);
        }

      }
      else if(trekkType == 5){
        // bondeforvandling med utslag av brikke:
        animerUtslagAvBrikke(tilFeltX, tilFeltY, nesteAuto);
        fraSP.getChildren().remove(iv);
        if(hviteBrikkerIV.contains(iv)){
          hviteBrikkerIV.remove(iv);
          UIHvitDronning nyDronning = new UIHvitDronning(this, tilFeltX, tilFeltY);
          ImageView nyDronningIV = nyDronning.bildeViser();
          tilSP.getChildren().add(nyDronningIV);
          hviteBrikkerIV.add(nyDronningIV);
        }
        else{
          svarteBrikkerIV.remove(iv);
          UISvartDronning nyDronning = new UISvartDronning(this, tilFeltX, tilFeltY);
          ImageView nyDronningIV = nyDronning.bildeViser();
          tilSP.getChildren().add(nyDronningIV);
          svarteBrikkerIV.add(nyDronningIV);
        }
      }
   }



  }

  public void animerReturAvBrikke(ImageView iv){
    // OBS: Flytter også brikkeIV over til nytt felt i feltene!!
    TranslateTransition overgang = new TranslateTransition(Duration.millis(Settinger.ANIMER_TREKK_TID), iv);
    overgang.setToX(0);
    overgang.setToY(0);
    overgang.play();
  }

  public void animerFlyttAvBrikke(int fraFeltX, int fraFeltY, int tilFeltX, int tilFeltY, int nesteHandling, boolean nesteAuto){
    ImageView iv;
    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];
    iv = (ImageView)fraSP.getChildren().get(2);
    iv.getParent().toFront();
    int deltaX = (tilFeltX - fraFeltX) * Settinger.RUTE_BREDDE;
    int deltaY = (fraFeltY - tilFeltY) * Settinger.RUTE_BREDDE;
    TranslateTransition brikkeBevegelse = new TranslateTransition(Duration.millis(Settinger.ANIMER_TREKK_TID), iv);
    // Flytter brikkeIV over til nytt felt ETTER animasjon:
    brikkeBevegelse.setOnFinished(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent event){
        fraSP.getChildren().remove(iv);
        iv.setTranslateX(0);
        iv.setTranslateY(0);
        tilSP.getChildren().add(iv);
        switch(nesteHandling){
          case 0:
            if(nesteAuto){ autoTrekk(); }
            break;
          case 1:
            animerUtslagAvBrikke(tilFeltX, tilFeltY, nesteAuto);
            break;
          case 2:
            animerFlyttAvBrikke(0, fraFeltY, 3, fraFeltY, 0, nesteAuto);
            break;
          case 3:
            animerFlyttAvBrikke(7, fraFeltY, 5, fraFeltY, 0, nesteAuto);
            break;
          case 4:
            animerBondeForvandling(tilFeltX, tilFeltY, 0, nesteAuto);
            break;
          case 5:
            animerBondeForvandling(tilFeltX, tilFeltY, 1, nesteAuto);
            break;
        }

      }
    });
    brikkeBevegelse.setToX(deltaX);
    brikkeBevegelse.setToY(deltaY);
    brikkeBevegelse.play();
  }

  public void animerUtslagAvBrikke(int feltX, int feltY, boolean nesteAuto){
    ImageView hovedBrikkeIV;
    ImageView utslattBrikkeIV;
    Rectangle feltet;
    StackPane SP = feltene[feltX][feltY];
    utslattBrikkeIV = (ImageView)SP.getChildren().get(2);
    hviteBrikkerIV.remove(utslattBrikkeIV);
    svarteBrikkerIV.remove(utslattBrikkeIV);
    feltet = (Rectangle)SP.getChildren().get(1);
    FadeTransition blinking = new FadeTransition(Duration.millis(75), feltet);
    blinking.setFromValue(1.0);
    blinking.setToValue(0.1);
    blinking.setAutoReverse(true);
    blinking.setCycleCount(20);

    int deltaX = (-Settinger.BRETT_BREDDE);
    int deltaY = 0;

    TranslateTransition utBrikkeBevegelse = new TranslateTransition(
                                                  Duration.millis(Settinger.ANIMER_TREKK_TID),
                                                  utslattBrikkeIV);
    utBrikkeBevegelse.setToX(deltaX);
    utBrikkeBevegelse.setToY(deltaY);

    SequentialTransition st = new SequentialTransition();
    st.getChildren().addAll(blinking, utBrikkeBevegelse);
    st.setOnFinished(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent e){
        SP.getChildren().remove(utslattBrikkeIV);
        if(nesteAuto){ autoTrekk(); }
      }
    });
    st.play();
  }

  public void animerBondeForvandling(int x, int y, int nesteHandling, boolean nesteAuto){
    StackPane feltetSP = feltene[x][y];
    ImageView iv = (ImageView)feltetSP.getChildren().get(2);
    Rectangle bakgrunn = (Rectangle)feltetSP.getChildren().get(0);
    bakgrunn.setFill(Color.ORANGE);
    Rectangle feltet = (Rectangle)feltetSP.getChildren().get(1);
    FadeTransition blinking = new FadeTransition(Duration.millis(75), feltet);
    blinking.setFromValue(1.0);
    blinking.setToValue(0.1);
    blinking.setAutoReverse(true);
    blinking.setCycleCount(20);
    blinking.setOnFinished(new EventHandler<ActionEvent>(){
      @Override
      public void handle(ActionEvent e){
        bakgrunn.setFill(Color.RED);
        feltetSP.getChildren().remove(iv);
        if(hviteBrikkerIV.contains(iv)){
          hviteBrikkerIV.remove(iv);
          UIHvitDronning nyDronning = new UIHvitDronning(UIMaster.this, x, y);
          ImageView nyDronningIV = nyDronning.bildeViser();
          feltetSP.getChildren().add(nyDronningIV);
          hviteBrikkerIV.add(nyDronningIV);
        }
        else{
          svarteBrikkerIV.remove(iv);
          UISvartDronning nyDronning = new UISvartDronning(UIMaster.this, x, y);
          ImageView nyDronningIV = nyDronning.bildeViser();
          feltetSP.getChildren().add(nyDronningIV);
          svarteBrikkerIV.add(nyDronningIV);
        }
        if(nesteHandling == 0){ autoTrekk(); }
        else{ animerUtslagAvBrikke(x, y, nesteAuto); }
      }
    });
    blinking.play();
  }

  public void leggInnBrikke(int farge, int brikkeTypeNr, int feltX, int feltY){
    UIBrikke nyBrikke = null;
    if(farge == 0){
      switch(brikkeTypeNr){
        case 1: nyBrikke = new UIHvitBonde(this, feltX, feltY);
                break;
        case 2: nyBrikke = new UIHvitSpringer(this, feltX, feltY);
                break;
        case 3: nyBrikke = new UIHvitLoper(this, feltX, feltY);
                break;
        case 4: nyBrikke = new UIHvitTaarn(this, feltX, feltY);
                break;
        case 5: nyBrikke = new UIHvitDronning(this, feltX, feltY);
                break;
        case 6: nyBrikke = new UIHvitKonge(this, feltX, feltY);
                break;
      }
    }
    else{
      switch(brikkeTypeNr){
        case 1: nyBrikke = new UISvartBonde(this, feltX, feltY);
                break;
        case 2: nyBrikke = new UISvartSpringer(this, feltX, feltY);
                break;
        case 3: nyBrikke = new UISvartLoper(this, feltX, feltY);
                break;
        case 4: nyBrikke = new UISvartTaarn(this, feltX, feltY);
                break;
        case 5: nyBrikke = new UISvartDronning(this, feltX, feltY);
                break;
        case 6: nyBrikke = new UISvartKonge(this, feltX, feltY);
                break;
      }
    }
    ImageView iv = nyBrikke.bildeViser();
    feltene[feltX][feltY].getChildren().add(iv);
    if(farge == 0){ hviteBrikkerIV.add(iv); }
    else{ svarteBrikkerIV.add(iv); }
  }

  private void settHvitDisabled(boolean dvaleStatus){
    for(ImageView iv: hviteBrikkerIV){ iv.setDisable(dvaleStatus); }
  }

  private void settSvartDisabled(boolean dvaleStatus){
    for(ImageView iv: svarteBrikkerIV){ iv.setDisable(dvaleStatus); }
  }
} // klasseslutt
