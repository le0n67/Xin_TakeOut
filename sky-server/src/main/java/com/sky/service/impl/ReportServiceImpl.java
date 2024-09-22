package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Date：2024/9/22  16:35
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1)) {
            dateList.add(date);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap() {
                {
                    put("begin", beginTime);
                    put("end", endTime);
                    put("status", Orders.COMPLETED);
                }
            };
            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);
        }
        return new TurnoverReportVO(StringUtils.join(dateList, ","), StringUtils.join(turnoverList, ","));
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> totalUserList = new ArrayList<>();
        List<Double> newUserList = new ArrayList<>();
        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1)) {
            dateList.add(date);
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);
            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);
            totalUserList.add(totalUser == null ? 0.0 : totalUser);
            newUserList.add(newUser == null ? 0.0 : newUser);
        }
        return new UserReportVO(StringUtils.join(dateList, ","), StringUtils.join(totalUserList, ","), StringUtils.join(newUserList, ","));

    }

    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        //遍历dateList，查询每天的订单量,每天的有效订单量,订单总数,有效订单总数
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date = begin; date.isBefore(end); date = date.plusDays(1)) {
            dateList.add(date);
            //获取日期开始结束具体时间
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //查询指定时间区间的订单量
            Map map = new HashMap() {{
                put("beginTime", beginTime);
                put("endTime", endTime);
            }};
            Integer orderCount = orderMapper.countByMap(map);
            //查询指定时间区间的有效订单量
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);
            //存放每天的订单量和有效订单量
            orderCountList.add(orderCount == null ? 0 : orderCount);
            validOrderCountList.add(validOrderCount == null ? 0 : validOrderCount);
        }
        //计算总订单量和有效订单量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer totalValidOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //计算总订单量和有效订单量的占比
        Double orderCompletionRate = (totalOrderCount == 0 ? 0.0 : totalValidOrderCount.doubleValue() / totalOrderCount);
        log.info("总订单量：{}，有效订单量：{}，订单完成率：{}%", totalOrderCount, totalValidOrderCount, orderCompletionRate * 100);
        //将orderCountList、validOrderCountList和dateList,orderCompletionRate转成字符串封装进VO
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate.doubleValue())
                .build();
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        //查询商品销量
        //select od.name,sum(od.number) from order_detail od,orders o where od.order_id = o.id and o.status = 5 and o.order_time >= beginTime and o.order_time <= endTime group by od.name order by sum(od.number) desc limit 10
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);
        //将goodsSalesDTOList中的name和number分别转成列表字符串封装进VO
        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ","))
                .numberList(StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ","))
                .build();
        return salesTop10ReportVO;
    }
}
