<template>
  <div class="main-index" :class="[mDashboard]">
      <el-row  style="height: 66%;padding-left: 2px;">
        <el-col :span="5" >
          <el-row  class="div-block">
            <el-col style="padding-left:2px;padding-top: 2px;">
             <index-sensor-gauge ref="echarts1"></index-sensor-gauge>
            </el-col>
          </el-row>
          <el-row  class="div-block">
            <el-col style="padding-left:2px;padding-top: 2px;">
              <index-video-pie ref="echarts2"></index-video-pie>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12" >
          <el-row style="height: 10%;width: 100%;">
            <el-col style="padding-left:2px;padding-top: 2px;">
              <div class="title">松江区食品安全监管平台</div>
            </el-col>
          </el-row>
          <el-row style="height: 90%;width: 100%;">
            <el-col style="padding-left:2px;">
              <index-map></index-map>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="2" >
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color1" :countData="bolckData.data1" :title="bolckData.title1" ></index-block>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color2" :countData="bolckData.data2" :title="bolckData.title2" ></index-block>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color3" :countData="bolckData.data3" :title="bolckData.title3" ></index-block>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color4" :countData="bolckData.data4" :title="bolckData.title4" ></index-block>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="5">
          <el-row  class="div-block">
            <el-col style="padding-left:2px;padding-top: 2px;">
              <index-grid  ref="echarts3"></index-grid>
            </el-col>
          </el-row>
          <el-row  class="div-block">
            <el-col style="padding-left:2px;padding-top: 2px;">
              <index-supervise ref="echarts4"></index-supervise>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-row  style="height: 33%;padding-left: 2px;">
        <el-col :span="5" style="padding-left:2px;padding-top: 2px;">
      <!--    <div class="table-div" style="text-align:center;background-color:#102133;height: 100%;width: 100%">
            <div class="table-title">安全事务完成率</div>
            <index-table></index-table>
          </div>-->
          <index-alarmTreatedRate ref="echarts5"></index-alarmTreatedRate>
        </el-col>
        <el-col :span="7" style="padding-left:2px;padding-top: 2px;">
          <index-sensor-trend ref="echarts6"></index-sensor-trend>
        </el-col>
        <el-col :span="7" style="padding-left:2px;padding-top: 2px;">
          <index-alarm ref="echarts7"></index-alarm>
        </el-col>
        <el-col :span="5" style="padding-left:2px;padding-top: 2px;">
          <index-warning ref="echarts8"></index-warning>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import {getEnterpriseTotal,getRegisterTotal} from 'api/admin/dashboard/index';
  import { loadDashboardHeight } from "api/screen";
  import { mapGetters } from "vuex";
    export default {
      name: "index_supervise",
      components: {
        "index-alarm": () => import("./components-statistics/index-alarm"),
        "index-map": () => import("./components-statistics/index-map"),
        "index-block": () => import("./components-statistics/index-block"),
        "index-supervise": () => import("./components-statistics/index-supervise"),
        // "index-subject": () => import("./components-statistics/index-subject"),
        "index-grid": () => import("./components-statistics/index-grid"),
        "index-table": () => import("./components-statistics/index-table"),
        "index-alarmTreatedRate": () => import("./components-statistics/index-alarmTreatedRate"),
        // "index-inspection": () => import("./components-statistics/index-inspection"),
        // "index-score":() => import("./components-statistics/index-score"),
        "index-warning":() => import("./components-statistics/index-warning"),
        "index-sensor-gauge":() => import("./components-statistics/index-sensor-gauge"),
        "index-sensor-trend":() => import("./components-statistics/index-sensor-trend"),
        "index-video-pie": () => import("./components-statistics/index-video-pie"),
      },
      data(){
          return {
            bolckData:{
              color1:'#1FDDB4',
              color2:'#F2D357',
              color3:'#00BFFF',
              color4:'#919BEE',
              data1:"1",
              data2:"2",
              data3:"20",
              data4:"12",
              title1:"企业总数",
              title2:"待审企业数",
              title3:"监控点位数",
              title4:"在线点位数",
            },

          }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        mDashboard(){
          return loadDashboardHeight();
        }
      },
      created(){
        this.getTotalCountInit();
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
          _self.$refs.echarts4.resizeEchart();
          _self.$refs.echarts5.resizeEchart();
          _self.$refs.echarts6.resizeEchart();
          _self.$refs.echarts7.resizeEchart();
          _self.$refs.echarts8.resizeEchart();
        };
      },
      methods:{
        getTotalCountInit(){
          getEnterpriseTotal({finalAreaId:this.user.areaId,enterpriseStatus:"3"}).then(response => {
            this.bolckData.data1=response;
          })
          getRegisterTotal({areaId:this.user.areaId,registerStatus:"0"}).then(response => {
            this.bolckData.data2=response;
          })
        },
      }
    }
</script>

<style scoped>
  .main-index{
    overflow: hidden;
  }
  .el-col{
    height: 100%;
  }

  .div-block{
    width: 100%;
    height: 50%;
    /*text-align: center;*/
  }
  .circle_div{
    height: 75%;
    width: 100%;
    margin-top: 10%;
  }
  .title{
    font-size: 3.2vh;
    letter-spacing:1px;
    color: #a4feff;
    font-weight: bold;
    height: 100%;
    text-align: center;
    background-color: #102133;
  }
  .block{
    width: 100%;
    height: 25%;
    margin-left: 5px;
    text-align: center;
  }
  .table-title{
    font-size: 1.6vh;
    font-weight: bolder;
    color:#3cfdff;
    text-align: center;
    line-height: 200%;
  }
</style>
