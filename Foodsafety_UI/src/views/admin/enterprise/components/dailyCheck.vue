<template>
  <div>
    <div v-if="dataFlag">
      <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
                border fit highlight-current-row style="width: 100%" highlight-current-row
                @selection-change="getSelection" height="75vh">
        <!--<el-table-column  type="selection"  width="55">
        </el-table-column>-->
        <el-table-column width="180px" align="center" label="应检查日期" prop="inspectionDate">
          <template slot-scope="scope">
            {{ timeFormatter(scope.row.inspectionDate, "{y}-{m}-{d}") }}
            <el-tag :type="getPlanStatusType(scope.row.planStatus)" prop="planStatus">
              {{ statusFormatter(scope.row.planStatus)}}
            </el-tag>
          </template>
        </el-table-column>
  <!--      <el-table-column width="250px" align="center" label="企业名称" prop="enterpriseName">

        </el-table-column>-->
        <el-table-column width="120px" align="center" label="所属地区" prop="areaName">

        </el-table-column>
        <el-table-column width="300px" align="center" label="生产地址" prop="productionAddress" show-overflow-tooltip>

        </el-table-column>
        <el-table-column width="200px" align="center" label="主体分类" prop="subjectClassification" :formatter="subjectClassificationFormatter">

        </el-table-column>
        <el-table-column width="100px" align="center" label="监管级别" prop="superviseClassification" :formatter="superviseClassificationFormatter">

        </el-table-column>
        <el-table-column width="120px" align="center" label="责任区域" prop="gridName">

        </el-table-column>
        <el-table-column width="120px" align="center" label="街道(乡镇)" prop="townStreet">

        </el-table-column>
        <el-table-column width="120px" align="center" label="居委会(村)" prop="committeeVillage">

        </el-table-column>

<!--        <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>-->
        <el-table-column align="center" label="操作" width="100" >
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-show="!listLoading" class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
      </div>

      <el-dialog  title="日常检查" fullscreen :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false" append-to-body>
        <div slot="title">
          <i class="el-icon-document">日常检查</i>
        </div>
        <el-form :model="result" ref="form" label-width="100px">
          <div style="height: 79vh;">
            <el-tabs v-model="tabPosition">
              <el-tab-pane label="计划信息">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="应检查日期" prop="inspectionDate">
                      {{ timeFormatter(form.inspectionDate, "{y}-{m}-{d}") }}
                      <el-tag :type="getPlanStatusType(form.planStatus)" prop="planStatus">
                        {{ statusFormatter(form.planStatus)}}
                      </el-tag>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="企业名称" prop="enterpriseName">
                      <el-input v-model.trim="form.enterpriseName" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="负责人" prop="corporateRepresentative">
                      <el-input v-model.trim="form.corporateRepresentative" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="主体分类" prop="subjectClassification">
                      <el-select  v-model="form.subjectClassification" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="监管级别" prop="superviseClassification">
                      <el-select  v-model="form.superviseClassification" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="街道(乡镇)" prop="townStreet">
                      <el-input v-model.trim="form.townStreet" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="居委会(村)" prop="committeeVillage">
                      <el-input v-model.trim="form.committeeVillage" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="注册地址" prop="registerAddress">
                      <el-input v-model.trim="form.registerAddress" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="生产地址" prop="productionAddress">
                      <el-input v-model.trim="form.productionAddress" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="8">
                    <el-form-item label="监管员" prop="monitors">
                      <el-input v-model.trim="form.monitorsName" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="协管员" prop="assistants">
                      <el-input v-model.trim="form.assistantsName" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="检验员" prop="standards">
                      <el-input v-model.trim="form.standardsName" placeholder="" disabled clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="检查信息" v-if="inspectionResultShow">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检查机构" prop="inspectedInstitutionName">
                      {{ result.inspectedInstitutionName }}
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检查人员" prop="inspectedUserName">
                      {{ result.inspectedUserName }}
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检查时间" prop="inspectedTime">
                      <el-date-picker v-model="result.inspectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检查模板" prop="templateId">
                      <el-select  v-model="result.templateId" placeholder="请选择" @change="templateChange" disabled clearable filterabler>
                        <el-option v-for="(item, index) in templateOptions" :key="item.templateId" :label="item.templateName" :value="item.templateId"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <template-project height="30vh" :templateId="templateId" :existResult="inspectionResultShow" :setProjectResult="setProjectResult" ref="template"></template-project>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="23">
                    <el-form-item label="主要问题" placeholder="" prop="mainProblems">
                      <el-input type="textarea"  :rows="2"  v-model.trim="result.mainProblems" disabled></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="处理措施" prop="treatmentMeasures">
                      <el-select  v-model="result.treatmentMeasures" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in treatmentMeasuresOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访日期" prop="revisitDate">
                      <el-date-picker v-model="result.revisitDate" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-form>

      </el-dialog>

    </div>
    <div v-if="!dataFlag">
      无日常检查数据
    </div>
  </div>

</template>

<script>
  import { getInspectionPlan,pageQury } from "api/admin/safety/inspection/index";
  import { getInspectionResult, getInspectionResultItem } from "api/admin/safety/inspection/result";
  import { all } from "api/admin/safety/inspection/template/index";
  import { mapGetters } from "vuex";
  import { parseValueToText, parseTime } from "utils/index";
  export default {
    name: "daily-check",
    props: ["enterpriseId"],
    watch: {
      'enterpriseId': function() {
        this.getList();
      }
    },
    components: {
      "template-project": () => import("views/admin/safety/inspection/components/project"),
    },
    data() {
      return {
        dataFlag:true,
        form: {
          planId: undefined,
          areaId: undefined,
          gridId: undefined,
          townStreet: undefined,
          committeeVillage: undefined,
          monitors: undefined,
          monitorsName: undefined,
          assistants: undefined,
          assistantsName: undefined,
          standards: undefined,
          standardsName: undefined,
          enterpriseId: undefined,
          enterpriseName: undefined,
          corporateRepresentative: undefined,
          registerAddress: undefined,
          productionAddress: undefined,
          subjectClassification: undefined,
          superviseClassification: undefined,
          inspectionDate: undefined,
          inspectionStatus: undefined,
          planStatus: undefined
        },
        result: {
          resultId: undefined,
          planId: undefined,
          inspectedTime: undefined,
          inspectedInstitution: undefined,
          inspectedInstitutionName: undefined,
          inspectedUser: undefined,
          inspectedUserName: undefined,
          templateId: undefined,
          templateName: undefined,
          mainProblems: undefined,
          treatmentMeasures: undefined,
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
          planStatus: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined
        },
        dialogFormVisible: false,
        inspectionResultShow: false,
        tableKey: 0,
        inspectionPlanSelection: [],
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
    created() {
      this.getInspectionTempate();
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"]),
      subjectClassificationOptions() {
        return this.staticData["企业主体分类"];
      },
      superviseClassificationOptions() {
        return this.staticData["监管级别"];
      },
      treatmentMeasuresOptions() {
        return this.staticData["处理措施"];
      },
      statusOptions(){
        return this.staticData["工单状态"];
      }
    },
    methods: {
      templateChange(value){
        if (value) {
          this.templateId = value;
        }
      },
      setProjectResult(){
        return this.resultList;
      },
      getInspectionTempate() {
        all({applicationScope:1, templateStatus:1}).then(response => {
          this.templateOptions = response;
        });
      },
      getPlanStatusType(planStatus){
        switch (planStatus){
          case '0':
            return 'danger';
          case '1':
            return 'success';
        }
      },
      getList() {
        this.listLoading = true;
        this.listQuery.enterpriseId=this.enterpriseId;
        pageQury(this.listQuery).then(response => {
          if(response.total>0){
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
            this.dataFlag=true;
          }else{
            this.listLoading = false;
            this.dataFlag=false;
          }
        });
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
        getInspectionPlan(row.planId).then(response => {
          this.form = response.data;
        });
        // 查询检查记录
        getInspectionResult({planId:row.planId}).then(response => {
          if (response.rel) {
            this.result = response.data;
            this.inspectionResultShow = true;
            // 查询检查记录明细
            getInspectionResultItem({resultId:this.result.resultId}).then(response => {
              if (response.rel) {
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
            this.inspectionResultShow = false;
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
        return parseValueToText(cellValue, this.statusOptions);
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
          planStatus: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined};
      },
      getSelection(selection) {
        this.inspectionPlanSelection = selection;
      },
      closeDialog(){
        this.resetTmp();
        this.tabPosition='0';
      },
      resetTmp() {
        this.result = {
          resultId: undefined,
          planId: undefined,
          inspectedTime: undefined,
          inspectedInstitution: undefined,
          inspectedInstitutionName: undefined,
          inspectedUser: undefined,
          inspectedUserName: undefined,
          templateId: undefined,
          templateName: undefined,
          mainProblems: undefined,
          treatmentMeasures: undefined,
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
