package com.easyjava.controller;


import java.util.List;
import com.easyjava.service.ProductInfoService;
import com.easyjava.entity.vo.ResponseVO;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
/**
 * @Description: 商品信息 Controller
 * @Author: false
 * @Date: 2024/05/10 17:12:57
 */
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController extends ABaseController{

	@Resource
	private ProductInfoService productInfoService;

	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query) {
		return getSuccessResponseVO(productInfoService.findListByPage(query));
	}

	/**
 	 * 新增
 	 */
	@RequestMapping("add")
	public ResponseVO add(ProductInfo bean) {
		Integer result = this.productInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增
 	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<ProductInfo> listBean) {
		this.productInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 批量新增或修改
 	 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ProductInfo> listBean) {
		this.productInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
 	 * 根据 Id 查询
 	 */
	@RequestMapping("getProductInfoById")
	public ResponseVO getProductInfoById(Integer id) {
		return getSuccessResponseVO(productInfoService.getProductInfoById(id));}

	/**
 	 * 根据 Id 更新
 	 */
	@RequestMapping("updateProductInfoById")
	public ResponseVO updateProductInfoById(ProductInfo bean, Integer id) {
		this.productInfoService.updateProductInfoById(bean, id);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Id 删除
 	 */
	@RequestMapping("deleteProductInfoById")
	public ResponseVO deleteProductInfoById(Integer id) {
		this.productInfoService.deleteProductInfoById(id);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Code 查询
 	 */
	@RequestMapping("getProductInfoByCode")
	public ResponseVO getProductInfoByCode(String code) {
		return getSuccessResponseVO(productInfoService.getProductInfoByCode(code));}

	/**
 	 * 根据 Code 更新
 	 */
	@RequestMapping("updateProductInfoByCode")
	public ResponseVO updateProductInfoByCode(ProductInfo bean, String code) {
		this.productInfoService.updateProductInfoByCode(bean, code);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 Code 删除
 	 */
	@RequestMapping("deleteProductInfoByCode")
	public ResponseVO deleteProductInfoByCode(String code) {
		this.productInfoService.deleteProductInfoByCode(code);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 SkuTypeAndColorType 查询
 	 */
	@RequestMapping("getProductInfoBySkuTypeAndColorType")
	public ResponseVO getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return getSuccessResponseVO(productInfoService.getProductInfoBySkuTypeAndColorType(skuType, colorType));}

	/**
 	 * 根据 SkuTypeAndColorType 更新
 	 */
	@RequestMapping("updateProductInfoBySkuTypeAndColorType")
	public ResponseVO updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		this.productInfoService.updateProductInfoBySkuTypeAndColorType(bean, skuType, colorType);
		return getSuccessResponseVO(null);
}

	/**
 	 * 根据 SkuTypeAndColorType 删除
 	 */
	@RequestMapping("deleteProductInfoBySkuTypeAndColorType")
	public ResponseVO deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		this.productInfoService.deleteProductInfoBySkuTypeAndColorType(skuType, colorType);
		return getSuccessResponseVO(null);
}
}