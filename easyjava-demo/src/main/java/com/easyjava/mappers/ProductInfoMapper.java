package com.easyjava.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 商品信息 Mapper
 * @Author: false
 * @Date: 2024/05/11 00:03:01
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {

	/**
 	 * 根据 Id 查询
 	 */
	T selectById(@Param("id")Integer id);

	/**
 	 * 根据 Id 更新
 	 */
	Integer updateById(@Param("bean") T t, @Param("id")Integer id); 

	/**
 	 * 根据 Id 删除
 	 */
	Integer deleteById(@Param("id")Integer id);

	/**
 	 * 根据 Code 查询
 	 */
	T selectByCode(@Param("code")String code);

	/**
 	 * 根据 Code 更新
 	 */
	Integer updateByCode(@Param("bean") T t, @Param("code")String code); 

	/**
 	 * 根据 Code 删除
 	 */
	Integer deleteByCode(@Param("code")String code);

	/**
 	 * 根据 SkuTypeAndColorType 查询
 	 */
	T selectBySkuTypeAndColorType(@Param("skuType")Integer skuType, @Param("colorType")Integer colorType);

	/**
 	 * 根据 SkuTypeAndColorType 更新
 	 */
	Integer updateBySkuTypeAndColorType(@Param("bean") T t, @Param("skuType")Integer skuType, @Param("colorType")Integer colorType); 

	/**
 	 * 根据 SkuTypeAndColorType 删除
 	 */
	Integer deleteBySkuTypeAndColorType(@Param("skuType")Integer skuType, @Param("colorType")Integer colorType);

}