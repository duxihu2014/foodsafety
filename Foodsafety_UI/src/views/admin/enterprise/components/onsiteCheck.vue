<template>
  <div>
    <div v-if="dataFlag">
      <el-form :model="result" ref="form" label-width="100px">
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
            <template-project height="30vh" :templateId="templateId" :existResult="onsiteResultShow" :setProjectResult="setProjectResult" ref="template"></template-project>
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

      </el-form>

    </div>
    <div v-if="!dataFlag">
      无现场核查数据
    </div>
  </div>

</template>

<script>
  import { getEnterprisePage } from "api/admin/enterprise/index";
  import { getOnsiteResult, getOnsiteResultItem } from "api/admin/enterprise/onsite/result";
  import { all } from "api/admin/safety/inspection/template/index";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  import {getOnsiteTask} from 'api/admin/dashboard/index';
  export default {
    name: "onsite-check",
    components: {
      "template-project": () => import("views/admin/safety/inspection/components/project"),
    },
    props: ["enterpriseId"],
    watch: {
      'enterpriseId': function() {
        this.getList();
      }
    },
    data() {
      return {
        dataFlag:true,
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
        getOnsiteResult({enterpriseId:this.enterpriseId}).then(response => {
          if (response.rel) {
            this.dataFlag=true;
            this.result = response.data;
            this.onsiteResultShow = true;
            // 查询检查记录明细
            getOnsiteResultItem({resultId:this.result.resultId}).then(response => {
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
            this.dataFlag=false;
            this.onsiteResultShow = false;
          }
        });
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
