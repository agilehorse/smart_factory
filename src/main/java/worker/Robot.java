package worker;

import factory.Material;
import visitor.FactoryVisitor;
import worker.event.Event;

import java.util.List;

public class Robot extends Device {

    private final RobotType robotType;

    public Robot(int lifespan, RobotType robotType) {
        super(lifespan);
        this.robotType = robotType;
    }

    @Override
    public String toString() {
        return robotType.toString();
    }

    public double getMoneyCosts(){
        return robotType.getCostPerTick();
    }

    @Override
    public List<Material> getMaterialConsumption() {
        return this.robotType.getMaterials();
    }

    @Override
    public double getOilConsumption() {
        return robotType.getLitresOfOilPerTick();
    }

    @Override
    public double getPowerConsumption() {
        return robotType.getKWOfElectricityPerTick();
    }

    @Override
    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    @Override
    public void update(Event event) {

    }
}
