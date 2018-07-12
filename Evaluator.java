/**
  Klasse med statiske metoder som evaluerer en stilling.
*/

class Evaluator{

  private static Stilling stilling = null;
  private static int[][][] brettet = null;
  private static int nesteTrekkFarge = 0;
  private static int forrigeTrekkFarge = 0;

  public static String grunnEvaluering(Stilling stillingen){
    String evalStreng = "";
    stilling = stillingen;
    brettet = stilling.hentBrett();
    nesteTrekkFarge = stilling.hentNesteTrekkFarge();
    forrigeTrekkFarge = 1 - nesteTrekkFarge;

    for(int evFarge = 0; evFarge < 2; evFarge++ ){
      boolean evaluererNesteTrekkFarge = (evFarge == nesteTrekkFarge);
      for(int y = 0; y < 8; y++){
        for(int x = 0; x < 8; x++){
          int felt = brettet[evFarge][x][y];
          switch(felt){
            case 0 : break;

            case 1 :  evalStreng += grunnBondeEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
            case 2 :  evalStreng += grunnSpringerEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
            case 3 :  evalStreng += grunnLoperEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
            case 4 :  evalStreng += grunnTaarnEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
            case 5 :  evalStreng += grunnDronningEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
            case 6 :  evalStreng += grunnKongeEval(x, y, evFarge, evaluererNesteTrekkFarge);
                      break;
          }
        }
      }
    }
    return evalStreng;
  }

  private static String grunnBondeEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String bondeEvalStreng = "Bonde på: " + x + ", " + y + ": ";
    int[][][] brettet = stilling.hentBrett();
    int retning = (evFarge * -2) + 1;
    int motstander = 1 - evFarge;
    // urort:True om bonden står på startposisjon:
    boolean urort = (evFarge == 0 && y == 1) || (evFarge == 1 && y == 6 );

    //Sjekker rett foran bonde: ledig?
    if((retning == 1 && evFarge == 0) || (retning == -1 && evFarge == 1)){
      //bondeEvalStreng += "X = " + x + " Retning: " + retning + "\n";
      //bondeEvalStreng += "felt " + x + ", " + (y + retning) + "er  tomt: " + feltErTomt(x + retning, y) + "\n";
      if(y + retning >= 0 && y + retning <= 7 &&  feltErTomt(x, y + retning)){
        stilling.leggTilTrekk(evFarge, 1, x, y, x, y + retning);
        bondeEvalStreng += " 1 foran ledig! ";
        if(urort && feltErTomt(x, y + retning * 2)){
          stilling.leggTilTrekk(evFarge, 1, x, y, x, y + retning * 2);
          bondeEvalStreng += " 2 foran ledig! ";
        }
      }

      // Sjekker skrå-trekk venstre (y-1) og skrå høyre(y+1), trekk mulig dersom motstander der:
      if(y + retning >= 0 && y + retning <= 7){
        if(x > 0 && brettet[motstander][x - 1][y + retning] != 0){
          stilling.leggTilTrekk(evFarge, 1, x, y, x - 1, y + retning);
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x - 1][y + retning]]);
        }
        if(x < 7 && brettet[motstander][x + 1 ][y + retning] != 0){
          stilling.leggTilTrekk(evFarge, 1, x, y, x + 1, y + retning);
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + 1][y + retning]]);
        }
      }
    }
    bondeEvalStreng += "\n";
    return bondeEvalStreng;
  }

  private static String grunnSpringerEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String evalStreng = "Eval: Springer " + x + ", " + y + ": ";
    int motstander = (1 - evFarge);
    if(x > 1){
      if(y > 0 && brettet[evFarge][x - 2][y - 1] == 0){
        evalStreng += "x-2, y-1 OK! ";
        stilling.leggTilTrekk(evFarge, 2, x, y, x - 2, y - 1);
        if(brettet[motstander][x - 2][y - 1] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x - 2][y - 1]]);
        }
      }
      if(y < 7 && brettet[evFarge][x - 2][y + 1] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x - 2, y + 1);
        if(brettet[motstander][x - 2][y + 1] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x - 2][y + 1]]);
        }
        evalStreng += "x-2, y+1 OK! ";
      }
    }
    if(x < 6){
      if(y > 0 && brettet[evFarge][x + 2][y - 1] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x + 2, y - 1);
        if(brettet[motstander][x + 2][y - 1] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + 2][y - 1]]);
        }
        evalStreng += "x+2, y-1 OK! ";
      }
      if(y < 7 && brettet[evFarge][x + 2][y + 1] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x + 2, y + 1);
        if(brettet[motstander][x + 2][y + 1] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + 2][y + 1]]);
        }
        evalStreng += "x+2, y+1 OK! ";
      }
    }
    if( y > 1){
      if(x > 0 && brettet[evFarge][x - 1][y - 2] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x - 1, y - 2);
        if(brettet[motstander][x - 1][y - 2] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x - 1][y - 2]]);
        }
        evalStreng += "x-1, y-2 OK! ";
      }
      if(x < 7 && brettet[evFarge][x + 1][y - 2] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x + 1, y - 2);
        if(brettet[motstander][x + 1][y - 2] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + 1][y - 2]]);
        }
        evalStreng += "x+1, y-2 OK! ";
      }

    }
    if( y < 6){
      if(x > 0 && brettet[evFarge][x - 1][y + 2] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x - 1, y + 2);
        if(brettet[motstander][x - 1][y + 2] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x - 1][y + 2]]);
        }
        evalStreng += "x-1, y+2 OK! ";
      }
      if(y < 7 && brettet[evFarge][x + 1][y + 2] == 0){
        stilling.leggTilTrekk(evFarge, 2, x, y, x + 1, y + 2);
        if(brettet[motstander][x + 1][y + 2] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + 1][y + 2]]);
        }
        evalStreng += "x+1, y+2 OK! ";
      }

    }

    return evalStreng + "\n"; }

  private static String grunnLoperEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String evalStreng = "Løper " + x + ", " + y + ": ";
    evalStreng += diagonalEval(x, y, evFarge, 3);
    return evalStreng;
  }
  private static String grunnTaarnEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String evalStreng = "Tårn " + x + ", " + y + ": ";
    evalStreng += rettLinjeEval(x, y, evFarge, 4);
    return evalStreng;
  }

  private static String grunnDronningEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String evalStreng = "Dronning " + x + ", " + y + ": ";
    evalStreng += rettLinjeEval(x, y, evFarge, 5);
    evalStreng += diagonalEval(x, y, evFarge, 5);
    return evalStreng;
   }
  private static String grunnKongeEval(int x, int y, int evFarge, boolean evaluererNesteTrekkFarge){
    String evalStreng = "Konge " + x + ", " + y + ": ";
    int motstander = (1 - evFarge);
    for(int deltaX = -1; deltaX <= 1; deltaX++){
      for(int deltaY = -1; deltaY <= 1; deltaY++){
        if(x + deltaX >= 0 && x + deltaX <= 7 &&
           y + deltaY >= 0 && y + deltaY <= 7 &&
           !(deltaX == 0 && deltaY == 0) &&
           brettet[evFarge][x + deltaX][y + deltaY] == 0){
          stilling.leggTilTrekk(evFarge, 6, x, y, x + deltaX, y + deltaY);
          // LEGG INN TRUSSELBONUS HERFRA OG NEDOVER!!!
          if(brettet[motstander][x + deltaX][y + deltaY] != 0){
            stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          }
        }
      }
      //Rokade sjekk:

      //Lang rokade(med a tårn):
      if(stilling.hentLangRokadeMulig(evFarge)){
        if(feltErTomt(3, y) && feltErTomt(2, y) && feltErTomt(1, y)){
          stilling.leggTilTrekk(evFarge, 6, 4, y, 2, y);
        }
      }
      //Kort rokade(med h tårn):
      if(stilling.hentKortRokadeMulig(evFarge)){
        if(feltErTomt(5, y) && feltErTomt(6, y)){
          stilling.leggTilTrekk(evFarge, 6, 4, y, 6, y);
        }
      }


    }
    return evalStreng; }

  private static String diagonalEval(int x, int y, int evFarge, int brikkeTypeNr){
    String evalStreng = "";
    int motstander = (1 - evFarge);

    // Sjekker oppover mot høyre:
    boolean ferdig = false;
    int deltaX = 1;
    int deltaY = 1;
    while(!ferdig && x + deltaX <= 7 && y + deltaY <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX++;
      deltaY++;
    }
    // Sjekker oppover mot venstre:
    ferdig = false;
    deltaX = -1;
    deltaY = 1;
    while(!ferdig && x + deltaX >= 0 && y + deltaY <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX--;
      deltaY++;
    }
    // Sjekker nedover mot høyre:
    ferdig = false;
    deltaX = 1;
    deltaY = -1;
    while(!ferdig && x + deltaX <= 7 && y + deltaY >= 0){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX++;
      deltaY--;
    }
    // Sjekker nedover mot venstre:
    ferdig = false;
    deltaX = -1;
    deltaY = -1;
    while(!ferdig && x + deltaX >= 0 && y + deltaY >= 0){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX--;
      deltaY--;
    }
    return evalStreng;
  }
  private static String rettLinjeEval(int x, int y, int evFarge, int brikkeTypeNr){
    String evalStreng = "";
    int motstander = (1 - evFarge);
    // Sjekker mot høyre:
    boolean ferdig = false;
    int deltaX = 1;
    int deltaY = 0;
    while(!ferdig && x + deltaX <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX++;
    }
    // Sjekker mot venstre:
    ferdig = false;
    deltaX = -1;
    deltaY = 0;
    while(!ferdig && x + deltaX >= 0){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaX--;
    }
    // Sjekker oppover:
    ferdig = false;
    deltaX = 0;
    deltaY = 1;
    while(!ferdig && y + deltaY <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaY++;
    }
    // Sjekker nedover:
    ferdig = false;
    deltaX = 0;
    deltaY = -1;
    while(!ferdig && y + deltaY >= 0){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        evalStreng += " x + " + deltaX + ", y + " + deltaY +"! ";
        stilling.leggTilTrekk(evFarge, brikkeTypeNr, x, y, x + deltaX, y + deltaY);
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrusselBonus(evFarge, Settinger.BRIKKEVERDIER[brettet[motstander][x + deltaX][y + deltaY]]);
          ferdig = true; }
      }
      else{ ferdig = true; }
      deltaY--;
    }


    return evalStreng; }

  private static boolean feltErTomt(int x, int y){
    return brettet[0][x][y] == 0 && brettet[1][x][y] == 0;
  }





}
