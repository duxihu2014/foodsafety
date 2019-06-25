<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getCycleScore} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
        name: "index-score",
      data(){
          return{
            enterpriseNames:[],
            scores:[],
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
          this.enterpriseNames=[];
          this.scores=[];
          getCycleScore({areaId:this.user.areaId, page: 0,limit: 10}).then(response => {
            if(response.length>0){
              for(let i=response.length-1;i>=0;i--){
                let item=response[i];
                let name=item.enterpriseName;
                this.enterpriseNames.push(name);
                this.scores.push(item.cycleScore);
              }
            }

            this.initCharts();
          })
        },
        initCharts() {
          this.lineEChart = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title: {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff',
              },
              text: '企业记分排行',
              subtext: '',
              x:'center'
             },
            backgroundColor: '#102133',
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            grid: {
              x: '10%',
              y: '20%',
              y2: '16%'
            },
            xAxis: {
              type: 'value',
              boundaryGap: [0, 0.01]
            },
            yAxis: {
              type: 'category',
              show: false,
              data: this.enterpriseNames
            },
            series: [
              {
                name: '记分',
                type: 'bar',
                data: this.scores,
                itemStyle: {
                  normal: {
                    label: {
                      show: true,
                      position: 'right',
                      formatter: '{b}'
                    }
                  }
                }
              }
            ]
          };
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
