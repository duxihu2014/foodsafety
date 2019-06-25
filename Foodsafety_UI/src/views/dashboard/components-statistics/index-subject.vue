<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getSubjectClassificationByArea} from 'api/admin/dashboard/index';
  import { parseValueToText, parseTime, findParentNode } from "utils/index";
  import { mapGetters } from "vuex";
    export default {
        name: "index-subject",
      data(){
        return{
          subjectCharts:{},
          subjectName:[],
          subjectClassificationEchartsDate:[],
        }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        subjectClassificationOptions() {
          return this.staticData["企业主体分类"];
        },
      },
      mounted () {
        this.$nextTick(function() {
          this.init();
        })
      },
      methods: {
          init(){
            getSubjectClassificationByArea({areaId:this.user.areaId}).then(response => {
              response.data.forEach(item => {
                let subjectName=this.subjectClassificationFormatter(item.subjectClassification);
                this.subjectName.push(subjectName);
                this.subjectClassificationEchartsDate.push(item.total);
              })
              this.initCharts();
            })
          },
        initCharts() {
          this.subjectCharts = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '经济主体分布',
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
              type: 'category',
              data:  this.subjectName
            },
            yAxis: {
              type: 'value',
              show: false
            },
            series: [{
              data:  this.subjectClassificationEchartsDate,
              type: 'bar',
              //barMaxWidth: 10,
              itemStyle:{
                normal:{
                  //颜色渐变函数 前四个参数分别表示四个位置依次为右/下/左/上
                  // color: new this.echarts.graphic.LinearGradient(0, 1, 1, 0, [{
                  //   offset: 0,
                  //   color: '#508DFF'
                  // }, {
                  //   offset: 0.5,
                  //   color: '#26C5FE'
                  // },{
                  //   offset: 1,
                  //   color: '#17fee8'
                  // }]),
                  label: {
                    show: true,
                    position: 'top',
                    formatter: '{c}'
                  }
                }
              },
            }]
          };
          this.subjectCharts.setOption(option);
        },
        resizeEchart(){
          this.subjectCharts.resize();
        },
        subjectClassificationFormatter( cellValue){
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
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
