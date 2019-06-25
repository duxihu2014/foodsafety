<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item label="原料名称" prop="materialNameLike" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.materialNameLike"> </el-input>
          </el-form-item>
          <el-form-item label="条形码" prop="barcodeCodingLike" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.barcodeCodingLike"> </el-input>
          </el-form-item>
          <el-form-item label="原料类型" prop="materialCategory" ref="certificateNumber" tab="1">
            <el-select v-model="listQuery.materialCategory" placeholder="请选择" clearable>
              <el-option v-for="item in  materialCategoryOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="采购日期" prop="purchaseDate" class="filter-item">
            <el-date-picker v-model="listQuery.purchaseDateArr" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

    </fieldset>
    <el-table :key='tableKey' :data="tableList" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">

      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column v-if="user.userType != 4" width="100px" align="center" label="所属企业" prop="enterpriseId" :formatter="function(row,column,cellValue){return enterpriseformatter(row.enterpriseId)}"></el-table-column>
      <el-table-column width="150px" align="center" label="原料名称" prop="materialName"></el-table-column>
      <el-table-column width="150px" align="center" label="原料类型" prop="materialCategory" :formatter="materialCategoryFormatter"></el-table-column>
      <el-table-column width="150px" align="center" label="条形码" prop="barcodeCoding"></el-table-column>
      <el-table-column width="180px" align="center" label="生产日期" prop="productionDate" :formatter="installTimeFormatter"></el-table-column>
      <el-table-column width="180px" align="center" label="采购日期" prop="purchaseDate" ></el-table-column>
      <el-table-column width="100px" align="center" label="采购数量" prop="purchaseQuantity"></el-table-column>
      <el-table-column width="100px" align="center" label="规格" prop="specification"></el-table-column>
      <el-table-column width="100px" align="center" label="保质期(天)" prop="shelfLife"></el-table-column>
      <el-table-column width="100px" align="center" label="计量单位" prop="unit"></el-table-column>
      <el-table-column width="150px" align="center" label="产地" prop="originPlace"></el-table-column>
      <el-table-column width="100px" align="center" label="品牌" prop="brand"></el-table-column>
      <el-table-column width="250px" align="center" label="生产企业" prop="productionName"></el-table-column>
      <el-table-column width="150px" align="center" label="生产许可证编号" prop="productionCertificateNo"></el-table-column>
      <el-table-column width="250px" align="center" label="供货商名称" prop="supplierName"></el-table-column>
      <el-table-column width="150px" align="center" label="供货商营业执照编码" prop="businessLicenceNoSupplier"></el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
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

      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <div style="height: 330px;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="基本信息">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="原料名称" prop="materialId" ref="materialId" tab="0">
                    <el-select v-model="form.materialId" placeholder="请选择" @change="handleMaterialChange" :disabled="userManager_view">
                      <el-option v-for="item in materialsOption" :key="item.materialId" :label="item.materialName" :value="item.materialId">
                        <span style="float: left">{{ item.materialName }}</span>
                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.barcodeCoding }}</span>
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="条形码" prop="barcodeCoding">
                    <el-input v-model.trim="form.barcodeCoding" placeholder="" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="生产日期" prop="productionDate" ref="productionDate" tab="0">
                    <el-date-picker v-model="form.productionDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" clearable :disabled="userManager_view">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="采购日期" prop="purchaseDate" ref="purchaseDate" tab="0">
                    <el-date-picker v-model="form.purchaseDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss" clearable :disabled="userManager_view">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="数量" prop="purchaseQuantity" ref="purchaseQuantity" tab="0">
                    <el-input-number v-model.trim="form.purchaseQuantity" :min="1" label="" :disabled="userManager_view"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="规格" prop="specification">
                    <el-input v-model.trim="form.specification" placeholder="" width="100px" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="单位" prop="unit">
                    <el-input v-model.trim="form.unit" placeholder="" width="100px" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="12">
                  <el-form-item label="保质期" prop="shelfLife" ref="shelfLife" tab="0">
                    <el-input-number v-model.trim="form.shelfLife" :min="1" label="" :disabled="userManager_view"></el-input-number>天
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="供货商" prop="supplierName">
                    <el-input v-model.trim="form.supplierName" placeholder="" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="userManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getObj,
  addObj,
  putObj,
  delObjs,
  getMaterialsMap,
  getAllMaterialsMap,
  getMaterial
} from "api/admin/purchase/prepackaging/index";
import { getAllEnterpriseMap } from "api/admin/equipment/audio";
import { getResource } from "api/admin/resource/index";
import { mapGetters } from "vuex";
import {
  parseValueToText,
  parseTime,
  parseForm,
  CompareDateTime
} from "utils/index";
import { loadGridHeight } from "api/screen";
import queryConditions from "components/QueryConditions/index";
export default {
  name: "audio",
  components: { queryConditions },
  data() {
    const checkDateValidator = (rule, value, callback) => {
      if (CompareDateTime(value, this.form.productionDate)) {
        callback("采购日期必须在生产日期之后！");
      } else {
        callback();
      }
    };
    return {
      height: undefined,
      needFixedRight: false,
      //--查询
      listQuery: {
        page: 1,
        limit: 20,
        materialNameLike: undefined,
        barcodeCodingLike: undefined,
        materialCategory: undefined, //
        purchaseDateArr: [],
        purchaseDateStart: undefined,
        purchaseDateEnd: undefined,
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined,
        extendSql: " and ISNULL(V.INSPECTION_REPORT_ID)"
      },
      //--表格
      tableKey: 0,
      tableList: null,
      total: null,
      listLoading: true,
      userSelection: [],
      //--对话框、 按钮
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        view: "查看"
      },
      userManager_btn_edit: true,
      userManager_view: false,
      userManager_update: true,
      //--表单
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址

      form: {
        purchaseId: undefined,
        enterpriseId: undefined,
        supplierId: undefined,
        supplierName: undefined,
        businessLicenceNoSupplier: undefined,
        materialId: undefined,
        materialName: undefined,
        materialCategory: undefined, //原材料的类型(预包装食品、散装食品、食品相关产品、食品添加剂、农产品和其他产品)
        barcodeCoding: undefined, //原材料的条码，散装食品和食用农产品没有条码，需要自己编写条码。
        specification: undefined,
        unit: undefined,
        shelfLife: undefined,
        originPlace: undefined,
        brand: undefined,
        productionName: undefined,
        productionCertificateNo: undefined,
        productionDate: undefined,
        purchaseDate: undefined,
        purchaseQuantity: undefined,

        inspectionReportFlag: false, // 是否有质检信息 ，true :有，false:无
        inspectionReportPhoto: undefined, // 质检报告图片
        validDate: undefined // 质检有效期
      },
      rules: {
        materialId: [{ required: true, message: "原料名称不能为空" }],
        purchaseDate: [
          { required: true, message: "采购日期不能为空" },
          { validator: checkDateValidator }
        ],
        purchaseQuantity: [{ required: true, message: "采购数量不能为空" }],
        shelfLife: [{ required: true, message: "保质期不能为空" }]
      },

      //--
      inspectionReportPhoto: undefined, //
      //--
      allEnterpriseOptions: [],
      materialsOption: [],
      tabPosition: "0",
      serverImageUrl: process.env.SERVERIMAGEURL
    };
  },
  created() {
    this.intiParam();
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

  },
  updated () {
    this.height=loadGridHeight();
  },
  computed: {
    ...mapGetters(["staticData", "user"]),
    materialCategoryOptions() {
      return this.staticData["原料类型"];
    }
  },
  methods: {
    setAreaId(data) {
      this.listQuery.areaId = data;
    },
    setGridId(data) {
      this.listQuery.gridId = data;
    },
    setEnterpriseId(data) {
      this.listQuery.enterpriseId = data;
    },
    intiParam() {
      let _this = this;
      if (_this.user.userType == 4) {
        _this.listQuery.enterpriseId = _this.user.enterpriseId;
        let materialQueryParam = {
          enterpriseId: this.user.enterpriseId
        };
        //原料信息
        _this.getMaterialsOption(materialQueryParam);
      } else {
        _this.getMaterialsOption(null);
      }
      //企业信息
      getAllEnterpriseMap()
        .then(function(response) {
          _this.allEnterpriseOptions = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "企业信息获取失败。"
          });
        });
    },
    //获取原料信息
    getMaterialsOption(materialQueryParam) {
      let _this = this;
      getAllMaterialsMap(materialQueryParam)
        .then(function(response) {
          _this.materialsOption = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "原料信息获取失败。"
          });
        });
    },
    getList() {
      this.listLoading = true;
      page(this.listQuery).then(response => {
        this.tableList = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    //查询
    handleFilter() {
      if (this.listQuery.purchaseDateArr.length > 0) {
        this.listQuery.purchaseDateStart = this.listQuery.purchaseDateArr[0];
        this.listQuery.purchaseDateEnd = this.listQuery.purchaseDateArr[1];
      }
      this.listQuery.page = 1;
      this.getList();
    },
    //重置查询条件
    resetQuery() {
      this.listQuery = {
        materialNameLike: undefined,
        barcodeCodingLike: undefined,
        materialCategory: undefined, //
        purchaseDateArr: [],
        purchaseDateStart: undefined,
        purchaseDateEnd: undefined,
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined,
        extendSql: " and ISNULL(V.INSPECTION_REPORT_ID)"
      };
      if (this.user.userType == 4) {
        this.listQuery.enterpriseId = this.user.enterpriseId;
      }
    },
    //跳转到指定页
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    //跳转到指定页
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    //选择当前行
    getSelection(selection) {
      this.userSelection = selection;
    },
    //表格样式
    tableRowClassName({ row, rowIndex }) {
      if (row.userStatus === "0") {
        return "warning-row";
      } else if (row.userStatus === "1") {
        return "success-row";
      }
      return "";
    },

    //
    handleMaterialChange(val) {
      getMaterial(val).then(response => {
        let item = response.data;
        this.form.materialId = item.materialId;
        this.form.materialName = item.materialName;
        this.form.barcodeCoding = item.barcodeCoding;
        this.form.specification = item.specification;
        this.form.unit = item.unit;
        this.form.shelfLife = item.shelfLife;
        this.form.originPlace = item.originPlace;
        this.form.brand = item.brand;
        this.form.productionName = item.productionName;
        this.form.productionCertificateNo = item.productionCertificateNo; //生产许可证号
        this.form.supplierId = item.supplierId;
        this.form.supplierName = item.supplierName;
        this.form.businessLicenceNoSupplier = item.businessLicenceNo
          ? item.businessLicenceNo
          : ""; //营业执照号
      });
    },
    handleView(row) {
      this.form = row;
      this.userManager_update = false;
      this.userManager_view = true;
      this.dialogFormVisible = true;
      this.dialogStatus = "view";
    },
    cancel(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    //重置表单
    resetTmp() {
      this.form = {
        purchaseId: undefined,
        enterpriseId: this.user.enterpriseId,
        supplierId: undefined,
        supplierName: undefined,
        businessLicenceNoSupplier: undefined,
        materialId: undefined,
        materialName: undefined,
        materialCategory: "1",
        barcodeCoding: undefined,
        specification: undefined,
        unit: undefined,
        shelfLife: undefined,
        originPlace: undefined,
        brand: undefined,
        productionName: undefined,
        productionCertificateNo: undefined,
        productionDate: undefined,
        purchaseDate: undefined,
        purchaseQuantity: undefined,
        inspectionReportFlag: false, // 是否有质检信息 ，true :有，false:无
        inspectionReportPhoto: undefined, // 质检报告图片
        validDate: undefined // 质检有效期
      };

      this.tabPosition = "0";

      this.$nextTick(function() {
        this.$refs["form"].clearValidate();
      });
    },

    installTimeFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      return parseTime(cellValue, "{y}-{m}-{d}");
    },

    enterpriseformatter(param) {
      if (!param) {
        return "";
      }
      if (!this.allEnterpriseOptions.length) {
        return param;
      }
      return this.allEnterpriseOptions.filter(item => {
        return item.value == param;
      })[0].text;
    },
    //关闭对话框
    closeDialog() {
      this.dialogFormVisible = false;
      this.getList();
    },

    materialCategoryFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.materialCategoryOptions);
    }
  } //end methods
};
</script>

