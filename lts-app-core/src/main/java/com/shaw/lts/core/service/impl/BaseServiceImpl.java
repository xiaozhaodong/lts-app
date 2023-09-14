package com.shaw.lts.core.service.impl;



import com.shaw.lts.core.service.BaseService;
import com.shaw.lts.persist.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Consumer;

/**
 * BaseServiceImpl
 * 基础impl
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/5/25 17:02
 **/
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private BaseMapper<T> baseMapper;

    /**
     * 当前泛型真实类型的Class
     */
    private final Class<T> modelClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        // 获得具体model，通过反射来根据属性条件查找数据
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void save(T t) {
        baseMapper.insertSelective(t);
    }

    @Override
    public void saveUseKeys(T t) {
        baseMapper.insertUseGeneratedKeys(t);
    }

    @Override
    public void saveList(List<T> tList) {
        baseMapper.insertList(tList);
    }

    @Override
    public T selectByPrimaryKey(Object obj) {
        return baseMapper.selectByPrimaryKey(obj);
    }

    @Override
    public int selectCount(T t) {
        return baseMapper.selectCount(t);
    }

    @Override
    public T selectOne(T t) {
        return baseMapper.selectOne(t);
    }

    @Override
    public T selectOne(Consumer<T> action) {
        T t = createInstance();
        action.accept(t);
        return baseMapper.selectOne(t);
    }

    @Override
    public T selectOneByExample(Object object) {
        return baseMapper.selectOneByExample(object);
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public List<T> selectList(T t) {
        return baseMapper.select(t);
    }

    @Override
    public List<T> selectList(Consumer<T> consumer) {
        T t = createInstance();
        consumer.accept(t);
        return baseMapper.select(t);
    }

    @Override
    public List<T> selectByCondition(Object object) {
        return baseMapper.selectByCondition(object);
    }

    @Override
    public List<T> selectByExample(Object object) {
        return baseMapper.selectByExample(object);
    }

    //    @Override
    //    public List<T> selectByIds(String ids) {
    //        return baseMapper.selectByIds(ids);
    //    }

    @Override
    public void update(T t) {
        baseMapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteByPrimaryKey(Object obj) {
        baseMapper.deleteByPrimaryKey(obj);
    }

    @Override
    public void deleteByExample(Object object) {
        baseMapper.deleteByExample(object);
    }

    //    @Override
    //    public void deleteByIds(String ids) {
    //        baseMapper.deleteByIds(ids);
    //    }
    private T createInstance() {
        try {
            return modelClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // 处理对象实例创建失败的异常情况
            throw new RuntimeException(e);
        }
    }
}
