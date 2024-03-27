package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: Mapper 基础抽象方法
 * @Author: KunSpireUp
 */

public interface BaseMapper<T, P> {
    /**
    * insert 插入
    */
    Integer insert(@Param("bean") T t);

    /**
    * insertOrUpdate 插入或更新
    */
    Integer insertOrUpdate(@Param("bean") T t);

    /**
    * insertBatch 批量插入
    */
    Integer insertBatch(@Param("list") List<T> list);

    /**
    * insertOrUpdateBatch 批量插入或更新
    */
    Integer insertOrUpdateBatch(@Param("list") List<T> list);

    /**
    * selectList 根据参数查询集合
    */
    List<T> selectList(@Param("query") P p);

    /**
    * selectCount 根据集合查询参数
    */
    Integer selectCount(@Param("query") P p);
}

