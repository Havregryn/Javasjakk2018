import java.util.ArrayList;

public class Settinger{
  public static final int BRETT_BREDDE = 600;
  public static final int RUTE_BREDDE = BRETT_BREDDE / 8;

  // TEGNING AV BRIKKER:
  public static final int BRIKKER_TEGNING_HVIT_Y = 356;
  public static final int BRIKKER_TEGNING_SVART_Y = 156;
  public static final int BRIKKER_TEGNING_BREDDE = 134;
  public static final int BRIKKER_TEGNING_HOYDE = 130;
  public static final int BRIKKER_TEGNING_KONGE_X = -8;
  public static final int BRIKKER_TEGNING_KONGE_BREDDE = 134;
  public static final int BRIKKER_TEGNING_DRONNING_X = 228;
  public static final int BRIKKER_TEGNING_DRONNING_BREDDE = 134;
  public static final int BRIKKER_TEGNING_TAARN_X = 463;
  public static final int BRIKKER_TEGNING_TAARN_BREDDE = 134;
  public static final int BRIKKER_TEGNING_LOPER_X = 700;
  public static final int BRIKKER_TEGNING_LOPER_BREDDE = 134;
  public static final int BRIKKER_TEGNING_SPRINGER_X = 940;
  public static final int BRIKKER_TEGNING_SPRINGER_BREDDE = 134;
  public static final int BRIKKER_TEGNING_BONDE_X = 1175;
  public static final int BRIKKER_TEGNING_BONDE_BREDDE = 134;

  public static final int ANIMER_TREKK_TID = 3000;

  // BRIKKEVERDIER:
  public static final double[] BRIKKEVERDIER = {0.0,
                                             1.0, //  1: Bonde
                                             3.0, //  2: Springer
                                             3.0, //  3: Løper
                                             5.0, //  4: Tårn
                                             9.0, //  5: Dronning
                                             18.0};// 6: Konge

  public static final double EGNE_TREKK_VERDI = 10;
  public static final double MOTST_TREKK_VERDI = 20;
  public static final double EGNE_BRIKKER_VEKTING = 100;
  public static final double MOTST_BRIKKER_VEKTING = 2;
  public static final double TRUSSELBONUS_VEKTING = 10;
  public static final double TRUET_VEKTING = 50;
  public static final double ROKADE_MULIG_BONUS = 10;


  public static final double MITTSPILL_BRIKKESUM_GRENSE = 54; // Brikkesum pr farge
  public static final double SLUTTSPILL_BRIKKESUM_GRENSE = 23;

  public static RatingSett aapningRating = new RatingSett( 10,  // Egne trekk verdi
                                                   5,  // Motst trekk verdi
                                                   100,  // Egne brikker verdi
                                                     2,  // Motst brikker vekting
                                                    5,  // Trusselbonus vekting
                                                    50,  // Truet vekting
                                                    2.5, // Egen defensiv vekting;
                                                     0, // Motst defensiv vekting;
                                                    100, // Rokade mulig bonus
                                                    "Åpning");

  //

  public static RatingSett midtspillRating = new RatingSett( 10,  // Egne trekk verdi
                                                    10,  // Motst trekk verdi
                                                   100,  // Egne brikker verdi
                                                     2,  // Motst brikker vekting
                                                    10,  // Trusselbonus vekting
                                                    50,  // Truet vekting
                                                    0.5, // Egen defensiv vekting;
                                                     0, // Motst defensiv vekting;
                                                     100, // Rokade mulig bonus
                                                     "Midtspill");

  //
  public static RatingSett sluttspillRating = new RatingSett( 10,  // Egne trekk verdi
                                                    50,  // Motst trekk verdi
                                                   100,  // Egne brikker verdi
                                                     2,  // Motst brikker vekting
                                                    30,  // Trusselbonus vekting
                                                    0,  // Truet vekting
                                                     0, // Egen defensiv vekting;
                                                     0, // Motst defensiv vekting;
                                                     100, // Rokade mulig bonus
                                                     "Sluttspill");
  //





}
