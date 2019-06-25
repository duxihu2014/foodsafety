package com.otec.foodsafety.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.otec.foodsafety.entity.system.SysDetail;
import com.otec.foodsafety.entity.system.SysDict;
import com.otec.foodsafety.mapper.system.SysDetailMapper;
import com.otec.foodsafety.mapper.system.SysDictMapper;
import com.otec.foodsafety.util.vo.ComboData;
import com.otec.foodsafety.web.context.ObjectFactory;
import com.otec.foodsafety.web.context.OscacheFactory;
import com.otec.foodsafety.web.context.SystemCache;

public class DictCache {
	private static DictCache instance = null;

	private DictCache() {
	}

	public static synchronized DictCache getInstance() {
		if (instance == null) {
			instance = new DictCache();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	private static Hashtable<String, Map<String, SysDetail>> getDictFromCache() {
		Hashtable<String, Map<String, SysDetail>> dict = (Hashtable<String, Map<String, SysDetail>>) OscacheFactory
				.getInstance().getObject(SystemCache.CACHE_DICT);
		return dict;
	}
	
	@SuppressWarnings("unchecked")
	public static Hashtable<String, List<ComboData>> loadStaticDataFromCache() {
		Hashtable<String,List<ComboData>> dict = (Hashtable<String, List<ComboData>>) OscacheFactory
				.getInstance().getObject(SystemCache.CACHE_DICT);
		return dict;
	}

	public static synchronized void reloadData() {
		SystemCache.reCacheDict();
	}

	public Hashtable<String, Map<String, SysDetail>> loadData() {
		SysDictMapper sDictMapper = (SysDictMapper) ObjectFactory
				.getBean(SysDictMapper.class);
		SysDetailMapper SysDetailMapper = (SysDetailMapper) ObjectFactory
				.getBean(SysDetailMapper.class);
		Hashtable<String, Map<String, SysDetail>> dict = new Hashtable<String, Map<String, SysDetail>>();
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("detailStatus", 1);
		List<SysDict> sDictList = sDictMapper.findEntitysByCondition(null);
		List<SysDetail> SysDetailList = SysDetailMapper
				.findEntitysByCondition(para);
		Map<String, SysDetail> dictDetailMap = null;
		for (SysDict sd : sDictList) {
			String dictId = sd.getDictId().toString();
			dictDetailMap = new HashMap<String, SysDetail>();
			for (SysDetail SysDetail : SysDetailList) {
				if (dictId.equals(SysDetail.getDictId().toString())) {
					dictDetailMap.put(SysDetail.getDetailName(), SysDetail);
				}
			}
			dict.put(sd.getDictName(), dictDetailMap);
		}
		return dict;
	}

	// 根据dictName得到detail对应的明细map<name,value>
	public static Map<String, String> getDetailNameMapByDictName(String dictName) {
		Hashtable<String, List<ComboData>> dict = loadStaticDataFromCache();
		if (dict == null || dict.size() == 0) {
			return null;
		}
		Map<String, String> params = new TreeMap<String, String>();
		List<ComboData> list = dict.get(dictName);
		for (ComboData data : list) {
			params.put(data.getText(), data.getValue());
		}
		return params;
	}

	public static Map<String, String> getDictDetail(String dictName) {
		Hashtable<String, List<ComboData>> dict = loadStaticDataFromCache();
		if (dict == null || dict.size() == 0) {
			return null;
		}
		Map<String, String> params = new TreeMap<String, String>();
		List<ComboData> list = dict.get(dictName);
		for (ComboData data : list) {
			params.put(data.getValue(), data.getText());
		}
		return params;
	}

	// 根据dictName得到detail对应的明细map<value,name>
	public static Map<String, String> getDetailValueMapByDictName(
			String dictName) {
		Hashtable<String, Map<String, SysDetail>> dict = getDictFromCache();
		if (dict == null || dict.size() == 0) {
			return null;
		}
		Map<String, String> params = new TreeMap<String, String>();
		Map<String, SysDetail> dictDetailMap = dict.get(dictName);
		for (Map.Entry<String, SysDetail> e : dictDetailMap.entrySet()) {
			SysDetail d = e.getValue();
			params.put(d.getDetailValue().toString(), d.getDetailName());
		}
		return params;
	}

	// 根据dictName和detailName得到detail对应的value
	public static String getDetailValue(String dictName, String detailName) {
		Hashtable<String, Map<String, SysDetail>> dict = getDictFromCache();
		if (dict == null || dict.size() == 0) {
			return null;
		}
		SysDetail dictDetail = dict.get(dictName).get(detailName);
		return dictDetail.getDetailValue().toString();
	}

	public Hashtable<String, List<ComboData>> loadStaticData() {
		SysDictMapper sDictMapper = (SysDictMapper) ObjectFactory
				.getBean(SysDictMapper.class);
		SysDetailMapper SysDetailMapper = (SysDetailMapper) ObjectFactory
				.getBean(SysDetailMapper.class);
		Hashtable<String, List<ComboData>> dict = new Hashtable<String, List<ComboData>>();
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("detailStatus", 1);
		List<SysDict> sDictList = sDictMapper.findEntitysByCondition(null);
		List<SysDetail> SysDetailList = SysDetailMapper
				.findEntitysByCondition(para);
		Map<String, SysDetail> dictDetailMap = null;
		for (SysDict sd : sDictList) {
			String dictId = sd.getDictId().toString();
			List<ComboData> lst = new ArrayList<ComboData>();
			for (SysDetail detail : SysDetailList) {
				if (dictId.equals(detail.getDictId().toString())) {
					ComboData data = new ComboData(detail.getDetailValue(),
							detail.getDetailName());
					lst.add(data);
				}

			}
			dict.put(sd.getDictName(), lst);
		}
		return dict;
	}

}