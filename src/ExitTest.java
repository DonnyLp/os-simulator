public class ExitTest extends UserlandProcess{
    @Override
    public void main() {
        try {
            System.out.println("Testing exit call, but lets sleep first...");
            OS.sleep(50);
            System.out.println("Woke up and now exiting....");
            OS.exit();
            cooperate();
            Thread.sleep(50);
        }   catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
