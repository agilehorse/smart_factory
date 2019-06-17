package factory;

import visitor.FactoryVisitor;
import worker.Interfaces.Observer;
import worker.event.Event;

/**
 * Represents products made in the factory. Product can be visited by Inspector
 * and can generate event when it is done. As defined in ProductType, each phase
 * of product creation takes certain time.
 */
public class Product implements Visitable, Observer {

    private final ProductType productType;
    private int[] phasesTicks;
    private int currentPhase;

    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    public Product(ProductType productType) {
        this.productType = productType;
        int[] ints  = productType.getPhasesTicks();
        this.phasesTicks = new int[ints.length];
        System.arraycopy(ints, 0, phasesTicks, 0, ints.length);
    }

    public void decreaseTicksInCurrentPhase() {
        this.phasesTicks[currentPhase]--;
    }

    public boolean currentPhaseIsDone() {
        if (this.phasesTicks[currentPhase] == 0) {
            currentPhase++;
            return true;
        }
        return false;
    }

    public boolean isDone() {
        return this.phasesTicks[this.phasesTicks.length - 1] == 0;
    }

    int[] getPhasesTicks() {
        return phasesTicks;
    }

    public ProductType getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        return productType.getName();
    }

    @Override
    public void update(Event event) {

    }
}
