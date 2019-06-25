<template>
  <div>
    <el-row>
      <el-col>
        <p style="text-align: center"  >企业委托加工备案信息</p>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <el-card class="box-card">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="企业信息">
              <div class="enterprisInfo-div">
                <fieldset class="fieldset">
                  <legend class="enterpriseInfo-legend" >委托企业信息</legend>
                  <el-form  :model="clientEnterprise" label-width="120px" style="margin: 20px 0px" :disabled="formEdit1" >
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="企业名称" prop="enterpriseName">
                          <el-autocomplete
                            v-model.trim="clientEnterprise.enterpriseName"
                            :fetch-suggestions="querySearch"
                            @select="handleSelect1">
                            <i
                              class="el-icon-close el-input__icon"
                              slot="suffix"
                              v-show="!formEdit1"
                              @click="clear1">
                            </i>
                          </el-autocomplete>

                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
                          <el-input v-model.trim="clientEnterprise.organizingInstitutionBarCode" :clearable="!formEdit1" ></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="法人代表" prop="corporateRepresentative">
                          <el-input v-model.trim="clientEnterprise.corporateRepresentative" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="身份证" prop="idCardNo">
                          <el-input v-model.trim="clientEnterprise.idCardNo" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="经济性质" prop="economicNature">
                          <el-select  v-model="clientEnterprise.economicNature" placeholder="请选择" :clearable="!formEdit1">
                            <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="主体分类" prop="subjectClassification">
                          <el-select  v-model="clientEnterprise.subjectClassification" placeholder="请选择" :clearable="!formEdit1">
                            <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="注册资金" prop="registeredCapital">
                          <el-input v-model.trim="clientEnterprise.registeredCapital" :clearable="!formEdit1">
                            <template slot="append">(万元)</template>
                          </el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="注册日期" prop="registerDate">
                          <el-date-picker type="date" placeholder="请选择" v-model.trim="clientEnterprise.registerDate" :clearable="!formEdit1"></el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="注册地址" prop="registerAddress">
                          <el-input v-model.trim="clientEnterprise.registerAddress" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="所属区域" prop="areaOptions1">
                          <el-cascader
                            :clearable="!formEdit1"
                            v-model="areaOptions1"
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
                        <el-form-item label="生产地址" prop="productionAddress">
                          <el-input v-model.trim="clientEnterprise.productionAddress" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="经营期限" prop="operatingTerm">
                          <el-date-picker type="date" placeholder="请选择" v-model.trim="clientEnterprise.operatingTerm" :clearable="!formEdit1"></el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="邮政编码" prop="postalCode">
                          <el-input v-model.trim="clientEnterprise.postalCode" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="电子邮件" prop="email">
                          <el-input v-model.trim="clientEnterprise.email" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="联系人" prop="contacts">
                          <el-input v-model.trim="clientEnterprise.contacts" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="联系电话" prop="contactNumber">
                          <el-input v-model.trim="clientEnterprise.contactNumber" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="手机号" prop="contactMobile">
                          <el-input v-model.trim="clientEnterprise.contactMobile" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="传真" >
                          <el-input v-model.trim="clientEnterprise.fax" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="22">
                        <el-form-item label="经营范围" prop="operationScope">
                          <el-input type="textarea" :rows="2" v-model.trim="clientEnterprise.operationScope" :clearable="!formEdit1"></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-form>
                </fieldset>
                <fieldset class="fieldset">
                  <legend class="enterpriseInfo-legend">被委托企业信息</legend>
                  <el-form  :model="consigneeEnterprise" label-width="120px" style="margin: 20px 0px">
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="企业名称" prop="enterpriseName">
                          <el-autocomplete
                            v-model.trim="consigneeEnterprise.enterpriseName"
                            :fetch-suggestions="querySearch"
                            @select="handleSelect2">
                            <i
                              class="el-icon-close el-input__icon"
                              slot="suffix"
                              @click="clear2">
                            </i>
                          </el-autocomplete>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
                          <el-input v-model.trim="consigneeEnterprise.organizingInstitutionBarCode" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="法人代表" prop="corporateRepresentative">
                          <el-input v-model.trim="consigneeEnterprise.corporateRepresentative" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="身份证" prop="idCardNo">
                          <el-input v-model.trim="consigneeEnterprise.idCardNo" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="经济性质" prop="economicNature">
                          <el-select  v-model="consigneeEnterprise.economicNature" placeholder="请选择" clearable>
                            <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="主体分类" prop="subjectClassification">
                          <el-select  v-model="consigneeEnterprise.subjectClassification" placeholder="请选择" clearable>
                            <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                          </el-select>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="注册资金" prop="registeredCapital">
                          <el-input v-model.trim="consigneeEnterprise.registeredCapital" clearable>
                            <template slot="append">(万元)</template>
                          </el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="注册日期" prop="registerDate">
                          <el-date-picker type="date" placeholder="请选择" v-model.trim="consigneeEnterprise.registerDate" clearable></el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="注册地址" prop="registerAddress">
                          <el-input v-model.trim="consigneeEnterprise.registerAddress" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="所属区域" prop="areaOptions2">
                          <el-cascader
                            clearable
                            v-model="areaOptions2"
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
                        <el-form-item label="生产地址" prop="productionAddress">
                          <el-input v-model.trim="consigneeEnterprise.productionAddress" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="经营期限" prop="operatingTerm">
                          <el-date-picker type="date" placeholder="请选择" v-model.trim="consigneeEnterprise.operatingTerm" clearable></el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="邮政编码" prop="postalCode">
                          <el-input v-model.trim="consigneeEnterprise.postalCode" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="电子邮件" prop="email">
                          <el-input v-model.trim="consigneeEnterprise.email" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="联系人" prop="contacts">
                          <el-input v-model.trim="consigneeEnterprise.contacts" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="联系电话" prop="contactNumber">
                          <el-input v-model.trim="consigneeEnterprise.contactNumber" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="11">
                        <el-form-item label="手机号" prop="contactMobile">
                          <el-input v-model.trim="consigneeEnterprise.contactMobile" clearable></el-input>
                        </el-form-item>
                      </el-col>
                      <el-col :span="11">
                        <el-form-item label="传真" >
                          <el-input v-model.trim="consigneeEnterprise.fax" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :span="22">
                        <el-form-item label="经营范围" prop="operationScope">
                          <el-input type="textarea" :rows="2" v-model.trim="consigneeEnterprise.operationScope" clearable></el-input>
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </el-form>
                </fieldset>
              </div>
            </el-tab-pane>
            <el-tab-pane label="委托合同信息">
              <el-form :model="commissionContract" label-width="120px" style="margin: 20px 0px">
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="委托合同编号">
                      <el-input   v-model.trim="commissionContract.contractCode" clearable></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="合同签订日期">
                      <el-date-picker type="date" placeholder="请选择" v-model.trim="commissionContract.contractDate" clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="合同总产值">
                      <el-input   v-model.trim="commissionContract.yield" clearable>
                        <template slot="append">(万元)</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="合同总产量">
                      <el-input   v-model.trim="commissionContract.output" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="合同生效日期">
                      <el-date-picker type="date" placeholder="请选择" v-model.trim="commissionContract.effectiveDate" clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="合同期限">
                      <el-date-picker type="date" placeholder="请选择" v-model.trim="commissionContract.contractPeriod" clearable></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="申请受理单位">
                      <el-input   v-model.trim="commissionContract.acceptId" clearable></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="电子文本上传">
              <el-form>
                <el-row>
                  <el-col :span="6" >
                    <el-form-item>
                      <el-upload
                        class="upload-demo"
                        action="https://jsonplaceholder.typicode.com/posts/"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :before-remove="beforeRemove"
                        :on-change="handleChange"
                        multiple
                        :limit="3"
                        :auto-upload="false"
                        :file-list="fileList">
                        <el-button size="small" type="primary">上传电子文本<i class="el-icon-upload el-icon--right"></i></el-button>
                      </el-upload>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="委托产品信息">
              <el-form  label-width="120px" style="margin: 20px 0px">
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="许可证编号" prop="licenseNo">
                      <el-autocomplete
                        popper-class="my-autocomplete"
                        v-model.trim="commissionProduct.licenseNo"
                        :fetch-suggestions="querySearch3"
                        @select="handleSelect3">
                        <i
                          class="el-icon-close el-input__icon"
                          slot="suffix"
                          @click="clear3">
                        </i>
                        <template slot-scope="{ item }">
                          <div class="licenseNo" >{{ item.value }}</div>
                          <span class="productName">{{ item.productName }}</span>
                        </template>
                      </el-autocomplete>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="产品名称" prop="productName">
                      <el-input v-model.trim="commissionProduct.productName"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="产品类型" prop="productName">
                      <el-input v-model.trim="commissionProduct.productName"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="产品规格型号" prop="specificationModel">
                      <el-input v-model="commissionProduct.specificationModel"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="11">
                    <el-form-item label="产品注册商标">
                      <el-input v-model="commissionProduct.registeredTrademark"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="11">
                    <el-form-item label="产品名称">
                      <el-input v-model="commissionProduct.productName"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <div class="setting-btn">
      <el-button size="medium">重 置</el-button>
      <el-button type="primary" size="medium" @click="save()">保 存</el-button>
    </div>

  </div>
</template>

<script>
  import store from 'store';
  import {getProductDetail} from 'api/admin/enterprise/index';
  import {save,getEnterprise} from 'api/admin/commission/index';
  import {getEnterpriseBase}from "api/admin/enterprise/base/index";
  import { mapGetters } from "vuex";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  export default {
    name: "detail",
    data(){
      return{
        tabPosition:"0",
        selectOption1:false,//判断委托方企业是选择的还是录入的（true：选择的；false：录入的）
        selectOption2:false,//判断被委托方企业是选择的还是录入的（true：选择的；false：录入的）
        selectOption3:false,
        areaOptions1:[],
        areaOptions2:[],
        formEdit1:false,//
        formEdit2:false,
        allEnterpriseArr:[],//保存可供筛选的企业
        allProductArr:[],//保存可供筛选的产品
        clientEnterprise:{//委托方
          enterpriseId:undefined
        },
        consigneeEnterprise:{//被委托方

        },
        commissionContract:{//合同信息

        },
        commissionProduct:{//委托加工产品

        },
        fileList:[],
      }
    },
    watch:{
      enterpriseId(newValue,oldValue){
        if(newValue){
          this.getEnterpriseProduct(newValue)
        }else{
          this.allProductArr=[];
        }
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
      enterpriseId(){
        return this.clientEnterprise.enterpriseId;
      }
    },
    mounted(){
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function () {
        this.setHeight()
      });
    },
    created(){
      this.getAllEnterprise();
      this.getClientEnterprise();
    },
    methods:{
      //获取所有企业
      getAllEnterprise(){
        getEnterprise().then(response=>{
          response.data.enterpriseData.forEach((item,index)=>{
            this.allEnterpriseArr.push({"value":item.enterpriseName,"data":item,"index":index})
          })
        })
      },
      //根据企业id获取
      getEnterpriseProduct(value){
        this.allProductArr=[];
        getProductDetail(value).then(response=>{
          response.data.forEach((item,index)=>{
            this.allProductArr.push({"value":item.licenseNo,"data":item,"productName":item.productName})
          })
            this.commissionProduct=response.data;
        })
      },
      //委托方企业(如果登陆的用户是企业用户，则委托方企业则是该企业)
      getClientEnterprise(){
        if(this.user.enterpriseId){
          this.formEdit1=true;
          getEnterpriseBase({"enterpriseId":this.user.enterpriseId}).then((response)=>{
            this.clientEnterprise=response.data;
            let str = findParentNode(this.areaData,this.clientEnterprise.areaId);
            this.areaOptions1=str.split(",");
            this.areaOptions1.push(this.clientEnterprise.areaId+'');
          });
        }
      },
      querySearch(queryString, cb){
        var allEnterpriseName = this.allEnterpriseArr;
        var results = queryString ? allEnterpriseName.filter(this.createStateFilter(queryString)) : allEnterpriseName;
        cb(results);
      },
      querySearch3(queryString, cb){
        var allProductArr = this.allProductArr;
        var results = queryString ? allProductArr.filter(this.createStateFilter(queryString)) : allProductArr;
        cb(results);
      },
      clear1(){
        if(this.selectOption1){
          this.clientEnterprise={};
        }else{
          this.clientEnterprise.enterpriseName=undefined;
        }
        this.selectOption1=false;
      },
      clear2(){
        if(this.selectOption2){
          this.consigneeEnterprise={};
        }else{
          this.consigneeEnterprise.enterpriseName=undefined;
        }
        this.selectOption2=false;
      },
      clear3(){
        if(this.selectOption3){
          this.commissionProduct={};
        }else{
          this.commissionProduct.licenseNo=undefined;
        }
        this.selectOption3=false;
      },
      handleSelect1(item){
        this.clientEnterprise=item.data;
        this.selectOption1=true;
      },
      handleSelect2(item){
        this.consigneeEnterprise=item.data;
        this.selectOption2=true;
      },
      handleSelect3(item){
        this.commissionProduct=item.data;
        this.selectOption3=true;
      },
      //大写转成小写
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) >-1);
        };
      },
      //设置高度和透明滚动条
      setHeight(){
        let height = store.getters.screenHeight;
        $(".box-card").css({"max-height":height-175+'px'});
        $('.enterprisInfo-div').css({"max-height":height-255+'px'});
        $('.enterprisInfo-div').slimScroll({
          width: '100%',
          height: '100%',
          color: 'transparent',
          railColor: 'transparent',
          allowPageScroll:false,
          railVisible: true,
          alwaysVisible: true
        });
      },
      handleRemove(file, fileList) {
        // console.log(file, fileList);
      },
      handlePreview(file) {
        window.open(file.url);
      },
      handleChange(file){
        this.fileList.push(file)
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      save(){
        this.clientEnterprise.areaId=this.areaOptions1[this.areaOptions1.length-1];
        this.consigneeEnterprise.areaId=this.areaOptions2[this.areaOptions2.length-1];
        let param = new FormData();
        param.append("clientEnterprise",JSON.stringify(this.clientEnterprise));
        param.append("consigneeEnterprise",JSON.stringify(this.consigneeEnterprise));
        param.append("commissionContract",JSON.stringify(this.commissionContract));
        param.append("commissionContractProduct",JSON.stringify({}));
        this.fileList.forEach(item=>{
          param.append("files",item.raw);
        })
        save(param).then(respon=>{
          // console.log(respon)
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" >
  .box-card{
    margin: 0px 30px;
  }
  .enterprisInfo-div{
    max-height: 550px;
    overflow: auto;
  }
  .enterpriseInfo-legend{
    margin-left: 20px;
    padding: 0 10px;
    color: #303133cc;
    background-color: #66b1ffa3;
    font-size: 14px;
    border: 1px #bbbbbb solid
  }
  .setting-btn{
    position: absolute;
    left:50%;
    bottom:20px;
  }
  .my-autocomplete {
    li {
      line-height: normal;
      padding: 7px;
      .licenseNo {
        text-overflow: ellipsis;
        overflow: hidden;
      }
      .productName {
        font-size: 12px;
        color: #b4b4b4;
      }
      .highlighted .productName {
        color: #ddd;
      }
    }
  }
</style>

