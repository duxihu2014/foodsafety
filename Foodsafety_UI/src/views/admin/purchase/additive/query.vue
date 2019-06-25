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
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

    </fieldset>

    <el-table :key='tableKey' :data="tableList" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">

      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column width="200px" align="center" label="所属企业" prop="enterpriseId" :formatter="function(row,column,cellValue){return enterpriseformatter(row.enterpriseId)}"></el-table-column>
      <el-table-column width="150px" align="center" label="原料名称" prop="materialName"></el-table-column>
      <el-table-column width="150px" align="center" label="条形码" prop="barcodeCoding"></el-table-column>
      <el-table-column width="180px" align="center" label="生产日期" prop="productionDate" :formatter="installTimeFormatter"></el-table-column>
      <el-table-column width="180px" align="center" label="进货日期" prop="purchaseDate" ></el-table-column>
      <el-table-column width="100px" align="center" label="采购数量" prop="purchaseQuantity"></el-table-column>
      <el-table-column width="100px" align="center" label="规格" prop="specification"></el-table-column>
      <el-table-column width="100px" align="center" label="保质期(天)" prop="shelfLife"></el-table-column>
      <el-table-column width="100px" align="center" label="计量单位" prop="unit"></el-table-column>
      <el-table-column width="150px" align="center" label="产地" prop="originPlace"></el-table-column>
      <el-table-column width="100px" align="center" label="品牌" prop="brand"></el-table-column>
      <el-table-column width="250px" align="center" label="生产企业" prop="productionName"></el-table-column>
      <el-table-column width="150px" align="center" label="生产许可证编号" prop="productionCertificateNo"></el-table-column>
      <el-table-column width="180px" align="center" label="有效期" prop="validDate" :formatter="installTimeFormatter"></el-table-column>
      <el-table-column width="150px" align="center" label="质检报告" prop="inspectionReportPhoto">
        <template slot-scope="scope">
          <el-button v-show="scope.row.inspectionReportPhoto" size="mini" type="primary" plain @click="handlePhoto(scope.row.inspectionReportPhoto)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column width="250px" align="center" label="供货商名称" prop="supplierName"></el-table-column>
      <el-table-column width="150px" align="center" label="供货商营业执照编码" prop="businessLicenceNoSupplier"></el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" @click="handleView(scope.row)">查看</el-button>
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
        <div>
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="基本信息">
              <el-row>
                <el-col :span="9">
                  <el-form-item label="原料名称" prop="materialId" ref="materialId" tab="0">
                    <el-select v-model="form.materialId" placeholder="请选择" @change="handleMaterialChange" :disabled="userManager_view">
                      <el-option v-for="item in materialsOption" :key="item.materialId" :label="item.materialName" :value="item.materialId">
                        <span style="float: left">{{ item.materialName }}</span>
                        <span style="float: right; color: #8492a6; font-size: 13px">{{ item.barcodeCoding }}</span>
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="3">
                  <el-form-item label="" prop="inspectionReportFlag" ref="inspectionReportFlag" tab="0" label-width="0px">
                    <el-checkbox v-model="form.inspectionReportFlag" @change="handleCheckChange" :disabled="userManager_view">质检信息</el-checkbox>
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
                  <el-form-item label="进货日期" prop="purchaseDate" ref="purchaseDate" tab="0">
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
              <div id="report">
                <fieldset class="fieldset">
                  <legend class="legend">质检信息</legend>
                  <el-row style="margin-top: 10px;">
                    <el-col>
                      <el-form-item label="有效期" prop="validDate" ref="validDate" v-if="!reportDisable" :rules="{required: true, message: '有效期不能为空'}">
                        <el-date-picker v-model="form.validDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1" :disabled="userManager_view"></el-date-picker>
                      </el-form-item>
                      <el-form-item label="有效期" prop="validDate" ref="validDate" v-else>
                        <el-date-picker v-model="form.validDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1" disabled></el-date-picker>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col>
                      <el-form-item label="质检照片" prop="inspectionReportPhoto" ref="inspectionReport" v-if="!reportDisable" :rules="{required: true, message: '质检报告照片不能为空'}" :disabled="userManager_view">
                        <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :file-list="inspectionReportPhotoFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="1" :auto-upload="false">
                          <i class="el-icon-plus"></i>
                        </el-upload>
                      </el-form-item>
                      <el-form-item label="质检照片" prop="inspectionReportPhoto" ref="inspectionReport" v-else>
                        <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :file-list="inspectionReportPhotoFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="1" :auto-upload="false" disabled>
                          <i class="el-icon-plus"></i>
                        </el-upload>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </fieldset>
              </div>
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
  getMaterial
} from "api/admin/purchase/prepackaging/index";
import { getAllEnterpriseMap } from "api/admin/equipment/audio";
import { getResource } from "api/admin/resource/index";
import { mapGetters } from "vuex";
import { parseValueToText, parseTime, parseForm } from "utils/index";
import { loadGridHeight } from "api/screen";
import queryConditions from "components/QueryConditions/index";
export default {
  name: "audio",
  components: {queryConditions},
  data() {
    return {
      height: undefined,
      needFixedRight: false,
      //--查询
      listQuery: {
        page: 1,
        limit: 20,
        materialNameLike: undefined,
        barcodeCodingLike: undefined,
        materialCategory: "4", //
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined
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
      inspectionReportPhotoFile: [],
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      fileFormat: ["image/jpeg", "image/png", "image/bmp", "image/gif"], // 允许上传的文件格式

      form: {
        purchaseId: undefined,
        enterpriseId: undefined,
        supplierId: undefined,
        supplierName: undefined,
        businessLicenceNoSupplier: undefined,
        materialId: undefined,
        materialName: undefined,
        materialCategory: "4", //原材料的类型(预包装食品、散装食品、食品相关产品、食品添加剂、农产品和其他产品)
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
        productionDate: [{ required: true, message: "生产日期不能为空" }],
        purchaseDate: [{ required: true, message: "进货日期不能为空" }],
        purchaseQuantity: [{ required: true, message: "采购数量不能为空" }],
        shelfLife: [{ required: true, message: "保质期不能为空" }]
      },
      pickerOptions1: {
        disabledDate(time) {
          return time.getTime() < Date.now();
        }
      },
      //--
      enterpriseFlag: false,
      reportDisable: true, //质检信息
      inspectionReportPhoto: undefined, //
      //--
      materialsOption: undefined,
      tabPosition: "0",
      serverImageUrl: process.env.SERVERIMAGEURL,
      updete_reportVal_Flag: false, //编辑信息中是否有质检报告信息
      update_bak_validDate: undefined, //编辑时用来备份质检信息中的生效日期
      update_bak_inspectionReportPhotoFile: [] //编辑时用来备份质检信息中的图片信息
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
    ...mapGetters(["staticData", "user"])
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
      this.listQuery.page = 1;
      this.getList();
    },
    //重置查询条件
    resetQuery() {
      this.listQuery = {
        materialNameLike: undefined,
        barcodeCodingLike: undefined,
        materialCategory: "4", //
        areaId: undefined,
        gridId: undefined,
        enterpriseId: undefined
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
    handleCheckChange(val) {
      if (val) {
        this.reportDisable = false;
        if (this.dialogStatus == "update" && this.updete_reportVal_Flag) {
          this.form.validDate = this.update_bak_validDate;
          this.inspectionReportPhotoFile = this.update_bak_inspectionReportPhotoFile;
          $("#pane-0 .el-upload--picture-card").hide();
        }
      } else {
        this.reportDisable = true;
        this.form.validDate = undefined;
        this.inspectionReportPhotoFile = [];
        $("#pane-0 .el-upload--picture-card").show();
      }
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
    handlePhoto(id) {
      getResource(id).then(response => {
        if (response.rel) {
          this.dialogImageUrl =
            this.serverImageUrl + "/" + response.data.resourcePath;
          this.dialogImageVisible = true;
        } else {
          this.$message({
            type: "error",
            showClose: true,
            message: `资源获取失败！`
          });
        }
      });
    },
    //
    handleCreate() {
      this.userManager_update = true;
      this.userManager_view = false;
      this.dialogFormVisible = true;
      this.dialogStatus = "create";
      this.resetTmp();
    },
    handleView(row) {
      this.form = row;
      if (row.inspectionReportId) {
        this.form.inspectionReportFlag = true;
        this.updete_reportVal_Flag = true;

        getResource(row.inspectionReportPhoto).then(response => {
          this.inspectionReportPhotoFile = [];
          this.inspectionReportPhotoFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });

          $("#pane-0 .el-upload--picture-card").hide();
        });
      } else {
        this.form.inspectionReportFlag = false;
        this.form.inspectionReportPhoto = "";
        this.inspectionReportPhotoFile = [];
        $("#pane-0 .el-upload--picture-card").show();
      }

      this.userManager_update = false;
      this.userManager_view = true;
      this.dialogFormVisible = true;
      this.dialogStatus = "view";
    },
    handleUpdate(row) {
      this.form = row;

      if (row.inspectionReportId) {
        this.form.inspectionReportFlag = true;
        this.reportDisable = false;
        this.updete_reportVal_Flag = true;
        this.update_bak_validDate = this.form.validDate;

        getResource(row.inspectionReportPhoto).then(response => {
          this.inspectionReportPhotoFile = [];
          this.update_bak_inspectionReportPhotoFile = [];
          this.inspectionReportPhotoFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });
          this.update_bak_inspectionReportPhotoFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });
          $("#pane-0 .el-upload--picture-card").hide();
        });
      } else {
        this.form.inspectionReportFlag = false;
        this.reportDisable = true;
        this.updete_reportVal_Flag = false;
        this.form.inspectionReportPhoto = "";
        this.inspectionReportPhotoFile = [];
        $("#pane-0 .el-upload--picture-card").show();
      }

      this.userManager_update = true;
      this.userManager_view = false;
      this.dialogFormVisible = true;
      this.dialogStatus = "update";
    },
    //  点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    handleImageRemove(file) {
      this.inspectionReportPhotoFile = [];
      if (this.inspectionReportPhotoFile.length == 0) {
        $("#pane-0 .el-upload--picture-card").show();
        this.form.inspectionReportPhoto = undefined; //此处设值，主要用于页面验证
      }
    },
    addImgFile(file) {
      if (this.fileFormat.indexOf(file.raw.type) < 0) {
        this.$message({
          type: "error",
          showClose: true,
          message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
        });
        $("#pane-0 .el-upload--picture-card").hide();
        return false;
      }
      this.inspectionReportPhotoFile.push(file);
      if (this.inspectionReportPhotoFile.length > 0) {
        $("#pane-0 .el-upload--picture-card").hide();
      }
    },
    handleDelete() {
      if (!this.userSelection.length) {
        this.$message({
          showClose: true,
          message: "请先选择一条信息！"
        });
        return;
      }
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          const idArr = [];
          this.userSelection.forEach(item => {
            idArr.push(item.purchaseId);
          });
          delObjs(idArr.join(","))
            .then(() => {
              this.$message({
                showClose: true,
                message: "删除成功",
                type: "success",
                duration: 2000
              });
              this.getList();
            })
            .catch(() => {});
        })
        .catch(() => {});
    },
    create(formName) {
      if (this.inspectionReportPhotoFile.length > 0) {
        this.form.inspectionReportPhoto = this.inspectionReportPhotoFile[0].name;
      } else {
        this.form.inspectionReportPhoto = undefined;
      }

      const set = this.$refs;
      set[formName].validate((valid, errors) => {
        if (valid) {
          this.form.inspectionReportPhoto = undefined;

          let param = new FormData();
          param.append("formData", parseForm(this.form));

          if (this.inspectionReportPhotoFile.length > 0) {
            param.append(
              "inspectionReportPhotoFile",
              this.inspectionReportPhotoFile[0].raw
            );
          }

          addObj(param).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
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
      if (this.inspectionReportPhotoFile.length > 0) {
        if (this.inspectionReportPhotoFile[0].name) {
          this.form.inspectionReportPhoto = this.inspectionReportPhotoFile[0].name;
        }
      } else {
        this.form.inspectionReportPhoto = undefined;
      }

      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          //未选质检报告
          if (!this.form.inspectionReportFlag) {
            this.form.validDate = undefined;
            this.inspectionReportPhotoFile = [];
          }
          let param = new FormData();
          param.append("formData", parseForm(this.form));
          if (this.inspectionReportPhotoFile.length > 0) {
            if (this.inspectionReportPhotoFile[0].raw) {
              param.append("inspectionReportPhoto", "1"); // 修改过照片，需要更新。
              param.append(
                "inspectionReportPhotoFile",
                this.inspectionReportPhotoFile[0].raw
              );
            } else {
              param.append("inspectionReportPhoto", "0"); // 未修改照片，不需要更新。
            }
          } else {
            param.append("inspectionReportPhoto", "-1"); // 删除照片
          }
          putObj(param).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
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
        materialCategory: "4",
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

      // this.$refs["inspectionReport"].resetField(); //当添加图片后，重置图片验证功能。
      //清除图片
      this.inspectionReportPhotoFile = [];
      $("#pane-0 .el-upload--picture-card").show();

      if (this.user.userType == 4) {
        this.enterpriseFlag = true;
      }
      this.tabPosition = "0";

      this.$nextTick(function() {
        this.$refs["form"].clearValidate();
      });
    },
    specialFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      return cellValue == "1" ? "是" : "否";
    },
    equStatusFormatter(row, column, cellValue) {
      let rsMsg = "";
      if (!cellValue) {
        rsMsg = "";
      }
      if (cellValue == "0") {
        rsMsg = "未知";
      } else if (cellValue == "1") {
        rsMsg = "在线";
      } else if (cellValue == "2") {
        rsMsg = "离线";
      }
      return rsMsg;
    },
    installTimeFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      return parseTime(cellValue, "{y}-{m}-{d}");
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["状态"]);
    },
    //工号格式化
    opUserformatter(param) {
      if (!param) {
        return "";
      }
      if (!this.allUserOptions.length) {
        return param;
      }
      return this.allUserOptions.filter(item => {
        return item.value == param;
      })[0].text;
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
      this.dialogCameraFormVisible = false;
      this.getList();
    }
  } //end methods
};
</script>

