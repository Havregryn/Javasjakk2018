/**
  Instans av denne klassen representerer en overordnet administrator av sjakk-
  spillet. Henter inn spiller prefs, setter i gang et parti.
*/
class Administrator{

  private UIMaster uiMaster;
  private Parti partiet;

  public Administrator(UIMaster uiMaster){
    this.uiMaster = uiMaster;
    //uiMaster.leggInnBrikke(0, 1, 1, 1);
    partiet = new Parti(standardStartBrett(), true, uiMaster);
  }

  /**
    Metode som oppretter standard start-brett og returnerer array med dette
  */
  private int[][][] standardStartBrett(){
    int[][][] brettet = new int[2][8][8];
    for(int x = 0; x < 8; x++){
      for(int y = 0;y < 8; y++){
        if(y == 0){
          brettet[1][x][0] = 0;
          if(x == 0 || x == 7){ brettet[0][x][y] = 4; }
          else if(x == 1 || x == 6){ brettet[0][x][y] = 2; }
          else if(x == 2 || x == 5){ brettet[0][x][y] = 3; }
          else if(x == 3){ brettet[0][x][y] = 5; }
          else if(x == 4){ brettet[0][x][y] = 6; }
        }
        else if(y == 1){ brettet[0][x][1] = 1; brettet[1][x][1] = 0;}
        else if(y > 1 && y < 6){ brettet[0][x][y] = 0; brettet[1][x][y] = 0; }
        else if(y == 6){ brettet[1][x][6] = 1; brettet[0][x][6] = 0;}
        else if(y == 7){
          brettet[0][x][6] = 0;
          if(x == 0 || x == 7){ brettet[1][x][y] = 4; }
          else if(x == 1 || x == 6){ brettet[1][x][y] = 2; }
          else if(x == 2 || x == 5){ brettet[1][x][y] = 3; }
          else if(x == 3){ brettet[1][x][y] = 5; }
          else if(x == 4){ brettet[1][x][y] = 6; }
        }

      }
    }
    return brettet;
  }

  private int[][][] spesialStartBrett(){
    int[][][]brettet = new int[2][8][8];
    brettet[0][0][0] = 6;
    //brettet[1][6][7] = 1;
    return brettet;
  }

  public Stilling hentStilling(){ return partiet.hentStilling(); }
}
