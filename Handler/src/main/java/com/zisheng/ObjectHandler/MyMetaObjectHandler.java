package com.zisheng.ObjectHandler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zisheng.MyUtils.ThreadUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 元对象处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
   // 创建日志记录对象
    private static final Logger log = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    /**
     * 当向数据库中插入数据时会调用该方法，为公共属性进行赋值操作
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertFill方法调用了" + metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", new ThreadLocal<>().get());
        metaObject.setValue("updateUser",  new ThreadLocal<>().get());
    }

    /**
     * 当修改数据库中的数据时，会调用该方法，为公共属性进行赋值操作
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updateFill方法调用了" + metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
        Long id = ThreadUtils.getThreadLocal();
        metaObject.setValue("updateUser",id);
    }
}
