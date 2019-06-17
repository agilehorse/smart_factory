package worker;

import factory.Material;
import factory.MaterialType;

import java.util.List;

/**
 * Definitions of positions of people working in the factory.
 * Each person has predefined the amount of material that he needs
 * for one hour of working, eg. validator only validates, does not
 * consume ani materials.
 */
public enum  HumanType {

    REPAIRER("HR", 600,
            List.of(
            new Material(MaterialType.Alloys,0.01),
            new Material(MaterialType.Coltan,0.01),
            new Material(MaterialType.Plastics,0.0001),
            new Material(MaterialType.PreciousMetals,0.00001),
            new Material(MaterialType.Metalloids, 0.05))),
    VALIDATOR("HV", 400,
            List.of(
                    new Material(MaterialType.Alloys,0),
                    new Material(MaterialType.Coltan,0),
                    new Material(MaterialType.Plastics,0),
                    new Material(MaterialType.PreciousMetals,0),
                    new Material(MaterialType.Metalloids, 0))),
    ELECTRONICS_ASSEMBLER("HEA", 500,
            List.of(
                    new Material(MaterialType.Alloys,0),
                    new Material(MaterialType.Coltan,0),
                    new Material(MaterialType.Plastics,0.0001),
                    new Material(MaterialType.PreciousMetals,0.0000001),
                    new Material(MaterialType.Metalloids, 0.05)));

    private final String name;
    private final int payPerTick;
    private final static String[] names = new String[]{"HR", "HV", "HEA"};
    private final List<Material> materials;


    HumanType(String name, int payPerTick, List<Material> materials) {
        this.name = name;
        this.payPerTick = payPerTick;
        this.materials = materials;
    }

    @Override
    public String toString() {
        return name;
    }


    public int getPayPerTick() {
        return payPerTick;
    }

    public static String[] getNames() {
        return names;
    }


    public List<Material> getMaterials() {
        return materials;
    }
}
