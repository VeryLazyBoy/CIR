package com.cir.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class FixedSizeSortedList<T> {
    HashSet<T> internalSet; // TreeSet lazy remove
    int size;
    Comparator<T> comparator;
    public FixedSizeSortedList(int size, Comparator<T> comparator) {
        this.size = size;
        internalSet = new HashSet<T>();
        this.comparator = comparator;
    }

    public void add(T item) {
        internalSet.add(item);
        TreeSet<T> tempSet = new TreeSet<>(comparator);
        tempSet.addAll(internalSet);
        if (tempSet.size() > size) {
            tempSet.pollFirst();
        }
        internalSet = new HashSet<>(tempSet);
    }

    public List<T> getSortedResultList() {
        List<T> resultList = new ArrayList<T>(internalSet);
        Collections.sort(resultList, comparator);
        Collections.reverse(resultList);
        return resultList;
    }
}
