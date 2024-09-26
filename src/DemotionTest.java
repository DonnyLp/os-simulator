public class DemotionTest extends UserlandProcess {
    @Override
    public void main() {
        while(true) {
            try {
                System.out.println("Testing demotion");
                cooperate();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}