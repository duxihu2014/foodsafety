package com.otec.foodsafety.web.equipment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpOk {

//    public static void main(String[] args) throws IOException {
//
//
//
//    }

    public static String post(String urlStr, String body) throws IOException {
        URL url = new URL(urlStr);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置Content-Type
        connection.setRequestProperty("Content-Type", "application/json");
        // 设置是否向httpUrlConnection输出，post请求设置为true，默认是false
        connection.setDoOutput(true);

        // 设置RequestBody
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
        printWriter.write(body);
        printWriter.flush();

        // 返回结果-字节输入流转换成字符输入流，控制台输出字符
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb);

        return sb.toString();
    }
}
