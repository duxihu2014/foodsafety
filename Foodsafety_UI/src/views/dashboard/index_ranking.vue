<template>
  <div class="main-index" :class="[mDashboard]">
    <el-row style="height: 50%; padding-left: 2px;">
      <el-col :span="12">
        <!-- 企业视频报警实时排行 -->
        <ranking-video ref="echarts1"></ranking-video>
      </el-col>
      <el-col :span="12" style="padding-left: 2px;">
        <!-- 企业传感器报警实时排行 -->
        <ranking-sensor ref="echarts2"></ranking-sensor>
      </el-col>
    </el-row>
    <el-row  style="height: 50%; padding-left: 2px; padding-top: 2px;">
      <el-col :span="12">
        <!-- 企业报警实时排行 -->
        <ranking-all ref="echarts3"></ranking-all>
      </el-col>
      <el-col :span="12" style="padding-left: 2px;">
        <ranking-table></ranking-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { loadDashboardHeight } from "api/screen";
  export default {
    name: "index_ranking",
    components: {
      "ranking-video": () => import("./components-ranking/ranking-video"),
      "ranking-sensor": () => import("./components-ranking/ranking-sensor"),
      "ranking-table": () => import("./components-ranking/ranking-table"),
      "ranking-all": () => import("./components-ranking/ranking-all")
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
        _self.$refs.echarts3.resizeEchart();
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
</style>
