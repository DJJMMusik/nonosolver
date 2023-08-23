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

    public String toString() {
        return "*" + Arrays.stream(fields).map(Object::toString).collect(Collectors.joining()) + "*" + lineClues.stream().map(Objects::toString).collect(Collectors.joining());
    }

    public int getLength() {
        return length;
    }

    public NanoCell[] getFields() {
        return fields;
    }

    public boolean solveStep() {
        if (finished) {
            return false;
        }
        if (!updated) {
            return false;
        }
        updated = false;
        return solve();
    }

    /**
     * @return true if the update logic did change the metrics
     */
    private boolean solve() {
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
            if(!fields[i].getStatus().isFilled()){
                continue;
            }
            Clue possibleClue = null;
            for (Clue clue : lineClues) {
                if (!clue.canContain(i)){
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
            if(possibleClue.needsToContainCell(i)){
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

    private boolean updateFinished() {
        for (NanoCell field : fields) {
            if (field.getStatus().isUnknown()) {
                return false;
            }
        }
        finished = true;
        return true;
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
