<template>
  <div>
    <el-tabs style="">
      <el-tab-pane label="报警信息">
        <el-form :model="alarmEvent" label-width="100px" disabled>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="equType==1" label="视频编号:">
                <el-input v-model.trim="alarmEvent.indexCode"></el-input>
              </el-form-item>
              <el-form-item v-else label="传感器编号:">
                <el-input v-model.trim="alarmEvent.indexCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="企业:">
                <el-input v-model.trim="alarmEvent.enterpriseName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="报警来源:">
                <el-input v-model.trim="alarmEvent.alarmSource"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报警类型:">
                <el-input v-model.trim="alarmEvent.alarmType"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="报警时间:">
            <el-input v-model.trim="alarmEvent.alarmTime"></el-input>
          </el-form-item>
          <el-form-item label="报警描述:">
            <el-input type="textarea" rows="2" v-model.trim="alarmEvent.content"></el-input>
          </el-form-item>
          <el-form-item v-if="equType==1">
            <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :file-list="listFile" :on-preview="handlePictureCardPreview" :limit="1" :auto-upload="false">
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
          <el-form-item v-if="equType==1" style="margin-top: -30px;"> 
          <!-- <videoStream  :vurl="videoObj"  ></videoStream> -->
            <video id="myvideo" :src="alarmEvent.videoUrl" controls="controls" autoplay="autoplay" height="320" width="520"> </video>
          </el-form-item>
        </el-form>
        <el-row v-if="equType==2">
          <el-col :span="20" :offset="2">
            <report :alarmDataId="alarmEvent.id" :type='alarmEvent.sensorAlarmType' v-if="equType==2"></report>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </div>
</template>
<script>
import { parseValueToText, parseTime } from "utils/index";
import alertConst from "api/admin/alarm/alertType";
import report from "views/admin/equipment/components/wdReport";
// import videoStream from "../equipment/videoStream";

export default {
  name: "alarmDetail",
  components: {
    report,
    // videoStream
  },
  data() {
    return {
      // videoObj:{
      //         vid:1,
      //         isShow:true,
      //         url:''
      //     },
      alarmEvent: {
        id: undefined,
        indexCode: undefined,
        alarmSource: undefined,
        alarmType: undefined,
        content: undefined,
        equName: undefined,
        enterpriseName: undefined,
        alarmTime: undefined,
        videoUrl: undefined,
        imgUrl: undefined,
        sensorAlarmType: undefined
      },
      fontColor: "",
      spanfontColorMap: {
        low: "color:#ffbf00;",
        centre: "color:#f7799f;",
        high: "color:red;"
      },
      serverImageUrl: process.env.SERVERIMAGEURL,
      listFile: [],
      dialogImageUrl: "",
      dialogImageVisible: false,
      sensorAlarmType: "" //   type='wd'   或type='sd'     
    };
  },
  props: ["alarmEventData", "equType"], //equType 1摄像头  2传感器
  watch: {
    alarmEventData: function() {
      this.initDiv();
    }

  },
  // created() {
  //   this.initDiv();
  // },
  methods: {
     initDiv(){

     },
    initAlarm(obj) {
      this.listFile = [];
      this.alarmEvent = obj;  


      // this.videoObj.url= obj.videoUrl;    
      
      
      if (this.alarmEvent.imgUrl) {
        this.listFile.push({
          url: this.alarmEvent.imgUrl
        });
        this.$nextTick(function() {
          $(".el-upload--picture-card").hide();
          $(".el-icon-delete").hide();
        });
      }
    },
    eventLevelFormatter(param) {
      if (!param) return "";
      if (param == 1) {
        return "中";
      } else if (param == 2) {
        return "高";
      } else {
        return "低";
      }
    },
    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      // "http://foodsafety.91catv.com:8081/Foodsafety/"+ 
      this.dialogImageUrl =file.url;
      this.dialogImageVisible = true;
    },
  } //end methods
};
</script>
<style scoped>
.gridtable {
  margin-top: 10px;
  width: 100%;
  font-family: verdana, arial, sans-serif;
  font-size: 12px;
  color: #333333;
  border-width: 0px;
  border-color: #666666;
  border-collapse: collapse;
  margin: auto;
}
.gridtable th {
  border-width: 1px;
  padding: 8px;
  border-style: solid;
  border-color: #666666;
  background-color: #dedede;
}
.gridtable td {
  border-width: 1px;
  padding: 8px;
  border-style: solid;
  border-color: #666666;
  background-color: #ffffff;
}
</style>
