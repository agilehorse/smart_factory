import factory.Factory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import report.ReportFactory;
import visitor.Director;
import visitor.Inspector;

import javax.naming.directory.InvalidAttributeValueException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Start point of the factory application.
 * NEEDS CONFIGURATION FILE as an argument eg. /config1.txt
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, InvalidAttributeValueException {
        String config;
        if (args.length == 1) {
            config = args[0];
        } else if (args.length == 0) {
            System.err.println("Configuration of factory is needed");
            return;
        } else {
            System.err.println("Too many arguments");
            return;
        }
        InputStream configStream = Factory.class.getResourceAsStream(config);
        if (configStream == null)
            throw new FileNotFoundException("resource not found: " + config);
        JSONObject json = new JSONObject(new JSONTokener(configStream));

        Factory factory = Factory.getInstance();
        factory.setUp(json);
        factory.setName("Robot");
        Director director = null;
        Inspector inspector = null;

        JSONArray visitors = (JSONArray) json.get("visitors");
        for (int i = 0; i < visitors.length(); i++) {
            JSONObject o = visitors.getJSONObject(i);
            String type = o.getString("type");
            int arrival = o.getInt("arrival");
            if (type.equals("director")) {
                director = new Director(arrival);
            } else if (type.equals("inspector")) {
                inspector = new Inspector(arrival);
            }
        }

        if (director == null || inspector == null)
            throw new InvalidAttributeValueException();

        while (!factory.isDone()) {
            factory.tick();
            // debug
            // System.out.println(factory.getCurrentTick());
            if (factory.getCurrentTick() == director.getArrivalTime()) {
                factory.accept(director);
            }
            if (factory.getCurrentTick() == inspector.getArrivalTime()) {
                factory.accept(inspector);
            }
        }

        JSONArray reports = (JSONArray) json.get("reports");
        HashMap<Integer, Integer[]> reportMap = new HashMap<>();
        for (int i = 0; i < reports.length(); i++) {
            JSONObject o = reports.getJSONObject(i);
            int type = o.getInt("type");
            int from = o.getInt("from");
            int to = o.getInt("to");
            ReportFactory.createReport(type, from, to).printReport();
            ReportFactory.createReport(type, from, to).saveToFile();
            reportMap.put(type, new Integer[]{from, to});
        }

        int F10 = (Integer) json.get("devices_states_tick");
        System.out.println("Reconstructing the states of the devices in tick " + F10);
        System.out.println("___________________________________________________________");
        factory.getStatesOfDevices(F10);

    }
}
