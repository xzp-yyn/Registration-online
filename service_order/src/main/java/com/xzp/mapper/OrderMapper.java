package com.xzp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzp.model.order.Order;
import com.xzp.vo.order.DataShowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/16 9:50
 * @Version 1.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<DataShowVo> findAllByParam(@Param("ordervo") DataShowVo dataShowVo);

}
