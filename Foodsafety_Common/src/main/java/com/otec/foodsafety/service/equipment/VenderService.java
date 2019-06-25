package com.otec.foodsafety.service.equipment;

import org.springframework.remoting.service.annotation.RemoteService;
import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.Vender;

@RemoteService
public interface VenderService extends BaseService<Vender, Long> {

}
