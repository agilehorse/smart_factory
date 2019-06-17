package report;

import factory.Consumption;
import factory.Factory;
import factory.Line;
import factory.Material;
import worker.Device;
import worker.Human;
import worker.HumanType;
import worker.Interfaces.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Creates a report about consumption of resources and materials
 * in the factory during the given interval of ticks. In the report there are
 * individual consumptions of workers as well as sums for the whole lines
 * and the whole factory.
 */
public class ConsumptionReport extends Report {

    public ConsumptionReport(int from, int to) {
        setFrom(from); setTo(to);
        Factory factory = Factory.getInstance();
        report.append(factory.toString());
        report.append("\n ");
//        workers that stayed in factory didn't work anything during these ticks
        Map<String, List<Worker>> workers = factory.getWorkers();
        Set<String> workerTypes = workers.keySet();
        report.append("Idle workers: ");
        for (String type : workerTypes) {
            for (Worker worker : workers.get(type)) {
                report.append("worker ");
                report.append(type);
                report.append(System.identityHashCode(worker));
                report.append(", ");
            }
        }
        report.append("\n ");
//        consumptions for each line
        List<Consumption> lineConsumptions = new ArrayList<>();
        for (Line line : factory.getLines()) {
            Consumption consumption = new Consumption();
            report.append(line.toString());
            report.append("\n   ");
            consumption.setName(line.toString());
            lineConsumptions.add(consumption);
            workers = line.getWorkers();
            workerTypes = workers.keySet();
//            getting consumptions of workers
            for (String type : workerTypes) {
                for (Worker worker : workers.get(type)) {
                    report.append("worker ");
                    report.append(type);
                    report.append(System.identityHashCode(worker));
                    double ticksWorked;
                    if (type.startsWith("H")) {
//                        humans only spends money and materials
                        Human human = (Human) worker;
                        report.append(" money paid: ");
//                        gets ticks worked from-to
                        ticksWorked = human.getTicksWorked().stream()
                                .filter(tick -> tick >= from && tick <= to).count();
                        List<Material> materialsSpent = Consumption.multiplyMaterials(human.getHumanType().getMaterials(), ticksWorked);
                        consumption.addMaterials(materialsSpent);
//                        pay is ticks worked by "tick-ly" pay
                        double money = ticksWorked * human.getHumanType().getPayPerTick();
                        report.append(money);
                        consumption.addMoneyPaid(money);
                        report.append(materialsSpent
                                .stream().map(Material::toString).collect(Collectors.joining(", ")));
                        report.append("\n   ");
                    } else {
                        Device device = (Device) worker;
//                        gets ticks worked from-to
                        ticksWorked = device.getStates().stream()
                                .filter(state -> state.toString().equals("Working")
                                        && state.getTick() >= from && state.getTick() <= to).count();
                        report.append(" electricity in Kwh ");
                        double electricityUsed = ticksWorked * device.getPowerConsumption();
                        consumption.addElectricityUsed(electricityUsed);
                        report.append(electricityUsed);
                        report.append(" oil in litres ");
                        double oilUsed = ticksWorked * device.getOilConsumption();
                        consumption.addOilUsed(oilUsed);
                        report.append(oilUsed);
                        report.append(" money spent ");
                        double moneySpend = ticksWorked * device.getOilConsumption();
                        consumption.addMoneyPaid(moneySpend);
                        report.append(moneySpend);
                        report.append(" materials spent ");
                        List<Material> materialsSpent = Consumption.multiplyMaterials(device.getMaterialConsumption(), ticksWorked);
                        consumption.addMaterials(materialsSpent);
                        report.append(materialsSpent
                                .stream().map(Material::toString).collect(Collectors.joining(", ")));
                        report.append("\n   ");
                    }
                }
            }
            report.append("\n ");
        }
        Consumption factoryConsumption = new Consumption();
        factoryConsumption.setName(factory.toString());
        for (int i = 0; i < factory.getLines().size(); i++) {
//            adds material costs to money total spend for line
            lineConsumptions.get(i).incrementMoneyByMaterialCosts();
            report.append("\n ");
            report.append(lineConsumptions.get(i).getName());
            report.append(" costs: ");
//            consumption output to console is handled by tostring
            report.append(lineConsumptions.get(i).toString());
//            adding each line's consumption to total factory consumption
            factoryConsumption.addMaterials(lineConsumptions.get(i).getMaterials());
            factoryConsumption.addMoneyPaid(lineConsumptions.get(i).getMoneyPaid());
            factoryConsumption.addElectricityUsed(lineConsumptions.get(i).getElectricityUsed());
            factoryConsumption.addOilUsed(lineConsumptions.get(i).getOilUsed());
        }
        factoryConsumption.addMoneyPaid(factory.getRepairersTicksWorked().stream().filter(tick -> tick >= from && tick <= to).collect(Collectors.toList()).size()*HumanType.REPAIRER.getPayPerTick());
        factoryConsumption.incrementMoneyByMaterialCosts();
        report.append("\n ");
        report.append(factoryConsumption.getName());
        report.append(" costs: ");
        report.append(factoryConsumption.toString());
    }

    @Override
    public void printReport() {
        System.out.println("Consumption Report for ticks: " + getFrom() + "-" + getTo());
        System.out.println("____________________________________________");
        System.out.println(report.toString() + "\n");
    }

    @Override
    public void saveToFile(){
        report.append("Consumption Report for ticks: " + getFrom() + "-" + getTo());
        report.append("\n____________________________________________");
        File file = new File("consumption_report.txt");
        try (PrintWriter out = new PrintWriter("consumption_report.txt")) {
            out.println(report.toString());
        } catch (FileNotFoundException e){
            System.err.println("File not found");
        }
    }
}
