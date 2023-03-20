package com.xzp.vo.cmn;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/30 16:05
 * @Version 1.0
 */

@Data
public class DictExcelVo {

    @ExcelProperty(value = "id",index = 0)
    private Long id;

    @ExcelProperty(value = "父级id",index = 1)
    private Long parentId;
    @ExcelProperty(value = "名称",index = 2)
    private String name;
    @ExcelProperty(value = "值",index = 3)
    private String value;
    @ExcelProperty(value = "编码",index = 4)
    private String dictCode;
}
