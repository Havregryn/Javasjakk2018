public class UIHvitTaarn extends UIHvitBrikke{

  static int taarnTeller = 0;
  private int serieNr;
  public UIHvitTaarn(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    serieNr = taarnTeller++;
    verdi = 4;
    bildeX = Settinger.BRIKKER_TEGNING_TAARN_X;
    bredde = Settinger.BRIKKER_TEGNING_TAARN_BREDDE;
  }

}
