<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getVideoTrend} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
    export default {
        name: "index_alarm",
      data(){
          return{
            dateArr:[],
            kzData:[],
            cyData:[],
            gzfData:[],
            aqmData:[],
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
      methods: {init() {
          this.dateArr=[];
          this.aqmData=[];
          this.kzData=[];
          this.gzfData=[];
          this.cyData=[];
          getVideoTrend({areaId:this.user.areaId}).then(response => {
            response.forEach(item=>{
              this.dateArr.push(this.getDate(item.date));
              this.aqmData.push(item.aqmCount);
              this.kzData.push(item.kzCount);
              this.gzfData.push(item.gzfCount);
              this.cyData.push(item.cyCount);
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
              text: '视频报警趋势图'
            },
            backgroundColor: '#102133',
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
                boundaryGap : false,
                data : this.dateArr
              }
            ],
            yAxis : [
              {
                type : 'value'
              }
            ],
            legend: {
              bottom: "bottom",
              left: 'center',
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data: ['未戴安全帽','未戴口罩','未穿工作装','抽烟']
            },
            series : [
              {
                name:'未戴安全帽',
                type:'line',
                areaStyle: {},
                data:this.aqmData
              },
              {
                name:'未戴口罩',
                type:'line',
                areaStyle: {},
                data:this.kzData
              },
              {
                name:'未穿工作装',
                type:'line',
                areaStyle: {},
                data:this.gzfData
              },
              {
                name:'抽烟',
                type:'line',
                areaStyle: {normal: {}},
                data:this.cyData
              }
            ]
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
