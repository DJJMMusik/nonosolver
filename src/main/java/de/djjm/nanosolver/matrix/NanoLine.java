package de.djjm.nanosolver.matrix;

import java.util.*;
import java.util.stream.Collectors;

public class NanoLine {
    private final NanoCell[] fields;
    private final int length;
    private final List<Clue> lineClues;
    private boolean updated;
    private boolean finished;

    public NanoLine(int length, List<Integer> lineClueNumbers) {
        if (lineClueNumbers.isEmpty()) {
            throw new IllegalArgumentException("lineClueNumbers is empty, but is required to hold at least one value." +
                    " If the line should be empty, the List needs to be filled with a single 0");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("There is no NonoLine length specified." +
                    " The length of a NonoLine has to be at least 1");
        }

        this.length = length;
        fields = new NanoCell[length];
        Arrays.setAll(fields, i -> new NanoCell());

        lineClues = new ArrayList<>();
        for (Integer lineClueNumber : lineClueNumbers) {
            lineClues.add(new Clue(lineClueNumber, length - 1));
        }

        updated = true;
        finished = false;
    }

    /**
     *
     */
    public NanoLine(String nanoLine) {
        String[] splittedInfo = nanoLine.split("I");

        char[] cellInfo = splittedInfo[1].trim().toCharArray();
        String[] clueInfos = splittedInfo[2].trim().split(" ");

        if (clueInfos.length == 0) {
            throw new IllegalArgumentException("There is no clue given, but at least one is required." +
                    " If the line should be empty, a single 0 as clue is required");
        }

        length = cellInfo.length;
        if (length == 0) {
            throw new IllegalArgumentException("There is no nonogram specified." +
                    " If you do not want to initialize the cell informations use Nanoline(int, List<Integer)");
        }
        fields = new NanoCell[length];
        for (int i = 0; i < cellInfo.length; i++) {
            fields[i] = new NanoCell(cellInfo[i]);
        }

        lineClues = new ArrayList<>();
        for (String clueInfo : clueInfos) {
            lineClues.add(new Clue(Integer.parseInt(clueInfo), length - 1));
        }

        updated = true;
        finished = false;
    }

    public String toString() {
        return "I" + Arrays.stream(fields).map(Object::toString).collect(Collectors.joining()) + "I" + lineClues.stream().map(Objects::toString).collect(Collectors.joining());
    }

    public int getLength() {
        return length;
    }

    public NanoCell[] getFields() {
        return fields;
    }

    /**
     * does one rotation of solve actions if needed
     *
     * @return false if there is no need to call the solveActions method, else the return value from the solveActions method.
     */
    public boolean solveStep() {
        // already done
        if (finished) {
            return false;
        }
        // no changes from last run
        if (!updated) {
            return false;
        }
        // new action run
        updated = false;
        return solveActions();
    }

    /**
     * does several Actions on the line, resulting in a solve step done
     *
     * @return true if the update logic did change the metrics
     */
    private boolean solveActions() {
        updatePossiblePositions();
        setClueFields();
        setEmptyFields();
        updateFinished();
        return updated;
    }

    /**
     * updates the possible positions for each clue
     */
    private void updatePossiblePositions() {
        calculateLowestPositions();
        calculateHighestPositions();
    }

    /**
     * updates the lowest possible position for each clue
     */
    private void calculateLowestPositions() {
        Clue clue = lineClues.get(0);
        if (!clue.isPlaced()) {
            clue.calculateLowestPosition(null, fields);
        }
        for (int i = 1; i < lineClues.size(); i++) {
            clue = lineClues.get(i);
            if (clue.isPlaced()) continue;
            clue.calculateLowestPosition(lineClues.get(i - 1), fields);
        }
    }

    /**
     * update the highest possible position
     */
    private void calculateHighestPositions() {
        Clue clue = lineClues.get(lineClues.size() - 1);
        if (!clue.isPlaced()) {
            clue.calculateHighestPosition(null, fields);
        }
        for (int i = lineClues.size() - 2; i >= 0; i--) {
            clue = lineClues.get(i);
            if (clue.isPlaced()) continue;
            clue.calculateHighestPosition(lineClues.get(i + 1), fields);
        }
    }

    private void setClueFields() {
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getStatus().isFilled()) {
                continue;
            }
            Clue possibleClue = null;
            for (Clue clue : lineClues) {
                if (!clue.canContain(i)) {
                    continue;
                }
                if (possibleClue != null) {
                    possibleClue = null;
                    break;
                }
                possibleClue = clue;
            }
            if (possibleClue == null) {
                continue;
            }
            if (possibleClue.needsToContainCell(i)) {
                updated = true;
            }
        }
        for (Clue clue : lineClues) {
            if (clue.isPlaced()) {
                return;
            }
            for (int j = clue.getHighestStart(); j <= clue.getLowestEnd(); j++) {
                if (!fields[j].getStatus().isFilled()) {
                    fields[j].setRequired();
                    updated = true;
                }
            }
            clue.checkPlaced();
        }
    }

    private void setEmptyFields() {
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getStatus().isUnknown()) {
                continue;
            }
            boolean isEmpty = true;
            for (Clue lineClue : lineClues) {
                if (lineClue.canContain(i)) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                fields[i].setUnreachable();
                updated = true;
            }
        }
    }

    private void updateFinished() {
        for (NanoCell field : fields) {
            if (field.getStatus().isUnknown()) {
                return;
            }
        }
        finished = true;
    }


    public boolean isFinished() {
        return finished;
    }

    public void setFieldStatus(int i, CellStatus status) {
        CellStatus oldStatus = fields[i].getStatus();
        if (!oldStatus.isUnknown()) {
            throw new IllegalStateException("The state of the NanoField is already set to " + oldStatus);
        }
        if (status.isEmpty()) {
            fields[i].setUnreachable();
        } else if (status.isFilled()) {
            fields[i].setRequired();
        }
        updated = true;
    }
}
