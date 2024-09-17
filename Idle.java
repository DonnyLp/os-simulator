public class Idle extends UserlandProcess {

  public Idle() {
    super();
  }

  public void main() {
    while(true) {
      try {
        cooperate();
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

