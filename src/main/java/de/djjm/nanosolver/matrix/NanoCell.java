package de.djjm.nanosolver.matrix;

public class NanoCell {
    public static final char symbolUnknown = '?';
    public static final char symbolFilled = '#';
    public static final char symbolEmpty = ' ';

    private CellStatus status;

    public NanoCell() {
        status = CellStatus.UNKNOWN;
    }

    public NanoCell(char status) {
        switch (status) {
            case symbolUnknown -> this.status = CellStatus.UNKNOWN;
            case symbolEmpty -> this.status = CellStatus.EMPTY;
            case symbolFilled -> this.status = CellStatus.FILLED;
            default -> new IllegalStateException("Could not parse " + status + " into a CellStatus");
        }
    }

    /**
     * set the status to empty if the status was unknown
     */
    public void setUnreachable() {
        if (!status.isUnknown()) {
            throw new IllegalStateException("The state of the NanoField is already set to " + status);
        }
        status = CellStatus.EMPTY;
    }

    /**
     * set the status to filled if the status was unknown
     */
    public void setRequired() {
        if (!status.isUnknown()) {
            throw new IllegalStateException("The state of the NanoField is already set to " + status);
        }
        status = CellStatus.FILLED;
    }

    public CellStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return switch (status) {
            case UNKNOWN -> String.valueOf(symbolUnknown);
            case FILLED -> String.valueOf(symbolFilled);
            case EMPTY -> String.valueOf(symbolEmpty);
        };
    }
}
