package factory;

import visitor.FactoryVisitor;

/**
 * Interface for all objects in factory that can be visited.
 */
public interface Visitable {

    void accept(FactoryVisitor v);

}
