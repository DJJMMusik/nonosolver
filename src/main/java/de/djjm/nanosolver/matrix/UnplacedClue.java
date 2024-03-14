package de.djjm.nanosolver.matrix;


import java.util.Arrays;
import java.util.stream.Collectors;

public class UnplacedClue implements Clue {
    public static final int GO_UP = 1;
    public static final int GO_DOWN = -1;
    public static final int NEEDED_DISTANCE = 2;
    private final int length;
    private int highestEnd;
    private int lowestStart;

    public UnplacedClue(int length, int maxPosition) {
        this.length = length;
        lowestStart = 0;
        highestEnd = maxPosition;
    }

    @Override
    public boolean isNotPlaced() {
        return true;
    }

    public Clue checkPlaced() {
        if(length == highestEnd - lowestStart + 1) return new PlacedClue(length,highestEnd,lowestStart);
        return this;
    }

    @Override
    public void calculateLowestPosition(Clue lowerClue, NonoCell[] fields) {
        //setting lowestStart to the lowest possible position
        if (lowerClue != null) {
            //setting the lowestStart to the first possible position in reference to the lower clue
            lowestStart = Math.max(lowerClue.getLowestEnd() + NEEDED_DISTANCE, lowestStart);//2 because one higher and one empty space between clues
        }
        lowestStart = calculatePossiblePosition(fields, GO_UP, lowestStart);
    }

    @Override
    public void calculateHighestPosition(Clue higherClue, NonoCell[] fields) {
        //setting lowestStart to the lowest possible position
        if (higherClue != null) {
            highestEnd = Math.min(higherClue.getHighestStart() - NEEDED_DISTANCE, highestEnd); //2 because one lower and one empty space between clues
        }
        highestEnd = calculatePossiblePosition(fields, GO_DOWN, highestEnd);
    }

    private int calculatePossiblePosition(NonoCell[] cells, int direction, int startValue) {
        int border = borderFromDirection(cells.length, direction);
        int calculatedValue = startValue;
        CellStatus previousStatus = getStatus(calculatedValue - direction, cells);
        int currentLength = 0;

        for (int checkPosition = calculatedValue; checkPosition * direction < border; checkPosition += direction) {
            CellStatus status = cells[checkPosition].getStatus();
            if (previousStatus.isFilled()) {
                // start not possible, because the position next to it is already used
                previousStatus = status;
                calculatedValue += direction;
                continue;
            }
            if (currentLength == length) {
                //having reached length goal
                if (!(status.isFilled())) {
                    // found possible position
                    return calculatedValue;
                }

                previousStatus = cells[calculatedValue].getStatus();
                calculatedValue += direction;
                continue;
            }
            if (status == CellStatus.EMPTY) {
                //not enough space until now, so reset
                currentLength = 0;
                previousStatus = status;
                calculatedValue = checkPosition + direction;
                continue;
            }
            //still space so continue
            currentLength++;
        }
        checkIfFits(currentLength, cells, direction, startValue, border);
        return calculatedValue;
    }

    private void checkIfFits(int currentLength, NonoCell[] cells, int direction, int startValue, int border) {
        if (currentLength != length) {
            throw new ArrayIndexOutOfBoundsException("Failed while calculation the possible position: \n" +
                    "Direction: " + direction + "\n" +
                    "Start value: " + startValue + "\n" +
                    "Border value: " + border + "\n" +
                    "I" + Arrays.stream(cells).map(Object::toString).collect(Collectors.joining()) + "I\n" +
                    "Clue information: " + length + " " + lowestStart + "-" + highestEnd);
        }
    }

    private static int borderFromDirection(int length, int direction) {
        return switch (direction) {
            case 1 -> length;
            case -1 -> 1;
            default ->
                    throw new IllegalArgumentException("The direction parameter can just hold the values 1 or -1 but is: " + direction);
        };
    }

    public boolean needsToContainCell(int i) {
        int highestEndOld = highestEnd;
        int lowestStartOld = lowestStart;
        highestEnd = Math.min(highestEnd, i + length - 1);
        lowestStart = Math.max(lowestStart, i - length + 1);
        return !(highestEndOld == highestEnd && lowestStartOld == lowestStart);
    }

    public boolean canContain(int i) {
        return i <= highestEnd && i >= lowestStart;
    }

    private CellStatus getStatus(int i, NonoCell[] fields) {
        if ((i == -1) || (i == fields.length)) {
            return CellStatus.EMPTY;
        } else {
            return fields[i].getStatus();
        }
    }

    public int getHighestStart() {
        return highestEnd - length + 1;
    }

    public int getLowestEnd() {
        return lowestStart + length - 1;
    }


    public String toString() {
        return " " + length;
    }

    public boolean fillKnownRequiredCells(NonoCell[] cells) {
        boolean change = false;
        for (int j = highestEnd - length + 1; j <= lowestStart + length - 1; j++) {
            if (cells[j].getStatus().isFilled()) {
                continue;
            }
            try {
                cells[j].setRequired();
            } catch (Exception e) {
                System.out.print("An exception was thrown with the following values: \n" +
                        "Clue: " + this + "\n" +
                        "Start-Position-End" + (highestEnd - length + 1) + " - " + j + " - " + (lowestStart + length - 1));
                throw e;
            }
            change = true;
        }
        return change;
    }
}

