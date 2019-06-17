package factory;


import org.json.JSONArray;
import org.json.JSONObject;
import visitor.FactoryVisitor;
import worker.*;
import worker.Interfaces.Worker;
import worker.event.Event;

import javax.naming.directory.InvalidAttributeValueException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton representing factory. Contains storage with already done products,
 * pool of repairers, queue with devices sorted by priority, all events that
 * have happened in the factory since the start, workers currently not assigned
 * to a line and lines, where all the work is done.
 */
public class Factory implements Visitable {

    private String name;
    private List<Product> storage;
    public static List<Repairer> repairers;
    private PriorityQueue<Device> brokenDevices;
    private boolean isDone;
    private List<Event> events;
    private static Factory instance;
    private int currentTick;
    private Map<String, List<Worker>> workers;
    private List<Line> lines;

    private Factory() {
        events = new ArrayList<>();
        currentTick = 0;
        workers = new HashMap<>();
        lines = new ArrayList<>();
        storage = new ArrayList<>();
        repairers = new ArrayList<>();
        Comparator<Device> comparator = new BrokenDeviceComparator(this);
        brokenDevices = new PriorityQueue<>(comparator);
    }

    public static Factory getInstance() {
        if (instance == null)
            instance = new Factory();
        return instance;
    }

    /**
     * @param json
     * @throws InvalidAttributeValueException
     * Sets up factory according to the JSON configuration file.
     */
    public void setUp(JSONObject json) throws InvalidAttributeValueException {

        JSONArray products = (JSONArray) json.get("products");
        JSONArray workers = (JSONArray) json.get("workers");

        JSONArray humans = (JSONArray) workers.get(0);
        JSONArray machines = (JSONArray) workers.get(1);
        JSONArray robots = (JSONArray) workers.get(2);

        hirePeople(humans);
        setUpMachines(machines);
        setUpRobots(robots);
        setUpProductionPlan(products);

        System.out.println("Factory successfully configured");
    }

    private void setUpProductionPlan(JSONArray products) {
        for (int i = 0; i < products.length(); i++) {
            Line line = new Line();
            this.getLines().add(line);
            JSONObject o = products.getJSONObject(i);
            String name = o.getString("name");
            int count = o.getInt("number");
            if (name.equals(ProductType.R2D2.getName())) {
                line.setOrder(2);
                Stack<Product> products1 = new Stack<>();
                for (int j = 0; j < count; j++) {
                    products1.push(new Product(ProductType.R2D2));
                }
                line.setProductsToMake(products1);
            }
            if (name.equals(ProductType.TERMINATOR.getName())) {
                line.setOrder(1);
                Stack<Product> products1 = new Stack<>();
                for (int j = 0; j < count; j++) {
                    products1.push(new Product(ProductType.TERMINATOR));
                }
                line.setProductsToMake(products1);
            }
        }
    }

    private void setUpRobots(JSONArray robots) throws InvalidAttributeValueException {
        for (int i = 0; i < robots.length(); i++) {
            JSONObject o = robots.getJSONObject(i);
            String name = o.getString("name");
            int count = o.getInt("number");
            int lifespan = o.getInt("lifespan");
            if (name.equals(RobotType.PARTS_ASSEMBLER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Robot(lifespan, RobotType.PARTS_ASSEMBLER));
                }
            } else if (name.equals(RobotType.SOFTWARE_UPLOADER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Robot(lifespan, RobotType.SOFTWARE_UPLOADER));
                }
            } else if (name.equals(RobotType.AI_UPLOADER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Robot(lifespan, RobotType.AI_UPLOADER));
                }
            } else {
                throw new InvalidAttributeValueException();
            }
        }
    }

    private void setUpMachines(JSONArray machines) throws InvalidAttributeValueException {

        for (int i = 0; i < machines.length(); i++) {
            JSONObject o = machines.getJSONObject(i);
            String name = o.getString("name");
            int count = o.getInt("number");
            int lifespan = o.getInt("lifespan");
            if (name.equals(MachineType.ACTUATORS_MAKER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Machine(lifespan, MachineType.ACTUATORS_MAKER));
                }
            } else if (name.equals(MachineType.ELECTRONICS_MAKER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Machine(lifespan, MachineType.ELECTRONICS_MAKER));
                }
            } else if (name.equals(MachineType.MECHANICAL_PARTS_MAKER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Machine(lifespan, MachineType.MECHANICAL_PARTS_MAKER));
                }
            } else if (name.equals(MachineType.SENSORS_MAKER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Machine(lifespan, MachineType.SENSORS_MAKER));
                }
            } else {
                throw new InvalidAttributeValueException();
            }
        }
    }

    private void hirePeople(JSONArray humans) throws InvalidAttributeValueException {
        for (int i = 0; i < humans.length(); i++) {
            JSONObject o = humans.getJSONObject(i);
            String name = o.getString("name");
            int count = o.getInt("number");
            if (name.equals(HumanType.ELECTRONICS_ASSEMBLER.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Human(HumanType.ELECTRONICS_ASSEMBLER));
                }
            } else if (name.equals(HumanType.REPAIRER.toString())) {
                for (int j = 0; j < count; j++) {
                    repairers.add(new Repairer());
                }
            } else if (name.equals(HumanType.VALIDATOR.toString())) {
                for (int j = 0; j < count; j++) {
                    this.getWorkers().putIfAbsent(name, new LinkedList<>());
                    this.getWorkers().get(name).add(new Human(HumanType.VALIDATOR));
                }
            } else {
                throw new InvalidAttributeValueException();
            }
        }
    }

    /**
     * Represents one working hour in the factory.
     * If lines have not finished working, initiates device reparation by calling {@link #repairDevices()} and calls each line to work.
     */
    public void tick() {
//        checks if all the lines are done
        Line done = null;
        for (Line line : getLines()) {
            if (line.isDone()) {
                done = line;
            }
            this.isDone = line.isDone();
            if (!isDone) break;
        }
        if (!isDone) {
            if (done != null) {
                for (Line working : getLines()) {
                    if (!working.equals(done)) {
                        int productsToMake = working.getProductsToMake().size();
                        Stack<Product> moreProducts = new Stack<>();
                        Stack<Product> workingLineProducts = working.getProductsToMake();
                        for (int i = 0; i < productsToMake / 2; i++) {
                            moreProducts.add(workingLineProducts.pop());
                        }
                        done.setProductsToMake(moreProducts);
                        done.setDone();
                    }
                }
            }
            repairDevices();
//            makes all lines work
            for (Line line : this.getLines()) {
                line.tick();
            }
            this.currentTick++;
        } else {
            System.out.println("The factory finished working");
        }
    }

    /**
     * @see #repairers
     * @see #brokenDevices
     * Is performed during a tick.
     * Each repairer partly repairs one broken device waiting in priority queue.
     * Notifies line waiting for a particular device once it gets repaired.
     */
    private void repairDevices() {
        if (!brokenDevices.isEmpty()) {
            List<Device> devicesBeingRepaired = new ArrayList<>();
            int size = repairers.size() < brokenDevices.size() ? repairers.size() : brokenDevices.size();
            for (int i = 0; i < size; i++) {
//                each available repairer repaires first broken device in queue
                Device repairedDevice = brokenDevices.poll();
                devicesBeingRepaired.add(repairedDevice);
                Repairer repairer = repairers.get(i);
                boolean deviceRepaired = repairer.repair(repairedDevice);
                if (deviceRepaired) {
//               checks if currently repaired device is waited for
                    if (repairedDevice.getLine().isWaiting() && repairedDevice.toString().equals(repairedDevice.getLine().getCurrentWorker().toString())) {
                        repairedDevice.getLine().setCurrentWorker(repairedDevice);
//                        finds event of line waiting which was generated on repaired device breakage
                        Event waitingEvent = getEvents().stream()
                                .filter(filteredEvent -> filteredEvent.getEventGenerator().equals(repairedDevice.getLine())
                                        && !filteredEvent.isCompleted()).collect(Collectors.toList()).get(0);
//                        event handler notifies line that it can start working
                        waitingEvent.setEventHandler(repairedDevice);
                        waitingEvent.notifyObservers();
                    }
                    devicesBeingRepaired.remove(repairedDevice);
                }
            }
//            returns not fully repaired devices to queue
            for (Device dev : devicesBeingRepaired) {
                if (!brokenDevices.contains(dev)) {
                    brokenDevices.add(dev);
                }
            }
        }
    }

    /**
     * @param product is added into storage of finished products if it's done.
     */
    void addProduct(Product product) {
        if (storage == null) {
            storage = new ArrayList<>();
        }
        if (product.isDone()) {
            storage.add(product);
            System.out.println(product.getProductType().getName() + ": " + product.getProductType().getPhrase() + "!");
        } else {
            System.out.println("Product you are trying to add is not yet finished!!!");
        }
    }

    public List<Integer> getRepairersTicksWorked() {
        return repairers.stream().map(Repairer::getTicksWorked)
                .reduce(new ArrayList<>(), (l1, l2)-> {
                    l1.addAll(l2); return l1;
                });
    }

    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    PriorityQueue<Device> getBrokenDevices() {
        return brokenDevices;
    }

    void addBrokenDevice(Device device) {
        Objects.requireNonNull(device);
        brokenDevices.add(device);
    }

    /**
     * @param f10
     * Reconstructs states of the devices in given tick
     * from the events stored in factory.
     */
    public void getStatesOfDevices(int f10) {
        for (Line line : lines) {
            System.out.println();
            System.out.println();
            System.out.println("Devices working on " + line.toString());
            System.out.println("__________________________");
            for (String name : line.getWorkers().keySet()) {
                if (name.startsWith("D")) {
                    for (Worker device : line.getWorkers().get(name)) {
                        Device dev = (Device) device;
                        System.out.print("Device " + dev.toString() + " was in state ");
                        List<Event> devEvents = dev.getMyEvents().stream()
                                .filter(event -> event.getTickCreated() <= f10)
                                .collect(Collectors.toList());
                        int length = devEvents.size();
                        if (length == 0) {
                            System.out.println("READY");
                        } else {
                            Event lastEvent = devEvents.get(length - 1);
                            switch (lastEvent.getEventType()) {
                                case DEVICE_TICK_WORKED:
                                    System.out.println("WORKING");
                                    break;
                                case DEVICE_BROKEN:
                                    System.out.println("BROKEN");
                                    break;
                                case DEVICE_MAINTAINED:
                                    System.out.println("MAINTAINED");
                                    break;
                                case DEVICE_READY:
                                    System.out.println("READY");
                                    break;
                                default:
                                    System.err.println("ERROR");
                            }
                        }
                    }
                }
            }
        }

    }


    public boolean isDone() {
        return isDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void addEvents(List<Event> events) {
        this.events.addAll(events);
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public Map<String, List<Worker>> getWorkers() {
        return workers;
    }

    public List<Product> getStorage() {
        return storage;
    }

    @Override
    public String toString() {
        return name + " factory";
    }
}
