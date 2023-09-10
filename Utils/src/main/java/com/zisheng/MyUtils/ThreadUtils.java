package com.zisheng.MyUtils;

public class ThreadUtils {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static final  void setThreadLocal(Long id)
    {
        threadLocal.set(id);
    }
    public static final Long getThreadLocal()
    {
        return threadLocal.get();
    }
}
