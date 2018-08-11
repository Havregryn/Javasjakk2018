/**
Abstrakt klasse med to subklasser: ReellStilling og imagStilling.
*/
import java.util.ArrayList;
import java.util.Random;

abstract class Stilling{
  protected StillingStatus status = null;
  protected int nesteTrekkFarge;
  protected int forrigeTrekkFarge;
  protected int[][][] brettet;
  protected ArrayList<Trekk> muligeTrekk;
  protected double[] grunnRating = {0, 0};
  protected double[] dypRating = {0, 0};
  protected int grunnMuligeTrekk[] = {0, 0};
  protected double[] grunnTrusselBonus = {0.0, 0.0};
  protected double[] offensiv = {0.0, 0.0};
  protected boolean[] c_d_erTruet ={false, false};
  protected boolean[] f_g_erTruet = {false, false};
  protected int antStillinger;
  private RatingSett rs;

  protected static int reellTrekkNr = 0, lengstFramTrekkNr = 0;

  // TESTING:
  protected static int instansTeller = 0;
  protected static int utforteTrekk = 0;
  protected static int fargeBytte = 0;
  private static int trekkLeggTil = 0;
  private static int trekkRiktigFarge = 0;
  private static int trekkFjerning = 0;



  protected String evalStreng = "";

  protected Random tilfeldig = new Random();

  //FEILSØK lOGG:
  private String logg = "LOGG:\n";


  public Stilling(int[][][] brettet, StillingStatus status, int nesteTrekkFarge, int antStillinger){
    instansTeller++;
    this.brettet = brettet;
    this.status = status;
    this.nesteTrekkFarge = nesteTrekkFarge;
    forrigeTrekkFarge = 1 - nesteTrekkFarge;
    this.antStillinger = antStillinger;
    muligeTrekk = new ArrayList<Trekk>(40);
  }

  // TESTMETODE:
  public String hentEvalStreng(){ return evalStreng; }
  public double[] hentGrunnrating(){return grunnRating; }
  public double[] hentDypRating(){ return dypRating; }
  public int[] hentGrunnMuligeTrekk(){ return grunnMuligeTrekk; }
  public double[] hentGrunnTrusselBonus(){ return grunnTrusselBonus; }
  public boolean[] hent_c_d_truet(){ return c_d_erTruet; }
  public boolean[] hent_f_g_truet(){ return f_g_erTruet; }

  public Trekk hentHoyestRatedeTrekk(){
    double hoyestGrunnRating = -99999999;
    Trekk besteTrekk = null;
    for( Trekk t : muligeTrekk){
      Stilling s = t.hentStillingEtterTrekk();
      if( s != null && s.hentGrunnrating()[nesteTrekkFarge] > hoyestGrunnRating){
        besteTrekk = t;
        hoyestGrunnRating = s.hentGrunnrating()[nesteTrekkFarge];
      }
    }
    return besteTrekk;
   }

  // reell/imag: begge!
  public int[][][] hentBrett(){ return brettet; }
  public StillingStatus hentStatus(){ return status; }

  // reell/imag: begge!
  public int hentNesteTrekkFarge(){ return nesteTrekkFarge; }

  // reell/imag: begge!
  public ArrayList<Trekk> hentTrekkListe(){ return muligeTrekk; }

  // reell/imag: begge!
  public void leggTilTrekk(int evFarge, int brikkeTypeNr, int trekkType, int fraX, int fraY, int tilX, int tilY){
    trekkLeggTil++;
    grunnMuligeTrekk[evFarge] += 1;
    if(evFarge == nesteTrekkFarge){
      trekkRiktigFarge++;
      muligeTrekk.add(new Trekk(evFarge, brikkeTypeNr, trekkType, fraX, fraY, tilX, tilY));
    }
    // Dersom trekket tar motstanders konge: motstander er i sjakk!!
    if(brettet[1 - evFarge][tilX][tilY] == 6){ status.erISjakk[1 - evFarge] = true; }
    int startY = evFarge * 7;
    offensiv[evFarge] -= (Settinger.BRIKKEVERDIER[brikkeTypeNr] * Math.abs(fraY - startY));
    offensiv[evFarge] += (Settinger.BRIKKEVERDIER[brikkeTypeNr] * Math.abs(tilY - startY));
    if(brettet[1 - evFarge][tilX][tilY] != 0){
      grunnTrusselBonus[evFarge] += Settinger.BRIKKEVERDIER[brettet[1 - evFarge][tilX][tilY]]; }

    // Oppdaterer flagg som varsler om trussel som gjør rokade ulovlig:
    // NB: Ulovlig rokade kan allikevel bli lagt inn dersom trusseltrekk ikke er registrert først.
    // Disse ulovlige rokadene fjernes ved framsyn ett trekk:
    if(evFarge == 0 && tilY == 7){
      logg += "truer muligens svart rokade\n";
      if(tilX == 2 || tilX == 3){ c_d_erTruet[1] = true; logg+= ("Svart cd truet av felt: " + fraX + ", " + fraY + "\n");}
      if(tilX == 5 || tilX == 6){ f_g_erTruet[1] = true; logg+= "Svart fg truet av felt: " + fraX + ", " + fraY + "\n";}
    }
    if(evFarge == 1 && tilY == 0){
      logg += "truer muligens hvit rokade\n";
      if(tilX == 2 || tilX == 3){ c_d_erTruet[0] = true; logg+= ("Hvit cd truet av felt: " + fraX + ", " + fraY + "\n");}
      if(tilX == 5 || tilX == 6){ f_g_erTruet[0] = true; logg += ("Hvit fg truet av felt: " + fraX + ", " + fraY + "\n");}
    }
  }

  public void fjernTrekk(Trekk t){
    muligeTrekk.remove(t);
    grunnMuligeTrekk[t.hentFarge()] -= 1;
    trekkFjerning++;
  }

  protected void avsluttInstans(){
    for(Trekk t : muligeTrekk){
      if(t.hentStillingEtterTrekk() != null){
        t.hentStillingEtterTrekk().avsluttInstans();
        t.settStillingEtterTrekk(null);
      }
    }
    instansTeller--;
  }

  // reell/imag: Evaluering
  public boolean hentLangRokadeMulig(int farge){
    return (status.kongeErUflyttet[farge] && status.aTaarnErUflyttet[farge] && !c_d_erTruet[farge]);
  }
  // reell/imag: Evaluering
  public boolean hentKortRokadeMulig(int farge){
    return (status.kongeErUflyttet[farge] && status.hTaarnErUflyttet[farge] && !f_g_erTruet[farge]);
  }
  // reell/imag: Evaluering
  public void leggTilTrusselBonus(int farge, double bonus){
    grunnTrusselBonus[farge] += bonus;
  }

  // begge!
  protected void oppdaterGrunnRating(){
    if(status.brikkeSum[0] < Settinger.MITTSPILL_BRIKKESUM_GRENSE ||
       status.brikkeSum[1] < Settinger.MITTSPILL_BRIKKESUM_GRENSE){
      rs = Settinger.midtspillRating;
    }
    else if(status.brikkeSum[0] < Settinger.SLUTTSPILL_BRIKKESUM_GRENSE ||
       status.brikkeSum[1] < Settinger.SLUTTSPILL_BRIKKESUM_GRENSE){
      rs = Settinger.sluttspillRating;
    }
    else{
      rs = Settinger.aapningRating;
    }

    for(int farge = 0; farge < 2; farge++){
      grunnRating[farge] =   status.brikkeSum[farge] * rs.egneBrikkerVekting
                           + grunnMuligeTrekk[farge] * rs.egneTrekkVerdi
                           + grunnTrusselBonus[farge] * rs.trusselBonusVekting
                           + offensiv[farge] * rs.egneOffensivVekting
                           - status.brikkeSum[1 - farge] * rs.motstBrikkerVekting
                           - grunnMuligeTrekk[1 - farge] * rs.motstTrekkVerdi
                           - grunnTrusselBonus[1 - farge] * rs.truetVekting
                           - offensiv[1 - farge] * rs.motstOffensivVekting;
      if(status.kongeErUflyttet[farge]){
        if(status.aTaarnErUflyttet[farge]){ grunnRating[farge] += rs.rokadeMuligBonus; }
        if(status.hTaarnErUflyttet[farge]){ grunnRating[farge] += rs.rokadeMuligBonus; }
      }

      if(status.erISjakk[farge] && grunnMuligeTrekk[farge] == 0){
        grunnRating[farge] -= 10000000;
        grunnRating[1 - farge] += 10000000;
      }
    }
  }

  public double[] oppdaterDypRating(){
    // de nederste stillingene sender grunnrating oppover. Snitt av nederste stillinger
    // blir rating ett lag over.
    double[] avlestRating = {0, 0};
    double[] sum = {0, 0};
    double[] beregnetRating = {0, 0};
    int antGyldigeTrekk = 0;
    for(Trekk t : muligeTrekk){
      if(t.hentStillingEtterTrekk() != null){
        avlestRating = t.hentStillingEtterTrekk().oppdaterDypRating();
        for(int farge = 0; farge < 2; farge++){
          sum[farge] += avlestRating[farge];
        }
        antGyldigeTrekk++;
      }
    }
    if(antGyldigeTrekk > 0){
      for(int farge = 0; farge < 2; farge++){
        beregnetRating[farge] = sum[farge]/antGyldigeTrekk;
      }
    }
    else{
      oppdaterGrunnRating();
      beregnetRating[0] = grunnRating[0];
      beregnetRating[1] = grunnRating[1];
    }
    dypRating[0] = beregnetRating[0];
    dypRating[1] = beregnetRating[1];
    return beregnetRating;
  }

  protected int[][][] kopiAvBrettet(){
    int[][][] brettKopi = new int[2][8][8];
    for(int farge = 0; farge < 2; farge++){
      for(int x = 0; x < 8; x++){
        for(int y = 0; y < 8; y++){
          brettKopi[farge][x][y] = brettet[farge][x][y];
        }
      }
    }
    return brettKopi;
  }

  // reell/imag: Kjekk å ha uansett
  @Override
  public String toString(){
    String s = "";
    if(nesteTrekkFarge == 0){ s += "HVIT SIN TUR\n"; }
    else{ s += "SVART SIN TUR\n"; }
    if(status.erISjakk[0]){ s += "HVIT ER I SJAKK!!\n"; }
    if(status.erISjakk[1]){ s += "SVART ER I SJAKK!!\n"; }
    s += "\nMulige trekk i stilling:\n";
    int i = 1;
    for(Trekk t : muligeTrekk){
      s += ((i++) +  ": " + t.toString() + "\n");
    }
    s += "Mulige trekk hvit:" + grunnMuligeTrekk[0] + "\n";
    s += "Mulige trekk svart:" + grunnMuligeTrekk[1] + "\n";
    s += "Hvit brikkesum: " + status.brikkeSum[0] + "\n";
    s += "Svart brikkesum: " + status.brikkeSum[1] + "\n";
    s += "Hvit trusselbonus: " + (grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING) + "\n";
    s += "Svart trusselbonus: " + (grunnTrusselBonus[1] * Settinger.TRUSSELBONUS_VEKTING) + "\n";
    s += "Grunnrating hvit : " + grunnRating[0] + "\n";
    s += "Grunnrating svart: " + grunnRating[1] + "\n";
    s += "Antall instanser: " + instansTeller;
    s += " leggTilTrekk" + trekkLeggTil;
    s += "trekkRiktigFarge: " + trekkRiktigFarge;
    s += "Nestetrekk nr: " + reellTrekkNr;
    s+= "lengstFramTrekkNr: " + lengstFramTrekkNr;
    s += rs.toString();



    return s;
  }

}
