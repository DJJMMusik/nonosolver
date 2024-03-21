package de.djjm.nanosolver.matrix.cell;

public class NonoCell {
    private static final char SYMBOL_UNKNOWN = '?';
    private static final char SYMBOL_FILLED = '#';
    private static final char SYMBOL_EMPTY = ' ';

    private CellStatus status;

    public NonoCell() {
        status = CellStatus.UNKNOWN;
    }

    public NonoCell(char status) {
        this.status = switch (status) {
            case SYMBOL_UNKNOWN -> CellStatus.UNKNOWN;
            case SYMBOL_EMPTY -> CellStatus.EMPTY;
            case SYMBOL_FILLED -> CellStatus.FILLED;
            default -> throw new IllegalStateException("Could not parse " + status + " into a CellStatus");
        };
    }

    /**
     * set the status to empty if the status was unknown
     */
    public void setUnreachable() {
        if (status.isFilled()) {
            throw new IllegalStateException("The state of the NanoField is already set to " + status);
        }
        status = CellStatus.EMPTY;
    }

    /**
     * set the status to filled if the status was unknown
     */
    public void setRequired() {
        if (status.isEmpty()) {
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
            case UNKNOWN -> String.valueOf(SYMBOL_UNKNOWN);
            case FILLED -> String.valueOf(SYMBOL_FILLED);
            case EMPTY -> String.valueOf(SYMBOL_EMPTY);
        };
    }

    public boolean processStatus(CellStatusList statusList, int length) {
        return switch (status) {
            case UNKNOWN -> statusList.processStatusUnknown(length, status);
            case FILLED -> statusList.processStatusFilled(length, status);
            case EMPTY -> statusList.processStatusEmpty(length);
        };
    }

    public boolean setCellStatus(CellStatus status) {
        if (status.isEmpty()) {
            setUnreachable();
            return true;
        } else if (status.isFilled()) {
            setRequired();
            return true;
        }
        return false;
    }
}
