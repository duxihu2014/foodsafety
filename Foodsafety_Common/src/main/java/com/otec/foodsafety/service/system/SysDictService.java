package com.otec.foodsafety.service.system;

import com.cykj.grcloud.service.base.BaseService;
import com.otec.foodsafety.entity.system.SysDict;
import com.otec.foodsafety.util.vo.ComboData;
import org.springframework.remoting.service.annotation.RemoteService;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@RemoteService
public interface SysDictService extends BaseService<SysDict, Long> {

	public Map<String, String> getDetailNameMap(String dictName);// 得到明细map<name,value>

	public Map<String, String> getDetailValueMap(String dictName);// 得到明细map<value,name>

	public String getDetailValue(String dictName, String detailName);// 得到明细value

	public Hashtable<String, List<ComboData>> loadStaticData();
}
