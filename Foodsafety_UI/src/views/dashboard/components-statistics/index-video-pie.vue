<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getAlarmCount} from 'api/admin/dashboard/index';
    export default {
        name: "index-video-pie",
      data(){
          return {
            alarmData:[],
            myChart:undefined,
            category:["未戴防护帽","未戴口罩","未穿工作装","抽烟","老鼠出没","陌生人进入"]
          }
      },
      mounted () {
        this.$nextTick(function() {
          this.init();
        })
      },
      methods: {
        init() {
          getAlarmCount({}).then(response => {
            // console.log(24,response)
        // Object
            //   data:
            //   treated: [{…}]
            //   untreated: Array(5)
            //   0:
            //   companeyN: ["上海达永快餐服务有限公司"]
            //   count: 31
            //   eventId: "10001"
            let treatedData = this.filterData(response.data.treated);
            let untreatedData = this.filterData(response.data.untreated);
            let untreatedCompaneyNames = this.filterCompaneyName(response.data.untreated);
            let totalCount = 0; //总数
            let total = treatedData.map(function(v, i) {
              totalCount = totalCount + v + untreatedData[i];
              return v + untreatedData[i];
            });
              // console.log(39,untreatedCompaneyNames)
            total.forEach((item,index) =>{
              if(index==0){
                this.alarmData.push({"name":"未戴防护帽","value":item,"cname":["未戴防护帽:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"未戴防护帽","value":item})
              }else if(index==1){
                this.alarmData.push({"name":"未戴口罩","value":item,"cname":["未戴口罩:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"未戴口罩","value":item})
              }else if(index==2){
                this.alarmData.push({"name":"未穿工作装","value":item,"cname":["未穿工作装:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"未穿工作装","value":item})
              }else if(index==3){
                this.alarmData.push({"name":"抽烟","value":item,"cname":["抽烟:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"抽烟","value":item})
              }else if(index==4){
                this.alarmData.push({"name":"老鼠出没","value":item,"cname":["老鼠出没:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"未穿工作装","value":item})
              }else if(index==5){
                this.alarmData.push({"name":"陌生人进入","value":item,"cname":["陌生人进入:"+item+" "+"("+(item/totalCount*100).toFixed(2)+"%)"].concat(untreatedCompaneyNames[index])})
                // this.alarmData.push({"name":"抽烟","value":item})
              }
            })
            this.initChart();
          })
        },
        initChart(){  
          this.myChart = this.echarts.init(this.$refs['charts'], 'dark');
          var option = {
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              text: '当前视频报警类型分布',
              subtext: '',
              x:'center'
            },
            tooltip : {
              trigger: 'item',
                // formatter: "{a} <br/>{b} : {c}%",
              formatter: function(params, ticket, callback) {  
                    let cname=params.data['cname']
                    let html='<div style="max-height:140px;margin-left:1px;overflow:hidden; width:150px;">';
                    for(let item of cname){
                      html+=`<div  style="
                          display:block;
                          margin-right:1px;
                          height:20px;
                          max-width:100%;
                          font-size:12px;
                          white-space:nowrap;
                          text-overflow:ellipsis;
                          overflow:hidden;
                          ">${item}</div>
                        `
                    }
                    html+="</div>"
                    params.marker=html
                //	console.log(data.answerCount,data.answerCount[params[0].dataIndex] ,params[0].dataIndex)
                  return params.marker  
              }

            },
            backgroundColor: '',
            // backgroundColor: '#102133',
            legend: {
              type:"scroll",
              bottom: "bottom",
              left: 'center',
              icon:"rect",
              pageIconColor: '#6495ed',
              pageIconInactiveColor: '#aaa',
              pageTextStyle:{
                color:'#ccc'
              },
              textStyle:{
                fontSize: 11,
                color:"#fff",

              },
              data: this.category
            },
            color : [ '#03bcfd', '#2649d7', '#c45906', '#1ab500', '#d8af00', '#7E00FF' ],
            series : [
              {
                name: '视频报警类型',
                type: 'pie',
                radius : '60%',
                center: ['50%', '50%'],
                data:this.alarmData,
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
          this.myChart.setOption(option);
        },
        resizeEchart(){
          this.myChart.resize();
        },
        filterData(data){
          let result =[0,0,0,0,0,0];
          data.forEach(item =>{
            if(item.eventId==1){
              result[0]=item.count;
            }else if(item.eventId==2){
              result[1]=item.count;
            }else if(item.eventId==3){
              result[2]=item.count;
            }else if(item.eventId==4){
              result[3]=item.count;
            }else if(item.eventId==5){
              result[4]=item.count;
            }else if(item.eventId==6){
              result[5]=item.count;
            }
          })
          return result;
        },
        filterCompaneyName(data){
          let result =["","","","","",""];
          data.forEach(item =>{
            if(item.eventId==1){
              result[0]=item.companeyName;
            }else if(item.eventId==2){
              result[1]=item.companeyName;
            }else if(item.eventId==3){
              result[2]=item.companeyName;
            }else if(item.eventId==4){
              result[3]=item.companeyName;
            }else if(item.eventId==5){
              result[4]=item.companeyName;
            }else if(item.eventId==6){
              result[5]=item.companeyName;
            }
          })
          return result;
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
