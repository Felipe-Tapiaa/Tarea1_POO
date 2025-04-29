import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class T1Stage4 {


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java T1Stage4 <configurationFile.txt>");
            System.exit(-1);
        }

        Scanner in = null;
        try {
            in = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File: " + args[0]);
            System.exit(-1);
        }

        T1Stage4 stage = new T1Stage4();
        stage.setupSimulator(in);
        stage.runSimulation();
    }

    public void setupSimulator(Scanner in) {  // create main objects from hardcoded configuration
        broker = new Broker();
        String component;
        String componentType;
        String componentName;
        String topicName;
        String fileName = null;
        publishers = new ArrayList<>();

        while (in.hasNext()) {
            component = in.next();

            if (component.equals("publicador")) { // it must be a publisher for a well structured config file
                componentName = in.next();
                topicName = in.next();
                Publisher publisher = new Publisher(componentName, broker, topicName);
                publishers.add(publisher);
            } 
            else if (component.equals("suscriptor")) {
                componentType = in.next();
                componentName = in.next();
                topicName = in.next();
                fileName = in.next();

                try {
                    if (componentType.equals("Seguidor")) {
                        Follower follower = new Follower(componentName, topicName, new PrintStream(fileName));
                        broker.subscribe(follower);
                    } 
                    else if (componentType.equals("Registrador")) {
                        Recorder recorder = new Recorder(componentName, topicName, new PrintStream(fileName));
                        broker.subscribe(recorder);
                    } 
                    else if (componentType.equals("Monitor")) {
                        Monitor monitor = new Monitor(componentName, topicName, new PrintStream(fileName));
                        broker.subscribe(monitor);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("File: " + fileName);
                    System.exit(-1);
                }
            }
        }
    }

    public void runSimulation() {
        Scanner inputScanner = new Scanner(System.in);

        while (inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            Scanner lineScanner = new Scanner(line);

            if (!lineScanner.hasNext()) {
                continue;
            }

            String publisherName = lineScanner.next();
            StringBuilder messageBuilder = new StringBuilder();

            while (lineScanner.hasNext()) {
                messageBuilder.append(lineScanner.next()).append(" ");
            }

            Publisher pub = findPublisher(publisherName);

            if (pub != null) {
                pub.publishNewEvent(messageBuilder.toString().trim());
            } else {
                System.out.println("Unknown Publisher");
            }
        }

        inputScanner.close();
    }

    private Publisher findPublisher(String name) {
        for (Publisher p : publishers) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    private Publisher gps;
    private Broker broker;
    private ArrayList<Publisher> publishers;
}
