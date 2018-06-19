public class UISvartBonde extends UISvartBrikke{

  static int bondeTeller = 0;
  private int serieNr;
  public UISvartBonde(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    serieNr = bondeTeller++;
    verdi = 1;
    bildeX = Settinger.BRIKKER_TEGNING_BONDE_X;
    bredde = Settinger.BRIKKER_TEGNING_BONDE_BREDDE;
  }
}
