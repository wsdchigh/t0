package com.wsdc.myfresh.logic;

public class Logic {
    /*
     *  下拉刷新逻辑分析
     *
     *  核心参数
     *      >   status          状态码
     *          >   spare       空闲期
     *          >   scroll      滑动期间
     *          >   load        刷新期间
     *          >   complete    刷新完成期间
     *
     *      >   freshDirection  刷新方向
     *          >   null        没有触发刷新
     *          >   down        下拉刷新
     *          >   up          上拉刷新
     *
     *      >   scrollSource    滑动来源
     *          >   null        屏幕上面暂时没有任何滑动
     *          >   finger      手指在屏幕上面滑动
     *          >   auto        松开手指触发回滑 自动回滑
     *
     *
     *      >   backSemaphore   回滑信号    (每一次回滑，都需要标记自身)
     *          >   sem_load    加载信号
     *          >   sem_cancel  取消信号
     *          >   sem_complete完成信号
     *
     *
     *
     *
     *
     *  核心函数
     *      >   dispatchTouchEvent(MotionEvent ev)
     *          >   frame中的事件流过这里，不采用原本自身默认的规则
     *          >   采用 MotionEvent + status 嵌套分情况进行处理
     *          >   这里接管所有的坐标  事件坐标一律下发到 IData 实例之中
     *
     *          >   MotionEvent.ACTION_DOWN :
     *              >   如果当前的scrollSource == null
     *                  >   标记scrollSource  (finger)
     *                  >   frame 分发down 事件
     *                  >   记录分发了一个down事件
     *              >   不作出任何处理
     *
     *          >   MotionEvent.ACTION_MOVE :
     *
     *              >   spare
     *                  >   根据当前滑动的方向 + frame自身的情况 决定是否需要 激活 刷新
     *                      >   激活
     *                          >   frame 下发一个cancel事件，取消之前分发的down事件
     *                          >   IData 分发一个刷新方向
     *                          >   将周期切换到 scroll 周期
     *                          >   因为需要刷新，会添加一个header或者tail 通知frame 重新布局
     *                          >   从IData中获取当前事件所需要引导的偏移 frame响应这个偏移
     *
     *                      >   未激活
     *                          >   正常的分发事件
     *
     *              >   scroll
     *                  >   从IData之中获取响应事件的偏移数值 frame响应偏移
     *                  >   如果逆着刷新方向偏移会导致 offsetAll == 0 此时会切换到 spare周期
     *
     *              >   load
     *                  >   如果此时scrollSource == auto 则不接受事件
     *                  >   如果 scrollSource == null || finger  标记为 finger
     *                      >   允许frame响应事件进行偏移
     *                      >   如果在偏移的时候 导致 offsetAll == 0
     *                          >   后续的偏移直到up事件 将不再处理事件 frame分发事件
     *                          >   需要下发一个down事件
     *
     *              >   complete
     *                  >   滑动可以跨过load来到complete周期  ，具体规则和load一样
     *
     *          >   MotionEvent.ACTION_UP :
     *              >   spare
     *                  >   frame分发事件
     *              >   scroll
     *                  >   根据总偏进行一次回滑 同时固定总偏
     *                  >   offsetAll > 180     offsetAll = 180 回滑到 180这个位置
     *                  >   offsetAll < 180     offsetAll = 0   回滑到 0 这个位置
     *                  >   标记scrollSource = auto
     *
     *              >   load
     *                  >
     *              >   complete
     *
     *      >   sem         信号函数
     *          >   标记scrollSource == null
     *          >
     */
}
