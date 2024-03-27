package com.easyjava.controller;

import com.easyjava.entity.vo.ResponseVO;;

import com.easyjava.enums.ResponseCodeEnum;;

/**
 * @Description: 信息返回状态
 * @Author: KunSpireUp
 * @Date: 3/27/2024 12:24 AM
 */
public class ABaseController {

	protected static final String STATUS_SUCCESS = "success";

	protected static final String STATUS_ERROR = "error";

	protected <T> ResponseVO getSuccessResponseVO(T t) {
		ResponseVO<T> responseVO = new ResponseVO<>();
		responseVO.setStatus(STATUS_SUCCESS);
		responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
		responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
		responseVO.setData(t);
		return responseVO;
	}
}
