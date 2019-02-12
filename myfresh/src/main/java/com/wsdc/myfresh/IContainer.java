package com.wsdc.myfresh;

public interface IContainer<T> {
    void add(T t);
    void remove(T t);
}
