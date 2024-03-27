package com.easyjava.service.impl;


import java.util.List;
import com.easyjava.entity.query.SimplePage;
import com.easyjava.enums.PageSize;
import com.easyjava.mappers.ProductInfoMapper;
import com.easyjava.service.ProductInfoService;
import com.easyjava.entity.vo.PaginationResultVO;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Description: 商品信息 业务接口实现
 * @Author: false
 * @Date: 2024/03/27 17:31:58
 */
@Service("productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService{

	@Resource
	private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

	/**
 	 * 根据条件查询列表
 	 */
	public List<ProductInfo> findListByParam(ProductInfoQuery query) {
		return this.productInfoMapper.selectList(query);	}

	/**
 	 * 根据条件查询数量
 	 */
	public Integer findCountByParam(ProductInfoQuery query) {
		return this.productInfoMapper.selectCount(query);	}

	/**
 	 * 分页查询
 	 */
	public PaginationResultVO<ProductInfo> findListByPage(ProductInfoQuery query) {
		Integer count = this.findCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ProductInfo> list = this.findListByParam(query);
		PaginationResultVO<ProductInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
 	 * 新增
 	 */
	public Integer add(ProductInfo bean) {
		return this.productInfoMapper.insert(bean);
	}

	/**
 	 * 批量新增
 	 */
	public Integer addBatch(List<ProductInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.productInfoMapper.insertBatch(listBean);
	}

	/**
 	 * 批量新增或修改
 	 */
	public Integer addOrUpdateBatch(List<ProductInfo> listBean) {
		if ((listBean == null) || listBean.isEmpty()) {
			return 0;
		}
			return this.productInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
 	 * 根据 Id 查询
 	 */
	public ProductInfo getProductInfoById(Integer id) {
		return this.productInfoMapper.selectById(id);}

	/**
 	 * 根据 Id 更新
 	 */
	public Integer updateProductInfoById(ProductInfo bean, Integer id) {
		return this.productInfoMapper.updateById(bean, id);}

	/**
 	 * 根据 Id 删除
 	 */
	public Integer deleteProductInfoById(Integer id) {
		return this.productInfoMapper.deleteById(id);}

	/**
 	 * 根据 Code 查询
 	 */
	public ProductInfo getProductInfoByCode(String code) {
		return this.productInfoMapper.selectByCode(code);}

	/**
 	 * 根据 Code 更新
 	 */
	public Integer updateProductInfoByCode(ProductInfo bean, String code) {
		return this.productInfoMapper.updateByCode(bean, code);}

	/**
 	 * 根据 Code 删除
 	 */
	public Integer deleteProductInfoByCode(String code) {
		return this.productInfoMapper.deleteByCode(code);}

	/**
 	 * 根据 SkuTypeAndColorType 查询
 	 */
	public ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoMapper.selectBySkuTypeAndColorType(skuType, colorType);}

	/**
 	 * 根据 SkuTypeAndColorType 更新
 	 */
	public Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		return this.productInfoMapper.updateBySkuTypeAndColorType(bean, skuType, colorType);}

	/**
 	 * 根据 SkuTypeAndColorType 删除
 	 */
	public Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoMapper.deleteBySkuTypeAndColorType(skuType, colorType);}
}