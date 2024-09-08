package com.sky.service;

import com.sky.dto.DishDTO;

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
}
