
import java.util.LinkedList;

class Buffer {
    private LinkedList<Integer> list = new LinkedList<>();
    private int capacity = 5;

    public synchronized void produce(int item)
            throws InterruptedException {
        while (list.size() == capacity) {
            wait();
        }

        list.add(item);
        System.out.println("Produced: " + item);
        notify();
    }

    public synchronized void consume()
            throws InterruptedException {
        while (list.isEmpty()) {
            wait();
        }

        int item = list.removeFirst();
        System.out.println("Consumed: " + item);
        notify();
    }
}

class Producer extends Thread {
    private Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int item = 1;

        try {
            while (true) {
                buffer.produce(item++);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer extends Thread {
    private Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        try {
            while (true) {
                buffer.consume();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProducerProblem
{
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();
    }
}