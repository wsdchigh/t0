package com.wsdc.g_a_0;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void t1(){
        RouterUtil.RouterBean parse = RouterUtil.parse("/public0/home/cart");
        System.out.println(parse.module_name);
        System.out.println(parse.router_level_1);
        System.out.println(parse.router_level_2);
        System.out.println(parse.router_name);
    }
}