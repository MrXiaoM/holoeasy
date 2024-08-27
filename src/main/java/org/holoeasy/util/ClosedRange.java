package org.holoeasy.util;

import java.util.ArrayList;
import java.util.List;

public class ClosedRange<T extends Comparable<T>> {
    public T start;
    public T endInclusive;

    public ClosedRange(T start, T endInclusive) {
        this.start = start;
        this.endInclusive = endInclusive;
    }

    public boolean contains(T value) {
        return value.compareTo(start) >= 0 && value.compareTo(endInclusive) <= 0;
    }

    public boolean isEmpty() {
        return start.compareTo(endInclusive) > 0;
    }

    public static <T extends Comparable<T>> ClosedRange<T> range(T start, T endInclusive) {
        return new ClosedRange<>(start, endInclusive);
    }
    public static <T extends Comparable<T>> List<ClosedRange<T>> rangeSingle(T start, T endInclusive) {
        List<ClosedRange<T>> list = new ArrayList<>();
        list.add(new ClosedRange<>(start, endInclusive));
        return list;
    }
}
