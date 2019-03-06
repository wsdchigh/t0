package com.wsdc.p_j_0.http;

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
