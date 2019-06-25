<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId">
          </queryConditions>
          <el-form-item label="内容关键字" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="内容关键字" v-model="listQuery.writingLike"> </el-input>
          </el-form-item>
          <el-form-item label="状态" class="filter-item">
            <el-select v-model="listQuery.status" placeholder="状态" >
              <!-- <el-option label="作废" value="0"></el-option> -->
              <el-option label="待受理" value="1"></el-option>
              <el-option label="已受理" value="2"></el-option>
              <el-option label="受理中" value="3"></el-option>
              <el-option label="受理结束" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button class="filter-item" type="primary" v-waves icon="search" @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>

    <el-tabs v-model="tabPosition" @tab-click="handleClick">
      <el-tab-pane label="平台上报" name="first">
      </el-tab-pane>
      <el-tab-pane label="公众上报" name="second">
      </el-tab-pane>
    </el-tabs>
    <!--  <div class="head-box">
       <div class="item-box">
         <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate">
           <i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
       </div>
     </div> -->
    <el-table :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column width="200px" align="center" label="投诉企业" prop="enterpriseName" show-overflow-tooltip></el-table-column>
      <el-table-column width="250px" align="center" label="投诉描述" prop="writing" show-overflow-tooltip></el-table-column>
      <el-table-column width="180px" align="center" label="提交时间" prop="createTime"></el-table-column>
      <el-table-column width="150px" align="center" label="提交人姓名" prop="submitName"></el-table-column>
      <el-table-column width="150px" align="center" label="提交者电话" prop="submitTel"></el-table-column>
      <el-table-column width="100px" align="center" label="状态" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status==0">{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==1" type="info">{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==2" type="warning">{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==3" type="warning">{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==4" type="warning">{{statusFormatter(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="center" label="处理结果" prop="handleResult" show-overflow-tooltip></el-table-column>
      <el-table-column width="150px" align="center" label="责任人" prop="personLiable" ></el-table-column>
      <el-table-column width="150px" align="center" label="责任人电话" prop="personLiableTel"></el-table-column>
      <el-table-column width="150px" align="center" label="处理人" prop="handleManId" :formatter="userFormatter"></el-table-column>
      <el-table-column width="150px" align="center" label="处理时间" prop="handleTime"></el-table-column>

      <el-table-column width="200px" align="center" label="回复内容" prop="writing1" show-overflow-tooltip></el-table-column>
      <el-table-column width="150px" align="center" label="回复人" prop="createOp1" :formatter="userFormatter"></el-table-column>
      <el-table-column width="150px" align="center" label="回复时间" prop="createTime1"></el-table-column>

      <el-table-column align="center" label="操作" width="200" fixed="right">
        <template slot-scope="scope">

          <el-button size="small"  v-if="scope.row.irregularityId<=0"  type="warning" @click="handleFiling(scope.row)">立案</el-button>

          <el-button size="small" v-if="scope.row.status==3" type="primary" @click="handleView(scope.row)">回复</el-button>
          <el-button  size="small" v-else type="primary" @click="handleView(scope.row)">查看</el-button>
          <!--      <el-button size="small" type="primary" @click="handleDelete(scope.row.complainEvaluateId)">作废</el-button>
               <el-button size="small" type="primary" @click="handleView(scope.row)">受理</el-button>
               <el-button size="small" type="primary" @click="handleView(scope.row)">立案</el-button> -->
          <!--<el-button size="small" type="success" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="small" type="success" @click="handleDelete(scope.row.complainEvaluateId)">删除</el-button> -->
          <!-- <el-button v-if="scope.row.status==1" size="small" type="success" @click="handleFault(scope.row)">回复</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '新增'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '修改'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <div style="height: 550px;">
        <el-tabs v-model="viewTabPanl">
          <el-tab-pane label="投诉信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px">
              <el-form-item v-if="isView" label="投诉企业" prop="enterpriseName">
                <el-input v-model="form.enterpriseName" disabled></el-input>
              </el-form-item>
              <el-form-item v-if="!isView" label="投诉企业" prop="enterprisId">
                <el-select v-model="enterpriseId" placeholder="请选择" clearable @change="handleSelectChange">
                  <el-option v-for="item in  enterpriseMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="投诉描述" prop="writing">
                <el-input type="textarea" :rows="2" v-model="form.writing" :disabled="isView"></el-input>
              </el-form-item>
              <el-form-item label="图片" prop="imgFile" ref="imgFile">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" multiple :file-list="imgFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="4" :disabled="isView" :auto-upload="false" :on-exceed="handleExceed">
                  <i class="el-icon-plus"></i>
                </el-upload>
              </el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="提交人" prop="submitName">
                    <el-input v-model="form.submitName" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="提交人电话" prop="submitTel">
                    <el-input v-model="form.submitTel" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交平台" prop="submitPlatformDesc">
                    <el-input v-model="form.submitPlatformDesc" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交时间" prop="createTime">
                    <el-input v-model="form.createTime" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <!--  <div slot="footer" class="dialog-footer" v-if="userManager_update">
         <el-button @click="cancel('form')">取 消</el-button>
         <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
         <el-button v-else type="primary" @click="update('form')">确 定</el-button>
       </div>  -->
      <div slot="footer" class="dialog-footer"  v-if="isView" style=" text-align: center;">
        <template v-if="form.status==1">
          <el-button type="danger"  @click="handleDelete(form.complainEvaluateId)">作废</el-button>
          <el-button type="primary" @click="handleAcceptance(form.complainEvaluateId)">受理</el-button>
        </template>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFaultFormVisible">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <div style="height: 550px;">
        <el-tabs v-model="viewTabPanl1">
          <el-tab-pane label="投诉信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px">
              <el-form-item v-if="isView" label="投诉企业" prop="enterpriseName">
                <el-input v-model="form.enterpriseName" disabled></el-input>
              </el-form-item>
              <el-form-item v-if="!isView" label="投诉企业" prop="enterprisId">
                <el-select v-model="enterpriseId" placeholder="请选择" clearable @change="handleSelectChange">
                  <el-option v-for="item in  enterpriseMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="投诉描述" prop="writing">
                <el-input type="textarea" :rows="2" v-model="form.writing" :disabled="isView"></el-input>
              </el-form-item>
              <el-form-item label="图片" prop="imgFile" ref="imgFile">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" multiple :file-list="imgFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="4" :disabled="isView" :auto-upload="false" :on-exceed="handleExceed">
                  <i class="el-icon-plus"></i>
                </el-upload>
              </el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="提交人" prop="submitName">
                    <el-input v-model="form.submitName" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="提交人电话" prop="submitTel">
                    <el-input v-model="form.submitTel" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交平台" prop="submitPlatformDesc">
                    <el-input v-model="form.submitPlatformDesc" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交时间" prop="createTime">
                    <el-input v-model="form.createTime" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane  label="处理信息">
            <el-form :model="solutionform" :rules="solutionRules" ref="solutionform" label-width="100px">
              <el-row >
                <el-col :span="12">
                  <el-form-item v-if="isView" label="处理人" prop="handleManName">
                    <el-input v-model="solutionform.handleManName" :disabled="isView" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item  label="处理时间" prop="handleTime">
                    <el-input v-model="solutionform.handleTime" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="责任人" prop="personLiable">
                    <el-input v-model="solutionform.personLiable" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="责任人电话" prop="personLiableTel">
                    <el-input  v-model="solutionform.personLiableTel" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item  label="处理结果" prop="handleResult">
                <el-input  type="textarea" :rows="3" v-model="solutionform.handleResult" :disabled="isView"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane  label="回复信息">
            <el-form :model="faultform" :rules="faultRules" ref="faultform" label-width="100px">
              <!-- <el-form :model="solutionform" :rules="solutionRules" ref="solutionform" label-width="100px">              -->
              <el-row v-if="form.status != 3">
                <el-col :span="12">
                  <el-form-item label="回复人" prop="createOpName1">
                    <el-input v-model="faultform.createOpName1" :disabled="isView" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item  label="回复时间" prop="createTime1">
                    <el-input v-model="faultform.createTime1" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="回复内容" prop="writing1">
                <el-input v-if="form.status == 3" type="textarea" :rows="3" v-model="faultform.writing1" ></el-input>
                <el-input v-else type="textarea" :rows="3" v-model="faultform.writing1" disabled></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer"  v-if="form.status == 3" style=" text-align: center;">
        <template v-if="viewTabPanl1=='2'">
          <el-button @click="cancel('faultform')">取 消</el-button>
          <el-button type="primary" @click="faultEdit('faultform')">确 定</el-button>
        </template>
        <template v-else>
          <el-button @click="handleFault(form.complainEvaluateId)">回复</el-button>
        </template>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>


    <el-dialog title="违规立案" :visible.sync="dialogFilingVisible"   :close-on-click-modal="false"  @close="closeFilingDialog">
      <el-form :model="filingform" :rules="filingRules" ref="filingform" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="涉案企业" prop="enterpriseName">
              <el-input v-model="filingform.enterpriseName" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="立案日期" prop="caseDate">
              <el-date-picker v-model="filingform.caseDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions" style="width:100%"   ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品类型:" prop="productType" >
              <el-cascader v-model="filingform.productType"   :options="productTypeOptions"  style="width:100%"  :show-all-levels="true" expand-trigger="hover" :change-on-select="false" @change="productTypeChange" :props="{label:'text', value: 'id'}" >
              </el-cascader>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="涉案产品" prop="productId">
              <el-select v-model="filingform.productId" placeholder="请选择" clearable filterable  style="width:100%"  >
                <el-option v-for="item in  products" :key="item.productId" :label="item.productName" :value="item.productId" > </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="案由" prop="caseCause">
              <el-input v-model="filingform.caseCause"  ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="涉案地点" prop="address">
              <el-input v-model="filingform.address"   ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="违法类型" prop="illegalType">
              <el-input v-model="filingform.illegalType"   ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="涉案金额" prop="amount">
              <el-input v-model="filingform.amount"   >
                <template slot="append">(元)</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="有无伤亡" prop="haveCasualty">
              <el-radio-group v-model="filingform.haveCasualty"   >
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="违法行为" prop="illegalActivities">
              <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="filingform.illegalActivities"   ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="违法事实" prop="illegalFact">
              <el-input  type="textarea"  :autosize="{ minRows: 3, maxRows: 3}" v-model="filingform.illegalFact"   ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <div slot="footer" class="dialog-footer"   style=" text-align: right;">
        <el-button type="primary" @click="submitFiling('filingform')">确 定</el-button>
        <el-button @click="dialogFilingVisible=false">取消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>

  import {
    page,
    getObj,
    putObj,
    addObj,
    delObj,
    evaluateEdit,
    acceptance
  } from "api/admin/evaluate/index";
  import parent from "api/admin/common/parent";
  import { parseValueToText,parseTime } from "utils/index";
  import { getAllUserMap } from "api/admin/user/index";
  import { getMulResource } from "api/admin/register/index";
  import queryConditions from "components/QueryConditions/index";
  import { getEnterpriseBaseMap } from "api/admin/safety/record/recyle/index";
  import { loadGridHeight } from "api/screen";
  import { page as getProducts } from "api/admin/enterprise/product/index";
  import { saveIrregularity } from "api/admin/irregularity/index";
  import { validateDecimals} from "utils/validate";
  import {  fetchProductTypeTree} from "api/admin/enterprise/product/index";

  export default {
    name: "event",
    components: {
      queryConditions
    },
    mixins: [parent],
    data() {
      const gradeValidator = (rule, value, callback) => {
        if (!this.gradeStar || this.gradeStar == 0) {
          callback(new Error("请设置评价分数"));
        } else {
          callback();
        }
      };

      return {
        height: undefined,
        needFixedRight: false,
        enterpriseId: undefined,
        enterpriseMap: [],
        equOptions: [],
        listQuery: {
          page: 1,
          limit: 20,
          writingLike: undefined,
          typeId: "1",
          submitPlatform: "1", //1平台 2微信
          enterpriseId: undefined,
          areaId: undefined,
          gridId: undefined,
          status: undefined,
        },
        form: {
          complainEvaluateId: undefined,
          typeId: "1",
          writing: undefined,
          imgPath: undefined,
          grade: undefined,
          submitName: undefined,
          submitTel: undefined,
          submitPlatform: "1", //1app 2微信
          enterpriseId: undefined
        },
        solutionform: {
          complainEvaluateId: undefined,
          handleManId: undefined,
          handleManName: undefined,
          handleResult: undefined,
          handleTime: undefined,
          personLiable: undefined,
          personLiableTel: undefined
        },
        faultform: {
          complainEvaluateId: undefined,
          writing1: undefined,
          createTime1: undefined,
          createOp1: undefined
        },
        rules: {
          gradeStar: [{ required: true, validator: gradeValidator }],
          writing: [
            { required: true, message: "请输入评价描述", trigger: "blur" }
          ]
        },
        solutionRules: {},
        faultRules: {
          writing1: [
            { required: true, message: "请输入回复内容", trigger: "blur" }
          ]
        },
        dialogFormVisible: false,
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "详情"
        },
        userManager_update: true,
        isView: false,
        httpUrl: "", //文件显示URL

        imgFile: [],
        dialogImageVisible: false, //图片预览窗口是否显示
        dialogImageUrl: undefined, //预览图片的地址
        fileFormat: ["image/jpeg", "image/png", "image/bmp", "image/gif"], // 允许上传的文件格式
        dialogFaultFormVisible: false,
        allUserOptions: [],
        tabPosition: "first",
        viewTabPanl: "0",
        viewTabPanl1:"0",
        gradeShow: "",
        gradeStar: undefined,
        dialogFilingVisible:false,
        filingform:{
          enterpriseId:undefined,
          productId:undefined,
          address:undefined,
          haveCasualty:0,
          illegalType:undefined,
          caseCause:undefined,
          caseDate:undefined
        },
        products:[],
        productTypeOptions: [],
        filingRules:{
          productId:[{ required: true, message: "请输入产品", trigger: "blur" }],
          productType:[{ required: true, message: "请选择产品类型", trigger: "blur" }],
          haveCasualty:[{ required: true, message: "请选择有无人员伤亡", trigger: "blur" }],
          illegalType:[{ required: true, message: "请输入违法类型", trigger: "blur" }],
          caseCause:[{ required: true, message: "请输入案由", trigger: "blur" }],
          caseDate:[{ required: true, message: "请输入立案日期", trigger: "blur" }],
          amount:[{ validator:validateDecimals, message: "请输入正确的金额", trigger: "blur" }]
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() >= Date.now();
          },
        }
      };
    },
    created() {
      this.init();
      this.getList();
      //得到产品类型树
      fetchProductTypeTree().then(response=> {
        this.productTypeOptions = response;
      }).catch((e) => {
      });
    },
    computed: {},
    mounted() {
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function() {
        let tableDiv = this.$el.querySelector(".el-table__body-wrapper");
        this.needFixedRight = !(
          $(tableDiv)
            .attr("class")
            .indexOf("is-scrolling-none") >= 0
        );
      });
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv = this.$el.querySelector(".el-table__body-wrapper");
        this.needFixedRight = !(
          $(tableDiv)
            .attr("class")
            .indexOf("is-scrolling-none") >= 0
        );
      };
    },
    updated () {
      this.height = loadGridHeight();
    },
    methods: {
      //表格样式
      tableRowClassName({ row, rowIndex }) {
        if (row.userStatus === "0") {
          return "warning-row";
        } else if (row.userStatus === "1") {
          return "success-row";
        }
        return "";
      },
      setAreaId(data) {
        this.listQuery.areaId = data;
      },
      setGridId(data) {
        this.listQuery.gridId = data;
      },
      setEnterpriseId(data) {
        this.listQuery.enterpriseId = data;
      },

      init() {
        let _this = this;
        _this.httpUrl = process.env.SERVERIMAGEURL;

        getAllUserMap()
          .then(response => {
            _this.allUserOptions = response.data;
          })
          .catch(() => {});
        //
        getEnterpriseBaseMap().then(response => {
          this.enterpriseMap = response["enterprise"];
        });
      },
      handleSelectChange(val) {
        this.form.enterpriseId = val;
      },
      //tab转换查询
      handleClick() {
        if (this.tabPosition == "first") {
          this.listQuery.submitPlatform = "1";
        } else {
          this.listQuery.submitPlatform = "2";
        }
        this.getList();
      },
      getList() {
        this.listLoading = true;
        page(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      eventTypeFormatter(row, column, cellValue) {
        if (!cellValue) {
          return "";
        }
        if (cellValue == "1") {
          return "投诉";
        } else if (cellValue == "2") {
          return "评价";
        }
      },
      userFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.allUserOptions);
      },
      submitPlatformFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["上报来源"]);
      },
      statusFormatter(cellValue) {
        if (!cellValue) {
          return "";
        }
        if (cellValue == "0") {
          return "无效";
        } else if (cellValue == "1") {
          return "待受理";
        } else if (cellValue == "2") {
          return "已受理";
        } else if (cellValue == "3") {
          return "受理中";
        } else if (cellValue == "4") {
          return "受理结束";
        }
      },
      resetQuery() {
        this.listQuery.writingLike = undefined;
        this.listQuery.typeId = "1";
        this.listQuery.submitPlatform = "1"; //1平台 2微信
        this.listQuery.enterpriseId = undefined;
        this.listQuery.areaId = undefined;
        this.listQuery.gridId = undefined;
        this.listQuery.status = undefined;

      },
      handleView(row) {
        this.imgFile = [];
        getObj(row.complainEvaluateId).then(response => {
          this.form = response.data;
          this.enterpriseId = response.data.enterpriseId + "";
          this.gradeShow = this.form.grade + "";
          this.gradeStar = this.form.grade / 2;
          this.form.submitPlatformDesc = parseValueToText(
            response.data.submitPlatform,
            this.staticData["上报来源"]
          );
          this.form.createOpName1 = parseValueToText(
            response.data.createOp1,
            this.allUserOptions
          );

          if (this.form.imgPath) {
            let mulResourceIds = this.form.imgPath;
            getMulResource(mulResourceIds).then(response => {
              let imgArr = response.data;
              this.imgFile = [];
              for (let i = 0; i < imgArr.length; i++) {
                this.imgFile.push({
                  url: this.httpUrl + "/" + imgArr[i].resourcePath,
                  id: imgArr[i].resourceId + ""
                });
              }
              $(".el-upload--picture-card").hide();
            });
          }
          // if(this.form.status==3){
          this.solutionform.handleManId = response.data.handleManId;
          this.solutionform.handleManName = parseValueToText(response.data.handleManId, this.allUserOptions);
          this.solutionform.handleResult = response.data.handleResult;
          this.solutionform.handleTime = parseTime(response.data.handleTime,'{y}-{m}-{d}');
          this.solutionform.personLiable = response.data.personLiable;
          this.solutionform.personLiableTel = response.data.personLiableTel;

          if(this.form.status==3){
            this.handleFault(response.data.complainEvaluateId);
          }else if(this.form.status==4){
            this.faultform.writing1 = response.data.writing1;
            this.faultform.createTime1 = parseTime(response.data.createTime1,'{y}-{m}-{d}');
            this.faultform.createOp1 = response.data.createOp1;
            this.faultform.createOpName1 =parseValueToText(response.data.createOp1, this.allUserOptions);
            this.dialogFaultFormVisible=true;
            this.viewTabPanl1 = "0";
          }else{
            this.dialogFormVisible = true;
          }

          this.dialogStatus = "view";
          this.isView = true;

          this.userManager_update = false;
        });
      },
      handleCreate() {
        this.resetTmp();
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.userManager_update = true;
        this.isView = false;
        this.$nextTick(() => {
          $(".el-upload--picture-card").show();
        });
      },
      handleUpdate(row) {
        getObj(row.complainEvaluateId).then(response => {
          this.form = response.data;
          this.enterpriseId = response.data.enterpriseId + "";
          this.gradeShow = this.form.grade + "";
          this.gradeStar = this.form.grade / 2;

          if (this.form.imgPath) {
            let mulResourceIds = this.form.imgPath;
            getMulResource(mulResourceIds).then(response => {
              let imgArr = response.data;

              this.imgFile = [];
              for (let i = 0; i < imgArr.length; i++) {
                this.imgFile.push({
                  url: this.httpUrl + "/" + imgArr[i].resourcePath,
                  id: imgArr[i].resourceId + ""
                });
              }
              $(".el-upload--picture-card").show();
            });
          }
          this.dialogStatus = "update";
          this.dialogFormVisible = true;
          this.userManager_update = true;
          this.isView = false;
        });
      },
      handleDelete(idArr) {
        this.$confirm("此操作将永久作废, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            delObj(idArr)
              .then(() => {
                this.$notify({
                  title: "成功",
                  message: "删除成功",
                  type: "success",
                  duration: 2000
                });
                this.getList();
                this.dialogFormVisible = false;
              })
              .catch(() => {});
          })
          .catch(() => {});
      },
      //10分制
      handleGradeChange(val) {
        this.gradeShow = val * 2 + "";
        this.form.grade = val * 2;
      },
      //回复
      handleFault(id) {
        this.faultform.complainEvaluateId = id;
        this.faultform.writing1 = "";
        this.faultform.createTime1 = undefined;
        this.faultform.createOp1 = undefined;

        this.dialogFaultFormVisible = true;
        this.viewTabPanl1 = "2"
      },
      create(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            let param = new FormData();
            param.append("ComplainEvaluate", JSON.stringify(this.form));
            for (let i = 0; i < this.imgFile.length; i++) {
              if (this.imgFile[i].raw != undefined) {
                //判断图片是否被更新
                param.append("file", this.imgFile[i].raw);
              } else {
                param.append("file", null);
              }
            }

            addObj(param).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "创建成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            let param = new FormData();
            param.append("ComplainEvaluate", JSON.stringify(this.form));
            for (let i = 0; i < this.imgFile.length; i++) {
              if (this.imgFile[i].raw != undefined) {
                //判断图片是否被更新
                param.append("file", this.imgFile[i].raw);
              } else {
                param.append("file", null);
              }
            }
            putObj(param).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "更新成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      faultEdit(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            evaluateEdit(this.faultform.complainEvaluateId, {
              answer: this.faultform.writing1
            }).then(() => {
              this.dialogFaultFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "更新成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      // 受理
      handleAcceptance(id){
        acceptance(id).then(() => {
          this.dialogFormVisible = false;
          this.getList();
          this.$notify({
            title: "成功",
            message: "操作成功",
            type: "success",
            duration: 2000
          });
        });
      },
      cancel() {
        this.dialogFormVisible = false;
        this.dialogFaultFormVisible = false;
      },
      resetTmp() {
        this.form = {
          complainEvaluateId: undefined,
          typeId: "1",
          writing: undefined,
          imgPath: undefined,
          grade: undefined,
          submitName: undefined,
          submitTel: undefined,
          submitPlatform: "1" //1app 2微信
        };
        this.imgFile = [];

        this.$nextTick(function() {
          this.$refs["form"].resetFields();
        });
      },

      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择 4 个文件，本次选择了 ${fileList.length} 个文件`
        );
      },

      // 点击"+"图标时显示弹出框
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
      },
      //删除图片
      handleImageRemove(file, fileList) {
        if (file.id) {
          let imgArr = this.form.imgPath.split(",");
          let index = imgArr.indexOf(file.id);
          if (index > -1) {
            imgArr.splice(index, 1);
          }
          this.form.imgPath = imgArr.join(",");
        }
        this.imgFile = fileList;
        if (this.imgFile.length == 0) {
          $(".el-upload--picture-card").show();
        }
      },
      addImgFile(file) {
        // 添加图片回调
        if (this.fileFormat.indexOf(file.raw.type) < 0) {
          this.$message({
            type: "error",
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.$refs["imgFile"].resetField(); //当添加图片后，重置图片验证功能。
        this.imgFile.push(file);
        if (this.imgFile.length > 4) {
          $(".el-upload--picture-card").hide();
        }
      },
      closeDialog() {
        this.form = {};
        this.imgFile = [];
        $(".el-upload--picture-card").show();
        this.isView = false;
        this.isUpdate = false;
        this.$refs["form"].resetFields();
        this.verifyResultReason = {};
      },
      handleFiling(row){

        getProducts({enterpriseId:row.enterpriseId,productStatus:"1"}).then(response => {
          this.products = response.rows;
        });
        this.filingform.enterpriseName=row.enterpriseName;
        this.filingform.enterpriseId=row.enterpriseId;
        this.filingform.complainEvaluateId=row.complainEvaluateId;
        this.dialogFilingVisible=true;
      },
      //提交立案
      submitFiling(formName){
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.filingform.productType=undefined;
            saveIrregularity(this.filingform).then(() => {
              this.dialogFilingVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "立案成功",
                type: "success",
                duration: 2000
              });
            });
          }
        });
      },
      closeFilingDialog(){
        if (this.$refs["filingform"]) {
          this.filingform={haveCasualty:0 };
          this.$refs["filingform"].resetFields();
        }
      },
      productTypeChange(val){
        this.products=[];
        this.filingform.productId=undefined;
        if(this.filingform.enterpriseId&&this.filingform.productType){
          getProducts({enterpriseId:this.filingform.enterpriseId,productTypePId:this.filingform.productType[0],productTypeId:this.filingform.productType[1],productStatus:"1"}).then(response => {
            this.products = response.rows;
          });
        }
      },
    }
  };
</script>
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>


