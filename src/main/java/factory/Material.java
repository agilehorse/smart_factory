package factory;


/**
 *  Represents material used on creation of the robots.
 */
public class Material {

    private double amountInKg;
    private final MaterialType materialType;

    public Material(MaterialType materialType, double count) {
        this.materialType = materialType;
        this.amountInKg = count;
    }

    MaterialType getMaterialType() {
        return materialType;
    }

    double getCount() {
        return amountInKg;
    }

    void setCount(double count) {
        this.amountInKg = count;
    }

    @Override
    public String toString() {
        return amountInKg + "kg of " + materialType;
    }

    double getPrice() {
        return amountInKg*materialType.getPricePerKg();
    }
}
