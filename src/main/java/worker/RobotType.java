package worker;

import factory.Material;
import factory.MaterialType;

import java.util.List;

public enum RobotType {

    PARTS_ASSEMBLER("DRPA", 500, 5, 300,
            List.of(
                    new Material(MaterialType.Alloys,0),
                    new Material(MaterialType.Coltan,0),
                    new Material(MaterialType.Plastics,0),
                    new Material(MaterialType.PreciousMetals,0),
                    new Material(MaterialType.Metalloids, 0))),
    SOFTWARE_UPLOADER("DRSU", 100, 0, 200,
            List.of(
                    new Material(MaterialType.Alloys,0),
                    new Material(MaterialType.Coltan,0),
                    new Material(MaterialType.Plastics,0),
                    new Material(MaterialType.PreciousMetals,0),
                    new Material(MaterialType.Metalloids, 0))),
    AI_UPLOADER("DRAU", 200, 0, 300,
            List.of(
                    new Material(MaterialType.Alloys,0),
                    new Material(MaterialType.Coltan,0),
                    new Material(MaterialType.Plastics,0),
                    new Material(MaterialType.PreciousMetals,0),
                    new Material(MaterialType.Metalloids, 0)));

    private final String name;
    private final int KWOfElectricityPerTick;
    private final int litresOfOilPerTick;
    private final int costPerTick;
    private final static String[] names = new String[]{"DRPA", "DRSU", "DRAU"};
    private final List<Material> materials;



    RobotType(String name, int KWOfElectricityPerTick, int litresOfOilPerTick, int costPerTick, List<Material> materials) {
        this.name = name;
        this.KWOfElectricityPerTick = KWOfElectricityPerTick;
        this.litresOfOilPerTick = litresOfOilPerTick;
        this.costPerTick = costPerTick;
        this.materials = materials;
    }

    @Override
    public String toString() {
        return name;
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

    public static String[] getNames() {
        return names;
    }

    public List<Material> getMaterials() {
        return materials;
    }
}
