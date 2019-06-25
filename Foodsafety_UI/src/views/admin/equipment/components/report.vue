<template>
  <div style="height: 700px" class="reportDiv">
    <div style="text-align: center;width: 70%">日期:{{rdate}}</div>
    <el-row>
      <el-col :span="18"><div ref="wdcharts" class="chart-size" ></div></el-col>
      <el-col :span="6"><div ref="wdcharts_pie" class="chart-size" ></div></el-col>
    </el-row>
    <el-row>
      <el-col :span="18"><div ref="sdcharts" class="chart-size" ></div></el-col>
      <el-col :span="6"><div ref="sdcharts_pie" class="chart-size" ></div></el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    report, getCurrentMonitor
  } from "api/admin/equipment/sensor";
  import {  parseTime } from "utils/index";
  export default {
    name: "index_charts123",
    data(){
      return{
        sensorNo:undefined,
        lineEChartList:[],
        lineEChart:{},
        pieEChart_wd:{},
        pieEChart_sd:{},
        pieEChart_wd_option:{},
        pieEChart_sd_option:{},
        xdata:[],
        ydata:[],
        xdata_sd:[],
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
        rdate:undefined
      }
    },
    mounted () {
      this.rdate = parseTime(new Date(), '{y}-{m}-{d}');
      this.$nextTick(function() {
        this.queryReport();
      });

      let _self=this;
      //设置首页样式
      // $(".main-container").css({height:"100%",backgroundColor:"#010E1C"});
      // $("#app").css({height:"100%"});
      //监听菜单栏收缩事件
      var timer;
      $(window).resize(function(){
        _self.resizeEchart();
        // timer= window.setInterval(function (){
        //   let width = $(".reportDiv").width();
        //   try{
        //     if(width!==$(".reportDiv").width()){
        //       width = $(".reportDiv").width();
        //     }else {
        //       resizeEcharts();
        //       clearTime()
        //     }
        //   }catch (e){
        //     clearTime()
        //   }
        // },100);
      });
    },
    props: ["no"],
    watch: {
      no: function() {
        this.queryReport();
      }
    },
    destroyed(){
      // window.clearInterval(this.timeId);
      clearInterval(this.wd_timer);
      clearInterval(this.sd_timer);
    },
   /* created() {
      this.initCharts();
    },*/
    methods: {
      queryReport(){
        // console.log("--------sensorNo="+this.no);
        this.sensorNo = this.no;
        report({sensorNo: this.sensorNo}).then(response => {
          this.wd_max = response.sensor.humidityHigh;
          this.wd_max_value = response.sensor.humidityHigh;
          this.wd_min = response.sensor.humidityLow;
          this.sd_max = response.sensor.dampnessHigh;
          this.sd_min = response.sensor.dampnessLow;
          response.list.forEach((item)=>{
            this.xdata.push(item.mini);
            this.ydata.push(item.humidity);
            this.wd_max_value = Math.max(this.wd_max_value, item.humidity);

            this.ydata_sd.push(item.dampness);
          });
          this.initCharts("wdcharts", this.xdata, this.ydata, "温度监控", this.wd_max, this.wd_min, (this.wd_max_value+10)-this.wd_max_value%10);
          this.initCharts("sdcharts", this.xdata, this.ydata_sd, "湿度监控", this.sd_max, this.sd_min, 100);

        getCurrentMonitor({sensorNo: this.sensorNo}).then(response => {
            this.wd_value = response.humidity;
            this.sd_value = response.dampness;
            this.initPieCharts_wd("wdcharts_pie",this.wd_min, this.wd_max);
            this.initPieCharts_sd("sdcharts_pie",this.sd_min, this.sd_max);
            // this.initPieCharts("sdcharts_pie", ['当前湿度','-'], [{value:this.sd_value, name:'当前温度'},{value:100-this.sd_value, name:''}], "当前湿度监控图");
          });
        });

      },
      initCharts(ref, x, y, title, max, min, ymax) {
        this.lineEChart = this.echarts.init(this.$refs[ref], 'dark');
        var option = {
          title: {
            text: title,
            textStyle: {
              fontSize: 14,
              fontWeight:"bold",
              color: '#3cfdff'
            },
          },
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
          toolbox: {
            left: 'center',
            feature: {
              dataZoom: {
                yAxisIndex: 'none'
              },
              restore: {},
              saveAsImage: {}
            }
          },
          dataZoom: [{
            startValue: '2014-06-01'
          }, {
            type: 'inside'
          }],
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
            data: y
            ,
            markLine: {
              silent: true,
              data: [{
                yAxis: min
              }, {
                yAxis: max
              }]
            }
          }
        };;
        this.lineEChart.setOption(option);
        this.lineEChartList.push(this.lineEChart);
      },
      resizeEchart(){
        // this.lineEChart.resize();
        this.lineEChartList.forEach((item)=>{
          item.resize();
        });
        this.pieEChart_wd.resize();
        this.pieEChart_sd.resize();

      },
      initPieCharts_wd(ref, vmin, vmax) {
        this.pieEChart_wd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_wd_option = {
          tooltip : {
            formatter: "{a} <br/>{b} : {c}℃"
          },
          toolbox: {
            feature: {
              restore: {},
              saveAsImage: {}
            }
          },
          series: [
            {
              name: '温度监控',
              type: 'gauge',
              min:-10,
              max:100,
              splitNumber:11,
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color:[[vmin/110, '#91c7ae'], [vmax/110, '#00cc33'], [1, '#c23531']]
                }
              },
              detail: {formatter:'{value}℃'},
              data: [{value: this.wd_value, name: '温度'}]
            }
          ]
        };
        this.pieEChart_wd.setOption(this.pieEChart_wd_option);
        this.refreshReport_wd();
      },
      initPieCharts_sd(ref, vmin, vmax) {
        this.pieEChart_sd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_sd_option = {
          tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
          },
          toolbox: {
            feature: {
              restore: {},
              saveAsImage: {}
            }
          },
          series: [
            {
              name: '湿度监控',
              type: 'gauge',
              min:0,
              max:100,
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color:[[vmin/100, '#91c7ae'], [vmax/100, '#00cc33'], [1, '#c23531']]
                }
              },
              detail: {formatter:'{value}%'},
              data: [{value: this.sd_value, name: '湿度'}]
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
            // this.wd_value = response.humidity;
            // this.sd_value = response.dampness;
            _this.pieEChart_wd_option.series[0].data[0].value = response.humidity;
          _this.pieEChart_wd.setOption(_this.pieEChart_wd_option, true);
        });
          // _this.pieEChart_wd_option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
          // _this.pieEChart_wd.setOption(_this.pieEChart_wd_option, true);
        },3000);
      },
      refreshReport_sd(){
        let _this = this;
        this.sd_timer = setInterval(function () {
          getCurrentMonitor({sensorNo: _this.sensorNo}).then(response => {
            // this.wd_value = response.humidity;
            // this.sd_value = response.dampness;
            _this.pieEChart_sd_option.series[0].data[0].value = response.dampness;
          _this.pieEChart_sd.setOption(_this.pieEChart_sd_option, true);
        });
          // _this.pieEChart_wd_option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
          // _this.pieEChart_wd.setOption(_this.pieEChart_wd_option, true);
        },3000);
      },
      clearTimer(){
        clearInterval(this.wd_timer);
        clearInterval(this.sd_timer);
        // console.log("---close timer---");
      },
    }
  }
</script>

<style scoped>
  .el-row{
    height: 48%;
  }
  .el-col{
    height: 100%;
  }
  .chart-size{

    width: 100%;
    height: 100%;
  }
</style>
