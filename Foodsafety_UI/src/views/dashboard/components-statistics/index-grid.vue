<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getGridEnterpriseCount} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
    export default {
        name: "index-grid",
      data(){
          return{
            gridCharts:{},
            gridTotal:[],
            gridName:[]
          }
      },
      computed: {
        ...mapGetters(["user"]),
      },
      mounted () {
        this.$nextTick(function() {
          this.init();
        })
      },
      methods: {
          init(){
            getGridEnterpriseCount({areaId:this.user.areaId,gridStatus:1}).then(response => {
              this.gridTotal=response.data;
              response.data.forEach(item=>{
                this.gridName.push(item.name)
              })
              this.initCharts();
            })
          },
        initCharts() {
          this.gridCharts = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '网格企业分布',
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
              data: this.gridName
            },
            series : [
              {
                name: '网格名称',
                type: 'pie',
                radius : '60%',
                center: ['50%', '50%'],
                data:this.gridTotal,
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
          this.gridCharts.setOption(option);
        },
        resizeEchart(){
          this.gridCharts.resize();
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
