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

import java.util.ArrayList;

public class UIMaster extends Application{
  private static StackPane[][] feltene = new StackPane[8][8];
  // X - akse: trekk fra 1. Y-akse: 0 = A, 7 = H

  private static Text statusFelt, hoyreTekstFelt;
  private static Administrator administrator;
  private static Stilling stilling;
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
    statusFelt.setFont(new Font(20));
    statusFelt.setTextAlignment(TextAlignment.CENTER);

    hoyreTekstFelt = new Text("Høyre");
    hoyreTekstFelt.setWrappingWidth(Settinger.RUTE_BREDDE * 6);
    hoyreTekstFelt.setFont(new Font(16));




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

    administrator = new Administrator(this);
    stilling = administrator.hentStilling();
    hoyreTekstFelt.setText(stilling.toString());
    //hoyreTekstFelt.setText(stilling.hentEvalStreng());
    //TestKlasse tk = new TestKlasse(this);

    // Test av brikkeflytt-animasjon:
    //animerUtslagAvBrikke(3, 0, 3, 6);
    //animerFlyttAvBrikke(6, 0, 5, 2);



  }

  public static void brikkeFlyttetMedMus(ImageView iv, double fraX, double fraY, double tilX, double tilY){
    int offset = Settinger.RUTE_BREDDE / 2;
    int fraFeltX = (int)((fraX - offset)/Settinger.RUTE_BREDDE);
    int fraFeltY = 7 - (int)((fraY - offset)/Settinger.RUTE_BREDDE);
    int tilFeltX = (int)((tilX - offset)/Settinger.RUTE_BREDDE);
    int tilFeltY = 7 - (int)((tilY - offset)/Settinger.RUTE_BREDDE);
    String s = "Fra: " + fraFeltX + ", " + fraFeltY + " til: " + tilFeltX + ", " + tilFeltY;
    //statusFelt.setText(s);
    //statusFelt.setText("Ant trekk: " + Integer.toString(stilling.hentTrekkListe().size()));

    //hoyreTekstFelt.setText(stilling.hentEvalStreng());

    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];

    int resultat = stilling.manueltTrekk(fraFeltX, fraFeltY, tilFeltX, tilFeltY);
    hoyreTekstFelt.setText(stilling.toString());
    //hoyreTekstFelt.setText(stilling.hentEvalStreng());
    //statusFelt.setText("resultat: " + resultat);
    if(resultat == -1){
      animerReturAvBrikke(iv);
    }
    else if(resultat == 0){
      fraSP.getChildren().remove(iv);
      iv.setTranslateX(0);
      iv.setTranslateY(0);
      tilSP.getChildren().add(iv);
    }
    else if(resultat == 1){
      animerUtslagAvBrikke(fraFeltX, fraFeltY, tilFeltX, tilFeltY);
      /*
      try{ Thread.sleep(Settinger.ANIMER_TREKK_TID); }
      catch(InterruptedException e){}
      */
      fraSP.getChildren().remove(iv);
      iv.setTranslateX(0);
      iv.setTranslateY(0);
      tilSP.getChildren().add(iv);
    }


  }

  public static void animerReturAvBrikke(ImageView iv){
    TranslateTransition overgang = new TranslateTransition(Duration.millis(Settinger.ANIMER_TREKK_TID), iv);
    overgang.setToX(0);
    overgang.setToY(0);
    overgang.play();
  }

  public static void animerFlyttAvBrikke(int fraFeltX, int fraFeltY, int tilFeltX, int tilFeltY){
    ImageView iv;
    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];
    iv = (ImageView)fraSP.getChildren().get(1);
    iv.getParent().toFront();
    int deltaX = (tilFeltX - fraFeltX) * Settinger.RUTE_BREDDE;
    int deltaY = (fraFeltY - tilFeltY) * Settinger.RUTE_BREDDE;
    TranslateTransition brikkeBevegelse = new TranslateTransition(Duration.millis(Settinger.ANIMER_TREKK_TID), iv);
    brikkeBevegelse.setToX(deltaX);
    brikkeBevegelse.setToY(deltaY);
    brikkeBevegelse.play();
  }

  public static void animerUtslagAvBrikke(int fraFeltX, int fraFeltY, int tilFeltX, int tilFeltY){
    ImageView hovedBrikkeIV;
    ImageView utslattBrikkeIV;
    Rectangle feltet;
    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];
    hovedBrikkeIV = (ImageView)fraSP.getChildren().get(2);
    utslattBrikkeIV = (ImageView)tilSP.getChildren().get(2);
    hviteBrikkerIV.remove(utslattBrikkeIV);
    svarteBrikkerIV.remove(utslattBrikkeIV);
    feltet = (Rectangle)tilSP.getChildren().get(1);
    hovedBrikkeIV.getParent().toFront();
    int deltaX = (tilFeltX - fraFeltX) * Settinger.RUTE_BREDDE;
    int deltaY = (fraFeltY - tilFeltY) * Settinger.RUTE_BREDDE;
    // Lager en Sekvensiell transition med tre transitions: (flytt, blink, fjern)
    TranslateTransition brikkeBevegelse = new TranslateTransition(
                                                Duration.millis(Settinger.ANIMER_TREKK_TID),
                                                hovedBrikkeIV);
    brikkeBevegelse.setToX(deltaX);
    brikkeBevegelse.setToY(deltaY);

    FadeTransition blinking = new FadeTransition(Duration.millis(100), feltet);
    blinking.setFromValue(1.0);
    blinking.setToValue(0.1);
    blinking.setAutoReverse(true);
    blinking.setCycleCount(10);

    deltaX = (-Settinger.BRETT_BREDDE);
    deltaY = 0;

    TranslateTransition utBrikkeBevegelse = new TranslateTransition(
                                                  Duration.millis(Settinger.ANIMER_TREKK_TID),
                                                  utslattBrikkeIV);
    utBrikkeBevegelse.setToX(deltaX);
    utBrikkeBevegelse.setToY(deltaY);

    SequentialTransition st = new SequentialTransition();
    st.getChildren().addAll(blinking, utBrikkeBevegelse);
    st.play();
  }

  public void leggInnBrikke(int farge, int brikkeTypeNr, int feltX, int feltY){
    statusFelt.setText("Legger inn brikke");
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
