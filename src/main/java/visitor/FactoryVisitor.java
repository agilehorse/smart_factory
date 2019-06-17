package visitor;

import factory.Factory;
import factory.Line;
import factory.Product;
import worker.Human;
import worker.Machine;
import worker.Robot;

import java.util.logging.Logger;

/**
 * Visitor interface for Director and Inspector.
 */
public interface FactoryVisitor {

    Logger logger = Logger.getLogger(FactoryVisitor.class.getName());

    void visit(Factory v);

    void visit(Line v);

    void visit(Product v);

    void visit(Human v);

    void visit(Machine machine);

    void visit(Robot v);

}
