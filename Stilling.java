/**
Instans av Stilling representerer stilling som vises på skjermen.
*/
class Stilling{
  private short nesteTrekkFarge;
  private short forrigeTrekkFarge;
  private short[][][] brettet;
  private Parti parti;

  public Stilling(short[][][] brettet, short nesteTrekkFarge, Parti parti){
    this.brettet = brettet;
    this.nesteTrekkFarge = nesteTrekkFarge;
    if(nesteTrekkFarge == 0){ forrigeTrekkFarge = 1; }
    else{ forrigeTrekkFarge = 0; }
    this.parti = parti;
  }

  public short hentFelt(int farge, int x, int y){ return brettet[farge][x][y]; }

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

  private void byttTrekkFarge(){
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
