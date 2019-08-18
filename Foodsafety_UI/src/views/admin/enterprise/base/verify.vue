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
          <!--<el-button size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>-->
          <el-button v-if="scope.row.verifyStatus == 1" size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>
          <el-button v-else size="mini" :disabled="true">审核</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <!--<el-button size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>-->
          <el-button v-if="scope.row.verifyStatus == 1" size="mini" type="primary" @click="handelAudit(scope.row)">审核</el-button>
          <el-button v-else size="mini" :disabled="true">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <!--审核对话框-->
    <el-dialog  title="基本信息审核"  :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <template>
          <i class="el-icon-edit">基本信息审核</i>
        </template>
      </div>
      <div style="height: 600px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="基本信息">
            <el-form :model="form" label-width="100px" :disabled="true">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="企业名称" prop="enterpriseName">
                    <el-input v-model.trim="form.enterpriseName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="社会信用代码" >
                    <el-input v-model.trim="form.organizingInstitutionBarCode" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="企业法人" prop="corporateRepresentative">
                    <el-input v-model.trim="form.corporateRepresentative"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="证件类型" prop="idType">
                    <el-select  v-model="form.idType" placeholder="请选择" :clearable="true">
                      <el-option v-for="item in idTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="证件号码" prop="idCardNo">
                    <el-input v-model.trim="form.idCardNo"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="主体分类" prop="subjectClassification">
                    <el-select  v-model="form.subjectClassification" placeholder="请选择" >
                      <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="经济性质" prop="economicNature">
                    <el-select  v-model="form.economicNature" placeholder="请选择">
                      <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="注册资金" prop="registeredCapital">
                    <el-input v-model.trim="form.registeredCapital" placeholder="注册资金" >
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="所属区域" prop="areaOptions">
                    <el-cascader
                      v-model="areaOptions"
                      :options="areaData"
                      :show-all-levels="true"
                      expand-trigger="hover"
                      :change-on-select="true"
                      :props="{label:'text', value: 'id'}">
                    </el-cascader>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="注册日期" prop="registerDate">
                    <el-date-picker type="date" placeholder="请选择" v-model.trim="form.registerDate"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="经营期限" prop="operatingTerm">
                    <el-date-picker type="date" placeholder="请选择" v-model.trim="form.operatingTerm"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="企业联系人" prop="contacts">
                    <el-input v-model.trim="form.contacts" placeholder="联系人"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="联系人手机号" prop="contactMobile">
                    <el-input v-model.trim="form.contactMobile"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="邮政编码" prop="postalCode">
                    <el-input v-model.trim="form.postalCode"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="企业邮箱" prop="email">
                    <el-input v-model.trim="form.email"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="联系电话" prop="contactNumber">
                    <el-input v-model.trim="form.contactNumber"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="传真" prop="fax">
                    <el-input v-model.trim="form.fax"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="生产地址" prop="productionAddress">
                    <el-input v-model.trim="form.productionAddress"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="注册地址" prop="registerAddress">
                    <el-input v-model.trim="form.registerAddress"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="检验员手机号" prop="inspectorMobile">
                    <el-input v-model.trim="form.inspectorMobile"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="地址坐标" prop="lnglat"  tab="0">
                    <el-input v-model="form.lnglat" placeholder="企业地址坐标" :readonly="true">
                      <i slot="suffix" class="el-icon-location" ></i>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="24">
                  <el-form-item label="经营范围" prop="operationScope">
                    <el-input type="textarea" :rows="2" v-model.trim="form.operationScope" placeholder="经营范围" :clearable="true"></el-input>
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
              <el-table-column align="center"  label="修改前" prop="old" >
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
  import {getBaseVerifyPage,getChangeById,verifyEnterpriseBase}from "api/admin/enterprise/base/index";
  import {getResource}from "api/admin/register/index";
  import { parseValueToText ,parseTime,findParentNode,getTreeText} from "utils/index";
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
          dataType: "1",
          areaId: undefined,
          enterpriseNameLike:undefined,
          changeType:undefined,
          verifyStatus:"1"
        },
        needFixedRight:false,
        dialogFormVisible: false,
        form:{},
        changeList:null,
        areaOptions:[],
        tableShow:false,//数据修改表格是否显示，变更类型为2（修改）时显示。
        changeReason:null,//数据变更原因
        changeType:null,//数据变更类型
        verifyForm:{
          verifyId:'',
          auditType:"",//审核是否通过 0：不通过，1：通过
          verifyConclusion:'',//审核结论
        },
        rules:{
          auditType:[{required: true, message: "请选择", trigger: "change"}],
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
      subjectClassificationOptions(){
        return this.staticData["企业主体分类"];
      },
      verifyStatusOptions(){
        return this.staticData["变更审核状态"]
      },
      economicNatureOptions(){
        return this.staticData["企业经济性质"];
      },
      idTypeOptions(){
        return this.staticData["证件类型"];
      }
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
        getBaseVerifyPage(this.listQuery).then(response => {
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
          let str = findParentNode(this.areaData,this.form.areaId);
          this.areaOptions=str.split(",");
          this.areaOptions.push(this.form.areaId+'');
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
            verifyEnterpriseBase(param).then(response => {
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
        if(cellValue=='enterpriseName'){
          return "企业的名称";
        }else if(cellValue=='organizingInstitutionBarCode'){
          return "社会信用代码";
        }else if(cellValue=='corporateRepresentative') {
          return "企业法人";
        }else if(cellValue=='idType'){
          return "证件类型";
        }else if(cellValue=='idCardNo'){
          return "证件号码";
        }else if(cellValue=='subjectClassification'){
          return "主体分类";
        }else if(cellValue=='economicNature'){
          return "经济性质";
        }else if(cellValue=='registeredCapital'){
          return "注册资金(万元)";
        }else if(cellValue=='areaId'){
          return "所属区域";
        } else if(cellValue=='registerDate'){
          return "注册日期";
        } else if(cellValue=='operatingTerm'){
          return "经营期限";
        }else if(cellValue=='contacts'){
          return "联系人";
        }else if(cellValue=='contactMobile'){
          return "联系人手机号";
        }else if(cellValue=='postalCode'){
          return "邮政编码";
        }else if(cellValue=='email'){
          return "企业邮箱";
        }else if(cellValue=='contactNumber'){
          return "联系电话";
        }else if(cellValue=='fax'){
          return "传真";
        }else if(cellValue=='productionAddress'){
          return "生产地址";
        }else if(cellValue=='registerAddress'){
          return "注册地址";
        }else if(cellValue=='inspectorMobile'){
          return "检验员手机号";
        }else if(cellValue=='lnglat'){
          return "地址坐标";
        }else if(cellValue=='operationScope'){
          return "经营范围";
        }

      },
      oldFormatter(row){
        let cellValue=row.old;
        if(row.name=='subjectClassification') {
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
        }else if(row.name=='idType'){
          return parseValueToText(cellValue, this.staticData["证件类型"]);
        }else if(row.name=='economicNature'){
          return parseValueToText(cellValue, this.staticData["企业经济性质"]);
        }else if(row.name=='areaId'){
          return cellValue?getTreeText(this.areaData,cellValue):'';
        }else if(row.name=='operatingTerm'||row.name=='registerDate'){
          return cellValue?parseTime(cellValue,'{y}-{m}-{d}'):"";
        }else{
          return cellValue;
        }
      },
      newFormatter(row){
        let cellValue=row.new;
        if(row.name=='subjectClassification') {
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
        }else if(row.name=='idType'){
          return parseValueToText(cellValue, this.staticData["证件类型"]);
        }else if(row.name=='economicNature'){
          return parseValueToText(cellValue, this.staticData["企业经济性质"]);
        }else if(row.name=='areaId'){
          return cellValue?getTreeText(this.areaData,cellValue):'';
        }else if(row.name=='operatingTerm'||row.name=='registerDate'){
          return cellValue?parseTime(cellValue,'{y}-{m}-{d}'):"";
        }else{
          return cellValue;
        }
      },
    },
  }
</script>

<style scoped>

</style>
