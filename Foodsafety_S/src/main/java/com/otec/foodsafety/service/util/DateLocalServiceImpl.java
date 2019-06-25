package com.otec.foodsafety.service.util;

import java.util.Date;

import com.otec.foodsafety.mapper.util.DateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateLocalServiceImpl implements DateLocalService {
	@Autowired
	DateMapper dateMapper;

	@Override
	public Date getDataBaseDate() {
		return dateMapper.getDataBaseDate();
	}

}
