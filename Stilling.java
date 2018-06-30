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
  private String evalStreng = "";

  public Stilling(int[][][] brettet, int nesteTrekkFarge, Parti parti, UIMaster uiMaster){
    this.brettet = brettet;
    this.nesteTrekkFarge = nesteTrekkFarge;
    grunnRating = 0;
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
    evalStreng += Evaluator.grunnEvaluering(this);
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

  public void leggTilBrikkeSum(int farge, int brikkeVerdi ){
    brikkeSum[farge] += brikkeVerdi;
  }

  public int manueltTrekk(int fraX, int fraY, int tilX, int tilY){
    int resultat = -1;
    for(Trekk trekk : muligeTrekk){
      if(fraX == trekk.hentFraX() &&
         fraY == trekk.hentFraY() &&
         tilX == trekk.hentTilX() &&
         tilY == trekk.hentTilY()){
           brettet[nesteTrekkFarge][tilX][tilY] = brettet[nesteTrekkFarge][fraX][fraY];
           brettet[nesteTrekkFarge][fraX][fraY] = 0;
           if(brettet[forrigeTrekkFarge][tilX][tilY] == 0){
             resultat = 0; }
           else{
             brettet[forrigeTrekkFarge][tilX][tilY] = 0;
             resultat = 1;
           }
           break;
         }
    }
    byttTrekkFarge();
    return resultat;
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

  private void byttTrekkFarge(){
    muligeTrekk = new ArrayList<Trekk>(40);
    for(int i = 0; i < 2; i++){
      brikkeSum[i] = 0;
      grunnMuligeTrekk[i] = 0;
    }
    if(nesteTrekkFarge == 0){
      nesteTrekkFarge = 1;
      forrigeTrekkFarge = 0;
    }
    else{
      nesteTrekkFarge = 0;
      forrigeTrekkFarge = 1;
    }
    evalStreng = Evaluator.grunnEvaluering(this);
  }

  @Override
  public String toString(){
    String s = "MULIGE TREKK I STILLING\n";
    int i = 1;
    for(Trekk t : muligeTrekk){
      s += ((i++) +  ": " + t.toString() + "\n");
    }
    s += "Mulige trekk hvit:" + grunnMuligeTrekk[0] + "\n";
    s += "Mulige trekk svart:" + grunnMuligeTrekk[1] + "\n";
    return s;
  }

}
