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
            let colors=["#2f41d3","#c45906","#c45906","#c45906"]
            let name=["A等级","B等级","C等级","D等级"]
            getSupervisionByArea({areaId:this.user.areaId}).then(response => {
              this.seriesData=response.data
              let totalCount=0;
              response.data.forEach(item=>{
                totalCount+=item.total;
              })
              response.data.forEach(item => {
                let result = {};
                if(item.regulatoryLevel=='1'){
                  result.value = item.total;
                  result.name = "A等级";
                  // 添加模拟数据
                  result.cname=['A等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:','zh',"fjdk",'jdfkdj','风角度讲费了闪姐发送否决了点击放大就废了就付款了三等奖费j',"fjdk",'jdfkdj','fjdfiei']
                }else if(item.regulatoryLevel=='2'){
                  result.value = item.total;
                  result.name = "B等级";
                  // 添加模拟数据
                  result.cname=['A等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:','zh','fdfhdfh风角度讲费了闪姐发送否决了点击放大就废了就付款了三等奖费j',"fjdk",'jdfkdj','fjdfiei']
                }else if(item.regulatoryLevel=='3'){
                  result.value = item.total;
                  result.name = "C等级"
                  // 添加模拟数据
                  result.cname=['A等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:','jdfkdj','否决了点击放大就废了就付款了三等奖费j',"fjdk",'jdfkdj','fjdfiei']
                }else if(item.regulatoryLevel=='4'){
                  result.value = item.total;
                  result.name = "D等级"
                  // 添加模拟数据
                  result.cname=['A等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:','zh',"fjdk",'jdfkdj','风角度讲费了闪姐发送否决了点击放大就废了就付款了三等奖费j',"fjdk",'jdfkdj','fjdfiei']
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
            backgroundColor: '',
            tooltip : {
              trigger: 'item',
              // formatter: "{a} <br/>{b} : {c} ({d}%)"
              formatter: function(params, ticket, callback) {  
                    let cname=params.data['cname']
                    let html='<div style="max-height:140px;margin-left:1px;overflow:hidden; width:140px;">';
                    for(let item of cname){
                      html+=`<div  style="
                          display:block;
                          margin-right:1px;
                          height:20px;
                          max-width:100%;
                          white-space:nowrap;;
                          text-overflow:ellipsis;
                          overflow:hidden;
                          font-size:12px;
                          ">${item}</div>
                        `
                    }
                    html+="</div>"
                    params.marker=html
                    // console.log(params[0],ticket)
                    // this.alarmData[params[0].dataIndex]
//										console.log(data.answerCount,data.answerCount[params[0].dataIndex] ,params[0].dataIndex)
                  return params.marker  
              }
            },
            legend: {
              orient: 'vertical',
              bottom: "bottom",
              left: 'center',
              icon:"rect",
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data: ['A等级','B等级','C等级','D等级']
            },
            color : [ '#2f41d3', '#c45906', '#d8af00', '#1ab500','#03bcfd', '#2649d7'],
            series:
             [
              {
                name: '监管等级',
                type: 'pie',
                // roseType : 'area',
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
              }, 
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
