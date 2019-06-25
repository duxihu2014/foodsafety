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
          <div style="height: 320px;">
            <el-form :model="form" label-width="120px" disabled>
                <el-row>
                <el-col :span="10">
                  <el-form-item label="原料名称:" prop="materialName">
                    <el-input v-model.trim="form.materialName" placeholder="请输入原料名称" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="原料类型:" prop="materialType">
                    <el-select v-model="form.materialType" placeholder="请选择" clearable >
                      <el-option v-for="item in  materialTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
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
                  <el-form-item label="年需求量:" prop="annualDemand">
                    <el-input v-model.trim="form.annualDemand" placeholder="请输入年需求量" ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
              <el-form-item label="执行标准代号:" prop="productStandard">
                <el-input v-model.trim="form.productStandard" placeholder="请输入执行标准代号" ></el-input>
              </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="生产厂家:" prop="manufacturer">
                    <el-input v-model.trim="form.manufacturer" placeholder="请输入生产厂家" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="国别:" prop="country">
                    <el-input v-model.trim="form.country" placeholder="请输入国别" ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="20">
              <el-form-item label="生产许可证编号:" prop="licenseNo">
                <el-input v-model.trim="form.licenseNo" placeholder="请输入生产许可证编号" ></el-input>
              </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-dialog>
    </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  verifyQuery,
  getChangeById
} from "api/admin/enterprise/material/index";
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
        dataType: "5",
        verifyStatus: "",
        enterpriseId: "",
        page: 1,
        limit: 20
      },
      needFixedRight: false,
      dialogFormVisible: false,
      form: {}
    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),
    verifyStatusOptions() {
      return this.staticData["变更审核状态"];
    },
    materialTypeOptions() {
      return this.staticData["原料类型"];
    },
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
    this.height = loadGridHeight();
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
      });
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
