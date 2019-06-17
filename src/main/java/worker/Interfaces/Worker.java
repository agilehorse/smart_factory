package worker.Interfaces;

import exceptions.BrokenDeviceException;
import factory.Line;
import factory.Product;
import factory.Visitable;

/**
 * Base interface for all workers in the factory - all can be visited
 * both by the director and the inspector.
 */
public interface Worker extends Visitable {

    boolean work(Product product) throws BrokenDeviceException;

    Line getLine();

    void setLine(Line line);
}
