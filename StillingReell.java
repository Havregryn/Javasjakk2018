/**
Instans av Stilling representerer stilling som vises på skjermen. Det foregår direkte
toveis kommunikasjon mellom uiMaster og Stilling instans.
*/


class StillingReell extends Stilling{

  private UIMaster uiMaster;
  private Parti parti;

  StillingReell(int[][][] brettet, StillingStatus status, int nesteTrekkFarge, int antStillinger, Parti parti, UIMaster uiMaster){
    super(brettet, status, nesteTrekkFarge, antStillinger);
    this.parti = parti;
    this.uiMaster = uiMaster;
    initierRokadeVariabler();
    initierBrikkeSum();
    Evaluator.finnMuligeTrekk(this);
    opprettStillingImagTre();
    oppdaterDypRating();
    oppdaterGrunnRating();
    parti.startNyRunde();
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

  // KUN REELL: Beregnes KUN ved opprettelse av reell stilling.
  private void initierBrikkeSum(){
    // UFERDIG
    for(int x = 0; x <= 7; x++){
      for(int y = 0; y <= 7; y++){
        if(brettet[nesteTrekkFarge][x][y] != 0){
          status.brikkeSum[nesteTrekkFarge] += Settinger.BRIKKEVERDIER[brettet[nesteTrekkFarge][x][y]];
        }
        if(brettet[forrigeTrekkFarge][x][y] != 0){
          status.brikkeSum[forrigeTrekkFarge] += Settinger.BRIKKEVERDIER[brettet[forrigeTrekkFarge][x][y]];
        }
      }
    }
  }

  // KUN reell: Finner ut om rokade er mulig fra start:
  public void initierRokadeVariabler(){
    // Rokade er kun mulig dersom konge og tårn er satt opp på normal startplass:
    status.kongeErUflyttet[0] = (brettet[0][4][0] == 6);
    status.aTaarnErUflyttet[0] = (brettet[0][0][0] == 4);
    status.hTaarnErUflyttet[0] = (brettet[0][7][0] == 4);
    status.kongeErUflyttet[1] = (brettet[1][4][7] == 6);
    status.aTaarnErUflyttet[1] = (brettet[1][0][7] == 4);
    status.hTaarnErUflyttet[1] = (brettet[1][7][7] == 4);
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
             bliTilBarn(trekket.hentStillingEtterTrekk());
             break;
           }
      }
      return trekkType;
    }

    // reell/imag: Brukes KUN av UiMaster:
    public Trekk autoTrekk(){
      // Denne metoden skal utføre trekket med høyest ranking i mulige trekk listen.
      // Dersom flere med samme ranking gjøres et tilfeldig valg av trekk.
      // Dersom ingen mulige trekk
      Trekk trekket = null, andreValgTrekk = null;
      double vinnerTrekkRating = -99999999, andreValgTrekkRating = -99999999;
      double[] trekkRating;
       for(Trekk t : muligeTrekk){
         trekkRating = t.hentStillingEtterTrekk().hentDypRating();
         if((nesteTrekkFarge == 0 && trekkRating[0] > vinnerTrekkRating) ||
            (nesteTrekkFarge == 1 && trekkRating[1] > vinnerTrekkRating)){
              if(trekket != null){
                andreValgTrekk = trekket;
                andreValgTrekkRating = andreValgTrekk.hentStillingEtterTrekk().hentDypRating()[nesteTrekkFarge];
              }
              trekket = t;
              vinnerTrekkRating = trekkRating[nesteTrekkFarge];
            }
       }
       if(andreValgTrekk != null && tilfeldig.nextInt(4) == 3 &&
          Math.abs(vinnerTrekkRating - andreValgTrekkRating) <
          0.5 * vinnerTrekkRating){
         trekket = andreValgTrekk;
       }
      bliTilBarn(trekket.hentStillingEtterTrekk());
      return trekket;
    }

    // Metode som endrer reell stilling slik at den blir som sitt barn og overtar
    // barnets plass i StillingImag-treet.
    private void bliTilBarn(StillingImag stillingBarn ){
      // Overtar alle variable fra barnet:
      status = stillingBarn.hentStatus();
      reellTrekkNr = stillingBarn.hentTrekkNr();
      forrigeTrekkFarge = nesteTrekkFarge;
      nesteTrekkFarge = 1 - nesteTrekkFarge;
      brettet = stillingBarn.hentBrett();
      for(Trekk t : muligeTrekk){
        Stilling s = t.hentStillingEtterTrekk();
        if(s != stillingBarn){
          if(s != null){ s.avsluttInstans(); }
          t.settStillingEtterTrekk(null);
        }
      }
      muligeTrekk = stillingBarn.hentTrekkListe();
      System.gc();
      grunnRating = stillingBarn.hentGrunnrating();
      dypRating = stillingBarn.hentDypRating();
      grunnMuligeTrekk = stillingBarn.hentGrunnMuligeTrekk();
      grunnTrusselBonus = stillingBarn.hentGrunnTrusselBonus();

      c_d_erTruet = stillingBarn.hent_c_d_truet();
      f_g_erTruet = stillingBarn.hent_f_g_truet();
      while(instansTeller < antStillinger && muligeTrekk.size() > 0){
          leggTilEnGenerasjon();
      }
      oppdaterDypRating();
      if(nesteTrekkFarge == 0){ parti.startNyRunde(); }
    }

    private void leggTilEnGenerasjon(){

      try{
        for(int i = 0; i < muligeTrekk.size() && instansTeller < antStillinger; i++){
          Trekk t = muligeTrekk.get(i);
          StillingImag s = t.hentStillingEtterTrekk();
          if(s == null){
            StillingImag nyStIm = new StillingImag(kopiAvBrettet(), t, status.kopi(), this, antStillinger, reellTrekkNr + 1);
            Thread.sleep(1);
            if(!Evaluator.trekkGodkjent(this, nyStIm)){
              muligeTrekk.remove(t);
              grunnMuligeTrekk[t.hentFarge()]--;
              i--;
            }
            else{
              t.settStillingEtterTrekk(nyStIm);
              if(reellTrekkNr + 1 > lengstFramTrekkNr){ lengstFramTrekkNr = reellTrekkNr + 1; }
            }
          }
          else{
            s.leggTilEnGenerasjon();
          }
        }
      }
      catch(InterruptedException e){
        System.exit(0);
      }


    }

    public void opprettStillingImagTre(){
      int testTeller = 0;
      evalStreng += "\nREELL: Oppretter imag St:\n";
      try{
        for(int i = 0; i < muligeTrekk.size(); i++){
          Trekk t = muligeTrekk.get(i);
          StillingImag s = new StillingImag(kopiAvBrettet(), t, status.kopi(), this, antStillinger, reellTrekkNr + 1);
          Thread.sleep(1);
          if(!Evaluator.trekkGodkjent(this, s)){
            muligeTrekk.remove(t);
            grunnMuligeTrekk[t.hentFarge()]--;
            i--;
          }
          else{
            t.settStillingEtterTrekk(s);
            if(reellTrekkNr + 1 > lengstFramTrekkNr){ lengstFramTrekkNr = reellTrekkNr + 1; }
            evalStreng += s.toString();
            testTeller ++;
          }
        }
        // NY tre-byggingsmetode som bygger en layer om gangen inntil max antall instanser:
        while(instansTeller < antStillinger && muligeTrekk.size() > 0){
          for(Trekk t : muligeTrekk){
            if(instansTeller < antStillinger){
              t.hentStillingEtterTrekk().leggTilEnGenerasjon();
            }
          }
          Thread.sleep(1);
        }
      }
      catch(InterruptedException e){
        System.exit(0);
      }

      evalStreng += ("\nAnt imagSt lagt til: " + testTeller + "\n");
    }
}
