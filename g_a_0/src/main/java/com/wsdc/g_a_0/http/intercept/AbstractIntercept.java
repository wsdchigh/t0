package com.wsdc.g_a_0.http.intercept;

import com.wsdc.g_a_0.http.Intercept;
import com.wsdc.g_a_0.http.InterceptChain;

public abstract class AbstractIntercept implements Intercept {
    InterceptChain chain;
    @Override
    public InterceptChain chain() {
        return chain;
    }

    @Override
    public void setChain(InterceptChain chain) {
        this.chain = chain;
    }
}
