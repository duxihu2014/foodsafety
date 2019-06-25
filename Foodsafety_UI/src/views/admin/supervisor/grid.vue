<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">

          <el-form-item label="乡镇(街道)"  class="filter-item">
            <el-input   style="width: 200px;" placeholder="" v-model.trim="listQuery.townStreet"> </el-input>
          </el-form-item>

          <el-form-item label="居委会(村)"  class="filter-item">
            <el-input   style="width: 200px;" placeholder="" v-model.trim="listQuery.committeeVillage"> </el-input>
          </el-form-item>


          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.gridStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="监管员"   class="filter-item">
            <el-select  v-model="listQuery.monitors" placeholder="请选择" clearable filterable>
              <el-option v-for="item in  monitorsOptions " :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
            </el-select>
          </el-form-item>

            <el-form-item label="协管员"   class="filter-item">
              <el-select  v-model="listQuery.assistants" placeholder="请选择" clearable filterable>
                <el-option v-for="item in  assistantsOptions" :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
              </el-select>
            </el-form-item>

          <el-form-item label="检验员"   class="filter-item">
            <el-select  v-model="listQuery.standards" placeholder="请选择" clearable  filterable>
              <el-option v-for="item in  standardsOptions" :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
            </el-select>
          </el-form-item>



          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>

    <div class="head-box">
      <div class="item-box">
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
      </div>
    </div>

    <el-table   :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="网格编号" width="80" prop="gridId"></el-table-column>
      <el-table-column align="center" label="网格名称" width="150" prop="gridName"></el-table-column>
      <el-table-column align="center" label="乡镇(街道)" width="150" prop="townStreet"  ></el-table-column>
      <el-table-column align="center" label="居委会(村)" width="200" prop="committeeVillage" ></el-table-column>
      <el-table-column align="center" label="监督员" width="100" prop="monitorsName" ></el-table-column>
      <el-table-column align="center" label="协管员" width="100" prop="assistantsName" ></el-table-column>
      <el-table-column align="center" label="检验员" width="100" prop="standardsName" ></el-table-column>
      <el-table-column align="center" label="状态" width="100" prop="gridStatus" :formatter="statusFormatter"></el-table-column>

      <el-table-column  align="center" fixed="right" label="操作" width="150"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)"  >编辑</el-button>

        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="200"  v-else>
        <template slot-scope="scope">
          <el-button size="mini"  type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)" >编辑</el-button>
         </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container" >
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false"  >
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '创建'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '编辑'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>



      <el-form :model="form" :rules="rules" ref="form" label-width="120px" >
             <el-row>

              <el-col :span="12">
                <el-form-item label="网格名称" prop="gridName" >
                  <el-input v-model.trim="form.gridName" placeholder=""  :readonly="viewReadOnly" ></el-input>
                </el-form-item>
              </el-col>


              <el-col :span="12">
                <el-form-item label="街道(乡镇)" prop="townStreet"  >
                  <el-input   v-model.trim="form.townStreet"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>

            </el-row>
            <el-row>

              <el-col :span="12">
                <el-form-item label="居委会(村)" prop="committeeVillage"  >
                  <el-input   v-model.trim="form.committeeVillage"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检验员" prop="standards" >
                  <el-select  v-model="form.standards" placeholder="请选择" clearable :disabled="viewReadOnly">
                    <el-option v-for="item in  standardsOptions" :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="监管员" prop="monitors"  >
                  <el-select  v-model="form.monitors" placeholder="请选择" clearable :disabled="viewReadOnly">
                    <el-option v-for="item in  monitorsOptions " :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="协管员" prop="assistants"  >
                  <el-select  v-model="form.assistants" placeholder="请选择" clearable :disabled="viewReadOnly">
                    <el-option v-for="item in  assistantsOptions" :key="item.personnelId" :label="item.personnelName" :value="item.personnelId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="标注网格" prop="markers" ref="markers" >
              <el-card :body-style="{ padding: '0px' }" shadow="never">
                <div style=" margin-left: 5px;" v-show="!viewReadOnly">
                  <el-button  size="mini" @click="resetGridding">重置</el-button>
                  <el-button  size="mini" @click="clearGridding">清除</el-button>
                </div>
                <div class="map_container">
                  <el-amap
                    ref="map"
                    :center="center"
                    :zoom="zoom"
                    :events="mapEvents"
                    :resizeEnable="true" >
                    <!-- 标记组件 -->
                    <el-amap-marker
                      v-for="(marker, index) in markers"
                      :key="index"
                      :vid="marker.vid"
                      :visible="marker.visible"
                      :offset="offset"
                      :position="marker.position"
                      :title="marker.position"
                      :icon="marker.icon"
                      :events="markEvents"
                      :draggable="!viewReadOnly"
                      :raiseOnDrag="true"></el-amap-marker>

                    <template v-for="(grid, index) in otherGrids">
                      <!-- 其它网格多边形 -->
                      <el-amap-polygon v-if="grid.maps.length>0"
                                       :path="formatGrid(grid.maps)"
                                       strokeColor="green"
                                       strokeOpacity=".8"
                                       :strokeWeight="2"
                                       fillColor="#0AB5F5"
                                       fillOpacity=".5"
                                       :events="otherGridEvents"
                                       :extData="grid.grid">
                      </el-amap-polygon>
                    </template>

                    <!-- 当前网格多边形 -->
                    <el-amap-polygon
                      :path="polygon"
                      strokeColor="red"
                      strokeOpacity=".8"
                      :strokeWeight="2"
                      fillColor="#FF5733"
                      fillOpacity=".5"
                    ></el-amap-polygon>

                    <!-- 网格信息窗口 -->
                    <el-amap-info-window
                      :content="mapWindowContent"
                      :position="mapWindowPosition"
                      :visible="mapWindowShow"
                    ></el-amap-info-window>

                  </el-amap>
                </div>
              </el-card>
             </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="manager_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>

  import { page, addObj, putObj, getObj,changeStatus,getOtherGrids} from "api/admin/supervisory/grid";
  import { getAllPersonnalThree} from "api/admin/supervisory/three";
  import { parseValueToText ,parseForm} from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import {getToken} from 'utils/auth';
  import {createDistrictPolygon} from 'api/model';

  export default {
    name: "index",
    data(){
      let  markersValidator = (rule, value, callback) => {
        let points = this.markers.filter((item)=>{
          return item.visible;
        });
        if(points.length < 3){
          return callback(new Error("标注点不能少于3个"));

        }else  callback();
      };
      return{
        height:undefined,
        list: null,
        total: null,
        listLoading: false,
        suppliersList:[],//供货商
        listQuery: {
          page: 1,
          limit: 20,
          personnelName: "",
          idCardNo: ""
        },
        form: {
          personnelName:undefined
        },
        monitorsOptions:[],
        assistantsOptions:[],
        standardsOptions:[],
        rules: {
          gridName: [{required: true, message: "请输入网格名称", trigger: "blur"}],
          townStreet: [{required: true, message: "请输入街道(乡镇)", trigger:  "blur"}],
          committeeVillage: [{required: true, message: "请输入居委会(村)", trigger: "blur"}],
          monitors: [{required: true, message: "请选择监管员", trigger: "change"}],
          assistants: [{required: true, message: "请选择协管员", trigger: "change"}],
          standards: [{required: true, message: "请选择检验员", trigger: "change"}],
           markers:[ {validator: markersValidator, trigger: "blur" }]
        },
        dialogFormVisible: false,
        viewReadOnly:false, //控制查看时的按钮显示
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        manager_update:true,
        selection: [],
        needFixedRight:false,
        zoom: process.env.MAP_ZOOM, // 缩放等级
        center: process.env.CENTER_POSITION, // 初始位置
        mapEvents: { // 地图事件
          click: this.addMarker,
          zoomchange : this.zoomChange,
          complete : this.loadComplete
        },
        visible: false, // 是否显示标记
        position: ["0","0"], // 标记位置
        markEvents: {
          rightclick: this.markerRightClick,
          dragend: this.markerDragend
        },
        polygon: [], // 多边形对象
        markers: [], // 坐标集
        offset: [-13,-13], // marker偏移量
        otherGrids:[],
        otherGridEvents: {
          mouseover: this.mapWindowMouseover,
          mouseout: this.mapWindowMouseout,
        },
        mapWindowShow:false,
        mapWindowPosition:[0,0],
        mapWindowContent:"",
        currentMap:[],
        map:undefined

      }
    },
    created() {
      this.getList();
      this.personnelTypeOptions.forEach((item)=>{
         getAllPersonnalThree({personnelType:item.value,gridStatus:'1',institutionId:this.user.institutionId}).then((response)=>{
           if(item.text.indexOf("监管员")>=0)
            this. monitorsOptions=response;
          else if(item.text.indexOf("协管员")>=0)
            this. assistantsOptions=response;
          else if(item.text.indexOf("检验员")>=0)
            this. standardsOptions=response;
        });
      });
    },
    mounted () {
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function () {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      });
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      }
      this.height=loadGridHeight();

     },
    computed: {
      ...mapGetters(["staticData","user"]),
      statusOptions(){ return this.staticData["状态"]; },
      personnelTypeOptions(){ return this.staticData["三员类型角色编号设置"]; }
    },
    methods: {
      loadComplete(e) { // 地图加载完成事件
        this.map = this.$refs['map'].$$getInstance();
        createDistrictPolygon(process.env.CURRENT_CITY,this.map);
       },
      mapWindowMouseover(e){ // 其它网格多边形鼠标移入事件
         let grid = e.target.getExtData();
        if(grid){
          this.mapWindowPosition = [e.lnglat.lng, e.lnglat.lat];
          let showContent=`<div class="info_window_msg">
                    <p><span>区域名称：</span><span>${grid.gridName}</span></p>
                    <p><span>乡镇(街道)：</span><span>${grid.townStreet}</span></p>
                    <p><span>居委会(村)：</span><span>${grid.committeeVillage}</span></p>
                    <p><span>监督员：</span><span>${grid.monitorsName}</span></p>
                    <p><span>协管员：</span><span>${grid.assistantsName}</span></p>
                    <p><span>检验员：</span><span>${grid.standardsName}</span></p>
                    </div>`;
          this.mapWindowContent =`<div style="color: #3399FF;font-size: 12px;">${showContent}</div>`;

          this.$nextTick(function () {
            this.mapWindowShow = true;
          });
        }
      },
      mapWindowMouseout(){ // 其它网格多边形鼠标移出事件
        this.mapWindowShow = false;
      },
      formatGrid(grid){ // 对象转数组
         return grid.map(item=>{
          return  [item.lng, item.lat];
        });
      },
      markerRightClick(e) { // 标记右击事件：移除标记
        if(!this.viewReadOnly){
          this.markers.forEach((item)=>{
            if(item.position.toString() === e.target.getTitle().toString()){
              item.visible = false;
              return true;
            }
          });
          this.drawGrid();
        }
      },
      clearGridding() {  // "重置网格"按钮点击事件
        this.markers.forEach((item)=>{
          item.visible =false;
        });
        this.markers.length = 0;
        this.polygon = 0;
      },
      resetGridding(){
        this.clearGridding();
        if( this.currentMap.length >= 3 ){
          // 设置地图中心点
          this.center = this.currentMap[0];
          this.currentMap.forEach(item => {
            this.addMarker({lnglat: {lng: item[0], lat: item[1]}});
          });
        }
      },

      addMarker(e) { // 地图单击事件：添加一个标记
        if(!this.viewReadOnly) {
          this.$refs['markers'].clearValidate();
          this.markers.push({
            //icon: this.markers.length > 0 ? require('assets/pointIcon.png') : require('assets/centerIcon.png'),
            icon: require('assets/point.png'),
            position: [e.lnglat.lng, e.lnglat.lat],
            visible: true,
            vid: this.markers.length > 0 ? this.markers[this.markers.length - 1].vid + 1 : 1
          });
          this.drawGrid();
        }
      },
      drawGrid() { // 绘制网格区域
        let paths = [];
        this.polygon = 0;
        this.markers.forEach((item)=>{
          item.visible /*&& item.vid !== 1*/ && paths.push(item.position);
        });
        if(paths.length >= 3){this.polygon = paths}
      },
      markerDragend(e) { // 标记拖动事件
        this.markers.map((item)=>{
          if(item.position.toString() === e.target.getTitle().toString()){
            item.position = [e.lnglat.lng, e.lnglat.lat];
            e.target.setTitle(item.position.toString());
            return true;
          }
        });
        this.drawGrid();
      },
      zoomChange(e){ // 地图缩放事件
        this.zoom = this.$refs['map'].$$getInstance().getZoom();
      //   this.initBounds();
      },
      getValidDateType(validStatus){
        switch (validStatus){
          case '2':
            return 'warning';
          case '3':
            return 'danger';
        }
      },
      tableRowClassName({row, rowIndex}) {
        if (row.staffStatus === '0') {
          return 'warning-row';
        } else if (row.staffStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      getList() {
        this.listLoading = true;
        page(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      handleFilter() {
        this.listQuery.page=1;
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      resetQuery() {
        this.listQuery.gridName="";
        this.listQuery.townStreet = "";
        this.listQuery.committeeVillage = "";
        this.listQuery.monitors = "";
        this.listQuery.assistants = "";
        this.listQuery.standards = "";
        this.listQuery.gridStatus = "";
      },

      create(formName) {
        const set = this.$refs;
        set[formName].validate((valid,errors) => {
          if (valid) {
            let points = this.markers.filter((item)=>{
              return item.visible;
            });
             if(points.length < 3){
              this.$message({
                type: 'error',
                showClose: true,
                message: '标记点不能少于3个'
              });
              return;
            }
            let pointMap = []; //范围点坐标
            points.forEach((item)=>{
              pointMap.push(item.position);
            });
            this.form.markers=pointMap;

            addObj(this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$message({
                showClose: true,
                message: "创建成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      cancel() {
        this.dialogFormVisible = false;
        this.resetTmp();
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate((valid,errors) => {
          if (valid) {

            let points = this.markers.filter((item)=>{
              return item.visible;
            });

             if(points.length < 3){
              this.$message({
                type: 'error',
                showClose: true,
                message: '标记点不能少于3个'
              });
              return;
            }
            let pointMap = []; //范围点坐标
            points.forEach((item)=>{
              pointMap.push(item.position);
            });
            this.form.markers=pointMap;

            putObj(this.form.gridId,this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$message({
                showClose: true,
                message: "更新成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      handleView(row) {
        getOtherGrids({gridId:row.gridId,institutionId:this.user.institutionId}).then((response)=>{
          this.otherGrids=this.mapGrouping(response.data);
          // console.log( this.otherGrids)
        });

        getObj(row.gridId).then(response => {
          this.form = response.data;

          if(this.form.markers.length>=3){
            this.form.markers.forEach((item)=>{
              this.markers.push({
                icon: require('assets/point.png'),
                position: [item[0], item[1]],
                visible: true,
                vid: this.markers.length > 0 ? this.markers[this.markers.length-1].vid + 1 : 1
              });
            });
            this.currentMap=this.form.markers;
            this.center = this.currentMap[0];
            this.drawGrid();
          }
          this.manager_update = false;
          this.viewReadOnly=true;
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
        });

      },
      mapGrouping(mapdata){
        let a={};
        let b=[];
        mapdata.forEach((item)=>{
            if (typeof (a[item.gridId]) == 'undefined') {
              a[item.gridId] = b.length;
              b[a[item.gridId]]={grid:item,maps:[]};
            }
            b[a[item.gridId]].maps.push(item);
        });

        return b;
      },
      handleUpdate(row) {

        getOtherGrids({gridId:row.gridId,institutionId:this.user.institutionId}).then((response)=>{
           this.otherGrids=this.mapGrouping(response.data);
          });

        getObj(row.gridId).then(response => {
          this.manager_update = true;
          this.form = response.data;

          if(this.form.markers.length>=3){
            this.form.markers.forEach((item)=>{
              this.markers.push({
                icon: require('assets/point.png'),
                position: [item[0], item[1]],
                visible: true,
                vid: this.markers.length > 0 ? this.markers[this.markers.length-1].vid + 1 : 1
              });
            });
            this.currentMap=this.form.markers;
            this.center = this.currentMap[0];
            this.drawGrid();
          }
          this.viewReadOnly=false;
          this.dialogFormVisible = true;
          this.dialogStatus = "update";
        });
      },
      handleCreate(row) {

        getOtherGrids({institutionId:this.user.institutionId}).then((response)=>{
          this.otherGrids=this.mapGrouping(response.data);
        });

        this.manager_update = true;
        this.dialogStatus = "create";
        this.viewReadOnly=false;
        this.dialogFormVisible = true;
      },
      //关闭弹出框时，清除表单验证，并且设置表单元素为空
      closeDialog(){
        this.resetTmp();
      },
      statusFormatter( row, column, cellValue ) {
        return parseValueToText(cellValue, this.statusOptions);
      },

      resetTmp() {
        this.form={ gridName:undefined,
          monitors: undefined,
          assistants: undefined,
          standards:undefined};

       this.clearGridding();
       this.currentMap=[];
       this.zoom= process.env.MAP_ZOOM, // 缩放等级
        this. center=process.env.CENTER_POSITION, // 初始位置
        this.$nextTick(function() {
          this.$refs['form'].clearValidate();
        });
      },
      getSelection(selection) {
        this.selection = selection;
      },
      tableStop: function () {  // 0停用
        this.stopOrStart(0);
      },
      tableStart: function () {  // 1启用
        this.stopOrStart(1);
      },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.selection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.selection.forEach(item => {
            if(item.gridStatus==1){
              flag=false;
            }
          });
          if(!flag){
            this.$message({
              showClose: true,
              type:'warning',
              message: "您选择的数据中有启用的数据，不能重复启用！"
            });
            return;
          }
        }else{//停用
          this.selection.forEach(item => {
            if(item.gridStatus==0){
              flag=false;
            }
          });
          if(!flag){
            this.$message({
              showClose: true,
              type:'warning',
              message: "您选择的数据中有停用的数据，不能重复停用！"
            });
            return;
          }
        }
        let operation = num?'启用':'停用';
        this.$confirm(`确定要${operation}该数据吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = [];
          this.selection.forEach((item)=>{
            ids.push(item.gridId);
          });
          ids= ids.length>1?ids.join(','):ids[0];
          let param = {"ids":ids, "status":num};
          changeStatus(param).then((response) => {
            if(response.rel){
              this.$message({
                type:'success',
                showClose: true,
                message: `${operation}成功！`
              });
            }else {
              this.$message({
                type:'error',
                showClose: true,
                message: `${operation}失败！`
              });
            }
            this.getList();
          }).catch(() => {
            this.$message({
              type: 'error',
              showClose: true,
              message: '网络错误，数据请求失败。'
            });
          });
        }).catch(() => {});
      },

    }
  }
</script>


<style scoped>


  .map_container{
    position: relative;
    width: 100%;
    height: 400px;
  }

</style>
