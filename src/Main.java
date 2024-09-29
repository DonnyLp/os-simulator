public class Main {
  public static void main(String [] args) {
    try {
      OS.startup(new Init(true));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
