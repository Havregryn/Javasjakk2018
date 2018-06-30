/**
  Instans av denne klassen representerer et parti med sjakk.
*/

class Parti{

  private boolean forsteTrekkHvit;
  private int nesteTrekkFarge;
  private UIMaster uiMaster;
  private Stilling stilling;
  private boolean hvitErAutomatisk, svartErAutomatisk;

  public Parti(int[][][] brettet, boolean forsteTrekkHvit, UIMaster uiMaster){
    this.forsteTrekkHvit = forsteTrekkHvit;
    if(forsteTrekkHvit){ nesteTrekkFarge = 0; }
    else{ nesteTrekkFarge = 1; }
    this.uiMaster = uiMaster;
    this.stilling = new Stilling(brettet, nesteTrekkFarge, this, uiMaster);
    stilling.settOppBrikkerUI();
  }

  public Stilling hentStilling(){ return stilling; }

  /**
  Metode som tar imot manuelle trekk fra UI. Returnerer -1 dersom ulovlig, 0 dersom
  lovlig, og 1 dersom slår ut brikke.

  */

}
