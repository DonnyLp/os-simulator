public class Init extends UserlandProcess {

  public Init() {
      super();
  }
  @Override
  public void main() {
      start();
      while(true) {
          try {
              OS.startup(new HelloWorld());
              OS.createProcess(new GoodbyeWorld());
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      }
  }
}
