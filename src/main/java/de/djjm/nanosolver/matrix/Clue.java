package de.djjm.nanosolver.matrix;


import java.util.Arrays;
import java.util.stream.Collectors;

public class Clue {
    private final int length;
    private boolean placed;
    private int highestEnd;
    private int lowestStart;

    public Clue(int length, int maxPosition) {
        this.length = length;
        if (length == 0) {
            lowestStart = -1;
            highestEnd = -1;
            placed = true;
        } else {
            lowestStart = 0;
            highestEnd = maxPosition;
            placed = false;
        }
    }

    public boolean isPlaced() {
        return placed;
    }

    public void checkPlaced() {
        placed = (length == highestEnd - lowestStart + 1);
    }

    public void calculateLowestPosition(Clue lowerClue, NanoCell[] fields) {
        final int GO_UP = 1;
        //setting lowestStart to the lowest possible position
        if (lowerClue != null) {
            //setting the lowestStart to the first possible position in reference to the lower clue
            lowestStart = Math.max(lowerClue.getLowestEnd() + 2, lowestStart);//2 because one higher and one empty space between clues
        }
        lowestStart = calculatePossiblePosition(fields, GO_UP, lowestStart);
    }

    public void calculateHighestPosition(Clue higherClue, NanoCell[] fields) {
        final int GO_DOWN = -1;
        //setting lowestStart to the lowest possible position
        if (higherClue != null) {
            highestEnd = Math.min(higherClue.getHighestStart() - 2, highestEnd); //2 because one lower and one empty space between clues
        }
        highestEnd = calculatePossiblePosition(fields, GO_DOWN, highestEnd);
    }

    public int calculatePossiblePosition(NanoCell[] cells, int direction, int startValue) {
        int border;

        if (direction == 1) {
            border = cells.length;
        } else if (direction == -1) {
            border = 1;
        } else {
            throw new IllegalArgumentException("The direction parameter can just hold the values 1 or -1 but is: " + direction);
        }

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
        if (currentLength != length) {
            throw new ArrayIndexOutOfBoundsException("Failed while calculation the possible position: \n" +
                    "Direction: " + direction + "\n" +
                    "Start value: " + startValue + "\n" +
                    "Border value: " + border + "\n" +
                    "I" + Arrays.stream(cells).map(Object::toString).collect(Collectors.joining()) + "I\n" +
                    "Clue information: " + length + " " + lowestStart + "-" + highestEnd);
        }
        return calculatedValue;
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

    private CellStatus getStatus(int i, NanoCell[] fields) {
        if ((i == -1) || (i == fields.length)) {
            return CellStatus.EMPTY;
        } else {
            return fields[i].getStatus();
        }
    }

    public int getHighestEnd() {
        return highestEnd;
    }

    public int getHighestStart() {
        return highestEnd - length + 1;
    }

    public int getLowestEnd() {
        return lowestStart + length - 1;
    }

    public int getLowestStart() {
        return lowestStart;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return " " + length;
    }
}
