import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

abstract class UIBrikke{

  protected boolean erHvit;
  protected int verdi, xPos, yPos, bildeX = 0, bildeY = 0, bredde = 0, hoyde = 0;
  protected String navn;


  public UIBrikke(int xPos, int yPos){
    this.xPos = xPos;
    this.yPos = yPos;
    hoyde = Settinger.BRIKKER_TEGNING_HOYDE;
  }

  public ImageView bildeViser(){
    Image bilde = new Image("brikker.png");
    ImageView iv = new FlyttbarImageView(bilde);
    iv.setFitWidth((int)(Settinger.RUTE_BREDDE * 0.8));
    iv.setPreserveRatio(true);
    Rectangle2D utsnitt = new Rectangle2D(bildeX, bildeY, bredde, hoyde);
    iv.setViewport(utsnitt);
    return iv;
  }

  private class FlyttbarImageView extends ImageView{
    double musFraX, musFraY, musTilX, musTilY;
    FlyttbarImageView(Image bilde){
      super(bilde);

      setOnMousePressed(event ->{
        this.getParent().toFront();
        musFraX = event.getSceneX();
        musFraY = event.getSceneY();
      });

      setOnMouseDragged(event ->{
        double deltaX = event.getSceneX() - musFraX;
        double deltaY = event.getSceneY() - musFraY;
        setTranslateX(deltaX);
        setTranslateY(deltaY);
      });

      setOnMouseReleased(event ->{
        musTilX = event.getSceneX();
        musTilY = event.getSceneY();
        JavaSjakk.brikkeFlyttetMedMus(this, musFraX, musFraY, musTilX, musTilY);

      });
    }
  }
}
