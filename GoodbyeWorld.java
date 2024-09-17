public class GoodbyeWorld extends UserlandProcess {

  public GoodbyeWorld() {
    super();
  }

  public void main() {
    while(true) {
      try {
        System.out.println("GoodbyeWorld");
        cooperate();
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
