<template>
  <div class="container">
    <!--登录框-->
    <div class="login_center" v-show="loginShow">
      <h2 style="text-align: center;margin-top: 80px;margin-bottom: 40px;">企业注册状态查询</h2>
      <el-form :model="registerLoginForm" :rules="rules" ref="loginForm" label-width="100px">
        <el-row>
          <!--添加一个隐藏的输入框，解决浏览器记住密码的功能，从而解决页面样式问题-->
          <el-col :span="10" :offset="6">
            <el-form-item label="隐藏框" style="display: none">
              <el-input></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10" :offset="6">
            <el-form-item label="账号" prop="userName">
              <el-input  v-model="registerLoginForm.userName" placeholder="账号" :clearable="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10" :offset="6">
            <el-form-item label="密码" prop="userPwd">
              <el-input type="password" v-model="registerLoginForm.userPwd" placeholder="密码" @keyup.enter.native="registerLogin" :clearable="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="10" :offset="6">
            <el-form-item>
              <el-button type="primary" style="width:100%;" :loading="loadingShow" @click.native.prevent="registerLogin">登 录</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <!--信息框-->
    <div v-show="!loginShow" class="message_center">
      <div >
        <div class="title">注册审批信息</div>
        <div class="approve_info">
          <!--审批状态-->
          <div class="status_info">
            <span style="font-weight: bold;font-size: 18px;">审批状态:</span>
            <span>
              <el-tag :type="formatterTagType()" style="font-weight: bold;font-size: 16px;margin-left: 10px">{{registerStatusFormatter}}</el-tag>
            </span>
          </div>
          <!--审批不通过理由-->
          <div class="reason" v-show="verifyResultReason">
            <div>{{verifyResultReason}}</div>
          </div>
        </div>
      </div>
      <div style="height: 620px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="企业基本信息">
            <el-form label-width="120px" :rules="rules" ref="form1" :model="form.registerBase" >
              <el-row>
                <el-col :span="11">
                  <el-form-item label="企业名称" prop="enterpriseName" ref="enterpriseName" tab="0">
                    <el-input v-model.trim="form.registerBase.enterpriseName" placeholder="企业名称" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode" ref="organizingInstitutionBarCode" tab="0">
                    <el-input v-model.trim="form.registerBase.organizingInstitutionBarCode" placeholder="社会信用代码" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="法人代表" prop="corporateRepresentative" ref="corporateRepresentative" tab="0">
                    <el-input v-model="form.registerBase.corporateRepresentative" placeholder="法人代表" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="身份证号" prop="idCardNo" ref="idCardNo" tab="0">
                    <el-input v-model.trim="form.registerBase.idCardNo" placeholder="身份证号" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="主体分类" prop="subjectClassification"  ref="subjectClassification" tab="0">
                    <el-select  v-model="form.registerBase.subjectClassification" placeholder="请选择" :disabled="readable" :clearable="true">
                      <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11" >
                  <el-form-item label="所属区域" prop="areaOptions"  ref="areaOptions" tab="0">
                    <el-cascader
                      v-model="areaOptions"
                      :options="area"
                      :show-all-levels="true"
                      expand-trigger="hover"
                      :change-on-select="true"
                      :clearable="true"
                      :disabled="readable"
                      :props="{label:'text', value: 'id'}">
                    </el-cascader>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="注册资金" prop="registeredCapital" ref="registeredCapital" tab="0">
                    <el-input v-model.trim="form.registerBase.registeredCapital" placeholder="注册资金" :readonly="readable" :clearable="true">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="经济性质" prop="economicNature" ref="economicNature" tab="0">
                    <el-select  v-model="form.registerBase.economicNature" placeholder="请选择"  :col="5" :disabled="readable" :clearable="true">
                      <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="联系人" prop="contacts" ref="contacts" tab="0">
                    <el-input v-model.trim="form.registerBase.contacts" placeholder="联系人" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="联系电话" prop="contactNumber" ref="contactNumber" tab="0">
                    <el-input v-model.trim="form.registerBase.contactNumber" placeholder="联系电话" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="联系人手机号" prop="contactMobile" ref="contactMobile" tab="0">
                    <el-input v-model.trim="form.registerBase.contactMobile" placeholder="联系人手机号" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="检验员手机" prop="inspectorMobile" ref="inspectorMobile" tab="0">
                    <el-input v-model.trim="form.registerBase.inspectorMobile" placeholder="检验员手机" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="邮政编码" prop="postalCode" ref="postalCode" tab="0">
                    <el-input v-model.trim="form.registerBase.postalCode" placeholder="邮政编码" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="企业注册日期" prop="registerDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerBase.registerDate" style="width: 100%;" :readonly="readable" :clearable="true"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="注册地址" prop="registerAddress" ref="registerAddress" tab="0">
                    <el-input v-model.trim="form.registerBase.registerAddress" placeholder="注册地址" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="生产地址" prop="productionAddress" ref="productionAddress" tab="0">
                    <el-input v-model.trim="form.registerBase.productionAddress" placeholder="生产地址" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row >
                <el-col :span="22">
                  <el-form-item label="经营范围" prop="operationScope" ref="operationScope" tab="0">
                    <el-input type="textarea" :rows="2" v-model.trim="form.registerBase.operationScope" placeholder="经营范围" :readonly="readable"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="营业执照信息" >
            <el-form :model="form.registerCertificate" :rules="rules" ref="form2"  label-width="100px" >
              <el-row >
                <el-col :span="11">
                  <el-form-item label="营业执照号"  prop="certificateNo" ref="certificateNo" tab="1">
                    <el-input v-model.trim="form.registerCertificate.certificateNo" placeholder="营业执照号" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="发证单位" prop="issuingUnit" ref="issuingUnit" tab="1">
                    <el-input v-model.trim="form.registerCertificate.issuingUnit" placeholder="发证单位" :readonly="readable" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="发证日期" prop="issuingDate" ref="issuingDate" tab="1">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerCertificate.issuingDate" style="width: 100%;" :readonly="readable" :clearable="true"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="有效期" prop="validDate" ref="validDate" tab="1">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerCertificate.validDate" style="width: 100%;" :readonly="readable" :clearable="true"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="22">
                  <el-form-item label="登记范围" prop="registerScope" ref="registerScope" tab="1">
                    <el-input type="textarea" :rows="5" v-model.trim="form.registerCertificate.registerScope" placeholder="登记范围" :readonly="readable"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="22">
                  <el-form-item label="营业执照" prop="imgFile" ref="imgFile" tab="1">
                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :file-list="imgFile"
                      :on-preview="handlePictureCardPreview"
                      :on-remove="handleImageRemove"
                      :on-change="addImgFile"
                      :limit="1"
                      :disabled="readable"
                      :auto-upload="false">
                      <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog  :visible.sync="dialogImageVisible" size="tiny"  :close-on-click-modal="false" append-to-body>
                      <img width="100%" :src="dialogImageUrl" alt="">
                    </el-dialog>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div style="margin-top: 15px;float:right;clear: both;margin-right:50%;" :class="{hideClass:isActive }">
        <el-button  type="primary" @click="commitBtn()">修 改</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {registerLogin,getResource,validateUserName,validateOrganizingInstitutionBarCode,updateRegister,getVerify}from "api/admin/register/index";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import {CheckSocialCreditCode,postalCodeValidator} from 'utils/validate';
  import idCardNoUtil from 'utils/idCardNoUtil';
    export default {
        name: "registerUserLogin",
      props:{
        dictData:{
          type:Object
        },
        area:{
          type:Array
        }
      },
      data(){
        const mobileValidator = (rule, value, callback) => {
          const mymobile =  /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
          if (value && !mymobile.test(value)) {
            return callback(new Error("电话号码格式不正确"));
          } else {
            callback();
          }
        };
        const cardNoValidator = (rule, value, callback) => {
          //15位和18位身份证号码的基本校验
          if(!value) callback() ;
          let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(value);
          if(!check) return callback(new Error('请输入正确的身份证号码'));
          //判断长度为15位或18位
          if(value.length==15){
            if(!idCardNoUtil.check15IdCardNo(value)){
              callback(new Error('请输入正确的身份证号码'));
            }else callback();

          }else if(value.length==18){
            if(!idCardNoUtil.check18IdCardNo(value))
              callback(new Error('请输入正确的身份证号码'));
            else callback();
          }else{
            callback(new Error('请输入正确的身份证号码'));
          }
        };
        const fileValidator =(rule, value, callback) =>{
          if(this.imgFile.length<1){
            callback(new Error('请选择营业执照附件'));
          }else{
            callback();
          }
        };
        const areaValidator=(rule, value, callback) =>{
          if(this.areaOptions.length<1){
            callback(new Error('请选择所属区域'));
          }else{
            callback();
          }
        };
        const organizingInstitutionBarCodeValidator = (rule, value, callback) => {
            if(value&&value!==this.codeSelected){
              validateOrganizingInstitutionBarCode({"organizingInstitutionBarCode":value}).then(response => {
                if( !response){
                  return callback(new Error("社会信用代码重复"));
                }else{
                  CheckSocialCreditCode(rule, value, callback);
                }
              })
            }else{
              callback();
            }

        };
          return {
            tabPosition:"0",
            registerLoginForm:{
              userName:undefined,
              userPwd:undefined
            },
            form:{
              registerBase:{},
              registerCertificate:{},
            },
            loadingShow:false,
            codeSelected:"",
            rules:{
              userName:[{required: true, message: "用户名不能为空", trigger: "blur"}],
              userPwd: [{required: true, message: "密码不能为空", trigger: "blur"}],
              //企业基本信息校验规则
              enterpriseName:[{required: true, message: "请输入企业名称", trigger: "blur"}],
              registerAddress:[{required: true, message: "请输入注册地址", trigger: "blur"}],
              areaOptions:[{required: true,validator: areaValidator, trigger: "blur"}],
              productionAddress:[{required: true, message: "请输入生产地址", trigger: "blur"}],
              organizingInstitutionBarCode:[{required: true, message: "请输入社会信用代码", trigger: "blur"},{validator: organizingInstitutionBarCodeValidator, trigger: "blur" }],
              subjectClassification:[{required: true, message: "请选择主体分类", trigger: "blur"}],
              corporateRepresentative:[{required: true, message: "请输入法人代表", trigger: "blur"}],
              idCardNo:[{required: true, message: "请输入身份证号", trigger: "blur"},{ validator: cardNoValidator, trigger: "blur" }],
              economicNature:[{required: true, message: "请选择企业经济性质", trigger: "blur"}],
              operationScope:[{required: true, message: "请输入企业经营范围", trigger: "blur"}],
              contacts:[{required: true, message: "请输入联系人", trigger: "blur"}],
              contactNumber:[{required: true, message: "请输入联系电话", trigger: "blur"},{ validator: mobileValidator, trigger: "blur" }],
              contactMobile:[{required: true, message: "请输入联系人手机号", trigger: "blur"},{ validator: mobileValidator, trigger: "blur" }],
              inspectorMobile:[{ validator: mobileValidator, trigger: "blur" }],
              postalCode:[{ validator: postalCodeValidator, trigger: "blur" }],
              registerDate:[{required: true, message: "请输入企业注册日期", trigger: "blur"}],
              //营业执照表单校验
              certificateNo:[{required: true, message: "请输入营业执照编号", trigger: "blur"}],
              issuingUnit:[{required: true, message: "请输入发证单位", trigger: "blur"}],
              issuingDate:[{required: true, message: "请输入发证日期", trigger: "blur"}],
              registerScope:[{required: true, message: "请输入登记范围", trigger: "blur"}],
              validDate:[{required: true, message: "请输入有效日期", trigger: "blur"}],
              imgFile:[{required: true, validator: fileValidator, trigger: "change"}],
            },
            loginShow:true,
            areaOptions:[],
            serverImageUrl:process.env.SERVERIMAGEURL,
            imgFile:[],
            upDateImgFile:[],//修改图片后存储文件
            dialogImageVisible:false,//图片预览窗口是否显示
            dialogImageUrl:undefined,//预览图片的地址
            readable:true,//是否可以修改表单
            registerStatusFormatter:undefined,//审批状态中文
            isActive:false,//根据审批状态切换隐藏和显示按钮
            fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
            verifyResultReason:null,
          }
      },
      computed: {
        subjectClassificationOptions(){
          return this.dictData["企业主体分类"];
        },
        economicNatureOptions(){
          return this.dictData["企业经济性质"];
        },
      },
      created(){

      },
      methods: {
          //登录
        registerLogin(){
          this.$refs["loginForm"].validate((valid) => {
            if (valid) {
              this.loadingShow=true;
              registerLogin(this.registerLoginForm).then(response=> {
                this.loadingShow=false;
                if(response.rel){
                  this.form=response.data;
                  // console.log(this.form)
                  this.codeSelected=this.form.registerBase.organizingInstitutionBarCode;
                  let str = findParentNode(this.area,this.form.registerBase.areaId);
                  this.areaOptions=str.split(",");
                  this.areaOptions.push(this.form.registerBase.areaId+'');
                  this.registerStatusFormatter=parseValueToText(this.form.registerStatus,this.dictData["企业注册状态"]);
                  this.form.registerStatus=="1"?this.readable=true:this.readable=false;
                  let resourceId = this.form.registerCertificate.certificatePhoto;
                  getResource(resourceId).then(response =>{
                    this.imgFile=[];
                    this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
                    $(".el-upload--picture-card").hide();
                  })
                  //如果审核状态是未通过，则需要查询审核记录表
                  if(this.form.registerStatus=="2"){
                    getVerify({"registerId":this.form.registerId}).then(response =>{
                      this.verifyResultReason=response.data.reason;
                    });
                  }
                  this.loginShow=false;
                }
              }).catch(() => {
                this.loadingShow=false;
              });
            }
          });
        },
        //不同审批状态转换为不同类型的标签
        formatterTagType(){
          if(this.form.registerStatus==0){//待审核
            return "warning";
          }else if((this.form.registerStatus==1)){//审核通过
            this.isActive=true;
            return "success";
          }else{//审核未通过
            return "danger";
          }
        },
        // 点击"+"图标时显示弹出框
        handlePictureCardPreview(file) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
        },
        //删除图片
        handleImageRemove(file){
          this.imgFile=[];
          this.upDateImgFile=[];
          if( this.imgFile.length==0){
            $(".el-upload--picture-card").show();
          }
        },
        addImgFile(file) { // 添加图片回调
          if(this.fileFormat.indexOf(file.raw.type) < 0 ){
            this.$message({
              type:'error',
              showClose: true,
              message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
            });
            return false;
          }
          this.$refs["imgFile"].resetField();//当添加图片后，重置图片验证功能。
          this.imgFile.push(file);
          this.upDateImgFile.push(file);
          if( this.imgFile.length>0){
            $(".el-upload--picture-card").hide();
          }
        },
        //修改后提交表单
        commitBtn(){
          let _self = this;
          let flag = true;
          _self.$refs["form1"].validate((valid,errors) => {
            if(!valid){
              flag=false;
              for (var key in errors){
                this.tabPosition = this.$refs[key].$attrs['tab'];
                break;
              }
            }
          });
          _self.$refs["form2"].validate((valid,errors) => {
            if(!(flag&&valid)){
              flag=false;
              for (var key in errors){
                this.tabPosition = this.$refs[key].$attrs['tab'];
                break;
              }
            }
          });
          if(flag){
            _self.$confirm(`确定要修改吗?`, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              _self.form.registerCertificate.validDate=parseTime(_self.form.registerCertificate.validDate);
              _self.form.registerCertificate.issuingDate=parseTime(_self.form.registerCertificate.issuingDate);
              _self.form.registerBase.registerDate=parseTime( _self.form.registerBase.registerDate);
              let areaOptions=_self.areaOptions;
              _self.form.registerBase.areaId=areaOptions[areaOptions.length-1];
              let param = new FormData();
              param.append("formData",JSON.stringify(_self.form));
              if(this.upDateImgFile.length>0){//如果营业执照被修改，则upDateImgFile的长度大于0，会更新图片，否则不跟新图片
                param.append("file",_self.imgFile[0].raw);
              }else {
                param.append("file",null);
              }
              updateRegister(param).then(response=> {
                if(response.rel){
                  _self.upDateImgFile=[];
                  _self.$message({
                    type:'success',
                    showClose: true,
                    message: `更新成功！`
                  });
                } else {
                  _self.$message({
                    type:'error',
                    showClose: true,
                    message: `更新失败！`
                  });
                }
              })
            })
          }
        },


      },


    }
</script>

<style scoped>

  .login_center{
    width:100%;
    height: 300px;
  }
  .title{
    border-bottom: 2px solid #d8d7d7;
    height: 40px;
    clear: both;
    overflow: hidden;
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    margin-top: 20px;
  }
  .approve_info{
    border-bottom: 2px solid #d8d7d7;
    height: 100px;
    margin-bottom: 10px;
    position: relative;
  }
  .status_info{
    float: left;
    width: 20%;
    padding-top: 30px;
  }
  .reason{
    float: left;
    padding-top: 30px;
  }
  .reason>div{
    float: left;
  }
  .hideClass{
    display: none;
  }
</style>
