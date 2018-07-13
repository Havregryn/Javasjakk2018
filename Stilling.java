/**
Instans av Stilling representerer stilling som vises på skjermen. Det foregår direkte
toveis kommunikasjon mellom uiMaster og Stilling instans.
*/
import java.util.ArrayList;
import java.util.Random;

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
  private boolean[] erISjakk = {false, false};
  private String evalStreng = "";

  private boolean[] kongeErUflyttet = {true, true};
  private boolean[] aTaarnErUflyttet = {true, true};
  private boolean[] hTaarnErUflyttet = {true, true};
  private boolean[] c_d_erTruet ={false, false};
  private boolean[] f_g_erTruet = {false, false};

  private Random tilfeldig = new Random();

  //FEILSØK lOGG:
  private String logg = "LOGG:\n";

  //Konstruktør, reell/imag: felles er brettet
  public Stilling(int[][][] brettet, int nesteTrekkFarge, Parti parti, UIMaster uiMaster){
    this.brettet = brettet;
    this.nesteTrekkFarge = nesteTrekkFarge;
    forrigeTrekkFarge = 1 - nesteTrekkFarge;
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

  //reell/imag: begge
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
  // reell/imag: må initieres via konstruktør i imag!!
  public void initierRokadeVariabler(){
    // Rokade er kun mulig dersom konge og tårn er satt opp på normal startplass:
    kongeErUflyttet[0] = (brettet[0][4][0] == 6);
    aTaarnErUflyttet[0] = (brettet[0][0][0] == 4);
    hTaarnErUflyttet[0] = (brettet[0][7][0] == 4);
    kongeErUflyttet[1] = (brettet[1][4][7] == 6);
    aTaarnErUflyttet[1] = (brettet[1][0][7] == 4);
    hTaarnErUflyttet[1] = (brettet[1][7][7] == 4);

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

  // TESTMETODE:
  public String hentEvalStreng(){ return evalStreng; }

  // reell/imag: Brukes denne av Evaluator?
  public int[][][] hentBrett(){ return brettet; }

  // reell/imag: Brukes av evaluator!
  public int hentNesteTrekkFarge(){ return nesteTrekkFarge; }

  // reell/imag: Evaluator!
  public ArrayList<Trekk> hentTrekkListe(){ return muligeTrekk; }

  // reell/imag: Evaluator!!
  public void leggTilTrekk(int evFarge, int brikkeTypeNr, int trekkType, int fraX, int fraY, int tilX, int tilY){
    grunnMuligeTrekk[evFarge] += Settinger.TREKK_VERDI;
    if(evFarge == nesteTrekkFarge){
      muligeTrekk.add(new Trekk(evFarge, brikkeTypeNr, trekkType, fraX, fraY, tilX, tilY));
    }
    if(brettet[1 - evFarge][tilX][tilY] == 6){ erISjakk[1 - evFarge] = true; }
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
           utforTrekk(trekket);
           break;
         }
    }
    if(trekkType != -1){ byttTrekkFarge(); }
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
    utforTrekk(trekket);
    byttTrekkFarge();
    return trekket;
  }

  private void utforTrekk(Trekk trekket){
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
      brikkeSum[1 - farge] -= Settinger.BRIKKEVERDIER[brettet[1 - farge][tilX][tilY]];
      brettet[1 - farge][tilX][tilY] = 0;
    }
    // Endrer brikkeSum og brikkeverdi ved bondeForvandling:
    if(trekkType == 4 || trekkType == 5){
      brettet[farge][tilX][tilY] = 5;
      brikkeSum[farge] -= Settinger.BRIKKEVERDIER[1];
      brikkeSum[farge] += Settinger.BRIKKEVERDIER[5];
    }

    //Sjekk om rokadebetingelser blir endret som følge av trekket:
    if(fraY == farge * 7){
      if(fraX == 0 && aTaarnErUflyttet[farge] == true){
        aTaarnErUflyttet[farge] = false;
      }
      if(fraX == 7 && hTaarnErUflyttet[farge] == true){
        hTaarnErUflyttet[farge] = false;
      }
      if(fraX == 4 && kongeErUflyttet[farge] == true){
        kongeErUflyttet[farge] = false;
      }
    }
  }


  private void byttTrekkFarge(){
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
    erISjakk[0] = false;
    erISjakk[1] = false;
    logg = "";
    evalStreng = Evaluator.grunnEvaluering(this);
    oppdaterGrunnRating();
  }
  // reell/imag: Evaluering
  public boolean hentLangRokadeMulig(int farge){
    return (kongeErUflyttet[farge] && aTaarnErUflyttet[farge] && !c_d_erTruet[farge]);
  }
  // reell/imag: Evaluering
  public boolean hentKortRokadeMulig(int farge){
    return (kongeErUflyttet[farge] && hTaarnErUflyttet[farge] && !f_g_erTruet[farge]);
  }
  // reell/imag: Evaluering
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

  // reell/imag: Kjekk å ha uansett
  @Override
  public String toString(){
    String s = "";
    if(nesteTrekkFarge == 0){ s += "HVIT SIN TUR\n"; }
    else{ s += "SVART SIN TUR\n"; }
    if(erISjakk[0]){ s += "HVIT ER I SJAKK!!\n"; }
    if(erISjakk[1]){ s += "SVART ER I SJAKK!!\n"; }
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
    s += "\nLOGG:\n" + (logg + "\n");

    return s;
  }

}
