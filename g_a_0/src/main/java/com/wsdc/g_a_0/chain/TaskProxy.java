package com.wsdc.g_a_0.chain;

/*
 *  给Task提供代理功能
 */
public interface TaskProxy<D> {
    /*
     *  ITask   任务
     *  <li>    之所以携带这个参数，这个参数携带了IChain参数
     *          提供更多可能的操作
     *  <li>    保存一条完整的调用链
     *  <li>    提供NIO操作的空间
     */
    int run(ITask task,D d) throws Exception;
}
