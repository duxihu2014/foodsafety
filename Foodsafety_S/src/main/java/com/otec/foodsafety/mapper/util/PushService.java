package com.otec.foodsafety.mapper.util;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PushService {
    protected static final Logger logger = LoggerFactory.getLogger(PushService.class);
    private JPushClient jpushClient = new JPushClient("531240dcb7bd8e0a17c5922d", "de3911ecfd161aef218257eb", null, ClientConfig.getInstance());

    public void push(){
        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            PushResult result = jpushClient.sendPush(payload);
            System.out.println(JSON.toJSONString(result));
            logger.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
        }

    }

    public static PushPayload buildPushObject_all_all_alert() {

//        return PushPayload.alertAll(ALERT);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias("oea_5"))
//                .setNotification(Notification.android(ALERT, "测试别名发送", null))
                .setMessage(Message.content("{\"id\":1}"))
                .build();
    }

    public PushResult pushAlarm(String id, String title, String content, List<String> tagList) {
        Map<String, String> contentMap = Maps.newHashMap();
        contentMap.put("id", id);
        contentMap.put("content", content);
        contentMap.put("title", title);
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tagList))
                .setMessage(Message.content(JSON.toJSONString(contentMap)))
                .build();
        PushResult result = null;
        try {
            result = jpushClient.sendPush(pushPayload);
            System.out.println("推送结果:"+JSON.toJSONString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args){
        PushService pushService = new PushService();
//        pushService.push();
        pushService.pushAlarm("1", "传感器预警", "温度监控预警", Lists.newArrayList("area_1347"));
    }
}
