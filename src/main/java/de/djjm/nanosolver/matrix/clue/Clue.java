package de.djjm.nanosolver.matrix.clue;

import de.djjm.nanosolver.matrix.cell.NonoCell;

public interface Clue {

    static Clue initializeClue(Integer length, int maxPosition) {
        if(length == 0) return new PlacedClue();
        return new UnplacedClue(length, maxPosition);
    }

    Clue checkPlaced();

    default void calculateLowestPosition(Clue lowerClue, NonoCell[] cells){}

    default void calculateHighestPosition(Clue higherClue, NonoCell[] cells){}

    boolean needsToContainCell(int i);

    boolean canContain(int i);

    @Override
    String toString();

    int getLowestEnd();

    int getHighestStart();

    boolean fillRequiredCells(NonoCell[] cells);
}
