<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import { getWarning } from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
      name: "index-warning",
      data(){
          return{
            dateArr: [],
            scArray: [],
            ecArray: [],
            totalArray: [],
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
          getWarning({areaId:this.user.areaId}).then(data => {
            // debugger
            data.forEach(item => {
              this.dateArr.push(item.validDate);
              this.scArray.push(item.scCount);
              this.ecArray.push(item.ecCount);
              this.totalArray.push(item.scCount + item.ecCount);
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
              text: '证照预警',
              // subtext: '',
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
            legend: {
              bottom: "bottom",
              left: 'center',
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data : ['餐饮服务许可证','健康证','总数']
            },
            xAxis : [
              {
                type : 'category',
                splitLine: {
                  "show": false
                },
                splitArea: {
                  "show": false
                },
                data : this.dateArr
              }
            ],
            yAxis: {
              type: 'value',
              splitLine: {
                show: false
              },
              splitArea: {
                "show": false
              },
              minInterval: 1
            },
            series: [{
              name: "餐饮服务许可证",
              type: "bar",
              stack: "总量",
              // barMaxWidth: 35,
              barGap: "10%",
              itemStyle: {
                normal: {
                  // color: "rgba(255,144,128,1)",
                  label: {
                    show: true,
                    textStyle: {
                      color: "#fff"
                    },
                    position: "insideTop",
                    formatter: function(p) {
                      return p.value > 0 ? (p.value) : '';
                    }
                  }
                }
              },
              data: this.ecArray,
            }, {
                name: "健康证",
                type: "bar",
                stack: "总量",
                itemStyle: {
                  normal: {
                    // color: "rgba(0,191,183,1)",
                    // "barBorderRadius": 0,
                    label: {
                      show: true,
                      position: "insideTop",
                      formatter: function(p) {
                        return p.value > 0 ? (p.value) : '';
                      }
                    }
                  }
                },
                data: this.scArray
              }, {
                name: "总数",
                type: "line",
                stack: "总量",
                symbolSize:10,
                symbol:'circle',
                itemStyle: {
                  normal: {
                    // color: "rgba(252,230,48,1)",
                    // "barBorderRadius": 0,
                    label: {
                      show: true,
                      position: "top",
                      formatter: function(p) {
                        return p.value > 0 ? (p.value) : '';
                      }
                    }
                  }
                },
                data: this.totalArray
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
