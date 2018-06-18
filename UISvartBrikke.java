abstract class UISvartBrikke extends UIBrikke{
  public UISvartBrikke(int xPos, int yPos){
    super(xPos, yPos);
    super.erHvit = false;
    super.bildeY = Settinger.BRIKKER_TEGNING_SVART_Y;
  }
}
