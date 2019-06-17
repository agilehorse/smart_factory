package worker.state;

import factory.Factory;
import factory.Line;
import worker.Interfaces.Context;

import java.util.Random;

/**
 * Base class for all the possible states of the devices.
 * The basic flow of the states is Ready - Working - Broken - Maintained - Ready
 * though it can also change to previous state eg. Ready - Working - Ready when
 * the device is returned back to the factory from line without having suffered a crash.
 */
public abstract class State {

    private Line line;
    Context context;
    private int lifespan;
    private int life;
    private int crashMoment;
    private int tick;

    State(Context context, int lifespan){
        this.context = context;
        this.lifespan = lifespan;
        this.life = lifespan;
        //The crash will emerge sometime between 90% and 150% of the lifespan. They are unpredictable these machines.
        Random rd = new Random();
        this.crashMoment = Math.round((float) 0.1*lifespan) - rd.nextInt(Math.round((float) 0.6*lifespan));
        this.tick = Factory.getInstance().getCurrentTick();
    }

    public abstract void changeToNextState();

    public abstract void changeToPreviousState();

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife(){
        return life;
    }

    public int getCrashMoment() {
        return crashMoment;
    }

    void setCrashMoment(int i){
        this.crashMoment = i;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    void setTick (int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return tick;
    }
}
