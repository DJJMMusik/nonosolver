package de.djjm.nanosolver.matrix;

import java.util.Arrays;

public class Nonogram {
    private final NanoLine[] horizontalLines;
    private final NanoLine[] verticalLines;

    /**
     * Creates a Nonogram object
     *
     * @param horizontalClues The given clues as Array of the columns with each having a array of the clues from left to right
     * @param verticalClues   The given clues as Array of the columns with each having a array of the clues from up to down
     */
    public Nonogram(Integer[][] horizontalClues, Integer[][] verticalClues) {
        int horizontalLength = horizontalClues.length;
        int verticalLength = verticalClues.length;
        horizontalLines = new NanoLine[horizontalLength];
        for (int i = 0; i < horizontalLines.length; i++) {
            horizontalLines[i] = new NanoLine(verticalLength, Arrays.asList(horizontalClues[i]));
        }
        verticalLines = new NanoLine[verticalLength];
        for (int i = 0; i < verticalLines.length; i++) {
            verticalLines[i] = new NanoLine(horizontalLength, Arrays.asList(verticalClues[i]));
        }
    }

    /**
     * Creates a Nonogram with prefilled information
     * for use pls try to work with a small amount of fields
     *
     * @param horizontalLines The given lines as Array from left to right
     * @param verticalLines  The given lines as Array from up to down
     */
    public Nonogram(NanoLine[] horizontalLines, NanoLine[] verticalLines) {
        this.horizontalLines = horizontalLines;
        this.verticalLines = verticalLines;
    }

    /**
     * do one solve iteration for the nanogram
     *
     * @return true if something changed else false
     */
    public boolean solveStep() {
        boolean horizontalUpdate = solveStep(horizontalLines);
        boolean verticalUpdate = solveStep(verticalLines);
        if (horizontalUpdate || verticalUpdate) {
            // if changes in the solveStep where made synchronize both arrays
            syncMatrix();
            return true;
        }
        return false;
    }

    /**
     * do one solve iteration for a specific direction (horizontal or vertical)
     *
     * @param lines the lines to be solved
     * @return true if something changed else false
     */
    private boolean solveStep(NanoLine[] lines) {
        boolean stepworked = false;
        for (NanoLine line : lines) {
            if (line.isFinished()) {
                continue;
            }
            if (line.solveStep()) {
                stepworked = true;
            }
        }
        return stepworked;
    }

    /**
     * synchronises the vertical and horizontal information
     */
    private void syncMatrix() {
        //sync horizontal into vertical
        syncDirection(horizontalLines, verticalLines);
        //sync vertical into horizontal
        syncDirection(verticalLines, horizontalLines);
    }

    /**
     * synchronises one Matrix to another
     *
     * @param source the Matrix being used as source
     * @param target the Matrix which is synchronized
     */
    private void syncDirection(NanoLine[] source, NanoLine[] target) {
        for (int i = 0; i < source.length; i++) {
            NanoCell[] sourceField = source[i].getFields();
            for (int j = 0; j < target.length; j++) {
                CellStatus sourceStatus = sourceField[j].getStatus();
                CellStatus targetStatus = target[j].getFields()[i].getStatus();
                if (!(sourceStatus.isUnknown() || targetStatus.equals(sourceStatus))) {
                    if ((j == 37 && i == 27)||(i == 37 && j == 27)){
                        System.out.print("Test");
                    }
                    target[j].setFieldStatus(i, sourceStatus);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Nonogram:\n" + "horizontalLines:\n" + String.join("\n", Arrays.stream(horizontalLines).map(Object::toString).toArray(String[]::new)) + "\n" + "verticalLines:\n" + String.join("\n", Arrays.stream(verticalLines).map(Object::toString).toArray(String[]::new));
    }
}
