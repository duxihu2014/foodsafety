<template>
  <div ref="map" class="chart-size"></div>
</template>

<script>
  import {getSensorMonitor} from 'api/admin/delivery/index';
    export default {
        name: "sensor-map",
      props:["enterpriseBase"],
      data(){
        return{
          /* 地图相关 */
          zoom: process.env.MAP_ZOOM, // 缩放等级
          center: [0,0], // 初始位置
          myChart:undefined,
          map:undefined,
          timeId:undefined,
        }
      },
      watch:{
        enterpriseBase(val){
          this.refreshData();
        },
      },
      destroyed(){
        this.clearTime();
      },
      mounted () {
        this.$nextTick(function() {
          this.initCharts();
        })
      },
      methods:{
        initCharts() {//初始化echarts
          this.myChart = this.echarts.init(this.$refs['map']);
          let option = {
            animation: false,
            amap: {
              maxPitch: 60,
              pitch: 45, //45 俯仰角
              viewMode:'3D',
              expandZoomRange: true,
              zoom:this.zoom,
              zooms: [3, 20],
              mapStyle: 'amap://styles/darkblue', //地图主题
              center: this.center, //中心点
              rotation: 0,  //顺时针旋转角度
              resizeEnable: true,
            },
            tooltip : {
              axisPointer:true,
              formatter: function (params, ticket, callback) {
                var showHtml="";
                showHtml +=params.name+'<br>';
                showHtml +='当前温度：'+params.value[2]+'℃<br>';
                showHtml +='当前湿度：'+params.value[3]+'%<br>';
                return showHtml;
              }
            },
          };

          this.myChart.setOption(option);
          this.map = this.myChart.getModel().getComponent('amap').getAMap();
        },
        refreshData(){//数据更新
          this.clearTime();
          if(this.myChart&&this.map){
            this.map.setCenter([this.enterpriseBase.longitude,this.enterpriseBase.latitude]);
            getSensorMonitor({"enterpriseId":this.enterpriseBase.enterpriseId}).then(response => {
              this.map.clearMap();
              let scatterData = [];
              let data = response.data;
              if( data.length){
                data.forEach(item =>{
                  scatterData.push({"name": item.sensorName,"value":[item.lon,item.lat,item.humidity,item.dampness]})
                  //设置隐形marker，为了地图自适应缩放
                  new AMap.Marker({
                    position: new AMap.LngLat(item.lon,item.lat),
                    map: this.map,
                    icon:new AMap.Icon({size:new AMap.Size(0,0)})
                  });
                })
              }
              let series= [];
              series.push(
                {
                  type: "scatter",
                  coordinateSystem: "amap",
                  zlevel: 2,
                  symbol: `image://${require('src/assets/sensor.png')}`,
                  symbolSize: 10,
                  itemStyle: {normal: {color: "#ffb336"}},
                  label: {
                    normal: {show: true, position: "right", formatter: "{b}"}
                  },
                  symbolSize:20,
                  data:scatterData
                }
              )
              this.map.setFitView(this.map.getAllOverlays("marker"));
              this.myChart.setOption({series:series});
            })
            let _self = this;
            _self.timeId = window.setInterval(function(){
              getSensorMonitor({"enterpriseId":_self.enterpriseBase.enterpriseId}).then(response => {
                let scatterData = [];
                let data = response.data;
                if( data.length){
                  data.forEach(item =>{
                    scatterData.push({"name": item.sensorName,"value":[item.lon,item.lat,item.humidity,item.dampness]})
                  })
                }
                let series= [];
                series.push(
                  {
                    type: "scatter",
                    coordinateSystem: "amap",
                    zlevel: 2,
                    symbol: `image://${require('src/assets/sensor.png')}`,
                    symbolSize: 10,
                    itemStyle: {normal: {color: "#ffb336"}},
                    label: {
                      normal: {show: true, position: "right", formatter: "{b}"}
                    },
                    symbolSize:20,
                    data:scatterData
                  }
                )
                _self.myChart.setOption({series:series});
              }).catch(() => {
                _self.clearTime();
              });
            },5000)
          }
        },
        //清除定时器
        clearTime(){
          window.clearInterval(this.timeId);
        },
        resizeEchart(){
          this.myChart.resize();
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
