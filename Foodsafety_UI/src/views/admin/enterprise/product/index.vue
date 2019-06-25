<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="产品名称" prop="productName" class="filter-item">
            <el-input v-model.trim="listQuery.productNameLike" placeholder="请输入产品名称" :disabled="isView"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="productStatus" class="filter-item">
            <el-select v-model="listQuery.productStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <div class="head-box">
      <div class="item-box">
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate">
          <i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>新增</span>
      </div>
    </div>
    <el-table :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="产品名称" width="200" prop="productName"></el-table-column>
      <el-table-column align="center" label="产品大类" width="200" prop="productTypePId" :formatter="productTypeFormatter"></el-table-column>
      <el-table-column align="center" label="产品小类" width="200" prop="productTypeId" :formatter="productTypeFormatter"></el-table-column>
      <el-table-column align="center" label="许可证编号" width="200" prop="licenseNo"></el-table-column>
      <el-table-column align="center" label="发证机构" width="250" prop="issuingInstitution"></el-table-column>
      <el-table-column align="center" label="证书状态" width="150" prop="certificateState" :formatter="certificateStateFormatter"></el-table-column>
      <el-table-column align="center" label="发证日期" width="150" prop="issuingDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="有效期" width="150" prop="validDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="变更状态" width="100" prop="verifyStatus">
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.verifyStatus=='1'">审核中</el-tag>
          <el-tag type="success" v-else-if="scope.row.verifyStatus=='2'||scope.row.verifyStatus=='3'">完成</el-tag>
          <el-tag type="info" v-else>未变更</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变更原因" width="200" prop="changeReason" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="规格型号" width="150" prop="specificationModel"></el-table-column>
      <el-table-column align="center" label="产品执行标准" width="150" prop="productStandard"></el-table-column>
      <el-table-column align="center" label="标准分类" width="120" prop="standardType"></el-table-column>
      <el-table-column align="center" label="注册商标" width="150" prop="registeredTrademark"></el-table-column>
      <el-table-column align="center" label="出厂检验方式" width="150" prop="factoryInspection" :formatter="factoryInspectionFormatter"></el-table-column>
      <el-table-column align="center" label="销售区域" width="200" prop="salesArea"></el-table-column>
      <el-table-column align="center" label="是否出口产品" width="120" prop="isExport" :formatter="exportFormatter"></el-table-column>
      <el-table-column align="center" label="信息类型" width="150" prop="infoType" :formatter="infoTypeFormatter"></el-table-column>
      <el-table-column align="center" label="产品状态" width="100" prop="productStatus" :formatter="statusFormatter"></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="250" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.verifyStatus == '1'">修改</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.verifyStatus == '1'">停产</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.verifyStatus == '1'">修改</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.verifyStatus == '1'">停产</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <!--修改，查看，新增表单对话框-->
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
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
      <div style="height: 650px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="产品信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="120px">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="产品名称:" prop="productName">
                    <el-input v-model.trim="form.productName" placeholder="请输入产品名称" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="产品类型:" prop="productTypeId">
                    <el-cascader v-model="selectProdeuctTypes" :options="productTypeOptions" :show-all-levels="true" expand-trigger="hover" :change-on-select="false" @change="setProductTypeId" :props="{label:'text', value: 'id'}" :disabled="isView">
                    </el-cascader>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="许可证编号:" prop="licenseNo">
                    <el-input v-model.trim="form.licenseNo" placeholder="请输入许可证编号" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="证书状态:" prop="certificateState">
                    <el-select v-model="form.certificateState" placeholder="请选择证书状态" clearable :disabled="isView">
                      <el-option v-for="item in  certificateStateOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="发证机构:" prop="issuingInstitution">
                <el-input v-model.trim="form.issuingInstitution" placeholder="请输入发证机构" :disabled="isView"></el-input>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="发证日期:" prop="issuingDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.issuingDate" :disabled="isView"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="有效期:" prop="validDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.validDate" :disabled="isView"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="规格型号:" prop="specificationModel">
                    <el-input v-model.trim="form.specificationModel" placeholder="请输入规格型号" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="出厂检验方式:" prop="factoryInspection">
                    <el-select v-model="form.factoryInspection" placeholder="请选择出厂检验方式" clearable :disabled="isView">
                      <el-option v-for="item in  factoryInspectionOption" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="产品执行标准:" prop="productStandard">
                    <el-input v-model.trim="form.productStandard" placeholder="请输入产品执行标准" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="标准分类:" prop="standardType">
                    <el-input v-model.trim="form.standardType" placeholder="请输入标准分类" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="注册商标:" prop="registeredTrademark">
                    <el-input v-model.trim="form.registeredTrademark" placeholder="请输入注册商标" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="信息类型:">
                    <el-select v-model="form.infoType" placeholder="请选择信息类型" clearable :disabled="isView">
                      <el-option v-for="item in  infoTypeOption" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="销售区域:" prop="salesArea">
                    <el-input v-model.trim="form.salesArea" placeholder="请输入销售区域" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="是否出口产品:" prop="isExport" :disabled="isView">
                    <el-radio v-model="form.isExport" label="1">是</el-radio>
                    <el-radio v-model="form.isExport" label="0">否</el-radio>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="产品图片:" prop="imgFile" ref="imgFile">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :file-list="imgFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="1" :disabled="isView" :auto-upload="false">
                  <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
                  <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="变更原因" v-if="user_btn_update">
            <el-form :model="changeVerifyForm" :rules="rules" ref="changeReasonForm">
              <el-form-item prop="reason">
                <el-input type="textarea" :rows="5" v-model.trim="changeVerifyForm.reason" :disabled="isView"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer" v-if="user_btn_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create()">确 定</el-button>
        <el-button v-else type="primary" @click="update()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getResource } from "api/admin/register/index";
import { parseValueToText, parseTime, findParentNode } from "utils/index";
import {
  page,
  getObj,
  getEnterpriseProduct,
  addEnterpriseProduct,
  changeEnterpriseProduct,
  delObj,
  fetchProductTypeTree,
  fetchAllProductTypeMap
} from "api/admin/enterprise/product/index";
import { loadGridHeight } from "api/screen";
export default {
  name: "index",
  data() {
    const fileValidator = (rule, value, callback) => {
      if (this.imgFile.length < 1) {
        callback(new Error("请选择营业执照附件"));
      } else {
        callback();
      }
    };
    return {
      tabPosition: "0",
      height: undefined,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        productNameLike: undefined,
        enterpriseId: undefined,
        productStatus: undefined
      },
      textMap: {
        update: "修改",
        create: "新增",
        view: "查看"
      },
      dialogStatus: "",
      dialogFormVisible: false,
      user_btn_update: false,
      isView: false, //是否是查看状态
      isUpdate: false, //是否是编辑状态，编辑状态的证照类型不可以修改
      needFixedRight: false,
      serverImageUrl: process.env.SERVERIMAGEURL,
      imgFile: [],
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      fileFormat: ["image/jpeg", "image/png", "image/bmp", "image/gif"], // 允许上传的文件格式
      form: { isExport: "0", productTypeId: undefined },
      formStr: "", //表单初始数据字符串，用于判断表单数据是否被修改
      changeVerifyForm: { reason: undefined },
      rules: {
        productName: [
          { required: true, message: "请输入产品名称", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        productTypeId: [
          { required: true, message: "请输入产品类型", trigger: "change" }
        ],
        licenseNo: [
          { required: true, message: "请输入许可证编号", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        certificateState: [{ required: true, message: "请选择证书状态" }],
        issuingInstitution: [
          { required: true, message: "请输入发证机构", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        issuingDate: [
          { required: true, message: "请输入发证日期", trigger: "blur" }
        ],
        validDate: [
          { required: true, message: "请输入有效日期", trigger: "blur" }
        ],
        specificationModel: [
          { required: true, message: "请输入规格型号", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        productStandard: [
          { required: true, message: "请输入执行标准", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        standardType: [
          { required: true, message: "请输入标准分类", trigger: "blur" },
          { max: 4, message: "长度不能超过4个字符", trigger: "blur" }
        ],
        registeredTrademark: [
          { required: true, message: "请输入注册商标", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        factoryInspection: [{ required: true, message: "请选择出厂检验方式" }],
        salesArea: [
          { required: true, message: "请输入销售区域", trigger: "blur" },
          { max: 64, message: "长度不能超过64个字符", trigger: "blur" }
        ],
        imgFile: [
          { required: true, validator: fileValidator, trigger: "change" }
        ],
        reason: [{ required: true, message: "请输入理由", trigger: "blur" }]
      },
      allProductTypeMap: [],
      productTypeOptions: [],
      selectProdeuctTypes: []
    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),

    certificateStateOptions() {
      return this.staticData["证书状态"];
    },
    factoryInspectionOption() {
      return this.staticData["产品出厂检验方式"];
    },
    exportOption() {
      return this.staticData["出口产品"];
    },
    infoTypeOption() {
      return this.staticData["产品信息类型"];
    },
    statusOptions() {
      return this.staticData["企业产品状态"];
    }
  },
  created() {
    this.initParam();
    this.getList();
  },
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
    this.height = loadGridHeight();
  },
  methods: {
    initParam() {
      let _this = this;
      //得到产品类型Map
      fetchAllProductTypeMap()
        .then(function(response) {
          _this.allProductTypeMap = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "产品类型获取失败。"
          });
        });
      //得到产品类型树
      fetchProductTypeTree()
        .then(function(response) {
          _this.productTypeOptions = response;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "产品类型获取失败。"
          });
        });
    },
    getList() {
      this.listLoading = true;
      this.listQuery.enterpriseId = this.user.enterpriseId;
      page(this.listQuery).then(response => {
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
      this.listQuery.page = 1;
      this.getList();
    },
    resetQuery() {
      this.listQuery.productNameLike = undefined;
      this.listQuery.productStatus = undefined;
    },
    //
    setProductTypeId(currentVal) {
      if (currentVal || currentVal.length > 0) {
        this.form.productTypePId = currentVal[currentVal.length - 2];
        this.form.productTypeId = currentVal[currentVal.length - 1];
      }
    },
    handleCreate() {
      //新增
      this.form.enterpriseId = this.user.enterpriseId;
      this.user_btn_update = true;
      this.tabPosition = "0";
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
    },
    handleView(row) {
      //查看
      getEnterpriseProduct(row.productId).then(response => {
        this.form = response.data;
        this.selectProdeuctTypes = [
          this.form.productTypePId + "",
          this.form.productTypeId + ""
        ];
        if(response.data.changeReason){
          this.changeVerifyForm.reason = response.data.changeReason;
        }else{
          this.changeVerifyForm.reason = undefined;
        }
        let resourceId = this.form.productPicture;
        getResource(resourceId).then(response => {
          this.imgFile = [];
          this.imgFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });
          $(".el-upload--picture-card").hide();
        });
        this.isView = true;
        this.dialogFormVisible = true;
        this.user_btn_update = false;
        this.dialogStatus = "view";
      });
    },
    handleUpdate(row) {
      //修改
      getObj(row.productId).then(response => {
        this.form = response.data;
        this.selectProdeuctTypes = [
          this.form.productTypePId + "",
          this.form.productTypeId + ""
        ];
        this.formStr = JSON.stringify(this.form);
        let resourceId = this.form.productPicture;
        getResource(resourceId).then(response => {
          this.imgFile = [];
          this.imgFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });
          $(".el-upload--picture-card").hide();
        });
        this.isUpdate = true;
        this.dialogFormVisible = true;
        this.tabPosition = "0";
        this.user_btn_update = true;
        this.dialogStatus = "update";
      });
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.certificateStatus === "0") {
        return "warning-row";
      } else if (row.certificateStatus === "1") {
        return "success-row";
      }
      return "";
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    //新增提交
    create() {
      let _self = this;
      let flag = true;
      _self.$refs["form"].validate((valid, errors) => {
        if (!valid) {
          flag = false;
          _self.tabPosition = "0";
        }
      });
      _self.$refs["changeReasonForm"].validate((valid, errors) => {
        if (!flag) {
          _self.tabPosition = "0";
        } else {
          if (!(flag && valid)) {
            flag = false;
            _self.tabPosition = "1";
          }
        }
      });
      if (flag) {
        _self.form.validDate = parseTime(_self.form.validDate);
        _self.form.issuingDate = parseTime(_self.form.issuingDate);
        let param = new FormData();
        param.append("EnterpriseProduct", JSON.stringify(_self.form));
        param.append("reason", _self.changeVerifyForm.reason);
        if (_self.imgFile[0].raw != undefined) {
          //判断图片是否被更新
          param.append("file", _self.imgFile[0].raw);
        } else {
          param.append("file", null);
        }
        addEnterpriseProduct(param).then(response => {
          _self.dialogFormVisible = false;
          _self.getList();
          if (response.rel) {
            _self.$message({
              type: "success",
              showClose: true,
              message: `操作成功！`
            });
          } else {
            _self.$message({
              type: "error",
              showClose: true,
              message: `操作失败！`
            });
          }
        });
      }
    },
    //修改提交
    update() {
      let _self = this;
      let flag = true;
      _self.$refs["form"].validate((valid, errors) => {
        if (!valid) {
          flag = false;
          _self.tabPosition = "0";
        } else {
          if (
            _self.imgFile[0].raw == undefined &&
            _self.formStr == JSON.stringify(_self.form)
          ) {
            //未修改
            flag = false;
            this.$message("您还未修改数据哦！");
            _self.tabPosition = "0";
          }
        }
      });
      _self.$refs["changeReasonForm"].validate((valid, errors) => {
        if (!flag) {
          _self.tabPosition = "0";
        } else {
          if (!(flag && valid)) {
            flag = false;
            _self.tabPosition = "1";
          }
        }
      });
      if (flag) {
        _self.form.validDate = parseTime(_self.form.validDate);
        _self.form.issuingDate = parseTime(_self.form.issuingDate);
        let param = new FormData();
        param.append("EnterpriseProduct", JSON.stringify(_self.form));
        param.append("reason", _self.changeVerifyForm.reason);
        if (_self.imgFile[0].raw != undefined) {
          //判断图片是否被更新
          param.append("file", _self.imgFile[0].raw);
        } else {
          param.append("file", null);
        }
        changeEnterpriseProduct(param).then(response => {
          _self.dialogFormVisible = false;
          _self.getList();
          if (response.rel) {
            _self.$message({
              type: "success",
              showClose: true,
              message: `操作成功！`
            });
          } else {
            _self.$message({
              type: "error",
              showClose: true,
              message: `操作失败！`
            });
          }
        });
      }
    },
    handleDelete(row) {
      this.$confirm("此操作会将产品永久停产, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delObj(row.productId)
            .then(() => {
              this.$message({
                showClose: true,
                message: "操作成功",
                type: "success",
                duration: 2000
              });
              this.getList();
            })
            .catch(() => {});
        })
        .catch(() => {});
    },

    //变更记录查询
    changeView(row) {
      getChangeByCertificateId({ certificateId: row.certificateId }).then(
        response => {
          // console.log(response);
        }
      );
    },
    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    //删除图片
    handleImageRemove(file) {
      this.imgFile = [];
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
      if (this.imgFile.length > 0) {
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
      this.$refs["changeReasonForm"].resetFields();
    },

    certificateStateFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["证书状态"]);
    },
    factoryInspectionFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["产品出厂检验方式"]);
    },
    exportFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["出口产品"]);
    },
    infoTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["产品信息类型"]);
    },
    productTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.allProductTypeMap);
    },
    timeFormatter(row, column, cellValue) {
      return parseTime(cellValue, "{y}-{m}-{d}");
    },
    statusFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      if (cellValue == "1") {
        return "正常";
      } else {
        return "停产";
      }
    }
  }
};
</script>

<style scoped>
</style>
