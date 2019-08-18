<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getSensorTrend} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";

  export default {
    name: "index_sensor-trend",
    data(){
      return{
        dateArr:[],
        wdData:[],
        sdData:[],
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
      init() {
        this.dateArr=[],
         this. wdData=[],
          this.sdData=[],
        getSensorTrend({areaId:this.user.areaId,enterpriseId:this.user.enterpriseId}).then(response => {
          response.forEach(item=>{
            this.dateArr.push(this.getDate(item.date));
            this.wdData.push(item.wdCount);
            this.sdData.push(item.sdCount);
          });
          this.initCharts();
        });
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
            x: 'center',
            text: 'IOT报警趋势图'
          },
          backgroundColor: '',
          grid: {
            x: '10%',
            y: '20%',
            y2: '16%'
          },
          tooltip : {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
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
              boundaryGap : false,
              // data : ['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
              data : this.dateArr
            }
          ],
          yAxis : [
            {
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
            }
          ],
          legend: {
            bottom: "bottom",
            left: 'center',
            textStyle:{
              fontSize: 11,
              color:"#fff",
            },
            data: ['温度报警数','湿度报警数']
          },
          series : [
            {
              name:'温度报警数',
              type:'line',
              color:'#E0B10B',
              areaStyle: {
                color:'#E0B10B ',
                opacity:1 
              },
              data:this.wdData
            },
            {
              name:'湿度报警数',
              type:'line',
              color:'#00FCFD',
              areaStyle: {
                color:'#00FCFD ',
                opacity:1 ,
              },
              data:this.sdData
            }
          ]
          // color:["#FFD33E"]
        };
        this.lineEChart.setOption(option);
      },
      resizeEchart(){
        this.lineEChart.resize();
      },
      getDate(dateStr){
        var base = new Date(dateStr);
        return [base.getMonth() + 1, base.getDate()].join('/');
      }
    }
  }
</script>

<style scoped>
  .chart-size{
    width: 100%;
    height: 100%;
  }
</style>
