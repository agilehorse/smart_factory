package factory;


/**
 * Types of materials used in the robot factory.
 */
public enum MaterialType {

    Alloys("Alloys", 10),
    Coltan("Coltan", 100),
    Plastics("Plastics", 10000),
    PreciousMetals("PreciousMetals", 90000),
    Metalloids("Metalloids", 50);


    private final String name;
    private final int pricePerKg;

    MaterialType(String name, int pricePerKg) {
        this.name = name;
        this.pricePerKg = pricePerKg;
    }

    public String getName() {
        return name;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }
}
