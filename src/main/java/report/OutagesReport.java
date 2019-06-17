package report;

import factory.Factory;
import worker.Interfaces.Observer;
import worker.event.Event;
import worker.event.EventType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 *  Creates a report about the longest, shortest and average outage,
 *  average time that the broken device must wait before the repairer
 *  is available and sourcess of outages sorted by the length of
 *  the outage.
 */
public class OutagesReport extends Report {

    OutagesReport(int from, int to) {
        setFrom(from); setTo(to);

        Factory factory = Factory.getInstance();
        //gets events from-to
        List<Event> events = factory.getEvents().stream()
                .filter(e -> e.getTickCreated() >= from
                        && e.getTickCompleted() <= to)
                .collect(Collectors.toList());
        //gets line outage events sorted by length of line waiting
        List<Event> lineOutages = events.stream().filter(e-> e.getEventType() == EventType.LINE_WAITING)
                .sorted(Comparator.comparing(Event::getTicksLasting)).collect(toList());
        if(!lineOutages.isEmpty()) {
            report.append("Longest outage: ");
            report.append(lineOutages.get(lineOutages.size() - 1).getTicksLasting());
            report.append("\n");
            report.append("Shortest outage: ");
            report.append(lineOutages.get(0).getTicksLasting());
            report.append("\n");
            //average outage in the best case scenario is 6 ticks (5 ticks of reparation)
            report.append("Average outage: ");
            report.append(lineOutages.stream().mapToInt(Event::getTicksLasting).sum() / lineOutages.size());
            report.append("\n");
            report.append("Average wait time for repairer: ");
            //gets device broken events
            List<Event> waitTime = events.stream().filter(event -> event.getEventType() == EventType.DEVICE_BROKEN).collect(toList());
            report.append(waitTime.stream().mapToDouble(Event::getTicksLasting).sum() / waitTime.size());
            report.append("\n");
            //sources of outages sorted by the length of the outage
            report.append("Sources of outages: \n");
            List<Observer> sources = lineOutages.stream()
                    //ticks lasting == length of the outage
                    .sorted(Comparator.comparing(Event::getTicksLasting))
                    //eventHandler is the same type as the device that caused the outage
                    .map(Event::getEventHandler).collect(toList());
            for(Observer o : sources){
                report.append(o.toString() + "\n");
            }
        }
    }

    @Override
    public void printReport() {
        System.out.println("Outages Report for ticks: " + getFrom() + "-" + getTo());
        System.out.println("______________________________________");
        System.out.println(report.toString() + "\n");
    }

    @Override
    public void saveToFile(){
        report.append("Outages Report for ticks: " + getFrom() + "-" + getTo());
        report.append("\n______________________________________");
        File file = new File("outages_report.txt");
        try (PrintWriter out = new PrintWriter("outages_report.txt")) {
            out.println(report.toString());
        } catch (FileNotFoundException e){
            System.err.println("File not found");
        }
    }
}
