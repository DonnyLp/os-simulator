import javax.print.attribute.standard.Fidelity;

public class Idle extends UserlandProcess {

  public Idle() {
    super();
  }

  public void main() {
    start();
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
