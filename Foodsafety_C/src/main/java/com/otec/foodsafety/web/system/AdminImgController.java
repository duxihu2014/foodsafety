package com.otec.foodsafety.web.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.util.SysInitConfig.CfgProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 图片上传管理
 * 
 * @author nier
 * @description
 * @date 2018-05-10
 */
@RestController
@RequestMapping(value = "/api/admin/img")
public class AdminImgController {
	Logger log = LoggerFactory.getLogger(AdminImgController.class);

	@RequestMapping(value = "init")
	public String upload(HttpServletRequest request) {
		return "img/upload";
	}

	@RequestMapping(value = "initSelect")
	public String select(HttpServletRequest request) {

		return "img/select";
	}

	@RequestMapping(value = "upload")
	public ResponseEntity<String> upload(HttpServletRequest request, HttpServletResponse response) {
		String uploadUrl = SysInitConfig.getInstance().get(CfgProp.UPLOADURL);
		String imageFolder = SysInitConfig.getInstance().get(CfgProp.IMAGEFOLDER);
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multiRequest.getFile("file");
		String s = "";
		try {
			s = HttpURLConnectionUtils.sendMessage(
					uploadUrl + "?fileName=" + multipartFile.getOriginalFilename() + "&imageFolder="+imageFolder,
					multipartFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jSONObject = JSON.parseObject(s);
		String fileName = jSONObject.getString("imgUrl");
		return ResponseEntity.ok(fileName);
		//writeToPage(fileName, response);
	}

	@RequestMapping(value = "uploadDo2")
	public ResponseEntity<String> uploadDo2(HttpServletRequest request, HttpServletResponse response) {
		String url = System.getProperty("user.dir") + "/src/main/webapp/static/img";
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multiRequest.getFile("file");
		String time = String.valueOf(new Date().getTime());
		String fileName = multipartFile.getOriginalFilename();
		String[] str2 = fileName.split("\\.");
		fileName = time + "." + str2[str2.length - 1];
		File targetFile = new File(url, fileName);

		try {
			multipartFile.transferTo(targetFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileUrl = fileName;
		return ResponseEntity.ok(fileUrl);
	}
}
