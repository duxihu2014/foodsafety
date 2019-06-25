<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getSupervisionByArea} from 'api/admin/dashboard/index';
  import { parseValueToText, parseTime, findParentNode } from "utils/index";
  import { mapGetters } from "vuex";

    export default {
        name: "index-supervise",
      data(){
        return{
          superviseCharts:{},
          supervisionTotal:[],
        }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        superviseClassificationOptions() {
          return this.staticData["监管级别"];
        },
      },
      mounted () {
        this.$nextTick(function() {
          this.init();
        })
      },
      methods: {
          init(){
            getSupervisionByArea({areaId:this.user.areaId}).then(response => {
              response.data.forEach(item => {
                let result = {};
                if(item.regulatoryLevel=='1'){
                  result.value = item.total;
                  result.name = "A等级"
                }else if(item.regulatoryLevel=='2'){
                  result.value = item.total;
                  result.name = "B等级"
                }else if(item.regulatoryLevel=='3'){
                  result.value = item.total;
                  result.name = "C等级"
                }else if(item.regulatoryLevel=='4'){
                  result.value = item.total;
                  result.name = "D等级"
                }
                this.supervisionTotal.push(result)
              });
              this.initCharts();
            });
          },
        initCharts() {
          this.superviseCharts = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '监管等级分布',
              subtext: '',
              x:'center'
            },
            backgroundColor: '#102133',
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
              bottom: "bottom",
              left: 'center',
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data: ['A等级','B等级','C等级','D等级']
            },
            series : [
              {
                name: '监管等级',
                type: 'pie',
                roseType : 'area',
                radius : '60%',
                center: ['50%', '50%'],
                data:this.supervisionTotal,
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
          this.superviseCharts.setOption(option);
        },
        resizeEchart(){
          this.superviseCharts.resize();
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
