public class Init extends UserlandProcess {
    private boolean testMode;
    public Init(boolean testMode) {
        super();
        this.testMode = testMode;
    }

    @Override
    public void main() {
        try {
            if(!testMode) {
                OS.createProcess(new HelloWorld(), OS.Priority.interactive);
                OS.createProcess(new GoodbyeWorld(), OS.Priority.interactive);
            }else {
                System.out.println("We're in test mode");
                OS.createProcess(new PagingTest(), OS.Priority.interactive);
                OS.createProcess(new PagingTest2(), OS.Priority.interactive);
                OS.createProcess(new PagingTest3(), OS.Priority.interactive);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
