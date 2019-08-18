<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import { getHealthWarning } from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
      name: "index-health-warning",
      data(){
          return{
            dateArr: [],
            scArray: [],
            lineEChart:{}
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
        init(){
          getHealthWarning({enterpriseId:this.user.enterpriseId}).then(data => {
            // debugger
            data.forEach(item => {
              this.dateArr.push(item.validDate);
              this.scArray.push(item.scCount);
            });
            this.initCharts();
          })
        },
        initCharts() {
          this.lineEChart = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '健康证预警',
              x:'center'
            },
            backgroundColor: '',
            grid: {
              x: '10%',
              y: '20%',
              y2: '16%'
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            legend: {
              bottom: "bottom",
              icon:"rect",
              left: 'center',
              textStyle:{
                  fontSize: 11,
                  color:"#fff",
              },
              data: ['健康证预警数量','餐饮服务许可证']
            },
            xAxis : [
              {
                type : 'category',
                axisLabel: {
                  color:'#3AF6F9',
                },
                axisLine: {
                  lineStyle: {
                      color:'#103D79'
                  }
                },
                axisTick:{       
                  show:false
                },
                data : this.dateArr,
              }
            ],
            yAxis : [{
                type : 'value',
                axisLabel: {
                  color:'#3AF6F9',
                },
                axisLine: {
                  lineStyle: {
                      color:'#103D79'
                  }
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
                minInterval: 1
              },
            ],
            series: [
               {
                name:'健康证预警数量',
                type:'bar',
                barWidth: '30px',
                barGap: "30%",
                color: new this.echarts.graphic.LinearGradient(  0, 0, 0, 1,
                [
                    {offset: 0, color: '#f2e300'},
                    {offset: 1, color: '#AC7C00'}
                ]),
                data:[20,50]
                // data: this.scArray
              },
     
            ]
          }
          this.lineEChart.setOption(option);
        },
        resizeEchart(){
          this.lineEChart.resize();
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
