<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item label="监管级别" class="filter-item">
            <el-select  v-model="listQuery.superviseClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="主体分类" class="filter-item">
            <el-select  v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="企业状态" class="filter-item">
            <el-select  v-model="listQuery.enterpriseStatus" placeholder="请选择" clearable filterabler>
              <!-- <el-option v-for="(item, index) in testData" :key="item.value" :label="item.text" :value="item.value"></el-option> -->
              <el-option v-for="(item, index) in enterpriseStatusData" :key="item.value" :label="item.text" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="营业执照" prop="certificateNo" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.certificateNoLike"> </el-input>
          </el-form-item>-->
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">

      <el-table-column align="center" label="监管等级" width="100" prop="superviseClassification"  fixed="left">
        <template slot-scope="scope">
          <el-tag >
            {{superviseClassificationFormatter(scope.row.superviseClassification)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="主体分类" width="180" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="联系人" width="100" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册地址" width="300" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="生产地址" width="300" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="营业执照编号" width="180" prop="certificateNo" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="企业状态" width="100" prop="enterpriseStatus" :formatter="enterpriseStatusFormatter"></el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog  title="档案详情" :visible.sync="dialogVisible" fullscreen :close-on-press-escape="false" @close="closeDialog" :closeOnClickModal="false">
      <div slot="title">
        <i class="el-icon-document">档案详情</i>
      </div>
      <el-tabs v-model="tabPosition">
        <el-tab-pane label="基本信息">
          <baseDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></baseDetail>
        </el-tab-pane>
        <el-tab-pane label="企业规模">
          <scaleDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></scaleDetail>
        </el-tab-pane>
        <el-tab-pane label="证照信息">
          <certificateDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></certificateDetail>
        </el-tab-pane>
        <el-tab-pane label="产品信息">
          <productDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></productDetail>
        </el-tab-pane>
        <el-tab-pane label="原料信息">
          <materialDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></materialDetail>
        </el-tab-pane>
        <el-tab-pane label="设备信息">
          <equipmentDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></equipmentDetail>
        </el-tab-pane>
        <el-tab-pane label="合作伙伴">
          <cooperativePartner :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></cooperativePartner>
        </el-tab-pane>
        <el-tab-pane label="餐饮服务许可证">
          <productLicense :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></productLicense>
        </el-tab-pane>
        <el-tab-pane label="员工健康证">
          <healthLicense :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></healthLicense>
        </el-tab-pane>
        <el-tab-pane label="现场核查">
          <onsiteCheck :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></onsiteCheck>
        </el-tab-pane>
        <el-tab-pane label="日常检查">
          <dailyCheck :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></dailyCheck>
        </el-tab-pane>
        <el-tab-pane label="回访情况">
          <revisitResult :enterpriseId="enterpriseId" :enterpriseName="enterpriseName" v-if="dialogVisible"></revisitResult>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>
<script>
import { getEnterpriseAllInfo, getResource } from "api/admin/enterprise/index";
import queryConditions from "components/QueryConditions/index";
import { mapGetters } from "vuex";
import { parseValueToText, parseTime, findParentNode } from "utils/index";
import { loadGridHeight } from "api/screen";


export default {
  name: "verify",
  components: {
    queryConditions,
    "baseDetail": () => import("./components/baseDetail"),
    "certificateDetail": () => import("./components/certificateDetail"),
    "scaleDetail": () => import("./components/scaleDetail"),
    "equipmentDetail": () => import("./components/equipmentDetail"),
    "productDetail": () => import("./components/productDetail"),
    "materialDetail": () => import("./components/materialDetail"),
    "cooperativePartner": () => import("./components/cooperativePartner"),
    "productLicense": () => import("./components/productLicense"),
    "healthLicense": () => import("./components/healthLicense"),
    "onsiteCheck": () => import("./components/onsiteCheck"),
    "dailyCheck": () => import("./components/dailyCheck"),
    "revisitResult": () => import("./components/revisitResult"),
  },
  data() {
    return {
      enterpriseId: '',
      enterpriseName: '',
      tabPosition: 0,
      height: undefined,
      serverImageUrl: process.env.SERVERIMAGEURL,
      imgFile: [],
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      area: null,
      areaOptions: [],
      list: null,
      total: null,
      listLoading: true,
      initStatus:'3', //企业状态默认值
      listQuery: {
        enterpriseStatus:'3',//新增企业状态
        superviseClassification: undefined,
        subjectClassification: undefined,
        certificateNoLike: undefined,
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined,
        page: 1,
        limit: 20,
      },
      tableKey: 0,
      enterpriseSelection: [],
      form: {
        registerBase: {},
        registerCertificate: {},
      },
      dialogVisible: false,
      dialogTitle: "注册企业审核",
      activeName: "first",

    }
  },

  computed: {
    ...mapGetters(["user", "staticData"]),
    subjectClassificationOptions() {
      return this.staticData["企业主体分类"];
    },
    superviseClassificationOptions() {
      return this.staticData["监管级别"];
    },
    enterpriseStatusData(){
      return this.staticData["企业状态"];

    }
    /*economicNatureOptions() {
      return this.staticData["企业经济性质"];
    }*/
  },
  updated () {
    this.height = loadGridHeight();
  },
  created() {
    this.getList();
  },
  methods: {
    setAreaId(data){
      this.listQuery.areaId = data;
    },
    setGridId(data){
      this.listQuery.gridId = data;
    },
    setEnterpriseId(data){
      this.listQuery.enterpriseId = data;
    },
    getList() {
      if (this.listQuery.areaId == undefined)
        this.listQuery.areaId = this.user.areaId;
      getEnterpriseAllInfo(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },

    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    resetQuery() {
      let page = this.listQuery.page;
      let limit = this.listQuery.limit;
      this.listQuery = { page: page, limit: limit,
        enterpriseStatus:'3',
        superviseClassification: undefined,
        subjectClassification: undefined,
        certificateNoLike: undefined,
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined };
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    getSelection(selection) {
      this.enterpriseSelection = selection;
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.compStatus === '0') {
        return 'warning-row';
      } else if (row.compStatus === '1') {
        return 'success-row';
      }
      return '';
    },
    //审核查看
    handleView(row) {
      // console.log(265,row);
      
      this.dialogVisible = true;
      this.enterpriseId = row.enterpriseId;
      this.enterpriseName = row.enterpriseName;
    },

    //取消提交
    cancel() {
      this.auditDialogVisible = false;
      this.mapDialogVisible = false;
    },

    closeDialog() {
      this.tabPosition='0';
    },
    //企业主体分类格式化
    subjectClassificationFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业主体分类"]);
    },
    registerStatusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业注册状态"]);
    },
    enterpriseStatusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['企业状态']);
    },
    superviseClassificationFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData["监管级别"]);
    },
  }
}

</script>
<style>
.enterpriseDialog>.el-dialog--center .el-dialog__body {
  text-align: initial;
  padding: 10px 25px 30px;
}

.map_container {
  position: relative;
  width: 100%;
  height: 400px;
}

</style>
