package worker;

import factory.Factory;
import factory.Line;
import factory.Product;
import visitor.FactoryVisitor;
import worker.Interfaces.Observer;
import worker.Interfaces.Worker;
import worker.event.Event;
import worker.event.EventType;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents people working in the factory.
 */
public class Human implements Worker, Observer {

    private final HumanType humanType;
    private Line line = null;
    private List<Integer> ticksWorked;

    public Human(HumanType humanType) {
        this.humanType = humanType;
        ticksWorked = new ArrayList<>();
    }

    public HumanType getHumanType() {
        return humanType;
    }

    @Override
    public boolean work(Product product) {
        ticksWorked.add(Factory.getInstance().getCurrentTick());
        product.decreaseTicksInCurrentPhase();
        if(product.isDone()){
             Event event = new Event(line.getFactory().getCurrentTick(), EventType.PRODUCT_MADE, product);
             line.getFactory().addEvent(event);
        }
        return product.currentPhaseIsDone();
    }

    @Override
    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return humanType.toString();
    }

    @Override
    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public List<Integer> getTicksWorked() {
        return ticksWorked;
    }

    @Override
    public void update(Event event) {

    }
}
