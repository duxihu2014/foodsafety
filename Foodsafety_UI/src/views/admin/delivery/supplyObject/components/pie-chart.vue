<template>
  <div ref="chart" class="chart-size"></div>
</template>

<script>
    export default {
        name: "pie-charts",
      props:["color","title","count"],
      data(){
          return {
            myChart:undefined,
          }
      },
      watch:{
        count(){
          this.initChart()
        }
      },
      mounted(){
        this.initChart()
      },
      methods:{
          initChart(){
            this.myChart = this.echarts.init(this.$refs['chart']);
            let option = {
              title:{
                text:this.title,
                textStyle:{
                  color:this.color,
                },
                x:"center",
                y:"bottom",
                padding:[0,0,30,0],
              },
              backgroundColor: "#102133",
              series: [
                {
                  type: 'pie',
                  center: ['50%', '50%'],
                  radius: ['50%', '60%'],
                  label: {
                    normal: {
                      position: 'center'
                    }
                  },
                  hoverAnimation:false,
                  data: [{
                    value: 200,
                    itemStyle: {
                      normal: {
                        color: this.color
                      }
                    },
                    label: {
                      normal: {
                        textStyle: {
                          color: this.color,
                          fontSize: 23
                        },
                        formatter: this.count,
                      }
                    }
                  }, {
                    value: 100,
                    name: '占位',
                    tooltip: {
                      show: false
                    },
                    itemStyle: {
                      normal: {
                        color: '#ccc'
                      }
                    },
                    label: {
                      normal: {
                        textStyle: {
                          fontSize:18,
                          color: this.color,
                        },
                        formatter: "公里",
                      }
                    }
                  }]
                }]
            }
            this.myChart.setOption(option)
          },
        resizeEchart(){
          this.myChart.resize();
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
