package com.otec.foodsafety.web.report;

import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.util.vo.FrontUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/reportAlarm/api")
@RestController
public class ReportAlarmController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    private AuthService authService;
    @Autowired
    private AlarmService alarmService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> list(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> result = Lists.newArrayList();
        String type = params.get("type");
        if(StringUtils.isBlank(type)){
            type = "0";
        }
        String token = request.getHeader("access-token");
        FrontUser userInfo = authService.getUserInfo(token);
        // 查询列表数据
        Map<String, Object> map = Maps.newHashMap();
        if(StringUtils.equalsIgnoreCase(userInfo.getUserType(), "4")){
            map.put("enterpriseId", userInfo.getEnterpriseId());
        }else if(!StringUtils.equalsIgnoreCase(userInfo.getUserType(), "1")){
            map.put("areaId", userInfo.getAreaId());
        }
        map.put("eventType", type);
        result = alarmService.findAlarmReport(map);
        return result;

    }
}
