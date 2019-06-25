<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import { inspectionResult } from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
    name: "index-inspection",
    data(){
      return{
        radarEChart: {},
        indicator: [],
        data: []
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
        inspectionResult({areaId:this.user.areaId}).then(response => {
          let result = [];
          if (response.rel) {
            response.data.forEach(item => {
              this.indicator.push({text: item.categoryName, max: item.totalCount});
              result.push(item.totalUnqualified);
            });
            this.data.push({name: '日常检查', value: result});
          }
          this.initCharts();
        });
      },
      initCharts() {
        this.radarEChart = this.echarts.init(this.$refs['charts'], 'dark');

        let option = {
          title : {
            textStyle: {
              fontSize: 15,
              fontWeight:"bold",
              color: '#3cfdff'
            },
            x: 'center',
            text: '日常检查结果统计'
          },
          tooltip : {
            trigger: 'axis'
          },
          backgroundColor: '#102133',
          polar : [
            {
              indicator : this.indicator,
              radius : '70%',
              center: ['50%', '55%'],
            }
          ],
          series : [
            {
              name: '日常检查结果统计',
              type: 'radar',
              data : this.data,
              itemStyle: {
                normal: {
                  areaStyle: {
                    type: 'default'
                  }
                }
              }
            }
          ]
        };
        this.radarEChart.setOption(option);
      },
      resizeEchart(){
        this.radarEChart.resize();
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
