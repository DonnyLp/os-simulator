public class SleepTest extends UserlandProcess{

    @Override
    public void main() {
            try {
                OS.sleep(50);
                System.out.println("I woke up!!!!!");
                OS.exit();
                cooperate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
