import worker.*;
import worker.Interfaces.Worker;

public class Generator {

    private int LIFESPAN = 100;

    public Worker createWorkerByAbbreviation(String abbreviation) {
        if (abbreviation.startsWith("H")) {
            if (abbreviation.equals("HV")) {
                return new Human(HumanType.VALIDATOR);
            } else if (abbreviation.equals("HEA")) {
                return new Human(HumanType.ELECTRONICS_ASSEMBLER);
            } else {
                return new Human(HumanType.REPAIRER);
            }
        } else if (abbreviation.startsWith("DR")) {
            if (abbreviation.equals("DRPA")) {
                return new Robot(LIFESPAN, RobotType.PARTS_ASSEMBLER);
            } else if (abbreviation.equals("DRAU")) {
                return new Robot(LIFESPAN, RobotType.AI_UPLOADER);
            } else {
                return new Robot(LIFESPAN, RobotType.SOFTWARE_UPLOADER);
            }
        } else if (abbreviation.startsWith("DM")) {
            if (abbreviation.equals("DMMPM")) {
                return new Machine(LIFESPAN, MachineType.MECHANICAL_PARTS_MAKER);
            } else if (abbreviation.equals("DMEM")) {
                return new Machine(LIFESPAN, MachineType.ELECTRONICS_MAKER);
            } else if (abbreviation.equals("DMAM")) {
                return new Machine(LIFESPAN, MachineType.ACTUATORS_MAKER);
            } else {
                return new Machine(LIFESPAN, MachineType.SENSORS_MAKER);
            }
        } else {
            throw new RuntimeException("Invalid input");
        }
    }
}
