package de.djjm.nanosolver.matrix;

import java.util.*;

public class CellStatusList {
    LinkedList<CellStatus> statusList;

    public CellStatusList() {
        statusList = new LinkedList<>();
    }

    public void add(CellStatus status) {
        statusList.add(status);
    }

    public void clear() {
        statusList.clear();
    }

    public boolean isLength(int length) {
        return length == statusList.size();
    }

    public boolean isTooLong(int length) {
        return length < statusList.size();
    }

    public void removeUpToFirstAppearance(CellStatus status) {
        if (statusList.contains(status)) {
            popUntil(status);
        }
    }

    private void popUntil(CellStatus status) {
        while (status != statusList.pop());
    }

    boolean processStatusEmpty(int length) {
        if (isLength(length)){
            return true;
        }
        clear();
        return false;
    }

    boolean processStatusFilled(int length, CellStatus status) {
        if (isLength(length)){
            removeUpToFirstAppearance(CellStatus.UNKNOWN);
        }
        add(status);
        return false;
    }

    boolean processStatusUnknown(int length, CellStatus status) {
        if (isTooLong(length)) {
            clear();
            return false;
        }
        if (isLength(length)) return true;
        add(status);
        return false;
    }
}
