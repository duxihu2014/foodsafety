<template>
    <div>
        <fieldset class="fieldset">
            <legend class="legend">查询条件</legend>
            <div class="field-box">
                <el-form :inline="true" :model="listQuery">
                    <el-form-item label="审批状态" prop="certificateType" class="filter-item">
                        <el-select v-model="listQuery.verifyStatus" placeholder="请选择" clearable>
                            <el-option v-for="item in  verifyStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item class="filter-item">
                        <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
                        <el-button v-waves @click="resetQuery()">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </fieldset>
        <el-table :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" :height="height">
            <el-table-column align="center" label="变更操作" width="100" prop="changeType" :formatter="changeTypeFormatter"></el-table-column>
            <el-table-column align="center" label="变更原因" width="300" prop="changeReason" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="变更人员" width="100" prop="changeUserName" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="变更时间" width="180" prop="changeTime"></el-table-column>
            <el-table-column align="center" label="审批状态" width="100" prop="verifyStatus">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.verifyStatus=='1'">{{verifyStatusFormatter(scope.row.verifyStatus)}}</el-tag>
                    <el-tag type="success" v-else-if="scope.row.verifyStatus=='2'">{{verifyStatusFormatter(scope.row.verifyStatus)}}</el-tag>
                    <el-tag type="danger" v-else-if="scope.row.verifyStatus=='3'">{{verifyStatusFormatter(scope.row.verifyStatus)}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" label="审批结论" width="300" prop="verifyConclusion" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="审批人员" width="100" prop="verifyUser" show-overflow-tooltip></el-table-column>
            <el-table-column align="center" label="审批时间" width="160" prop="verifyTime"></el-table-column>
            <el-table-column align="center" fixed="right" label="操作" width="100" v-if="needFixedRight">
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="100" v-else>
                <template slot-scope="scope">
                    <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div v-show="!listLoading" class="pagination-container">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
        </div>
        <el-dialog  title="变更后信息" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
            <el-form :model="form" label-width="120px" disabled>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="产品名称:" prop="productName">
                    <el-input v-model.trim="form.productName" placeholder="请输入产品名称" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="产品类型:" prop="productTypeId">
                    <el-cascader v-model="selectProdeuctTypes" :options="productTypeOptions" :show-all-levels="true" expand-trigger="hover" :change-on-select="false" @change="setProductTypeId" :props="{label:'text', value: 'id'}" >
                    </el-cascader>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="许可证编号:" prop="licenseNo">
                    <el-input v-model.trim="form.licenseNo" placeholder="请输入许可证编号" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="证书状态:" prop="certificateState">
                    <el-select v-model="form.certificateState" placeholder="请选择证书状态" clearable >
                      <el-option v-for="item in  certificateStateOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="22">
                  <el-form-item label="发证机构:" prop="issuingInstitution">
                    <el-input v-model.trim="form.issuingInstitution" placeholder="请输入发证机构" ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="9">
                  <el-form-item label="发证日期:" prop="issuingDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.issuingDate" ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="9" :offset="2">
                  <el-form-item label="有效期:" prop="validDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.validDate" ></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="规格型号:" prop="specificationModel">
                    <el-input v-model.trim="form.specificationModel" placeholder="请输入规格型号" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="出厂检验方式:" prop="factoryInspection">
                    <el-select v-model="form.factoryInspection" placeholder="请选择出厂检验方式" clearable >
                      <el-option v-for="item in  factoryInspectionOption" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="产品执行标准:" prop="productStandard">
                    <el-input v-model.trim="form.productStandard" placeholder="请输入产品执行标准" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="标准分类:" prop="standardType">
                    <el-input v-model.trim="form.standardType" placeholder="请输入标准分类" ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="注册商标:" prop="registeredTrademark">
                    <el-input v-model.trim="form.registeredTrademark" placeholder="请输入注册商标" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="信息类型:">
                    <el-select v-model="form.infoType" placeholder="请选择信息类型" clearable >
                      <el-option v-for="item in  infoTypeOption" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="销售区域:" prop="salesArea">
                    <el-input v-model.trim="form.salesArea" placeholder="请输入销售区域" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="是否出口产品:" prop="isExport" >
                    <el-radio v-model="form.isExport" label="1">是</el-radio>
                    <el-radio v-model="form.isExport" label="0">否</el-radio>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="产品图片:" prop="imgFile" ref="imgFile">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" :file-list="imgFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="1" :auto-upload="false">
                  <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
                  <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
              </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import { verifyQuery, getChangeById } from "api/admin/enterprise/product/index";
import { getResource } from "api/admin/register/index";
import { parseValueToText, parseTime } from "utils/index";
import { loadGridHeight } from "api/screen";

export default {
  name: "base",
  data() {
    return {
      height: undefined,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        dataType: "4",
        verifyStatus: "",
        enterpriseId: "",
        page: 1,
        limit: 20
      },
      needFixedRight: false,
      dialogFormVisible: false,
      serverImageUrl: process.env.SERVERIMAGEURL,
      imgFile: [],
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      form: {}
    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),
    verifyStatusOptions() {
      return this.staticData["变更审核状态"];
    },
    certificateTypeOptions() {
      return this.staticData["证照类型"];
    }
  },
  created() {
    this.getList();
  },
  mounted() {
    //首次整个视图都渲染完毕后执行
    this.$nextTick(function () {
      let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
      this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
    });
    //监听屏幕的改变
    window.onresize = () => {
      let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
      this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
    };
    this.height = loadGridHeight() - 50 ;
  },
  methods: {
    getList() {
      this.listQuery.enterpriseId = this.user.enterpriseId;
      verifyQuery(this.listQuery).then(response => {
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
      this.listQuery.verifyStatus = "";
      this.listQuery.enterpriseId = "";
    },
    handleView(row) {
      this.dialogFormVisible = true;
      getChangeById(row.changeId).then(response => {
        this.form = response.data.formData;
        let resourceId = this.form.productPicture;
        getResource(resourceId).then(response => {
          this.imgFile = [];
          this.imgFile.push({
            url: this.serverImageUrl + "/" + response.data.resourcePath
          });
          $(".el-upload--picture-card").hide();
        });
      });
    },
    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    changeTypeFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData["企业变更操作类型"]);
    },
    changeTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业变更操作类型"]);
    },
    verifyStatusFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData["变更审核状态"]);
    }
  }
};
</script>

<style scoped>
</style>
