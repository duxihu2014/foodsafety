<template>
  <div style="height: 100%;">
    <el-row style="height: 100%">
      <el-col :span="10">
        <el-row style="height: 100%">
          <el-col>
            <div  style="height: 100%;position: relative;">
              <div ref="echarts" style="height: 100%;position: relative;z-index: 1;">
              </div>
                <!-- padding:0px 5px; -->
              <div style="
                text-align:center;
                position: absolute;
                z-index: 3;
                top: 40%;
                left: 11%;
                right:11%;
                font-size: 3.5vh;color:#ffb336;">
               {{hours}}<span style="font-size:2vh ;color:#fff;">时</span>
                {{minutes}}<span style="font-size:2vh ;color:#fff">分</span>
                {{seconds}}<span style="font-size:2vh ;color:#fff">秒</span>
              </div>
              <div style="text-align:center;padding:0px 5px;position: absolute;z-index: 2;top: 60%;width:100%;font-size: 1.5vh;color:#8c9393">
                <span style="display: block; color: #51d7f8;">上次报警时间</span>
                <span style="display:block; color: #51d7f8;">{{alarmTime}}</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="4">
        <el-row style="height: 50%">
          <el-col>
            <div style="margin-top: 75px;font-size: 4vh ;line-height:45px;" >
              <span style="font-size: 1.7vh;vertical-align:middle">&nbsp;&nbsp;&nbsp;监管等级:</span>
              <span v-if="superviseClassification==1" style="color: #17ae12;vertical-align:middle;">A</span>
              <span v-else-if="superviseClassification==2" style="color: #2a52ae;vertical-align:middle;">B</span>
              <span v-else-if="superviseClassification==3" style="color: #ffa724;vertical-align:middle;">C</span>
              <span v-else-if="superviseClassification==4" style="color: #dd0e0b;vertical-align:middle;">D</span>
            </div>
          </el-col>
        </el-row>
        <el-row style="height: 50%">
          <el-col>
            <div style=" margin-top: 10px;font-size: 1.7vh;">
              晨检红绿灯:
              <img class="blink fa fa-lightbulb-o light_waring" src="../../../assets/full_screen/jd_2.png" alt="" v-if="morningCheckResult">
              <img class="blink fa fa-lightbulb-o light_waring" src="../../../assets/full_screen/jd_1.png" alt="" v-else>
              <!-- <i class="blink fa fa-lightbulb-o" style=" color: #10ae4a;" v-if="morningCheckResult"></i>
              <i class="blink fa fa-lightbulb-o" style=" color: #dd0e0b;" v-else></i> -->
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="10">
        <el-row style="height: 50%;">
          <el-col :span="12">
            <div class="alarm-block margin_top2" >
              <div class="bell" style="">
                <!-- <i class="fa fa-bell-o" style="color:#00a2ae"></i> -->
                <img class="icon_waring" src="../../../assets/full_screen/yj_ic1.png" alt="">
              </div>
              <div class="statistic-content margin_top">
                <div style="color:#00a2ae">
                  <span >{{dateData}}</span>
                </div>
                <div>
                  <span class="title_color">本日报警数</span>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="alarm-block margin_top2"  >
              <div class="bell" >
                <!-- <i class="fa fa-bell-o" style="color: #10ae4a"></i> -->
                <img class="icon_waring" src="../../../assets/full_screen/yj_ic2.png" alt="">
              </div>
              <div class="statistic-content margin_top" >
                <div style="color:#10ae4a">
                  <span>{{weekData}}</span>
                </div>
                <div>
                  <span class="title_color">本周报警数</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row style="height: 50%;">
          <el-col :span="12">
            <div class="alarm-block margin_top2">
              <div class="bell">
                <img class="icon_waring" src="../../../assets/full_screen/yj_ic3.png" alt="">
                <!-- <i class="fa fa-bell-o" style="color: #ae4a34"></i> -->
              </div>
              <div class="statistic-content margin_top">
                <div style="color:#ae4a34">
                  <span>{{monthData}}</span>
                </div>
                <div>
                  <span class="title_color">本月报警数</span>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="alarm-block margin_top2">
              <div class="bell">
                <img class="icon_waring" src="../../../assets/full_screen/yj_ic4.png" alt="">
                <!-- <i class="fa fa-bell-o" style="color: #8433ae;"></i> -->
              </div>
              <div class="statistic-content margin_top">
                <div style="color:#8433ae;">
                  <span>{{yearData}}</span>
                </div>
                <div >
                  <span class="title_color">本年报警数</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

  </div>

</template>

<script>
  import {getEnterpriseBase}from "api/admin/enterprise/base/index";
  import { getEnterpriseAlarmCount,getRecordMorningCheck } from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  import fetch from 'utils/fetch';
    export default {
        name: "index-center-data",
        data(){
            return{
              myChart:undefined,
              alarmData:undefined,
              alarmTime:undefined,
              timeId:undefined,
              hours:"00",
              minutes:"00",
              seconds:"00",
              dateData:0,
              weekData:0,
              monthData:0,
              yearData:0,
              superviseClassification:undefined,
              morningCheckResult:true,
            }
        },
    /*    watch:{
          alarmTime(v1,v2){
            let _self = this;
            if(v2){

            }else{
              _self.timeInterval = window.setInterval(function(){
                _self.dateDiff(_self.alarmTime);
              },1000)
              window.setInterval(function(){
                _self.alarmTime="2019-02-20 10:10:01";
              },10000)
            }
          }
        },*/
        computed: {
          ...mapGetters(["user"])
        },
        created(){
            this.initData();
        },
        mounted(){
          this.initCharts();
        },
        methods:{
          initCharts() {//初始化echarts
            this.myChart = this.echarts.init(this.$refs['echarts'], 'dark');
            let option = {
              title: {
                text: '安全生产时间',
                x: 'center',
                y: '30%',
                textStyle: {
                  color:'#fff',
                  fontSize:'18'
                }
              },
              color: ['rgba(176, 212, 251,0)'],
              backgroundColor: '',
              series: [
                {
                name: 'Line 1',
                type: 'pie',
                radius: ['80%', '82%'],
                itemStyle: {
                  normal: {
                    label: {
                      show: false
                    },
                    labelLine: {
                      show: false
                    }
                  }
                },
                hoverAnimation: false,//是否开启放大效果
                data:[
                  { name: '01',
                    value: 60,
                    itemStyle: {
                      normal: {
                        color: ['#25A7E1']
                      }
                    }
                  },
                  {name: '02', value: 30}
                ]
              },
              {
                name: 'Line 2',
                type: 'pie',
                radius: ['80%', '78%'],
                // radius: ['68%', '71%'],
                itemStyle: {
                  normal: {
                    label: {
                      show: false
                    },
                    labelLine: {
                      show: false
                    }
                  }
                },
                hoverAnimation: false,
                data: [{
                  name:'01',
                  value: 100,
                  itemStyle: {
                    normal: {
                      color: ['#154082'],
                      // color: ['rgba(218, 220, 223, 0.62)'],
                    }
                  }
                }, {
                  name: '02', value: 0}
                ]
              }]
            }
            this.myChart.setOption(option);
          },
          initData(){
            //1.获取报警统计数据
            getEnterpriseAlarmCount({}).then(result => {
              let data = result.data
              data.dateData.forEach(item=>{
                this.dateData+=item.count;
              })
              data.weekData.forEach(item=>{
                this.weekData+=item.count;
              })
              data.monthData.forEach(item=>{
                this.monthData+=item.count;
              })
              data.yearData.forEach(item=>{
                this.yearData+=item.count;
              })
            })
            //2.获取安全监管等级
            getEnterpriseBase({"enterpriseId":this.user.enterpriseId}).then((response)=> {
              this.superviseClassification = response.data.superviseClassification;
            })
            //3.获取晨检情况
            getRecordMorningCheck({"enterpriseId":this.user.enterpriseId}).then((response)=> {
             if(response.data.checkResult==1){
               this.morningCheckResult=true;
             }else if(response.data.checkResult==0){
               this.morningCheckResult=false;
             }
            })
            //4.获取实时报警数据
            let _self = this;
            let url = `/api/admin/alert/list?page=1&limit=20&enterpriseId=${this.user['enterpriseId']}`;
            _self.timeId = window.setInterval(function(){
              fetch({url, method: 'get',}).then(data => {
                _self.alarmData = data.rows[0];
                _self.alarmTime = data.rows[0].alarmTime;
                _self.dateDiff(_self.alarmTime);
                _self.initCharts();
              }).catch(err =>{
                _self.clearTime();
                this.$message.error('报警列表数据获取失败！');
              });
            },1000)


          },
          //计算两时间相差
          dateDiff(time){
            var dateBegin = new Date(time.replace(/-/g, "/"));//开始时间
            var dateEnd = new Date();//结束时间（当前系统时间）
            var dateDiff = dateEnd.getTime() - dateBegin.getTime();//时间差的毫秒数
            this.hours = Math.floor(dateDiff/(60*60*1000))//计算相差小时数
            var leave1 = dateDiff%(60*60*1000);//计算小时后剩余的毫秒数
            this.minutes =  Math.floor(leave1/(60*1000))<10?"0"+ Math.floor(leave1/(60*1000)): Math.floor(leave1/(60*1000))//计算相差分钟数
            var leave2 = dateDiff%(60*1000);//计算分钟后剩余的毫秒数
            this.seconds =  Math.floor(leave2/(1000))<10?"0"+Math.floor(leave2/(1000)):Math.floor(leave2/(1000))//计算相差秒数
          },
          resizeEchart(){
            this.myChart.resize();
          },
          //清除定时器
          clearTime(){
            window.clearInterval(this.timeId)
          }
        }
    }
</script>

<style scoped>
  .el-col{
    height: 100%;
  }
  .title{
    text-align: center;
    color:#3cfdff;
  }
  .alarm-block{
    height:70%;
    margin: 10px 5px 0px 0px;
    border: 1px rgba(218, 220, 223, 0.62) solid;
    border-radius: 10px;
    /* background-color: #102133; */
  }
  .alarm-block:nth-child(1){
    margin-left: 10px;
    /* border:1px solid red; */
  }
  .alarm-block > .bell{
    height:100%;
    width: 30%;
    float: left;
    text-align: center;
    font-size:3.5vh;
    line-height: 200%;
  }
 .statistic-content{
    float: left;
    height:100%;
    width: 70%;
  }
  .statistic-content>div:nth-child(1){
    height: 53%;
    line-height: 250%;
  }
  .statistic-content>div:nth-child(1)>span:nth-child(1){
    margin-left: 10px;
    font-size: 3.5vh;
    text-align: center;
  }
  .statistic-content>div:nth-child(2){
    height: 40%;
    margin-left: 20px;
    line-height: 200%;
    font-size: 1.5vh
  }
  /* 定义keyframe动画，命名为blink */
  @keyframes blink{
    0%{opacity: 1;}
    100%{opacity: 0;}
  }
  /* 添加兼容性前缀 */
  @-webkit-keyframes blink {
    0% { opacity: 1; }
    100% { opacity: 0; }
  }
  @-moz-keyframes blink {
    0% { opacity: 1; }
    100% { opacity: 0; }
  }
  @-ms-keyframes blink {
    0% {opacity: 1; }
    100% { opacity: 0;}
  }
  @-o-keyframes blink {
    0% { opacity: 1; }
    100% { opacity: 0; }
  }
  /* 定义blink类*/
  .blink{
    font-size: 40px;
    animation: blink 1.5s linear infinite;
    /* 其它浏览器兼容性前缀 */
    -webkit-animation: blink 1.5s linear infinite;
    -moz-animation: blink 1.5s linear infinite;
    -ms-animation: blink 1.5s linear infinite;
    -o-animation: blink 1.5s linear infinite;
  }

  /* 改版 开始 */
  .light_waring{
    width:30px;
    vertical-align: bottom;
  }
  .icon_waring{
    width:35px;
    vertical-align: top;
    margin-top:15px;
    padding-left:12px;

  }
  .margin_top{
    margin-top:12px;
  }
  .margin_top2{
    margin-top:22px;
    border-radius: 3px;
    background: rgba(56,111,207,0);
    box-shadow: 0px 0px 10px rgba(0,43,137,1) inset;
    border: 1px solid rgba(0,43,137,1);


  }
  .title_color{
    color:#B1CAFE
  }
  /* 改版 结束 */
</style>
