package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询菜品信息
     *
     * @param dishPageQueryDTO 菜品分页查询条件传输对象，包含分页及查询条件
     * @return 返回菜品信息分页结果，包含当前页的菜品数据及分页相关元数据
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID删除菜品记录
     *
     * @param id 菜品的唯一标识符
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据ID查询菜品信息
     *
     * @param id 菜品的唯一标识符
     * @return 返回找到的菜品详细信息，如果找不到则返回null
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);
}
