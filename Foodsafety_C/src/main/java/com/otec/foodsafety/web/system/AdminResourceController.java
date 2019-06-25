package com.otec.foodsafety.web.system;

import com.otec.foodsafety.entity.system.SysResource;
import com.otec.foodsafety.service.system.SysResourceService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/admin/resouce")
public class AdminResourceController extends
        VueBaseController<SysResourceService, SysResource, Long> {
}
