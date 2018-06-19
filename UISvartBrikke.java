abstract class UISvartBrikke extends UIBrikke{
  public UISvartBrikke(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    super.erHvit = false;
    super.bildeY = Settinger.BRIKKER_TEGNING_SVART_Y;
  }
}
