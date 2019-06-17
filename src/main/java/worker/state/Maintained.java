package worker.state;

import factory.Factory;
import worker.Interfaces.Context;

public class Maintained extends State {

    private int progress;

    Maintained(Context context, int lifespan) {
        super(context, lifespan);
        progress = 0;
        this.setTick(Factory.getInstance().getCurrentTick());
    }

    public void makeProgress(){
        progress += 20;
    }

    public int getProgress(){
        return progress;
    }

    @Override
    public void changeToNextState() {
        State newState = new Ready(context,  (int) Math.ceil(0.9 * getLifespan()));
        newState.setLife(newState.getLifespan());
        newState.setCrashMoment(this.getCrashMoment());
        context.setState(newState);
    }

    @Override
    public String toString() {
        return "Maintained";
    }

    @Override
    public void changeToPreviousState() {

    }
}
