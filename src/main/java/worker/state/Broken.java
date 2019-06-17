package worker.state;

import factory.Factory;
import worker.Interfaces.Context;

public class Broken extends State {

    Broken(Context context, int lifespan) {
        super(context, lifespan);
        this.setTick(Factory.getInstance().getCurrentTick());
    }

    @Override
    public void changeToNextState() {
        context.setState(new Maintained(context, getLifespan()));
    }

    @Override
    public void changeToPreviousState() {

    }

    @Override
    public String toString() {
        return "Broken";
    }
}
