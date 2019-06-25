<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getAlarmCount} from 'api/admin/dashboard/index';
    export default {
        name: "index-video-pie",
      data(){
          return {
            alarmData:[],
            myChart:undefined,
            category:["未戴安全帽","未戴口罩","未穿工作装","抽烟"]
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
            let treatedData = this.filterData(response.data.treated);
            let untreatedData = this.filterData(response.data.untreated);
            let total = treatedData.map(function(v, i) {
              return v + untreatedData[i];
            });
            total.forEach((item,index) =>{
              if(index==0){
                this.alarmData.push({"name":"未戴安全帽","value":item})
              }else if(index==1){
                this.alarmData.push({"name":"未戴口罩","value":item})
              }else if(index==2){
                this.alarmData.push({"name":"未穿工作装","value":item})
              }else if(index==3){
                this.alarmData.push({"name":"抽烟","value":item})
              }

            })
            this.initChart();
          })
        },
        initChart(){
          this.myChart = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '当前视频报警类型分布',
              subtext: '',
              x:'center'
            },
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            backgroundColor: '#102133',
            legend: {
              bottom: "bottom",
              left: 'center',
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data: this.category
            },
            series : [
              {
                name: '视频报警类型',
                type: 'pie',
                radius : '60%',
                center: ['50%', '50%'],
                data:this.alarmData,
                itemStyle: {
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          this.myChart.setOption(option);
        },
        resizeEchart(){
          this.myChart.resize();
        },
        filterData(data){
          let result =[0,0,0,0];
          data.forEach(item =>{
            if(item.eventId==1){
              result[0]=item.count;
            }else if(item.eventId==2){
              result[1]=item.count;
            }else if(item.eventId==3){
              result[2]=item.count;
            }else if(item.eventId==4){
              result[3]=item.count;
            }
          })
          return result;
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
