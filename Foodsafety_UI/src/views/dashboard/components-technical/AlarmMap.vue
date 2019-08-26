<template>
        <!--<el-amap
                v-loading="false"
                ref="map"
                :center="[121.215040,31.032480]"
                :expandZoomRange="true"
                :zooms= "[3, 20]"
                :zoom= "20"
                :events="mapEvents">
        </el-amap>-->

    <div ref="alarmMap" class="tec-page"></div>
</template>

<script>
  import { getEnterpriseInfo, getGridsInfo } from 'api/admin/dashboard/index';
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { createDistrictPolygon, createMarkerCluster } from "api/model";
  import { mapGetters } from "vuex";
    export default {
        name: 'AlarmMap',
        components: {},
        data(){
            return {
                zoom: process.env.MAP_ZOOM, // 缩放等级
                center: [0,0], // 初始位置
                mapEvents: {

                },
                pointData:[],
                elmap:undefined,
                compGeo:{},
            }
        },
        computed: {
          ...mapGetters(["user", "staticData"]),
        },
        mounted(){
            // this.initEchart();
            this.init();
        },
        methods: {
            init(){
              getEnterpriseInfo({finalAreaId:this.user.areaId,enterpriseStatus:'3'}).then(response=>{
                //debugger
                let enterpriseInfo = response.data;
                enterpriseInfo.forEach(item => {
                  let pointItem = {name: item.enterpriseInfo.enterpriseName, value: [item.enterpriseInfo.longitude, item.enterpriseInfo.latitude, item.alarmCount]};
                  this.pointData.push(pointItem);
                  // this.compGeo = {};
                  this.compGeo["comp_"+item.enterpriseInfo.enterpriseId] = [item.enterpriseInfo.longitude, item.enterpriseInfo.latitude];
                })
            console.log("----坐标数据");
            console.log(this.pointData);
            if(this.pointData.length > 0){
                console.log(898989889);
                
              console.log(this.pointData[0].value[0]+","+this.pointData[0].value[1]);
              this.center = [this.pointData[0].value[0], this.pointData[0].value[1]];
            }
            this.initEchart();
              })

            },
            moveMap(compId){
              let point = this.compGeo["comp_"+compId];
              if(point){
                this.elmap.setCenter([point[0], point[1]]);
              }
            },
            initEchart(){
                let echart = this.echarts.init(this.$refs['alarmMap']);
                echart.setOption({
                    amap: {
                        center: this.center,
                        expandZoomRange:true,
                        zooms: [3, 20],
                        zoom: 15,
                        viewMode: '3D',
                        mapStyle: 'amap://styles/darkblue'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: function (params) {
                            if(typeof(params.value)[2] == "undefined"){
                                return params.name + ' : ' + params.value;
                            }else{
                                return params.name + ' : ' + params.value[2];
                            }
                        }
                    },
                    series: [
                        {
                            name: '点',
                            type: 'scatter',
                            coordinateSystem: 'amap',
                            symbol: 'pin',
                            symbolSize: 60,
                            label: {
                                normal: {
                                    show: true,
                                    textStyle: {
                                        color: '#fff',
                                        fontSize: 12,
                                    },
                                    formatter: function (params) {
                                        if(typeof(params.value)[2] == "undefined"){
                                            return params.value;
                                        }else{
                                            return params.value[2];
                                        }
                                    }
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: '#F62157', //标志颜色
                                }
                            },
                            zlevel: 6,
                            /*data: [
                                {name: "湖畔天地", value: [121.205040, 31.032480, 32]},
                                {name: "中央公园", value: [121.226040, 31.036680, 8]},
                                {name: "湖畔天地2", value: [121.220040, 31.028480, 19]}
                            ],*/
                          data:this.pointData,
                        },
                        {
                        type: 'effectScatter',
                        coordinateSystem: 'amap',
                        rippleEffect: {
                            brushType: 'stroke'
                        },
                        label: {
                            normal: {
                                show: true,
                                position: 'right',
                                formatter: '{b}'
                            }
                        },
                        symbolSize: 30,
                        itemStyle: {
                            normal: {
                                color: 'rgba(8,185,232,.9)'
                            }
                        },
                          data:this.pointData
                        /*data: [
                            {name: '湖畔天地', value: [121.205040,31.032480, 32]},
                            {name: '中央公园', value: [121.226040,31.036680, 8]},
                            {name: '湖畔天地2', value: [121.220040,31.028480, 19]},
                        ]*/
                    }]
                });

              this.elmap = echart.getModel().getComponent('amap').getAMap();
              if(this.pointData.length > 0){
                console.log(this.pointData[0].value[0]+","+this.pointData[0].value[1]);
                console.log(this.elmap);
                  // this.elmap.setCenter([this.pointData[0].value[0],this.pointData[0].value[1]]);
                // this.elmap.setCenter([this.pointData[0].value[0],this.pointData[0].value[1]]);
                // this.elmap.setCenter([121.218347,31.002938]);
                // this.elmap. 121.218347,31.002938
              }

            }
        }

    }


</script>

<style scoped>

    .tec-page {
        width: 100%;
        height: 100%;
        
    }



</style>
