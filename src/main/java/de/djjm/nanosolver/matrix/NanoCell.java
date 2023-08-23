package de.djjm.nanosolver.matrix;

public class NanoCell {
    private CellStatus status;

    public NanoCell() {
        status = CellStatus.UNKNOWN;
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
            case UNKNOWN -> "?";
            case FILLED -> "#";
            case EMPTY -> " ";
        };
    }
}
