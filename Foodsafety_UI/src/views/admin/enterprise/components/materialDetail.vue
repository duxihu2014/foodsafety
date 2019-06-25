<template>
  <div>
    <div v-if='dataFlag'>
      <el-table :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" :row-class-name="tableRowClassName" height="75vh" tooltip-effect="dark">
        <el-table-column align="center" label="原料名称" width="200" prop="materialName"></el-table-column>
        <el-table-column align="center" label="原料类型" width="200" prop="materialType" :formatter="materialTypeFormatter"></el-table-column>
        <el-table-column align="center" label="规格型号" width="200" prop="specificationModel"></el-table-column>
        <el-table-column align="center" label="年需求量" width="250" prop="annualDemand"></el-table-column>
        <el-table-column align="center" label="执行标准代号 " width="150" prop="productStandard"></el-table-column>
        <el-table-column align="center" label="生产厂家" width="150" prop="manufacturer"></el-table-column>
        <el-table-column align="center" label="国别" width="150" prop="country"></el-table-column>
        <el-table-column align="center" label="生产许可证编号" width="150" prop="licenseNo"></el-table-column>
        <el-table-column align="center" label="变更状态" width="100" prop="verifyStatus">
          <template slot-scope="scope">
            <el-tag type="warning" v-if="scope.row.verifyStatus=='1'">审核中</el-tag>
            <el-tag type="success" v-else-if="scope.row.verifyStatus=='2'||scope.row.verifyStatus=='3'">完成</el-tag>
            <el-tag type="info" v-else>未变更</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="变更原因" width="200" prop="changeReason" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" label="原料状态" width="100" prop="materialStatus" :formatter="statusFormatter"></el-table-column>
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
      <!--修改，查看，新增表单对话框-->
      <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false" :modal="false">
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
        <div style="height: 600px;">
          <el-tabs v-model="tabPosition">
            <el-tab-pane label="原料信息">
              <el-form :model="form" :rules="rules" ref="form" label-width="130px">
                <el-row>
                  <el-col :span="10">
                    <el-form-item label="原料名称:" prop="materialName">
                      <el-input v-model.trim="form.materialName" placeholder="请输入原料名称" :disabled="isView"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" :offset="2">
                    <el-form-item label="原料类型:" prop="materialType">
                      <el-select v-model="form.materialType" placeholder="请选择" clearable :disabled="isView">
                        <el-option v-for="item in  materialTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
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
                    <el-form-item label="年需求量:" prop="annualDemand">
                      <el-input v-model.trim="form.annualDemand" placeholder="请输入年需求量" :disabled="isView"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="执行标准代号:" prop="productStandard">
                  <el-input v-model.trim="form.productStandard" placeholder="请输入执行标准代号" :disabled="isView"></el-input>
                </el-form-item>
                <el-row>
                  <el-col :span="10">
                    <el-form-item label="生产厂家:" prop="manufacturer">
                      <el-input v-model.trim="form.manufacturer" placeholder="请输入生产厂家" :disabled="isView"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10" :offset="2">
                    <el-form-item label="国别:" prop="country">
                      <el-input v-model.trim="form.country" placeholder="请输入国别" :disabled="isView"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="生产许可证编号:" prop="licenseNo">
                  <el-input v-model.trim="form.licenseNo" placeholder="请输入生产许可证编号" :disabled="isView"></el-input>
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
    <div v-if='!dataFlag'>
      无原料数据
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getResource } from "api/admin/register/index";
import { parseValueToText, parseTime, findParentNode } from "utils/index";
import {
  page,
  getObj,
  getEnterpriseMaterial,
  addEnterpriseMaterial,
  changeEnterpriseMaterial,
  delObj
} from "api/admin/enterprise/material/index";
import { loadGridHeight } from "api/screen";
export default {
  name: "index",
  data() {
    return {
      dataFlag: true,
      tabPosition: "0",
      height: undefined,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        materialNameLike: undefined,
        materialType: undefined,
        enterpriseId: undefined,
        materialStatus: undefined
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

      form: { isExport: "0", productTypeId: undefined },
      formStr: "", //表单初始数据字符串，用于判断表单数据是否被修改
      rules: {}

    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),

    materialTypeOptions() {
      return this.staticData["原料类型"];
    },
    statusOptions() {
      return this.staticData["企业原料状态"];
    }
  },
  props: {
    enterpriseId: {
      default: undefined
    }
  },
  watch: {
    'enterpriseId': function() {
      this.getList();
    }
  },
  created() {
    this.initParam();
    this.getList();
  },
  mounted() {
    //首次整个视图都渲染完毕后执行
    this.$nextTick(function() {
      let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
      if(tableDiv){
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      }
    });
    //监听屏幕的改变
    window.onresize = () => {
      let tableDiv = this.$el.querySelector(".el-table__body-wrapper");
      if(tableDiv){
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      }
    };
    this.height = loadGridHeight();
  },
  methods: {
    initParam() {},
    getList() {
      this.listLoading = true;
      this.listQuery.enterpriseId = this.enterpriseId;
      page(this.listQuery).then(response => {
        if (response.total > 0) {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
          this.dataFlag = true;
        } else {
          this.listLoading = false;
          this.dataFlag = false;
        }
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
      this.listQuery.materialNameLike = undefined;
      this.listQuery.materialType = undefined;
      this.listQuery.materialStatus = undefined;
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
      getEnterpriseMaterial(row.materialId).then(response => {
        this.form = response.data;
        this.isView = true;
        this.dialogFormVisible = true;
        this.user_btn_update = false;
        this.dialogStatus = "view";
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


    closeDialog() {
      this.form = {};
      this.isView = false;
      this.isUpdate = false;
      this.$refs["form"].resetFields();
      this.verifyResultReason = {};

    },

    materialTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["原料类型"]);
    },
    timeFormatter(row, column, cellValue) {
      return parseTime(cellValue, "{y}-{m}-{d}");
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业原料状态"]);
    }
  }
};

</script>
<style scoped>
</style>
