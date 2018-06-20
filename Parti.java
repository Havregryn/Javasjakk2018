class Parti{

  private boolean forsteTrekkHvit;
  private short nesteTrekkFarge;
  private UIMaster uiMaster;
  private Stilling stilling;

  public Parti(short[][][] brettet, boolean forsteTrekkHvit, UIMaster uiMaster){
    this.forsteTrekkHvit = forsteTrekkHvit;
    if(forsteTrekkHvit){ nesteTrekkFarge = 0; }
    else{ nesteTrekkFarge = 1; }
    this.uiMaster = uiMaster;
    this.stilling = new Stilling(brettet, nesteTrekkFarge, this);
    settOppBrikkerUI();
  }

  /**
  Metode som tar imot manuelle trekk fra UI. Returnerer -1 dersom ulovlig, 0 dersom
  lovlig, og 1 dersom slår ut brikke.

  */
  public int manueltTrekk(int fraX, int fraY, int tilX, int tilY){
    return stilling.manueltTrekk(fraX, fraY, tilX, tilY);
  }

  private void settOppBrikkerUI(){
    short hvitPaaFelt, svartPaaFelt;
    for(int x = 0; x < 8; x++){
      for(int y = 0; y < 8; y++){
        hvitPaaFelt = stilling.hentFelt(0, x, y);
        svartPaaFelt = stilling.hentFelt(1, x, y);
        if(hvitPaaFelt != 0){ uiMaster.leggInnBrikke(0, hvitPaaFelt, x, y); }
        else if(svartPaaFelt != 0){ uiMaster.leggInnBrikke(1, svartPaaFelt, x, y); }
      }
    }
  }

}
