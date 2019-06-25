package com.otec.foodsafety.api;

import com.otec.foodsafety.service.system.SysDictService;
import com.otec.foodsafety.util.SysInitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/dict/")
@RestController
public class DictInterfafce {

    @Autowired
    SysDictService sysDictService;

    @GetMapping(value = "loadStaticData")
    public ResponseEntity<?> loadStaticData() {
        return ResponseEntity.ok(sysDictService.loadStaticData());
    }



    @GetMapping(value = "getImageServerUrl")
    public ResponseEntity<?> getImageServerUrl() {
        Map<Object, Object> item = new HashMap<Object, Object>();
        String imageServerUrl = SysInitConfig.getInstance().get(SysInitConfig.CfgProp.IMAGESERVERURL);
        item.put("imageServerUrl",imageServerUrl);
        return ResponseEntity.ok(item);
    }
}
