package com.otec.foodsafety.mapper.safety;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResultItem;

import java.util.List;
import java.util.Map;

public interface OnsiteVerificationResultItemMapper extends GenericMapper<OnsiteVerificationResultItem,Long>{
    List<Map<String, Object>> getResultItem(Map<String, String> params);
}
