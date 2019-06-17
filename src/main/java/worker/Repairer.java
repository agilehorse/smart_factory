package worker;

import factory.Factory;
import factory.Line;
import factory.Product;
import visitor.FactoryVisitor;
import worker.Interfaces.Observer;
import worker.Interfaces.Worker;
import worker.event.Event;
import worker.event.EventType;
import worker.state.Broken;
import worker.state.Maintained;

import java.util.ArrayList;
import java.util.List;

public class Repairer implements Worker, Observer {

    private final HumanType humanType = HumanType.REPAIRER;
    private Line line = null;
    private List<Integer> ticksWorked;
    private static List<Event> eventsBroken;


    public Repairer() {
        ticksWorked = new ArrayList<>();
        eventsBroken = new ArrayList<>();
    }

    @Override
    public boolean work(Product product) {
        return false;
    }


    @Override
    public void accept(FactoryVisitor v) {

    }

    @Override
    public Line getLine() {
        return this.line;
    }

    @Override
    public void setLine(Line line) {
        throw new RuntimeException("Repairer cannot have a specific line. He works for all");
    }

    public boolean repair(Device device) {
        this.ticksWorked.add(Factory.getInstance().getCurrentTick());
        if(device.getCurrentState().getClass().equals(Broken.class)){
            Event current = null;
            for (Event e: eventsBroken) {
                if(e.getEventGenerator().equals(device)){
                    current = e;
                    break;
                }
            }
            if(current!=null){
                eventsBroken.remove(current);
                current.setEventHandler(this);
                current.setTickCompleted(device.getLine().getFactory().getCurrentTick());
            }
            device.changeToNextState();
            Event event = new Event(device.getCurrentState().getTick(), EventType.DEVICE_MAINTAINED, device);
            device.addToEvents(event);
            device.getLine().getFactory().addEvent(event);

        } else {
            Maintained state = (Maintained) device.getCurrentState();
            state.makeProgress();
            if(state.getProgress() == 100){
                Event event = new Event(device.getCurrentState().getTick(), EventType.DEVICE_READY, device);
                event.setTickCompleted(device.getCurrentState().getTick());
                device.addToEvents(event);
                device.getLine().getFactory().addEvent(event);
                device.changeToNextState();
                return true;
            } else {
                System.out.println("Device " + device.toString() + " on "+ state.getProgress() + "%");
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Repairer";
    }

    public List<Integer> getTicksWorked() {
        return ticksWorked;
    }

    @Override
    public void update(Event event) {
        if(event.getEventType()==EventType.DEVICE_BROKEN){
            if(!eventsBroken.contains(event)){
           eventsBroken.add(event);}
        }
    }
}
