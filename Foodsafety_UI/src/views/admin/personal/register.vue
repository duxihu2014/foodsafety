<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item label="主体分类" class="filter-item">
            <el-select  v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="监管级别" class="filter-item">
            <el-select  v-model="listQuery.superviseClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>-->
          <el-form-item label="核查状态" class="filter-item">
            <el-select  v-model="listQuery.enterpriseStatus" placeholder="请选择" clearable filterable>
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>

    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
              @selection-change="getSelection" :height="height">
      <!--<el-table-column  type="selection"  width="55">
      </el-table-column>-->
      <!--<el-table-column align="center" label="企业编号" width="100" prop="enterpriseId"></el-table-column>-->
      <el-table-column align="center" label="" width="100px">
        <template slot-scope="scope">
          <el-tag :type="getEnterpriseStatus(scope.row.enterpriseStatus)" prop="inspectionDate">
            {{statusFormatter(scope.row.enterpriseStatus)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="企业名称" width="250px" prop="enterpriseName" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column align="center" label="所属区域" width="120px" prop="areaName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180px" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <!--<el-table-column align="center" label="营业执照编号" width="200px" prop="registerCertificate.certificateNo" :show-overflow-tooltip="true"></el-table-column>-->
      <el-table-column align="center" label="法人代表" width="150px" prop="corporateRepresentative" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册地址" width="300px" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="生产地址" width="300px" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="主体分类" width="200px" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="联系人" width="100" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <!--<el-table-column align="center" label="企业状态" width="100" prop="enterpriseStatus" :formatter="statusFormatter" :show-overflow-tooltip="true"></el-table-column>-->
      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog title="现场核查" top="3vh" fullscreen :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <i class="el-icon-document">现场核查</i>
      </div>
      <el-form :model="result" ref="form" label-width="100px">
        <div style="height: 79vh;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="企业信息">
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
              <el-row >
                <el-col :span="11">
                  <el-form-item label="法人代表" prop="corporateRepresentative">
                    <el-input v-model="form.corporateRepresentative" placeholder="法人代表" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="身份证号" prop="idCardNo">
                    <el-input v-model="form.idCardNo" placeholder="身份证号" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="主体分类" prop="subjectClassification">
                    <el-select  v-model="form.subjectClassification" placeholder="请选择" :disabled="true">
                      <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11" >
                  <el-form-item label="所属区域" prop="areaOptions">
                    <el-input v-model.trim="form.areaName" placeholder="" disabled clearable></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="注册资金" prop="registeredCapital">
                    <el-input v-model="form.registeredCapital" placeholder="注册资金" :disabled="true">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="经济性质" prop="economicNature">
                    <el-select  v-model="form.economicNature" placeholder="请选择"  :disabled="true">
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
              <el-row >
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
              <el-row >
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
              <el-row >
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

              <!--<el-row >
                <el-col :span="22">
                  <el-form-item label="经营范围" prop="operationScope">
                    <el-input type="textarea" :rows="5" v-model="form.operationScope" placeholder="经营范围" :disabled="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>-->
            </el-tab-pane>
            <el-tab-pane label="核查信息" v-if="onsiteResultShow">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="核查时间" prop="verificateTime" tab="0">
                    <el-date-picker v-model="result.verificateTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" clearable></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="核查模板" prop="templateId" tab="0">
                    <el-select  v-model="result.templateId" placeholder="请选择" @change="templateChange" clearable filterabler>
                      <el-option v-for="(item, index) in templateOptions" :key="item.templateId" :label="item.templateName" :value="item.templateId"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <template-project :height="height" :templateId="templateId" :existResult="onsiteResultShow" :setProjectResult="setProjectResult" ref="template"></template-project>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="23">
                  <el-form-item label="主要问题" placeholder="" prop="mainProblems">
                    <el-input type="textarea"  :rows="2"  v-model.trim="result.mainProblems"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="核查结果" prop="verificateConclusion">
                    <el-select  v-model="result.verificateConclusion" placeholder="请选择" clearable filterabler>
                      <el-option v-for="(item, index) in treatmentMeasuresOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <!--<el-col :span="12">
                  <el-form-item label="回访日期" prop="revisitDate">
                    <el-date-picker v-model="result.revisitDate" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" clearable></el-date-picker>
                  </el-form-item>
                </el-col>-->
              </el-row>
            </el-tab-pane>

          </el-tabs>
        </div>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
  import { getEnterprisePage } from "api/admin/enterprise/index";
  import { getOnsiteResult, getOnsiteResultItem } from "api/admin/enterprise/onsite/result";
  import { all } from "api/admin/safety/inspection/template/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import { parseValueToText, parseTime } from "utils/index";
  import queryConditions from "components/QueryConditions/index";
  export default {
    name: "onsite",
    components: {
      "template-project": () => import("../safety/inspection/components/project"),
      queryConditions
    },
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
          areaName: undefined,
          enterpriseStatus: undefined
        },
        result: {
          resultId: undefined,
          enterpriseId: undefined,
          verificateTime: undefined,
          verificateInstitution: undefined,
          verificateInstitutionName: undefined,
          verificateUser: undefined,
          verificateUserName: undefined,
          templateId: undefined,
          templateName: undefined,
          mainProblems: undefined,
          verificateConclusion: undefined,
          revisitDate: undefined,
          revisitPlanId: undefined
        },
        resultList : [],
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          subjectClassification: undefined,
          superviseClassification: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined,
          someStatus: "0,1,2,3,4"
        },
        dialogFormVisible: false,
        onsiteResultShow: false,
        tableKey: 0,
        onsiteSelect: [],
        statusArray: [{value:'0', text:'待核查'}, {value:'1', text:'核查通过'}, {value:'2', text:'核查未通过'}],
        templateOptions: [],
        needFixedRight:false,
        templateId: undefined,
        tabPosition: "0"
      };
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
      };
    },
    updated () {
      this.height=loadGridHeight();
    },
    created() {
      this.getTempate();
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData", "user"]),
      subjectClassificationOptions() {
        return this.staticData["企业主体分类"];
      },
      economicNatureOptions(){
        return this.staticData["企业经济性质"];
      },
      superviseClassificationOptions() {
        return this.staticData["监管级别"];
      },
      treatmentMeasuresOptions() {
        return this.staticData["核查结论"];
      },
      statusOptions(){
        return this.statusArray;
      }
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
      templateChange(value){
        if (value) {
          this.templateId = value;
        }
      },
      setProjectResult(){
        return this.resultList;
      },
      getTempate() {
        all({applicationScope:4, templateStatus:1}).then(response => {
          this.templateOptions = response;
        });
      },
      getEnterpriseStatus(enterpriseStatus){
        switch (enterpriseStatus){
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
        if (this.listQuery.areaId == undefined) {
          this.listQuery.areaId = this.user.areaId;
        }
        getEnterprisePage(this.listQuery).then(response => {
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
      handleView(row) {
        // 查询检查计划详情
        this.form = row;
        // 查询检查记录
        getOnsiteResult({enterpriseId:row.enterpriseId}).then(response => {
          if (response.rel) {
            // debugger
            this.result = response.data;
            this.onsiteResultShow = true;
            // 查询检查记录明细
            getOnsiteResultItem({resultId:this.result.resultId}).then(response => {
              if (response.rel) {
                // debugger
                let data = response.data;
                data.forEach(item => {
                  let resultsUnqualified = [];
                  if (item.resultsUnqualified) {
                    let arr = item.resultsUnqualified.split(' ');
                    arr.forEach(unqualified => {
                      resultsUnqualified.push(parseInt(unqualified));
                    });
                  }
                  let dataItem = {projectId: item.projectId, isRectified: item.isRectified, phoneRectified: item.rectifiedResourceId ? [item.rectifiedResourceId] : [], resultsQualified: item.resultsQualified, resultsUnqualified: resultsUnqualified, resultsPhoto: [item.resourceId]};
                  //console.log(dataItem);
                  this.resultList.push(dataItem);
                });
                // 一定要等检查记录明细查询完毕后，才能触发模板的change事件。
                this.templateChange(this.result.templateId);
              } else {
                this.$message({
                  type:'error',
                  showClose: true,
                  message: `查询检查记录失败！`
                });
              }
            });
          } else {
            this.onsiteResultShow = false;
          }
        });
        this.dialogFormVisible = true;
      },
      subjectClassificationFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["企业主体分类"]);
      },
      superviseClassificationFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["监管级别"]);
      },
      statusFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData["企业状态"]);
      },
      timeFormatter(datetime, format) {
        return parseTime(datetime, format);
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit,
          subjectClassification: undefined,
          superviseClassification: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined,
          someStatus: "0,1,2,3,4"};
      },
      getSelection(selection) {
        this.onsiteSelect = selection;
      },
      closeDialog(){
        this.resetTmp();
        this.tabPosition='0';
      },
      resetTmp() {
        this.result = {
          resultId: undefined,
          enterpriseId: undefined,
          verificateTime: undefined,
          verificateInstitution: undefined,
          verificateInstitutionName: undefined,
          verificateUser: undefined,
          verificateUserName: undefined,
          templateId: undefined,
          templateName: undefined,
          mainProblems: undefined,
          verificateConclusion: undefined,
          revisitDate: undefined,
          revisitPlanId: undefined};
        this.templateId = undefined;
        this.$nextTick(function () {
          this.$refs['form'].clearValidate();
        });
      }
    }
  };
</script>
