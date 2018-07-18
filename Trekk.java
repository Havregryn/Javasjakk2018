class Trekk{
  private int farge, brikkeTypeNr;
  private int fraX, tilX, fraY, tilY;
  // Koordinat som registrerer felt kongen passerer ved rokade:
  private int rokadePasseringsFeltX = -1;
  private int dypRating;
  private Stilling stillingEtterTrekk;
  private int trekkType; //Viser type trekk:
                         // 0 = vanlig
                         // 1 = slår ut motst.
                         // 2 = lang rokade
                         // 3 = kort rokade
                         // 4 bondeForvandling
                         // 5 bondeforvandling med utslag motstander.

  public Trekk(int farge, int brikkeTypeNr, int trekkType, int fraX, int fraY, int tilX, int tilY){
    this.farge = farge;
    this.brikkeTypeNr = brikkeTypeNr;
    this.trekkType = trekkType;
    this.fraX = fraX;
    this.fraY = fraY;
    this.tilX = tilX;
    this.tilY = tilY;
    if( brikkeTypeNr == 6 && Math.abs(fraX - tilX) == 2){
      rokadePasseringsFeltX = (fraX + tilX) / 2;
    }

  }

  public int hentFarge(){ return farge; }
  public int hentBrikkeTypeNr(){ return brikkeTypeNr; }
  public int hentTrekkType(){ return trekkType; }
  public int hentFraX(){ return fraX; }
  public int hentFraY(){ return fraY; }
  public int hentTilX(){ return tilX; }
  public int hentTilY(){ return tilY; }

  public void settStillingEtterTrekk(StillingImag stIm){
    stillingEtterTrekk = stIm;
  }
  public StillingImag hentStillingEtterTrekk(){
    return (StillingImag) stillingEtterTrekk;
  }

  public int hentRokadeType(){
    if(brikkeTypeNr == 6 && rokadePasseringsFeltX == 3){ return 1; } // Lang rokade
    if(brikkeTypeNr == 6 && rokadePasseringsFeltX == 5){ return 2; } // Kort rokade
    return 0;
  }

  public int hentRokadePasseringsFeltX(){ return rokadePasseringsFeltX; }

  @Override
  public String toString(){
    String s = "Ittno";
    //if(stillingEtterTrekk != null){ s = stillingEtterTrekk.toString(); }
    return "Trekk brikkeType:  " + brikkeTypeNr +
           " fra: " + fraX + ", " + fraY +
           " til: " + tilX + ", " + tilY +
           " trekkType: " + trekkType +
           " st etter trekk: " + s;
  }

}
