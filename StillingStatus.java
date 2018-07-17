/**
Class containing the variables neccessary for evaluating a move.
*/
class StillingStatus{

  boolean[] erISjakk = {false, false};
  boolean[] kongeErUflyttet = {true, true};
  boolean[] aTaarnErUflyttet = {true, true};
  boolean[] hTaarnErUflyttet = {true, true};
  int brikkeSum[] = {0, 0};

  public StillingStatus kopi(){
    StillingStatus ss = new StillingStatus();
    for(int farge = 0; farge < 2; farge++){
      ss.erISjakk[farge] = this.erISjakk[farge];
      ss.kongeErUflyttet[farge] = this.kongeErUflyttet[farge];
      ss.aTaarnErUflyttet[farge] = this.aTaarnErUflyttet[farge];
      ss.hTaarnErUflyttet[farge] = this.hTaarnErUflyttet[farge];
      ss.brikkeSum[farge] = this.brikkeSum[farge];
    }
    return ss;
  }

}
