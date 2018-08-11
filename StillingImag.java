/**
Instances of this class represent one particular chess positioning. It is
also used as a node in the look ahead tree. */

import java.util.ArrayList;

class StillingImag extends Stilling{

  // TIL TESTING:
  private int trekkLeggTil, testTeller = 0;
  private Trekk trekkPre;
  private String logg = "\nIMAG ST LOGG\n";


  private Stilling forelder;
  private int trekkNr;

  public StillingImag(int[][][] brettet,
                      Trekk trekkPre,
                      StillingStatus status,
                      Stilling forelder,
                      int antStillinger,
                      int trekkNr){ // Trekk Nr i spill, stillinger på samme nivå i tre
                                    // har samme trekkNr.

    super(brettet, status, trekkPre.hentFarge(), antStillinger);
    this.forelder = forelder;
    this.trekkPre = trekkPre;
    this.trekkNr = trekkNr;
    utforTrekk(trekkPre);
    byttTrekkFarge();
    //leggTilBarn();
    oppdaterGrunnRating();
  }

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
    Evaluator.finnMuligeTrekk(this);
  }

  // brukes kun av IMAG for å opprette stilling ETTER preTrekk:
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

    // Flytter tårn ved rokade:
    if(trekkType == 2){
      brettet[nesteTrekkFarge][3][tilY] = brettet[nesteTrekkFarge][0][fraY];
      brettet[nesteTrekkFarge][0][fraY] = 0;
    }
    if(trekkType == 3){
      brettet[nesteTrekkFarge][5][tilY] = brettet[nesteTrekkFarge][7][fraY];
      brettet[nesteTrekkFarge][7][fraY] = 0;
    }

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

  public void leggTilEnGenerasjon(){
    for(int i = 0; i < muligeTrekk.size() && instansTeller < antStillinger; i++){
      Trekk t = muligeTrekk.get(i);
      StillingImag s = t.hentStillingEtterTrekk();
      if(s == null){
        StillingImag nyStIm = new StillingImag(kopiAvBrettet(),t , status.kopi(), this, antStillinger, trekkNr + 1);
        if(!Evaluator.trekkGodkjent(this, nyStIm)){
          muligeTrekk.remove(t);
          grunnMuligeTrekk[t.hentFarge()]--;
          i--;
        }
        else{
          t.settStillingEtterTrekk(nyStIm);
          if(trekkNr + 1 > lengstFramTrekkNr){ lengstFramTrekkNr = trekkNr + 1; }
        }
      }
      else{
        s.leggTilEnGenerasjon();
      }
    }
  }

  public Trekk hentTrekkPre(){ return trekkPre; }

  public int hentTrekkNr(){ return trekkNr; }

  @Override
  public String toString(){

    return "Img ant barn:" + testTeller;
  }




}
