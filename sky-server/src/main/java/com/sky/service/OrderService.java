package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;

/**
 * Date：2024/9/19  21:10
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    void complete(Long id);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void reminder(Long id);
}
