abstract class UIHvitBrikke extends UIBrikke{
  public UIHvitBrikke(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    super.erHvit = true;
    super.bildeY = Settinger.BRIKKER_TEGNING_HVIT_Y;
  }
}
