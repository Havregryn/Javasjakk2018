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
import javafx.util.Duration;





public class JavaSjakk extends Application{

  private static StackPane[][] feltene = new StackPane[8][8];
  // X - akse: trekk fra 1. Y-akse: 0 = A, 7 = H

  private static Text statusFelt;

  public static void main(String[] args){
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
            Rectangle r = new Rectangle(Settinger.RUTE_BREDDE,
                                        Settinger.RUTE_BREDDE);
            if(hvittFelt){ r.setFill(Color.WHITE); }
            else{ r.setFill(Color.GREY); }
            StackPane stack = new StackPane();
            feltene[x - 1][8 - y] = stack;
            stack.getChildren().add(r);
            brettGP.add(stack, x, y);
            hvittFelt = !hvittFelt;
          }

        }
      }
      hvittFelt = !hvittFelt;
    }
    // Legger inn brikker, kun for testing:
    for(int x = 0; x < 8; x++){
      UIHvitBonde hb = new UIHvitBonde(0, 0);
      feltene[x][1].getChildren().add(hb.bildeViser());

      feltene[x][6].getChildren().add(new UISvartBonde(0, 0).bildeViser());
    }
    feltene[0][0].getChildren().add(new UIHvitTaarn(0, 0).bildeViser());
    feltene[1][0].getChildren().add(new UIHvitSpringer(0, 0).bildeViser());
    feltene[2][0].getChildren().add(new UIHvitLoper(0, 0).bildeViser());
    feltene[3][0].getChildren().add(new UIHvitDronning(0, 0).bildeViser());
    feltene[4][0].getChildren().add(new UIHvitKonge(0, 0).bildeViser());
    feltene[5][0].getChildren().add(new UIHvitLoper(0, 0).bildeViser());
    feltene[6][0].getChildren().add(new UIHvitSpringer(0, 0).bildeViser());
    feltene[7][0].getChildren().add(new UIHvitTaarn(0, 0).bildeViser());

    feltene[0][7].getChildren().add(new UISvartTaarn(0, 0).bildeViser());
    feltene[1][7].getChildren().add(new UISvartSpringer(0, 0).bildeViser());
    feltene[2][7].getChildren().add(new UISvartLoper(0, 0).bildeViser());
    feltene[3][7].getChildren().add(new UISvartDronning(0, 0).bildeViser());
    feltene[4][7].getChildren().add(new UISvartKonge(0, 0).bildeViser());
    feltene[5][7].getChildren().add(new UISvartLoper(0, 0).bildeViser());
    feltene[6][7].getChildren().add(new UISvartSpringer(0, 0).bildeViser());
    feltene[7][7].getChildren().add(new UISvartTaarn(0, 0).bildeViser());

    statusFelt = new Text("Tekstfeltet");
    statusFelt.setWrappingWidth(Settinger.BRETT_BREDDE);
    statusFelt.setY(Settinger.BRETT_BREDDE + 100);
    statusFelt.setFont(new Font(20));
    statusFelt.setTextAlignment(TextAlignment.CENTER);





    Pane hovedKulisse = new Pane();
    hovedKulisse.getChildren().add(brettGP);
    hovedKulisse.getChildren().add(statusFelt);


    Scene scene = new Scene(hovedKulisse);
    teater.setScene(scene);
    teater.setTitle("Java-sjakk");
    teater.show();


  }

  public static void brikkeFlyttetMedMus(ImageView iv, double fraX, double fraY, double tilX, double tilY){
    int offset = Settinger.RUTE_BREDDE / 2;
    int fraFeltX = (int)((fraX - offset)/Settinger.RUTE_BREDDE);
    int fraFeltY = 7 - (int)((fraY - offset)/Settinger.RUTE_BREDDE);
    int tilFeltX = (int)((tilX - offset)/Settinger.RUTE_BREDDE);
    int tilFeltY = 7 - (int)((tilY - offset)/Settinger.RUTE_BREDDE);
    String s = "Fra: " + fraFeltX + ", " + fraFeltY + " til: " + tilFeltX + ", " + tilFeltY;
    statusFelt.setText(s);

    StackPane fraSP = feltene[fraFeltX][fraFeltY];
    StackPane tilSP = feltene[tilFeltX][tilFeltY];

    /*
    fraSP.getChildren().remove(iv);
    iv.setTranslateX(0);
    iv.setTranslateY(0);
    tilSP.getChildren().add(iv);
    */

    animerFlyttAvBrikke(iv, tilX, tilY, fraX, fraY);
  }

  public static void animerFlyttAvBrikke(ImageView iv, double fraX, double fraY, double tilX, double tilY){
    TranslateTransition overgang = new TranslateTransition(Duration.millis(Settinger.ANIMER_TREKK_TID), iv);
    overgang.setToX(0);
    overgang.setToY(0);
    overgang.play();

  }



  private static void registrerFlytt(int fraFeltX, int fraFeltY, int tilFeltX, int tilFeltY){

  }

}
