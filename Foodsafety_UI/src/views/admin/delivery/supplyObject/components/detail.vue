<template>
  <div>
    <el-form :model="supplyObject" :rules="rules" ref="form" label-width="120px" :disabled="dialogStatus=='view'">
      <el-row>
        <el-col :span="11">
          <el-form-item label="供应对象" prop="enterpriseName" >
            <el-autocomplete
              v-model.trim="supplyObject.enterpriseName"
              :fetch-suggestions="querySearch"
              :disabled="dialogStatus=='update'"
              @select="handleSelect">
              <i
                class="el-icon-close el-input__icon"
                slot="suffix"
                v-show="selectOption && !isView"
              @click="clear">
              </i>
            </el-autocomplete>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="社会信用代码"  prop="organizingInstitutionBarCode">
            <el-input v-model.trim="supplyObject.organizingInstitutionBarCode" :disabled="selectOption||dialogStatus=='update'"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="法人代表" prop="corporateRepresentative">
            <el-input v-model.trim="supplyObject.corporateRepresentative" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="证件号码" prop="idCardNo">
            <el-input v-model.trim="supplyObject.idCardNo" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="主体分类" prop="subjectClassification">
            <el-select  v-model="supplyObject.subjectClassification" placeholder="请选择" :disabled="selectOption">
              <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="经济性质" prop="economicNature">
            <el-select  v-model="supplyObject.economicNature" placeholder="请选择" :disabled="selectOption">
              <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="注册资金" prop="registeredCapital" >
            <el-input v-model.trim="supplyObject.registeredCapital" placeholder="注册资金" :disabled="selectOption">
              <template slot="append">(万元)</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="所属区域" prop="areaOptions">
            <el-cascader
              :disabled="selectOption"
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
            <el-date-picker type="date" placeholder="请选择" v-model.trim="supplyObject.registerDate" :disabled="selectOption"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="经营期限" prop="operatingTerm">
            <el-date-picker type="date" placeholder="请选择" v-model.trim="supplyObject.operatingTerm" :disabled="selectOption"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="企业联系人" prop="contacts">
            <el-input v-model.trim="supplyObject.contacts" placeholder="联系人" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="联系人手机号" prop="contactMobile">
            <el-input v-model.trim="supplyObject.contactMobile" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="邮政编码" prop="postalCode">
            <el-input v-model.trim="supplyObject.postalCode" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="企业邮箱" prop="email">
            <el-input v-model.trim="supplyObject.email" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model.trim="supplyObject.contactNumber" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="传真" prop="fax">
            <el-input v-model.trim="supplyObject.fax" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="生产地址" prop="productionAddress">
            <el-input v-model.trim="supplyObject.productionAddress" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="注册地址" prop="registerAddress">
            <el-input v-model.trim="supplyObject.registerAddress" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="11">
          <el-form-item label="检验员手机号" prop="inspectorMobile">
            <el-input v-model.trim="supplyObject.inspectorMobile" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="地址坐标" prop="lnglat"  tab="0">
            <el-input v-model="supplyObject.lnglat" placeholder="企业地址坐标" :disabled="selectOption" readonly>
              <i slot="suffix" class="el-icon-location" @click="setLnglat"></i>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="22">
          <el-form-item label="经营范围" prop="operationScope">
            <el-input type="textarea" :rows="2" v-model.trim="supplyObject.operationScope" placeholder="经营范围" :disabled="selectOption"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row style="text-align: center" v-show="dialogStatus!=='view'">
        <el-form-item>
          <el-button @click="reset()">重 置</el-button>
          <el-button type="primary" @click="save()" v-show="dialogStatus=='create'">保 存</el-button>
          <el-button type="primary" @click="update()" v-show="dialogStatus=='update'">保 存</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-dialog  :visible.sync="dialogVisible" size="tiny"  :close-on-click-modal="false" append-to-body>
      <div style="width: 100%;height: 500px; padding:10px 0px ">
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
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import idCardNoUtil from 'utils/idCardNoUtil';
  import {CheckSocialCreditCode,postalCodeValidator} from 'utils/validate';
  import {add,getAllEnterprise,getObj,putObj} from 'api/admin/delivery/index';
    export default {
        name: "detail",
      props:{
        supplyObjectId: {
          default: undefined
        },
        dialogStatus:{
          default: undefined
        }
      },
      data(){
        const cardNoValidator = (rule, value, callback) => {
          //15位和18位证件号码的基本校验
          if(!value) callback() ;
          let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(value);
          if(!check) return callback(new Error('请输入正确的证件号码'));
          //判断长度为15位或18位
          if(value.length==15){
            if(!idCardNoUtil.check15IdCardNo(value)){
              callback(new Error('请输入正确的证件号码'));
            }else callback();

          }else if(value.length==18){
            if(!idCardNoUtil.check18IdCardNo(value))
              callback(new Error('请输入正确的证件号码'));
            else callback();
          }else{
            callback(new Error('请输入正确的证件号码'));
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
          selectOption:false,//判断是选择企业还是录入企业（true：选择  false：录入）
          allEnterpriseArr:[],//所有可供选择的企业
          supplyObject:{
            lnglat:undefined
          },
          areaOptions:[],
          dialogVisible:false,
          /* 地图相关 */
          zoom: process.env.MAP_ZOOM, // 缩放等级
          center: process.env.CENTER_POSITION, // 初始位置
          mapEvents: { // 地图事件
            click: this.addMarker,
            zoomchange : this.zoomChange
          },
          visible: false, // 是否显示标记
          position: ["0","0"], // 标记位置
          rules:{
            enterpriseName:[{required: true, message: "请输入企业名称", trigger: "change"}],
            organizingInstitutionBarCode:[{required: true, message: "请输入社会信用代码", trigger: "blur"},{validator:CheckSocialCreditCode,trigger:'blur'}],
            registerAddress:[{required: true, message: "请输入注册地址", trigger: "blur"}],
            areaOptions:[{required: true, validator: areaValidator, trigger: "blur"}],
            productionAddress:[{required: true, message: "请输入生产地址", trigger: "blur"}],
            subjectClassification:[{required: true, message: "请选择主体分类", trigger: "blur"}],
            corporateRepresentative:[{required: true, message: "请输入法人代表", trigger: "blur"}],
            idCardNo:[{required: true, message: "请输入证件号码", trigger: "blur"},{ validator: cardNoValidator, trigger: "blur" }],
            economicNature:[{required: true, message: "请选择企业经济性质", trigger: "blur"}],
            operationScope:[{required: true, message: "请输入企业经营范围", trigger: "blur"}],
            contacts:[{required: true, message: "请输入联系人", trigger: "blur"}],
            contactNumber:[{required: true, message: "请输入联系电话", trigger: "blur"},{ validator: mobileValidator, trigger: "blur" }],
            contactMobile:[{required: true, message: "请输入联系人手机号", trigger: "blur"},{ validator: mobileValidator, trigger: "blur" }],
            inspectorMobile:[{ validator: mobileValidator, trigger: "blur" }],
            postalCode:[{ validator: postalCodeValidator, trigger: "blur" }],
            registeredCapital:[{ validator: validateDecimals, trigger: "blur" }],
            registerDate:[{required: true, message: "请输入企业注册日期", trigger: "blur"}],
            lnglat:[{required: true, message: "请输入地址坐标", trigger: "blur"}],
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
        },
      },
      created(){
        this.init();
      },
      methods:{
        zoomChange(e){ // 地图缩放事件
          this.zoom = this.$refs['map'].$$getInstance().getZoom();
        },
        addMarker(e) { // 地图单击事件
          if(!this.selectOption && this.dialogStatus!=='view') {
            this.position = [e.lnglat.lng, e.lnglat.lat];
            this.supplyObject.longitude=e.lnglat.lng;
            this.supplyObject.latitude=e.lnglat.lat;
            this.supplyObject.lnglat = e.lnglat.lng + "," + e.lnglat.lat;
            this.visible = true;
          }
        },
        init(){//数据初始化
          if(this.dialogStatus == "update"){
            this.getSupplyObject();
          }else if(this.dialogStatus== "view"){
            this.getSupplyObject();
          }else if(this.dialogStatus== "create") {
            this.getAllEnterprise();
          }
        },
        //根据供应单位id获取供应单位信息
        getSupplyObject(){
          getObj(this.supplyObjectId).then(response=>{
            this.supplyObject = response.data;
            let str = findParentNode(this.areaData,response.data.areaId);
            this.areaOptions=str.split(",");
            this.areaOptions.push(response.data.areaId+'');
            this.supplyObject.lnglat = this.supplyObject.longitude+","+  this.supplyObject.latitude;
          })
        },
        //获取所有企业
        getAllEnterprise(){
          getAllEnterprise().then(response=>{
            response.data.enterpriseData.forEach((item,index)=>{
              this.allEnterpriseArr.push({"value":item.enterpriseName,"data":item,"index":index})
            })
          })
        },
        querySearch(queryString, cb){
          var allEnterpriseName = this.allEnterpriseArr;
          var results = queryString ? allEnterpriseName.filter(this.createStateFilter(queryString)) : allEnterpriseName;
          cb(results);
        },
        handleSelect(item){
          this.$refs["form"].resetFields();
          this.selectOption=true;
          let str = findParentNode(this.areaData,item.data.areaId);
          this.areaOptions=str.split(",");
          this.areaOptions.push(item.data.areaId+'');
          this.supplyObject = Object.assign({},item.data);
          this.supplyObject.lnglat = item.data.longitude + ',' + item.data.latitude;
        },
        clear(){
          this.$refs["form"].resetFields();
          this.selectOption=false;
          this.supplyObject={};
          this.areaOptions=[];
        },
        //大写转成小写
        createStateFilter(queryString) {
          return (state) => {
            return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) >-1);
          };
        },
        save(){//新增保存
          this.$refs["form"].validate((valid) => {
            if (valid) {
              this.supplyObject.areaId=this.areaOptions[this.areaOptions.length-1];
              this.supplyObject.registerDate = parseTime( this.supplyObject.registerDate);
              if( this.supplyObject.operatingTerm ){
                this.supplyObject.operatingTerm = parseTime( this.supplyObject.operatingTerm);
              }
              this.supplyObject.lnglat=undefined;
              add(this.supplyObject).then(response=>{
                if(response.rel){
                  this.$message({
                    type:'success',
                    showClose: true,
                    message: `操作成功！`
                  });
                }else{
                  this.$message({
                    type:'error',
                    showClose: true,
                    message: `操作失败！`
                  });
                }
                this.$emit("closeDialog")
              })
            }
          });
        },
        update(){//修改保存
          this.$refs["form"].validate((valid) => {
            if (valid) {
              this.supplyObject.areaId=this.areaOptions[this.areaOptions.length-1];
              this.supplyObject.registerDate = parseTime( this.supplyObject.registerDate);
              if( this.supplyObject.operatingTerm ){
                this.supplyObject.operatingTerm = parseTime( this.supplyObject.operatingTerm);
              }
              this.supplyObject.lnglat=undefined;
              putObj(this.supplyObject.supplyObjectId,this.supplyObject).then(response=>{
                if(response.rel){
                  this.$message({
                    type:'success',
                    showClose: true,
                    message: `操作成功！`
                  });
                }else{
                  this.$message({
                    type:'error',
                    showClose: true,
                    message: `操作失败！`
                  });
                }
                this.$emit("closeDialog")
              })
            }
          });
          // console.log(this.supplyObject)
        },
        reset(){
          if(this.dialogStatus == "view"){
            this.supplyObject={};
            this.areaOptions=[];
            this.selectOption=false;
            this.$refs["form"].resetFields();
          }else if(this.dialogStatus == "update"){
            this.$refs["form"].resetFields();
            this.getSupplyObject();
          }

        },
        setLnglat(){
          this.dialogVisible=true;
          if(this.supplyObject.lnglat){
            this.position = [this.supplyObject.longitude, this.supplyObject.latitude];
            this.center = [this.supplyObject.longitude, this.supplyObject.latitude];
            this.visible = true;
          }
        },
      },
    }
</script>

<style scoped>

</style>
