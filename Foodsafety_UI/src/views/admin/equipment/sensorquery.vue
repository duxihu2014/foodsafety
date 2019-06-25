<template>
    <div class="app-container calendar-list-container">
        <fieldset class="fieldset">
            <legend class="legend">查询条件</legend>
            <div class="field-box">
                <el-form :inline="true" :model="listQuery">

                    <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>

                    <el-form-item label="传感器名称" class="filter-item">
                        <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="传感器名称" v-model.trim="listQuery.equipmentNameLike"> </el-input>
                    </el-form-item>

                    <el-form-item class="filter-item">
                        <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
                        <el-button v-waves @click="resetQuery()">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </fieldset>
        <el-row>
            <el-col>
                <el-table :key='tableKey' :data="list" tooltip-effect="dark" v-loading.body="listLoading" border highlight-current-row style="width: 100%;margin-top:0px;" @selection-change="getSelection" :height="height" :header-cell-style="{'background-color': '#fafafa'}">
                    <!-- <el-table-column type="selection" align="center" width="50"></el-table-column> -->
                    <el-table-column width="200" align="center" label="传感器名称" prop="sensorName">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="传感器编码" prop="sensorNo">
                    </el-table-column>
                    <el-table-column width="120" align="center" label="创建时间" prop="createTime" :formatter="dateFormatter">
                    </el-table-column>
                    <el-table-column width="230" align="center" label="企业" prop="enterpriseName" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column width="150" align="center" label="分组" prop="enterpriseGroupName">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="最低温度" prop="humidityLow" :formatter="wdFormatter">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="最高温度" prop="humidityHigh" :formatter="wdFormatter">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="最低湿度" prop="dampnessLow" :formatter="sdFormatter">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="最高湿度" prop="dampnessHigh" :formatter="sdFormatter">
                    </el-table-column>
                    <el-table-column width="150" align="center" label="型号" prop="equModel">
                    </el-table-column>
                    <el-table-column align="center" fixed="right" label="操作" width="100" v-if="needFixedRight">
                        <template slot-scope="scope">
                            <el-button size="mini" type="primary" @click="handleReport(scope.row)">查看</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="操作" width="100" v-else>
                        <template slot-scope="scope">
                            <el-button size="mini" type="primary" @click="handleReport(scope.row)">查看</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div v-show="!listLoading" class="pagination-container">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
                </div>
            </el-col>
        </el-row>

        <el-dialog title="查看" :visible.sync="dialogReportVisible" width="90%"  size="full">
            <report :no="reportNo" v-if="dialogReportVisible"></report>
        </el-dialog>

    </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  page,
  checkEquNo
} from "api/admin/equipment/sensor";
import { getModelMap } from "api/admin/equipmentModel/index";
import { getAllUserMap } from "api/admin/user/index";
import { parseTime } from "utils/index";
import report from "./components/report";
import { loadGridHeight } from "api/screen";
import queryConditions from "components/QueryConditions/index";

export default {
  name: "",
  components: {
    report,
    queryConditions
  },
  data() {
    return {
      height: undefined,
      reportNo: undefined,
      allUserOptions: [], //所有用户
      currOrgUserOptions: [], //当前设备用户
      userSelection: [],
      modelOptions: [],
      audioOptions: [],
      unitId: "",
      listQuery: {
        page: 1,
        limit: 10,
        equipmentNameLike: undefined,
        enterpriseId: undefined,
        enterpriseGroupId: undefined,
        status: "1",
        areaId: undefined,
        gridId: undefined
      },
      tableKey: 0,
      list: null,
      total: null,
      listLoading: false,
      userManager_btn_edit: true,

      dialogFormVisible: false,
      dialogVideoVisible: false,
      dialogReportVisible: false,
      videoReset: false,

      isEdit: false,
      isShowPeople: false, //是否显示设备管理员
      enterprise_update: false,
      compId: undefined,
      groupId: undefined,
      needFixedRight: false
    };
  },
  created() {
    this.init();
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
    this.height = loadGridHeight();
  },
  computed: {
    ...mapGetters(["staticData", "user"])
  },
  watch: {},
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
    init() {
      let _this = this;
      //初始化地区信息
      //初始化用户信息
      this.getUserMap(null);
      this.initModelOptions();
    },
    getUserMap(param) {
      getAllUserMap(param)
        .then(response => {
          if (param) {
            this.currOrgUserOptions = response.data;
          } else {
            _this.allUserOptions = response.data;
          }
        })
        .catch(() => {});
    },
    initModelOptions() {
      getModelMap().then(response => {
        this.modelOptions = response.data;
      });
    },
    initAudioOptions() {
      getAudioMap().then(response => {
        this.audioOptions = response.data;
      });
    },

    getList() {
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    getSelection(selection) {
      this.userSelection = selection;
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    handleFilter() {
      this.getList();
    },
    handleReport(data) {
      this.reportNo = data.sensorNo;
      // console.log("----set no= " + this.reportNo);
      this.dialogReportVisible = true;
    },
    cancel() {
      this.dialogFormVisible = false;
    },

    resetQuery() {
      this.listQuery = {
        equipmentNameLike: "",
        enterpriseId: undefined,
        enterpriseGroupId: undefined,
        areaId: undefined,
        gridId: undefined
      };
    },
    resetForm() {},
    dateFormatter(row, column, cellValue) {
      if (cellValue) {
        return parseTime(cellValue, "{y}-{m}-{d}");
      } else {
        return "";
      }
    },
    wdFormatter(row, column, cellValue) {
      return cellValue + "℃";
    },
    sdFormatter(row, column, cellValue) {
      return cellValue + "%";
    },
    //格式化用户
    formatUser(param) {
      if (!param) {
        return "";
      }
      if (!this.allUserOptions.length) {
        return param;
      }
      return this.allUserOptions.filter(item => {
        return item.value == param;
      })[0].text;
    }
  } //end methods
};
</script>
<style scoped>
.filter-container {
  padding-bottom: 10px;
  margin: 0 auto;
  text-align: center;
}
.main-container {
  position: relative;
  min-height: 100%;
  height: 96vh;
  padding-top: 110px;
  transition: margin-left 0.28s ease-out;
  margin-left: 12.5rem;
  background-color: #e4e7ed;
}
</style>


