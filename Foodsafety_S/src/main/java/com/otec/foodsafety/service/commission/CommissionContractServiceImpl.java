package com.otec.foodsafety.service.commission;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.commission.CommissionContract;
import com.otec.foodsafety.entity.commission.CommissionContractFiles;
import com.otec.foodsafety.entity.commission.CommissionContractProduct;
import com.otec.foodsafety.entity.commission.CommissionEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.commission.CommissionContractFilesMapper;
import com.otec.foodsafety.mapper.commission.CommissionContractMapper;
import com.otec.foodsafety.mapper.commission.CommissionContractProductMapper;
import com.otec.foodsafety.mapper.commission.CommissionEnterpriseMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: weichang
 * @Date: 2018/12/11 0011 下午 14:38
 */
@Service
@Transactional
public class CommissionContractServiceImpl extends BaseServiceImpl<CommissionContract, Long> implements CommissionContractService{
    @Autowired
    public void setMapper(CommissionContractMapper mapper) {
        setGenericMapper(mapper);
    }
    @Autowired
    private SysResourceMapper sysResourceMapper;
    @Autowired
    private CommissionContractMapper contractMapper;
    @Autowired
    private CommissionContractFilesMapper contractFilesMapper;
    @Autowired
    private CommissionContractProductMapper contractProductMapper;
    @Autowired
    private CommissionEnterpriseMapper commissionEnterpriseMapper;

    @Override
    public void save(List<CommissionEnterprise> commissionEnterpriseList, CommissionContract contract, List<SysResource> sysResources/*, CommissionContractProduct contractProduct*/) {
        CommissionEnterprise clientEnterprise = commissionEnterpriseList.get(0);//委托方企业
        CommissionEnterprise consigneeEnterprise = commissionEnterpriseList.get(1);//被委托方企业
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("organizingInstitutionBarCode",clientEnterprise.getOrganizingInstitutionBarCode());
        if(commissionEnterpriseMapper.findEntitysByCondition(param).size()==0){//判断企业是否在委托加工企业信息表中
            commissionEnterpriseMapper.persist(clientEnterprise);
        }else{
            clientEnterprise = commissionEnterpriseMapper.findEntitysByCondition(param).get(0);
        }
        param.put("organizingInstitutionBarCode",consigneeEnterprise.getOrganizingInstitutionBarCode());
        if(commissionEnterpriseMapper.findEntitysByCondition(param).size()==0){//判断企业是否在委托加工企业信息表中
            commissionEnterpriseMapper.persist(consigneeEnterprise);
        }else{
            consigneeEnterprise = commissionEnterpriseMapper.findEntitysByCondition(param).get(0);
        }
        contract.setClientEnterpriseId(clientEnterprise.getCommissionEnterpriseId());
        contract.setConsigneeEnterpriseId(consigneeEnterprise.getCommissionEnterpriseId());
        contract.setContractStatus("1");//未提交审核
        contractMapper.persist(contract);
        for(SysResource resource :sysResources){
            sysResourceMapper.persist(resource);
            CommissionContractFiles contractFiles = new CommissionContractFiles();
            contractFiles.setContractId(contract.getContractId());
            contractFiles.setResourceId(resource.getResourceId());
            contractFilesMapper.persist(contractFiles);
        }
    }
}
