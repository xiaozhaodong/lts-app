package com.shaw.lts.core.service;

import java.util.List;
import java.util.function.Consumer;

/**
 * BaseService
 * 基础Service
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/5/25 16:49
 **/
public interface BaseService<T> {

    /**
     * 新增
     * @param t 对象
     */
    void save(T t);

    /**
     * 新增对象，返回ID
     * @param t 对象
     */
    void saveUseKeys(T t);

    /**
     * 批量保存
     * @param tList 对象集合
     */
    void saveList(List<T> tList);

    /**
     * 根据主健查询
     * @param obj 查询
     * @return 对象
     */
    T selectByPrimaryKey(Object obj);

    /**
     * 查询数量
     * @param t 对象
     * @return 数量
     */
    int selectCount(T t);

    /**
     * 查询一条记录
     * @param t 查询条件对象
     * @return  对象
     */
    T selectOne(T t);

    /**
     * 查询一条记录
     * @param consumer 查询条件对象动作
     * @return  对象
     */
    T selectOne(Consumer<T> consumer);

    /**
     * 根据example查询
     * @param object Example
     * @return 对象
     */
    T selectOneByExample(Object object);

    /**
     * 查询全部
     * @return 对象集合
     */
    List<T> selectAll();

    /**
     * 查询一组记录
     * @param t 查询条件对象
     * @return 对象集合
     */
    List<T> selectList(T t);

    /**
     * 查询一组记录
     * @param consumer 查询条件对象
     * @return 对象集合
     */
    List<T> selectList(Consumer<T> consumer);

    /**
     * 根据条件查询
     * @param object 条件对象
     * @return 对象集合
     */
    List<T> selectByCondition(Object object);

    /**
     * 根据条件查询
     * @param object Example
     * @return 对象集合
     */
    List<T> selectByExample(Object object);

//    /**
//     * 根据IDS查询
//     * @param ids 逗号分割的id
//     * @return 对象集合
//     */
//    List<T> selectByIds(String ids);

    /**
     * 更新对象
     * @param t 对象
     */
    void update(T t);

    /**
     * 主键删除
     * @param obj 主键
     */
    void deleteByPrimaryKey(Object obj);

    /**
     * 根据Example删除
     * @param obj Example
     */
    void deleteByExample(Object obj);

//    /**
//     * 主键集合删除
//     * @param ids 主键集合
//     */
//    void deleteByIds(String ids);
}
