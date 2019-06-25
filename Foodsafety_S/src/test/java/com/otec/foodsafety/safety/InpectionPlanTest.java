package com.otec.foodsafety.safety;

import com.otec.foodsafety.SpringTestCase;
import com.otec.foodsafety.service.safety.InspectionPlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/spring/applicationContext*.xml"})
public class InpectionPlanTest extends SpringTestCase {

	@Autowired
	InspectionPlanService inspectionPlanService;
	
	@Test
	public void testCreateInpectionPlan() {
		inspectionPlanService.createInspectionPlan(1L);
	}

}
