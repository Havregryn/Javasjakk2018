public class UISvartKonge extends UISvartBrikke{

  public UISvartKonge(UIMaster ui, int xPos, int yPos){
    super(ui, xPos, yPos);
    verdi = 10;
    bildeX = Settinger.BRIKKER_TEGNING_KONGE_X;
    bredde = Settinger.BRIKKER_TEGNING_KONGE_BREDDE;
  }
}
