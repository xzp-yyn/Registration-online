package com.xzp.utils;

import com.xzp.cmn.CmnClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/14 11:04
 * @Version 1.0
 */
@Component
public class CmnClientUtil {
    @Autowired
    private CmnClient cmnClient;

    public   String getByNotEmpty(String value){
        if(value==null || value.equals("")){
            return "";
        }
        String nameByvalue = cmnClient.getNameByvalue(Integer.valueOf(value));
        if(nameByvalue==null||nameByvalue.equals("")){
            return "";
        }
        return nameByvalue;
    }
}
