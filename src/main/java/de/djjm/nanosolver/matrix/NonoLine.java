package de.djjm.nanosolver.matrix;

import java.util.*;
import java.util.stream.Collectors;

public class NonoLine {
    private final NonoCell[] cells;
    private final int length;
    private final List<Clue> lineClues;
    private boolean updated;
    private boolean finished;

    public NonoLine(int length, List<Integer> lineClueNumbers) {
        this.length = length;
        checkNonoLineRequirements(lineClueNumbers);

        cells = new NonoCell[length];
        initializeCells();

        lineClues = new ArrayList<>();
        initializeClues(length, lineClueNumbers);

        updated = true;
        finished = false;
    }

    private void checkNonoLineRequirements(List<Integer> lineClueNumbers) {
        if (lineClueNumbers.isEmpty()) {
            throw new IllegalArgumentException("lineClueNumbers is empty, but is required to hold at least one value." +
                    " If the line should be empty, the List needs to be filled with a single 0");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("There is no NonoLine length specified." +
                    " The length of a NonoLine has to be at least 1");
        }
    }

    private void initializeClues(int length, List<Integer> lineClueNumbers) {
        for (Integer lineClueNumber : lineClueNumbers) {
            lineClues.add(Clue.initializeClue(lineClueNumber, length - 1));
        }
    }

    private void initializeCells() {
        Arrays.setAll(cells, i -> new NonoCell());
    }

    /**
     *
     */
    public NonoLine(String nanoLine) {
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
        cells = new NonoCell[length];
        for (int i = 0; i < cellInfo.length; i++) {
            cells[i] = new NonoCell(cellInfo[i]);
        }

        lineClues = new ArrayList<>();
        for (String clueInfo : clueInfos) {
            lineClues.add(Clue.initializeClue(Integer.parseInt(clueInfo), length - 1));
        }

        updated = true;
        finished = false;
    }

    public int getLength() {
        return length;
    }

    public NonoCell[] getCells() {
        return cells;
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
        Clue previousClue = null;
        for (Clue clue : lineClues) {
            clue.calculateLowestPosition(previousClue, cells);
            previousClue = clue;
        }
    }

    /**
     * update the highest possible position
     */
    private void calculateHighestPositions() {
        Clue clue = lineClues.get(lineClues.size() - 1);
        if (clue.isNotPlaced()) {
            clue.calculateHighestPosition(null, cells);
        }
        for (int i = lineClues.size() - 1 - 1; i >= 0; i--) {
            clue = lineClues.get(i);
            if (clue.isNotPlaced()) {
                clue.calculateHighestPosition(lineClues.get(i + 1), cells);
            }
        }
    }

    private void setClueFields() {
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].getStatus().isFilled()) {
                continue;
            }
            boolean contained = false;
            Clue possibleClue = null;
            for (Clue clue : lineClues) {
                if (!clue.canContain(i)) {
                    continue;
                }
                if (possibleClue != null) {
                    possibleClue = null;
                    break;
                }
                contained = true;
                possibleClue = clue;
            }
            if (!contained) {
                throw new IllegalStateException("In this line there are filled pieces, which are not part of a clue:\n" +
                        "Nonoline: " + this);
            }
            if (possibleClue == null) {
                continue;
            }
            if (possibleClue.needsToContainCell(i)) {
                updated = true;
            }
        }
        for (int i = 0; i < lineClues.size(); i++) {
            Clue clue = lineClues.get(i);
            if (clue.fillKnownRequiredCells(cells)) updated = true;
            if (updated) {
                lineClues.set(i, clue.checkPlaced());
            }
//           TODO 20240313 catch exception and append by informations
        }
    }

    private void setEmptyFields() {
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].getStatus().isUnknown()) {
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
                cells[i].setUnreachable();
                updated = true;
            }
        }
    }

    private void updateFinished() {
        for (NonoCell field : cells) {
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
        CellStatus oldStatus = cells[i].getStatus();
        if (!oldStatus.isUnknown()) {
            throw new IllegalStateException("The state of the NanoField is already set to " + oldStatus);
        }
        if (status.isEmpty()) {
            cells[i].setUnreachable();
        } else if (status.isFilled()) {
            cells[i].setRequired();
        }
        updated = true;
    }

    public String toString() {
        if (finished) {
            return "I" + Arrays.stream(cells).map(Object::toString).collect(Collectors.joining()) + "I" + lineClues.stream().map(Objects::toString).collect(Collectors.joining());
        } else {
            return "-" + Arrays.stream(cells).map(Object::toString).collect(Collectors.joining()) + "-" + lineClues.stream().map(Objects::toString).collect(Collectors.joining());
        }
    }
}
