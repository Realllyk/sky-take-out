package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品合对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页擦汗寻
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除功能
     * @param ids
     */
    public void deleteBatch(List<Long> ids);

    /**
     * 根据菜品id获取菜品和口味信息
     * @param id
     * @return
     */
    public DishVO getIdwithFlavor(Long id);

    /**
     * 根据id修改菜品基本信息和口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id列举菜品信息
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId);

    /**
     * 根据菜品id 起售或停售菜品
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id);
}
