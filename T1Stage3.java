import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class T1Stage3 {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java T1Stage3 <configurationFile.txt>");
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

        T1Stage3 stage = new T1Stage3();
        stage.setupSimulator(in);
        stage.runSimulation();
    }

    public void setupSimulator(Scanner in) { // create main objects from hardcoded configuration
        broker = new Broker();
        String component;
        String componentType;
        String componentName;
        String topicName;
        String fileName = null;
        publishers = new ArrayList<>();
        component = in.next();
        componentName = in.next();
        topicName = in.next();
        if (component.equals("publicador")) {
            Publisher publisher = new Publisher(componentName, broker, topicName);
            publishers.add(publisher);
        }

        // creamos al primer Seguidor
        component = in.next();
        componentType = in.next();
        componentName = in.next();
        topicName = in.next();
        fileName = in.next();

        try {
            if (component.equals("suscriptor")) {
                if (componentType.equals("Seguidor")) {
                    Follower follower1 = new Follower(componentName, topicName, new PrintStream(fileName));
                    broker.subscribe(follower1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File: " + fileName);
            System.exit(-1);
        }

        // creamos al segundo Seguidor 
        component = in.next();
        componentType = in.next();
        componentName = in.next();
        topicName = in.next();
        fileName = in.next();

        try {
            if (component.equals("suscriptor")) {
                if (componentType.equals("Seguidor")) {
                    Follower follower2 = new Follower(componentName, topicName, new PrintStream(fileName));
                    broker.subscribe(follower2);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File: " + fileName);
            System.exit(-1);
        }
    }

    public void runSimulation() {
        Scanner inputScanner = new Scanner(System.in);

        while (inputScanner.hasNextLine()) {
            String linea = inputScanner.nextLine();
            Scanner lineaScanner = new Scanner(linea);

            if (!lineaScanner.hasNext()) {
                continue;
            }

            String publisherName = lineaScanner.next();
            StringBuilder mensaje = new StringBuilder();

            while (lineaScanner.hasNext()) {
                mensaje.append(lineaScanner.next()).append(" ");
            }

            Publisher pub = findPublisher(publisherName);

            if (pub != null) {
                pub.publishNewEvent(mensaje.toString().trim());
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
