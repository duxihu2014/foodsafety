package com.otec.foodsafety.service.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otec.foodsafety.entity.operation.CateringStaff;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.purchase.SupplierServiceMapper;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.CommonUtil;
import com.otec.foodsafety.util.HttpURLConnectionUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.purchase.Materials;
import com.otec.foodsafety.mapper.purchase.MaterialsMapper;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MaterialsServiceImpl extends BaseServiceImpl<Materials, Long> implements MaterialsService {

	@Autowired
	SysResourceService sysResourceMapper;
	@Autowired
	MaterialsMapper materialsMapper;
	@Autowired
	SupplierServiceMapper supplierServiceMapper;


	@Autowired
	public void setMapper(MaterialsMapper mapper) {
		setGenericMapper(mapper);
	}

	@Override
	public List<Materials> findMaterialsByCondition(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return materialsMapper.findMaterialsByCondition(condition);
	}

	@Override
	public Materials findMaterialByCondition(Map<String, Object> condition) {

		List<Materials> materialslist = materialsMapper.findMaterialsByCondition(condition);
		if (materialslist != null && materialslist.size() == 1) {
			return materialslist.get(0);
		} else {
			return null;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void add(Materials entity, MultipartFile imageFile, String uploadUrl, String imageFolder) throws  Exception {

		if(imageFile!=null){
			String fileName = imageFile.getOriginalFilename();
			String str = HttpURLConnectionUtils.sendMessage(uploadUrl + "?fileName=" + fileName+ "&imageFolder="+imageFolder,
					imageFile.getBytes());
			JSONObject jSONObject = JSON.parseObject(str);
			String path = jSONObject.getString("imgUrl");
			String subfix= fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
			String rename = path.split("/")[path.split("/").length-1];
			//2.将图片信息写入数据库
			SysResource resource = new SysResource();
			resource.setResourceName(fileName);
			resource.setResourceRename(rename);
			resource.setResourceStorage(ResourceStorage.LOCAL.toString());
			resource.setResourceExtension(subfix);
			resource.setResourceStatus("1");
			resource.setResourceType(ResourceType.getTypeBySuffix(resource.getResourceExtension()));
			resource.setResourceLength(imageFile.getSize());
			resource.setResourcePath( path );
			sysResourceMapper.persist(resource);
			entity.setProductionCertificatePhoto(resource.getResourceId());
		}
		entity.setMaterialStatus("1");
		materialsMapper.persist(entity);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateMaterial(Materials entity, SysResource resource) throws Exception {
		if(resource==null){
			entity.setProductionCertificatePhoto(null);
		}else{
			if(entity.getProductionCertificatePhoto()!=null){
				SysResource old=sysResourceMapper.findById(entity.getProductionCertificatePhoto());
				CommonUtil.copyBean(resource, old, false);
				sysResourceMapper.updateById(old);
				entity.setProductionCertificatePhoto(old.getResourceId());
			}else{
				sysResourceMapper.persist(resource);
				entity.setProductionCertificatePhoto(resource.getResourceId());
			}
		}
		materialsMapper.updateById(entity);
	}

	@Override
	public Integer getPageCount(Map<String, String> params) {
		return materialsMapper.getPageCount(params);
	}

	@Override
	public List<Materials> getPage(Map<String, String> params, int start, int pageSize) {
		RowBounds rowBounds = new RowBounds(start, pageSize);
		return materialsMapper.getPage(params,rowBounds);
	}

	@Override
	public Materials findByMaterialId(Long id) {
		Materials materials=materialsMapper.findMaterialsById(id);
		if(materials.getProductionCertificatePhoto()!=null){
			SysResource resource= sysResourceMapper.findById(materials.getProductionCertificatePhoto());
			if(resource!=null)
				materials.setResourcePath(resource.getResourcePath());
		}
		return materials;
	}


	@Override
	public void batchChangeStatus(Long[] ids, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ids", ids);
		map.put("status", status);
		materialsMapper.batchChangeStatus(map);
	}

}
