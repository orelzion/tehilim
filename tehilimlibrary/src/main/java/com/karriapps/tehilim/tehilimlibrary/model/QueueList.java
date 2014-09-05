package com.karriapps.tehilim.tehilimlibrary.model;

import java.lang.reflect.Array;

/**
 * Created by Orel on 02/09/2014.
 */
public class QueueList<E> {
    private int maxSize;
    private int mSize;
    private E[] mObjects;

    public QueueList(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean add(E e) {
        addFirst(e);

        return true;
    }

    public E get(int location) {
        return mSize < location ? null : mObjects[location];
    }

    private void addFirst(E e) {
        if (mSize < maxSize)
            mSize++;
        if (mObjects == null) {
            mObjects = (E[]) Array.newInstance(e.getClass(), maxSize);
            mObjects[0] = e;
            return;
        }
        E[] temp = (E[]) Array.newInstance(e.getClass(), maxSize);
        for (int i = 0; i < maxSize - 1; i++) {
            E obj = mObjects[i];
            temp[i + 1] = obj;
        }
        temp[0] = e;
        mObjects = temp;
    }

    public int size() {
        return mSize;
    }

    public E[] getList() {
        if (mObjects == null)
            return null;
        E[] retVal = (E[]) Array.newInstance(mObjects[0].getClass(), mSize);
        for (int i = 0; i < mSize; i++) {
            retVal[i] = mObjects[i];
        }
        return retVal;
    }

    public QueueList<E> setList(E[] es) {
        mObjects = es;
        mSize = es.length;
        return this;
    }
}
