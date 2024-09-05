public class Idle extends UserlandProcess {
  public void main() {
  }

  @Override
  public void run() {
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
