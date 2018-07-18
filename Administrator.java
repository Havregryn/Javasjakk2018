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
    partiet = new Parti(standardStartBrett(),
                        true,   // Hvit begynner?
                        uiMaster,
                        false,   // Hvit auto?
                        false,  // Svart auto?
                        2); // Dybde, mulige framtidige trekk tre-struktur.
  }

  /**
    Metode som oppretter standard start-brett og returnerer array med dette
  */
  private int[][][] standardStartBrett(){
    //                           A  B  C  D  E  F  G  H
    int brettOppsettHvit[][] = {{0, 0 ,0 ,0 ,0 ,0 ,0 ,0}, //8
                                {0, 0 ,0 ,0 ,0 ,0 ,0 ,0}, //7
                                {0, 0, 0, 0, 0, 0, 0, 0}, //6
                                {0, 0, 0, 0, 0, 0, 0, 0}, //5
                                {0, 0, 0, 0, 0, 0, 0, 0}, //4
                                {0, 0, 0, 0, 0, 0, 0, 0}, //3
                                {1, 1, 1, 1, 1, 1, 1, 1}, //2
                                {4, 2, 3, 5, 6, 3, 2, 4}};//1
    //                            A  B  C  D  E  F  G  H
    int brettOppsettSvart[][] = {{4, 2, 3, 5, 6, 3, 2, 4}, //8
                                 {1, 1, 1, 1, 1, 1, 1, 1}, //7
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //6
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //5
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //4
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //3
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //2
                                 {0, 0, 0, 0, 0, 0, 0, 0}};//1


    int[][][]brettet = new int[2][8][8];
    for(int x = 0; x < 8; x++){
      for(int y = 0; y < 8; y++){
        brettet[0][x][y] = brettOppsettHvit[7-y][x];
        brettet[1][x][y] = brettOppsettSvart[7-y][x];
      }
    }
    return brettet;
  }

  private int[][][] spesialStartBrett(){
    //                           A  B  C  D  E  F  G  H
    int brettOppsettHvit[][] = {{0, 0 ,0 ,0 ,0 ,0 ,0 ,0}, //8
                                {0, 0 ,0 ,0 ,0 ,0 ,0 ,0}, //7
                                {0, 0, 0, 0, 0, 0, 0, 0}, //6
                                {0, 0, 0, 0, 0, 0, 0, 0}, //5
                                {0, 0, 0, 0, 0, 0, 0, 0}, //4
                                {0, 0, 0, 0, 0, 0, 0, 0}, //3
                                {0, 0, 0, 0, 0, 0, 0, 1}, //2
                                {0, 0, 0, 0, 0, 0, 0, 0}};//1
    //                            A  B  C  D  E  F  G  H
    int brettOppsettSvart[][] = {{0, 0, 0, 0, 0, 0, 0, 0}, //8
                                 {1, 0, 0, 0, 0, 0, 0, 0}, //7
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //6
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //5
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //4
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //3
                                 {0, 0, 0, 0, 0, 0, 0, 0}, //2
                                 {0, 0, 0, 0, 0, 0, 0, 0}};//1

    int[][][]brettet = new int[2][8][8];
    for(int x = 0; x < 8; x++){
      for(int y = 0; y < 8; y++){
        brettet[0][x][y] = brettOppsettHvit[7-y][x];
        brettet[1][x][y] = brettOppsettSvart[7-y][x];
      }
    }
    return brettet;
  }
  public Parti hentParti(){ return partiet; }

  public StillingReell hentStilling(){ return partiet.hentStilling(); }
}
