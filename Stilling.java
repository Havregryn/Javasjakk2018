/**
Abstrakt klasse med to subklasser: ReellStilling og imagStilling.
*/
import java.util.ArrayList;
import java.util.Random;

abstract class Stilling{
  protected StillingStatus status = null;
  private int nesteTrekkFarge;
  private int forrigeTrekkFarge;
  protected int[][][] brettet;
  protected ArrayList<Trekk> muligeTrekk;
  private int grunnRating = 0;
  private int grunnMuligeTrekk[] = {0, 0};
  private double[] grunnTrusselBonus = {0.0, 0.0};
  private boolean[] c_d_erTruet ={false, false};
  private boolean[] f_g_erTruet = {false, false};
  protected int dybde;

  // TESTING:
  private static int instansTeller = 0;
  private static int utforteTrekk = 0;
  private static int fargeBytte = 0;
  private static int trekkLeggTil = 0;
  private static int trekkRiktigFarge = 0;



  protected String evalStreng = "";

  protected Random tilfeldig = new Random();

  //FEILSØK lOGG:
  private String logg = "LOGG:\n";


  public Stilling(int[][][] brettet, StillingStatus status, int nesteTrekkFarge, int dybde){
    instansTeller++;
    this.brettet = brettet;
    this.status = status;
    this.nesteTrekkFarge = nesteTrekkFarge;
    forrigeTrekkFarge = 1 - nesteTrekkFarge;
    this.dybde = dybde;
    muligeTrekk = new ArrayList<Trekk>(40);
    initierBrikkeSum();
    evalStreng += Evaluator.grunnEvaluering(this);
    oppdaterGrunnRating();
  }

  //reell/imag: begge
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
  // reell/imag: må initieres via konstruktør i imag!! Tas imot fra forrige brett
  public void initierRokadeVariabler(){
    // Rokade er kun mulig dersom konge og tårn er satt opp på normal startplass:
    status.kongeErUflyttet[0] = (brettet[0][4][0] == 6);
    status.aTaarnErUflyttet[0] = (brettet[0][0][0] == 4);
    status.hTaarnErUflyttet[0] = (brettet[0][7][0] == 4);
    status.kongeErUflyttet[1] = (brettet[1][4][7] == 6);
    status.aTaarnErUflyttet[1] = (brettet[1][0][7] == 4);
    status.hTaarnErUflyttet[1] = (brettet[1][7][7] == 4);

  }

  // TESTMETODE:
  public String hentEvalStreng(){ return evalStreng; }
  public int hentGrunnrating(){return grunnRating; }

  // reell/imag: begge!
  public int[][][] hentBrett(){ return brettet; }

  // reell/imag: begge!
  public int hentNesteTrekkFarge(){ return nesteTrekkFarge; }

  // reell/imag: begge!
  public ArrayList<Trekk> hentTrekkListe(){ return muligeTrekk; }

  // reell/imag: begge!
  public void leggTilTrekk(int evFarge, int brikkeTypeNr, int trekkType, int fraX, int fraY, int tilX, int tilY){
    trekkLeggTil++;
    grunnMuligeTrekk[evFarge] += Settinger.TREKK_VERDI;
    if(evFarge == nesteTrekkFarge){
      trekkRiktigFarge++;
      muligeTrekk.add(new Trekk(evFarge, brikkeTypeNr, trekkType, fraX, fraY, tilX, tilY));
    }
    // Dersom trekket tar motstanders konge: motstander er i sjakk!!
    if(brettet[1 - evFarge][tilX][tilY] == 6){ status.erISjakk[1 - evFarge] = true; }
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




  // private, intern, brukes av begge!
  protected void utforTrekk(Trekk trekket){
    utforteTrekk++;
    int trekkType = trekket.hentTrekkType();
    int farge = trekket.hentFarge();
    int fraX = trekket.hentFraX();
    int fraY = trekket.hentFraY();
    int tilX = trekket.hentTilX();
    int tilY = trekket.hentTilY();

    brettet[nesteTrekkFarge][tilX][tilY] = brettet[nesteTrekkFarge][fraX][fraY];
    brettet[nesteTrekkFarge][fraX][fraY] = 0;

    // Trekker fra brikkeSum når brikke slås ut:
    if(trekkType == 1 || trekkType == 5 ){
      status.brikkeSum[1 - farge] -= Settinger.BRIKKEVERDIER[brettet[1 - farge][tilX][tilY]];
      brettet[1 - farge][tilX][tilY] = 0;
    }
    // Endrer brikkeSum og brikkeverdi ved bondeForvandling:
    if(trekkType == 4 || trekkType == 5){
      brettet[farge][tilX][tilY] = 5;
      status.brikkeSum[farge] -= Settinger.BRIKKEVERDIER[1];
      status.brikkeSum[farge] += Settinger.BRIKKEVERDIER[5];
    }

    //Sjekk om rokadebetingelser blir endret som følge av trekket:
    if(fraY == farge * 7){
      if(fraX == 0 && status.aTaarnErUflyttet[farge] == true){
        status.aTaarnErUflyttet[farge] = false;
      }
      if(fraX == 7 && status.hTaarnErUflyttet[farge] == true){
        status.hTaarnErUflyttet[farge] = false;
      }
      if(fraX == 4 && status.kongeErUflyttet[farge] == true){
        status.kongeErUflyttet[farge] = false;
      }
    }
  }

  // begge!
  protected void byttTrekkFarge(){
    fargeBytte++;
    muligeTrekk = new ArrayList<Trekk>(40);
    grunnMuligeTrekk[0] = 0;
    grunnMuligeTrekk[1] = 0;
    grunnTrusselBonus[0] = 0;
    grunnTrusselBonus[1] = 0;
    c_d_erTruet[0] = false;
    c_d_erTruet[1] = false;
    f_g_erTruet[0] = false;
    f_g_erTruet[1] = false;
    forrigeTrekkFarge = nesteTrekkFarge;
    nesteTrekkFarge = 1 - nesteTrekkFarge;
    status.erISjakk[0] = false;
    status.erISjakk[1] = false;
    evalStreng = Evaluator.grunnEvaluering(this);
    oppdaterGrunnRating();
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
  public void leggTilTrusselBonus(int farge, int bonus){
    grunnTrusselBonus[farge] += bonus;
  }

  // begge!
  private void oppdaterGrunnRating(){
    grunnRating =   status.brikkeSum[0]
                  + grunnMuligeTrekk[0]
                  + (int)Math.round(grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING)
                  - status.brikkeSum[1]
                  - grunnMuligeTrekk[1]
                  - (int)Math.round(grunnTrusselBonus[0] * Settinger.TRUSSELBONUS_VEKTING);
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
    s += "Grunnrating: " + grunnRating + "\n";
    s += "Ant instanser: " + instansTeller;
    s += " leggTilTrekk" + trekkLeggTil;
    s += "trekkRiktigFarge: " + trekkRiktigFarge;



    return evalStreng;
  }

}
