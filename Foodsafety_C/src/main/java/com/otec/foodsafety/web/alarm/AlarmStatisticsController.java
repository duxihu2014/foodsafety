package com.otec.foodsafety.web.alarm;

import com.cykj.grcloud.entity.page.GridDataModel;
import com.cykj.grcloud.entity.page.PageObject;
import com.google.common.collect.Maps;
import com.otec.foodsafety.entity.alarm.Alarm;
import com.otec.foodsafety.entity.equipment.Equipment;
import com.otec.foodsafety.entity.sensor.SensorInfo;
import com.otec.foodsafety.service.alarm.AlarmService;
import com.otec.foodsafety.service.equipment.EquipmentService;
import com.otec.foodsafety.service.sensor.SensorInfoService;
import com.otec.foodsafety.web.VueBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *   报警管理
 * 
 * @author Administrator
 *
 */

@RestController
@RequestMapping("api/alarm/statistics")
public class AlarmStatisticsController extends VueBaseController<AlarmService, Alarm, Long> {
	Logger log = LoggerFactory.getLogger(AlarmStatisticsController.class);



	@Autowired
	private SensorInfoService sensorInfoService;

	@Autowired
	private EquipmentService equipmentService;





	//1-视频及IOT预警处理情况统计
	@RequestMapping(value = "/allAlarmStatistics", method = RequestMethod.GET)
	@ResponseBody
	public List allAlarmStatisticsByType() {


//		69	报警类型	报警类型 10001温度高10002温度低10003湿度高10004湿度低	10005水浸
//		69	摄像头报警类型	1未戴安全帽2未戴口罩3未穿工作装4抽烟5老鼠出没6陌生人进入

		String warning_items[] = {"未戴防护帽","未戴口罩","未穿工作装","抽烟","老鼠出没","陌生人进入","温度高","温度低","湿度高","湿度低","水浸"};//预警项
		String event_ids[] = {"1","2","3","4","5","6","10001","10002","10003","10004","10005"};//item编码
		String event_types[] = {"1","1","1","1","1","1","2","2","2","2","2"};//预警项  type 1-视频，2-传感器

		List<Map<String, Object>> datamap = new ArrayList<>();
		for (int i = 0; i <event_ids.length ; i++) {
			String warning_item = warning_items[i];
			String event_id = event_ids[i];
			String event_type = event_types[i];

//			2.循环查询数据
//			SELECT count(1) FROM V_ALARM  where event_type = ${event_type} and EVENT_ID=${event_id}  and STATUS=?
//			EVENT_TYPE  1:摄像头 2:传感器
//			EVENT_ID    对应报警类型  温度10001 10002 湿度为10003  10004
//			STATUS   ，


			Map mapAlerm = new HashMap<String, Object>();

			mapAlerm.put("eventId", event_id);
			mapAlerm.put("eventType",event_type);
			mapAlerm.put("status","1");//1-未处理
			Integer un_deal = biz.countAlarmByCondition(mapAlerm);
			mapAlerm.put("status","2");//2-已处理
			Integer already_deal = biz.countAlarmByCondition(mapAlerm);


			Map<String, Object> result = Maps.newHashMap();

			result.put("warning_item", warning_item);//预警项
			result.put("warning_type", event_type);//预警项  type= 1-视频，2-传感器
			result.put("handle_num", un_deal+already_deal);//违规总数
			result.put("already_deal", already_deal);//已处理
			result.put("un_deal", un_deal);//未处理

			datamap.add(result);
		}

		return datamap;
	}


	//2-视频及IOT预警情况
    @RequestMapping(value = "/allAlarmStatisticsByPeriodType", method = RequestMethod.GET)
    @ResponseBody
    public List allAlarmStatisticsByPeriodType(@RequestParam String periodType,//periodType 1-年报，2-近周报
											   @RequestParam String year) {//year 2019,2018

		List<Map<String, Object>> datamap = new ArrayList<>();


		if(periodType.equals("2")){//周报
			String date[] = new String[7];
			for (int i = 0; i <7 ; i++) {
				String datetime = DateTest.getDateString(i);
				date[i]=datetime;
			}
		for (int i = 0; i < date.length; i++) {
			Map<String, Object> result = Maps.newHashMap();
			Map mapAlerm = new HashMap<String, Object>();
			mapAlerm.put("startDate", date[i]);
			mapAlerm.put("endDate", date[i]);

			mapAlerm.put("eventType","1");
			Integer video_num = biz.countAlarmByCondition(mapAlerm);
			result.put("video_num", video_num);//视频

			mapAlerm.put("eventType","2");
			mapAlerm.put("eventId", "10001");
			Integer temperature_low_num = biz.countAlarmByCondition(mapAlerm);
			mapAlerm.put("eventId", "10002");
			Integer temperature_top_num = biz.countAlarmByCondition(mapAlerm);
			result.put("temperature_num", temperature_low_num+temperature_top_num);//温度

			mapAlerm.put("eventId", "10003");
			Integer humidity_low_num = biz.countAlarmByCondition(mapAlerm);
			mapAlerm.put("eventId", "10004");
			Integer humidity_top_num = biz.countAlarmByCondition(mapAlerm);
			result.put("humidity_num", humidity_low_num+humidity_top_num);//湿度

			mapAlerm.put("eventId", "10005");
			Integer water_num = biz.countAlarmByCondition(mapAlerm);
			result.put("water_num", water_num);//水浸

			result.put("total_num", video_num+temperature_low_num+temperature_top_num+humidity_low_num+humidity_top_num+water_num);//总数

			result.put("date", date[i]);//日期

			datamap.add(result);
		}
		}

		if(periodType.equals("1")) {// 年报

			String date[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};

			for (int i = 0; i < date.length; i++) {
				Map<String, Object> result = Maps.newHashMap();
				Map mapAlerm = new HashMap<String, Object>();
				mapAlerm.put("monthDate", year+date[i]);

				mapAlerm.put("eventType","1");
				Integer video_num = biz.countAlarmByCondition(mapAlerm);
				result.put("video_num", video_num);//视频

				mapAlerm.put("eventType","2");
				mapAlerm.put("eventId", "10001");
				Integer temperature_low_num = biz.countAlarmByCondition(mapAlerm);
				mapAlerm.put("eventId", "10002");
				Integer temperature_top_num = biz.countAlarmByCondition(mapAlerm);
				result.put("temperature_num", temperature_low_num+temperature_top_num);//温度

				mapAlerm.put("eventId", "10003");
				Integer humidity_low_num = biz.countAlarmByCondition(mapAlerm);
				mapAlerm.put("eventId", "10004");
				Integer humidity_top_num = biz.countAlarmByCondition(mapAlerm);
				result.put("humidity_num", humidity_low_num+humidity_top_num);//湿度

				mapAlerm.put("eventId", "10005");
				Integer water_num = biz.countAlarmByCondition(mapAlerm);
				result.put("water_num", water_num);//水浸

				result.put("total_num", video_num+temperature_low_num+temperature_top_num+humidity_low_num+humidity_top_num+water_num);//总数

				result.put("date", date[i]);//日期

				datamap.add(result);
			}

		}

        return datamap;
    }

	/**
	 * 3-视频及IOT预警情况--年报，周报报警明细
	 */
	@RequestMapping(value = "/allAlarmStatisticsByPeriodTypeDetail", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel allAlarmStatisticsByPeriodTypeDetail(
			@RequestParam("selectType") String selectType,//selectType=1-周报查询，2-年报查询，
			@RequestParam("date") String date, //date=2019-07-23
			@RequestParam("yearAndMonth") String yearAndMonth,//2019-01
			@RequestParam("type") String type//type(1-视频，2-温度，3-湿度，4-水浸)
		) {
		//时间 预警位置  备注
//		List<Map<String, Object>> datamap = new ArrayList<>();
//		Map<String, Object> result = Maps.newHashMap();
//
//		result.put("datetime", "2019/07/28 22:00:30");//时间
//		result.put("equName", "人脸识别摄像头");//预警位置---设备名称
//		result.put("remarkType", "视频");//备注-   1-视频,2-传感器 (温度,湿度)
//
//
//		datamap.add(result);
//
//
//		Map<String, Object> result2 = Maps.newHashMap();
//
//		result2.put("datetime", "2019/08/28 23:00:30");//时间
//		result2.put("equName", "达永粮油仓库温湿度02");//预警位置---设备名称
//		result2.put("remarkType", "视频");//备注-   1-视频,2-传感器 (温度,湿度)
//
//
//		datamap.add(result2);



		Map<String, String> params = new HashMap<>();
		if(selectType.equals("1")){//1-周报查询，2-年报查询，
			params.put("startDate", date);
			params.put("endDate", date);
		}

		if(selectType.equals("2")){//1-周报查询，2-年报查询，
			params.put("monthDate", yearAndMonth);
		}

		if(type.equals("1")){//(1-视频，2-温度，3-湿度，4-水浸)
			params.put("eventType","1");
		}else if(type.equals("2")){//(1-视频，2-温度，3-湿度，4-水浸)
			params.put("eventType","2");
			params.put("extendSql", "and event_id in (10001,10002)");
		}else if(type.equals("3")){//(1-视频，2-温度，3-湿度，4-水浸)
			params.put("eventType","2");
			params.put("extendSql", "and event_id in (10003,10004)");
		}else if(type.equals("4")){//(1-视频，2-温度，3-湿度，4-水浸)
			params.put("eventType","2");
			params.put("eventId", "10005");
		}

		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);

		return model;
	}



	/**
	 * 4-位置预警情况统计
	 */
	@RequestMapping(value = "/allEquStatistics", method = RequestMethod.GET)
	@ResponseBody
	public List allEquStatistics(@RequestParam Map<String, String> params,
			@RequestParam("startDate") String startDate,//开始时间
			@RequestParam("endDate") String endDate//结束时间
	) {

		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		po.getCondition().put("status", "1");
		GridDataModel model = equipmentService.getGridDataByCondition(po);
		List<Equipment> list = (List<Equipment> )model.getRows();


		List<Map<String, Object>> datamap = new ArrayList<>();

		for (Equipment equipment:list){
			System.out.println(equipment.getEquipmentNo());//设备编号
			System.out.println(equipment.getEquipmentName());//设备名称



			Map mapAlerm = new HashMap<String, Object>();
			mapAlerm.put("startDate", startDate);
			mapAlerm.put("endDate", endDate);

			//mapAlerm.put("eventType","1");
			mapAlerm.put("indexCode",equipment.getEquipmentNo());
			Integer video_num = biz.countAlarmByCondition(mapAlerm);

			Map<String, Object> result = Maps.newHashMap();
			result.put("equId", equipment.getEquipmentNo());//设备ID
			result.put("equName", equipment.getEquipmentName());//预警位置---设备名称
			result.put("num", video_num);//备注
			result.put("remarkType", "视频");//备注-   1-视频,2-传感器 (温度,湿度)
			datamap.add(result);
		}

		GridDataModel model2 = sensorInfoService.getGridDataByCondition(po);
		List<SensorInfo> list2 = (List<SensorInfo> )model2.getRows();

		for (SensorInfo sensorInfo:list2){
			System.out.println(sensorInfo.getSensorNo());//设备编号
			System.out.println(sensorInfo.getSensorName());//设备名称


			Map mapAlerm = new HashMap<String, Object>();
			mapAlerm.put("startDate", startDate);
			mapAlerm.put("endDate", endDate);


			//mapAlerm.put("eventType","1");
			mapAlerm.put("indexCode",sensorInfo.getSensorNo());
			Integer video_num = biz.countAlarmByCondition(mapAlerm);

			Map<String, Object> result = Maps.newHashMap();
			result.put("equId", sensorInfo.getSensorNo());//设备ID
			result.put("equName", sensorInfo.getSensorName());//预警位置---设备名称
			result.put("num", video_num);//备注
			result.put("remarkType", "传感器");//备注-   1-视频,2-传感器 (温度,湿度)
			datamap.add(result);
		}


//		List<Map<String, Object>> datamap = new ArrayList<>();
//		Map<String, Object> result = Maps.newHashMap();
//
//		result.put("equId", "xxfd");//设备ID
//		result.put("equName", "人脸识别摄像头");//预警位置---设备名称
//		result.put("num", 10);//备注
//		result.put("remarkType", "视频");//备注-   1-视频,2-传感器 (温度,湿度)
//
//
//		datamap.add(result);
//
//
//		Map<String, Object> result2 = Maps.newHashMap();
//
//		result2.put("equId", "xxfd2");//设备ID
//		result2.put("equName", "达永粮油仓库温湿度02");//预警位置---设备名称
//		result2.put("num", 11);//备注
//		result2.put("remarkType", "传感器");//备注-   1-视频,2-传感器 (温度,湿度)
//
//
//		datamap.add(result2);
//

		return datamap;
	}



	/**
	 * 5-位置预警情况统计---报警明细
	 */
	@RequestMapping(value = "/allEquStatisticsDetail", method = RequestMethod.GET)
	@ResponseBody
	public GridDataModel allEquStatisticsDetail(
			@RequestParam("equId") String equId,//
			@RequestParam("startDate") String startDate,//开始时间
			@RequestParam("endDate") String endDate//结束时间
	) {



		Map<String, String> params = new HashMap<>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("indexCode",equId);



		// 查询列表数据
		PageObject po = getPageObject(params);
		po.getCondition().putAll(params);
		GridDataModel model = biz.getGridDataModelByCondition(po);


//		//时间 预警位置  备注
//		List<Map<String, Object>> datamap = new ArrayList<>();
//		Map<String, Object> result = Maps.newHashMap();
//		result.put("datetime", "2019/07/28 22:00:30");//时间
//		result.put("equName", "人脸识别摄像头");//预警位置---设备名称
//		result.put("remarkType", "视频");//备注-   1-视频,2-传感器 (温度,湿度)
//		datamap.add(result);
//		Map<String, Object> result2 = Maps.newHashMap();
//		result2.put("datetime", "2019/08/28 23:00:30");//时间
//		result2.put("equName", "达永粮油仓库温湿度02");//预警位置---设备名称
//		result2.put("remarkType", "传感器");//备注-   1-视频,2-传感器(温度,湿度)
//		datamap.add(result2);

		return model;
	}



	
}