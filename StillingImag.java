/**
Instances of this class represent one particular chess positioning. It is
also used as a node in the look ahead tree. */

class StillingImag extends Stilling{

  private Stilling forelder;

  public StillingImag(int[][][] brettet,
                      Trekk trekkPre,
                      StillingStatus status,
                      Stilling forelder,
                      int dybde){ // Hvor mange generasjoner UNDER denne?

    super(brettet, status, trekkPre.hentFarge(), dybde);
    super.status = status;
    this.forelder = forelder;
    super.utforTrekk(trekkPre);
    super.byttTrekkFarge();
    leggTilBarn();
  }

  private void leggTilBarn(){
    for(Trekk t : muligeTrekk){
      if(dybde > 0){
        t.settStillingEtterTrekk(new StillingImag(kopiAvBrettet(), t, status.kopi(), this, dybde - 1));
      }
    }
  }

  @Override
  public String toString(){
    return " ant trekk: " + muligeTrekk.size();
  }




}
