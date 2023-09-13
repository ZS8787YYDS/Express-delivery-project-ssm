package com.zisheng.MyUtils;

/**
 *
 */
public class ThreadUtils {
    // 创建一个ThreadLocal对象，指定泛型为Long，表示该变量存储的是Long类型的数据
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    // 用于设置值的静态方法
    public static final  void setThreadLocal(Long id)
    {
        threadLocal.set(id);
    }
    // 用于获取值的方法
    public static final Long getThreadLocal()
    {
        return threadLocal.get();
    }
}
