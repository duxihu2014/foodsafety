<template>
  <div style="height: 300px">
    <div>日期:{{rdate}}</div>
    <div ref="charts" class="chart-size" ></div>
  </div>
</template>

<script>
  import {
    report, getCurrentMonitor, alarmReport
  } from "api/admin/equipment/sensor";
  import {  parseTime } from "utils/index";
  export default {
    name: "report_charts",
    data(){
      return{
        alarmId:undefined,
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
        // this.queryReport();
      })
    },
    props: ["alarmDataId","type"],
    watch: {
      alarmDataId: function() {
        this.queryReport();
      }
    },
   /* created() {
      this.initCharts();
    },*/
    methods: {
      queryReport(){
        this.alarmId = this.alarmDataId;
        alarmReport({alarmId: this.alarmId}).then(response => {
          // console.log('====================================')
          // console.log(58,response)
          // console.log('====================================')
          this.wd_max = response.sensor.humidityHigh;
          this.wd_max_value = response.sensor.humidityHigh;
          this.wd_min = response.sensor.humidityLow;
          this.sd_max = response.sensor.dampnessHigh;
          this.sd_min = response.sensor.dampnessLow;

  
          response.list.forEach((item)=>{
            this.xdata.push(item.mini);
              if(this.type == 'wd'){
                this.ydata.push(item.humidity);
                this.wd_max_value = Math.max(this.wd_max_value, item.humidity);
              }else {
                this.ydata.push(item.dampness);
              }
          });

          let tn = this.type == 'wd'?'温度':'湿度';
          // console.log("--------type="+this.type);
          // console.log("--------tn="+tn);


          // console.log(78, this.xdata, this.ydata, );
          // console.log(79, this.xdata, this.ydata, );
          // if(response.list.length>1){
            if(this.type == 'wd'){
              this.initCharts("charts", this.xdata, this.ydata, tn+"监控统计图", this.wd_max, this.wd_min, (this.wd_max_value+10)-this.wd_max_value%10);
            }else{
              this.initCharts("charts", this.xdata, this.ydata, tn+"监控统计图", this.sd_max, this.sd_min, 100);
            }
        // }
          // this.initCharts("sdcharts", this.xdata, this.ydata_sd, "湿度监控统计图");
        });

        getCurrentMonitor({sensorNo: this.sensorNo}).then(response => {
          this.wd_value = response.humidity;
          this.sd_value = response.dampness;
          // this.initPieCharts_wd("wdcharts_pie");
          // this.initPieCharts_sd("sdcharts_pie");
          // this.initPieCharts("sdcharts_pie", ['当前湿度','-'], [{value:this.sd_value, name:'当前温度'},{value:100-this.sd_value, name:''}], "当前湿度监控图");
        });
      },
      initCharts(ref, x, y, title, max, min, ymax) {
        this.lineEChart = this.echarts.init(this.$refs[ref], 'dark');
        var option = {
          title: {
            text: title
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
              saveAsImage: {
                name:title+"日期:("+this.rdate+")"
              }
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
      },
      resizeEchart(){
        this.lineEChart.resize();
      },
      initPieCharts_wd(ref) {
        this.pieEChart_wd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_wd_option = {
          tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
          },
          toolbox: {
            feature: {
              restore: {},
              saveAsImage: {
              }
            }
          },
          series: [
            {
              name: '温度监控',
              type: 'gauge',
              min:-10,
              max:100,
              detail: {formatter:'{value}℃'},
              data: [{value: this.wd_value, name: '当前温度'}]
            }
          ]
        };
        this.pieEChart_wd.setOption(this.pieEChart_wd_option);
        this.refreshReport_wd();
      },
      initPieCharts_sd(ref) {
        this.pieEChart_sd = this.echarts.init(this.$refs[ref], 'dark');
        this.pieEChart_sd_option = {
          tooltip : {
            formatter: "{a} <br/>{b} : {c}%"
          },
          toolbox: {
            feature: {
              restore: {},
              saveAsImage: {
              }
            }
          },
          series: [
            {
              name: '湿度监控',
              type: 'gauge',
              min:0,
              max:100,
              detail: {formatter:'{value}%'},
              data: [{value: this.sd_value, name: '当前湿度'}]
            }
          ]
        };
        this.pieEChart_sd.setOption(this.pieEChart_sd_option);
        this.refreshReport_wd();
      },
      resizeEchart(){
        this.lineEChart.resize();
      },
      refreshReport_wd(){
        let _this = this;
        setInterval(function () {
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
        setInterval(function () {
          getCurrentMonitor({sensorNo: _this.sensorNo}).then(response => {

            // this.wd_value = response.humidity;
            // this.sd_value = response.dampness;
            _this.pieEChart_sd_option.series[0].data[0].value = response.dampness;
          _this.pieEChart_sd.setOption(_this.pieEChart_sd_option, true);
        });
          // _this.pieEChart_wd_option.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
          // _this.pieEChart_wd.setOption(_this.pieEChart_wd_option, true);
        },3000);
      }
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
