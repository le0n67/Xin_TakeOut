package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date：2024/9/21  18:44
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    //超时订单
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrders() {
        log.info("定时处理超时订单{}", LocalDateTime.now());
        List<Orders> timeOutOrders = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if (timeOutOrders != null && timeOutOrders.size() > 0) {
            for (Orders order : timeOutOrders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    //处理配送状态订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrders() {
        log.info("定时处理处于派送状态的订单{}", LocalDateTime.now());
        List<Orders> deliveryOrders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
        if (deliveryOrders != null && deliveryOrders.size() > 0) {
            for (Orders order : deliveryOrders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

}
