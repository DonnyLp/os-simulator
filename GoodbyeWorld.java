public class GoodbyeWorld extends UserlandProcess {

  public GoodbyeWorld() {
    super();
  }

  public void main() {
    start();
    while(true) {
      System.out.println("GoodByeWorld");
      try {
        Thread.sleep(50);
        cooperate();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
