package report;

import factory.Factory;
import factory.Line;
import worker.Interfaces.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Creates a report about the changes in configuration of lines
 * in factory during a given interval.
 */
public class FactoryConfigurationReport extends Report {

    FactoryConfigurationReport(int from, int to) {
        setFrom(from);
        setTo(to);

        Factory factory = Factory.getInstance();
        report.append(factory.toString());
        report.append("\n\n ");
        for (Line line : factory.getLines()) {
            report.append(line.toString());
            report.append("\n");
//            configuration changes is map of tick - product type
            List<Integer> keys = new ArrayList<>(line.getConfigurationChanges().keySet());
//            gets only those keys which are from-to
            keys = keys.stream()
                    .sorted()
                    .filter(integer -> integer >= from && integer <= to).collect(Collectors.toList());
            report.append("\n ");
            if (keys.size() == 0) {
                report.append(" Configuration hasn't changed since the beginning.\n");
                report.append("  Current configuration is: ");
//                current configuration is empty
                if (line.getLineConfiguration().empty()) {
                    report.append("empty in tick ").append(factory.getCurrentTick());
                } else {
                    report.append(line.getLineConfiguration().stream().map(Worker::toString).collect(Collectors.joining(" ")));
                }
                report.append("\n");
            } else {
                for (int key : keys) {
                    report.append(" Workers configured in tick ");
                    report.append(key);
//                    current configuration in tick is get by product type it was making
                    report.append(line.getConfigurationChanges().get(key).getLineConfigTemplate()
                            .stream().reduce("", (str1, str2) -> str1.concat(" " + str2)));
                    report.append(" to make product: ");
                    report.append(line.getConfigurationChanges().get(key).getName());
                    report.append("\n ");
                }
                report.append("\n ");
            }
        }
    }

    @Override
    public void printReport() {
        System.out.println("Factory Configuration Report for ticks: " + getFrom() + "-" + getTo());
        System.out.println("____________________________________________________");
        System.out.println(report.toString() + "\n");
    }

    @Override
    public void saveToFile() {
        report.append("Factory Configuration Report for ticks: " + getFrom() + "-" + getTo());
        report.append("\n____________________________________________________");
        File file = new File("factory_configuration_report.txt");
        try (PrintWriter out = new PrintWriter("factory_configuration_report.txt")) {
            out.println(report.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
    }
}
