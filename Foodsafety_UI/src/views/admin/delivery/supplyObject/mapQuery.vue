<template>
  <div class="main-index" :class="[mDashboard,{'main-index-fullScreen':isFullScreen}]" >
    <el-row style="height: 5%">
      <el-col>
        <el-radio-group v-model="radioSelected"  style="display: block;position: absolute;top: 30px;left: 5px;z-index: 999">
          <el-radio-button :label="1">配送对象</el-radio-button>
          <el-radio-button :label="2">实时监控</el-radio-button>
        </el-radio-group>
        <div class="title">松江区食品安全餐饮配送图</div>
       <!-- <i class="fa fa-arrows-alt fullscreen-icon" aria-hidden="true" @click='click' :class="{'is-fullScreen':isFullScreen}"></i>-->
      </el-col>
    </el-row>
    <el-row  style="height: 95%">
      <el-col :span="19">
        <el-row style="height: 70%">
          <el-col :span="24" style="padding-left:2px;padding-top: 2px;">
            <div style="height: 100%;position: relative">
              <index-map :enterpriseBase="enterpriseBase" ref="echarts1" :class="{ inActive:radioSelected==1?false:true}"></index-map>
              <sensor-map :enterpriseBase="enterpriseBase" ref="echarts2"   :class="{ inActive:radioSelected==2?false:true}"></sensor-map>
            </div>
          </el-col>
          <!--<el-col :span="12" style="padding-left:2px;padding-top: 2px;">
            <div style="height: 100%">
                <index-map :enterpriseBase="enterpriseBase" ref="echarts1"></index-map>
            </div>
          </el-col>
          <el-col :span="12" style="padding-left:2px;padding-top: 2px;">
            <div style="height: 100%">
              <sensor-map :enterpriseBase="enterpriseBase" ref="echarts2"></sensor-map>
            </div>
          </el-col>-->
        </el-row>
        <el-row style="height: 30%">
          <el-col :span="3"  style="padding-left:2px;padding-top: 2px;">
            <weather :weatherData="weatherData"></weather>
          </el-col>
          <el-col :span="12"  style="padding-left:2px;padding-top: 2px;">
            <road :roadData="roadData"></road>
          </el-col>
          <el-col :span="3"  style="padding-left:2px;padding-top: 2px;">
            <pie-chart title="最远距离" color="#D87A80"  :count="maxDistance" ref="echarts3"></pie-chart>
          </el-col>
          <el-col :span="3"  style="padding-left:2px;padding-top: 2px;">
            <pie-chart title="最近距离" color="#B6A2DE"  :count="minDistance" ref="echarts4"></pie-chart>
          </el-col>
          <el-col :span="3"  style="padding-left:2px;padding-top: 2px;">
            <pie-chart title="平均距离" color="#5AB1EF"  :count="averageDistance" ref="echarts5"></pie-chart>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="5">
        <el-row style="height: 50%">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <div class="ul-container">
              <div class="ul-container-title">集体用餐企业</div>
              <ul class="enterprise-ul">
                <template v-for="(item, index) in list1">
                  <li @click="selectClick(item,index)" style="cursor: pointer;" >
                    <span><i class="fa fa-truck"></i>&nbsp{{item.enterpriseName}}</span>
                    <span>{{item.contactNumber}}</span>
                  </li>
                </template>
              </ul>
            </div>
          </el-col>
        </el-row>
        <el-row style="height: 50%">
          <el-col style="padding-left:2px;padding-top: 2px;">
            <div  class="ul-container">
              <div class="ul-container-title">集体用餐配企业配送对象</div>
              <ul id="supplyObject-ul">
                <template v-for="(item, index) in list2">
                  <li>
                    <span><i class="fa fa-home"></i>&nbsp{{item.enterpriseName}}</span>
                    <span>{{item.contactNumber}}</span>
                  </li>
                </template>
              </ul>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import screenfull from 'screenfull';
  import { loadDashboardHeight } from "api/screen";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { mapGetters } from "vuex";
  import {getEnterprsieHasSupplyObject,getAllEnterprsieHasSupplyObject,getAllSupplyObject,getSensorMonitor} from 'api/admin/delivery/index';
    export default {
        name: "map-query",
      components: {
        screenfull,
        "index-map": () => import("./components/map"),
        "sensor-map": () => import("./components/sensor-map"),
        "pie-chart": () => import("./components/pie-chart"),
        "weather": () => import("./components/weather"),
        "road": () => import("./components/road")
      },
      data(){
        return{
          isFullScreen : false,
          fromData:[],//起点数据
          enterpriseBase:{},//选中的企业
          supplyObjects:[],//选中的配送对象
          list1: null,
          list2: null,
          listQuery: {
            enterpriseId: undefined,
          },
          maxDistance:undefined,//最远距离
          minDistance:undefined,//最近距离
          averageDistance:undefined,//平均距离
          weatherData:{},//天气数据
          roadData:[],//道路拥堵数据
          radioSelected:1
        }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        mDashboard(){
          return loadDashboardHeight();
        }
      },
      watch:{
        enterpriseBase(val){
          //高德地图api 获取天气预报数据
          $.get("https://restapi.amap.com/v3/weather/weatherInfo?city=310117&key=6ca7b720f2ab2a48f749c1e19c3d1c47",data =>{
            this.weatherData = data.lives[0];
          })
          //高德地图获取 拥堵数据
          $.get("https://restapi.amap.com/v3/traffic/status/circle?key=6ca7b720f2ab2a48f749c1e19c3d1c47&radius=4999&level=4&extensions=all&location="+val.longitude+","+val.latitude,data =>{

            this.roadData  = data.trafficinfo.roads.slice(0,10);
          })
          this.getSupplyObjectList();
          /*this.fromData.length = 0;
          let jsonData = {};
          jsonData.name = val.enterpriseName;
          jsonData.position = [val.longitude,val.latitude];
          this.fromData.push(jsonData);//[{"name":"shanghai","position":[11,22]}]*/
        },
      },
      mounted() {
        let _self=this;
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
        };
      },
      created(){
        this.init();
      },
      methods: {
        init(){//数据初始化
          //1.默认选择第一个企业
          getAllEnterprsieHasSupplyObject(this.listQuery).then(response => {
            this.list1 = response;
            this.enterpriseBase = this.list1[0];
            this.$nextTick(function() {
              $(".enterprise-ul>li").eq(0).css({"color": "#ffa022","fontSize": "1.6vh"})
            })

          });
        },
        getSupplyObjectList(){
          getAllSupplyObject({"enterpriseId":this.enterpriseBase.enterpriseId}).then(response => {
            this.list2 = response;
            $("#supplyObject-ul").scrollTop(0);
            let distanceArr = [];
            response.forEach(item =>{
              let number = this.getDistance(this.enterpriseBase.latitude,this.enterpriseBase.longitude,item.latitude,item.longitude);
              distanceArr.push(number)
            })
            this.maxDistance=this.arrMaxNum2(distanceArr);
            this.minDistance=this.arrMinNum2(distanceArr);
            this.averageDistance=this.arrAverageNum2(distanceArr);
          });
        },
        selectClick(item,index){
          $(".enterprise-ul>li").css({"color": "","fontSize": "1.4vh"})
          $(".enterprise-ul>li").eq(index).css({"color": "#ffa022","fontSize": "1.6vh"})
          this.enterpriseBase=item;
        },
        /*计算距离   ---start*/
        getDistance( lat1,  lng1,  lat2,  lng2){
          let radLat1 = lat1*Math.PI / 180.0;
          let radLat2 = lat2*Math.PI / 180.0;
          let a = radLat1 - radLat2;
          let  b = lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
          let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
            Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
          s = s *6378.137 ;// EARTH_RADIUS;
          s = Math.round(s * 10000) / 10000;
          return s;
        },
        arrMaxNum2(arr){
          return Math.max.apply(null,arr).toFixed(2);
        },
        arrMinNum2(arr){
          return Math.min.apply(null,arr).toFixed(2);
        },
         arrAverageNum2(arr){
          var sum = eval(arr.join("+"));
          return (~~(sum/arr.length*100)/100).toFixed(2);
        }
        /*计算距离   ---end*/
      }
    }
</script>

<style scoped>
  .el-col{
    height: 100%;
  }
  /*标题样式*/
  .title{
    font-size: 3.2vh;
    letter-spacing:1px;
    color: #a4feff;
    font-weight: bold;
    height: 100%;
    text-align: center;
    background-color: #102133;
  }
  /*列表容器样式*/
  .ul-container{
    height: 100%;
    color: white;
    background-color: #102133;
  }
  /*列表标题样式*/
  .ul-container-title{
    height: 10%;
    font-size: 2vh;
    font-weight: bolder;
    color:#3aaec2;
    text-align: center;
    line-height: 200%;
    background-color: #253040;
  }
  .ul-container>ul{
    height: 90%;
    padding: 0px 10px;
    overflow-y: scroll;
    margin: 0px;
  }
  .ul-container>ul::-webkit-scrollbar {
    display: none;
  }
  .ul-container>ul>li{
    font-size: 1.4vh;
    height: 40px;
    line-height: 40px;
    list-style: none;
    color: #2d8ac7;
    border-bottom: 1px #8c9393 dashed;
    overflow: hidden;
  }
  .ul-container>ul>li>:nth-child(1) {
    display: inline-block;
    float: left;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    letter-spacing: 1px;
    max-width: 65%;

  }
  .ul-container>ul>li>:nth-child(2) {
    display: inline-block;
    float: right;
  }
  .ul-container>ul>li:focus{
    color: red;
    font-size: 1.5vh;
  }
  /*全屏样式*/
  .main-index-fullScreen{
    padding-top: 1px;
    padding-left: 30px;
    padding-right: 30px;
    overflow: auto;
    height: 100%;
    background-color: #010E1C;
  }
  .is-fullScreen{
    display: none !important;
  }
  .fullscreen-icon{
    position: absolute;
    right: 1.2rem;
    font-size: 22px;
    color: #bfcbd9;
    line-height: 3rem;
    display: inline-block;
    cursor: pointer;
    transition: .38s;
    z-index: 9999;
  }
  /*解决google浏览器全屏后其他部分为黑色的问题*/
  .main-index:-webkit-full-screen {
    width: 100%;
    height: 100%;
  }
  .main-index{
    /* height: 89vh;*/
    overflow: hidden;
  }
  .inActive{
    position: absolute;
    z-index: -1;
  }
</style>
