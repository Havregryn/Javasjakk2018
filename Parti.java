/**
  Instans av denne klassen representerer et parti med sjakk.
*/

class Parti{

  private boolean forsteTrekkHvit;
  private int nesteTrekkFarge;
  private UIMaster uiMaster;
  private Stilling stilling;
  private boolean[] automatisk;


  public Parti(int[][][] brettet, boolean forsteTrekkHvit, UIMaster uiMaster, boolean hvitErAuto, boolean svartErAuto){
    this.forsteTrekkHvit = forsteTrekkHvit;
    automatisk = new boolean[2];
    automatisk[0] = hvitErAuto;
    automatisk[1] = svartErAuto;
    if(forsteTrekkHvit){ nesteTrekkFarge = 0; }
    else{ nesteTrekkFarge = 1; }
    this.uiMaster = uiMaster;
    this.stilling = new Stilling(brettet, nesteTrekkFarge, this, uiMaster);
    stilling.settOppBrikkerUI();
  }



  public Stilling hentStilling(){ return stilling; }

}
