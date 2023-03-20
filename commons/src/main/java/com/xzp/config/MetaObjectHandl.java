package com.xzp.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author xuezhanpeng
 * @Date 2022/10/10 16:42
 * @Version 1.0
 */

/**
 * 公共字段填充
 */
@Component
public class MetaObjectHandl implements MetaObjectHandler {
    //插入时必须同时拥有这四个属性，否则报错
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime",new Date());
        metaObject.setValue("updateTime",new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",new Date());
    }
}
