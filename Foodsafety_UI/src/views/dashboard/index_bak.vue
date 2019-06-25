<template>
  <div class="main-page" v-if="user.userType==3">
      <!-- 模式切换 -->
      <div class="pattern-container">
        <el-radio-group v-model="pattern" @change="patternChange">
          <el-radio-button label="企业视图"></el-radio-button>
          <el-radio-button label="预警视图"></el-radio-button>
          <el-radio-button label="事务视图" v-if="user.userType==3 && user.personnelId>0"></el-radio-button>
        </el-radio-group>
      </div>

      <!-- 地图组件 -->
      <el-amap
        ref="map"
        vid="MainMap"
        :center="center"
        :zoom="zoom"
        :viewMode="viewMode"
        :pitch="pitch"
        :events="mapEvents"
        :mapStyle="mapStyle"
        :features="features"
        :plugin="plugin"
      >

        <!-- 当前网格多边形 -->
        <template v-for="(item, index) in gridOptions">
          <el-amap-polygon v-if="item.grid.markers.length>0"
                           :path="item.grid.markers"
                           :strokeColor="strokeColor"
                           strokeOpacity=".8"
                           :strokeWeight="2"
                           fillColor="#fff"
                           fillOpacity=".5"
                           :events="polygonEvents"
                           :extData="{'gridId':item.grid.gridId}">
          </el-amap-polygon>
        </template>



        <!--<el-amap-text text="25" :position="[121.227876,31.034817]"></el-amap-text>-->

        <!-- 区域信息气泡框 -->
        <!--<el-amap-info-window v-if="showInfoWindow" :position="windowPosition">
            <ul style="margin: 5px; line-height: 25px; list-style: none; padding-left: 0;">
                <li>
                    <span>经度：</span><span class="grey">{{windowContent.lng || 0}}</span>
                </li>
                <li>
                    <span>纬度：</span><span class="grey">{{windowContent.lat || 0}}</span>
                </li>
                &lt;!&ndash;<li>
                    <span>视频状态：</span><div :class="vWindowContent.status==1?'videoNormal':'videoAbnormal'"></div><span class="grey">{{vWindowContent.status==1?'正常':'故障'}}</span>
                </li>
                <li>
                    <span>操作：</span><a href="javascript:void(0)" @blur="cancelTimer()" @click="play()" style="display: inline-block; padding: 0 8px; border-radius: 5px; color: #FFF; background-color: #1c8de0; ">播放</a>&lt;!&ndash;<el-button type="primary" size="mini" @blur="cancelTimer()" @click="play">播放</el-button>&ndash;&gt;
                </li>&ndash;&gt;
            </ul>
        </el-amap-info-window>-->


      </el-amap>
      <el-dialog title="详情" :visible.sync="dialogVisible" @close="closeDialog" :close-on-click-modal="false"  width="75%">
        <EnterpriseView :currAreaInfo="currAreaInfo" v-if="dialogVisible&&pattern=='企业视图'"></EnterpriseView>
        <CertView :currAreaInfo="currAreaInfo" v-if="dialogVisible&&pattern=='预警视图'"></CertView>
        <TaskView :currAreaInfo="currAreaInfo" v-if="dialogVisible&&pattern=='事务视图'"></TaskView>
      </el-dialog>
  </div>
  <div class="main-page" v-else-if="user.userType!==3" style="background-color: #1f2d3d"></div>
</template>


<script>
  import { mapGetters } from "vuex";
  import {getGridsEnterpriseStatistics,getGridsCertificateStatistics,getGridsTaskStatistics} from 'api/admin/dashboard/index'
  import EnterpriseView from './enterpriseView'
  import CertView from './certView'
  import TaskView from './taskView'
  import {createDistrictPolygon} from 'api/model';
  export default {
    name: 'dashboard',
    components: {CertView,EnterpriseView,TaskView},
    data(){
      return {
        plugin: [/*{
                pName: 'MapType',
                defaultType: 0
              },{
                pName: 'ControlBar',
                position: {
                  left:'-80px',
                  top:'80px'
                }
              },{
          pName: 'ToolBar',
          locate: false,
          noIpLocate: false,
          useNative: false
        },'Scale','OverView'*/],
        features: ['bg','road',/*'building',*/'point'],
        pattern: '企业视图', // 1企业视图 2、监管视图  2、其它视图
        zoom: process.env.MAP_ZOOM, // 缩放等级
        center: process.env.CENTER_POSITION, // 初始位置
        viewMode:process.env.VIEW_MODE, // 地图模式 2D 3D
        pitch:process.env.PITCH,// 俯仰角度 2D无效
        mapEvents: { // 地图事件
          complete: this.loadComplete,
          zoomchange: this.zoomChange,
        },
        polygon: [[121.227833,31.037759], [121.223155,31.033015], [121.231867,31.032978]], // 多边形对象
        polygonEvents: {  // 多边形事件
          click: this.polygonClick,
          // mouseover: this.polygonMouseover
        },
        gridOptions:[],//网格对象
        // mapStyle: 'amap://styles/', // 地图主题
        // strokeColor: 'green', // 区域多边形边框颜色
        mapStyle : 'amap://styles/macaron',
        strokeColor :'#FF3399',
        currAreaInfo: '',  // 当前点击的区域信息
        dialogVisible: false, //是否显示详情窗口
        showInfoWindow: false,  // 是否显示网格弹窗
        windowPosition: [],  // 网格弹窗坐标
        windowContent: '',  // 网格弹窗内容
        tabsValue: '1',  // 网格弹窗内选项卡当前选中项
        textArr: [],
        textCountName:"企业数量"

      }
    },

    computed: {
      ...mapGetters(["staticData","user"]),
    },
    mounted () {
      // 请求网格数据

    },
    created(){
        this.init();
    },
    methods: {
      init(){
        if(this.user.userType==3){
          this.getGridsEnterpriseStatisticsList();
        }
      },
      loadComplete(e) { // 地图加载完成事件
        this.map = this.$refs['map'].$$getInstance();
        createDistrictPolygon(process.env.CURRENT_CITY,this.map);
        this.renderText();
        this.map.setFitView();
      },
      //企业视图网格数据加载
      getGridsEnterpriseStatisticsList(){
        getGridsEnterpriseStatistics({areaId:this.user.areaId}).then((response)=>{
          this.gridOptions=response.data;
          this.renderText();
        });
      },
      //预警视图网格数据加载
      getGridsCertificateStatisticsList(){
        getGridsCertificateStatistics({areaId:this.user.areaId}).then((response)=>{
          this.gridOptions=response.data;
          this.renderText();
        });
      },
      //事物视图网格数据加载
      getGridsTaskStatisticsList(){
        getGridsTaskStatistics({areaId:this.user.areaId}).then((response)=>{
          this.gridOptions=response.data;
          this.renderText();
       });
  },
      zoomChange(e){ // 地图缩放事件
        this.zoom = this.$refs['map'].$$getInstance().getZoom();
        this.renderText();
      },
      renderText(){  // 地图初始化完成后调用
        let _this = this;
        let arr = [];
        const map = _this.$refs.map.$$getInstance();
        _this.textArr.length > 0 && _this.textArr.forEach(item => {
         item.hide();
       });
        _this.textArr = [];
        _this.gridOptions.forEach(item=>{
          const data={};
          data.title=item.count==0?"0":item.count;
          data.position=[item.grid.centerLongitude,item.grid.centerLatitude];
          // 创建纯文本标记
          // var text = new AMap.Text({
          //   text: data.title,
          //   position: data.position,
          //   verticalAlign: 'center',
          //   map: map,
          //   visible: true,
          //   style: {
          //     'background-color': '#3399FF',
          //     'color': 'white',
          //     'opacity': '.8',
          //     'font-size': '16px',
          //     /*'transform': `scale(${_this.getSize()}) translate(0 , -10px)`*/
          //   //  'transform': `scale(${_this.getSize()}) translate(0 , -10px)`
          //   }
          // });


          var text1 = new AMap.Text({
            text: `<p>网格名称：${item.grid.gridName}</p><p>${this.textCountName}：${data.title}</p>`,
            position: data.position,
            verticalAlign: 'center',
            map: map,
            visible: true,
            offset:new AMap.Pixel(50,-50),
            zoom:20,
            style: {
              'padding': '0 .5rem 0 .5rem',
              'border-radius': '.25rem',
              'background-color': 'white',
              'border-width': 0,
              'box-shadow': '0 2px 6px 0 rgba(50, 50, 50, .5)',
              'text-align': 'left',
              'font-size': '14px',
              'color': '#3e3e3e',
              'opacity': '.8',
            }
          }).on('click', function (e) {
            _this.currAreaInfo ={'gridId':item.grid.gridId};
            _this.dialogVisible = true;
          });

          arr.push(text1);
          _this.textArr = arr;
        })

      },
      getSize(){
        let ratio = {18:1, 17:.9, 16:.8, 15:.7, 14:.4, 13:.3};
        return ratio[this.zoom] || 0
      },
      patternChange(val){
        // 切换地图主题
        switch (val){
          case '企业视图':
            // this.mapStyle = 'amap://styles/';
            // this.strokeColor = 'green';
            this.mapStyle = 'amap://styles/macaron';
            this.strokeColor = '#FF3399';
            this.textCountName="企业数量";
            this.getGridsEnterpriseStatisticsList();
            break;
          case '预警视图':
            this.mapStyle = 'amap://styles/darkblue';
            this.strokeColor = '#3333FF';
            this.textCountName="预警数量";
            this.getGridsCertificateStatisticsList();
            break;
          case '事务视图':
            this.mapStyle = 'amap://styles/light';
            this.strokeColor = '#666666';
            this.textCountName="事务数量";
            this.getGridsTaskStatisticsList();
            break;
          default:
            this.mapStyle = 'amap://styles/macaron';
            this.strokeColor = '#FF3399';
            this.textCountName="企业数量";
        }
      },
      polygonMouseover(e){  // 网格鼠标移入事件
        const aInfo = e.target.F.extData;
        const {lnglat:{lng, lat}} = e;
        this.showInfoWindow = false;
        this.windowPosition= [lng, lat];
        this.windowContent= {lng, lat};
        this.$nextTick(() => {
          this.showInfoWindow = true;
        });
      },
      polygonClick(e){  // 多边形点击事件
        this.currAreaInfo = e.target.getExtData();
        this.dialogVisible = true;
      },
      closeDialog(){
        // console.log('关闭详情窗口');
      },
      tabChange(val){
        // console.log(this.tabsValue);
      },

    }
  }
</script>

<style scoped>

  .main-page {
    width: 100%;
    height: 100%;
    position: relative;
  }

  .pattern-container {
    position: absolute;
    top: 20px;
    left: 20px;
    z-index: 1;
  }

  .grey {
    color: #666;
  }


</style>
