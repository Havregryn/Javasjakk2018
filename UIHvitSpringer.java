public class UIHvitSpringer extends UIHvitBrikke{

  static int springerTeller = 0;
  private int serieNr;
  public UIHvitSpringer(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    serieNr = springerTeller++;
    verdi = 4;
    bildeX = Settinger.BRIKKER_TEGNING_SPRINGER_X;
    bredde = Settinger.BRIKKER_TEGNING_SPRINGER_BREDDE;
  }
}
