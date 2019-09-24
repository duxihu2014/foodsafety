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
          superviseName:[]
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
            this.superviseClassificationOptions.forEach(item=>{
               let resultData = {};
               resultData.name=item.text+'等级';
               resultData.value=0;
               resultData.rel=item.value;
               resultData.cname=[resultData.name+":0(0.00%)"]
               this.superviseName.push(resultData.name)
               this.supervisionTotal.push(resultData)
            })

            getSupervisionByArea({areaId:this.user.areaId}).then(response => {
              // console.log(34,response)
              this.seriesData=response.data
              let totalCount=0;
              response.data.forEach(item=>{
                totalCount+=item.total;
              })
              response.data.forEach(item => {
                let result = {};
                for(let i=0;i<this.supervisionTotal.length;i++){
                  if(item.regulatoryLevel==this.supervisionTotal[i].rel){

                    this.supervisionTotal[i].value=item.total;
                    this.supervisionTotal[i].cname=[this.supervisionTotal[i].name+":"+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)'].concat(item.companeyName)
                    return ;
                  }
                }
                // if(item.regulatoryLevel=='1'){
                //   result.value = item.total;
                //   result.name = "A等级";
                //   // 添加模拟数据
                //   result.cname=['A等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:'].concat(item.companeyName)
                // }else if(item.regulatoryLevel=='2'){
                //   result.value = item.total;
                //   result.name = "B等级";
                //   // 添加模拟数据
                //   result.cname=['B等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:'].concat(item.companeyName)
                // }else if(item.regulatoryLevel=='3'){
                //   result.value = item.total;
                //   result.name = "C等级"
                //   // 添加模拟数据
                //   result.cname=['C等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:'].concat(item.companeyName)
                // }else if(item.regulatoryLevel=='4'){
                //   result.value = item.total;
                //   result.name = "D等级"
                //   // 添加模拟数据
                //   result.cname=['D等级:'+item.total+"("+(item.total/totalCount*100).toFixed(2)+'%)','企业名称:'].concat(item.companeyName)
                // }
                // this.supervisionTotal.push(result)
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
                  return params.marker  
              }
            },
            legend: {
              // orient: 'vertical',
              bottom: "bottom",
              left: 'center',
              icon:"rect",
              type:"scroll",
              pageIconColor: '#6495ed',
              pageIconInactiveColor: '#aaa',
              pageTextStyle:{
                color:'#ccc'
              },
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              data: this.superviseName
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
