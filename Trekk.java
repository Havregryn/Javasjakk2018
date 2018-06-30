class Trekk{
  private int brikkeTypeNr;
  private int fraX, tilX, fraY, tilY;
  private int dypRating;
  //private MuligStilling stillingEtterTrekk;

  public Trekk(int brikkeTypeNr, int fraX, int fraY, int tilX, int tilY){
    this.brikkeTypeNr = brikkeTypeNr;
    this.fraX = fraX;
    this.fraY = fraY;
    this.tilX = tilX;
    this.tilY = tilY;
  }

  public int hentBrikkeTypeNr(){ return brikkeTypeNr; }
  public int hentFraX(){ return fraX; }
  public int hentFraY(){ return fraY; }
  public int hentTilX(){ return tilX; }
  public int hentTilY(){ return tilY; }

  @Override
  public String toString(){
    return "Trekk brikkeType:  " + brikkeTypeNr +
           " fra: " + fraX + ", " + fraY +
           " til: " + tilX + ", " + tilY;
  }

}
