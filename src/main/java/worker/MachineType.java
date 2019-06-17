package worker;

import factory.Material;
import factory.MaterialType;

import java.util.List;

public enum MachineType {

    MECHANICAL_PARTS_MAKER("DMMPM", 500, 5,100,
            List.of(
            new Material(MaterialType.Alloys,0.01),
            new Material(MaterialType.Coltan,0.01),
            new Material(MaterialType.Plastics,0),
            new Material(MaterialType.PreciousMetals,0),
            new Material(MaterialType.Metalloids, 0.001))),
    ELECTRONICS_MAKER("DMEM", 300, 1,300,
            List.of(
            new Material(MaterialType.Alloys,0.01),
            new Material(MaterialType.Coltan,0),
            new Material(MaterialType.Plastics,0.0001),
            new Material(MaterialType.PreciousMetals,0.00003),
            new Material(MaterialType.Metalloids, 5))),
    ACTUATORS_MAKER("DMAM", 400, 1, 200,
            List.of(
            new Material(MaterialType.Alloys,0.05),
            new Material(MaterialType.Coltan,0),
            new Material(MaterialType.Plastics,0.0001),
            new Material(MaterialType.PreciousMetals,0.00001),
            new Material(MaterialType.Metalloids, 0.02))),
    SENSORS_MAKER("DMSM", 300, 1,400,
            List.of(
            new Material(MaterialType.Alloys,0.01),
                new Material(MaterialType.Coltan,0),
                new Material(MaterialType.Plastics,0.0001),
                new Material(MaterialType.PreciousMetals,0.00002),
                new Material(MaterialType.Metalloids, 0.03)));

    private final String name;
    private final int KWOfElectricityPerTick;
    private final int litresOfOilPerTick;
    private final int costPerTick;
    private final static String[] names = new String[]{"DMMPM", "DMEM", "DMAM", "DMSM"};
    private final List<Material> materials;

    MachineType(String name, int kwOfElectricityPerTick, int litresOfOilPerTick, int costPerTick, List<Material> materials) {
        this.name = name;
        KWOfElectricityPerTick = kwOfElectricityPerTick;
        this.litresOfOilPerTick = litresOfOilPerTick;
        this.costPerTick = costPerTick;
        this.materials = materials;
    }

    public int getKWOfElectricityPerTick() {
        return KWOfElectricityPerTick;
    }

    public int getLitresOfOilPerTick() {
        return litresOfOilPerTick;
    }


    public int getCostPerTick() {
        return costPerTick;
    }

    @Override
    public String toString() {
        return name;
    }

    public static String[] getNames() {
        return names;
    }

    public List<Material> getMaterials() {
        return materials;
    }
}
