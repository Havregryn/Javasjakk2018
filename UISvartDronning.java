public class UISvartDronning extends UISvartBrikke{

  public UISvartDronning(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    verdi = 10;
    bildeX = Settinger.BRIKKER_TEGNING_DRONNING_X;
    bredde = Settinger.BRIKKER_TEGNING_DRONNING_BREDDE;
  }
}
