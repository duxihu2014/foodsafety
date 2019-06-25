package com.otec.foodsafety.mapper.safety;

import org.apache.ibatis.annotation.myibatis.GenericMapper;
import com.otec.foodsafety.entity.safety.OnsiteVerificationResult;

import java.util.List;
import java.util.Map;

public interface OnsiteVerificationResultMapper extends GenericMapper<OnsiteVerificationResult,Long>{

    List<Map<String, Object>> getResult(Map<String, String> params);
}
