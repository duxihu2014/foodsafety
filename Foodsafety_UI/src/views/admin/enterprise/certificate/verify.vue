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
      <el-dialog  title="证照审核"  :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
        <div slot="title">
          <template>
            <i class="el-icon-edit">证照审核</i>
          </template>
        </div>
        <div style="height: 600px;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="证照信息">
              <el-form :model="form" label-width="100px" disabled>
                <el-form-item label="证照名称:" prop="certificateName">
                  <el-input v-model.trim="form.certificateName"  placeholder="请输入证照名称" ></el-input>
                </el-form-item>
                <el-form-item label="证照编号:" prop="certificateNo">
                  <el-input v-model.trim="form.certificateNo" placeholder="请输入证照编号"></el-input>
                </el-form-item>
                <el-row>
                  <el-col :span="10">
                    <el-form-item label="证照类型:" prop="certificateType">
                      <el-select  v-model="form.certificateType" placeholder="请选择证照类型">
                        <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="发证单位:" prop="issuingUnit" >
                  <el-input v-model.trim="form.issuingUnit" placeholder="请输入发证单位"></el-input>
                </el-form-item>
                <el-row>
                  <el-col :span="10">
                    <el-form-item label="发证日期:" prop="issuingDate" >
                      <el-date-picker type="date" placeholder="请选择" v-model="form.issuingDate"></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" :offset="2">
                    <el-form-item label="有效期:" prop="validDate" >
                      <el-date-picker type="date" placeholder="请选择" v-model="form.validDate"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="登记范围:">
                  <el-input type="textarea" v-model.trim="form.registerScope"  :rows="3" placeholder="请输入登记范围"></el-input>
                </el-form-item>
                <el-form-item label="证照图片:" prop="imgFile" ref="imgFile">
                  <el-upload
                    action="https://jsonplaceholder.typicode.com/posts/"
                    list-type="picture-card"
                    :file-list="imgFile"
                    :on-preview="handlePictureCardPreview"
                    :limit="1"
                    :auto-upload="false">
                    <i class="el-icon-plus"></i>
                  </el-upload>
                  <el-dialog  :visible.sync="dialogImageVisible" size="tiny"  :close-on-click-modal="false" append-to-body>
                    <img width="100%" :src="dialogImageUrl" alt="">
                  </el-dialog>
                </el-form-item>
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
                 <el-table-column align="center"  label="修改前" prop="old"  >
                <template slot-scope="scope">
                  <el-button v-if="scope.row.name=='certificatePhoto'"size="mini" type="primary" plain
                     @click="handlePhoto(scope.row.old)">图片</el-button>
                   <el-col v-else>{{oldFormatter(scope.row)}}</el-col>
                </template>
               </el-table-column>

              <el-table-column align="center"  label="修改后" prop="new" >
                <template slot-scope="scope">
                  <el-button v-if="scope.row.name=='certificatePhoto'"size="mini" type="primary" plain
                     @click="handlePhoto(scope.row.new)">图片</el-button>
                  <el-col v-else>{{newFormatter(scope.row)}}</el-col>
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
    import {getCertificateVerifyPage,getChangeById,verifyEnterpriseCertificate}from "api/admin/enterprise/certificate/index";
    import {getResource}from "api/admin/register/index";
    import { parseValueToText ,parseTime} from "utils/index";
    import { loadGridHeight } from "api/screen";
    export default {
      components: {},
      name: "verify",
      props: {
        tabName:{
          default: undefined
        }
      },
      watch:{
        total(val){
          this.$emit('setCount',val,2);
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
                dataType: "2",
                areaId: undefined,
                enterpriseNameLike:undefined,
                changeType:undefined,
                verifyStatus:"1"

              },
              needFixedRight:false,
              dialogFormVisible: false,
              serverImageUrl:process.env.SERVERIMAGEURL,
              imgFile:[],
              dialogImageVisible:false,//图片预览窗口是否显示
              dialogImageUrl:undefined,//预览图片的地址
              form: {},
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
        certificateTypeOptions(){
          return this.staticData["证照类型"]
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
          getCertificateVerifyPage(this.listQuery).then(response => {
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
          this.listQuery.verifyStatus= "1";
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
            let resourceId = this.form.certificatePhoto;
            getResource(resourceId).then(response => {
              this.imgFile=[];
              this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              $(".el-upload--picture-card").hide();
            })
          })
        },
        closeDialog(){
          this.tabPosition='0';
          this.form={};
          this.imgFile=[];
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
              verifyEnterpriseCertificate(param).then(response => {
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
        // 点击"+"图标时显示弹出框
        handlePictureCardPreview(file) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
        },
        dataTypeFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业数据变更类型"]);
        },
        changeTypeFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业变更操作类型"]);
        },
        nameFormatter(row, column, cellValue){
          if(cellValue=='certificateName'){
            return "证照名称";
          }else if(cellValue=='certificateNo'){
            return "证照编号";
          }else if(cellValue=='certificateType'){
            return "证照类型";
          }else if(cellValue=='issuingUnit'){
            return "发证单位";
          }else if(cellValue=='issuingDate'){
            return "发证日期";
          }else if(cellValue=='validDate'){
            return "有效期";
          }else if(cellValue=='registerScope'){
            return "登记范围";
          }else if(cellValue=='certificatePhoto'){
            return "证照图片";
          }
        },
         oldFormatter(row){
          let cellValue=row.old;
          if(row.name=='certificateType') {
            return this.formatCertificateType(cellValue);
          }else {
            return cellValue;
          }
        },
        newFormatter(row){
           let cellValue=row.new;
          if(row.name=='certificateType') {
            return this.formatCertificateType(cellValue);
          }else {
            return cellValue;
          }
        },
        formatCertificateType(param){
          if (!param) { return '无' }
          if (!this.certificateTypeOptions.length) { return param }
          return this.certificateTypeOptions.filter((item) => {
            return item.value == param
          })[0].text
        },
        handlePhoto(id){
            getResource(id).then(response => {
              if (response.rel) {
                this.dialogImageUrl = this.serverImageUrl+'/'+response.data.resourcePath;
                this.dialogImageVisible = true;
              }else{
                this.$message({
                  type:'error',
                  showClose: true,
                  message: `资源获取失败！`
                });
              }
            });
        }
      }
    }
</script>

<style scoped>
  .reason{

  }
  .changeType{

  }
</style>
