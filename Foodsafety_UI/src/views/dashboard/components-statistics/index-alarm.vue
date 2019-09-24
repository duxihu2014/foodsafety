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
        lsData:[],
        msrData:[],
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
        this.lsData=[];
        this.msrData=[];
        getVideoTrend({areaId:this.user.areaId}).then(response => {
          // console.log(response,1111111111111111)
          response.forEach(item=>{
            this.dateArr.push(this.getDate(item.date));
            this.aqmData.push(item.aqmCount);
            this.kzData.push(item.kzCount);
            this.gzfData.push(item.gzfCount);
            this.cyData.push(item.cyCount);
            this.lsData.push(item.lsCount);
            this.msrData.push(item.msrCount);
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
              boundaryGap : false,
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
                  }
            }
            
          ],
          legend: {
            bottom: "bottom",
            left: 'center',
            type:"scroll",
            pageIconColor: '#6495ed',
            pageIconInactiveColor: '#aaa',
            pageTextStyle:{
              color:'#ccc'
            },
            textStyle:{
              fontSize: 11,
              color:"#fff",
            },
            data: ['未戴防护帽','未戴口罩','未穿工作装','抽烟','老鼠出没','陌生人进入']
          },
          series : [
            {
              name:'未戴防护帽',
              type:'line',
              color:"#1AB500",
              areaStyle: {},
              data:this.aqmData
            },
            {
              name:'未戴口罩',
              type:'line',
              color:'#00bfff',
              areaStyle: {},
              data:this.kzData
            },
            {
              name:'未穿工作装',
              type:'line',
              color:'#2a4cd8',
              areaStyle: {},
              data:this.gzfData
            },
            {
              name:'抽烟',
              type:'line',
              color:'#d9b200',
              areaStyle: {normal: {}},
              data:this.cyData
            },
            {
              name:'老鼠出没',
              type:'line',
              color:'#7e00ff',
              areaStyle: {normal: {}},
              data:this.lsData
            },
            {
              name:'陌生人进入',
              type:'line',
              color:'#1ab500',
              areaStyle: {normal: {}},
              data:this.msrData
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
