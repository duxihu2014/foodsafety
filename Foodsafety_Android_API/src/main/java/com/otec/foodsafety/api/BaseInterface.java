package com.otec.foodsafety.api;

import com.cykj.grcloud.entity.page.PageObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BaseInterface {

    @Autowired
    protected HttpServletRequest request;


    protected PageObject getPageObject(Map<String, String> params) {
        PageObject pageObject = new PageObject();
        String currPageStr = params.get("page");
        if (StringUtils.isBlank(currPageStr)) {
            currPageStr = "1";
        }
        String pageSizeStr = params.get("limit");
        String property = params.get("sort");// 排序字段
        String orderByClause = null;
        if (StringUtils.isNotBlank(property)) {
            orderByClause = getColumn(property) + " " + params.get("order"); // 升序，降序
        }

        try {
            pageObject.setCurrPage(Integer.valueOf(currPageStr));
            pageObject.setPageSize(Integer.valueOf(pageSizeStr));
        } catch (NumberFormatException e) {
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
            if (Character.isUpperCase(c)) {
                buffer.insert(i++, '_');
            }
        }
        return buffer.toString();
    }
}
