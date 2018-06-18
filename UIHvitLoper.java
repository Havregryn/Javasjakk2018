public class UIHvitLoper extends UIHvitBrikke{
  static int taarnTeller = 0;
  private int serieNr;
  public UIHvitLoper(int xPos, int yPos){
    super(xPos, yPos);
    serieNr = taarnTeller++;
    verdi = 4;
    bildeX = Settinger.BRIKKER_TEGNING_LOPER_X;
    bredde = Settinger.BRIKKER_TEGNING_LOPER_BREDDE;
  }
}
