package worker.state;

import factory.Factory;
import worker.Interfaces.Context;

public class Working extends State {

    Working(Context context, int lifespan) {
        super(context, lifespan);
        this.setTick(Factory.getInstance().getCurrentTick());
    }

    public void changeToNextState() {
        State newState = new Broken(context, getLifespan());
        newState.setLife(0);
        newState.setCrashMoment(this.getCrashMoment());
        context.setState(newState);
    }

    @Override
    public void changeToPreviousState() {
        State newState = new Ready(context, getLifespan());
        newState.setLife(this.getLife());
        newState.setCrashMoment(this.getCrashMoment());
        context.setState(newState);
    }

    @Override
    public String toString() {
        return "Working";
    }
}
