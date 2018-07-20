/**
  Instans av denne klassen representerer et parti med sjakk.
*/

class Parti{

  private boolean forsteTrekkHvit;
  private int nesteTrekkFarge, trekkNr = 0;
  private UIMaster uiMaster;
  private StillingReell stilling;
  private boolean[] automatisk;
  private int dybde; // antall nivåer i tre med mulige trekk.


  public Parti(int[][][] brettet,
               boolean forsteTrekkHvit,
               UIMaster uiMaster,
               boolean hvitErAuto,
               boolean svartErAuto,
               int dybde){
    this.forsteTrekkHvit = forsteTrekkHvit;
    automatisk = new boolean[2];
    automatisk[0] = hvitErAuto;
    automatisk[1] = svartErAuto;
    if(forsteTrekkHvit){ nesteTrekkFarge = 0; } else{ nesteTrekkFarge = 1; }
    this.uiMaster = uiMaster;
    if(dybde < 1){ dybde = 1; }
    this.dybde = dybde;
    this.stilling = new StillingReell(brettet, new StillingStatus(), nesteTrekkFarge, dybde, this, uiMaster);
    stilling.settOppBrikkerUI();
  }



  public StillingReell hentStilling(){ return stilling; }

  public boolean hentAutomatisk(int farge){ return automatisk[farge]; }

}
