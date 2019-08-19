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
            console.log(355555,response)
                  // aqmCount: 0
                  // cyCount: 0
                  // date: "2019-08-12"
                  // gzfCount: 0
                  // kzCount: 0
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
                axisTick:{       
                  show:false
                },
                axisLine: {
                  lineStyle: {
                      color:'#103D79'
                  }
                },
                boundaryGap : false,
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
                        color: ['#0D2756'],
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
              data: ['未戴防护帽','未戴口罩','未穿工作装','抽烟','陌生人进入']
            },
            series : [
              {
                name:'未戴防护帽',
                type:'line',
                 color:'#00bfff',
                areaStyle: {},

                //       name: "餐饮服务许可证",
                // type: "bar",
                // stack: "总量",
                // // barMaxWidth: 35,
                // barGap: "10%",
                // itemStyle: {
                //   normal: {
                //     color: "#04fe97",
                //     label: {
                //       show: true,
                //       textStyle: {
                //         color: "#fff"
                //       },
                //       position: "insideTop",
                //       formatter: function(p) {
                //         return p.value > 0 ? (p.value) : '';
                //       }
                //     }
                //   }
                // },

                // data:[120, 132, 101, 134, 90, 230, 210]
                data:this.aqmData
              },
              {
                name:'未戴口罩',
                type:'line',
                 color:'#2a4cd8',
                areaStyle: {},
                // data:[120, 132, 101, 134, 90, 230, 210]
                data:this.kzData
              },
              {
                name:'未穿工作装',
                type:'line',
                color:'#d9b200',
                areaStyle: {},
                // data:[120, 132, 101, 134, 90, 230, 210]
                data:this.gzfData
              },
              {
                name:'抽烟',
                type:'line',
                 color:'#7e00ff',
                areaStyle: {normal: {}},
                // data:[120, 132, 101, 134, 90, 230, 210]
                data:this.cyData
              },
              // {
              //   name:'陌生人进入',
              //   type:'line',
              //    color:'#1ab500',
              //   areaStyle: {normal: {}},
              //   // data:[120, 132, 101, 134, 90, 230, 210]
              //   data:this.cyData
              // }
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
