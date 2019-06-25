package com.otec.foodsafety.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HttpURLConnectionUtils {
	protected static final Logger logger = LoggerFactory.getLogger(HttpURLConnectionUtils.class);
	
	protected static HttpURLConnection getHttpURLConnection(String requestUrl)
			throws Exception {
		logger.info("url = "+ requestUrl);
		URL url = new URL(requestUrl);
		URLConnection conn = url.openConnection();
		if (conn == null) {
			logger.error("打开接口地址失败: " + "【{}】",requestUrl);
			return null;
		}
		HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setUseCaches(false);
		httpUrlConnection.setRequestProperty("Charset", "UTF-8");
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setReadTimeout(5000);
		httpUrlConnection.connect();
		return httpUrlConnection;
	}

	public static String sendMessage(String requestURL,byte buffer[]) throws Exception {
		HttpURLConnection httpUrlConnection = null;
		DataOutputStream dataOutputStream = null;
		String result = null;
		try {
			httpUrlConnection = getHttpURLConnection(requestURL);
			dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
			dataOutputStream.write(buffer);
			dataOutputStream.flush();
			Scanner scanner = null; 
			if (httpUrlConnection.getResponseCode() != 200) {
				// 接收接口的返回报文
				scanner = new Scanner(httpUrlConnection.getErrorStream());
				scanner.useDelimiter("\\Z");
				result = "{\"success\":false,\"msg\":\""+scanner.next()+"\"}";
				//result = "{\"success\":false}";
				scanner.close();
			}else{
				scanner = new Scanner(httpUrlConnection.getInputStream());
				scanner.useDelimiter("\\Z"); 
				result = scanner.next();
				scanner.close();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			try {
				dataOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpUrlConnection.disconnect();
		}
	}
	
	
	public static String sendMessage(String requestURL) throws Exception {
		HttpURLConnection httpUrlConnection = null;
		String result = null;
		try {
			httpUrlConnection = getHttpURLConnection(requestURL);
			Scanner scanner = null; 
			if (httpUrlConnection.getResponseCode() != 200) {
				// 接收接口的返回报文
				scanner = new Scanner(httpUrlConnection.getErrorStream());
				scanner.useDelimiter("\\Z");
				result = "{\"success\":false,\"msg\":\""+scanner.next()+"\"}";
				//result = "{\"success\":false}";
				scanner.close();
			}else{
				scanner = new Scanner(httpUrlConnection.getInputStream());
				scanner.useDelimiter("\\Z"); 
				result = scanner.next();
				scanner.close(); 
				 
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw e;
		} finally {
			httpUrlConnection.disconnect();
		}
		//return "{\"success\":false,\"msg\":\"Connection refused!\"}";
	}
	
	 
	
	
	public static void main(String args[]) throws Exception{
		String s = HttpURLConnectionUtils.sendMessage("http://192.168.1.200:8680/IntelligentCity_IMG/receiveDataServlet?fileName=abc.xml&imageFolder=dbcity", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><city-config><uploadUrl>http://192.168.1.200:8680/IntelligentCity_IMG/receiveDataServlet</uploadUrl></city-config>".getBytes());
		System.out.println(s);
	}
}