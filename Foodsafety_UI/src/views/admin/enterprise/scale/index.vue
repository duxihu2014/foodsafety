<template>
    <div class="app-container calendar-list-container">
        <div class="title">
          <h3>
            企业规模信息
            <span style="margin-left: 20px;">
              <el-tag v-if="changeType=='未变更'">{{changeType}}</el-tag>
              <el-tag v-if="changeType=='完成'" type="success">{{changeType}}</el-tag>
              <el-tag v-if="changeType=='审核中'" type="warning">{{changeType}}</el-tag>
            </span>
          </h3>
        </div>
        <div class="content">
          <el-form :model="form" label-width="200px" ref="scaleForm" :rules="rules">
              <el-form-item label="企业的规模:" prop="enterpriseScale">
                <el-select v-model="form.enterpriseScale">
                  <el-option v-for="item in  enterpriseScaleOptions" :key="item.value" :label="item.text" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="企业总人数:" prop="enterpriseEmployees">
                    <el-input v-model.number="form.enterpriseEmployees"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="专业技术人员数:" prop="enterpriseTechnicians">
                    <el-input v-model.number="form.enterpriseTechnicians"></el-input>
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
          <div class="head-box">
            <div class="item-box" style="position: absolute;left: 10px;">
              <span class="mg-r-15 head-item" style="font-weight: 700;font-size: 15px;color: #2e6be4;">
                根据国家《统计上大中小微企业划分办法》(国统字[2011]75号),餐饮行业划分标准：
              </span>
            </div>
          </div>
          <el-table :data="enterpriseScaleModel" border>
            <el-table-column align="center" prop="label" label="指标"></el-table-column>
            <el-table-column align="center" prop="unit" label="单位"></el-table-column>
            <el-table-column align="center" prop="a" label="大型"></el-table-column>
            <el-table-column align="center" prop="b" label="中性"></el-table-column>
            <el-table-column align="center" prop="c" label="小型"></el-table-column>
            <el-table-column align="center" prop="d" label="微型"></el-table-column>
          </el-table>
          <div class="foot-box">
            <div class="item-box" style="position: absolute;left: 10px;">
              <span class="mg-r-15 head-item" style="font-weight: 700;font-size: 15px;color: #2e6be4;">
                说明:大型、中型和小型企业须同时满足所列指标的下限,否则下划一档;微型企业只须满足所列指标中的一项即可。
              </span>
            </div>
          </div>
        </div>
        <div class="foot">
          <el-button  @click="reset()">重 置</el-button>
          <el-button type="primary" v-if="isCreate" :disabled="!isCommit"  @click="add()">新 增</el-button>
          <el-button type="primary" v-else :disabled="!isCommit"  @click="update()">修 改</el-button>
        </div>
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
  import { validateDecimals} from "utils/validate";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { getEnterpriseScale,getEnterpriseScaleChange,updateEnterpriseScale,addEnterpriseScale} from "api/admin/enterprise/scale/index";
    export default {
        name: "index",
      data(){
          const validateNumber=(rule, value, callback)=> {
            if (!Number.isInteger(value)) {
              callback(new Error('请输入数字值'));
            } else {
              if (value < 0|| value>99999) {
                callback(new Error('请输入正确的数值'));
              } else {
                callback();
              }
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
        return {
          enterpriseScaleModel:[
            {"label":"从业人员(X)","unit":"人","a":"X≥300","b":"100≤X＜300","c":"10≤X＜100","d":"X＜10"},
            {"label":"营业收入(Y)","unit":"万元","a":"X≥10000","b":"2000≤X＜10000","c":"100≤X＜2000","d":"X＜100"},
            ],
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
          changeVerifyForm:{reason:undefined},
          isCreate:false,
          isCommit:false,//判断是否可以提交修改或者新增
          changeDialogVisible:false,
          formStr:"",//保存原始的form表单数据字符串。
          rules:{
            enterpriseEmployees:[{required: true, message: "请输入企业总人数", trigger: "blur"},{ validator: validateNumber, trigger: "blur" }],
            enterpriseTechnicians:[{required: true, message: "请输入专业技术人员数", trigger: "blur"},{ validator: validateNumber, trigger: "blur" }],
            areaFloor:[{ validator: validateDecimals, trigger: "blur" }],
            areaBuilding:[{ validator: validateDecimals, trigger: "blur" }],
            fixedAssets:[{ validator: validateDecimals, trigger: "blur" }],
            floatingCapital:[{ validator: validateDecimals, trigger: "blur" }],
            grossAnnualOutputValue:[{ validator: validateDecimals, trigger: "blur" }],
            annualSales:[{ validator: validateDecimals, trigger: "blur" }],
            annualTaxPayment:[{ validator: validateDecimals, trigger: "blur" }],
            annualProfit:[{ validator: validateDecimals, trigger: "blur" }],
            reason:[],
          },
          changeType:'',
        }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        enterpriseScaleOptions(){
          return this.staticData["企业规模"]
        },
      },
      created(){
        this.getChanges();
        this.initEnterpriseScale();
      },
      methods:{
        initEnterpriseScale(){
          this.form.enterpriseId=this.user.enterpriseId;
          getEnterpriseScale({"enterpriseId": this.form.enterpriseId}).then((response)=>{
            if(response.length==0){//如果规模信息原始表没有数据，操作为新增，否则为修改
              this.isCreate=true;
            }else{
              this.form=response[0];
              this.formStr=JSON.stringify(this.form);
              this.isCreate=false;
            }
          })
        },
        getChanges(){
          getEnterpriseScaleChange({"enterpriseId":this.user.enterpriseId}).then((response)=>{
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
        /*新增*/
        add(){
          let _self = this;
          _self.$refs["scaleForm"].validate((valid) => {
            if (valid) {
              _self.changeDialogVisible = true;
            }
          })
        },
        /*修改*/
        update(){
          let _self = this;
          if(_self.formStr!==JSON.stringify(_self.form)){//判断表单数据是否修改，如果修改了则可以提交，否重无法提交
            _self.$refs["scaleForm"].validate((valid) => {
              if (valid) {
                _self.changeDialogVisible=true;
              }
            })
          }else{
            this.$message("您还未修改数据哦！")
          }
        },
        /*重置*/
        reset(){
          this.$refs["scaleForm"].resetFields();
          if(!this.isCreate){//修改
            this.initEnterpriseScale();
          }else{//新增
              this.form.enterpriseScale='';
              this.form.enterpriseEmployees='';
              this.form.enterpriseTechnicians='';
              this.form.areaFloor='';
              this.form.areaBuilding='';
              this.form.fixedAssets='';
              this.form.floatingCapial='';
              this.form.grossAnnualOtputValue='';
              this.form.annualSales='';
              this.form.annualTaxPayment='';
              this.form.annualProfit='';
          }

        },
        closeDialog(){
          this.$nextTick(function() {
            this.$refs['changeReasonForm'].clearValidate();
            this.getChanges();
          });
        },
        confirm(){
          this.$refs["changeReasonForm"].validate((valid) => {
            if (valid) {
              let param = new FormData();
              param.append("enterpriseScale",JSON.stringify(this.form))
              param.append("reason",this.changeVerifyForm.reason);
              if(this.isCreate){//新增
                addEnterpriseScale(param).then((response)=>{
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
              }else{//修改
                updateEnterpriseScale(param).then((response)=>{
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

            }
          })
        },
        cancel(){
          this.changeDialogVisible=false;
          this.changeVerifyForm.reason=undefined;
        },
      },

    }
</script>

<style scoped>
  .operation{
    margin: 10px 120px;
  /*  border-bottom: 1px solid #dfe6ec;*/
  }
  .title{
    text-align: center;
    margin-bottom: 30px;
  }
  .content{
    margin: 10px 120px;
  }
  .foot-box{
    position: relative;
    height: 35px;
    border-bottom: 1px solid #dfe6ec;
    border-left: 1px solid #dfe6ec;
    border-right: 1px solid #dfe6ec;
    font-size: 14px;
    color: #1f2d3d;
  }
  .foot{
    text-align: center;
    padding-top: 50px;
  }
</style>
