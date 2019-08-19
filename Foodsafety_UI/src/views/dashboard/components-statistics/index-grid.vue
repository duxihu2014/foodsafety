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
              // console.log(28,response);
              this.gridTotal=response.data;
              // console.log(29,this.gridTotal);
              let totalCount=500;   //需要计算总数  返回真数据后 在下面的foreach中累加 item.value值
              response.data.forEach(item=>{
                this.gridName.push(item.name)
                // 添加模拟数据
                item.cname=["分布: "+"("+(item.value/totalCount*100).toFixed(2)+"%)","企业名称:"].concat(item.companyName)
              })
              this.gridTotal=response.data
              // this.gridTotal=[{name:'松江',value:10},{name:'车墩镇',value:0},{name:'佘山镇',value:50}]
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
              // formatter: "{a} <br/>{b} : {c} ({d}%)"
              formatter: function(params, ticket, callback) {  
                  let cname=params.data.cname
                  let html='<div style="max-height:140px;margin-left:1px;overflow:hidden; width:150px;">';
                  for(let item of cname){
                    html+=`<div  style="
                        display:block;
                        margin-right:1px;
                        height:20px;
                        max-width:100%;
                        white-space:nowrap;
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
            backgroundColor: '',
            legend: {
              bottom: "bottom",
              left: 'center',
              type:"scroll",
              textStyle:{
                fontSize: 11,
                color:"#fff",
              },
              // data: [20,50,90]
              data: this.gridName
            },
            color : [ '#7e00ff', '#c45906', '#d8af00', '#1ab500','#2f41d3','#03bcfd'],
            series : [
              {
                name: '网格名称',
                type: 'pie',
                radius : '60%',
                center: ['50%', '50%'],
                data:this.gridTotal,
                itemStyle: {
                  // color: "#7e00ff",
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
