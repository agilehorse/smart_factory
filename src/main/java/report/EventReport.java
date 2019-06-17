package report;

import factory.Factory;
import worker.event.Event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


/**
 * Creates a report about all events that have happened
 * in the factory in the given interval of ticks. The events
 * are grouped by the type of the event.
 */
public class EventReport extends Report {

    private Map<String, List<Event>> groupedEvents;

    EventReport(int from, int to) {
        setFrom(from); setTo(to);

        Factory factory = Factory.getInstance();
//        gets a map of events grouped by "eventType-eventGenerator-eventHandler"
        groupedEvents = new TreeMap<>(
                factory.getEvents().stream()
                .filter(event -> event.getTickCreated() >= from && event.getTickCreated() <= to)
                .sorted(Comparator.comparing(Event::getTickCreated))
                .collect(Collectors.groupingBy(this::getGroupingByKey,
                        Collectors.mapping((Event e) -> e, toList()))));
        String[] list = new String[3];
        for (String str : groupedEvents.keySet()) {
//            breaks grouping key down to the list
            String[] tempList = str.split("\\.");
            if (list[0] == null || !list[0].equals(tempList[0])) {
                list[0] = tempList[0];
                list[1] = tempList[1];
                list[2] = tempList[2];
//                grouped by type
                report.append("\n\n");
                report.append("\nType: ");
                report.append(list[0]);
                report.append("\nSource:  ");
                report.append(list[1]);
                if(!list[2].equals("0")) {
                    report.append("\nHandler:   ");
                    report.append(list[2]);
                }
                report.append("\nTicks:    ");
            } else if (!list[1].equals(tempList[1])) {
                list[1] = tempList[1];
                list[2] = tempList[2];
//                grouped by source
                report.append("\nSource:  ");
                report.append(list[1]);
                if(!list[2].equals("0")) {
                    report.append("\nHandler:   ");
                    report.append(list[2]);
                }
                report.append("\nTicks:    ");
            } else {
//                grouped by handler
                list[2] = tempList[2];
                if(!list[2].equals("0")) {
                    report.append("\nHandler:   ");
                    report.append(list[2]);
                }
                report.append("\nTicks:    ");
            }
            report.append(" ");
            for (Event event : groupedEvents.get(str)) {
                report.append(event.getTickCreated());
                report.append(", ");
            }
        }
    }

    /**
     * Creates a key with which event can be grouped by.
     * @param event - event from which grouping key is extracted
     * @return string grouping key with pattern "eventType-eventGenerator-eventHandler"
     */
    private String getGroupingByKey(Event event) {
        return event.getEventType() + "." + event.getEventGenerator().toString() + ' ' + System.identityHashCode(event.getEventGenerator())
                + "." + ( event.getEventHandler()==null ? "" : event.getEventHandler().toString() + ' ' )
                + System.identityHashCode(event.getEventHandler());
    }
    @Override
    public void printReport() {
        System.out.println("Event Report for ticks: " + getFrom() + "-" + getTo());
        System.out.println("________________________________");
        if (report.toString().equals("")) {
            report.append("Nikdo nedorazil do prace.\nV tovarne se nic nedeje.\nNic se nerozbilo.");
        }
        System.out.println(report.toString() + "\n");
    }

    @Override
    public void saveToFile(){
        report.append("Event Report for ticks: " + getFrom() + "-" + getTo());
        report.append("\n________________________________");
        File file = new File("event_report.txt");
        try (PrintWriter out = new PrintWriter("event_report.txt")) {
            out.println(report.toString());
        } catch (FileNotFoundException e){
            System.err.println("File not found");
        }
    }
}
