public class JavaSjakk{

  private static UIMaster uiMaster;
  private static Administrator adm;

  public static void main(String[] args){
    uiMaster = new UIMaster();
    uiMaster.startUI(args);
  }
}
