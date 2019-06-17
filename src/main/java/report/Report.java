package report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Base class for all the reports.
 */
public class Report {

    private int from;
    private int to;

    StringBuffer report;

    Report(){
    report = new StringBuffer();
    }

    public void printReport(){
        System.out.println(report.toString());
    }

    public void saveToFile(){
        report.append("Test Report for ticks: " + getFrom() + "-" + getTo());
        report.append("\n____________________________________________________");
        File file = new File("test_report.txt");
        try (PrintWriter out = new PrintWriter("test_report.txt")) {
            out.println(report.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
    }


    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
