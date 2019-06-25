package com.otec.foodsafety.web.commission;

import com.otec.foodsafety.entity.commission.CommissionContract;
import com.otec.foodsafety.entity.commission.CommissionContractProduct;
import com.otec.foodsafety.entity.commission.CommissionEnterprise;
import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.commission.CommissionContractService;
import com.otec.foodsafety.service.commission.CommissionEnterpriseService;
import com.otec.foodsafety.util.JSONUtils;
import com.otec.foodsafety.util.ResourceStorage;
import com.otec.foodsafety.util.ResourceType;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:47
 */
@RestController
@RequestMapping(value = "api/commission/contract")
public class CommissionContractController extends VueBaseController<CommissionContractService, CommissionContract, Long> {
    @Autowired
    private CommissionEnterpriseService commissionEnterpriseService;
    @Autowired
    private CommissionContractService commissionContractService;

    /**
     * 新增委托加工合同
     * @param clientEnterpriseStr
     * @param consigneeEnterpriseStr
     * @param commissionContractStr
     * @param multipartFiles
     * @param commissionContractProductStr
     * @return
     */
    @RequestMapping(value="/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ObjectRestResponse<CommissionContract> changeBase(
            @RequestParam(value = "clientEnterprise") String clientEnterpriseStr,
            @RequestParam(value = "consigneeEnterprise") String consigneeEnterpriseStr,
            @RequestParam(value = "commissionContract") String commissionContractStr,
            @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestParam(value = "commissionContractProduct") String commissionContractProductStr) {
        String fileName, fileSubfix;
        try {
            CommissionEnterprise clientEnterprise = JSONUtils.fromJson(clientEnterpriseStr, CommissionEnterprise.class);//委托方企业
            CommissionEnterprise consigneeEnterprise = JSONUtils.fromJson(consigneeEnterpriseStr, CommissionEnterprise.class);//被委托方企业
            List<CommissionEnterprise> commissionEnterpriseList =new ArrayList<>();
            commissionEnterpriseList.add(clientEnterprise);
            commissionEnterpriseList.add(consigneeEnterprise);
            CommissionContract commissionContract = JSONUtils.fromJson(commissionContractStr,CommissionContract.class);//委托加工合同
            //CommissionContractProduct commissionContractProduct = JSONUtils.fromJson(commissionContractProductStr,CommissionContractProduct.class);
            //commissionContractProduct.setContractId(commissionContract.getContractId());
            List<SysResource> sysResourceList = new ArrayList<>();
            for(MultipartFile file : multipartFiles){
                fileName = file.getOriginalFilename();
                fileSubfix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//文件后缀
                SysResource sysResource = new SysResource();
                sysResource.setResourceName(fileName);
                sysResource.setResourceStorage(ResourceStorage.LOCAL.toString());
                sysResource.setResourceExtension(fileSubfix);
                sysResource.setResourceStatus("1");
                sysResource.setResourceType(ResourceType.getTypeBySuffix(fileSubfix));
                sysResource.setResourceContent(file.getBytes());
                sysResource.setResourceLength(file.getSize());
                sysResourceList.add(sysResource);
            }
            commissionContractService.save(commissionEnterpriseList,commissionContract,sysResourceList);
            return new ObjectRestResponse<CommissionContract>().rel(true);
        } catch (Exception e) {
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<CommissionContract>().rel(false);
            resp.setStatus(500);
            resp.setMessage("添加备案信息失败");
            return resp;
        }
    }

    /**
     * 获取所有企业信息（建档企业+已经添加的省外企业）
     * @return
     */
    @RequestMapping(value="/getEnterprise", method = RequestMethod.GET)
    public  ObjectRestResponse<Map<String,Object>> getEnterprise(){
        try{
            Map<String,Object> result = new HashMap<>();
            result.put("enterpriseData", commissionEnterpriseService.getAllEnterprise());
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
     * 获取已经备案的企业信息
     * @return
     */
    @RequestMapping(value="/getCommissionEnterprise", method = RequestMethod.GET)
    public  ObjectRestResponse<CommissionEnterprise> getCommissionEnterprise(@RequestParam Map<String, Object> params){
        try{
            return new ObjectRestResponse<Map<String,Object>>().rel(true).data(commissionEnterpriseService.findEntitysByCondition(params));
        }catch(Exception e){
            e.printStackTrace();
            ObjectRestResponse resp = new ObjectRestResponse<Map<String,Object>>().rel(false);
            resp.setStatus(500);
            resp.setMessage("获取数据失败");
            return resp;
        }
    }

}
