package factory;

import worker.HumanType;
import worker.MachineType;
import worker.RobotType;

import java.util.List;

/**
 *  Product types of our robot factory.
 */
public enum ProductType {

    TERMINATOR(
            "Arnold", "I'll be back",
            List.of(
                    MachineType.MECHANICAL_PARTS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.ACTUATORS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.SENSORS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.ELECTRONICS_MAKER.toString(),
                    HumanType.ELECTRONICS_ASSEMBLER.toString(),
                    RobotType.SOFTWARE_UPLOADER.toString(),
                    RobotType.AI_UPLOADER.toString(),
                    HumanType.VALIDATOR.toString()
            ),
            new int[]{4, 1, 4, 1, 2, 1, 2, 1, 1, 2, 1}),
    R2D2("Artoo-Detoo", "Peep bloop peep",
            List.of(
                    MachineType.MECHANICAL_PARTS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.ACTUATORS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.SENSORS_MAKER.toString(),
                    RobotType.PARTS_ASSEMBLER.toString(),
                    MachineType.ELECTRONICS_MAKER.toString(),
                    HumanType.ELECTRONICS_ASSEMBLER.toString(),
                    RobotType.SOFTWARE_UPLOADER.toString(),
                    HumanType.VALIDATOR.toString()
            ),
            new int[]{2, 1, 1, 1, 2, 1, 1, 1, 1, 1}
            );

    private final String name;
    private final String phrase;
    private final List<String> configTemplate;
    private final int[] phasesTicks;

    ProductType(String name, String phrase, List<String> configTemplate, int[] phasesTicks) {
        this.name = name;
        this.phrase = phrase;
        this.configTemplate = configTemplate;
        this.phasesTicks = phasesTicks;
    }


    public String getName() {
        return name;
    }

    public String getPhrase() {
        return phrase;
    }

    public List<String> getLineConfigTemplate() {
        return configTemplate;
    }

    public int[] getPhasesTicks() {
        return phasesTicks;
    }

    @Override
    public String toString() {
        return name;
    }

}
