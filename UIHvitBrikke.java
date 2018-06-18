abstract class UIHvitBrikke extends UIBrikke{
  public UIHvitBrikke(int xPos, int yPos){
    super(xPos, yPos);
    super.erHvit = true;
    super.bildeY = Settinger.BRIKKER_TEGNING_HVIT_Y;
  }
}
