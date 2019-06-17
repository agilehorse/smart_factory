package worker.state;

import factory.Factory;
import worker.Interfaces.Context;

public class Ready extends State {

    public Ready(Context context, int lifespan) {
        super(context, lifespan);
        this.setTick(Factory.getInstance().getCurrentTick());
    }

    @Override
    public void changeToNextState() {
        State newState = new Working(context, getLifespan());
        newState.setLife(this.getLife());
        newState.setCrashMoment(this.getCrashMoment());
        context.setState(newState);
    }

    @Override
    public void changeToPreviousState() {

    }

    @Override
    public String toString() {
        return "Ready";
    }
}
