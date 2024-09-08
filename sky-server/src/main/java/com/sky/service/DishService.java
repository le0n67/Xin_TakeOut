package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * Date：2024/9/8  16:34
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */
public interface DishService {
    /**
     * 新增菜品,同时保存口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
