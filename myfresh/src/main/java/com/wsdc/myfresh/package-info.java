/*
 *                                      常用控件的测量体系(onMeasure 函数)
 *  >   关于View测量的参数
 *      >   MeasureSpec 测量规格
 *          这个实例用于存储当前View的期待结果(mode)和具体的测量尺寸(size)，两者使用一个int表示(前2位标识mode，后30位标识size)
 *
 *          >   mode
 *              >   UNSPECIFIED     00
 *                  >   未指定具体的大小
 *              >   EXACTLY         01
 *                  >   指定了具体的大小
 *              >   AT_MOST         11
 *                  >   指定了一个上限
 *
 *
 *
 *          >   size
 *
 *      >   关于测量的几个静态函数
 *          >   ViewGroup.measureChildWithMargins()
 *              >   评估子控件的MeasureSpec并开始测量
 *
 *          >   ViewGroup.getChildMeasureSpec(int spec,int padding,int childDimension)
 *              >   生成子控件MeasureSpec的实现函数
 *              >   结构图
 *                                  固定数值            MATCH_PARENT            WRAP_CONTENT
 *
 *              EXACTLY             EXACTLY             EXACTLY                 AT_MOST
 *
 *              AT_MOST             EXACTLY             AT_MOST                 AT_MOST
 *
 *              UNSPECIFIED         EXACTLY             UNSPECIFIED             UNSPECIFIED
 *
 *              >   基本上不会下发     UNSPECIFIED 系统函数是不会下发这种mode
 *              >
 *
 *          >   View.combineMeasureState(int state1,int state2)
 *              >   联合子控件的mode
 *
 *          >   View.resolveSizeAndState(int size,int measureSpec,int childMeasureState)
 *              >   生成自身最终的MeasureSpec  (通常修改尺寸，并不会修改mode)
 *              >   size表示的是测量出来的最小要求尺寸 (适用于wrap_content)
 *              >   measureSpec存储固定尺寸          (适用于match_parent或者固定数值)
 *
 *
 *      >   LayoutParams0(布局参数)
 *          每一个View都有自身的一个布局参数
 *          >   width
 *              >   固定数值    >= 0
 *              >   MATCH_PARENT -1
 *              >   WRAP_CONTENT -2
 *          >   height
 *          >   margin
 *          主要表达的是这三个参数，当前控件的大小和相对其他控件或者父容器的位置
 *          如果宽高不是固定数值，那么表达的是一种意愿，这种意愿可能不会实现(可以被修改)
 *
 *      >   显示参数
 *          >   visibility
 *              >   visible
 *                  >   参与布局，并且显示
 *              >   invisible
 *                  >   参与布局，不显示
 *              >   gone
 *                  >   不参与布局，并且不显示
 *
 *
 *
 *      >   MeasureSpec和LayoutParams的联合使用
 *          >   LayoutParams0    表达的是view期待的结果
 *          >   MeasureSpec     联合父容器 + LayoutParams所产生的预期结果
 *              >   父容器会根据自身的情景 + 子控件的LayoutParams 联合生成一个MeasureSpec
 *                  分发给子控件，这也是LayoutParams唯一生效的地方
 *
 *      >   padding和margin
 *          >   padding 衬料 可以理解为内间距 View相对于自己的一个内部内容的偏移 (宽高是包含这个数值)
 *          >   margin  外间距 相对于父容器或者平行View的距离   (宽高不包含这个数值)
 *
 *          >   ViewGroup
 *              >   如果vg设置了leftPadding 那么所有和父容器左边对齐的控件均必须要间距这个数值
 *          >   View
 *              >   如果view设置了leftPadding 通常限定了左边的位置
 *
 *          >   padding是控件自身的属性，不会影响到子控件的宽高
 *          >   margin是LayoutParams的属性
 */