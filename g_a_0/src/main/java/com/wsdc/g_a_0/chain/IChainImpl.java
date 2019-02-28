package com.wsdc.g_a_0.chain;

import com.wsdc.g_a_0.thread0.IThread;

import java.util.Map;

public class IChainImpl extends AbstractIChain {
    public IChainImpl() {
    }

    public IChainImpl(Map<Integer, Object> map) {
        super(map);
    }

    public IChainImpl(IThread<ITask<Integer, Map<Integer, Object>>> t1, Map<Integer, Object> map) {
        super(t1, map);
    }

    @Override
    public void post(ITask<Integer, Map<Integer, Object>> task, IChain<Integer, Map<Integer, Object>> chain) {

    }
}
