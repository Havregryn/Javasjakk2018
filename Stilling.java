/**
Instans av Stilling representerer stilling som vises på skjermen. Det foregår direkte
toveis kommunikasjon mellom uiMaster og Stilling instans.
*/
import java.util.ArrayList;

class Stilling{
  private UIMaster uiMaster;
  private int nesteTrekkFarge;
  private int forrigeTrekkFarge;
  private int[][][] brettet;
  private Parti parti;
  private ArrayList<Trekk> muligeTrekk;
  private int grunnRating;
  private int brikkeSum[];
  private int grunnMuligeTrekk[];
  private double[] grunnTrusselBonus = {0.0, 0.0};
  private String evalStreng = "";

  private boolean[] kongeErUflyttet = {true, true};
  private boolean[] aTaarnErUflyttet = {true, true};
  private boolean[] hTaarnErUflyttet = {true, true};

  public Stilling(int[][][] brettet, int nesteTrekkFarge, Parti parti, UIMaster uiMaster){
    this.brettet = brettet;
    this.nesteTrekkFarge = nesteTrekkFarge;
    if(nesteTrekkFarge == 0){ forrigeTrekkFarge = 1; }
    else{ forrigeTrekkFarge = 0; }
    this.parti = parti;
    this.uiMaster = uiMaster;
    muligeTrekk = new ArrayList<Trekk>(40);
    brikkeSum = new int[2];
    brikkeSum[0] = 0;
    brikkeSum[1] = 0;
    grunnMuligeTrekk = new int[2];
    grunnMuligeTrekk[0] = 0;
    grunnMuligeTrekk[1] = 0;
    grunnRating = 0;
    initierRokadeVariabler();
    initierBrikkeSum();
    evalStreng += Evaluator.grunnEvaluering(this);
    oppdaterGrunnRating();
  }

  private void initierBrikkeSum(){
    // UFERDIG
    for(int x = 0; x <= 7; x++){
      for(int y = 0; y <= 7; y++){
        if(brettet[nesteTrekkFarge][x][y] != 0){
          brikkeSum[nesteTrekkFarge] += Settinger.BRIKKEVERDIER[brettet[nesteTrekkFarge][x][y]];
        }
        if(brettet[forrigeTrekkFarge][x][y] != 0){
          brikkeSum[forrigeTrekkFarge] += Settinger.BRIKKEVERDIER[brettet[forrigeTrekkFarge][x][y]];
        }
      }
    }
  }

  public void initierRokadeVariabler(){
    // Rokade er kun mulig dersom konge og tårn er satt opp på normal startplass:
    kongeErUflyttet[0] = (brettet[0][4][0] == 6);
    aTaarnErUflyttet[0] = (brettet[0][0][0] == 4);
    hTaarnErUflyttet[0] = (brettet[0][7][0] == 4);
    kongeErUflyttet[1] = (brettet[1][4][7] == 6);
    aTaarnErUflyttet[1] = (brettet[1][0][7] == 4);
    hTaarnErUflyttet[1] = (brettet[1][7][7] == 4);

  }

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

  // TESTMETODE:
  public String hentEvalStreng(){ return evalStreng; }

  public int hentFelt(int farge, int x, int y){ return brettet[farge][x][y]; }

  public int[][][] hentBrett(){ return brettet; }

  public int hentNesteTrekkFarge(){ return nesteTrekkFarge; }

  public ArrayList<Trekk> hentTrekkListe(){ return muligeTrekk; }

  public void leggTilTrekk(int evFarge, int brikkeTypeNr, int fraX, int fraY, int tilX, int tilY){
    grunnMuligeTrekk[evFarge] += Settinger.TREKK_VERDI;
    if(evFarge == nesteTrekkFarge){
      muligeTrekk.add(new Trekk(brikkeTypeNr, fraX, fraY, tilX, tilY));
    }
  }



  public int manueltTrekk(int fraX, int fraY, int tilX, int tilY){
    int resultat = -1;
    for(Trekk trekk : muligeTrekk){
      if(fraX == trekk.hentFraX() &&
         fraY == trekk.hentFraY() &&
         tilX == trekk.hentTilX() &&
         tilY == trekk.hentTilY()){


           // Utfører trekket!
           brettet[nesteTrekkFarge][tilX][tilY] = brettet[nesteTrekkFarge][fraX][fraY];
           brettet[nesteTrekkFarge][fraX][fraY] = 0;

           boolean bondeForvandling = (trekk.hentBrikkeTypeNr() == 1 && tilY == (7 - nesteTrekkFarge * 7));
           // Sjekker om rokade:
           if(trekk.hentBrikkeTypeNr() == 6 && fraX - tilX == 2){
             brettet[nesteTrekkFarge][0][fraY] = 0;
             brettet[nesteTrekkFarge][3][fraY] = 4;
             resultat = 2; // Indikerer lang rokade med a Tårn.
           }
           else if(trekk.hentBrikkeTypeNr() == 6 && fraX - tilX == -2){
             brettet[nesteTrekkFarge][7][fraY] = 0;
             brettet[nesteTrekkFarge][5][fraY] = 4;
             resultat = 3; // indikerer kort rokade med h tårn.
           }
           else{
             // Ikke rokade, vanlig trekk (evt bondeforvandling):
             if(brettet[forrigeTrekkFarge][tilX][tilY] == 0){
               if(bondeForvandling){
                 brettet[nesteTrekkFarge][tilX][tilY] = 5;
                 brikkeSum[nesteTrekkFarge] -= Settinger.BRIKKEVERDIER[1];
                 brikkeSum[nesteTrekkFarge] += Settinger.BRIKKEVERDIER[5];
                 resultat = 4;
               }
               else { resultat = 0; }
             }
             else{
               // Utslag av brikke, evt med bondeForvandling:
               brikkeSum[forrigeTrekkFarge] -= Settinger.BRIKKEVERDIER[brettet[forrigeTrekkFarge][tilX][tilY]];
               brettet[forrigeTrekkFarge][tilX][tilY] = 0;
               if(bondeForvandling){
                 brettet[nesteTrekkFarge][tilX][tilY] = 5;
                 brikkeSum[nesteTrekkFarge] -= Settinger.BRIKKEVERDIER[1];
                 brikkeSum[nesteTrekkFarge] += Settinger.BRIKKEVERDIER[5];
                 resultat = 5;
               }
               else { resultat = 1; }
             }
           }


           //Sjekk om rokadebetingelser blir endret som følge av trekket:
           if(fraY == nesteTrekkFarge * 7){
             if(fraX == 0 && aTaarnErUflyttet[nesteTrekkFarge] == true){
               aTaarnErUflyttet[nesteTrekkFarge] = false;
             }
             if(fraX == 7 && hTaarnErUflyttet[nesteTrekkFarge] == true){
               hTaarnErUflyttet[nesteTrekkFarge] = false;
             }
             if(fraX == 4 && kongeErUflyttet[nesteTrekkFarge] == true){
               kongeErUflyttet[nesteTrekkFarge] = false;
             }
           }
           break; // Avbryter søk i mulige trekk-listen fordi match ble funnet/trekk ble utført.
         }
    }
    if(resultat != -1){
      byttTrekkFarge(); }
    return resultat;
  }

  private void byttTrekkFarge(){
    muligeTrekk = new ArrayList<Trekk>(40);
    grunnMuligeTrekk[0] = 0;
    grunnMuligeTrekk[1] = 0;
    grunnTrusselBonus[0] = 0;
    grunnTrusselBonus[1] = 0;
    forrigeTrekkFarge = nesteTrekkFarge;
    nesteTrekkFarge = 1 - nesteTrekkFarge;
    evalStreng = Evaluator.grunnEvaluering(this);
    oppdaterGrunnRating();
  }

  public boolean hentLangRokadeMulig(int farge){
    return (kongeErUflyttet[farge] && aTaarnErUflyttet[farge]);
  }
  public boolean hentKortRokadeMulig(int farge){
    return (kongeErUflyttet[farge] && hTaarnErUflyttet[farge]);
  }

  public void leggTilTrusselBonus(int farge, int bonus){
    grunnTrusselBonus[farge] += bonus;
  }

  private void oppdaterGrunnRating(){
    grunnRating =   brikkeSum[0]
                  + grunnMuligeTrekk[0]
                  + (int)Math.round(grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING)
                  - brikkeSum[1]
                  - grunnMuligeTrekk[1]
                  - (int)Math.round(grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING);
  }


  @Override
  public String toString(){
    String s = "";
    if(nesteTrekkFarge == 0){ s += "HVIT SIN TUR\n"; }
    else{ s += "SVART SIN TUR\n"; }
    s += "\nMulige trekk i stilling:\n";
    int i = 1;
    for(Trekk t : muligeTrekk){
      s += ((i++) +  ": " + t.toString() + "\n");
    }
    s += "Mulige trekk hvit:" + grunnMuligeTrekk[0] + "\n";
    s += "Mulige trekk svart:" + grunnMuligeTrekk[1] + "\n";
    s += "Hvit brikkesum: " + brikkeSum[0] + "\n";
    s += "Svart brikkesum: " + brikkeSum[1] + "\n";
    s += "Hvit trusselbonus: " + (grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING) + "\n";
    s += "Svart trusselbonus: " + (grunnTrusselBonus[1] * Settinger.TRUSSELBONUS_VEKTING) + "\n";
    s += "Grunnrating: " + grunnRating + "\n";
    return s;
  }

}
