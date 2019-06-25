<template>
  <div style="height: 700px">
    <div>日期:{{rdate}}</div>


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
        lineEChart:{},
        pieEChart_wd:{},
        pieEChart_sd:{},
        pieEChart_wd_option:{},
        pieEChart_sd_option:{},
        xdata:[],
        ydata:[],
        xdata_sd:[],
        ydata_sd:[],
        wd_value: undefined,
        sd_value: undefined,
        rdate:undefined
      }
    },
    mounted () {
      this.rdate = parseTime(new Date(), '{y}-{m}-{d}');
      this.$nextTick(function() {
        this.queryReport();
      })
    },
    props: ["no"],
    watch: {
      no: function() {
        this.queryReport();
      }
    },
   /* created() {
      this.initCharts();
    },*/
    methods: {
      queryReport(){
        // console.log("--------sensorNo="+this.no);
        this.sensorNo = this.no;
        report({sensorNo: this.sensorNo}).then(response => {
          response.forEach((item)=>{
            this.xdata.push(item.mini);
            this.ydata.push(item.humidity);
            this.ydata_sd.push(item.dampness);
          });
          this.initCharts("wdcharts", this.xdata, this.ydata, "温度监控统计图");
          this.initCharts("sdcharts", this.xdata, this.ydata_sd, "湿度监控统计图");
        });

        getCurrentMonitor({sensorNo: this.sensorNo}).then(response => {
          this.wd_value = response.humidity;
          this.sd_value = response.dampness;
          this.initPieCharts_wd("wdcharts_pie");
          this.initPieCharts_sd("sdcharts_pie");
          // this.initPieCharts("sdcharts_pie", ['当前湿度','-'], [{value:this.sd_value, name:'当前温度'},{value:100-this.sd_value, name:''}], "当前湿度监控图");
      });

      },
      initCharts(ref, x, y, title) {
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
         /* visualMap: {
            top: 10,
            right: 10,
            pieces: [{
              gt: 0,
              lte: 50,
              color: '#096'
            }, {
              gt: 50,
              lte: 100,
              color: '#ffde33'
            }, {
              gt: 100,
              lte: 150,
              color: '#ff9933'
            }, {
              gt: 150,
              lte: 200,
              color: '#cc0033'
            }, {
              gt: 200,
              lte: 300,
              color: '#660099'
            }, {
              gt: 300,
              color: '#7e0023'
            }],
            outOfRange: {
              color: '#999'
            }
          },*/
          series: {
            name: title,
            type: 'line',
            data: y
            /*,
            markLine: {
              silent: true,
              data: [{
                yAxis: 50
              }, {
                yAxis: 100
              }, {
                yAxis: 150
              }, {
                yAxis: 200
              }, {
                yAxis: 300
              }]
            }*/
          }
        };;
        this.lineEChart.setOption(option);
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
              saveAsImage: {}
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
              saveAsImage: {}
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
