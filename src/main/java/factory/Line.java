package factory;

import exceptions.BrokenDeviceException;
import exceptions.NotEnoughWorkersException;
import visitor.FactoryVisitor;
import worker.Device;
import worker.Interfaces.Observer;
import worker.Interfaces.Worker;
import worker.event.Event;
import worker.event.EventType;
import worker.state.Ready;

import java.util.*;

/**
 * Represents lines in the factory - can be visited by the Director and
 * can generate event when waiting on a broken device.
 */
public class Line implements Visitable, Observer {

    private Stack<Worker> lineConfiguration;
    private Product currentProduct;
    private Factory factory;
    private boolean waiting = false;
    private Worker currentWorker;
    private Stack<Product> productsToMake;
    private int order;
    private boolean isDone;
    private Map<Integer, ProductType> configurationChanges;
    private ProductType lastProductType;
    private boolean underConfiguration;
    private Map<String, List<Worker>> workers;

    public Line() {
        this.lineConfiguration = new Stack<>();
        this.factory = Factory.getInstance();
        configurationChanges = new HashMap<>();
        workers = new HashMap<>();
    }

    /**
     * Sets up line workers depending on product which is to be made.
     * Workers are added from workers on this line, or which are present in factory.
     * If currently needed worker type is device and it's being repaired, the configuration will be halted and
     * will continue in the following ticks once there are enough workers.
     * @param productType includes info how the line should be configured to make a product of this product type
     * @throws NotEnoughWorkersException if the factory doesn't provide enough workers of each type
     * @see #lineConfiguration
     * @see #workers
     */
    private void setLineConfiguration(ProductType productType) {
        boolean broken = false;
        List<String> workerConfig = productType.getLineConfigTemplate();
        if (underConfiguration) {
            if (lineConfiguration.size() >= workerConfig.size()) {
//           there can't be more workers in line configuration than are actually needed
                lineConfiguration.clear();
                System.out.println("!!!INVALID <" + toString() + "> CONFIGURATION, RESETTING!!!");
            }
//            gets only those workers that haven't been ut into line configuration yet
            workerConfig = workerConfig.subList(0, workerConfig.size() - lineConfiguration.size());
        } else {
            if (lastProductType == null || lastProductType != productType) {
                configurationChanges.put(this.factory.getCurrentTick(), productType);
            }
            lineConfiguration.clear();
        }
        outer:
        for (int i = workerConfig.size() - 1; i > -1; i--) {
            String workerType = workerConfig.get(i);
//            looking for needed workers for configuration in this line's workers
            if (this.workers.containsKey(workerType) && !this.workers.get(workerType).isEmpty()) {
                for (Worker worker : this.workers.get(workerType)) {
//                    needed device to configure is broken
                    if (this.factory.getBrokenDevices().contains(worker)) {
                        broken = true;
                    } else {
                        if (!this.lineConfiguration.contains(worker)) {
                            lineConfiguration.push(worker);
                            continue outer;
                        }
                    }
                }
            }
//            looking for workers in factory workers
            Map<String, List<Worker>> factoryWorkers = factory.getWorkers();
            List<Worker> factoryWorkersOfType = factoryWorkers.get(workerType);
            for (int j = 0; j < factoryWorkersOfType.size(); j++) {
                Worker worker = factoryWorkersOfType.get(j);
                if (!lineConfiguration.contains(worker)) {
                    lineConfiguration.push(worker);
                    worker.setLine(this);
                    if (this.workers.containsKey(worker.toString())) {
                        this.workers.get(worker.toString()).add(factoryWorkers.get(workerType).remove(j));
                    } else {
                        this.workers.put(worker.toString(), new ArrayList<>());
                        this.workers.get(worker.toString()).add(factoryWorkers.get(workerType).remove(j));
                    }
                    continue outer;
                }
            }
//          current needed worker is needed
            if (broken) {
                System.out.println("!!!There are not enough workers of needed type to set up <" + toString() + "> " +
                        "configuration. Configuration of the line will continue later, when needed devices are repaired!!! ");
                underConfiguration = true;
                return;
            } else {
//                shouldn't reach here if the configuration was correct
                throw new NotEnoughWorkersException("!!!UNEXPECTED STATE, NOT ENOUGH WORKERS TO CONFIGURE!!!");
            }
        }
        if (lineConfiguration.size() == productType.getLineConfigTemplate().size()) {
            underConfiguration = false;
            this.currentWorker = lineConfiguration.pop();
            this.lastProductType = productType;
        }
    }

    Stack<Product> getProductsToMake() {
        return productsToMake;
    }

    /**
     * Sets products to make and calls {@link #setLineConfiguration(ProductType)}
     * @param productsToMake - products to make by line
     */
    void setProductsToMake(Stack<Product> productsToMake) {
        if (productsToMake == null || productsToMake.empty()) {
            System.out.println("No products to make on " + toString());
        } else {
            this.productsToMake = productsToMake;
            setLineConfiguration(productsToMake.peek().getProductType());
        }
    }

    /**
     * Represents one working hour in the line.
     *
     * Either marks line as done, if it finished working.
     * or continues line configuration if it's not finished.
     * or if the current needed device is not being repaired
     * continues working on current product and calls {@link #workOnProduct()}}
     *
     * @see #isDone
     * @see #underConfiguration
     * @see #currentProduct
     * @see #currentWorker
     * @see #lineConfiguration

     */
    void tick() {
        if (!isDone) {
            if (this.currentProduct == null && (this.productsToMake == null || this.productsToMake.empty())) {
                this.isDone = true;
                System.out.println("The line " + order + " has no products to make");
            } else if (underConfiguration) {
                Product product = currentProduct == null ? this.productsToMake.peek() : currentProduct;
                setLineConfiguration(product.getProductType());
            } else {
                if (!waiting) {
                    workOnProduct();
                }
            }
        }
    }

    /**
     * Works one tick in certain phase of product making process.
     * If current worker finished a phase of product making process, next worker in line configuration is set as current worker
     * Calls {@link #setLineConfiguration(ProductType)} once currentProduct is finished and sets a next one
     * from productsToMake as currentProduct.
     * If current worker is a device and breaks during working process calls {@link #lookForWorkers(Worker)}
     * to find a substitution, otherwise generates an event that the line is waiting.
     *
     * @see #productsToMake
     * @see #currentWorker
     * @see #currentProduct
     * @see #lineConfiguration
     */
    private void workOnProduct() {
//        pops product from queue if the last was finished
        if (this.currentProduct == null) {
            this.currentProduct = productsToMake.pop();
        }
        try {
            if (currentWorker.work(currentProduct)) {
//                current phase of product making was done
                if (currentWorker.toString().startsWith("D")) {
                    Device dev = (Device) currentWorker;
                    dev.changeToPreviousState();
                }
                if (this.currentProduct.isDone()) {
                    this.factory.addProduct(this.currentProduct);
                    if (!productsToMake.empty()) {
                        setLineConfiguration(this.productsToMake.peek().getProductType());
                    }
                    this.currentProduct = null;
                } else {
                    if (!lineConfiguration.empty())
                        this.currentWorker = lineConfiguration.pop();
                }
            }
        } catch (BrokenDeviceException e) {
            this.factory.addBrokenDevice((Device) this.currentWorker);
            Worker newWorker = lookForWorkers(this.currentWorker);
            if (newWorker != null) {
                this.currentWorker = newWorker;
                workOnProduct();
            } else {
                Event waiting = new Event(factory.getCurrentTick(), EventType.LINE_WAITING, this);
                update(waiting);
            }
        } catch (NullPointerException e) {
            System.out.println("INVALID CONFIGURATION. Current worker not defined");
        }
    }

    /**
     *  Either searches through workers on this line.
     * or workers in the factory.
     * Returns null if no worker was found.
     * @param neededWorker is current device which got broken while working
     * @return worker needed to do the work in current tick.
     * Looks for the same type of worker as neededWorker so that the work on line can continue.
     * @see #workers
     */
    private Worker lookForWorkers(Worker neededWorker) {
//        looking in this line's workers
        if (!this.workers.get(neededWorker.toString()).isEmpty()) {
            for (Worker worker : this.workers.get(neededWorker.toString())) {
                if (!neededWorker.equals(worker)) {
                    return worker;
                }
            }
        }
//        looking in factory workers
        List<Worker> factoryWorkers = this.factory.getWorkers().get(neededWorker.toString());
        if (!factoryWorkers.isEmpty()) {
            Worker newWorker = factoryWorkers.remove(0);
            newWorker.setLine(this);
            this.workers.get(newWorker.toString()).add(newWorker);
            return newWorker;
        } else {
            return null;
        }
    }


    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return order == line.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }

    public boolean isWaiting() {
        return waiting;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public Stack<Worker> getLineConfiguration() {
        return lineConfiguration;
    }

    @Override
    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    void setCurrentWorker(Worker currentWorker) {
        this.currentWorker = currentWorker;
    }

    boolean isDone() {
        return isDone;
    }

    public Factory getFactory() {
        return factory;
    }

    public Map<Integer, ProductType> getConfigurationChanges() {
        return configurationChanges;
    }


    public Map<String, List<Worker>> getWorkers() {
        return workers;
    }

    void setDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "Line " + order;
    }

    /**
     * Updates waiting state of the line
     * @param event if the handler is set, sets waiting to false, otherwise to true.
     * @see #waiting
     */
    @Override
    public void update(Event event) {
        if (waiting) {
            if (event.getEventHandler().equals(currentWorker)) {

                event.setTickCompleted(factory.getCurrentTick());
                this.waiting = false;
            } else {
                System.out.println("INVALID EVENT PROVIDED FOR LINE UPDATE");
            }
        } else {
            this.waiting = true;
            factory.addEvent(event);
            event.registerObserver(this);
        }
    }
}
