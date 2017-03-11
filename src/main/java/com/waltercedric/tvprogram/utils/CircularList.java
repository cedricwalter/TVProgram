package com.waltercedric.tvprogram.utils;

import java.util.ArrayList;

// do not work for mutable list, only support ArrayList, used onyl for channels
public class CircularList<E> extends ArrayList<E> {

    @Override
    public E get(int index) {

        int listSize = super.size();
        int indexToGet = index % listSize;

        //might happen to be negative
        indexToGet = (indexToGet < 0) ? indexToGet + listSize : indexToGet;

        return super.get(indexToGet);
    }
}