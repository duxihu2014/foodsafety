<template>
<div style=" height: 80vh;">
  <div class="centre">
    <div class="title">
      <div class="title_text">企业用户注册</div>
    </div>
    <div id="step">
      <!-- 步骤组件 -->
        <el-steps :active="active"   align-center finish-status="success">
          <el-step title="账号信息" ></el-step>
          <el-step title="企业基本信息" ></el-step>
          <el-step title="营业执照信息" ></el-step>
        </el-steps>
      <!--内容-->
      <div id="step_content">
        <!--账号信息-->
        <el-form v-show="active==0" :rules="rules" ref="form1" :model="accountForm" label-width="100px" status-icon style="padding-top: 50px">
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5">
              <el-form-item label="账号" prop="userName">
                <el-input v-model="accountForm.userName" placeholder="账号" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5">
              <el-form-item label="密码" prop="userPwd">
                <el-input type="password" v-model="accountForm.userPwd" placeholder="密码" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5" >
              <el-form-item label="确认密码" prop="userPwdConfirm">
                <el-input type="password" v-model="accountForm.userPwdConfirm" placeholder="确认密码" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5">
              <el-form-item label="用户姓名" prop="realName">
                <el-input v-model="accountForm.realName" placeholder="用户姓名" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5">
              <el-form-item label="联系电话" prop="mobile">
                <el-input v-model="accountForm.mobile" placeholder="联系电话" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-bottom: 10px">
            <el-col :span="11" :offset="5">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="accountForm.email" placeholder="邮箱" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <!--企业基本信息-->
        <el-form v-show="active==1" :rules="rules" ref="form2" label-width="120px" :model="enterpriseForm" status-icon>
          <el-row>
            <el-col :span="11">
              <el-form-item label="企业名称" prop="enterpriseName">
                <el-input v-model.trim="enterpriseForm.enterpriseName" placeholder="企业名称" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
                <el-input v-model.trim="enterpriseForm.organizingInstitutionBarCode" placeholder="社会信用代码" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="法人代表" prop="corporateRepresentative">
                <el-input v-model.trim="enterpriseForm.corporateRepresentative" placeholder="法人代表" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="身份证号" prop="idCardNo">
                <el-input v-model.trim="enterpriseForm.idCardNo" placeholder="身份证号" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="主体分类" prop="subjectClassification">
                <el-select  v-model="enterpriseForm.subjectClassification" placeholder="请选择" :clearable="true">
                  <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="11" >
              <el-form-item label="所属区域" prop="areaOptions">
                <el-cascader
                  v-model="enterpriseForm.areaOptions"
                  :options="area"
                  :show-all-levels="true"
                  expand-trigger="hover"
                  :change-on-select="true"
                  :clearable="true"
                  :props="{label:'text', value: 'id'}">
                </el-cascader>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="注册资金" prop="registeredCapital">
                <el-input v-model.trim="enterpriseForm.registeredCapital" placeholder="注册资金" :clearable="true">
                  <template slot="append">(万元)</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="经济性质" prop="economicNature">
                <el-select  v-model="enterpriseForm.economicNature" placeholder="请选择"  :col="5" :clearable="true">
                  <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="联系人" prop="contacts">
                <el-input v-model.trim="enterpriseForm.contacts" placeholder="联系人" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="联系电话" prop="contactNumber">
                <el-input v-model.trim="enterpriseForm.contactNumber" placeholder="联系电话" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="联系人手机号" prop="contactMobile">
                <el-input v-model.trim="enterpriseForm.contactMobile" placeholder="联系人手机号" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="检验员手机" prop="inspectorMobile">
                <el-input v-model.trim="enterpriseForm.inspectorMobile" placeholder="检验员手机" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="邮政编码" prop="postalCode">
                <el-input v-model.trim="enterpriseForm.postalCode" placeholder="邮政编码" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="企业注册日期" prop="registerDate">
                <el-date-picker type="date" placeholder="请选择" v-model="enterpriseForm.registerDate" style="width: 100%;" :clearable="true"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="注册地址" prop="registerAddress">
                <el-input v-model.trim="enterpriseForm.registerAddress" placeholder="注册地址" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="生产地址" prop="productionAddress">
                <el-input v-model.trim="enterpriseForm.productionAddress" placeholder="生产地址" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row >
            <el-col :span="22">
              <el-form-item label="经营范围" prop="operationScope">
                <el-input type="textarea" :rows="2" v-model.trim="enterpriseForm.operationScope" placeholder="经营范围" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <!--证照信息-->
        <el-form v-show="active==2" :rules="rules" ref="form3" :model="licenseForm" status-icon label-width="100px" >
          <el-row >
            <el-col :span="11">
              <el-form-item label="营业执照号"  prop="certificateNo">
                <el-input v-model.trim="licenseForm.certificateNo" placeholder="营业执照号" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="发证单位" prop="issuingUnit">
                <el-input v-model.trim="licenseForm.issuingUnit" placeholder="发证单位" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="11">
              <el-form-item label="发证日期" prop="issuingDate">
                <el-date-picker type="date" placeholder="请选择" v-model="licenseForm.issuingDate" style="width: 100%;" :clearable="true"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="有效期" prop="validDate">
                <el-date-picker type="date" placeholder="请选择" v-model="licenseForm.validDate" style="width: 100%;" :clearable="true"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="22">
              <el-form-item label="登记范围" prop="registerScope">
                <el-input type="textarea" :rows="5" v-model.trim="licenseForm.registerScope" placeholder="登记范围" :clearable="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row >
            <el-col :span="22">
              <el-form-item label="营业执照" prop="imgFile" ref="certificatePhoto">
                  <el-upload
                    action="https://jsonplaceholder.typicode.com/posts/"
                    list-type="picture-card"
                    :file-list="imgFile"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleImageRemove"
                    :on-change="addImgFile"
                    :limit="1"
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
        <!--提交审核提示-->
        <div v-show="active==3" style="padding-top:180px;margin:auto;text-align: center;font-size: 20px;color: #198a1b;">
          恭喜您，提交审核成功！
        </div>
      </div>
      <el-button v-if="active<3"
                 type="primary"
                 :disabled="active==0?true:false"
                 @click="preStep"
                 style="float: left; margin-left:10%; margin-bottom: 10px;">
        上一步
      </el-button>
      <el-button type="primary"
                 v-show="active<2"
                 @click="nextStep"
                 style="float: left; margin-left:65%; margin-bottom: 10px;">
        下一步
      </el-button>
      <el-button type="primary"
                 v-show="active==2"
                 @click="handleCommit"
                 style="float: left; margin-left:65%; margin-bottom: 10px;">
        提交审核
      </el-button>
      <el-button type="primary"
                 v-show="active==3"
                 @click="closeDialog()"
                 style="float: left; margin-left:45%; margin-bottom: 10px;">
        完成
      </el-button>
    </div>
  </div>

</div>
</template>

<script>
  import {getDict,getAreaTree,commitRegister,validateUserName,validateOrganizingInstitutionBarCode} from "api/admin/register/index"
  import { parseValueToText ,parseTime} from "utils/index";
  import idCardNoUtil from 'utils/idCardNoUtil';
  import {CheckSocialCreditCode,postalCodeValidator} from 'utils/validate';
  export default {
      name: "register",
      props:{
        dictData:{
          type:Object
        },
        area:{
          type:Array
        }
      },
      data(){
        const userNameValidator = (rule, value, callback) => {
          validateUserName({"userName":value}).then(response => {
            if( !response){
              return callback(new Error("该用户名已被注册"));
            }else{
              callback();
            }
            })
        };
        const mobileValidator = (rule, value, callback) => {
          const mymobile =  /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
          if (value && !mymobile.test(value)) {
            return callback(new Error("电话号码格式不正确"));
          } else {
            callback();
          }
        };
        const confirmPwdValidator = (rule, value, callback) => {
          if(value && value!==this.accountForm.userPwd){
            return callback(new Error("密码不一致！"));
          }else{
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
        const organizingInstitutionBarCodeValidator = (rule, value, callback) => {
          validateOrganizingInstitutionBarCode({"organizingInstitutionBarCode":value}).then(response => {
            if( !response){
              return callback(new Error("社会信用代码重复"));
            }else{
              CheckSocialCreditCode(rule, value, callback);
            }
          })
        };
        return{
          imgFile:[],
          dialogImageVisible:false,//图片预览窗口是否显示
          dialogImageUrl:undefined,//预览图片的地址
          fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
          active:0,
          accountForm:{//注册信息基本数据
            userName:"",
            userPwd:undefined,
            userPwdConfirm:undefined,//确认密码
            realName:undefined,
            mobile:undefined,
            email:undefined
          },
          enterpriseForm:{//企业基本信息数据
            enterpriseName:undefined,
            registerAddress:undefined,
            areaId:undefined,
            productionAddress:undefined,
            organizingInstitutionBarCode:undefined,
            subjectClassification:undefined,
            corporateRepresentative:undefined,
            idCardNo:undefined,
            registeredCapital:undefined,
            economicNature:undefined,
            operationScope:undefined,
            postalCode:undefined,
            contacts:undefined,
            contactNumber:undefined,
            contactMobile:undefined,
            inspectorMobile:undefined,
            registerDate:undefined,
            areaOptions:[],
          },
          licenseForm:{//证照信息
            certificateType:undefined,
            certificateName:undefined,
            certificateNo:undefined,
            registerScope:undefined,
            issuingDate:undefined,
            issuingUnit:undefined,
            validDate:undefined,
            certificatePhoto:undefined
          },
          rules:{
            //账号信息校验规则
            userName:[{required: true, message: "请输入账号", trigger: "blur"},{validator: userNameValidator, trigger: "blur" }],
            userPwd:[{required: true, message: "请输入密码", trigger: "blur"}],
            userPwdConfirm:[{required: true, message: "请确认密码", trigger: "blur"},{ validator: confirmPwdValidator, trigger: "blur" }],
            realName:[{required: true, message: "请输入用户姓名", trigger: "blur"}],
            mobile:[{required: true, message: "请输入联系电话", trigger: "blur"},{ validator: mobileValidator, trigger: "blur" }],
            email:[{ required: true, message: '请输入邮箱地址', trigger: 'blur' },{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'}],
            //企业基本信息校验规则
            enterpriseName:[{required: true, message: "请输入企业名称", trigger: "blur"}],
            registerAddress:[{required: true, message: "请输入注册地址", trigger: "blur"}],
            areaOptions:[{required: true, message: "请选择所属区域", trigger: "blur"}],
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
    watch:{
      active(curVal,oldVal){
        if(curVal==1&&oldVal==0){
          this.$refs["form1"].validate((valid,errors) => {
            if (!valid) {
              this.active=0;
            }
          })
        }else if(curVal==2&&oldVal==1){
          this.$refs["form2"].validate((valid,errors) => {
            if (!valid) {
              this.active=1;
            }
          })
        }
      },
    },
    methods:{
      nextStep(){
        this.active++;
        if (this.active> 3){
          this.active = 3
        };
      },
      preStep(){
        this.active--;
        if (this.active< 0){
          this.active = 0
        };
      },
      //提交审核
      handleCommit(){
        let _self = this;
        _self.$refs["form3"].validate((valid,errors) => {
          if (valid) {
            _self.$confirm(`确定要提交审核吗?`, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              _self.licenseForm.validDate=parseTime(_self.licenseForm.validDate);
              _self.licenseForm.issuingDate=parseTime(_self.licenseForm.issuingDate);
              _self.enterpriseForm.registerDate=parseTime( _self.enterpriseForm.registerDate);
              let areaOptions=_self.enterpriseForm.areaOptions;
              _self.enterpriseForm.areaId=areaOptions[areaOptions.length-1];
              let param = new FormData();
              param.append("accountForm",JSON.stringify(_self.accountForm));
              param.append("enterpriseForm",JSON.stringify(_self.enterpriseForm));
              param.append("licenseForm",JSON.stringify(_self.licenseForm));
              param.append("file",_self.imgFile[0].raw);
              commitRegister(param).then(response=> {
                if(response=="success"){
                  _self.active=3;
                } else {
                  this.$message({
                    type:'error',
                    showClose: true,
                    message: `提交审核失败！`
                  });
                }
              })
            })
          }else{
            _self.active=2;
          }
        })
      },
      closeDialog(){
        this.$emit('closeDialog');
      },
      //删除图片
      handleImageRemove(file){
        this.imgFile=[];
        if( this.imgFile.length==0){
          $(".el-upload--picture-card").show();
        }
      },
      // 点击"+"图标时显示弹出框
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
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
        this.$refs["certificatePhoto"].resetField();//当添加图片后，重置图片验证功能。
        this.imgFile.push(file);
        if( this.imgFile.length>0){
          $(".el-upload--picture-card").hide();
        }
      }
    }
  }
</script>

<style scoped>
  .centre{
    width: 60%;
    margin: auto;
  }
  .title{
    border-bottom: 2px solid #d8d7d7;
    height: 50px;
    clear: both;
    overflow: hidden;
  }
  .title .title_text{
    float: left;
    padding-left: 20px;
    line-height: 50px;
    font-size: 20px;
    color: #102133;
  }
  #step{
    margin: 20px 0px;
    padding: 20px;
    background-color: white;
    overflow: hidden;
    border: 1px solid #ebebeb;
    border-radius: 3px;
    transition: .2s;
   /* background: rgba(31, 45, 61, 0.1);*/
  }
  #step_content{
    margin-top: 40px;
    min-height: 580px;
  }


</style>
