package de.djjm.nanosolver.matrix;


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
        //setting lowestStart to the lowest possible position
        if (lowerClue != null) {
            //setting the lowestStart to the first possible position in reference to the lower clue
            lowestStart = Math.max(lowerClue.getLowestEnd() + 2, lowestStart);//2 because one higher and one empty space between clues
        }
        CellStatus previousStatus = getStatus(lowestStart-1, fields);
        int currentLength = 0;
        for (int i = lowestStart; i < fields.length; i++) {
            CellStatus status = fields[i].getStatus();
            if (currentLength == length) {
                //having reached length goal
                if (!(status == CellStatus.FILLED || previousStatus == CellStatus.FILLED)) {
                    return;
                }
                previousStatus = fields[lowestStart].getStatus();
                lowestStart++;
                continue;
            }
            if (status == CellStatus.EMPTY) {
                //not enough space until now, so reset
                currentLength = 0;
                lowestStart++;
            } else {
                //still space so continue
                currentLength++;
            }
        }
        if (currentLength != length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void calculateHighestPosition(Clue higherClue, NanoCell[] fields) {
        //setting lowestStart to the lowest possible position
        if (higherClue != null) {
            highestEnd = Math.min(higherClue.getHighestStart() - 2, highestEnd); //2 because one lower and one empty space between clues
        }
        CellStatus previousStatus = getStatus(highestEnd + 1, fields);
        int currentLength = 0;
        for (int i = highestEnd; i >= 0; i--) {
            CellStatus status = fields[i].getStatus();
            if (currentLength == length) {
                //having reached length goal
                if (!(status == CellStatus.FILLED || previousStatus == CellStatus.FILLED)) {
                    return;
                }
                previousStatus = fields[highestEnd].getStatus();
                highestEnd--;
                continue;
            }
            if (status == CellStatus.EMPTY) {
                //not enough space until now, so reset
                currentLength = 0;
                highestEnd--;
            } else {
                //still space so continue
                currentLength++;
            }
        }
        if (currentLength != length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public boolean needsToContainCell(int i) {
        int highestEndOld = highestEnd;
        int lowestStartOld = lowestStart;
        highestEnd = Math.min(highestEnd, i + length - 1);
        lowestStart = Math.max(lowestStart, i - length + 1);
        return !(highestEndOld== highestEnd && lowestStartOld == lowestStart);
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
