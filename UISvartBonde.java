public class UISvartBonde extends UISvartBrikke{

  static int bondeTeller = 0;
  private int serieNr;
  public UISvartBonde(int xPos, int yPos){
    super(xPos, yPos);
    serieNr = bondeTeller++;
    verdi = 1;
    bildeX = Settinger.BRIKKER_TEGNING_BONDE_X;
    bredde = Settinger.BRIKKER_TEGNING_BONDE_BREDDE;
  }
}
