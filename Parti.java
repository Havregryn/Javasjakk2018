/**
  Instans av denne klassen representerer et parti med sjakk.
*/

import java.util.ArrayList;

class Parti{

  private boolean forsteTrekkHvit;
  private int nesteTrekkFarge, rundeNr = 0;
  private UIMaster uiMaster;
  private StillingReell stilling;
  private boolean[] automatisk;
  private int antStillinger;
  private ArrayList<StillingReell> stillingStack;


  public Parti(int[][][] brettet,
               boolean forsteTrekkHvit,
               UIMaster uiMaster,
               boolean hvitErAuto,
               boolean svartErAuto,
               int antStillinger){
    this.forsteTrekkHvit = forsteTrekkHvit;
    automatisk = new boolean[2];
    automatisk[0] = hvitErAuto;
    automatisk[1] = svartErAuto;
    Evaluator.settAutomatisk(automatisk);
    if(forsteTrekkHvit){ nesteTrekkFarge = 0; } else{ nesteTrekkFarge = 1; }
    this.uiMaster = uiMaster;
    this.antStillinger = antStillinger;
    this.stilling = new StillingReell(brettet, new StillingStatus(), nesteTrekkFarge, antStillinger, this, uiMaster);
    stilling.settOppBrikkerUI();
    stillingStack = new ArrayList<StillingReell>();
  }

  public void startNyRunde(){ rundeNr++; }
  public int hentRundeNr(){ return rundeNr;}


  public StillingReell hentStilling(){ return stilling; }

  public boolean hentAutomatisk(int farge){ return automatisk[farge]; }

}
