<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="企业名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" placeholder="" :clearable="true" v-model.trim="listQuery.enterpriseName"> </el-input>
          </el-form-item>
          <el-form-item label="主体分类" class="filter-item">
            <el-select v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%" highlight-current-row @selection-change="getSelection" :height="height">
      <el-table-column align="center" label="企业名称" width="250px" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="所属区域" width="120px" prop="areaName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="变更状态" width="120px" prop="verifyStatus" >
        <template slot-scope="scope">
          <el-tag :type="getVerifyStatus(scope.row.verifyStatus)" prop="inspectionDate">
            {{verifyStatusFormatter(scope.row.verifyStatus)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="主体分类" width="200px" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180px" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="营业执照编号" width="200px" prop="registerCertificate.certificateNo" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册地址" width="300px" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="生产地址" width="300px" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系人" width="100" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDeal(scope.row)" :disabled="scope.row.verifyStatus==1">建档
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDeal(scope.row)" :disabled="scope.row.verifyStatus==1">建档
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  title="核查建档" fullscreen :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <i class="el-icon-document">核查建档</i>
      </div>
      <el-form :model="result" ref="form" :rules="rules" label-width="110px">
        <div style="height: 79vh;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="建档信息">
              <el-row>
                <el-col :span="5" >
                  <el-form-item label="监管级别" prop="regulatoryLevel" ref="regulatoryLevel" class="filter-item" tab="0">
                    <el-select v-model="result.regulatoryLevel" placeholder="请选择" clearable filterabler>
                      <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6" >
                  <el-form-item label="企业地址坐标" prop="lnglat" class="filter-item">
                    <el-input v-model="result.lnglat" placeholder="企业地址坐标" >
                      <i slot="suffix" class="el-icon-location" @click="handleIconClick"></i>
                    </el-input >
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="责任区域" prop="regulatory" class="filter-item">
                    <el-input v-model="gridName" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
                  <div class="map_container" style="height:600px">
                    <div style="width: 90%;height: 100%; ">
                      <el-amap ref="map" :center="center" :zoom="zoom" :events="mapEvents" :resizeEnable="true">

                        <template v-for="(grid, index) in otherGrids">
                          <!-- 其它网格多边形 -->
                          <el-amap-polygon v-if="grid.maps.length>0"
                                           :ref="`polygon_${index}`"
                                           :path="grid.maps"
                                           strokeColor="green"
                                           strokeOpacity=".8"
                                           :strokeWeight="2"
                                           fillColor="#0AB5F5"
                                           fillOpacity=".2"
                                           :events="otherGridEvents"
                                           :extData="grid.grid">
                          </el-amap-polygon>
                        </template>
                        <!-- 标记组件 -->
                        <el-amap-marker
                          :position="position"
                          :visible="visible"
                        ></el-amap-marker>
                        <!-- 网格信息窗口 -->
                        <el-amap-info-window :content="mapWindowContent" :position="mapWindowPosition" :visible="mapWindowShow"></el-amap-info-window>

                      </el-amap>
                    </div>

                  </div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="企业信息" name="first">
              <el-row>
                <el-col :span="11">
                  <el-form-item label="企业名称" prop="enterpriseName">
                    <el-input v-model="form.enterpriseName" placeholder="企业名称" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
                    <el-input v-model="form.organizingInstitutionBarCode" placeholder="社会信用代码" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="法人代表" prop="corporateRepresentative">
                    <el-input v-model="form.corporateRepresentative" placeholder="法人代表" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="证件号码" prop="idCardNo">
                    <el-input v-model="form.idCardNo" placeholder="证件号码" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="主体分类" prop="subjectClassification">
                    <el-select v-model="form.subjectClassification" placeholder="请选择" :disabled="true">
                      <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="所属区域" prop="areaOptions">
                    <el-input v-model.trim="form.areaName" placeholder="" disabled clearable></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="注册资金" prop="registeredCapital">
                    <el-input v-model="form.registeredCapital" placeholder="注册资金" :disabled="true">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="经济性质" prop="economicNature">
                    <el-select v-model="form.economicNature" placeholder="请选择" :disabled="true">
                      <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="联系人" prop="contacts">
                    <el-input v-model="form.contacts" placeholder="联系人" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="联系电话" prop="contactNumber">
                    <el-input v-model="form.contactNumber" placeholder="联系电话" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="联系人手机号" prop="contactMobile">
                    <el-input v-model="form.contactMobile" placeholder="联系人手机号" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="检验员手机" prop="inspectorMobile">
                    <el-input v-model="form.inspectorMobile" placeholder="检验员手机" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="邮政编码" prop="postalCode">
                    <el-input v-model="form.postalCode" placeholder="邮政编码" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="企业注册日期" prop="registerDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerDate" style="width: 100%;" :disabled="true" :clearable="true"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="注册地址" prop="registerAddress">
                    <el-input v-model="form.registerAddress" placeholder="注册地址" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="生产地址" prop="productionAddress">
                    <el-input v-model="form.productionAddress" placeholder="生产地址" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>

          </el-tabs>
        </div>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="comp_deal" style="text-align: center">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="create()">确 定</el-button>
      </div>

    </el-dialog>
    <el-dialog>

    </el-dialog>
  </div>
</template>

<script>
import { getInspectionPlan } from "api/admin/safety/inspection/index";
import {
  getEnterprisePage,
  setRegulatoryLevel
} from "api/admin/enterprise/index";
// import { addObj } from "api/admin/safety/onsite/result";
import { all } from "api/admin/safety/inspection/template/index";
import { mapGetters } from "vuex";
import { loadGridHeight } from "api/screen";
import { parseValueToText, parseTime, parseForm } from "utils/index";
import {  getObj } from "api/admin/supervisory/grid";
import { createDistrictPolygon } from "api/model";

export default {
  name: "onsite",
  components: {},
  data() {
    return {
      height: undefined,
      form: {
        enterpriseId: undefined,
        enterpriseName: undefined,
        corporateRepresentative: undefined,
        registerAddress: undefined,
        productionAddress: undefined,
        subjectClassification: undefined,
        superviseClassification: undefined,
        registerDate: undefined,
        areaName: undefined
      },
      result: {
        enterpriseId: undefined,
        regulatoryLevel: undefined,
        lnglat:undefined,
      },
      rules: {
        regulatoryLevel: [
          { required: true, message: "请选择监管级别", trigger: "change" }
        ],
        lnglat:[{ required: true, message: "请选择企业坐标", trigger: "change" }]
      },
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        enterpriseName: undefined,
        subjectClassification: undefined,
        superviseClassification: undefined,
        enterpriseStatus: "1" //1
      },
      comp_deal: false,
      dialogFormVisible: false,
      tableKey: 0,
      inspectionPlanSelection: [],
      statusArray: [
        { value: "1", text: "本日" },
        { value: "2", text: "已超时" },
        { value: "3", text: "未超时" }
      ],
      templateOptions: [],
      needFixedRight: false,
      templateId: undefined,
      tabPosition: "0",
      // 地图相关
      zoom: process.env.MAP_ZOOM, // 缩放等级
      center: process.env.CENTER_POSITION, // 初始位置
      mapEvents: {
        // 地图事件
        zoomchange: this.zoomChange,
        complete: this.loadComplete
      },
      position: ["0", "0"], // 标记位置
      visible: false, // 是否显示标记
      otherGrids: [],
      otherGridEvents: {
        click: this.gridClick,
        rightclick	: this.mapWindowMouseover,
        mouseout: this.mapWindowMouseout
      },
      mapWindowShow: false,
      mapWindowPosition: [0, 0],
      mapWindowContent: "",
      map: undefined,
      gridName:undefined,
    };
  },
  mounted() {
    //首次整个视图都渲染完毕后执行
    this.$nextTick(function() {
      let tableDiv = this.$el.querySelector(".el-table__body-wrapper");
      this.needFixedRight = !(
        $(tableDiv)
          .attr("class")
          .indexOf("is-scrolling-none") >= 0
      );
    });
    //监听屏幕的改变
    window.onresize = () => {
      let tableDiv = this.$el.querySelector(".el-table__body-wrapper");
      this.needFixedRight = !(
        $(tableDiv)
          .attr("class")
          .indexOf("is-scrolling-none") >= 0
      );
    };
    this.height = loadGridHeight();
  },
  created() {
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData", "areaData", "user"]),
    subjectClassificationOptions() {
      return this.staticData["企业主体分类"];
    },
    economicNatureOptions() {
      return this.staticData["企业经济性质"];
    },
    superviseClassificationOptions() {
      return this.staticData["监管级别"];
    },
    treatmentMeasuresOptions() {
      return this.staticData["核查结论"];
    },
    statusOptions() {
      return this.statusArray;
    }
  },
  methods: {
    // //地图相关
    loadComplete(e) {
      // 地图加载完成事件
      this.map = this.$refs["map"].$$getInstance();
      createDistrictPolygon(process.env.CURRENT_CITY, this.map);
    },
    gridClick(e) {
      this.position = [e.lnglat.lng, e.lnglat.lat];
      this.result.lnglat = e.lnglat.lng+","+ e.lnglat.lat;
      this.visible = true;
    },
    mapWindowMouseover(e) {
      // 其它网格多边形鼠标移入事件
      let grid = e.target.getExtData();
      if (grid) {
        this.mapWindowPosition = [e.lnglat.lng, e.lnglat.lat];
        let showContent = `<div class="info_window_msg">
                    <p><span>区域名称：</span><span>${grid.gridName}</span></p>
                    <p><span>乡镇(街道)：</span><span>${
                      grid.townStreet
                    }</span></p>
                    <p><span>居委会(村)：</span><span>${
                      grid.committeeVillage
                    }</span></p>
                    <p><span>监督员：</span><span>${
                      grid.monitorsName
                    }</span></p>
                    <p><span>协管员：</span><span>${
                      grid.assistantsName
                    }</span></p>
                    <p><span>检验员：</span><span>${
                      grid.standardsName
                    }</span></p>
                    </div>`;
        this.mapWindowContent = `<div style="color: #3399FF;font-size: 12px;">${showContent}</div>`;

        this.$nextTick(function() {
          this.mapWindowShow = true;
        });
      }
    },
    mapWindowMouseout() {
      // 其它网格多边形鼠标移出事件
      this.mapWindowShow = false;
    },
    zoomChange(e) {
      // 地图缩放事件
      this.zoom = this.$refs["map"].$$getInstance().getZoom();
    },
    getGrid(gridid) {
      getObj(gridid).then(response => {
        this.otherGrids = this.mapGrouping(response.data);
        this.gridName = response.data.gridName;
        this.center=[response.data.centerLongitude,response.data.centerLatitude];
      });
    },
    mapGrouping(mapdata) {
      let a = {};
      let b = [];
        if (typeof a[mapdata.gridId] == "undefined") {
          a[mapdata.gridId] = b.length;
          b[a[mapdata.gridId]] = { grid: mapdata, maps:mapdata.markers};
        }
      return b;
    },
    templateChange(value) {
      if (value) {
        this.templateId = value;
      }
    },
    getVerifyStatus(vaule){
      switch (vaule){
        case '0':
          return 'primary';
        case '1':
          return 'success';
        case '2':
          return 'danger';
      }
    },
    getList() {
      this.listLoading = true;
      getEnterprisePage(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
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
    create() {
      const set = this.$refs;
      set["form"].validate((valid, errors) => {
        if (valid) {
          this.result.enterpriseId = this.form.enterpriseId;
          let param = new FormData();
          param.append("enterpriseId", this.result.enterpriseId);
          param.append("regulatoryLevel", this.result.regulatoryLevel);
          param.append("lnglat", this.result.lnglat);
          setRegulatoryLevel(param).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
              message: "建档成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          for (var key in errors) {
            this.tabPosition = this.$refs[key].$attrs["tab"];
            break;
          }
          return false;
        }
      });
    },

    handleDeal(row) {
      this.form = row;
      if (this.form.registerDate) {
        this.form.registerDate = this.timeFormatter(
          this.form.registerDate,
          "{y}-{m}-{d}"
        );
      }
      this.comp_deal = true;
      this.dialogFormVisible = true;
      this.getGrid(row.gridId);
    },

    subjectClassificationFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业主体分类"]);
    },
    superviseClassificationFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["监管级别"]);
    },
    statusFormatter(cellValue) {
      return parseValueToText(cellValue, this.statusOptions);
    },
    verifyStatusFormatter(cellValue){
      if(cellValue==1){
        return "变更中";
      }else{
        return "未变更";
      }
    },
    timeFormatter(datetime, format) {
      return parseTime(datetime, format);
    },
    resetQuery() {
      let page = this.listQuery.page;
      let limit = this.listQuery.limit;
      this.listQuery = {
        page: page,
        limit: limit,
        enterpriseName: undefined,
        subjectClassification: undefined,
        superviseClassification: undefined,
        enterpriseStatus: "1"
      };
    },
    getSelection(selection) {
      this.inspectionPlanSelection = selection;
    },
    closeDialog() {
      this.resetTmp();
      this.tabPosition = "0";
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    resetTmp() {
      this.result = {
        enterpriseId: undefined,
        regulatoryLevel: undefined,
        gridId: undefined
      };
      this.templateId = undefined;
      this.$nextTick(function() {
        this.$refs["form"].clearValidate();
      });
    },
    handleIconClick(){

    }
  }
};
</script>
<style scoped>
.map_container {
  position: relative;
  width: 100%;
  height: 400px;
}
</style>
