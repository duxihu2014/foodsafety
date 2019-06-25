<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import { getAlarmRanking } from 'api/admin/dashboard/index';
  import { mapGetters } from 'vuex';
  export default {
    name: "ranking-all",
    data(){
      return {
        myChart: undefined,
        totalAll: [],
        totalVideo: [],
        totalSensor: [],
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
        getAlarmRanking({areaId: this.user.areaId, limit: 10}).then(response => {
          // console.log(response);
          if (response.rel) {
            response.data.forEach(item => {
              this.enterpriseNames.unshift(item.enterpriseName);
              this.totalAll.unshift(item.totalAll);
              this.totalVideo.unshift(item.totalVideo);
              this.totalSensor.unshift(item.totalSensor);
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
            text: 'IOT报警总排行',
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
            data: ['报警总数', '视频报警数', '传感器报警数']
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
              name: '报警总数',
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
              name: '视频报警数',
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
              data: this.totalVideo
            },
            {
              name: '传感器报警数',
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
              data: this.totalSensor
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
