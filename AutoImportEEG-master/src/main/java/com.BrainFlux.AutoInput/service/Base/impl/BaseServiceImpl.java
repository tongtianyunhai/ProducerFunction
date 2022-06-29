package com.BrainFlux.AutoInput.service.Base.impl;


import com.BrainFlux.AutoInput.common.reflect.ReflectionUtils;
import com.BrainFlux.AutoInput.mapper.base.MyMapper;
import com.BrainFlux.AutoInput.service.Base.BaseService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：gly
 * @Version：1.0
 * @Date：2021/7/2-16:48
 * @Since:jdk1.8
 */

public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private MyMapper<T> myMapper;

    protected MyMapper<T> getMyMapper() {
        return myMapper;
    }

    @Override
    public List<T> list() {
        return myMapper.selectList(null);
    }

    @Override
    public List<T> search(Wrapper<T> wrapper) {
        return myMapper.selectList(wrapper);
    }

    @Override
    public T findById(Serializable id) {
        return myMapper.selectById(id);
    }

    @Override
    public int save(T t) {
        //强转 ：不行
        //反射：不行
        //工具：
        //框架：
      /*  Class<?> aClass = t.getClass();
        Object o = aClass.newInstance();
        Class<? extends BaseServiceImpl> aClass1 = this.getClass();*/
        //新增的时候需要添加创建人以及创建时间
        //根据工具类的反射 调用
        ReflectionUtils.invokeMethod(t, "setData", null, null);
        return myMapper.insert(t);
    }

    @Override
    public int updateById(T t) {
        ReflectionUtils.invokeMethod(t, "setData", null, null);
        return myMapper.updateById(t);
    }

    @Override
    public int deleteById(Serializable id) {
        return myMapper.deleteById(id);
    }

    @Transactional
    @Override
    public int batchDeleteByIdList(List<Serializable> idList) {
        return myMapper.deleteBatchIds(idList);
    }
}
