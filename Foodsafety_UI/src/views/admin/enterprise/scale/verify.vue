<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="企业名称" prop="enterpriseNameLike" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;"  placeholder=""  v-model.trim="listQuery.enterpriseNameLike"> </el-input>
          </el-form-item>
          <el-form-item label="变更类型" prop="certificateStatus" class="filter-item">
            <el-select  v-model="listQuery.changeType" placeholder="请选择" clearable>
              <el-option v-for="item in  changeTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="变更审核状态" prop="verifyStatus" class="filter-item">
            <el-select  placeholder="请选择" v-model.trim="listQuery.verifyStatus">
              <el-option v-for="item in  verifyStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"   :height="height">
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="企业信用代码" width="170" prop="organizingInstitutionBarCode"></el-table-column>
      <el-table-column align="center" label="信息类型" width="150" prop="dataType" :formatter="dataTypeFormatter"></el-table-column>
      <el-table-column align="center" label="变更类型" width="120" prop="changeType" :formatter="changeTypeFormatter"></el-table-column>
      <el-table-column align="center" label="变更时间" width="180" prop="changeTime"></el-table-column>
      <el-table-column align="center" label="变更原因" width="200" prop="changeReason" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="变更人员" width="100" prop="changeUserName" ></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="100" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button v-if="scope.row.verifyStatus == 1" size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>
          <el-button v-else size="mini" :disabled="true">审核</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <el-button v-if="scope.row.verifyStatus == 1" size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>
          <el-button v-else size="mini" :disabled="true">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <!--审核对话框-->
    <el-dialog  title="规模审核"  :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <template>
          <i class="el-icon-edit">规模审核</i>
        </template>
      </div>
      <div style="height: 600px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="规模信息">
            <el-form :model="form" label-width="200px" :disabled="true">
              <el-form-item label="企业的规模:" prop="enterpriseScale">
                <el-select v-model="form.enterpriseScale">
                  <el-option v-for="item in  enterpriseScaleOptions" :key="item.value" :label="item.text" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="企业总人数:" prop="enterpriseEmployees">
                    <el-input v-model="form.enterpriseEmployees"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="专业技术人员数:" prop="enterpriseTechnicians">
                    <el-input v-model="form.enterpriseTechnicians"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="占地面积:" prop="areaFloor">
                    <el-input v-model="form.areaFloor">
                      <template slot="append">(万平方米)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="建筑面积:" prop="areaBuilding">
                    <el-input v-model="form.areaBuilding">
                      <template slot="append">(万平方米)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="固定资产(现值):" prop="fixedAssets">
                    <el-input v-model="form.fixedAssets">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="流动资金:" prop="floatingCapital">
                    <el-input v-model="form.floatingCapital">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="年总产值:" prop="grossAnnualOutputValue">
                    <el-input v-model="form.grossAnnualOutputValue">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="年销售额:" prop="annualSales">
                    <el-input v-model="form.annualSales">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="年缴税金额:" prop="annualTaxPayment">
                    <el-input v-model="form.annualTaxPayment">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="年利润:" prop="annualProfit">
                    <el-input v-model="form.annualProfit">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="更新信息">
            <el-row style="margin: 5px 20px 20px 20px;">
              <el-col :span="2">变更原因：</el-col>
              <el-col :span="13" >{{changeReason}}</el-col>
              <el-col :span="4":offset="3">
                <span>变更类型：</span>
                <el-tag v-if="changeType=='1'">新 增</el-tag>
                <el-tag type="warning" v-else-if="changeType=='2'">修 改</el-tag>
                <el-tag type="danger" v-else-if="changeType=='3'">删 除</el-tag>
              </el-col>
            </el-row>
            <el-table :data="changeList"  border v-if="tableShow">
              <el-table-column align="center"  label="修改内容" prop="name" :formatter="nameFormatter"></el-table-column>
              <el-table-column align="center"  label="修改前" prop="old">
                <template slot-scope="scope">
                  <el-col>{{oldFormatter(scope.row)}}</el-col>
                </template>
              </el-table-column>
              <el-table-column align="center"  label="修改后" prop="new">
                <template slot-scope="scope">
                  <el-col>{{newFormatter(scope.row)}}</el-col>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="审核操作">
            <el-form label-width="120px" :model="verifyForm" :rules="rules" ref="verifyForm">
              <el-form-item label="审核是否通过:" prop="auditType">
                <el-radio-group v-model="verifyForm.auditType">
                  <el-radio :label="1">通过</el-radio>
                  <el-radio :label="0">不通过</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="审核意见:"  prop="verifyConclusion">
                <el-input type="textarea" :rows="5"  v-model="verifyForm.verifyConclusion"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer" >
        <el-button @click="cancel()">取 消</el-button>
        <el-button  type="primary" @click="commit()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getScaleVerifyPage,getChangeById,verifyEnterpriseScale}from "api/admin/enterprise/scale/index";
  import {getResource}from "api/admin/register/index";
  import { parseValueToText ,parseTime} from "utils/index";
  import { loadGridHeight } from "api/screen";
    export default {
      name: "verify",
      props: {
        tabName:{
          default: undefined
        }
      },
      data(){
        return {
          tabPosition:'0',
          height:undefined,
          list: null,
          total: null,
          listLoading: true,
          listQuery: {
            page: 1,
            limit: 20,
            dataType: "3",
            areaId: undefined,
            enterpriseNameLike:undefined,
            changeType:undefined,
            verifyStatus:"1"
          },
          needFixedRight:false,
          dialogFormVisible: false,
          form:{
            enterpriseId:'',
            enterpriseScale:'',
            enterpriseEmployees:'',
            enterpriseTechnicians:'',
            areaFloor:'',
            areaBuilding:'',
            fixedAssets:'',
            floatingCapital:'',
            grossAnnualOutputValue:'',
            annualSales:'',
            annualTaxPayment:'',
            annualProfit:''
          },
          changeList:null,
          tableShow:false,//数据修改表格是否显示，变更类型为2（修改）时显示。
          changeReason:null,//数据变更原因
          changeType:null,//数据变更类型
          verifyForm:{
            verifyId:'',
            auditType:"",//审核是否通过 0：不通过，1：通过
            verifyConclusion:'',//审核结论
          },
          rules:{
            auditType:[{required: true, message: "请选择", trigger: "blur"}],
            verifyConclusion:[{required: true, message: "请输入审核结论", trigger: "blur"}],
          }
        }
      },
      computed: {
        ...mapGetters(["user", "staticData", "areaData"]),
        changeTypeOptions(){
          return this.staticData["企业变更操作类型"]
        },
        statusOptions(){
          return this.staticData["企业非基本信息状态"]
        },
        enterpriseScaleOptions(){
          return this.staticData["企业规模"]
        },
        verifyStatusOptions(){
          return this.staticData["变更审核状态"]
        },
      },
      created(){
        this.getList();
      },
      mounted(){
        //首次整个视图都渲染完毕后执行
        this.$nextTick(function () {
          let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
          this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);

          this.height=loadGridHeight(this.tabName);
        });
        //监听屏幕的改变
        window.onresize = () => {
          let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
          this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);

          this.height=loadGridHeight(this.tabName);
        }
      },
      methods: {
        getList() {
          this.listLoading = true;
          this.listQuery.areaId = this.user.areaId;
          getScaleVerifyPage(this.listQuery).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
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
        handleFilter() {
          this.listQuery.page=1;
          this.getList();
        },
        resetQuery() {
          this.listQuery.verifyStatus="1";
          this.listQuery.enterpriseNameLike= "";
          this.listQuery.changeType=undefined;
        },
        handelAudit(row){
          this.dialogFormVisible=true;
          if(row.changeType=='2'){
            this.tableShow=true;
          }else{
            this.tableShow=false;
          }
          this.changeReason=row.changeReason;
          this.changeType=row.changeType;
          this.verifyForm.verifyId=row.verifyId;
          getChangeById(row.changeId).then(response => {
            this.form=response.data.formData;
            this.changeList=response.data.compareData;
          })
        },
        closeDialog(){
          this.tabPosition='0';
          this.form={};
          this.verifyForm.verifyId='';
          this.verifyForm.auditType='';
          this.verifyForm.verifyConclusion='';
          this.$refs['verifyForm'].resetFields();
        },
        //提交审核
        commit(){
          let _self = this;
          _self.$refs["verifyForm"].validate((valid,errors) => {
            if(valid){
              let param = new FormData();
              param.append("changeId",this.form.changeId);
              param.append("auditType",this.verifyForm.auditType);
              param.append("verifyConclusion",this.verifyForm.verifyConclusion);
              param.append("verifyId",this.verifyForm.verifyId);
              verifyEnterpriseScale(param).then(response => {
                _self.dialogFormVisible=false;
                _self.getList();
                if(response.rel){
                  _self.$message({
                    type:'success',
                    showClose: true,
                    message: `操作成功！`
                  });
                }else {
                  _self.$message({
                    type:'error',
                    showClose: true,
                    message: `操作失败！`
                  });
                }
              })
            }else{
              this.tabPosition='2';
            }
          })
        },
        cancel(){
          this.dialogFormVisible=false;
        },
        dataTypeFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业数据变更类型"]);
        },
        changeTypeFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业变更操作类型"]);
        },
        nameFormatter(row, column, cellValue){
          if(cellValue=='enterpriseScale'){
            return "企业的规模";
          }else if(cellValue=='enterpriseEmployees'){
            return "企业总人数";
          }else if(cellValue=='enterpriseTechnicians'){
            return "专业技术人员数";
          }else if(cellValue=='areaFloor'){
            return "占地面积";
          }else if(cellValue=='areaBuilding'){
            return "建筑面积";
          }else if(cellValue=='fixedAssets'){
            return "固定资产(现值)";
          }else if(cellValue=='floatingCapital'){
            return "流动资金";
          }else if(cellValue=='grossAnnualOutputValue'){
            return "年总产值";
          } else if(cellValue=='annualSales'){
            return "年销售额";
          } else if(cellValue=='annualTaxPayment'){
            return "年缴税金额";
          }else if(cellValue=='annualProfit'){
            return "年利润";
          }
        },
        oldFormatter(row){
          let cellValue=row.old;
          if(row.name=='subjectClassification') {
            return parseValueToText(cellValue, this.staticData["企业主体分类"]);
          }else{
            return cellValue;
          }
        },
        newFormatter(row){
          let cellValue=row.new;
          if(row.name=='enterpriseScale') {
            return parseValueToText(cellValue, this.staticData["企业主体规模"]);
          }else{
            return cellValue;
          }
        },
      },
    }
</script>

<style scoped>

</style>
