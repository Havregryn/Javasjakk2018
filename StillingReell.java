/**
Instans av Stilling representerer stilling som vises på skjermen. Det foregår direkte
toveis kommunikasjon mellom uiMaster og Stilling instans.
*/


class StillingReell extends Stilling{

  private UIMaster uiMaster;
  private Parti parti;

  StillingReell(int[][][] brettet, StillingStatus status, int nesteTrekkFarge, int dybde, Parti parti, UIMaster uiMaster){
    super(brettet, status, nesteTrekkFarge, dybde);
    initierRokadeVariabler();
    this.parti = parti;
    this.uiMaster = uiMaster;
    opprettStillingImagTre();
  }

  // reell/imag: KUN reell!
  public void settOppBrikkerUI(){
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

  // reell/imag: KUN UIMaster.
  /**
  Metode som evaluerer et mulig trekk mottat fra UIMaster og utfører dette trekket
  dersom det ligger i mulige-trekk listen. Metdoen returner et resultat
  som angir om dette er et gyldig trekk, og isåfall type trekk.
  Dersom trekket er gyldig utføres det, deretter resettes alle stillings variabler,
  farge byttes og stilling evalueres.
  Metoden benyttes kun av UIMaster.
  */
    public int manueltTrekk(int fraX, int fraY, int tilX, int tilY){
      int trekkType = -1;

      for(Trekk trekket : muligeTrekk){
        if(fraX == trekket.hentFraX() && fraY == trekket.hentFraY() &&
           tilX == trekket.hentTilX() && tilY == trekket.hentTilY()){
             trekkType = trekket.hentTrekkType();
             super.utforTrekk(trekket);
             break;
           }
      }
      if(trekkType != -1){ super.byttTrekkFarge(); }
      return trekkType;
    }

    // reell/imag: Brukes KUN av UiMaster:
    public Trekk autoTrekk(){
      // Denne metoden skal utføre trekket med høyest ranking i mulige trekk listen.
      // Dersom flere med samme ranking gjøres et tilfeldig valg av trekk.
      // Dersom ingen mulige trekk
      Trekk trekket = null;
      if(muligeTrekk.size() > 0){
        trekket = muligeTrekk.get(tilfeldig.nextInt(muligeTrekk.size()));
      }
      super.utforTrekk(trekket);
      byttTrekkFarge();
      return trekket;
    }

    public void opprettStillingImagTre(){
      for(Trekk t : muligeTrekk){
        t.settStillingEtterTrekk(new StillingImag(kopiAvBrettet(), t, status.kopi(), this, dybde));
      }
    }

}
