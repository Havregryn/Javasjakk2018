/**
  Klasse med statiske metoder som evaluerer en stilling og finner mulige trekk.
*/

import java.util.ArrayList;

class Evaluator{

  private static boolean[] automatisk = {false, false};

  public static void settAutomatisk(boolean[] auto){
    automatisk = auto;
  }



  public static boolean trekkGodkjent(Stilling stillingPre, StillingImag stillingPost){
    Trekk t = stillingPost.hentTrekkPre();
    int farge = t.hentFarge();
    boolean godkjent = (!stillingPost.hentStatus().erISjakk[t.hentFarge()]);
    return godkjent;

  }


  public static void finnMuligeTrekk(Stilling stilling){
    int[][][] brettet = stilling.hentBrett();
    int nesteTrekkFarge = stilling.hentNesteTrekkFarge();
    // Evaluerer alltid motstander først for å fange opp sjakk/rokade status.
    // nesteTrfarge = 0 gir evFarge = 1 gir delta = -1, nTf 1 gir delta 1
    for(int evFarge = 1 - nesteTrekkFarge ; evFarge < 2 && evFarge >= 0; evFarge += (nesteTrekkFarge * 2 - 1)){
      for(int y = 0; y < 8; y++){
        for(int x = 0; x < 8; x++){
          int felt = brettet[evFarge][x][y];
          switch(felt){
            case 0 : break;

            case 1 :  grunnBondeEval(stilling, brettet, x, y, evFarge);
                      break;
            case 2 :  grunnSpringerEval(stilling, brettet, x, y, evFarge);
                      break;
            case 3 :  grunnLoperEval(stilling, brettet, x, y, evFarge);
                      break;
            case 4 :  grunnTaarnEval(stilling, brettet, x, y, evFarge);
                      break;
            case 5 :  grunnDronningEval(stilling, brettet, x, y, evFarge);
                      break;
            case 6 :  grunnKongeEval(stilling, brettet, x, y, evFarge);
                      break;
          }
        }
      }
    }
  }

  private static void grunnBondeEval(Stilling stilling, int[][][] brettet,
                                       int x, int y, int evFarge){
    int retning = (evFarge * -2) + 1;
    int motstander = 1 - evFarge;
    int trekkType = 0;
    // urort:True om bonden står på startposisjon:
    boolean urort = (evFarge == 0 && y == 1) || (evFarge == 1 && y == 6 );
    // Er bonden rett før forvandling, altså på neste siste rad?
    if(6 - evFarge * 5 == y){trekkType = 4; } else{ trekkType = 0; }

    //Sjekker rett foran bonde: ledig?
    if((retning == 1 && evFarge == 0) || (retning == -1 && evFarge == 1)){
      if(y + retning >= 0 && y + retning <= 7 &&  feltErTomt(brettet, x, y + retning)){
        stilling.leggTilTrekk(evFarge, 1, trekkType, x, y, x, y + retning);
        if(urort && feltErTomt(brettet, x, y + retning * 2)){
          stilling.leggTilTrekk(evFarge, 1, 0, x, y, x, y + retning * 2);
        }
      }

      // Sjekker skrå-trekk venstre (x-1) og skrå høyre(x+1), trekk mulig dersom motstander der:
      if(y + retning >= 0 && y + retning <= 7){
        if(x > 0 && brettet[motstander][x - 1][y + retning] != 0){
          stilling.leggTilTrekk(evFarge, 1, trekkType + 1, x, y, x - 1, y + retning);
        }
        if(x < 7 && brettet[motstander][x + 1 ][y + retning] != 0){
          stilling.leggTilTrekk(evFarge, 1, trekkType + 1, x, y, x + 1, y + retning);
        }
      }
    }
  }

  private static void grunnSpringerEval(Stilling stilling, int[][][] brettet,
                                          int x, int y, int evFarge){
    int motstander = (1 - evFarge);
    if(x > 1){
      if(y > 0 && brettet[evFarge][x - 2][y - 1] == 0){
        if(brettet[motstander][x - 2][y - 1] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x - 2, y - 1);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x - 2, y - 1);
        }
      }
      if(y < 7 && brettet[evFarge][x - 2][y + 1] == 0){
        if(brettet[motstander][x - 2][y + 1] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x - 2, y + 1);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x - 2, y + 1);
        }
      }
    }
    if(x < 6){
      if(y > 0 && brettet[evFarge][x + 2][y - 1] == 0){
        if(brettet[motstander][x + 2][y - 1] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x + 2, y - 1);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x + 2, y - 1);
        }
      }
      if(y < 7 && brettet[evFarge][x + 2][y + 1] == 0){
        if(brettet[motstander][x + 2][y + 1] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x + 2, y + 1);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x + 2, y + 1);
        }
      }
    }
    if( y > 1){
      if(x > 0 && brettet[evFarge][x - 1][y - 2] == 0){
        if(brettet[motstander][x - 1][y - 2] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x - 1, y - 2);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x - 1, y - 2);
        }
      }
      if(x < 7 && brettet[evFarge][x + 1][y - 2] == 0){
        if(brettet[motstander][x + 1][y - 2] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x + 1, y - 2);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x + 1, y - 2);
        }
      }

    }
    if( y < 6){
      if(x > 0 && brettet[evFarge][x - 1][y + 2] == 0){
        if(brettet[motstander][x - 1][y + 2] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x - 1, y + 2);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x - 1, y + 2);
        }
      }
      if(x < 7 && brettet[evFarge][x + 1][y + 2] == 0){
        if(brettet[motstander][x + 1][y + 2] != 0){
          stilling.leggTilTrekk(evFarge, 2, 1, x, y, x + 1, y + 2);
        }
        else{
          stilling.leggTilTrekk(evFarge, 2, 0, x, y, x + 1, y + 2);
        }
      }

    }
  }

  private static void grunnLoperEval(Stilling stilling, int[][][] brettet,
                                       int x, int y, int evFarge){
    diagonalEval(stilling, brettet, x, y, evFarge, 3);
  }
  private static void grunnTaarnEval(Stilling stilling, int[][][] brettet,
                                       int x, int y, int evFarge){
    rettLinjeEval(stilling, brettet, x, y, evFarge, 4);
  }

  private static void grunnDronningEval(Stilling stilling, int[][][] brettet,
                                          int x, int y, int evFarge){
    rettLinjeEval(stilling, brettet, x, y, evFarge, 5);
    diagonalEval(stilling, brettet, x, y, evFarge, 5);
   }
  private static void grunnKongeEval(Stilling stilling, int[][][] brettet,
                                       int x, int y, int evFarge){
    int motstander = (1 - evFarge);
    for(int deltaX = -1; deltaX <= 1; deltaX++){
      for(int deltaY = -1; deltaY <= 1; deltaY++){
        if(x + deltaX >= 0 && x + deltaX <= 7 &&
           y + deltaY >= 0 && y + deltaY <= 7 &&
           !(deltaX == 0 && deltaY == 0) &&
           brettet[evFarge][x + deltaX][y + deltaY] == 0){
          if(brettet[motstander][x + deltaX][y + deltaY] != 0){
            stilling.leggTilTrekk(evFarge, 6, 1, x, y, x + deltaX, y + deltaY);
          }
          else{
            stilling.leggTilTrekk(evFarge, 6, 0, x, y, x + deltaX, y + deltaY);
          }
        }
      }
    }
    //Rokade sjekk:

    //Lang rokade(med a tårn):
    if(stilling.hentLangRokadeMulig(evFarge)){
      if(feltErTomt(brettet, 3, y) && feltErTomt(brettet, 2, y) && feltErTomt(brettet, 1, y)){
        stilling.leggTilTrekk(evFarge, 6, 2, 4, y, 2, y);
      }
    }
    //Kort rokade(med h tårn):
    if(stilling.hentKortRokadeMulig(evFarge)){
      if(feltErTomt(brettet, 5, y) && feltErTomt(brettet, 6, y)){
        stilling.leggTilTrekk(evFarge, 6, 3, 4, y, 6, y);
      }
    }
  }

  private static void diagonalEval(Stilling stilling, int[][][] brettet, int x, int y,
                                     int evFarge, int brikkeTypeNr){
    int motstander = (1 - evFarge);

    // Sjekker oppover mot høyre:
    boolean ferdig = false;
    int deltaX = 1;
    int deltaY = 1;
    while(!ferdig && x + deltaX <= 7 && y + deltaY <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
      }
      else{ ferdig = true; }
      deltaX--;
      deltaY--;
    }
  }

  private static void rettLinjeEval(Stilling stilling, int[][][] brettet, int x, int y,
                                     int evFarge, int brikkeTypeNr){
    int motstander = (1 - evFarge);
    // Sjekker mot høyre:
    boolean ferdig = false;
    int deltaX = 1;
    int deltaY = 0;
    while(!ferdig && x + deltaX <= 7){
      if(brettet[evFarge][x + deltaX][y + deltaY] == 0){
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
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
        if(brettet[motstander][x + deltaX][y + deltaY] != 0){
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 1, x, y, x + deltaX, y + deltaY);
          ferdig = true;
        }
        else{
          stilling.leggTilTrekk(evFarge, brikkeTypeNr, 0, x, y, x + deltaX, y + deltaY);
        }
      }
      else{ ferdig = true; }
      deltaY--;
    }
  }

  private static boolean feltErTomt(int[][][] brettet, int x, int y){
    return brettet[0][x][y] == 0 && brettet[1][x][y] == 0;
  }





}
