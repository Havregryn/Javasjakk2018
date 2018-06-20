class Parti{

  private boolean forsteTrekkHvit;
  private short[][][] brettet;
  private UIMaster uiMaster;

  public Parti(short[][][] brettet, boolean forsteTrekkHvit, UIMaster uiMaster){
    this.brettet = brettet;
    this.forsteTrekkHvit = forsteTrekkHvit;
    this.uiMaster = uiMaster;
    settOppBrikkerUI();
  }

  private void settOppBrikkerUI(){
    int hvitPaaFelt, svartPaaFelt;
    for(int x = 0; x < 8; x++){
      for(int y = 0; y < 8; y++){
        hvitPaaFelt = brettet[0][x][y];
        svartPaaFelt = brettet[1][x][y];
        if(hvitPaaFelt != 0){ uiMaster.leggInnBrikke(0, hvitPaaFelt, x, y); }
        else if(svartPaaFelt != 0){ uiMaster.leggInnBrikke(1, svartPaaFelt, x, y); }
      }
    }
  }

}
