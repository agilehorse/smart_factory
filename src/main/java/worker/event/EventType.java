package worker.event;

/**
 * Event type used also in outages report and event report.
 */
public enum EventType {
    LINE_WAITING,
    PRODUCT_MADE,
    DEVICE_BROKEN ,
    DEVICE_MAINTAINED ,
    DEVICE_READY,
    DEVICE_TICK_WORKED
}
