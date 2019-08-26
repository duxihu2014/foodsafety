package com.otec.foodsafety.api.enterprise;

import com.otec.foodsafety.api.BaseInterface;
import com.otec.foodsafety.entity.equipment.EquipmentPushflowInfo;
import com.otec.foodsafety.service.equipment.EquipmentPushflowInfoService;
import com.otec.foodsafety.util.VideoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/equipmentPushflowInfo")
@RestController
public class EquipmentPushflowInfoInterface extends BaseInterface {

    @Autowired
    EquipmentPushflowInfoService equipmentPushflowInfoService;


    /**
     *  通过视频推流设备ID，拿详情信息
     * @param
     * @return
     */
    @RequestMapping(value="/equipmentPushflowInfo/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    public EquipmentPushflowInfo equipmentPushflowInfo(@PathVariable Long id) {
        return equipmentPushflowInfoService.findById(id);
    }

    /**
     *  通过视频推流设备ID，拿详情信息
     * @param
     * @return
     */
    @RequestMapping(value="/getToken" ,method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getToken(  @RequestParam(name = "rtsp_address", required = true, defaultValue = "") String rtsp_address,
                          @RequestParam(name = "device_id", required = true, defaultValue = "") String device_id,
                          @RequestParam(name = "equipmentId", required = true, defaultValue = "") String equipmentId) {
        String base64Str = VideoUtil.getBase64Str(rtsp_address,device_id,equipmentId);
        System.out.println("base64Str--->"+base64Str);
        String getToken = VideoUtil.getToken(rtsp_address,device_id,equipmentId);
        System.out.println("getToken--->"+getToken);
        Map<String, Object> params= new HashMap<>();
        params.put("base64Str",base64Str);
        params.put("getToken",getToken);
        return ResponseEntity.ok(params);
    }

}
