package com.otec.foodsafety.util;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.system.SysResourceMapper;

/**
 * 图片服务类，
 * 
 * @author Administrator
 *
 */
public class ImageUtil {
	/**
	 * 上改图片到指定文件夹并将相应信息保存到resource表
	 * 
	 * @param uploadUrl      上传文件服务URl
	 * @param imageFolder    保存文件夹
	 * @param resource  上传图片
	 * @param resourceMapper
	 * @return resourceId
	 * @throws IOException
	 * @throws Exception
	 */
	public static Long ImageUpload(String uploadUrl, String imageFolder, SysResource resource,
			SysResourceMapper resourceMapper) throws IOException, Exception {
		String fileName = resource.getResourceName();
		byte[] fileByte = resource.getResourceContent();
		// 1.1先将图片上传服务器
		String str = HttpURLConnectionUtils.sendMessage(
				uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder, fileByte);
		JSONObject jSONObject = JSON.parseObject(str);
		String path = jSONObject.getString("imgUrl");
		String subfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
		String rename = path.split("/")[path.split("/").length - 1];
		// 1.2.将图片信息写入数据库
		resource.setResourceName(fileName);
		resource.setResourceRename(rename);
		resource.setResourceStorage(ResourceStorage.REMOTE.toString());
		resource.setResourceExtension(subfix);
		resource.setResourceStatus("1");
		resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
		resource.setResourcePath(path);
		resource.setResourceContent(null);
		resourceMapper.persist(resource);
		return resource.getResourceId();
	}

	/**
	 * 上改图片到指定文件夹并将返回路径
	 * 
	 * @param uploadUrl
	 * @param imageFolder
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String ImageUpload(String uploadUrl, String imageFolder, MultipartFile multipartFile)
			throws IOException, Exception {
		String fileName = multipartFile.getOriginalFilename();
		// 1.1先将图片上传服务器
		String str = HttpURLConnectionUtils.sendMessage(
				uploadUrl + "?fileName=" + fileName + "&imageFolder=" + imageFolder, multipartFile.getBytes());
		JSONObject jSONObject = JSON.parseObject(str);
		String path = jSONObject.getString("imgUrl");
		String subfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());// 文件后缀
		String rename = path.split("/")[path.split("/").length - 1];
		// 返回图片服务器相对路径
		return path;
	}
}
