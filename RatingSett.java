public class RatingSett{

  public double egneTrekkVerdi;
  public double motstTrekkVerdi;
  public double egneBrikkerVekting;
  public double motstBrikkerVekting;
  public double trusselBonusVekting;
  public double truetVekting;
  public double egneOffensivVekting;
  public double motstOffensivVekting;
  public double rokadeMuligBonus;
  private String faseType;

  public RatingSett(double egneTrekkVerdi,
                    double motstTrekkVerdi,
                    double egneBrikkerVekting,
                    double motstBrikkerVekting,
                    double trusselBonusVekting,
                    double truetVekting,
                    double egneOffensivVekting,
                    double motstOffensivVekting,
                    double rokadeMuligBonus,
                    String fasetype){
    this.egneTrekkVerdi = egneTrekkVerdi;
    this.motstTrekkVerdi = motstTrekkVerdi;
    this.egneBrikkerVekting = egneBrikkerVekting;
    this.motstBrikkerVekting = motstBrikkerVekting;
    this.trusselBonusVekting = trusselBonusVekting;
    this.egneOffensivVekting = egneOffensivVekting;
    this.motstOffensivVekting = motstOffensivVekting;
    this.truetVekting = truetVekting;
    this.rokadeMuligBonus = rokadeMuligBonus;
    this.faseType = faseType;
                    }

  @Override
  public String toString(){
    return faseType;
  }
}
