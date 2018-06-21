/**
Instans av Stilling representerer stilling som vises på skjermen. Det foregår direkte
toveis kommunikasjon mellom uiMaster og Stilling instans.
*/
import java.util.ArrayList;

class Stilling{
  private UIMaster uiMaster;
  private short nesteTrekkFarge;
  private short forrigeTrekkFarge;
  private short forrigeFargeMuligetrekk;
  private short[][][] brettet;
  private Parti parti;
  private ArrayList<Trekk> muligeTrekk;

  public Stilling(short[][][] brettet, short nesteTrekkFarge, Parti parti, UIMaster uiMaster){
    this.brettet = brettet;
    this.nesteTrekkFarge = nesteTrekkFarge;
    if(nesteTrekkFarge == 0){ forrigeTrekkFarge = 1; }
    else{ forrigeTrekkFarge = 0; }
    this.parti = parti;
    this.uiMaster = uiMaster;
    muligeTrekk = new ArrayList<Trekk>(40);
    this.forrigeFargeMuligetrekk = 0;
  }

  public short hentFelt(int farge, int x, int y){ return brettet[farge][x][y]; }

  public short[][][] hentBrett(){ return brettet; }

  public short hentNesteTrekkFarge(){ return nesteTrekkFarge; }

  public ArrayList<Trekk> hentTrekkListe(){ return muligeTrekk; }

  public void leggTilForrigeFargeMuligeTrekk(short rating)/////

  public short manueltTrekk(int fraX, int fraY, int tilX, int tilY){
    short resultat = -1;
    if(brettet[nesteTrekkFarge][fraX][fraY] != 0 && brettet[nesteTrekkFarge][tilX][tilY] == 0){
      //Riktig farge flyttes og ikke til samme farge
      brettet[nesteTrekkFarge][tilX][tilY] = brettet[nesteTrekkFarge][fraX][fraY];
      brettet[nesteTrekkFarge][fraX][fraY] = 0;
      if(brettet[forrigeTrekkFarge][tilX][tilY] == 0){
        resultat = 0;
        byttTrekkFarge();
      }
      else{
        brettet[forrigeTrekkFarge][tilX][tilY] = 0;
        resultat = 1;
        byttTrekkFarge();
      }
    }
    return resultat;
  }

  public void settOppBrikkerUI(){
    short hvitPaaFelt, svartPaaFelt;
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
    forrigeFargeMuligetrekk = 0;
    if(nesteTrekkFarge == 0){
      nesteTrekkFarge = 1;
      forrigeTrekkFarge = 0;
    }
    else{
      nesteTrekkFarge = 0;
      forrigeTrekkFarge = 1;
    }
  }

}
