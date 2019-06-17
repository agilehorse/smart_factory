package visitor;

import factory.Factory;
import factory.Line;
import factory.Product;
import worker.*;
import worker.Interfaces.Worker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.stream.Stream;


/**
 * Visits all devices in factory and on lines according to their current functionality.
 * More worn out devices are visited first. Functionality == life percentage in these terms,
 * can be a negative number when it works longer than expected.
 */
public class Inspector implements FactoryVisitor {

    private final int arrivalTime;

    public Inspector(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }


    public void visit(Factory factory) {
        logger.log(Level.INFO, "Inspector starting the visit of factory at tick number " +
                factory.getCurrentTick());
        String[] machines_robots_names = Stream.concat(Arrays.stream(MachineType.getNames()),
                Arrays.stream(RobotType.getNames()))
                .toArray(String[]::new);
        FunctionalityDeviceComparator comp = new FunctionalityDeviceComparator();
        PriorityQueue<Device> devices = new PriorityQueue<>(comp);
        for (String name : factory.getWorkers().keySet()) {
            if (Arrays.stream(machines_robots_names).anyMatch(t -> t.equals(name))) {
                for (Worker w : factory.getWorkers().get(name)) {
                    devices.add((Device) w);
                }
            }
        }
        for(Line line: factory.getLines()){
        for (String name : line.getWorkers().keySet()) {
            if (Arrays.stream(machines_robots_names).anyMatch(t -> t.equals(name))) {
                for (Worker w : line.getWorkers().get(name)) {
                    devices.add((Device) w);
                }
            }
        }}
        while (!devices.isEmpty()){
            if(devices.peek().toString().startsWith("DR")){
                visit((Robot) devices.poll());
            } else if(devices.peek().toString().startsWith("DM")){
                visit((Machine) devices.poll());
            }
        }

        logger.log(Level.INFO, "Frowny inspector slowly walks away " +
                "from the factory as he is writing down some mysterious notes in his notebook");

    }

    public void visit(Machine machine) {
        logger.log(Level.INFO, "Inspector visiting machine " + machine.toString()
                + " with functionality of " + machine.getFunctionality() + "%");
    }

    public void visit(Robot robot) {
        logger.log(Level.INFO, "Inspector visiting robot " + robot.toString()
                + " with functionality of " + robot.getFunctionality() + "%");

    }

    class FunctionalityDeviceComparator implements Comparator<Device> {

        /*
        * Inspector inspects the devices with lower functionality at first.
        * */
        @Override
        public int compare(Device o1, Device o2) {
            return o1.getFunctionality() - o2.getFunctionality() ;
        }
    }

    public void visit(Line line) {
    }

    @Override
    public void visit(Product v) {

    }

    @Override
    public void visit(Human v) {

    }
}