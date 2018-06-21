/**
  Klasse med statiske metoder som evaluerer en stilling.
*/

class Evaluator{
  short bondeVerdi = Settinger.BONDE_VERDI;
  short springerVerdi = Settinger.SPRINGER_VERDI;
  short loperVerdi = Settinger.LOPER_VERDI;
  short taarnVerdi = Settinger.TAARN_VERDI;
  short dronningverdi = Settinger.DRONNING_VERDI;


  public static short grunnEvaluering(Stilling stilling){
    short[][][] brettet = stilling.hentBrett();
    ArrayList<Trekk> muligeTrekk = stilling.hentTrekkListe();
    short nesteTrekkFarge = stilling.hentNesteTrekkFarge();
    short forrigeTrekkFarge;
    if(nesteTrekkFarge == 0){ forrigeTrekkFarge = 1; }
    else{ forrigeTrekkFarge == 0; }

    short[]rating = new short[2];
    rating[0] = 0;
    rating[1] = 0;
    for(short evFarge = 0; evFarge < 2; evFarge++ ){
      for(short y = 0; y < 8; y++){
        for(short x = 0; x < 8; x++){
          short felt = brettet[evFarge][x][y];
          switch(felt){
            case 0 : break;

            case 1 :  rating[evFarge] += grunnBondeEval(brette x, y, t, evFarge);
                      break;
            case 2 :  rating[evFarge] += grunnSpringerEval(brette x, y, t, evFarge);
                      break;
            case 3 :  rating[evFarge] += grunnLoperEval(brette x, y, t, evFarge);
                      break;
            case 4 :  rating[evFarge] += grunnTaarnEval(brettet, x, y, evFarge);
                      break;
            case 5 :  rating[evFarge] += grunnDronningEval(brette x, y, t, evFarge);
                      break;
            case 6 :  rating[evFarge] += grunnKongeEval(brette x, y, t, evFarge);
                      break;
          }
        }
      }
    }
  }
  return rating[nesteTrekkFarge] - rating[forrigeTrekkFarge];

  private static short grunnBondeEval(short[][][] brettet, short x, short y, short evFarge){
    int retning = (evFarge * -2) + 1;
    if(x + retning <= 0 && x + retning <= 7){
      Trekk trekk = new Trekk
    }


}
