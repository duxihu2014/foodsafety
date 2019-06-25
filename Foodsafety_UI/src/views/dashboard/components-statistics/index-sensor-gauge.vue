<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getSensorCount} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
    name: "index-sensor-gauge",
    data(){
      return{
        eChart: {},
        wdCount: 0,
        sdCount: 0
      }
    },
    computed: {
      ...mapGetters(["user"])
    },
    mounted () {
      this.$nextTick(function() {
        this.init();
      })
    },
    methods: {
      init() {
        getSensorCount({areaIds:this.user.areaId}).then(response => {
          if(response.count_wd)
            this.wdCount=response.count_wd;
          else
            this.wdCount=0;
          if(response.count_sd)
            this.sdCount=response.count_sd;
          else
            this.sdCount=0;
          this.initCharts();
        });
      },
      initCharts() {
        this.eChart = this.echarts.init(this.$refs['charts'], 'dark');

        let option = {
          backgroundColor: '#102133',
          title : {
            textStyle: {
              fontSize: 15,
              fontWeight:"bold",
              color: '#3cfdff'
            },
            text: '当前IOT报警数',
            subtext: '',
            x:'center'
          },
          tooltip : {
            show:false
          },
           series : [
            {
              name:'温度',
              type:'gauge',
              min:0,
              max:100,
              center: ['25%', '50%'],
              splitNumber:5,
              radius: '55%',
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color: [[1, '#1e90ff']],
                  width: 2,
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              axisLabel: {            // 坐标轴小标记
                textStyle: {       // 属性lineStyle控制线条样式
                  fontWeight: 'bolder',
                  color: '#fff',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
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
                offsetCenter: [0, '130%'],
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  fontSize: 15,
                  color: '#fff',
                }
              },
              detail : {
                offsetCenter: [0, '90%'],
                fontSize: 24,
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  color: '#ff4500'
                }
              },
              data:[{value: this.wdCount, name: '温度报警数'}]
            },
            {
              name:'湿度',
              type:'gauge',
              min:0,
              max:100,
              center: ['75%', '50%'],
              splitNumber:5,
              radius: '55%',
              axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                  color: [[1, '#1e90ff']],
                  width: 2,
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
                }
              },
              axisLabel: {            // 坐标轴小标记
                textStyle: {       // 属性lineStyle控制线条样式
                  fontWeight: 'bolder',
                  color: '#fff',
                  shadowColor : '#fff', //默认透明
                  shadowBlur: 10
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
                offsetCenter: [0, '130%'],
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  fontSize: 15,
                  color: '#fff',
                }
              },
              detail : {
                offsetCenter: [0, '90%'],
                fontSize: 24,
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                  color: '#ff4500'
                }
              },
              data:[{value: this.sdCount, name: '湿度报警数'}]
            }

          ]
        };

        this.eChart.setOption(option);


      },
      resizeEchart(){
        this.eChart.resize();
      },
    }
  }
</script>

<style scoped>
  .chart-size{
    width: 100%;
    height: 100%;
  }
</style>
