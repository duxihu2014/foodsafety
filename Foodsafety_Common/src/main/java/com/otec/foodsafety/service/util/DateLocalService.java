package com.otec.foodsafety.service.util;

import org.springframework.remoting.service.annotation.RemoteService;

import java.util.Date;

@RemoteService
public interface DateLocalService {
	
	Date getDataBaseDate();
	
}
