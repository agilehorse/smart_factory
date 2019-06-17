package worker;

import exceptions.BrokenDeviceException;
import factory.Line;
import factory.Product;
import worker.Interfaces.Context;
import worker.Interfaces.Observer;
import worker.Interfaces.Worker;
import worker.Interfaces.costsAPI;
import worker.event.Event;
import worker.event.EventType;
import worker.state.Ready;
import worker.state.State;

import java.util.ArrayList;
import java.util.List;


/**
 * Base class for Machines and Robots. Represents the context in which the states are
 * changing, generates event when it gets broken.
 * Every device has its initial lifespan which is given in the configuration.
 */
public abstract class Device implements Context, Worker, costsAPI, Observer {

    private State currentState;
    private int tickCrashed;
    private List<State> states;
    private Line line;
    private List<Event> myEvents;

    public Device(int lifespan) {
        this.currentState = new Ready(this, lifespan);
        states = new ArrayList<>();
        myEvents = new ArrayList<>();
    }

    void changeToNextState(){
        currentState.setLine(line);
        states.add(currentState);
        currentState.changeToNextState();
    }

    public void changeToPreviousState(){
        currentState.setLine(line);
        states.add(currentState);
        currentState.changeToPreviousState();
    }

    State getCurrentState() {
        return currentState;
    }

    public void setState(State state) {
        currentState.setLine(line);
        states.add(currentState);
        currentState = state;
    }

    private void setTickCrashed() {
        this.tickCrashed = line.getFactory().getCurrentTick();
    }

    public int getTickCrashed() {
        return tickCrashed;
    }

    /**
     * @param product
     * @return boolean whether the current phase of the product is done and next worker can
     * start working or not yet
     * @throws BrokenDeviceException
     * Work on product method.
     * If the device is in state Ready it is first assigned to a line and switches state to Working
     * and starts working. Each tick worked decrements the life of the device until it gets to its
     * crash moment and it crashes and BrokenDeviceExcepetion is thrown.
     */
    public boolean work(Product product) throws BrokenDeviceException {
        if (currentState.getClass().equals(Ready.class)) {
            currentState.setLine(line);
            states.add(currentState);
            currentState.changeToNextState();
        }
        if (currentState.getLife() == currentState.getCrashMoment()) {
            currentState.setLine(line);
            states.add(currentState);
            currentState.changeToNextState();
            setTickCrashed();
            Event alert = new Event(currentState.getTick(), EventType.DEVICE_BROKEN, this);
            myEvents.add(alert);
            line.getFactory().addEvent(alert);
            throw new BrokenDeviceException();
        }
        Event worked = new Event(currentState.getTick(), EventType.DEVICE_TICK_WORKED, this);
        myEvents.add(worked);
        worked.setTickCompleted(currentState.getTick());
        line.getFactory().addEvent(worked);
        currentState.setLife(currentState.getLife() - 1);
        product.decreaseTicksInCurrentPhase();
        if(product.isDone()){
            Event done = new Event(line.getFactory().getCurrentTick(), EventType.PRODUCT_MADE, product);
            line.getFactory().addEvent(done);
        }
        return product.currentPhaseIsDone();
    }

    private int getLife() {
        return this.currentState.getLife();
    }

    private int getLifeSpan() {return this.currentState.getLifespan();}

    @Override
    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @Override
    public int getFunctionality() {
        float fun = (float) getLife()/ (float) getLifeSpan();
        fun *=100;
        return Math.round(fun);
    }

    public List<State> getStates() {
        return states;
    }

    void addToEvents(Event event){
        myEvents.add(event);
    }

    public List<Event> getMyEvents(){
        return myEvents;
    }
}
