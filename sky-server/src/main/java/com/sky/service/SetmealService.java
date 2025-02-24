package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐，并维护套餐与菜品的关系表
     * @param setmealDTO
     */
    public void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐和套餐菜品关联信息
     * @param ids
     */
    public void deleteBatch(List<Long> ids);

    /**
     * 分页条件查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐信息 和 套餐菜品关联信息
     * @param setmealDTO
     */
    public void update(SetmealDTO setmealDTO);

    /**
     * 根据套餐id 获取 套餐基本信息 及 相关的菜品信息
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id);
}
