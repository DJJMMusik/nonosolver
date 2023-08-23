package de.djjm.nanosolver.matrix;

public enum CellStatus {
    UNKNOWN, FILLED, EMPTY;

    public boolean isFilled() {
        return this == FILLED;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
