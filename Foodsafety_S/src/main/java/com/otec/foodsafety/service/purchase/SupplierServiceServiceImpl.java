package com.otec.foodsafety.service.purchase;

import com.cykj.grcloud.service.impl.base.BaseServiceImpl;
import com.otec.foodsafety.entity.purchase.SupplierService;
import com.otec.foodsafety.entity.purchase.SupplierServiceEnterprise;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.mapper.purchase.SupplierServiceEnterpriseMapper;
import com.otec.foodsafety.mapper.purchase.SupplierServiceMapper;
import com.otec.foodsafety.mapper.system.SysResourceMapper;
import com.otec.foodsafety.util.CommonUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupplierServiceServiceImpl extends BaseServiceImpl<
        SupplierService, Long> implements SupplierServiceService {
    @Autowired
    SupplierServiceMapper supplierServiceMapper;
    @Autowired
    SupplierServiceEnterpriseMapper supplierServiceEnterpriseMapper;
    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    public void setMapper(SupplierServiceMapper mapper) {
        setGenericMapper(mapper);
    }

    @Override
    public Integer getSupplierServiceCount(Map<String, String> params) {
        return supplierServiceMapper.getSupplierServiceCount(params);
    }

    @Override
    public List<SupplierService> getSupplierServicePage(Map<String, String> params, int start, int pageSize) {
        RowBounds rowBounds = new RowBounds(start, pageSize);
        return supplierServiceMapper.getSupplierServicePage(params, rowBounds);
    }

    @Override
    public void batchChangeStatus(Long[] ids, String status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ids", ids);
        map.put("status", status);
        supplierServiceMapper.batchChangeStatus(map);
    }

    @Override
    public void create(SupplierService supplierService, SysResource idCardFront,
                       SysResource idCardBack, SysResource businessLicence, SupplierServiceEnterprise supplierServiceEnterprise) {
        // 保存图片数据到数据库
        if (idCardFront != null) {
            sysResourceMapper.persist(idCardFront);
            supplierService.setIdCardNoPhotoFront(idCardFront.getResourceId());
        }
        if (idCardBack != null) {
            sysResourceMapper.persist(idCardBack);
            supplierService.setIdCardNoPhotoBack(idCardBack.getResourceId());
        }
        if (businessLicence != null) {
            sysResourceMapper.persist(businessLicence);
            supplierService.setBusinessLicencePhoto(businessLicence.getResourceId());
        }
        // 创建供货商信息
        supplierServiceMapper.persist(supplierService);
        // 保存企业供货商关系数据
        supplierServiceEnterprise.setSupplierId(supplierService.getSupplierId());
        supplierServiceEnterpriseMapper.persist(supplierServiceEnterprise);
    }

    @Override
    public void update(SupplierService supplierService, SysResource idCardFront,
                       SysResource idCardBack, SysResource businessLicence) throws Exception {
        SupplierService entity = supplierServiceMapper.findById(supplierService.getSupplierId());
        // 更新图片数据到数据库
        if (idCardFront != null && idCardFront.getResourcePath() != null) {
            sysResourceMapper.persist(idCardFront);
            supplierService.setIdCardNoPhotoFront(idCardFront.getResourceId());
        } else if (idCardFront != null && idCardFront.getResourcePath() == null) {
            // 删除图片
            if (entity.getIdCardNoPhotoFront() != null) {
                idCardFront = sysResourceMapper.findById(entity.getIdCardNoPhotoFront());
                idCardFront.setResourceStatus("0");
                sysResourceMapper.updateById(idCardFront);

                supplierService.setIdCardNoPhotoFront(null);
            }
        } else {}
        if (idCardBack != null && idCardBack.getResourcePath() != null) {
            sysResourceMapper.persist(idCardBack);
            supplierService.setIdCardNoPhotoBack(idCardBack.getResourceId());
        } else if (idCardBack != null && idCardBack.getResourcePath() == null) {
            // 删除图片
            if (entity.getIdCardNoPhotoBack() != null) {
                idCardBack = sysResourceMapper.findById(entity.getIdCardNoPhotoBack());
                idCardBack.setResourceStatus("0");
                sysResourceMapper.updateById(idCardBack);

                supplierService.setIdCardNoPhotoBack(null);
            }
        } else {}
        if (businessLicence != null && businessLicence.getResourcePath() != null) {
            sysResourceMapper.persist(businessLicence);
            supplierService.setBusinessLicencePhoto(businessLicence.getResourceId());
        } else if (businessLicence != null && businessLicence.getResourcePath() == null) {
            // 删除图片
            if (entity.getBusinessLicencePhoto() != null) {
                businessLicence = sysResourceMapper.findById(entity.getBusinessLicencePhoto());
                businessLicence.setResourceStatus("0");
                sysResourceMapper.updateById(businessLicence);

                supplierService.setBusinessLicencePhoto(null);
            }
        } else {}
        // 更新供货商信息
        CommonUtil.copyBean(supplierService, entity, false);
        supplierServiceMapper.updateById(entity);
    }

    @Override
    public List<SupplierService> getSuppliersByEnterpriseId(Map<String, Object> map) {
        return  supplierServiceMapper.getSuppliersByEnterpriseId(map);
    }

}
