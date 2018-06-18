public class UIHvitTaarn extends UIHvitBrikke{

  static int taarnTeller = 0;
  private int serieNr;
  public UIHvitTaarn(int xPos, int yPos){
    super(xPos, yPos);
    serieNr = taarnTeller++;
    verdi = 4;
    bildeX = Settinger.BRIKKER_TEGNING_TAARN_X;
    bredde = Settinger.BRIKKER_TEGNING_TAARN_BREDDE;
  }

}
