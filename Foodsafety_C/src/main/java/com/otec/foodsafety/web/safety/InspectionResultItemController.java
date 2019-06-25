package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.InspectionResultItem;
import com.otec.foodsafety.service.safety.InspectionResultItemService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/inspection/result/item")
public class InspectionResultItemController extends VueBaseController<
        InspectionResultItemService, InspectionResultItem, Long> {
    @Autowired
    InspectionResultItemService inspectionResultItemService;

    @RequestMapping(value = "/getInspectionResultItem", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getInspectionResultItem(@RequestParam Map<String, Object> params) {
        List<Map<String, Object>> list = inspectionResultItemService.getInspectionResultItem(params);
        if (list != null && !list.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true)
                    .data(list);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }

}
