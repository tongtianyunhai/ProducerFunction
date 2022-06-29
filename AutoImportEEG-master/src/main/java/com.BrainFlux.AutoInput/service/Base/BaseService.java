package com.BrainFlux.AutoInput.service.Base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：whl
 * @Version：1.0
 * @Date：2021/7/2-16:45
 * @Since:jdk1.8
 */
public interface BaseService<T>{
    /**
     * query all
     * @return
     */
    List<T> list();

    /**
     * query by condition
     * @param wrapper
     * @return
     */
    List<T> search(Wrapper<T> wrapper);

    /**
     * query by id
     * @param id
     * @return
     */
    T findById(Serializable id);

    /**
     * save information
     * @param t
     * @return
     */
    int save(T t);

    /**
     * update by id
     * @param t
     * @return
     */
    int updateById(T t);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteById(Serializable id);

    /**
     * delete a list
     * @param idList
     * @return
     */
    int batchDeleteByIdList(List<Serializable> idList);
}
