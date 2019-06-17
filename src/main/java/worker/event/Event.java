package worker.event;

import factory.Factory;
import worker.Interfaces.Observable;
import worker.Interfaces.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *  Provides a way to communicate between workers.
 */
public class Event implements Observable {

    private final EventType eventType;
    private final Observer eventGenerator;
    private Observer eventHandler;
    private final int tickCreated;
    private int tickCompleted = 0;
    private List<Observer> subscribers;

    //pomoci subscriberu bychom mohli udelat celou implementaci chodu tovarny jako
    //chain of responsibility, ale rozhodli jsme se nechat jak mame - pokud bychom
    //na kodu meli do budoucna dal stavet asi by se vyplatilo predelat
    public Event(int tickCreated, EventType eventType, Observer eventGenerator) {
        this.tickCreated = tickCreated;
        this.eventType = eventType;
        subscribers = new ArrayList<>();
        switch (eventType) {
            case DEVICE_BROKEN:
                System.out.println("Alert! Device " + eventGenerator.toString() + " got broken!");
                subscribers.addAll(Factory.repairers);
                break;
            case DEVICE_MAINTAINED:
                System.out.println("Event info: Device " + eventGenerator.toString() + " is being maintained.");
                break;
            case DEVICE_READY:
                System.out.println("Event info: Device " + eventGenerator.toString() + " is ready!");
                break;
            case PRODUCT_MADE:
                System.out.println("Event info: Product " + eventGenerator.toString() + " is done.");
                break;
            case LINE_WAITING:
                System.out.println(eventGenerator.toString() + " waiting for workers.");
                break;
        }
        this.eventGenerator = eventGenerator;
        notifyObservers();
    }

    public void setEventHandler(Observer eventHandler) {
        if (this.eventHandler == null) {
            this.eventHandler = eventHandler;
            System.out.println("Event handler has been assigned");
        } else
            System.out.println("The event handler has already been assigned");
    }

    public List<Observer> getSubscribers() {
        return subscribers;
    }

    public int getTickCreated() {
        return tickCreated;
    }

    public int getTickCompleted() {
        return tickCompleted;
    }

    public void setTickCompleted(int tickCompleted) {
        this.tickCompleted = tickCompleted;
    }

    public Observer getEventGenerator() {
        return eventGenerator;
    }

    public Observer getEventHandler() {
        return eventHandler;
    }

    public EventType getEventType() {
        return eventType;
    }

    public boolean isCompleted() {
        return this.eventHandler != null;
    }

    @Override
    public void registerObserver(Observer observer) {
        subscribers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : subscribers) {
            observer.update(this);
        }
    }

    public int getTicksLasting() {
        return tickCompleted-tickCreated;
    }
}
