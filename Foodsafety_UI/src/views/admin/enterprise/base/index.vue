<template>
    <div class="app-container">
      <el-row class="base-container" style="height: 100%">
        <el-col :span="16">
          <div class="title">
            <h3>
              企业基本信息
              <span style="margin-left: 20px;" v-if="enterpriseBase.superviseClassification">
              <el-tag v-if="enterpriseBase.superviseClassification=='1'">等级：A</el-tag>
              <el-tag v-else-if="enterpriseBase.superviseClassification=='2'">等级：B</el-tag>
              <el-tag  v-else-if="enterpriseBase.superviseClassification=='3'">等级：C</el-tag>
              <el-tag  v-else-if="enterpriseBase.superviseClassification=='4'">等级：D</el-tag>
            </span>
              <span style="margin-left: 10px">
              <el-tag v-if="changeType=='未变更'">{{changeType}}</el-tag>
              <el-tag v-if="changeType=='完成'" type="success">{{changeType}}</el-tag>
              <el-tag v-if="changeType=='审核中'" type="warning">{{changeType}}</el-tag>
            </span>
            </h3>
          </div>
          <el-form :model="enterpriseBase" :rules="rules" ref="baseForm" label-width="120px">
            <el-row>
              <el-col :span="11">
                <el-form-item label="企业名称" prop="enterpriseName">
                  <el-input v-model.trim="enterpriseBase.enterpriseName"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="社会信用代码" >
                  <el-input v-model.trim="enterpriseBase.organizingInstitutionBarCode" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="企业法人" prop="corporateRepresentative">
                  <el-input v-model.trim="enterpriseBase.corporateRepresentative"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="身份证" prop="idCardNo">
                  <el-input v-model.trim="enterpriseBase.idCardNo"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="主体分类" prop="subjectClassification">
                  <el-select  v-model="enterpriseBase.subjectClassification" placeholder="请选择" >
                    <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="经济性质" prop="economicNature">
                  <el-select  v-model="enterpriseBase.economicNature" placeholder="请选择">
                    <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="注册资金" prop="registeredCapital">
                  <el-input v-model.trim="enterpriseBase.registeredCapital" placeholder="注册资金" >
                    <template slot="append">(万元)</template>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
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
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="注册日期" prop="registerDate">
                  <el-date-picker type="date" placeholder="请选择" v-model.trim="enterpriseBase.registerDate"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="经营期限" prop="operatingTerm">
                  <el-date-picker type="date" placeholder="请选择" v-model.trim="enterpriseBase.operatingTerm"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="企业联系人" prop="contacts">
                  <el-input v-model.trim="enterpriseBase.contacts" placeholder="联系人"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="联系人手机号" prop="contactMobile">
                  <el-input v-model.trim="enterpriseBase.contactMobile"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="邮政编码" prop="postalCode">
                  <el-input v-model.trim="enterpriseBase.postalCode"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="企业邮箱" prop="email">
                  <el-input v-model.trim="enterpriseBase.email"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="联系电话" prop="contactNumber">
                  <el-input v-model.trim="enterpriseBase.contactNumber"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="传真" prop="fax">
                  <el-input v-model.trim="enterpriseBase.fax"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="生产地址" prop="productionAddress">
                  <el-input v-model.trim="enterpriseBase.productionAddress"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="注册地址" prop="registerAddress">
                  <el-input v-model.trim="enterpriseBase.registerAddress"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="11">
                <el-form-item label="检验员手机号" prop="inspectorMobile">
                  <el-input v-model.trim="enterpriseBase.inspectorMobile"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="11">
                <el-form-item label="地址坐标" prop="lnglat"  tab="0">
                  <el-input v-model="enterpriseBase.lnglat" placeholder="企业地址坐标" :readonly="true">
                    <i slot="suffix" class="el-icon-location" ></i>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row >
              <el-col :span="22">
                <el-form-item label="经营范围" prop="operationScope">
                  <el-input type="textarea" :rows="2" v-model.trim="enterpriseBase.operationScope" placeholder="经营范围" :clearable="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div style="text-align: center;padding-top: 10px">
            <el-button @click="resetForm('baseForm')">重 置</el-button>
            <el-button type="primary" :disabled="changeType=='审核中'" @click="commitForm()">修 改</el-button>
          </div>
        </el-col>
        <el-col :span="8" >
          <el-row style="height: 75%;">
            <el-col>
              <div style="width: 100%;height: 100%; margin:3px 0px ">
                <el-amap
                  ref="map"
                  :center="center"
                  :zoom="zoom"
                  :events="mapEvents">
                  <!-- 标记组件 -->
                  <el-amap-marker
                    :position="position"
                    :visible="visible"
                  ></el-amap-marker>
                </el-amap>
              </div>
            </el-col>
          </el-row>
          <el-row style="height:25%;">
            <el-col>
              <div id="qrcode"></div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      <el-dialog   width="40%" :visible.sync="changeDialogVisible" @close="closeDialog">
        <div >
          <el-form :model="changeVerifyForm"  :rules="rules" ref="changeReasonForm">
            <el-form-item label="修改理由:" prop="reason">
              <el-input type="textarea" :rows="5" v-model.trim="changeVerifyForm.reason"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="cancel()">取 消</el-button>
            <el-button type="primary"  @click="confirm()">确 认</el-button>
      </span>
      </el-dialog>
    </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import QRCode from 'qrcodejs2';
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import {getUserEnterprise,getEnterpriseBase,changeEnterpriseBase,getEnterpriseBaseChange}from "api/admin/enterprise/base/index";
  import idCardNoUtil from 'utils/idCardNoUtil';
  import {postalCodeValidator} from 'utils/validate';
    export default {
        name: "index",
      data(){
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
        const mobileValidator = (rule, value, callback) => {
          const mymobile =  /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
          if (value && !mymobile.test(value)) {
            return callback(new Error("电话号码格式不正确"));
          } else {
            callback();
          }
        };
        const validateDecimals =(rule, value, callback)=>{
          if(!value){callback()}
          let reg = /^(([1-9]{1}\d*)|0)(\.\d{1})?$/;
          if (!reg.test(value)  ) {
            callback(new Error('请输入正确的数值'))
          }else{
            if (value < 0|| value>9999999.9) {
              callback(new Error('请输入正确的数值'));
            } else {
              callback();
            }
          }
        };
        const areaValidator= (rule, value, callback) => {
          if(this.areaOptions.length<1){
            callback(new Error('请选择所属区域'));
          }else{
            callback();
          }
        };
          return{
            changeType:'',
            formStr:"",//保存原始的form表单数据字符串。
            enterpriseId:undefined,
            enterpriseBase:{
              lnglat:undefined
            },
            changeVerifyForm:{reason:undefined},
            areaOptions:[],
            QR_image:require('assets/QR.jpg' ),//二维码图片
            /* 地图相关 */
            zoom: process.env.MAP_ZOOM, // 缩放等级
            center: process.env.CENTER_POSITION, // 初始位置
            mapEvents: { // 地图事件
             // click: this.addMarker,
              zoomchange : this.zoomChange
            },
            visible: false, // 是否显示标记
            position: ["0","0"], // 标记位置
            markEvents: { // 标记事件
              dragend: this.markerDragend
            },
            changeDialogVisible:false,
            rules:{
              enterpriseName:[{required: true, message: "请输入企业名称", trigger: "blur"}],
              registerAddress:[{required: true, message: "请输入注册地址", trigger: "blur"}],
              areaOptions:[{required: true, validator: areaValidator, trigger: "blur"}],
              productionAddress:[{required: true, message: "请输入生产地址", trigger: "blur"}],
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
              registeredCapital:[{ validator: validateDecimals, trigger: "blur" }],
              registerDate:[{required: true, message: "请输入企业注册日期", trigger: "blur"}],
              reason:[{required: true, message: "请输入修改理由", trigger: "blur"}]
            },
          }
      },
      computed: {
        ...mapGetters(["user","staticData","areaData"]),
        subjectClassificationOptions(){
          return this.staticData["企业主体分类"];
        },
        economicNatureOptions(){
          return this.staticData["企业经济性质"];
        }
      },
      created(){
        this.getChanges();
        this.initEnterpriseBase();
      },
      mounted(){
          //设置中间容器的高度
        $(".app-container").css({"height":$(window).height()-130+"px"});
        this.qrcode();
      },
      methods:{
          initEnterpriseBase(){
            this.enterpriseId=this.user.enterpriseId;
            getEnterpriseBase({"enterpriseId":this.enterpriseId}).then((response)=>{
              this.enterpriseBase=response.data;
              this.formStr=JSON.stringify(response.data);
              if(this.enterpriseBase.latitude&&this.enterpriseBase.longitude){
                this.enterpriseBase.lnglat = this.enterpriseBase.longitude + ',' + this.enterpriseBase.latitude;
                this.position = [this.enterpriseBase.longitude, this.enterpriseBase.latitude];
                this.center = [this.enterpriseBase.longitude, this.enterpriseBase.latitude];
                this.visible = true;
              }else{
                this.position = ["0","0"];
                this.center = process.env.CENTER_POSITION;
                this.zoom = process.env.MAP_ZOOM
                this.visible = false;
              }
              let str = findParentNode(this.areaData,this.enterpriseBase.areaId);
              this.areaOptions=str.split(",");
              this.areaOptions.push(this.enterpriseBase.areaId+'');
            }).catch(()=>{
            })
          },
        getChanges(){
          getEnterpriseBaseChange({"enterpriseId":this.enterpriseId}).then((response)=>{
            let changes = response.data;
            if(changes.length>0){
              if(changes[0].verifyStatus=="2"||changes[0].verifyStatus=="3"){
                this.changeType="完成";
                this.isCommit=true;
              }else{
                this.changeType="审核中";
                this.isCommit=false;
              }
            }else{
              this.changeType="未变更";
              this.isCommit=true;
            }
          })
        },
        //重置表单修改数据
        resetForm(formName){
          this.$refs[formName].resetFields();
          this.initEnterpriseBase();
        },
        commitForm(){
            let _self = this;
          _self.enterpriseBase.areaId=Number(_self.areaOptions[_self.areaOptions.length-1]);//注意要转成数字类型
            if(_self.formStr!==JSON.stringify(_self.enterpriseBase)){//判断表单数据是否修改，如果修改了则可以提交，否重无法提交
              _self.$refs["baseForm"].validate((valid) => {
                if (valid) {
                  _self.enterpriseBase.registerDate = parseTime(_self.enterpriseBase.registerDate);
                  if(_self.enterpriseBase.operatingTerm ){
                    _self.enterpriseBase.operatingTerm = parseTime(_self.enterpriseBase.operatingTerm);
                  }
                  _self.changeDialogVisible=true;
                }
              })
            }else{
              this.$message("您还未修改数据哦！")
            }

        },
        zoomChange(e){ // 地图缩放事件
          this.zoom = this.$refs['map'].$$getInstance().getZoom();
        },
        addMarker(e) { // 地图单击事件
          this.position = [e.lnglat.lng, e.lnglat.lat];
          this.enterpriseBase.lnglat = e.lnglat.lng+","+ e.lnglat.lat;
          this.visible = true;
        },
        markerDragend(e) { // 标记拖动事件
          this.position = [e.lnglat.lng, e.lnglat.lat];
          this.enterpriseBase.lnglat = e.lnglat.lng+","+ e.lnglat.lat;
        },
        cancel(){
          this.changeDialogVisible=false;
          this.changeVerifyForm.reason=undefined;
        },
        confirm(){
          this.$refs["changeReasonForm"].validate((valid) => {
            if (valid) {
              let param = new FormData();
              param.append("enterpriseBase",JSON.stringify(this.enterpriseBase))
              param.append("reason",this.changeVerifyForm.reason);
              changeEnterpriseBase(param).then((response)=>{
                this.changeDialogVisible=false;
                if(response.rel){
                  this.$message({
                    type:'success',
                    showClose: true,
                    message: `操作成功！`
                  });
                }else {
                  this.$message({
                    type:'error',
                    showClose: true,
                    message: `操作失败！`
                  });
                }
              })
            }
          })
        },
        closeDialog(){
          this.$nextTick(function() {
            this.getChanges();
            this.$refs['changeReasonForm'].clearValidate();
          });
        },
        qrcode () {
          new QRCode('qrcode', {
            text:this.user.enterpriseId+"", // 二维码内容
            //render: 'canvas', // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
            colorDark : "#000",
            colorLight : "#fff",
          })
        }
      }
    }
</script>

<style >
  .app-container{
    position: relative;
  }
  .base-container .el-col{
    height: 100%;
  }

  .title{
    text-align: center;
    margin-bottom: 10px;
  }
  #qrcode {
    display: inline-block;
    width: 100%;
    height: 100%;
  }
  #qrcode>img {
    width: 200px;
    height:200px;
    background-color: #fff;
    padding: 6px;
    margin: auto;
  }
</style>
