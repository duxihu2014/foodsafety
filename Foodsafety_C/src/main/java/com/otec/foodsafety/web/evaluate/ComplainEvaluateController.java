package com.otec.foodsafety.web.evaluate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.otec.foodsafety.entity.system.SysResource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.evaluate.ComplainEvaluate;
import com.otec.foodsafety.entity.jwt.AuthService;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.evaluate.ComplainEvaluateService;
import com.otec.foodsafety.service.system.SysAreaService;
import com.otec.foodsafety.service.system.SysDictService;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.util.vo.FrontUser;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;

/**
 * 投诉评价
 * 
 * @author Administrator
 *
 */

@RestController
@RequestMapping("api/admin/complainEvaluate")
public class ComplainEvaluateController extends VueBaseController<ComplainEvaluateService, ComplainEvaluate, Long> {
	@Autowired
	private SessionFilter sessionFilter;
	@Autowired
	private AuthService authService;
	@Autowired
	SysAreaService sysAreaService;
	@Autowired
	private ComplainEvaluateService complainEvaluateService;
	@Autowired
	SysDictService sysDictService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel list(@RequestParam Map<String, String> params) {
		
		String token = request.getHeader("access-token");
		FrontUser userInfo = authService.getUserInfo(token);
		if("4".equals(userInfo.getUserType())){//企业用户
			if(null!=userInfo.getEnterpriseId()&&!"".equals(userInfo.getEnterpriseId())){
				params.put("enterpriseId", userInfo.getEnterpriseId().toString());
			}else{
				return null;
			}
		}else if ("2".equals(userInfo.getUserType()) || "3".equals(userInfo.getUserType())) {
			if (StringUtils.isNotEmpty(params.get("enterpriseId"))) {
				params.put("enterpriseId", params.get("enterpriseId"));
			} else if (StringUtils.isNotEmpty(params.get("gridId"))) {
				params.put("gridId", params.get("gridId"));
			} else if (StringUtils.isNotEmpty(params.get("areaId"))) {
				List list = new ArrayList<>();
				list = sysAreaService.listAllChildId(params.get("areaId"), list);
				params.put("areaIds", StringUtils.join(list.toArray(), ','));
			} else {
				List list = new ArrayList<>();
				list = sysAreaService.listAllChildId(String.valueOf(userInfo.getAreaId()), list);
				params.put("areaIds", StringUtils.join(list.toArray(), ','));
			}
		}		
		
		params.put("extendSql", " and MIR.STATUS !=0");
		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);		 
		return model;

	}
	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<ComplainEvaluate> addComplainEvaluate(
			@RequestPart(value = "file", required = false) MultipartFile[] multipartFileArr,
			@RequestParam(value = "ComplainEvaluate") String complainEvaluateStr) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
			String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
			ComplainEvaluate complainEvaluate = JSONUtils.fromJson(complainEvaluateStr, ComplainEvaluate.class);
			List<SysResource> resourceList = new ArrayList<SysResource>();
			if (multipartFileArr != null && multipartFileArr.length > 0) {
				for (MultipartFile multipartFile : multipartFileArr) {
					SysResource resource = new SysResource();
					resource.setResourceContent(multipartFile.getBytes());
					resource.setResourceName(multipartFile.getOriginalFilename());
					resource.setResourceLength(multipartFile.getSize());
					resourceList.add(resource);
				}
			}
			complainEvaluateService.chargeComplainEvaluate(uploadUrl, imageFolder, resourceList,
					sysUser.getUserId(), complainEvaluate, "1");
		
			return new ObjectRestResponse<ComplainEvaluate>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}

	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/charge", method = RequestMethod.POST)
	@ResponseBody
	public ObjectRestResponse<ComplainEvaluate> updateComplainEvaluate(
			@RequestPart(value = "file", required = false) MultipartFile[] multipartFileArr,
			@RequestParam(value = "ComplainEvaluate") String complainEvaluateStr) {
		try {
			SysUser sysUser = sessionFilter.getJWTUser(request);
			String uploadUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.UPLOADURL);
			String imageFolder = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGEFOLDER);
			ComplainEvaluate complainEvaluate = JSONUtils.fromJson(complainEvaluateStr, ComplainEvaluate.class);
			List<SysResource> resourceList = new ArrayList<SysResource>();
			if (multipartFileArr != null && multipartFileArr.length > 0) {
				for (MultipartFile multipartFile : multipartFileArr) {
					SysResource resource = new SysResource();
					resource.setResourceContent(multipartFile.getBytes());
					resource.setResourceName(multipartFile.getOriginalFilename());
					resource.setResourceLength(multipartFile.getSize());
					resourceList.add(resource);
				}
			}
			complainEvaluateService.chargeComplainEvaluate(uploadUrl, imageFolder, resourceList,
					sysUser.getUserId(), complainEvaluate, "2");

			return new ObjectRestResponse<ComplainEvaluate>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
			resp.setStatus(500);
			resp.setMessage("error");
			return resp;
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ObjectRestResponse<ComplainEvaluate> remove(@PathVariable Long id) {
		try {
			ComplainEvaluate entity = biz.findById(id);
			entity.setStatus("0");// 无效
			biz.updateById(entity);
//			biz.removeById(id);
			return new ObjectRestResponse<ComplainEvaluate>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}

	@RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
	public ObjectRestResponse<ComplainEvaluate> remove(@PathVariable("ids") String ids) {
		try {
			String[] aDeleteId = ids.split(",");
			for (String id : aDeleteId) {
//				biz.removeById(Long.valueOf(id));
				ComplainEvaluate entity = biz.findById(Long.valueOf(id));
				entity.setStatus("0");// 无效
				biz.updateById(entity);
			}
			return new ObjectRestResponse<ComplainEvaluate>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);

			resp.setStatus(500);
			resp.setMessage("删除失败!");
			return resp;
		}
	}
	//受理
		@RequestMapping(value = "acceptance/{id}", method = RequestMethod.GET)
		@ResponseBody
		public ObjectRestResponse<ComplainEvaluate> acceptance(@PathVariable Long id) {

			try {
//				String answer = params.get("answer");
				SysUser sysUser = sessionFilter.getJWTUser(request);
				ComplainEvaluate entity = biz.findById(id);

				entity.setStatus("2");// 已受理待处理
				
				entity.setAcceptanceId(Integer.valueOf(sysUser.getUserId() + ""));
				entity.setAcceptanceTime(new Date());

				biz.updateById(entity);

				return new ObjectRestResponse<ComplainEvaluate>().rel(true);
			} catch (Exception e) {
				e.printStackTrace();
				ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
				resp.setStatus(500);
				resp.setMessage("系统错误，联系管理员");
				return resp;

			}
		}
		/**
		 * 处理
		 * @param id
		 * @param params
		 * @return
		 */
		@RequestMapping(value = "handleEdit/{id}", method = RequestMethod.GET)
		@ResponseBody
		public ObjectRestResponse<ComplainEvaluate> handleEdit(@PathVariable Long id,
				@RequestParam Map<String, String> params) {

			try {
				String handleResult = params.get("handleResult");//处理结果
				String personLiable = params.get("personLiable"); //责任人
				String personLiableTel = params.get("personLiableTel");//责任人电话
				
				
				SysUser sysUser = sessionFilter.getJWTUser(request);
				ComplainEvaluate entity = biz.findById(id);

				entity.setStatus("3");// 已处理
				entity.setHandleResult(handleResult);
				entity.setPersonLiable(personLiable);
				entity.setPersonLiableTel(personLiableTel);
				entity.setHandleManId(Integer.valueOf(sysUser.getUserId() + ""));
				entity.setHandleTime(new Date());

				biz.updateById(entity);

				return new ObjectRestResponse<ComplainEvaluate>().rel(true);
			} catch (Exception e) {
				e.printStackTrace();
				ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
				resp.setStatus(500);
				resp.setMessage("系统错误，联系管理员");
				return resp;

			}
		}
	/**
	 * 受理回复
	 * 
	 * @param id
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "evaluateEdit/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjectRestResponse<ComplainEvaluate> evaluateEdit(@PathVariable Long id,
			@RequestParam Map<String, String> params) {

		try {
			String answer = params.get("answer");
			SysUser sysUser = sessionFilter.getJWTUser(request);
			ComplainEvaluate entity = biz.findById(id);

			entity.setStatus("2");// 受理完成
			entity.setWriting1(answer);
			entity.setCreateOp1(Integer.valueOf(sysUser.getUserId() + ""));
			entity.setCreateTime1(new Date());

			biz.updateById(entity);

			return new ObjectRestResponse<ComplainEvaluate>().rel(true);
		} catch (Exception e) {
			e.printStackTrace();
			ObjectRestResponse resp = new ObjectRestResponse<ComplainEvaluate>().rel(false);
			resp.setStatus(500);
			resp.setMessage("系统错误，联系管理员");
			return resp;

		}
	}
	

}
