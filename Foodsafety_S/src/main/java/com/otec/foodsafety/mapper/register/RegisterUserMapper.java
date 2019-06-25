package com.otec.foodsafety.mapper.register;

import com.otec.foodsafety.entity.register.RegisterUser;
import org.apache.ibatis.annotation.myibatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RegisterUserMapper extends GenericMapper<RegisterUser, Long> {
    public void addRegisterUser(Map<String, Object> map);

    public RegisterUser getByUserName(@Param("userName") String userName);

    Integer getRegisterEnterprisePageCount(Map map);

    List<RegisterUser> getRegisterEnterprisePage(Map map, RowBounds rowBounds);
}
