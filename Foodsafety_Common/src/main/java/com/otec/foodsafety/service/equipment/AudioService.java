package com.otec.foodsafety.service.equipment;

import org.springframework.remoting.service.annotation.RemoteService;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.equipment.Audio;

@RemoteService
public interface AudioService extends BaseService<Audio,Long>{

}
