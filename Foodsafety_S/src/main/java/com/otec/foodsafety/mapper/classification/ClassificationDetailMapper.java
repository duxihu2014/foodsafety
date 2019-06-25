package com.otec.foodsafety.mapper.classification;

import com.otec.foodsafety.entity.classification.ClassificationDetail;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: xiongjing
 * @Date: 2019/01/17 0011 下午 14:39
 */
public interface ClassificationDetailMapper extends GenericMapper<ClassificationDetail,Long> {

     List<Map> getChangeRecords(@Param("enterpriseId") Long enterpriseId);

}
