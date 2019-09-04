<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getComplainCount} from 'api/admin/dashboard/index';
  export default {
    name: "index-complain-process",
    data(){
      return {
        myChart:undefined,
        treatedData:[],
        untreatedData:[],
        category:["平台上报","公众上报"],
      }
    },
    computed:{
      totalData:function(){
        let treatedData = this.treatedData;
        let untreatedData = this.untreatedData;
        let total = treatedData.map(function(v, i) {
          return v + untreatedData[i];
        });
        return total;
      }
    },
    mounted () {
      this.$nextTick(function() {
        this.init();
      })
    },
    methods: {
      init() {
        getComplainCount({}).then(response => {
          this.treatedData = [response.data.count1,response.data.count3];
          this.untreatedData = [response.data.count2,response.data.count4];
          this.initChart();
        })
      },
      initChart(){
        this.myChart = this.echarts.init(this.$refs['charts'], 'dark');
        let option ={
          title : {
            textStyle: {
              fontSize: 15,
              fontWeight:"bold",
              color: '#3cfdff'
            },
            x: 'center',
            text: '投诉立案统计'
          },
          backgroundColor: '',
          grid: {
            // x: '10%',
            y: '25%',
            y2: '20%'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            top: 20,
            icon:'rect',
            left: 'center',
            textStyle:{
              fontSize: 11,
              color:"#fff",
            },
            data: ['待处理','处理完成']
          },
          xAxis: [
            {
              type: "category",
           /*   axisLabel: {
                show: true,
                rotate: 30
              },*/
              axisLabel: {
                color:'#3AF6F9',
              },
              splitLine: {
                "show": false
              },
              splitArea: {
                "show": false
              },
              axisTick:{       
                show:false
              },
              axisLine: {
                lineStyle: {
                  color:'#103D79'
                }
            },
              data: [55,99]
              // data: this.category,
            }
          ],
          yAxis: [{
            type: 'value',
            axisLabel: {
              color:'#3AF6F9',
            },
            axisLine: {
              lineStyle: {
                color:'#103D79'
              }
            },
            splitArea: {
              show: false,
            },
            splitLine: {
                show: true,
                lineStyle:{
                color: '#0D2756',
                type:"solid"
              }
            },
            axisTick:{       
                show:false
            },
          }],

          series: [{
            name: "待处理",
            type: 'bar',
            stack: "总量",
            color:'#FFFD00',
            itemStyle:{
              normal: {
                label:{
                  show: true,
                  position:'insideTop',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              }
            },
            data: [29,59,55,11]
            // data: this.untreatedData
          },{
            name: "处理完成",
            type: 'bar',
            barWidth: '20px',
            stack: "总量",
            color:'#03F39C',
            itemStyle:{
              normal: {
                label:{
                  show: true,
                  position:'insideTop',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              }
            },
            data:[2,33,36,11]
            // data: this.treatedData
          },{
              name: "总数",
              type: "line",
              stack: "总量",
              symbolSize:10,
              color:"#FFFD00",
              symbol:'circle',
              itemStyle: {
                normal: {
                  // color: "rgba(252,230,48,1)",
                  label: {
                    show: true,
                    position: "top",
                    formatter: function(p) {
                      return p.value > 0 ? (p.value) : '';
                    }
                  }
                }
              },
              data:[31,92,91,22]
              // data: this.totalData
            },
          ]
        }
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
