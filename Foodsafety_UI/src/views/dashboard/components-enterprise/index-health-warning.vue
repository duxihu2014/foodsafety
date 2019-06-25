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
            backgroundColor: '#102133',
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
            xAxis : [
              {
                type : 'category',
                data : this.dateArr,
                axisTick: {
                  alignWithLabel: true
                }
              }
            ],
            yAxis : [
              {
                type : 'value',
                minInterval: 1
              },
            ],
            series: [ {
                name:'健康证预警数量',
                type:'bar',
                barWidth: '20px',
                data: this.scArray
              }
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
