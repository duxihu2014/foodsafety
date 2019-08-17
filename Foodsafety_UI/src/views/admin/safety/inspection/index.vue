<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="企业名称" class="filter-item">
            <!--<el-select  v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>-->
            <el-input @keyup.enter.native="handleFilter" placeholder="" :clearable="true" v-model.trim="listQuery.enterpriseName_like"> </el-input>
          </el-form-item>
          <el-form-item label="监管级别" class="filter-item">
            <el-select  v-model="listQuery.superviseClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.status" placeholder="请选择" clearable filterable>
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
      <el-table-column width="180px" align="center" label="应检查日期" prop="inspectionDate">
        <template slot-scope="scope">
          <span>{{ timeFormatter(scope.row.inspectionDate, "{y}-{m}-{d}") }}</span>
          <el-tag :type="getInspectionDateType(scope.row.inspectionStatus)" prop="inspectionDate">
            {{statusFormatter(scope.row.inspectionStatus)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column width="250px" align="center" label="企业名称" prop="enterpriseName" show-overflow-tooltip>

      </el-table-column>
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

      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDeal(scope.row)">执行
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDeal(scope.row)">执行
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  title="日常检查" fullscreen :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <i class="el-icon-document">日常检查</i>
      </div>
      <el-form :model="result" ref="form" :rules="rules" label-width="100px">
        <div style="height: 79vh;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="检查信息">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="检查时间" prop="inspectedTime" ref="inspectedTime" tab="0">
                    <el-date-picker v-model="result.inspectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" clearable :picker-options="pickerOptions1"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="检查模板" prop="templateId" ref="templateId" tab="0">
                    <el-select  v-model="result.templateId" placeholder="请选择" @change="templateChange" clearable filterabler>
                      <el-option v-for="(item, index) in templateOptions" :key="item.templateId" :label="item.templateName" :value="item.templateId"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <template-project :height="height" :templateId="templateId" ref="template"></template-project>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="23">
                  <el-form-item label="主要问题" placeholder="" prop="mainProblems" ref="mainProblems">
                    <el-input type="textarea"  :rows="2"  v-model.trim="result.mainProblems"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="处理措施" prop="treatmentMeasures">
                    <el-select  v-model="result.treatmentMeasures" placeholder="请选择" clearable filterabler>
                      <el-option v-for="(item, index) in treatmentMeasuresOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="回访日期" prop="revisitDate">
                    <el-date-picker v-model="result.revisitDate" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" clearable :picker-options="pickerOptions2" @change="revisitDateChange"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="计划信息">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="应检查日期" prop="inspectionDate">
                    <!--<el-date-picker v-model="form.inspectionDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" disabled clearable :picker-options="pickerOptions1"></el-date-picker>-->
                    <el-tag type="info" prop="inspectionDate">
                      {{ timeFormatter(form.inspectionDate, "{y}-{m}-{d}") }}
                    </el-tag>
                    <el-tag :type="getInspectionDateType(form.inspectionStatus)" prop="inspectionStatus">
                      {{statusFormatter(form.inspectionStatus)}}
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
          </el-tabs>
        </div>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="inspectionPlan_deal">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="create()">确 定</el-button>
      </div>

    </el-dialog>

  </div>
</template>

<script>
  import { page, getInspectionPlan } from "api/admin/safety/inspection/index";
  import { addObj } from "api/admin/safety/inspection/result";
  import { all } from "api/admin/safety/inspection/template/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import { parseValueToText, parseTime, parseForm } from "utils/index";
  export default {
    name: "inspectionPlan",
    components: {
      "template-project": () => import("./components/project"),
    },
    props: {
      tabName:{
        default: undefined
      }
    },
    watch:{
      total(val){
        this.$emit('setCount',val,1);
      }
    },
    data() {
      return {
        height: undefined,
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
          inspectionStatus: undefined
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
        projectResult: [],
        rules: {
          inspectedTime: [{required: true, message: "请选择检查时间", trigger: "change"}],
          templateId: [{required: true, message: "请选择检查模板", trigger: "change"}],
          mainProblems: [
            { required: false, message: "请输入主要问题", trigger: "blur" },
            { min: 1, max: 256, message: "长度在 1 到 256 个字符", trigger: "blur" }
          ]
        },
        list: null,
        projectList: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          enterpriseName_like: undefined,
          subjectClassification: undefined,
          superviseClassification: undefined,
          status: undefined
        },
        inspectionPlan_deal: false,
        dialogFormVisible: false,
        tableKey: 0,
        inspectionPlanSelection: [],
        statusArray: [{value:'1', text:'本日'}, {value:'2', text:'已超时'}, {value:'3', text:'未超时'}],
        templateOptions: [],
        needFixedRight:false,
        templateId: undefined,
        tabPosition: "0",
        pickerOptions1: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
        },
        pickerOptions2: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        }
      };
    },
    mounted () {
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function () {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);

        this.height = loadGridHeight(this.tabName);
      });
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);

        this.height = loadGridHeight(this.tabName);
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
        return this.statusArray;
      }
    },
    methods: {
      revisitDateChange(value) {
        if (value) {
          this.rules.mainProblems[0].required = true;
        } else {
          this.rules.mainProblems[0].required = false;
          this.$refs['form'].clearValidate();
        }
      },
      templateChange(value) {
        if (value) {
          this.templateId = value;
        }
      },
      getInspectionTempate() {
        all({applicationScope:1, templateStatus:1}).then(response => {
          this.templateOptions = response;
        });
      },
      getInspectionDateType(inspectionStatus){
        switch (inspectionStatus){
          case '1':
            return 'warning';
          case '2':
            return 'danger';
          case '3':
            return 'success';
        }
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
      handleDeal(row) {
        getInspectionPlan(row.planId).then(response => {
          this.form = response.data;
        });
        this.inspectionPlan_deal = true;
        this.dialogFormVisible = true;
      },
      create() {
        const set = this.$refs;
        set["form"].validate((valid, errors) => {
          if (valid) {
            this.projectResult = this.$refs.template.getProjectResult();
            //console.log(this.projectResult);
            let flag = false;
            let photoResultData = [];
            this.projectResult.forEach(project => {
              if(project.resultsQualified == undefined /*|| project.resultsUnqualified.length === 0*/
                || project.resultsPhoto.length === 0){
                flag = true;
                return false;
              }
              photoResultData.push(project.resultsPhoto[0].raw);
            });
            if (this.projectResult.length === 0 || flag) {
              this.$message({
                showClose: true,
                message: '请完成项目的检查信息'
              });
              this.tabPosition = "0";
              return false;
            }
            this.result.planId = this.form.planId;

            let param = new FormData();
            param.append('resultData', parseForm(this.result));
            param.append('projectResultData', parseForm(this.projectResult));
            // 多个图片必须往同一个参数里追加，不能直接传数组，后台会接收不到。
            // param.append('photoResultData', photoResultData));
            photoResultData.forEach(photo => {
              param.append('photoResultData', photo);
            });

            addObj(param).then(() => {
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
            for (var key in errors) {
              this.tabPosition = this.$refs[key].$attrs['tab'];
              break;
            }
            ;
            return false;
          }
        });
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
          enterpriseName_like: undefined,
          subjectClassification: undefined,
          superviseClassification: undefined,
          status: undefined};
      },
      getSelection(selection) {
        this.inspectionPlanSelection = selection;
      },
      closeDialog(){
        this.resetTmp();
        this.tabPosition='0';
      },
      cancel() {
        this.dialogFormVisible = false;
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
        this.projectResult = [];
        this.$nextTick(function () {
          this.$refs['form'].clearValidate();
        });
      }
    }
  };
</script>
