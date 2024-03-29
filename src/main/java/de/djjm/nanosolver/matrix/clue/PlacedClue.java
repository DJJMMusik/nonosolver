package de.djjm.nanosolver.matrix.clue;

import de.djjm.nanosolver.matrix.cell.NonoCell;

public class PlacedClue implements Clue {
    private final int length;
    private final int end;
    private final int start;

    public PlacedClue() {
        length = 0;
        start = -1;
        end = -1;
    }

    public PlacedClue(int length, int highestEnd, int lowestStart){
        this.length = length;
        this.end = highestEnd;
        this.start = lowestStart;
    }

    public Clue checkPlaced() {
        return this;
    }

    public boolean needsToContainCell(int i) {
        return i <= end && i >= start;
    } // TODO duplicate from canContain

    public boolean canContain(int i) {
        return i <= end && i >= start;
    }

    public String toString() {
        return " " + length;
    }

    public int getLowestEnd() {
        return end;
    }

    public int getHighestStart() {
        return start;
    }

    public boolean fillRequiredCells(NonoCell[] cells) {
        return false;
    }
}

