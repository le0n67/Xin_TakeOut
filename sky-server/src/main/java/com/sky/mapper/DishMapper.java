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

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     *
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

    /**
     * 根据ID批量删除菜品记录
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 更新菜品信息
     *
     * @param dish 要更新的菜品对象
     * @AutoFill(OperationType.UPDATE) 指示该方法用于自动填充菜品信息的更新操作
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态条件查询菜品
     *
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     *
     * @param setmealId
     * @return
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
