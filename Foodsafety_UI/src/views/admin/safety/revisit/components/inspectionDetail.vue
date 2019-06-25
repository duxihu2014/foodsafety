<template>
  <div>
    <el-row>
      <el-col>
        <!-- 详情显示区域 -->
        <div>
          <!-- 基本信息显示区域 -->
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="回访计划" name="0">
              <el-form :model="resultPaln" :disabled="true" label-width="100px">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="企业名称" prop="enterpriseName">
                      <el-input v-model="resultPaln.enterpriseName"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访来源" prop="revisitSource">
                      <el-select v-model="resultPaln.revisitSource" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in revisitSourceOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="主体分类" prop="subjectClassification">
                      <el-select v-model="resultPaln.subjectClassification" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="监管级别" prop="superviseClassification">
                      <el-select v-model="resultPaln.superviseClassification" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="监督员" prop="monitors">
                      <el-select v-model="resultPaln.monitors+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="协管员" prop="assistants">
                      <el-select v-model="resultPaln.assistants+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="上次检查时间" prop="inspectedTime">
                      <el-date-picker v-model="resultPaln.inspectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="上次检查人员" prop="inspectedUser">
                      <el-select v-model="resultPaln.inspectedUser+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="上次检查机构" prop="inspectedInstitution">
                      <el-select v-model="resultPaln.inspectedInstitution+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in institutionMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访完成时间" prop="finishTime">
                      <el-date-picker v-model="resultPaln.finishTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检验员" prop="standards">
                      <el-select v-model="resultPaln.standards+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="主要问题:" prop="mainProblems">
                      <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 3}" v-model="resultPaln.mainProblems" disabled></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="回访情况" name="1" v-if="revisitResultShow">
              <el-form :model="resultForm" :disabled="true" label-width="100px">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="回访时间" prop="revisitTime">
                      <el-date-picker v-model="resultForm.revisitTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访人员" prop="revisitUser">
                      <el-select v-model="resultForm.revisitUser+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="回访机构" prop="revisitInstitution">
                      <el-select v-model="resultForm.revisitInstitution+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in institutionMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访场所" prop="revisitPlace">
                      <el-input v-model="resultForm.revisitPlace"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="在场人员" prop="enterprisePersonnel">
                      <el-input v-model="resultForm.enterprisePersonnel"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="人员职务" prop="enterpriseDuty">
                      <el-input v-model="resultForm.enterpriseDuty"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="处理措施" prop="treatmentMeasures">
                      <el-select v-model="resultForm.treatmentMeasures" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in treatmentOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="处理结论" prop="treatmentConclusion">
                      <el-select v-model="resultForm.treatmentConclusion" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in treatmentConclusionOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="日常检查" name="2" v-if="defaultShow">
              <el-form :model="basicInfoFrom" label-width="100px">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检查时间" prop="inspectedTime">
                      <el-date-picker v-model="basicInfoFrom.inspectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检查机构" prop="inspectedInstitution">
                      <el-select v-model="basicInfoFrom.inspectedInstitution+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in institutionMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检查人员" prop="inspectedUser">
                      <el-select v-model="basicInfoFrom.inspectedUser+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in personnelMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检查模板" prop="templateId">
                      <el-select v-model="basicInfoFrom.templateId" placeholder="请选择" @change="templateChange" disabled clearable filterabler>
                        <el-option v-for="(item, index) in templateMap" :key="item.templateId" :label="item.templateName" :value="item.templateId"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <template-project :height="height" :templateId="templateId" :existResult="true" :setProjectResult="setProjectResult" ref="template"></template-project>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="23">
                    <el-form-item label="主要问题" placeholder="" prop="mainProblems">
                      <el-input type="textarea" :rows="2" v-model.trim="basicInfoFrom.mainProblems" disabled></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="处理措施" prop="treatmentMeasures">
                      <el-select v-model="basicInfoFrom.treatmentMeasures+''" placeholder="请选择" disabled clearable filterabler>
                        <el-option v-for="(item, index) in treatmentOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="回访时间" prop="revisitDate">
                      <el-date-picker v-model="basicInfoFrom.revisitDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>
<script>
  import { getPersonnelMap, getInstitutionMap, getRevisitResultByPlan, getRevisitPlan } from "api/admin/safety/revisit/index";
  import { getObj, getInspectionResultItem } from "api/admin/safety/inspection/result";
  import { all } from "api/admin/safety/inspection/template/index";
  import { mapGetters } from 'vuex';
  import { parseValueToText } from 'utils/index';
  import { loadGridHeight } from "api/screen";
  import { getTemplateAllProject } from "api/admin/safety/inspection/template/project";
  import { getAllCategory } from "api/admin/safety/inspection/template/category";
  import { getResource } from "api/admin/resource/index";
  export default {
    components: {
      "template-project": () => import("../../../safety/inspection/components/project"),
    },
    name: 'inspectionDetail',
    data() {
      return {
        tabPosition: '0',
        height: undefined,
        templateId: undefined,
        result: [],
        submitBtnState: false,
        basicInfoFrom: {

        },
        resultForm: {

        },
        resultPaln: {
          enterpriseName: undefined,
          revisitSource: undefined,
          monitors: undefined

        },
        needFixedRight: false,
        resultItem: [],
        resultItemTmp: [],
        institutionMap: [],
        personnelMap: [],
        templateMap: [],
        tableKey: 0,
        list: null,
        resultList : [],
        revisitResultShow: false,
        defaultShow: false, // 为了日常检查在回访情况标签后加载
        dialogImageVisible: false,
        dialogImageUrl: '',
        serverImageUrl: process.env.SERVERIMAGEURL
      }
    },
    props: {
      revisitPlanId: {
        type: [Number, String],
        required: true
      }
    },
    mounted() {
      this.initSelect();
      this.getInstitutionMap();
      this.getPersonnelMap();
      this.getInspectionTempate();
      this.height = loadGridHeight() - 180;

    },
    watch: {
      revisitPlanId: {
        handler(val, oldval) {
          this.initSelect()
        }
      }
    },
    computed: {
      ...mapGetters(['staticData', 'user']),
      treatmentOptions() {
        return this.staticData['处理措施'];
      },
      subjectClassificationOptions() {
        return this.staticData['企业主体分类'];
      },
      superviseClassificationOptions() {
        return this.staticData['监管级别'];
      },
      treatmentConclusionOptions() {
        return this.staticData['回访处理结论'];
      },
      revisitSourceOptions() {
        return this.staticData['回访来源'];
      }
    },
    methods: {
      getInstitutionMap() {
        getInstitutionMap().then(response => {
          this.institutionMap = response['institution'];
        });
      },
      getPersonnelMap() {
        getPersonnelMap().then(response => {
          this.personnelMap = response['personnel'];
        });
      },
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
          this.templateMap = response;
        });
      },
      initSelect() {
        //回访计划
        getRevisitPlan(this.revisitPlanId).then(response => {
          this.resultPaln = response.data;
          //回访计划对应的的日常检查记录及模版
          getObj(this.resultPaln.resultId).then(response => {
            // debugger
            this.basicInfoFrom = response.data;
            this.defaultShow = true;
            getInspectionResultItem({ resultId: this.basicInfoFrom.resultId }).then(response => {
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
                this.templateChange(this.basicInfoFrom.templateId);
              } else {
                this.$message({
                  type:'error',
                  showClose: true,
                  message: `查询检查记录失败！`
                });
              }
            });
          });
        });
        //回访计划结果
        getRevisitResultByPlan(this.revisitPlanId).then(response => {
          if (response.data) {
            this.resultForm = response.data;
            this.revisitResultShow = true;
          }
        });
      },

      handlePhoto(id) {
        getResource(id).then(response => {
          if (response.rel) {
            this.dialogImageUrl = this.serverImageUrl + '/' + response.data.resourcePath;
            // console.log(this.dialogImageUrl)
            this.dialogImageVisible = true;
          } else {
            this.$message({
              type: 'error',
              showClose: true,
              message: `资源获取失败！`
            });
          }
        });
      },
      handleChange(file, fileList) {
        if (this.fileFormat.indexOf(file.raw.type) < 0) {
          this.$message({
            type: 'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.result[this.currentIndex].resultsPhoto.push(file);
        //console.log(this.result[0].resultsPhoto)
        // if(this.result[this.currentIndex].resultsPhoto.length>0){
        //   $(".el-table__row current-row .el-upload--text").hide();
        // }
      },
      institutionFormatter(cellValue) {
        return parseValueToText(cellValue, this.institutionMap);
      },
      personnelFormatter(cellValue) {
        return parseValueToText(cellValue, this.personnelMap);
      },
      templateIdFormatter(cellValue) {
        return parseValueToText(cellValue, this.templateMap);
      },
      treatmentFormatter(cellValue) {
        return parseValueToText(cellValue, this.treatmentOptions);
      },
      rectifiedFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData["整改情况"]);
      },
      revisitSourceFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData['回访来源']);
      },
      superviseClassFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData['监管级别']);
      },
      treatmentConclusionFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData['回访处理结论']);
      },
      subjectClassFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData['企业主体分类']);
      }
    }
  }

</script>
<style scoped>
  .head-container {
    position: relative;
    height: 3rem;
    padding-top: .7rem;
    border-top: 2px solid #dfe6ec;
    border-left: 2px solid #dfe6ec;
    border-right: 2px solid #dfe6ec;
    background-color: #fbfdff;
  }

  .head-right {
    position: absolute;
    right: 0;
    top: 0;
    padding: .7rem 1rem;
  }

  .detail-container {
    height: 82vh;
    border: 2px solid #dfe6ec;
  }

  .fieldset {
    margin-bottom: 10px;
    padding: 0;
    border: 1px solid #e2e2e2;
  }

  .legend {
    margin-left: 20px;
    padding: 0 10px;
    font-weight: 300;
    color: #bbbbbb;
    font-size: 15px;
  }

  .field-box {
    padding: 10px 0 0 30px;
  }

  .font-btn {
    font-size: 15px;
    vertical-align: bottom;
    margin-right: 5px;
  }

  td {
    height: 30px;
  }

  .text-left {
    text-align: left;
    width: 10%;

  }

  .text-right {
    text-align: left;
    width: 40%;
    color: #989898
  }


  .right-container {
    width: 100%;
    height: 100%;
    min-height: 100%;
    border-left: 2px solid #dfe6ec;
    margin-left: 4%;
  }

  .pic-container {
    height: 100%;
    padding: 10px;
  }

  .list-item {
    width: 100%;
    height: 24%;
    cursor: pointer;
    -webkit-border-radius: 0 10%;
    -moz-border-radius: 0 10%;
    border-radius: 0 10%;
  }

</style>
