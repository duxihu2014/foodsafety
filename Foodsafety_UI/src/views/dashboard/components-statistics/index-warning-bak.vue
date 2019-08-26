<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getWarning} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
      name: "index-warning",
      data(){
          return{
            totalData:[],
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
          getWarning({areaId:this.user.areaId}).then(response => {
            let result = {};
            result.value = response.enterprisecer;
            result.name = "餐饮服务许可证";
            this.totalData.push(result);
            result = {};
            result.value = response.staffcer;
            result.name = "健康证";
            this.totalData.push(result);
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
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            // legend: {
            //   data : ['餐饮服务许可证','健康证']
            // },
            legend: {
              bottom: "bottom",
              left: 'center',
               type:"scroll",
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data : ['餐饮服务许可证','健康证']
            },
            series : [
              {
                name:'证照预警',
                type:'funnel',
                width: '50%',
                x : '26%',
                itemStyle: {
                  normal: {
                    // color: 各异,
                    label: {
                      position: 'left'
                    }
                  }
                },
                data: this.totalData
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
