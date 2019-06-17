package report;

/**
 * Factory class for report creation.
 */
public class ReportFactory {

    public static Report createReport(int type, int from, int to){
        switch (type){
            case 1:
                return new OutagesReport(from, to);
            case 2:
                return new EventReport(from, to);
            case 3:
                return new ConsumptionReport(from, to);
            case 4:
                return new FactoryConfigurationReport(from, to);
                default:
                    System.err.println("No such report type!");
                   return null;
        }
    }

}
