public class HelloWorld extends UserlandProcess {

  public HelloWorld () {
   super();
  }

  public void main() {
    while(true) {
      try {
        System.out.println("HelloWorld");
        cooperate();
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
