<template>
  <div ref="charts" class="chart-size" ></div>
</template>

<script>
  import {getAlarmCount} from 'api/admin/dashboard/index';
    export default {
        name: "index-alarm-treated-rate",
      data(){
          return {
            myChart:undefined,
            treatedData:[],
            untreatedData:[],
            category:["未戴安全帽","未戴口罩","未穿工作装","抽烟",'老鼠出没','陌生人进入',"温度高","温度低","湿度高","湿度低"],
          }
      },
      computed:{
        totalData:function(){
          let treatedData = this.treatedData;
          let untreatedData = this.untreatedData;
          let total = treatedData.map(function(v, i) {
            return v + untreatedData[i];
          });

          return total;
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
            let treatedData = response.data.treated;
            let untreatedData = response.data.untreated;
            this.treatedData = this.filterData(treatedData);
            this.untreatedData = this.filterData(untreatedData);
            this. initChart();
          })
        },
        initChart(){
          let total = this.totalData;
          this.myChart = this.echarts.init(this.$refs['charts'], 'dark');
          let option ={
            title : {
              textStyle: {
                fontSize: 15,
                fontWeight:"bold",
                color: '#3cfdff'
              },
              x: 'center',
              text: '当前报警处理情况统计'
            },
            backgroundColor: '',
            // backgroundColor: '#102133',
            legend: {
              top: 20,
              icon:"rect",
              textStyle: {
                fontSize: 11,
                color: '#fff',
              },
              selectedMode:false,
              data: ['已处理', '总数']
            },
            tooltip : {
              trigger: 'axis',
              formatter: function (params, ticket, callback) {
                let param1 = params[0];
                let param2 = params[1];
                var showHtm="";
                var name = param1.name;
                var text1 = param1.seriesName;
                var value1 = param1.value;
                var text2 = param2.seriesName;
                var value2 = param2.value;
                showHtm+= "报警类型："+name+'<br>';
                showHtm+= text2+'：'+value2+'<br>';
                showHtm+= text1+'：'+value1+'<br>';
                if(value2==0){
                  showHtm+='完成率：0.000%'+'<br>';
                }else{
                  showHtm+='完成率：'+((value1/value2)*100).toFixed(3)+'%'+'<br>';
                }
                return showHtm;
              }
            },
            yAxis:  {
              type: 'value',
              axisTick : {show: false},
              axisLine: {
                show: false,
                lineStyle:{
                  color:'#fff',
                }
              },
              splitLine: {
                show: false
              },
            },
            xAxis: [
              {
                type: 'category',
                axisTick : {show: false},
                axisLabel: {
                  show: true,
                  rotate: 60,
                  fontSize:11,
                },
                data: this.category,
              },
              {
                type: 'category',
                axisLine: {show:false},
                axisTick: {show:false},
                axisLabel: {show:false},
                splitArea: {show:false},
                splitLine: {show:false},
                data: this.category,
              },

            ],
            yAxis: [{
              type: "value",
              splitLine: {show: false},
              axisTick: {show: false},
              axisLabel: {interval: 0,color:'#3AF6F9'},
              splitArea: {show: false},
            }],

            series: [{
              name: "总数",
              type: 'bar',
              xAxisIndex:1,
              itemStyle:{
                normal: {
                  show: true,
                  color:'#0f36ad',
                  borderWidth:0,
                  label:{
                    show: false,
                    // show: true,
                    position:'top',
                    formatter: function(p) {
                      return p.value > 0 ? (p.value) : '';
                    }
                  }
                }
              },
              barGap:'0%',
              barCategoryGap:'50%',
              data: this.totalData
            },
            {
              name: "已处理",
              type: 'bar',
              itemStyle:{
                normal: {
                  show: true,
                  color: new this.echarts.graphic.LinearGradient(  0, 0, 0, 1,
                    [
                        {offset: 0, color: '#23edfa'},
                        {offset: 1, color: '#0061b2'}
                    ]),
                  borderWidth:0,
                  label:{
                    show: false,
                    // show: true,
                    textStyle: {
                      color: "#fff"
                    },
                    formatter: function(p) {
                      return p.value > 0 ? (p.value) : '';
                    }
                  }
                }
              },
              barGap:'0%',
              barCategoryGap:'50%',
              data: this.treatedData
            },
            ]
          }
          this.myChart.setOption(option)
        },
        resizeEchart(){
          this.myChart.resize();
        },
        filterData(data){
          let result =[0,0,0,0,0,0,0,0];
          data.forEach(item =>{
            if(item.eventId==1){
              result[0]=item.count;
            }else if(item.eventId==2){
              result[1]=item.count;
            }else if(item.eventId==3){
              result[2]=item.count;
            }else if(item.eventId==4){
              result[3]=item.count;
            }else if(item.eventId==10001){
              result[4]=item.count;
            }else if(item.eventId==10002){
              result[5]=item.count;
            }else if(item.eventId==10003){
              result[6]=item.count;
            }else if(item.eventId==10004){
              result[7]=item.count;
            }
          })
         return result;
        },
      },
    }
</script>

<style scoped>
  .chart-size{
    width: 100%;
    height: 100%;
  }
</style>
