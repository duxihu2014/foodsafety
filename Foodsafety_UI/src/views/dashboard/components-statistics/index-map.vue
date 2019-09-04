<template>
    <div class="amap-size index_map">
      <el-amap
        v-loading="mapLoading"
        ref="map"
        :center="center"
        :zoom="zoom"
        mapStyle='amap://styles/darkblue'
        :events="mapEvents"
        >
        <!--<template v-for="(item, index) in enterpriseInfo">
          <el-amap-marker :position="lnglatFormatter(item.enterpriseInfo)"
            :events="markerEvents"
            :extData="item.enterpriseInfo"
          ></el-amap-marker>
        </template>-->
        <!-- 当前网格多边形 -->
        <div v-for="(item, index) in girdInfo" :key="index">
          <el-amap-polygon v-if="item.markers.length>0"
                           :path="item.markers"
                           :strokeColor="strokeColor"
                           strokeOpacity=".8"
                           :strokeWeight="2"
                           fillColor="#fff"
                           fillOpacity=".5"
                           :extData="{'gridId':item.gridId}"
                           >
          </el-amap-polygon>
        </div>
        <el-amap-info-window  :content="mapWindowContent" :position="mapWindowPosition" :visible="mapWindowShow"></el-amap-info-window>
      </el-amap>
    </div>
</template>

<script>
  import { getEnterpriseInfo, getGridsInfo } from 'api/admin/dashboard/index';
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { createDistrictPolygon, createMarkerCluster } from "api/model";
  import { mapGetters } from "vuex";
    export default {
        name: "index-map",
      data(){
          return{
            zoom: process.env.MAP_ZOOM, // 缩放等级
            mapLoading: true, // 全屏显示加载窗
            center: process.env.CENTER_POSITION, // 初始位置
            mapEvents: {
              // 地图事件
              //zoomchange: this.zoomChange,
              complete: this.loadComplete
            },
            markerEvents:{
              click: this.markerClick,
              mouseover:this.mapwindowMouseinter, //新添加的鼠标移入
              mouseout: this.mapWindowMouseout,
            },
            mapWindowShow: false,
            mapWindowPosition: [0, 0],
            mapWindowContent: "",
            enterpriseInfo: [],
            girdInfo:[],
            // strokeColor :'yellow',
            strokeColor :'#FF3399',
          }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
      },
      mounted () {

      },
      methods:{
        loadComplete(e){
          this.mapLoading = false;
          this.map = this.$refs['map'].$$getInstance();
          createDistrictPolygon(process.env.CURRENT_CITY, this.map);
          this.init();

        },
        init(){
          getEnterpriseInfo({finalAreaId:this.user.areaId,enterpriseStatus:'3'}).then(response=>{
            //debugger
            this.enterpriseInfo = response.data;
            createMarkerCluster(this.map, 1, this.enterpriseInfo, this.markerEvents);
          })
          getGridsInfo({areaId:this.user.areaId,gridStatus:1}).then(response=>{
            this.girdInfo=response.data;
            // console.log(this.girdInfo);
          })
        },
        lnglatFormatter(item){
          return [item.longitude, item.latitude]
        },
        markerClick(e){
          let data = e.target.getExtData(); 
          if (data) {
            //this.mapWindowShow = false;
            this.mapWindowPosition = [e.lnglat.lng, e.lnglat.lat];
            let showContent = `<div>
                    <p><span>企业名称：</span><span>${data.enterpriseName}</span></p>
                    <p><span>法人代表：</span><span>${data.corporateRepresentative}</span></p>
                    <p><span>经济性质：</span><span>${this.economicNatureFormatter(data.economicNature)}</span></p>
                    <p><span>主体分类：</span><span>${this.subjectClassificationFormatter(data.subjectClassification)}</span></p>
                    <p><span>信用等级：</span><span>${this.superviseClassificationFormatter(data.superviseClassification)}</span></p>
                    <p><span>联系人：</span><span>${data.contacts}</span></p>
                    <p><span>联系电话：</span><span>${data.contactNumber}</span></p>
                    </div>`;
            this.mapWindowContent = `<div style="color: #fff;font-size: 10px;">${showContent}</div>`;
            this.$nextTick(function() {
              this.mapWindowShow = true;
            });
          }
        },
        mapWindowMouseout(){
          this.mapWindowShow = false;
        },
        mapwindowMouseinter(e){
          let data = e.target.getExtData();
          if (data) {
            this.mapWindowPosition = [e.lnglat.lng, e.lnglat.lat];
            let showContent = `<div  >
                    <span ">${data.enterpriseName}</span>
                    </div>`;
            this.mapWindowContent = `<div style="color: #fff;font-size: 10px;">${showContent}</div>`;
            this.$nextTick(function() {
              this.mapWindowShow = true;
            });
          }
        },
        subjectClassificationFormatter(value){
          return parseValueToText(value, this.staticData["企业主体分类"]);
        },
        economicNatureFormatter(value){
          return parseValueToText(value, this.staticData["企业经济性质"]);
        },
        superviseClassificationFormatter(value) {
          return parseValueToText(value, this.staticData["监管级别"]);
        },
      },
    }
</script>

<style scoped>
  .amap-size{
    width: 100%;
    height: 100%;
    position: relative;
  }

</style>
