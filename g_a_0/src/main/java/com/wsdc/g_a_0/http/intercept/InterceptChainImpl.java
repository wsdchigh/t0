package com.wsdc.g_a_0.http.intercept;

import com.wsdc.g_a_0.http.ICall;
import com.wsdc.g_a_0.http.Intercept;
import com.wsdc.g_a_0.http.InterceptChain;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InterceptChainImpl implements InterceptChain {
    List<Intercept> list = new LinkedList<>();
    int index = 0;
    @Override
    public void add(Intercept intercept) {
        list.add(intercept);
    }

    @Override
    public void intercept(ICall call) throws IOException {
        if(index < list.size()){
            list.get(index++).intercept(call);
        }
    }

    @Override
    public InterceptChain chain() {
        return this;
    }

    @Override
    public void setChain(InterceptChain chain) {

    }
}
