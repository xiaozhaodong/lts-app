package com.shaw.lts.persist;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * BaseMapper
 * 基础mapper
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/5/23 17:12
 **/
//public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T>, ConditionMapper<T> {
//}
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, ConditionMapper<T> {
}
