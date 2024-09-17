public class Init extends UserlandProcess {
    public Init() {
        super();
    }

    @Override
    public void main() {
        try {
            System.out.println("Booting up....");
            OS.createProcess(new HelloWorld());
            OS.createProcess(new GoodbyeWorld());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
