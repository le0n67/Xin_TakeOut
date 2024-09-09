package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     *  菜品起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    void update(DishDTO dishDTO);

    DishVO getById(Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
