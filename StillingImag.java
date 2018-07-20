/**
Instances of this class represent one particular chess positioning. It is
also used as a node in the look ahead tree. */

class StillingImag extends Stilling{

  // TIL TESTING:
  private int trekkLeggTil, testTeller = 0;
  private Trekk trekkPre;
  private String logg = "\nIMAG ST LOGG\n";


  private Stilling forelder;

  public StillingImag(int[][][] brettet,
                      Trekk trekkPre,
                      StillingStatus status,
                      Stilling forelder,
                      int dybde){ // Hvor mange generasjoner UNDER denne?

    super(brettet, status, trekkPre.hentFarge(), dybde);
    super.status = status;
    this.forelder = forelder;
    this.trekkPre = trekkPre;
    super.utforTrekk(trekkPre);
    super.byttTrekkFarge();
    leggTilBarn();
    Evaluator.fjernUlovligeTrekk(this);
    oppdaterGrunnRating();
  }

  private void leggTilBarn(){
    for(Trekk t : muligeTrekk){
      if(dybde > 0){
        t.settStillingEtterTrekk(new StillingImag(kopiAvBrettet(), t, status.kopi(), this, dybde - 1));
        testTeller++;
      }
    }
  }

/*
  //TESTING!
  @Override
  public void leggTilTrekk(int evFarge, int brikkeTypeNr, int trekkType, int fraX, int fraY, int tilX, int tilY){
    trekkLeggTil++;
  }
  */

  @Override
  public String toString(){

    return "Img ant barn:" + testTeller;
  }




}
