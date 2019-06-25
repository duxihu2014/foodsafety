<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import { getAlarmRanking } from 'api/admin/dashboard/index';
  import { mapGetters } from 'vuex';
  export default {
    name: "ranking-video",
    data(){
      return {
        myChart: undefined,
        totalAll: [],
        eventId10001: [],
        eventId10002: [],
        eventId10003: [],
        eventId10004: [],
        enterpriseNames: [],
      }
    },
    mounted () {
      this.$nextTick(function() {
        this.init();
      })
    },
    computed: {
      ...mapGetters(["user"]),
    },
    methods: {
      init() {
        getAlarmRanking({areaId: this.user.areaId, eventType: 2, limit: 10}).then(response => {
          console.log(response);
          if (response.rel) {
            response.data.forEach(item => {
              this.enterpriseNames.unshift(item.enterpriseName);
              this.totalAll.unshift(item.totalSensor);
              this.eventId10001.unshift(item.eventId10001);
              this.eventId10002.unshift(item.eventId10002);
              this.eventId10003.unshift(item.eventId10003);
              this.eventId10004.unshift(item.eventId10004);
            });
            this.initChart();
          }
        });
      },
      initChart(){
        this.myChart = this.echarts.init(this.$refs['charts'], 'dark');
        let option = {
          title: {
            textStyle: {
              fontSize: 15,
              fontWeight:"bold",
              color: '#3cfdff',
            },
            text: '传感器报警排行',
            subtext: '',
            x:'center'
          },
          backgroundColor: '#102133',
          tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
              type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          legend: {
            top: 20,
            textStyle: {
              fontSize: 11,
              color: '#fff',
            },
            selectedMode:false,
            data: ['总数量', "温度高", "温度低", "湿度高", "湿度低"]
          },
          grid: {
            top: '20%',
            left: '3%',
            right: '5%',
            bottom: '3%',
            containLabel: true
          },
          xAxis:  {
            type: 'value',
            splitLine: {
              show: false
            }
          },
          yAxis: {
            type: 'category',
            splitLine: {
              show: false
            },
            axisLabel: {
              interval: 0,
              // rotate: 30,
              formatter: function (value) {
                if (value.length > 10) {
                  var nValue = "";
                  var rowNumber = Math.ceil(value.length / 10);
                  for (var p = 0; p < rowNumber; p++) {
                    var tempStr = "";
                    var start = p * 10;
                    var end = start + 10;
                    if (p == rowNumber - 1) {
                      tempStr = value.substring(start, value.length);
                    } else {
                      tempStr = value.substring(start, end) + "\n";
                    }
                    nValue += tempStr;
                  }
                  return nValue;
                }
                return value;
              }
            },
            data: this.enterpriseNames
          },
          series: [
            {
              name: '总数量',
              type: 'bar',
              //stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'insideRight',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              },
              data: this.totalAll
            },
            {
              name: '温度高',
              type: 'bar',
              stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'insideRight',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              },
              data: this.eventId10001
            },
            {
              name: '温度低',
              type: 'bar',
              stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'insideRight',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              },
              data: this.eventId10002
            },
            {
              name: '湿度高',
              type: 'bar',
              stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'insideRight',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              },
              data: this.eventId10003
            },
            {
              name: '湿度低',
              type: 'bar',
              stack: '总量',
              label: {
                normal: {
                  show: true,
                  position: 'insideRight',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              },
              data: this.eventId10004
            }
          ]
        };
        this.myChart.setOption(option)
      },
      resizeEchart(){
        this.myChart.resize();
      }
    },
  }
</script>

<style scoped>
  .chart-size{
    width: 100%;
    height: 100%;
  }
</style>
