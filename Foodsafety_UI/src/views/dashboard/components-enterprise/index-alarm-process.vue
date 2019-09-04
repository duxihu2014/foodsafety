<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getAlarmCount} from 'api/admin/dashboard/index';
  export default {
    name: "index-alarm-process",
    data(){
      return {
        myChart:undefined,
        treatedData:[],
        untreatedData:[],
        category:["未戴防护帽","未戴口罩","未穿工作装","抽烟","温度高","温度低","湿度高","湿度低"],
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
        getAlarmCount({}).then(response => {
          let treatedData = response.data.treated;
          let untreatedData = response.data.untreated;
          this.treatedData = this.filterData(treatedData);
          this.untreatedData = this.filterData(untreatedData);
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
            text: '报警处理情况统计'
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
            data: ['已处理','未处理','总数']
          },

          xAxis: [
            {
              type: "category",
              axisTick:{       
                show:false
              },
              axisLabel: {
                show: true,
                rotate: 30
              },
              axisLine: {
                lineStyle: {
                    color:'#103D79'
                },
                splitLine: {
                  show: false
                },
                splitArea: {
                  show: false
                }
          
            },
              // data:[30,50,55,66],
              data: this.category
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
              show: false
            },
            splitLine: {
                show: false
            },
            axisTick:{       
                show:false
            },
          }],

          series: [{
            name: "已处理",
            type: 'bar',
            stack: "总量",
            itemStyle:{
              normal: {
                color: new this.echarts.graphic.LinearGradient(  0, 0, 0, 1,
                [
                    {offset: 0, color: '#23edfa'},
                    {offset: 1, color: '#0061b2'}
                ]),
                // "barBorderRadius": 0,
                label:{
                  show: true,
                  position:'insideTop',
                  formatter: function(p) {
                    return p.value > 0 ? (p.value) : '';
                  }
                }
              }
            },
            // data: [70,23,44,22]
            data: this.treatedData
          }, {
              name: "未处理",
              type: 'bar',
              stack: "总量",
              itemStyle:{
                normal: {
                  color:'#0f36ad',
                  // "barBorderRadius": 0,
                  label:{
                    show: true,
                    position:'insideTop',
                    formatter: function(p) {
                      return p.value > 0 ? (p.value) : '';
                    }
                  }
                }
              },
              // data: [99,888,555,444]
              data: this.untreatedData
            },{
              name: "总数",
              type: "line",
              stack: "总量",
              symbolSize:10,
              symbol:'circle',
              color:'#D9AE00',
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
              data: this.totalData
            },
          ]
        }
        this.myChart.setOption(option)
      },
      resizeEchart(){
        this.myChart.resize();
      },
      filterData(data){
        let result =[0,0,0,0,0,0,0,0];
        data.forEach(item =>{
          if(item.eventId==1){
            result[0]=item.count;
          }else if(item.eventId==2){
            result[1]=item.count;
          }else if(item.eventId==3){
            result[2]=item.count;
          }else if(item.eventId==4){
            result[3]=item.count;
          }else if(item.eventId==10001){
            result[4]=item.count;
          }else if(item.eventId==10002){
            result[5]=item.count;
          }else if(item.eventId==10003){
            result[6]=item.count;
          }else if(item.eventId==10004){
            result[7]=item.count;
          }
        });
        return result;
      },
    },
  }
</script>

<style scoped>
  .chart-size{
    width: 100%;
    height: 100%;
  }
</style>
