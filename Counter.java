public class Counter extends Subscriber {
    private int count;

    protected Counter() {}

    public Counter(String name, String topicName) {
        super(name, topicName);
        this.count = 0;
    }
    public void update(String message) {
        count++;
    }

    public int getCount() {
        return count;
    }
    public void printSummary() {
        System.out.println("Counter [" + getName() + "] received " + count + " messages.");
    }
}
