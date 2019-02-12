package com.wsdc.g_j_0;

import java.util.List;

/*
 *  链表关系    (父子关系)
 */
public interface ILInk0<T> {
    T parent();

    //  存在多个子
    List<T> children();

    //  只有一个子
    T child();
}
