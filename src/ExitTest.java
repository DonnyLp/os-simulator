public class ExitTest extends UserlandProcess{
    @Override
    public void main() {
        try {
            System.out.println("This is a test process for the exit kernel call");
            OS.sleep(50);
            System.out.println("Exiting....");
            OS.exit();
            cooperate();
            Thread.sleep(50);
        }   catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
