package worker.Interfaces;

import worker.event.Event;

public interface Observer {

    void update(Event event);
}
