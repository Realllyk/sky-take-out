package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐，并维护套餐与菜品的关系表
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setMeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setMeal);
        // 向套餐表新增套餐信息
        setmealMapper.insert(setMeal);

        // 获取生成的套餐主键，通过.xml中的userGenerateId和keyProperties
        Long id = setMeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        // 向套餐菜品关系表新增套餐菜品关系信息
        if(setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 批量删除套餐和套餐菜品关联信息
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 起售中的套餐不能删除
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });


        // 根据ids删除套餐表中的套餐信息
        setmealMapper.deleteByIds(ids);

        // 根据ids在setmeal_dish中，删除对应setmeal_id的关联信息
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 分页条件查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 修改套餐信息 和 套餐菜品关联信息
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        // 修改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 删除套餐 关联的 套餐菜品信息
        List<Long> ids = new ArrayList<Long>();
        ids.add(setmeal.getId());
        setmealDishMapper.deleteBySetmealIds(ids);

        // 插入新的 套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
            dishMapper.getById(setmealDish.getDishId());
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 根据套餐id 获取 套餐基本信息 及 相关的菜品信息
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        // 在套餐表中查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        // 在套餐菜品关系表中 查询 套餐菜品关联信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 根据套餐id 起售或者停售 套餐
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        // 如果套餐中有菜品的状态为停售，则不能起售套餐
        if(status == StatusConstant.ENABLE){
            List<Dish> dishes = dishMapper.getBySetmealId(id);
            dishes.forEach(dish -> {
                if(dish.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
