package worker;

import factory.Material;
import visitor.FactoryVisitor;
import worker.event.Event;

import java.util.List;

public class Machine extends Device {

    private final MachineType machineType;

    public Machine(int lifespan, MachineType machineType) {
        super(lifespan);
        this.machineType = machineType;
    }

    @Override
    public String toString() {
        return machineType.toString();
    }

    public double getMoneyCosts(){
        return machineType.getCostPerTick();
    }

    @Override
    public List<Material> getMaterialConsumption() {
        return this.machineType.getMaterials();
    }

    @Override
    public double getOilConsumption() {
        return this.machineType.getLitresOfOilPerTick();
    }

    @Override
    public double getPowerConsumption() {
        return this.machineType.getKWOfElectricityPerTick();
    }

    @Override
    public void accept(FactoryVisitor v) {
        v.visit(this);
    }

    @Override
    public void update(Event event) {

    }
}
