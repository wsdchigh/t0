package com.wsdc.g_j_0;

import java.util.List;

/*
 *  转换接口
 *  <li>    O   origin/old
 *  <li>    N   new
 *
 *  <li>    支持批量转换
 *  <li>    支持单个转换
 */
public interface IConvert0<O,N> {
    N convert(O o);

    List<N> convert(List<O> os);
}
