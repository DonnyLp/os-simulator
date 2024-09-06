public class HelloWorld extends UserlandProcess {

  public HelloWorld () {
    super();
  }

  public void main() {
    start();
    while(true) {
      System.out.println("HelloWorld");
      try {
        Thread.sleep(50);
        cooperate();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
