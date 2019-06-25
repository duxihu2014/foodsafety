<template>
  <div class="main-index" :class="[mDashboard]">
    <el-row  style="height: 66%;padding-left: 2px;">
      <el-col :span="6" >
        <el-row  class="div-block">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <!--报警处理统计柱状图-->
            <index-alarm-process ref="echarts1"></index-alarm-process>
          </el-col>
        </el-row>
        <el-row  class="div-block">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <!--报警类型分布饼图-->
            <index-video-pie ref="echarts2"></index-video-pie>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="12" >
        <el-row style="height: 50%;width: 100%;" class="div-block">
          <el-col :span="24" style="padding-left:2px;padding-top: 2px;">
            <!--温度和湿度仪表盘-->
            <index-sensor-realtime ref="echarts3"></index-sensor-realtime>
          </el-col>
          <!--<el-col :span="12" style="padding-left:2px;padding-top: 2px;">
            &lt;!&ndash;传感器列表&ndash;&gt;
            传感器列表
          </el-col>-->
        </el-row>
        <el-row style="height: 50%;width: 100%;" class="div-block">
          <el-col style="padding-left:2px;;padding-top: 2px;">
            <!--中心数据显示区域-->
            <index-center-data ref="echarts5"></index-center-data>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <el-row  class="div-block">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <!--健康预警柱状曲线图-->
            <index-health-warning ref="echarts6"></index-health-warning>
          </el-col>
        </el-row>
        <el-row  class="div-block">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <!--投诉立案统计柱状图-->
            <index-complain-process ref="echarts10"></index-complain-process>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row  style="height: 33%;padding-left: 2px;" class="div-block">
      <el-col :span="6" style="padding-left:2px;padding-top: 2px;">
        <!--视频报警7天趋势曲线-->
        <index-alarm ref="echarts7"></index-alarm>
      </el-col>
      <el-col :span="12" style="padding-left:2px;padding-top: 2px;">
        <!--报警事件实时滚动-->
        <index-alarm-realtime></index-alarm-realtime>
      </el-col>
      <el-col :span="6" style="padding-left:2px;padding-top: 2px;">
        <!--IOT报警7天趋势曲线-->
        <index-sensor-trend ref="echarts9"></index-sensor-trend>
      </el-col>
      <!--<el-col :span="6" style="padding-left:2px;padding-top: 2px;">
        &lt;!&ndash;摄像头列表&ndash;&gt;
        摄像头列表
      </el-col>-->
    </el-row>
  </div>
</template>

<script>
  import { loadDashboardHeight } from "api/screen";
  export default {
    name: "index_enterprise",
    components: {
      "index-alarm-process": () => import("./components-enterprise/index-alarm-process"),
      "index-center-data": () => import("./components-enterprise/index-center-data"),
      "index-sensor-trend":() => import("./components-statistics/index-sensor-trend"),
      "index-alarm": () => import("./components-statistics/index-alarm"),
      "index-video-pie": () => import("./components-statistics/index-video-pie"),
      "index-health-warning": () => import("./components-enterprise/index-health-warning"),
      "index-alarm-realtime": () => import("./components-enterprise/index-alarm-realtime"),
      "index-sensor-realtime": () => import("./components-enterprise/index-sensor-realtime"),
      "index-complain-process": () => import("./components-enterprise/index-complain-process"),
    },
    data(){
      return {

      }
    },
    computed: {
      mDashboard(){
        return loadDashboardHeight();
      }
    },
    created(){
      // alert("enterprise index here");
    },
    mounted () {
      let _self=this;
      //设置首页样式
      $(".main-container").css({height:"100%",backgroundColor:"#010E1C"});
      $("#app").css({height:"100%"});
      //监听菜单栏收缩事件
      var timer = [];
      $(window).resize(function(){
        timer.push(window.setInterval(function (){
          let width = $(".main-menu-container").width();
          try{
            if(width!==$(".main-menu-container").width()){
              width = $(".main-menu-container").width();
            }else {
              resizeEcharts();
              clearTime()
            }
          }catch (e){
            clearTime()
          }
        },100));
      });
      function clearTime(){
        timer.forEach(item=>{
          window.clearInterval(item);
        })
        timer=[];
      };
      //重新设置echarts的大小
      function resizeEcharts(){
        _self.$refs.echarts1.resizeEchart();
        _self.$refs.echarts2.resizeEchart();
        _self.$refs.echarts5.resizeEchart();
        _self.$refs.echarts3.resizeEchart();
        _self.$refs.echarts6.resizeEchart();
        _self.$refs.echarts7.resizeEchart();
        _self.$refs.echarts9.resizeEchart();
        _self.$refs.echarts10.resizeEchart();
      };
    },
    methods:{

    }
  }
</script>

<style scoped>
  .main-index{
    overflow: hidden;
  }
  .el-col{
    height: 100%;
    color: #F0F0F0;
  }
  .div-block{
    width: 100%;
    height: 50%;
    /*text-align: center;*/
  }
</style>
