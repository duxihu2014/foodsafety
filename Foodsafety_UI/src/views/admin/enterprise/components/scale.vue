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
      <!--     <el-table-column align="center" label="变更类型" width="100" prop="changeType">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.changeType=='1'">{{changeTypeFormatter(scope.row.changeType)}}</el-tag>
          <el-tag type="warning" v-else-if="scope.row.changeType=='2'">{{changeTypeFormatter(scope.row.changeType)}}</el-tag>
          <el-tag type="danger" v-else-if="scope.row.changeType=='3'">{{changeTypeFormatter(scope.row.changeType)}}</el-tag>
        </template>
      </el-table-column> -->

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
      <el-form :model="form" label-width="120px" :disabled="true">
        <el-form-item label="企业的规模:" prop="enterpriseScale">
          <el-select v-model="form.enterpriseScale">
            <el-option v-for="item in  enterpriseScaleOptions" :key="item.value" :label="item.text" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="10">
            <el-form-item label="企业总人数:" prop="enterpriseEmployees">
              <el-input v-model="form.enterpriseEmployees"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="专业技术人员数:" prop="enterpriseTechnicians">
              <el-input v-model="form.enterpriseTechnicians"></el-input>
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
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  scaleVerifyQuery,
  getChangeById
} from "api/admin/enterprise/scale/index";
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
        dataType: "3",
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
    enterpriseScaleOptions() {
      return this.staticData["企业规模"];
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
    this.height = loadGridHeight();
  },
  methods: {
    getList() {
      this.listQuery.enterpriseId = this.user.enterpriseId;
      scaleVerifyQuery(this.listQuery).then(response => {
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
