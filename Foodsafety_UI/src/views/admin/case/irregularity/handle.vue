<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>

          <el-form-item label="状态" prop="caseStatus" class="filter-item">
            <el-select v-model="listQuery.caseStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  caseStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <div class="head-box">
      <div class="item-box">
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
      </div>
    </div>
    <el-table   :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName"></el-table-column>
      <el-table-column align="center" label="产品分类" width="200" prop="productType" :formatter="productTypeFormatter"></el-table-column>
      <el-table-column align="center" label="产品名称" width="150" prop="productName"></el-table-column>
      <el-table-column align="center" label="立案日期" width="200" prop="caseDate" ></el-table-column>
      <el-table-column align="center" label="案件流程状态" width="200" prop="caseStatus" :formatter="statusFormatter"></el-table-column>
      <el-table-column  align="center" fixed="right" label="操作" width="250"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" v-if="scope.row.caseStatus==1" type="warning" @click="handlePunish(scope.row)">处罚</el-button>
          <el-button size="mini" v-if="scope.row.caseStatus<3" type="success" @click="handleClose(scope.row)">结案</el-button>
        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="250"  v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary"  @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" v-if="scope.row.caseStatus==1" type="warning" @click="handlePunish(scope.row)">处罚</el-button>
          <el-button size="mini" v-if="scope.row.caseStatus<3" type="success" @click="handleClose(scope.row)">结案</el-button>
        </template>
      </el-table-column>

    </el-table>
    <div v-show="!listLoading" class="pagination-container" >
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '创建'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '编辑'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
        <el-tabs  v-model="tabPosition">
          <el-tab-pane label="基本信息" >
            <el-form :model="form" :rules="filingRules" ref="form" label-width="100px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="涉案企业" prop="enterpriseId">
                  <el-select v-model="form.enterpriseId" placeholder="请选择" clearable filterable @change="productTypeChange"  style="width:100%"  :disabled="filingReadOnly">
                    <el-option v-for="item in  enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId"  > </el-option>
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="立案日期" prop="caseDate">
                  <el-date-picker v-model="form.caseDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions" style="width:100%"  :disabled="filingReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="产品类型:" prop="productType" >
                  <el-cascader v-model="form.productType" :disabled="filingReadOnly" :options="productTypeOptions"  style="width:100%"  :show-all-levels="true" expand-trigger="hover" :change-on-select="false" @change="productTypeChange" :props="{label:'text', value: 'id'}" >
                  </el-cascader>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="涉案产品" prop="productId">
                  <el-select v-model="form.productId" placeholder="请选择" clearable filterable  style="width:100%" :disabled="filingReadOnly">
                    <el-option v-for="item in  products" :key="item.productId" :label="item.productName" :value="item.productId" > </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="案由" prop="caseCause">
                  <el-input v-model="form.caseCause" :disabled="filingReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="涉案地点" prop="address">
                  <el-input v-model="form.address"  :disabled="filingReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="违法类型" prop="illegalType">
                  <el-input v-model="form.illegalType"  :disabled="filingReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="涉案金额" prop="amount">
                  <el-input v-model="form.amount"  :disabled="filingReadOnly">
                    <template slot="append">(元)</template>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="有无伤亡" prop="haveCasualty">
                  <el-radio-group v-model="form.haveCasualty"  :disabled="filingReadOnly">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="违法行为" prop="illegalActivities">
                  <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="form.illegalActivities"  :disabled="filingReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="违法事实" prop="illegalFact">
                  <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="form.illegalFact"  :disabled="filingReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            </el-form>

          </el-tab-pane>
          <el-tab-pane label="处罚情况" v-if="punishShow">
            <el-form :model="punishform" :rules="punishRules" ref="punishform" label-width="180px">
              <el-form-item label="行政处罚内容" prop="content">
                <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="punishform.content"  :disabled="punishReadOnly"></el-input>
              </el-form-item>
              <el-form-item label="行政处罚决定书下达时间" prop="punishmentDate">
                <el-date-picker v-model="punishform.punishmentDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :disabled="punishReadOnly"></el-date-picker>
              </el-form-item>
              <el-form-item label="处罚款到位情况" prop="fineSituation">
                <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="punishform.fineSituation"   :disabled="punishReadOnly"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>

          <el-tab-pane label="结案情况"  v-if="closeShow">
            <el-form :model="closeform" :rules="closeRules" ref="closeform" label-width="100px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="承办单位" prop="undertakeUnit">
                    <el-input   v-model="closeform.undertakeUnit"   :disabled="closeReadOnly"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="处理结果" prop="processingResult">
                    <el-select v-model="closeform.processingResult" placeholder="请选择" clearable :disabled="closeReadOnly">
                      <el-option v-for="item in  resultStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="是否结案" prop="isClose">
                    <el-radio-group v-model="closeform.isClose" :disabled="closeReadOnly">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="结案时期" prop="closeDate">
                    <el-date-picker v-model="closeform.closeDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions" :disabled="closeReadOnly" ></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="移送单位" prop="transferUnit">
                    <el-input   v-model="closeform.transferUnit" :disabled="closeReadOnly" ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="移送原因" prop="transferReason">
                    <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="closeform.transferReason" :disabled="closeReadOnly"  ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="移送日期" prop="transferDate">
                    <el-date-picker v-model="closeform.transferDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择"  :disabled="closeReadOnly"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>

        </el-tabs>

      <div slot="footer" class="dialog-footer" v-if="manager_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button   type="primary" @click="save()">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>

  import { page, getObj,saveIrregularity,saveCaseClose,saveCasePunish} from "api/admin/irregularity/index";
  import { parseValueToText } from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import {getToken} from 'utils/auth';
  import queryConditions from "components/QueryConditions/index";
  import { validateDecimals} from "utils/validate";
  import {  fetchProductTypeTree,fetchAllProductTypeMap} from "api/admin/enterprise/product/index";
  import {getEnterprise} from 'api/admin/enterprise/index';
  import { page as getProducts } from "api/admin/enterprise/product/index";

  export default {
    name: "irregularity",
    components: {
      queryConditions
    },
    data(){
      return{
        height:undefined,
        list: null,
        total: null,
        listLoading: false,
        listQuery: {
          page: 1,
          limit: 20,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined,
          caseStatus:undefined
        },
        dialogFormVisible: false,
        filingReadOnly:false,//立案信息只读
        punishReadOnly:false,//处罚信息只读
        closeReadOnly:false,//结案信息只读
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        manager_update:true,
        selection: [],
        needFixedRight:false,
        tabPosition: "0",
        enterpriseOptions:[],
        form:{ haveCasualty:0, },
        products:[],
        productTypeOptions: [],
        filingRules:{
          enterpriseId:[{ required: true, message: "请选择企业", trigger: "blur" }],
          productType:[{ required: true, message: "请选择产品类型", trigger: "blur" }],
          productId:[{ required: true, message: "请选择产品", trigger: "blur" }],
          haveCasualty:[{ required: true, message: "请选择有无人员伤亡", trigger: "blur" }],
          illegalType:[{ required: true, message: "请输入违法类型", trigger: "blur" }],
          caseCause:[{ required: true, message: "请输入案由", trigger: "blur" }],
          caseDate:[{ required: true, message: "请输入立案日期", trigger: "blur" }],
          amount:[{ validator:validateDecimals, message: "请输入正确的金额", trigger: "blur" }]
        },
        punishform:{content:'',punishmentDate:undefined,fineSituation:undefined},
        punishRules:{
          content:[{ required:true, message: "请输入行政处罚内容", trigger: "blur" }],
          punishmentDate:[{ required: true, message: "请输入处罚决定书下达时间", trigger: "blur" }],
          fineSituation:[{ required:true, message: "请输入处罚款到位情况", trigger: "blur" }]
        },
        closeform:{isClose:0},
        closeRules:{
          undertakeUnit:[{ required:true, message: "请输入承办单位", trigger: "blur" }],
          processingResult:[{ required: true, message: "请输入处理结果", trigger: "blur" }],
          closeDate:[{ required:true, message: "请输入结案日期", trigger: "blur" }],
          isClose:[{ required: true, message: "请选择是否结案", trigger: "blur" }]
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() >= Date.now();
          },
        },
       punishShow:true,closeShow:true,
       saveflag:1,
        allProductTypeMap:[]
      }
    },
    created() {
      //得到产品数组
      fetchAllProductTypeMap().then(response=> {
        this.allProductTypeMap = response.data;
      });

      //得到产品类型树
      fetchProductTypeTree().then(response=> {
           this.productTypeOptions = response;
        }).catch((e) => {
           this.$message({ type: "error",  showClose: true,  message: "产品类型获取失败。"  });
        });
      getEnterprise({finalAreaId:this.user.areaId,enterpriseStatus:'3',sort:"enterpriseName",order:"ASC"}).then((response)=>{
        this.enterpriseOptions = response;
      }).catch(()=>{
        this.$message({
          type:'error',
          showClose: true,
          message: '企业信息获取失败。'
        });
      });
      this.getList();
    },
    mounted () {
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function () {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      });
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      }
    },
    updated () {
      this.height = loadGridHeight();
    },
    computed: {
      ...mapGetters(["staticData","user"]),
      caseStatusOptions() { return this.staticData["违规案件状态"];  },
      resultStatusOptions(){return this.staticData["违规案件处理结果"]; }
    },
    methods: {
      handleClose(row){
        this.saveflag=3;
        this.closeform.irregularityId=row.irregularityId;
        getObj(row.irregularityId).then(response => {
          this.form = response.data.irregularityCase;
          this.form.productType=[response.data.productPTypeId+"",response.data.irregularityCase.productType+""];
          this.productTypeChange();
          this.form.haveCasualty=parseInt(response.data.irregularityCase.haveCasualty);

          if(response.data.punish!=null){
            this.punishform=response.data.punish;
            this.punishShow=true;
            this.punishReadOnly=true;
          }else{
            this.punishShow=false;
          }
          this.closeShow=true;
          this.manager_update = true;
          this.filingReadOnly=true;
          this.closeReadOnly=false;
          this.dialogStatus = "update";

          this.dialogFormVisible = true;
          if(this.punishShow) this.tabPosition='2';
          else  this.tabPosition='1';
        });
      },
      handlePunish(row){
         this.saveflag=2;
         this.punishform.irregularityId=row.irregularityId;
         getObj(row.irregularityId).then(response => {
           this.form = response.data.irregularityCase;
          this.form.productType=[response.data.productPTypeId+"",response.data.irregularityCase.productType+""];
          this.productTypeChange();
          this.form.haveCasualty=parseInt(response.data.irregularityCase.haveCasualty);
          this.punishShow=true;
          this.closeShow=false;
          this.filingReadOnly=true;
          this.punishReadOnly=false;
          this.closeReadOnly=true;
          this.manager_update = true;
          this.dialogFormVisible = true;
          this.tabPosition='1';
          this.dialogStatus = "update";
         });
      },
      handleCreate() {
        this.punishShow=false;
        this.closeShow=false;
        this.filingReadOnly=false;
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.saveflag=1;
        this.tabPosition='0';
        this.manager_update = true;
      },
      handleView(row) {
        getObj(row.irregularityId).then(response => {
          this.form = response.data.irregularityCase;
          this.form.productType=[response.data.productPTypeId+"",response.data.irregularityCase.productType+""];
          this.productTypeChange();
          this.form.haveCasualty=parseInt(response.data.irregularityCase.haveCasualty);
          if(this.form.caseStatus==1){
            this.punishShow=false;
            this.closeShow=false;
          }else if(this.form.caseStatus==2){
            this.punishform= response.data.punish;
            this.punishShow=true;
            this.closeShow=false;
          }else if(this.form.caseStatus==3){
            this.punishform= response.data.punish;
            this.closeform= response.data.caseClose;
            this.closeform.isClose=parseInt(response.data.caseClose.isClose);
            this.punishShow=true;
            this.closeShow=true;
          }
          this.manager_update = false;
          this.filingReadOnly=true;//立案信息只读
          this.punishReadOnly=true;//处罚信息只读
          this.closeReadOnly=true;//结案信息只读
          this.dialogFormVisible = true;
          this.dialogStatus = "view";

        });
      },
      save(){
         switch (this.saveflag){
           case 1:
             this.submitFiling("form");
             break;
           case 2:
             this.submitPunish("punishform");
             break;
           case 3:
             this.submitClose("closeform");
             break;
         }
      },
      //提交结案信息
      submitClose(formName){
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            saveCaseClose(this.closeform).then((response) => {
              this.dialogFormVisible = false;
              this.getList();
              if(response.rel)
                this.$message({ type:'success',   showClose: true,  message: '结案信息保存成功'  });
              else
                this.$message({ message: response.message, type: "error",showClose: true });
            });
          }else  {
            if(this.punishShow) this.tabPosition='2';
            else  this.tabPosition='1';
          }
        });
      },
      //提交处罚信息
      submitPunish(formName){
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            saveCasePunish(this.punishform).then((response) => {
              this.dialogFormVisible = false;
              this.getList();
              if(response.rel)
                this.$message({ type:'success',   showClose: true,  message: '处罚信息保存成功'  });
              else
                this.$message({ message: response.message, type: "error",showClose: true });
            });
          }else
            this.tabPosition='1';
        });
      },
      //提交立案
      submitFiling(formName){
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.form.productType=undefined;
            saveIrregularity(this.form).then((response) => {
              this.dialogFormVisible = false;
              this.getList();
              if(response.rel)
                this.$message({ type:'success',   showClose: true,  message: '立案保存成功'  });
              else
                this.$message({ message: response.message, type: "error",showClose: true });
            });
          }
        });
      },
      productTypeChange(val){
        this.products=[];
        this.form.productId=undefined;
        if(this.form.enterpriseId&&this.form.productType){
          getProducts({enterpriseId:this.form.enterpriseId,productTypePId:this.form.productType[0],productTypeId:this.form.productType[1],productStatus:"1"}).then(response => {
            this.products = response.rows;
          });
        }
      },
      setAreaId(data){ this.listQuery.areaId = data;},
      setGridId(data){this.listQuery.gridId = data; },
      setEnterpriseId(data){ this.listQuery.enterpriseId = data;},
      tableRowClassName({row, rowIndex}) {
        if (row.staffStatus === '0') {
          return 'warning-row';
        } else if (row.staffStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      getList() {
        this.listLoading = true;
        page(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });

      },
      resetQuery(){
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit,
          caseStatus: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined  };
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      cancel() {
        this.dialogFormVisible = false;
        this.dialogStatus = "create";
        this.resetTmp();
      },

      //关闭弹出框时，清除表单验证，并且设置表单元素为空
      closeDialog(){
        this.resetTmp();
        this.tabPosition='0';
      },
      resetTmp() {
        if (this.$refs["form"]) {
          this.form={ haveCasualty:0};
          this.punishform={content:undefined,punishmentDate:undefined,fineSituation:undefined };
          this.closeform={ isClose:0};
          this.$refs["form"].clearValidate();
          if(this.punishShow)
            this.$refs["punishform"].clearValidate();
          if(this.closeShow)
            this.$refs["closeform"].clearValidate();
        }
      },
      getSelection(selection) { this.selection = selection;},
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.caseStatusOptions);
      },
      productTypeFormatter(row, column, cellValue){
        return parseValueToText(cellValue, this.allProductTypeMap);
      }
    }
  }
</script>

