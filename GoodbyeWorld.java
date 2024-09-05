public class GoodbyeWorld extends UserlandProcess {
  public void main() {

  }

  @Override
  public void run() {
    while(true) {
      try {
        cooperate();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("HelloWorld");
    }
  }
}
