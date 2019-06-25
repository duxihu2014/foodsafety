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
      <el-form :model="form" label-width="100px" :disabled="true">
        <!--<el-row>
          <el-col :span="8">
            <el-form-item label="企业名称" prop="enterpriseName">
              <el-input v-model.trim="form.enterpriseName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="社会信用代码">
              <el-input v-model.trim="form.organizingInstitutionBarCode" :readonly="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业法人" prop="corporateRepresentative">
              <el-input v-model.trim="form.corporateRepresentative"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="身份证" prop="idCardNo">
              <el-input v-model.trim="form.idCardNo"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主体分类" prop="subjectClassification">
              <el-select v-model="form.subjectClassification" placeholder="请选择">
                <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经济性质" prop="economicNature">
              <el-select v-model="form.economicNature" placeholder="请选择">
                <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="注册资金" prop="registeredCapital">
              <el-input v-model.trim="form.registeredCapital" placeholder="注册资金">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属区域" prop="areaOptions">
              <el-cascader v-model="areaOptions" :options="areaData" :show-all-levels="true" expand-trigger="hover" :change-on-select="true" :props="{label:'text', value: 'id'}">
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="注册日期" prop="registerDate">
              <el-date-picker type="date" placeholder="请选择" v-model.trim="form.registerDate"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="经营期限" prop="operatingTerm">
              <el-date-picker type="date" placeholder="请选择" v-model.trim="form.operatingTerm"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业联系人" prop="contacts">
              <el-input v-model.trim="form.contacts" placeholder="联系人"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系人手机号" prop="contactMobile">
              <el-input v-model.trim="form.contactMobile"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="邮政编码" prop="postalCode">
              <el-input v-model.trim="form.postalCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="企业邮箱" prop="email">
              <el-input v-model.trim="form.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="contactNumber">
              <el-input v-model.trim="form.contactNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="传真" prop="fax">
              <el-input v-model.trim="form.fax"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="生产地址" prop="productionAddress">
              <el-input v-model.trim="form.productionAddress"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="注册地址" prop="registerAddress">
              <el-input v-model.trim="form.registerAddress"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="检验员手机号" prop="inspectorMobile">
              <el-input v-model.trim="form.inspectorMobile"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="地址坐标" prop="lnglat" tab="0">
              <el-input v-model="form.lnglat" placeholder="企业地址坐标" :readonly="true">
                <i slot="suffix" class="el-icon-location"></i>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="22">
            <el-form-item label="经营范围" prop="operationScope">
              <el-input type="textarea" :rows="5" v-model.trim="form.operationScope" placeholder="经营范围" :clearable="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>-->
        <el-row>
          <el-col :span="11">
            <el-form-item label="企业名称" prop="enterpriseName">
              <el-input v-model="form.enterpriseName" placeholder="企业名称" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
              <el-input v-model="form.organizingInstitutionBarCode" placeholder="社会信用代码" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="11">
            <el-form-item label="法人代表" prop="corporateRepresentative">
              <el-input v-model="form.corporateRepresentative" placeholder="法人代表" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="身份证号" prop="idCardNo">
              <el-input v-model="form.idCardNo" placeholder="身份证号" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="主体分类" prop="subjectClassification">
              <el-select  v-model="form.subjectClassification" placeholder="请选择" :disabled="true">
                <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11" >
            <el-form-item label="所属区域" prop="areaOptions">
              <el-input v-model.trim="form.areaName" placeholder="" disabled clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="11">
            <el-form-item label="注册资金" prop="registeredCapital">
              <el-input v-model="form.registeredCapital" placeholder="注册资金" :disabled="true">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="经济性质" prop="economicNature">
              <el-select  v-model="form.economicNature" placeholder="请选择"  :disabled="true">
                <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="联系人" prop="contacts">
              <el-input v-model="form.contacts" placeholder="联系人" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="联系电话" prop="contactNumber">
              <el-input v-model="form.contactNumber" placeholder="联系电话" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="11">
            <el-form-item label="联系人手机号" prop="contactMobile">
              <el-input v-model="form.contactMobile" placeholder="联系人手机号" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="检验员手机" prop="inspectorMobile">
              <el-input v-model="form.inspectorMobile" placeholder="检验员手机" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="11">
            <el-form-item label="邮政编码" prop="postalCode">
              <el-input v-model="form.postalCode" placeholder="邮政编码" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="企业注册日期" prop="registerDate">
              <el-date-picker type="date" placeholder="请选择" v-model="form.registerDate" style="width: 100%;" :disabled="true" :clearable="true"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="11">
            <el-form-item label="注册地址" prop="registerAddress">
              <el-input v-model="form.registerAddress" placeholder="注册地址" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="生产地址" prop="productionAddress">
              <el-input v-model="form.productionAddress" placeholder="生产地址" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="22">
            <el-form-item label="经营范围" prop="operationScope">
              <el-input type="textarea" :rows="2" v-model="form.operationScope" placeholder="经营范围" :disabled="true"></el-input>
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
  baseVerifyQuery,
  getChangeById
} from "api/admin/enterprise/base/index";
import { parseValueToText, parseTime, findParentNode } from "utils/index";
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
        dataType: "1",
        verifyStatus: "",
        enterpriseId: "",
        page: 1,
        limit: 20
      },
      needFixedRight: false,
      dialogFormVisible: false,
      form: {},
      areaOptions: []
    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),
    verifyStatusOptions() {
      return this.staticData["变更审核状态"];
    },
    subjectClassificationOptions() {
      return this.staticData["企业主体分类"];
    },
    economicNatureOptions() {
      return this.staticData["企业经济性质"];
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
      baseVerifyQuery(this.listQuery).then(response => {
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
        let str = findParentNode(this.areaData, this.form.areaId);
        this.areaOptions = str.split(",");
        this.areaOptions.push(this.form.areaId + "");
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
