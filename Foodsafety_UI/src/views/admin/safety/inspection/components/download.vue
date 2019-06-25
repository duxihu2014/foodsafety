<template>
    <div >
      <div class="download-container">
          <div class="download-title">计划信息</div>
          <div>
            <el-form class="planForm" :model="planForm">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="应检查日期:" prop="inspectionDate">
                    {{ timeFormatter(planForm.inspectionDate, "{y}-{m}-{d}") }}
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="企业名称:" prop="enterpriseName">{{planForm.enterpriseName}}</el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="负责人:" prop="corporateRepresentative">{{planForm.corporateRepresentative}}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="主体分类:" prop="subjectClassification">{{subjectClassificationFormatter(planForm.subjectClassification)}}</el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="监管级别:" prop="superviseClassification">{{superviseClassificationFormatter(planForm.superviseClassification)}}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="街道(乡镇):" prop="townStreet">{{planForm.townStreet}}</el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="居委会(村):" prop="committeeVillage">{{planForm.committeeVillage}}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col >
                  <el-form-item label="注册地址:" prop="registerAddress">{{planForm.registerAddress}}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col >
                  <el-form-item label="生产地址:" prop="productionAddress">{{planForm.productionAddress}}</el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="监管员:" prop="monitors">{{planForm.monitorsName}}</el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="协管员:" prop="assistants">{{planForm.assistantsName}}</el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="检验员:" prop="standards">{{planForm.standardsName}}</el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </div>
      <div class="download-container" v-if="resultShow">
        <div class="download-title">检查信息</div>
        <div>
          <el-form :model="resultForm">
            <el-row>
              <el-col :span="12">
                <el-form-item label="检查机构:" prop="inspectedInstitutionName">{{ resultForm.inspectedInstitutionName }}</el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检验人员:" prop="inspectedUserName">{{resultForm.inspectedUserName}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="检查时间:" prop="inspectedTime">{{timeFormatter(resultForm.inspectedTime, "{y}-{m}-{d}")}}</el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检查模板:" prop="templateId">{{templateFormatter(resultForm.templateId)}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="检查结果:" prop="templateId">
                  <div class="result-table">
                    <el-table :data="list"
                              :border="true" style="margin-bottom: 5px;"
                              header-cell-class-name="result-table-header"
                              row-class-name="result-table-row">
                      <el-table-column width="50"  align="center" label="序号" prop="inspectionDate">
                        <template slot-scope="scope">
                          {{scope.$index+1}}
                        </template>
                      </el-table-column>
                      <el-table-column width="70" align="center" label="重要程度" prop="importanceDegree" :formatter="importanceDegreeFormatter"></el-table-column>
                      <el-table-column width="100"  align="center" label="检查项目" prop="projectName"></el-table-column>
                      <el-table-column   align="center" label="检查内容" prop="projectContent" ></el-table-column>
                      <el-table-column  width="100" align="center" label="检查方法" prop="inspectionMethod" ></el-table-column>
                      <el-table-column  align="center" label="判定标准" prop="criterion"></el-table-column>
                      <el-table-column width="100" align="center" label="结果" prop="qualifiedAnswer" :formatter="qualifiedFormatter"></el-table-column>
                      <el-table-column align="center" label="不合格情况" prop="unqualifiedAnswer" :formatter="unqualifiedFormatter"></el-table-column>
                      <el-table-column align="center" label="整改情况" v-if="resultForm.revisitPlanId" :formatter="rectifiedFormatter"></el-table-column>
                    </el-table>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col >
                <el-form-item label="主要问题:" prop="mainProblems" >{{resultForm.mainProblems}}</el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="处理措施:" prop="treatmentMeasures" > {{treatmentMeasuresFormatter(resultForm.treatmentMeasures)}}</el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="回访时间:" prop="revisitDate" >{{resultForm.revisitDate?timeFormatter(resultForm.revisitDate, "{y}-{m}-{d}"):resultForm.revisitDate}}</el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { all } from "api/admin/safety/inspection/template/index";
  import {getInspectionPlan } from "api/admin/safety/inspection/index";
  import { getInspectionResult, getInspectionResultItem } from "api/admin/safety/inspection/result";
  import { getTemplateAllProject } from "api/admin/safety/inspection/template/project";
  import { parseValueToText, parseTime } from "utils/index";
    export default {
        name: "download",
        props:{
          planId:{
            default: undefined
          }
        },
      data(){
          return{
            planForm:{
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
            resultForm: {
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
            templateOptions:[],
            resultShow:false,//检查结果内容是否显示
            list: [],
            listLoading:false,
          }
      },
      computed: {
        ...mapGetters(["staticData"]),
      },
      created(){
        this.getPlanFormData();
        this.getResultData();
        this.getInspectionTempate();
      },
      methods:{
          //获取所有模板数据
        getInspectionTempate() {
          all({applicationScope:1, templateStatus:1}).then(response => {
            this.templateOptions = response;
          });
        },
        //获取计划信息数据
          getPlanFormData(){
            getInspectionPlan(this.planId).then(response => {
              this.planForm = response.data;
            });
          },
        //获取检查信息数据
          getResultData(){
            getInspectionResult({planId:this.planId}).then(response => {
              if (response.rel) {
                this.list=[];
                this.resultForm = response.data;
                this.resultShow = true;
                // 查询检查记录明细
                getInspectionResultItem({resultId:this.resultForm.resultId}).then(response => {
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
                      this.resultList.push(dataItem);
                    });
                    //获取检查模板数据
                    getTemplateAllProject({templateId:this.resultForm.templateId, projectStatus:1}).then(response => {
                      response.forEach(item => {
                        let nObj = Object.assign({}, item, {
                          isRectified: undefined,
                          phoneRectified: [],
                          qualifiedAnswer: "",
                          unqualifiedAnswer: [],
                          resultsPhoto: []
                        });
                        this.list.push(nObj);
                      });
                      if (this.resultList.length > 0) {
                        this.list.forEach(item => {
                          this.resultList.forEach(result => {
                            if (item.projectId === result.projectId) {
                              item.itemId = result.itemId;
                              item.isRectified = result.isRectified
                              if(result.phoneRectified.length > 0)
                                item.phoneRectified.push({id: result.phoneRectified});
                              item.qualifiedAnswer = result.resultsQualified;
                              item.unqualifiedAnswer = result.resultsUnqualified;
                              item.resultsPhoto.push({id: result.resultsPhoto});
                            }
                          });
                        });
                      }
                    })
                  } else {
                    this.$message({
                      type:'error',
                      showClose: true,
                      message: `查询检查记录失败！`
                    });
                  }
                });
              } else {
                this.resultShow = false;
              }
            });
          },

        timeFormatter(datetime, format) {
          return parseTime(datetime, format);
        },
        subjectClassificationFormatter(value) {
          return parseValueToText(value, this.staticData["企业主体分类"]);
        },
        superviseClassificationFormatter(value) {
          return parseValueToText(value, this.staticData["监管级别"]);
        },
        importanceDegreeFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.staticData["重要程度"]);
        },
        templateFormatter(value){
          let result = "";
          this.templateOptions.forEach(item=>{
            if(item.templateId==value){
              result = item.templateName;
            }
          })
          return result;
        },
        rectifiedFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["整改情况"]);
        },
        treatmentMeasuresFormatter(cellValue){
          return parseValueToText(cellValue, this.staticData["处理措施"]);
        },
        qualifiedFormatter(row, column, cellValue){
          let result ="";
          if(cellValue){
            result=row.resultsQualified.split(' ')[cellValue]
          }else{
            result="无"
          }
          return result;
        },
        unqualifiedFormatter(row, column, cellValue){
          let result ="";
          if(cellValue){
            cellValue.forEach(iten=>{
              result+=row.resultsUnqualified.split(' ')[iten]+";";
            })

          }else{
            result="无"
          }
          var reg=/;$/gi;
          result=result.replace(reg,"");
          return result;
        }
      },
    }
</script>

<style >
  .download-container{
    margin: 0px 20px 10px 20px;
  }
  .download-title{
    font-size: 15px;
    color: #1f2d3d;
    padding: 10px 1px;
    border-bottom: 1px #8c9393 solid;
  }
  .download-container .el-form-item{
    margin-bottom: -7px;
  }
  .result-table-header{
    font-size: 12px;
  }
  .result-table-row{
    font-size: 12px;
  }
  .result-table-header.is-leaf{
    border-right: 1px solid #324157 !important;
    border-bottom:1px solid #324157 !important;
  }
  .result-table .el-table--border td {
    border-right: 1px solid #324157;
  }
  .result-table .el-table td{
    border-bottom: 1px solid #324157;
  }
  .result-table .el-table--border, .el-table--group {
     border: 1px solid #324157;
   }

</style>
