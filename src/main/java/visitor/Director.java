package visitor;

import factory.*;
import worker.*;
import worker.Interfaces.Worker;

import java.util.Arrays;
import java.util.logging.Level;


/**
 * Visits factory in the given time and takes tour hierarchically from
 * factory to storage with products to lines and their workers. When he meets
 * people in the factory he greets them. He does not visit products that are
 * not done yet as it would be silly.
 */
public class Director implements FactoryVisitor {

    private final int arrivalTime;

    public Director(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void visit(Factory factory) {
        logger.log(Level.INFO, "Director starting the visit of factory at tick number " +
        factory.getCurrentTick());
        for (String name: factory.getWorkers().keySet()) {
           if(Arrays.stream(HumanType.getNames()).anyMatch(t -> t.equals(name))){
               for (Worker worker: factory.getWorkers().get(name)) {
                   greet(worker);
               }
           }
        }
        logger.log(Level.INFO, "Director checking the storage, pleased to see the storage contains " +
        factory.getStorage().size() + " products");

        for (Product product : factory.getStorage()){
            product.accept(this);
        }

        for (Line line: factory.getLines()) {
            line.accept(this);
        }
        logger.log(Level.INFO, "Director happily finished the visit and he is now getting back to his coffee and his secretary");
    }

    private void greet(Worker worker) {
        logger.log(Level.INFO, "Director greeting worker " + worker.toString());
    }

    public void visit(Line line) {
        logger.log(Level.INFO, "Director visiting the " +
                line.toString());
        if(line.isWaiting()){
            logger.log(Level.WARNING, "Director observing that the line is waiting");
        } else {
            if(line.getCurrentWorker()!=null) {
                logger.log(Level.INFO, "Director observing currently working " + line.getCurrentWorker().toString());
            }
        }
//        for (Product product : line.getProductsToMake()) {
//            product.accept(this);
//        }
        for (Worker worker : line.getLineConfiguration()) {
            worker.accept(this);
        }
        for (String name: line.getWorkers().keySet()) {
            if(Arrays.stream(HumanType.getNames()).anyMatch(t -> t.equals(name))){
                for (Worker worker: line.getWorkers().get(name)) {
                    greet(worker);
                }
            }
        }
    }

    public void visit(Product product) {
        logger.log(Level.INFO, "Director visiting product " + product.getProductType().getName());
    }

    public void visit(Human human) {
        logger.log(Level.INFO, "Director visiting worker " + human.getHumanType().toString());
    }

    public void visit(Machine machine) {
        logger.log(Level.INFO, "Director visiting machine " + machine.toString());
    }

    public void visit(Robot robot) {
        logger.log(Level.INFO, "Director visiting robot " + robot.toString());
    }
}
