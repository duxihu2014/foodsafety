<template>
  <div style="height: 100%;background-color: #102133;">
    <!--<el-row>
      <el-col :span="18"><div ref="wdcharts" class="chart-size" ></div></el-col>
      <el-col :span="6"><div ref="wdcharts_pie" class="chart-size" ></div></el-col>
    </el-row>
    <el-row>
      <el-col :span="18"><div ref="sdcharts" class="chart-size" ></div></el-col>
      <el-col :span="6"><div ref="sdcharts_pie" class="chart-size" ></div></el-col>
    </el-row>-->
    <el-row>
      <el-col :span="7">
        <div ref="wdcharts_pie" class="chart-size" ></div>
      </el-col>
      <el-col :span="7">
        <div ref="sdcharts_pie" class="chart-size" ></div>
      </el-col>
      <el-col :span="10">
        <div class="ul-container">
          <div class="ul-container-title">传感器在线列表</div>
          <ul class="enterprise-ul">
            <template v-for="(item, index) in sensorList">
              <li @click="selectClick(item,index)" style="cursor: pointer;" >
                <span><i class="fa fa-podcast"></i>&nbsp{{item.sensorName}}</span>
                <span>{{item.enterpriseGroupName}}</span>
              </li>
            </template>
          </ul>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { report, getCurrentMonitor, page } from "api/admin/equipment/sensor";
  import {  parseTime } from "utils/index";
  export default {
    name: "index_sensor_realtime",
    data(){
      return{
        sensorNo: undefined,
        lineEChartList:[],
        lineEChart:{},
        pieEChart_wd:{},
        pieEChart_sd:{},
        pieEChart_wd_option:{},
        pieEChart_sd_option:{},
        xdata:[],
        ydata:[],
        ydata_sd:[],
        wd_max_value: 0,
        wd_value: undefined,
        sd_value: undefined,
        wd_timer: undefined,
        sd_timer: undefined,
        wd_max:undefined,
        wd_min:undefined,
        sd_max:undefined,
        sd_min:undefined,
        sensorList: []
      }
    },
    mounted () {
      this.$nextTick(function() {
        this.queryReport();
      });

      let _self=this;
      //监听菜单栏收缩事件
      $(window).resize(function(){
        _self.resizeEchart();
      });
    },
    watch: {
      sensorNo: function() {
        this.getReport();
      }
    },
    destroyed(){
      this.clearTimer();
    },
    methods: {
      queryReport(){
        page({page: 1, limit: 20, status: "1"}).then(response => {
          this.sensorList = response.rows;
          if (this.sensorList.length > 0) {
            this.sensorNo = this.sensorList[0].sensorNo;
            this.$nextTick(function() {
              $(".enterprise-ul>li").eq(0).css({"color": "#ffa022","fontSize": "1.6vh"})
            })
            this.getReport();
          }
        });
      },
      getReport(){
        report({sensorNo: this.sensorNo}).then(response => {
          this.wd_max = response.sensor.humidityHigh;
          this.wd_max_value = response.sensor.humidityHigh;
          this.wd_min = response.sensor.humidityLow;
          this.sd_max = response.sensor.dampnessHigh;
          this.sd_min = response.sensor.dampnessLow;
          // response.list.forEach((item)=>{
          //   this.xdata.push(item.mini);
          //   this.ydata.push(item.humidity);
          //   this.wd_max_value = Math.max(this.wd_max_value, item.humidity);
          //   this.ydata_sd.push(item.dampness);
          // });
          // this.initCharts("wdcharts", this.xdata, this.ydata, "实时温度监控", this.wd_max, this.wd_min, (this.wd_max_value+10)-this.wd_max_value%10);
          // this.initCharts("sdcharts", this.xdata, this.ydata_sd, "实时湿度监控", this.sd_max, this.sd_min, 100);
          getCurrentMonitor({sensorNo: this.sensorNo}).then(response => {
            this.wd_value = response.humidity;
            this.sd_value = response.dampness;
            this.initPieCharts_wd("wdcharts_pie",this.wd_min, this.wd_max);
            this.initPieCharts_sd("sdcharts_pie",this.sd_min, this.sd_max);
          });
        });
      },
      initCharts(ref, x, y, title, max, min, ymax) {
        this.lineEChart = this.echarts.init(this.$refs[ref], 'dark');
        var option = {
          title: {
            textStyle: {
              fontSize: 15,
              fontWeight:"bold",
              color: '#3cfdff'
            },
            x: 'center',
            text: title
          },
          backgroundColor: '#102133',
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            data: x
          },
          yAxis: {
            min:0,
            max:ymax,
            splitLine: {
              show: false
            }
          },
          visualMap: {
            top: 10,
            right: 10,
            pieces: [{
              gt: 0,
              lte: min,
              color: '#ffde33'
            }, {
              gt: min,
              lte: max,
              color: '#ff9933'
            }, {
              gt: max,
              color: '#cc0033'
            }],
            outOfRange: {
              color: '#cc0033'
            }
          },
          series: {
            name: title,
            type: 'line',
            data: y,
            markLine: {
              silent: true,
              data: [{
                yAxis: min
              }, {
                yAxis: max
              }]
            }
          }
        };
        this.lineEChart.setOption(option);
        this.lineEChartList.push(this.lineEChart);
      },
      resizeEchart(){
        this.lineEChartList.forEach((item)=>{
          item.resize();
        });
        this.pieEChart_wd.resize();
        this.pieEChart_sd.resize();
      },
      initPieCharts_wd(ref, vmin, vmax) {
        this.pieEChart_wd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_wd_option = {
          backgroundColor: '#102133',
          tooltip : {
            formatter: "{a} <br/>{b} : {c}℃"
          },
          series: [
            {
              name: '实时温度',
              type: 'gauge',
              min:-10,
              max:100,
              splitNumber:11,
              center: ['50%', '50%'],
              radius: '80%',
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color:[[vmin/110, '#91c7ae'], [vmax/110, '#00cc33'], [1, '#c23531']],
                  width: 3
                }
              },
              axisTick: {            // 坐标轴小标记
                length :8,        // 属性length控制线长
                lineStyle: {       // 属性lineStyle控制线条样式
                  color: 'auto',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              splitLine: {           // 分隔线
                length :16,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                  width:3,
                  color: '#fff',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              pointer: {           // 分隔线
                width:3,
                shadowColor : '#fff', //默认透明
                shadowBlur: 5
              },
              title : {
                // offsetCenter: [0, '-120%'],
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  fontSize: 15,
                  fontWeight:"bold",
                  color: '#91c7ae',
                }
              },
              detail: {
                formatter:'{value}℃',
                fontSize: 24,
                offsetCenter: [0, '90%'],
              },
              data: [{value: this.wd_value, name: '温度℃'}]
            }
          ]
        };
        this.pieEChart_wd.setOption(this.pieEChart_wd_option);
        this.refreshReport_wd();
      },
      initPieCharts_sd(ref, vmin, vmax) {
        this.pieEChart_sd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_sd_option = {
          backgroundColor: '#102133',
          tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
          },
          series: [
            {
              name: '实时湿度',
              type: 'gauge',
              min:0,
              max:100,
              center: ['50%', '50%'],
              radius: '80%',
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color:[[vmin/100, '#91c7ae'], [vmax/100, '#00cc33'], [1, '#c23531']],
                  width: 3
                }
              },
              axisTick: {            // 坐标轴小标记
                length :8,        // 属性length控制线长
                lineStyle: {       // 属性lineStyle控制线条样式
                  color: 'auto',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              splitLine: {           // 分隔线
                length :16,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                  width:3,
                  color: '#fff',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              pointer: {           // 分隔线
                width:3,
                shadowColor : '#fff', //默认透明
                shadowBlur: 5
              },
              title : {
                // offsetCenter: [0, '-120%'],
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  fontSize: 15,
                  fontWeight:"bold",
                  color: '#91c7ae',
                }
              },
              detail: {
                formatter:'{value}%',
                fontSize: 24,
                offsetCenter: [0, '90%'],
              },
              data: [{value: this.sd_value, name: '湿度%'}]
            }
          ]
        };
        this.pieEChart_sd.setOption(this.pieEChart_sd_option);
        this.refreshReport_sd();
      },
      refreshReport_wd(){
        let _this = this;
        this.wd_timer = setInterval(function () {
          getCurrentMonitor({sensorNo: _this.sensorNo}).then(response => {
            _this.pieEChart_wd_option.series[0].data[0].value = response.humidity;
            _this.pieEChart_wd.setOption(_this.pieEChart_wd_option, true);
          }, error => {
            _this.clearTimer();
          });
        },60000);
      },
      refreshReport_sd(){
        let _this = this;
        this.sd_timer = setInterval(function () {
          getCurrentMonitor({sensorNo: _this.sensorNo}).then(response => {
            _this.pieEChart_sd_option.series[0].data[0].value = response.dampness;
            _this.pieEChart_sd.setOption(_this.pieEChart_sd_option, true);
          }, error => {
            _this.clearTimer();
          });
        },60000);
      },
      selectClick(item,index){
        $(".enterprise-ul>li").css({"color": "","fontSize": "1.4vh"})
        $(".enterprise-ul>li").eq(index).css({"color": "#ffa022","fontSize": "1.6vh"})
        this.sensorNo=item.sensorNo;
      },
      clearTimer(){
        clearInterval(this.wd_timer);
        clearInterval(this.sd_timer);
      }
    }
  }
</script>

<style scoped>
  .el-row{
    height: 100%;
  }
  .el-col{
    height: 100%;
  }
  .chart-size{
    width: 100%;
    height: 100%;
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
    /*background-color: #253040;*/
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
</style>
