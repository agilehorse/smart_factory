package worker.Interfaces;

import factory.Material;

import java.util.List;

/**
 * API for devices to provide info.
 */
public interface costsAPI {

    int getFunctionality();

    List<Material> getMaterialConsumption();

    double getOilConsumption();

    double getPowerConsumption();

    public double getMoneyCosts();
}
