public class SleepTest extends UserlandProcess{

    @Override
    public void main() {
            try {
                System.out.println("Going to sleep......");
                OS.sleep(50);
                System.out.println("I woke up!!!!!");
                cooperate();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
