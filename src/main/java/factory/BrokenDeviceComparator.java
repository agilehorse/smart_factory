package factory;

import worker.Device;

import java.util.Comparator;

/**
 * Provides a compare method specifying the priority of broken devices.
 */
public class BrokenDeviceComparator implements Comparator<Device> {

    private final Factory factory;

    BrokenDeviceComparator(Factory factory) {
        this.factory = factory;
    }


    /**
     * Compares two devices according to their priority.
     * @param device1 broken device inside priority queue, or newly broken being inserted into queue
     * @param device2 broken device inside priority queue, or newly broken being inserted into queue
     * @return 1 if device1 has higher priority, -1 if the device2 has higher priority
     * Priority of a device is higher if:
     * 1. its line has higher priority than other device's line
     * 2. its line is waiting for it and at the same time other device's line is not waiting for the other device
     * 3. it broke sooner than the other device
     */
    @Override
    public int compare(Device device1, Device device2) {
//        the device1 has higher priority if it's line order is lesser
        if (device1.getLine().getOrder() < device2.getLine().getOrder()) {
            return 1;
        } else if (device1.getLine().getOrder() > device2.getLine().getOrder()) {
            return -1;
        } else {
//            the device1 has higher priority if the line that it's on is currently waiting and at the same time it's waiting for this type of device
//            and also the device2's line is not waiting and it's not waiting for the device2's type
            if ((device1.getLine().isWaiting() && device1.getLine().getCurrentWorker().toString().equals(device1.toString()))
                    &&
                    !(device2.getLine().isWaiting() && device1.getLine().getCurrentWorker().toString().equals(device2.toString()))) {
                return 1;
            } else if (!(device1.getLine().isWaiting() && device1.getLine().getCurrentWorker().toString().equals(device1.toString()))
                    &&
                    (device2.getLine().isWaiting() && device1.getLine().getCurrentWorker().toString().equals(device2.toString()))) {
                return -1;
            } else {
//                the device1 has higher priority if there is less amount of devices of the same type in the factory than the devices of the device2's type
                if (factory.getWorkers().get(device1.toString()).size() < factory.getWorkers().get(device2.toString()).size()) {
                    return 1;
                } else if (factory.getWorkers().get(device1.toString()).size() > factory.getWorkers().get(device2.toString()).size()) {
                    return -1;
                } else {
//                    device1 has higher priority if it crashed before device2
                    return Integer.compare(device2.getTickCrashed(), device1.getTickCrashed());
                }
            }
        }
    }
}
