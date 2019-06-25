<template>
    <div ref="map" class="chart-size">

    </div>
</template>

<script>
  import {getMapData} from 'api/admin/delivery/index';
    export default {
      name: "map",
      props:["enterpriseBase"],
      data(){
        return{
          /* 地图相关 */
          zoom: process.env.MAP_ZOOM, // 缩放等级
          center: [0,0], // 初始位置
          myChart:undefined,
          map:undefined,
        }
      },
      watch:{
        enterpriseBase(){
          this.refreshData();
        },
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
              zoom: this.zoom,
              expandZoomRange: true,
              zooms: [3, 20],
              mapStyle: 'amap://styles/darkblue', //地图主题
              center: this.center, //中心点
              rotation: 0,  //顺时针旋转角度
              resizeEnable: true
            },
          };
          this.myChart.setOption(option);
          this.map = this.myChart.getModel().getComponent('amap').getAMap();
        },
        refreshData(){//数据更新
          if(this.myChart&&this.map){
            this.map.setCenter([this.enterpriseBase.longitude,this.enterpriseBase.latitude]);
            getMapData({"enterpriseId":this.enterpriseBase.enterpriseId}).then(response => {
              this.map.clearMap();
              let series= [];
              let linesData =[];
              let effectScatterData=[];
              let fromData = response[0].fromData;
              let toData = response[0].toData;
              new AMap.Marker({
                position: new AMap.LngLat(fromData.longitude,fromData.latitude),
                map: this.map,
                icon:new AMap.Icon({size:new AMap.Size(0,0)})
              });
              toData.forEach(item =>{
                linesData.push({ coords: [[fromData.longitude,fromData.latitude], [item.longitude,item.latitude]]})
                effectScatterData.push({name: item.enterpriseName, value: [item.longitude,item.latitude]})
                //设置隐形marker，为了地图自适应缩放
                new AMap.Marker({
                  position: new AMap.LngLat(item.longitude,item.latitude),
                  map: this.map,
                  icon:new AMap.Icon({size:new AMap.Size(0,0)})
                });
              })
              series.push(
                {
                  type: "effectScatter",
                  coordinateSystem: "amap",
                  zlevel: 3,
                  rippleEffect: {brushType: "stroke"},
                  label: {normal: {show: true, position: "right", formatter: "{b}"}},
                  itemStyle: {normal: {color: "#ffb336"}},
                  symbolSize:20,
                  data: [{name: fromData.enterpriseName, value:[fromData.longitude,fromData.latitude]}]
                },
                {
                  type: "lines",
                  coordinateSystem:"amap",
                  zlevel: 1,
                  effect: {
                    show: true,
                    period: 8,
                    trailLength: 0.5,
                    color: '#fff',
                    symbolSize: 3
                  },
                  lineStyle: {
                    normal: {
                      color: "#ffa022",
                      width: 0,
                      curveness: 0.2
                    }
                  },
                  data: linesData
                },
                {
                  type: "lines",
                  coordinateSystem: "amap",
                  zlevel: 2,
                  symbol: ['none', 'arrow'],
                  symbolSize: 10,
                  effect: {
                    show: true,
                    period: 8,
                    trailLength: 0,
                    symbol: `image://${require('src/assets/truck.png')}`,
                    symbolSize: 15
                  },
                  lineStyle: {
                    normal: {
                      color: "#ffa022",
                      width: 1,
                      opacity: 0.6,
                      curveness: 0.2
                    }
                  },
                  data: linesData
                },
                {
                  type: "effectScatter",
                  coordinateSystem: "amap",
                  zlevel: 3,
                  rippleEffect: {brushType: "stroke"},
                  label: {
                    normal: {show: true, position: "right", formatter: "{b}"}
                  },
                  itemStyle: {normal: {color: "#ff1a1e" }},
                  symbolSize:"15",
                  data:effectScatterData
                }
              )
              this.map.setFitView(this.map.getAllOverlays("marker"));
              this.myChart.setOption({series:series});
            })
          }
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
