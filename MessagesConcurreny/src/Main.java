import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Message message = new Message();
        Thread thread1 = new Thread(new Writer(message));
        Thread thread2 = new Thread(new Reader(message));

        thread1.start();
        thread2.start();
    }
}

class Message {
    private String message;
    private boolean empty = true;
    public synchronized String read() {
        while(empty) {
            try {
                wait();
            } catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        empty = true;
        notifyAll();
        return message;
    }

    public synchronized void write(String message) {
        while(!empty) {
            try {
                wait();
            } catch(InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        empty = false;
        notifyAll();
        this.message = message;
    }
}

class Writer implements Runnable {
    private Message message;
    public Writer(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        String[] messages = {
                "Humpty Dumpty sat on a wall",
                "Humpty Dumpty had a great fall",
                "All the king's horses and all the king's men",
                "Couldn't possibly put Humpty together again"
        };

        Random random = new Random(); // creates something random from the random class

        for (int i = 0; i < messages.length; i++) {
            message.write(messages[i]);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        message.write("Finished");
    }
}

class Reader implements Runnable {
    private Message message;
    public Reader(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (String latestMessage = message.read(); !latestMessage.equals("Finished"); latestMessage = message.read()) {
            System.out.println(latestMessage);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}