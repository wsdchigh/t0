package com.wsdc.g_a_0.thread0;

import java.util.Collection;

/*
 *  业务线程
 */
public interface IThread<A> {
    void doFirst(A a);

    void doLast(A a);

    void doAll(Collection<A> c);

    void exit();

    void run0(A a);
}
