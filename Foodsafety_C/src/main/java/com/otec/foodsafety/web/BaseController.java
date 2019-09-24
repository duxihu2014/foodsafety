package com.otec.foodsafety.web;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.util.vo.Result;
import com.otec.foodsafety.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class BaseController{
	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 *
	 * @param request
	 * @return
	 */
	protected PageObject getPageObject(HttpServletRequest request){
		return getPageObject(request,null);
	}

	/**
	 *order asc
	 *page	1
	 *rows	10
	 *sort	roleId
	 * @param request
	 * @param orderByClause
	 * @return
	 */
	protected PageObject getPageObject(HttpServletRequest request,
									   String orderByClause){
		PageObject pageObject = new PageObject();
		String currPageStr = request.getParameter("page");
		if(StringUtils.isBlankString(currPageStr)){
			currPageStr="1";
		}
		String pageSizeStr = request.getParameter("limit");
		String property = request.getParameter("sort");
		if (StringUtils.isEmpty(orderByClause)) {
			if (!StringUtils.isEmpty(property)) {
				orderByClause = getColumn(property) + " "
						+ request.getParameter("order");
			}
		}
		try{
			pageObject.setCurrPage(Integer.valueOf(currPageStr));
			pageObject.setPageSize(Integer.valueOf(pageSizeStr));
		}catch(NumberFormatException e){
			pageObject.setCurrPage(1);
			pageObject.setPageSize(Integer.MAX_VALUE);
		}
		pageObject.addCondition("orderByClause", orderByClause);
		return pageObject;
	}

	private String getColumn(String property) {
		StringBuffer buffer = new StringBuffer(property);
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if(Character.isUpperCase(c)){
				buffer.insert(i++, '_');
			}
		}
		return buffer.toString();
	}

	private static String makeJsonResponse(boolean result, String errMsg) {
		if (result) {
			return "{\"success\":"+result+",\"msg\":\"" + errMsg + "\"}";
		}
		return "{\"success\":"+result+",\"msg\":\"" + errMsg + "\"}";
	}


	public static void ajaxJsonResponse(PrintWriter out,boolean result,String errMsg){
		writeToPage(makeJsonResponse(result,errMsg),out);
	}

	public static void ajaxJsonResponseSuccess(PrintWriter out){
		ajaxJsonResponse(out,true,"操作成功");
	}

	public static void writeToPage(String content,PrintWriter out){
		out.print(content);
		out.flush();
		out.close();
	}

	public static void writeToPage(String content, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(content);
		} catch (IOException e) {
			logger.warn("write to page error: ", e);
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}


	public Result convertToRtnObj(GridDataModel model){
		Result rtn =  Result.ok();
		rtn.put("msg", "");
		rtn.put("count", model.getTotal());

		rtn.put("data",model.getRows());

		return rtn;



	}



}