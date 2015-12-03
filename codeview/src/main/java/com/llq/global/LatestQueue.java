package com.llq.global;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class LatestQueue<E> extends ArrayDeque<E> {

    private int size;

    public LatestQueue(int size) {
        super(size);
        this.size = size;
    }

    public void addItem(E e) {
        remove(e);// if e is not in the queue, nothing will happen
        addFirst(e);
        if (size() > size) {
            removeLast();
        }
    }

    public List<File> toList(){
        File[] a = toArray(new File[]{});
        return Arrays.asList(a);
    }

}
