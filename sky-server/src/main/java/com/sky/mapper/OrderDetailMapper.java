package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Date：2024/9/19  21:19
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 插入订单数据
     * @param
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
