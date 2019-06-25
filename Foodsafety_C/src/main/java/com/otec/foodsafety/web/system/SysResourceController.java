package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.util.SysInitConfig;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/resource")
public class SysResourceController extends VueBaseController<SysResourceService, SysResource, Long> {
    @Autowired
    SysResourceService sysResourceService;

    public ObjectRestResponse<SysResource> get(@PathVariable Long id) {
        SysResource sysResource = sysResourceService.findById(id);
        if (sysResource != null) {
//            String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
//            sysResource.setResourcePath(imageServerUrl + '/' + sysResource.getResourcePath());
            return new ObjectRestResponse<SysResource>().rel(true)
                    .data(sysResource);
        } else {
            ObjectRestResponse resp = new ObjectRestResponse<SysResource>().rel(false);
            resp.setStatus(500);
            resp.setMessage("资源获取异常");
            return resp;
        }
    }

}
