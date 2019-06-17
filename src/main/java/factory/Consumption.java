package factory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tracks consumption of materials - reflected in consumption report.
 */
public class Consumption {

    private double electricityUsed = 0;
    private double oilUsed = 0;
    private double moneyPaid = 0;
    private List<Material> materials;
    private String name;

    public Consumption() {
        this.materials = List.of(
                new Material(MaterialType.Alloys,0),
                new Material(MaterialType.Coltan,0),
                new Material(MaterialType.Plastics,0),
                new Material(MaterialType.PreciousMetals,0),
                new Material(MaterialType.Metalloids, 0));
    }

    public double getElectricityUsed() {
        return electricityUsed;
    }

    public double getOilUsed() {
        return oilUsed;
    }

    public double getMoneyPaid() {
        return moneyPaid;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void addElectricityUsed(double electricityUsed) {
        this.electricityUsed += electricityUsed;
    }

    public void addOilUsed(double oilUsed) {
        this.oilUsed += oilUsed;
    }

    public void addMoneyPaid(double moneyPaid) {
        this.moneyPaid += moneyPaid;
    }


    /**
     * Adds count of each material in the list to the count of each material in the internal list of consumption.
     * @param materials materials to be added
     */
    public void addMaterials(List<Material> materials) {
        for (Material thisMaterial : this.materials) {
            for (Material newMaterial : materials) {
                if (thisMaterial.getMaterialType().equals(newMaterial.getMaterialType())) {
                    thisMaterial.setCount(thisMaterial.getCount() + newMaterial.getCount());
                }
            }
        }
    }

    /**
     * 
     * @param materials materials
     * @param multiplier multiplier
     * @return multiplied materials
     */
    public static List<Material> multiplyMaterials(List<Material> materials, double multiplier) {
        for (Material material : materials) {
            material.setCount(material.getCount()*multiplier);
        }
        return materials;
    }

    @Override
    public String toString() {
        return  "electricity in Kwh " + electricityUsed + " oil in litres " + oilUsed + " money spend " + moneyPaid
                + " materials " +
                materials
                        .stream().map(Material::toString).collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * @see #materials
     * @see #moneyPaid
     * Each material costs money. This methods adds costs of all materials in this consumption to moneyPaid total. 
     */
    public void incrementMoneyByMaterialCosts() {
        for (Material material : materials) {
            moneyPaid += material.getCount() * material.getPrice();
        }
    }
}
