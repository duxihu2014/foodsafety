package com.otec.foodsafety.web.safety;

import com.otec.foodsafety.entity.jwt.ObjectRestResponse;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;
import com.otec.foodsafety.service.safety.OnsiteVerificationResultItemService;
import com.otec.foodsafety.web.VueBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/safety/onsite/result/item")
public class OnsiteVerificationResultItemController extends VueBaseController<
        OnsiteVerificationResultItemService, OnsiteVerificationResultItem, Long> {
    @Autowired
    OnsiteVerificationResultItemService onsiteVerificationResultItemService;

    @RequestMapping(value = "/getOnsiteVerificationResultItem", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Map<String, Object>> getOnsiteVerificationResultItem(@RequestParam Map<String, String> params) {
        List<Map<String, Object>> list = onsiteVerificationResultItemService.getResultItem(params);
        if (list != null && !list.isEmpty())
            return new ObjectRestResponse<Map<String, Object>>().rel(true)
                    .data(list);
        else
            return new ObjectRestResponse<Map<String, Object>>().rel(false);
    }
}
