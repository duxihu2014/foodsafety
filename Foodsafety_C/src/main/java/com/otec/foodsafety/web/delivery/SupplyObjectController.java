package com.otec.foodsafety.web.delivery;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.otec.foodsafety.entity.delivery.SupplyObject;
import com.otec.foodsafety.entity.delivery.SupplyObjectEnterprise;
import com.otec.foodsafety.entity.enterprise.EnterpriseBase;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.sensor.SensorMonitor;
import com.otec.foodsafety.entity.system.SysUser;
import com.otec.foodsafety.service.delivery.SupplyObjectEnterpriseService;
import com.otec.foodsafety.service.delivery.SupplyObjectService;
import com.otec.foodsafety.service.sensor.SensorMonitorService;
import com.otec.foodsafety.service.util.DateLocalService;
import com.otec.foodsafety.util.StringUtils;
import com.otec.foodsafety.web.VueBaseController;
import com.otec.foodsafety.web.context.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/delivery/supplyObject")
public class SupplyObjectController extends VueBaseController<SupplyObjectService, SupplyObject, Long> {
    @Autowired
    DateLocalService dateLocalService;
    @Autowired
    SupplyObjectService supplyObjectService;
    @Autowired
    SupplyObjectEnterpriseService supplyObjectEnterpriseService;
    @Autowired
    SensorMonitorService sensorMonitorService;
    @Autowired
    SessionFilter sessionFilter;

    /**
     * 获取所有的可选配送企业信息（已建档的企业+已添加到配送单位表的企业）
     * @return
     */
    @RequestMapping(value="/getAllEnterprise",method = RequestMethod.GET)
    public ObjectRestResponse<Map<String,Object>> getEnterprise(){
        try{
            Map<String,Object> result = new HashMap<>();
            result.put("enterpriseData", supplyObjectService.getAllEnterprise());
            return new ObjectRestResponse<Map<String,Object>>().rel(true).data(result);
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Map<String,Object>>().rel(false);
            resp.setStatus(500);
            resp.setMessage("获取数据失败");
            return resp;
        }
    }

    /**
     * 新增供应对象
     * @param supplyObject
     * @return
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public ObjectRestResponse<SupplyObject> addSupplyObject(@RequestBody SupplyObject supplyObject){
        try {
            SysUser sysUser = sessionFilter.getJWTUser(request);
            Long enterpriseId = sysUser.getEnterpriseId();
            supplyObjectService.add(supplyObject,enterpriseId);
            return new ObjectRestResponse<SupplyObject>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplyObject>().rel(false);
            resp.setStatus(500);
            resp.setMessage("新增失败!");
            return resp;
        }
    }

    /**
     * 根据条件获取所有配送对象企业分页数据
     * @param params
     * @return
     */
    @Override
    public GridDataModel list(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = supplyObjectEnterpriseService.getPageCount(params);
        List<SupplyObject> list = supplyObjectEnterpriseService.getPage(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }
    @Override
    public List<SupplyObject> all(@RequestParam Map<String, Object> params) {
        return supplyObjectEnterpriseService.getAll(params);
    }

    /**
     * 根据id删除配送对象企业
     * @param ids
     * @return
     */
    @RequestMapping(value = "/all/{ids}", method = RequestMethod.DELETE)
    public ObjectRestResponse<SupplyObjectEnterprise> removeByIds(@PathVariable String ids) {
        try {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(ids)) {
                String [] strArr = ids.split(",");
                for( String id : strArr){
                    supplyObjectEnterpriseService.removeById(Long.valueOf(id));
                }
            }
            return new ObjectRestResponse<SupplyObjectEnterprise>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<SupplyObjectEnterprise>().rel(false);
            resp.setStatus(500);
            resp.setMessage("删除失败");
            return resp;
        }
    }

    /**
     * 获取所有集中用餐企业 分页数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/getEnterprsieBase/list", method = RequestMethod.GET)
    public GridDataModel getPageHasSupplyObject(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());
        // 查询列表数据
        PageObject po = getPageObject(params);
        Integer total = supplyObjectEnterpriseService.getPageCountHasSupplyObject(params);
        List<EnterpriseBase> list = supplyObjectEnterpriseService.getPageHasSupplyObject(params,po.getOffset(),po.getPageSize());
        GridDataModel model = new GridDataModel(list,total);
        return model;
    }

    /**
     * 获取所有集中用餐企业
     * @param params
     * @return
     */
    @RequestMapping(value = "/getEnterprsieBase/all", method = RequestMethod.GET)
    public  List<EnterpriseBase> getAllHasSupplyObject(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());
        // 查询列表数据
        List<EnterpriseBase> list = supplyObjectEnterpriseService.getAllHasSupplyObject(params);
        return list;
    }

    /**
     * 获取地图组件加载的数据
     * @param params
     * @return
     */
    @RequestMapping(value="/getMapData",method = RequestMethod.GET)
    public  List getMapData(@RequestParam Map<String, String> params) {

        SysUser sysUser = sessionFilter.getJWTUser(request);
        if ("2".equals(sysUser.getUserType()) || "3".equals(sysUser.getUserType())){

            //如果没有选择查询条件中的地区，默认只查询登录用户所在地区内的数据
            if(StringUtils.isEmpty(params.get("areaId")) ){
                params.put("areaId",sysUser.getAreaId()+"");
            }

        } else if("4".equals(sysUser.getUserType()))
            params.put("enterpriseId",sysUser.getEnterpriseId().toString());
        // 查询列表数据
        List<EnterpriseBase> enterpriseBaseList = supplyObjectEnterpriseService.getAllHasSupplyObject(params);
        List result = new ArrayList();
        for(EnterpriseBase eb : enterpriseBaseList){
            Map map = new HashMap();
            map.put("fromData",eb);
            Map param = new HashMap();
            param.put("enterpriseId",eb.getEnterpriseId());
            map.put("toData",supplyObjectEnterpriseService.getAll(param));
            result.add(map);
        }
        return result;
    }

  @RequestMapping(value="/getSensorMonitor",method = RequestMethod.GET)
  public  ObjectRestResponse<SensorMonitor> getSensorMonitor(@RequestParam Map<String, Object> params) {
      try {
          List<SensorMonitor> result = sensorMonitorService.getSupplySensorMonitor(params);
          return new ObjectRestResponse<SensorMonitor>().rel(true).data(result);
      } catch (Exception e) {
          e.printStackTrace();
          ObjectRestResponse resp = new ObjectRestResponse<SensorMonitor>().rel(false);
          resp.setStatus(500);
          resp.setMessage("获取传感器信息失败");
          return resp;
      }
  }
}
