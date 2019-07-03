package com.otec.foodsafety.web.equipment;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.zip.Deflater;

/**
 * Created by wjc on 2019/6/26 0026.
 */

public class VideoUtil {


    //获取MD5值
    public static String getMD5String(String str) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();
            return re_md5.toLowerCase();

        } catch (Exception e) {
            return null;
        }
    }

    //base 64编码
    public static String getBase64(byte[] paramByteArray) {
        byte[] paramByte = Base64.encodeBase64(paramByteArray);
        return new String(paramByte);

    }


    //压缩
    public static byte[] compress(byte[] data) {
        if (data == null || data.length <= 0)
            return null;

        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
//            logger.error("fatalError", e);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
//                logger.error("fatalError", e);
            }
        }
        compresser.end();
        return output;
    }


    public static String getToken(String url, String deviceId, String equipmentId) {
        try {
            String base64 = getBase64Str(url, deviceId, equipmentId);
            String token = "/gsms/video/getCameraM3u8Url" + base64 + "suzhou_jingcheng_zhihui_xinxi_xx_0529";
            return getMD5String(token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getBase64Str(String rtsp_addressUrl, String deviceId, String equipmentId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("device_id", deviceId);
        jsonObject.put("key_id", deviceId + "_" + equipmentId);
        jsonObject.put("rtsp_address", rtsp_addressUrl);
        String jsonStr = jsonObject.toString();
        return getBase64(compress(jsonStr.getBytes()));
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0 || "null".equals(str)) return true;
        else return false;
    }


    public static void main(String[] args) throws IOException {


//         String device_id = "19g06k03r4";
//        String equipmentId = "13";
//        String rtsp_address = "rtsp://admin:canlin12345@192.168.1.200:554/Streaming/Channels/502?transportmode=unicast";

        String device_id = "19h06s03g7";
        String equipmentId = "47";
        String rtsp_address = "rtsp://admin:a1234567@192.168.1.103:554/Streaming/Channels/202?transportmode=unicast";

        //final String SECRET = "";
        String base64Str = VideoUtil.getBase64Str(rtsp_address,device_id,equipmentId);
        System.out.println("base64Str--->"+base64Str);
        String getToken = VideoUtil.getToken(rtsp_address,device_id,equipmentId);
        System.out.println("getToken--->"+getToken);

        String returnVal = HttpOk.post("http://58.215.171.233:18080/gsms/video/getCameraM3u8Url?token=" + getToken, base64Str);
        System.out.println("returnVal--->"+returnVal);



//        String postUrlStr = "http://58.215.171.233:18080/gsms/video/set_video_device_info";
//        HttpOk.post(postUrlStr, "{\"id\":\"ig_qft2rg3s9u5_6\",\"deviceId\":\"19g06k03r4\",\"name\":\"达永推流设备1\"}");

    }
}
