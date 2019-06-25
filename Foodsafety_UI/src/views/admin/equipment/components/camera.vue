<template>
  <div class="app-container calendar-list-container">
    <el-tabs>
      <el-tab-pane label="摄像头信息">
        <fieldset class="fieldset" v-if="opFlag==2">
          <legend class="legend">查询条件</legend>
          <div class="field-box">
            <el-form :inline="true" :model="listQuery">
              <el-form-item label="摄像头编码" prop="userStatus" class="filter-item">
                <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.equipmentNoLike"> </el-input>
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
            <span v-if="opFlag==1" class="mg-r-15 head-item" @click.stop.prevent="handleUnBindCamera">
              <i class="fa fa-unlink menu-icon vam" aria-hidden="true"></i>批量解绑</span>
            <span v-else class="mg-r-15 head-item" @click.stop.prevent="handleBindCamera">
              <i class="fa fa-link menu-icon vam" aria-hidden="true"></i>批量绑定</span>
          </div>
        </div>
        <el-table :key='tableKey' :data="tableList" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column width="200px" align="center" label="所属企业" prop="enterpriseId" :formatter="function(row,column,cellValue){return enterpriseformatter(row.enterpriseId)}"></el-table-column>
          <el-table-column width="150px" align="center" label="摄像头编码" prop="equipmentNo"></el-table-column>
          <el-table-column width="200px" align="center" label="摄像头名称" prop="equipmentName"></el-table-column>
          <el-table-column width="120px" align="center" label="设备描述" prop="equipmentDescribe"></el-table-column>
          <el-table-column width="100px" align="center" label="设备状态" prop="equipmentStatus" :formatter="equStatusFormatter"></el-table-column>
          <el-table-column align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button v-if="opFlag==1" size="mini" type="success" @click="unBindRow(scope.row)">解绑</el-button>
              <el-button v-else size="mini" type="success" @click="bindRow(scope.row)">确定</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-show="!listLoading" class="pagination-container">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  getBindCameraByAudioId,
  getNoBindCameraByAudioId,
  bindCamera,
  delObjs
} from "api/admin/equipment/equaudio";
import { getAllEnterpriseMap } from "api/admin/equipment/audio";
import { mapGetters } from "vuex";
import { parseValueToText, parseTime } from "utils/index";
import { loadGridHeight } from "api/screen";

export default {
  name: "audio",
  components: {},
  data() {
    return {
      height: 400,
      needFixedRight: false,
      //--查询
      listQuery: {
        page: 1,
        limit: 20,
        equipmentNoLike: undefined,
        enterpriseId: undefined,
        audioId: undefined,
        status: "1"
      },
      //--表格
      tableKey: 0,
      tableList: null,
      total: null,
      listLoading: false,
      userSelection: [],

      userManager_btn_edit: true,
      //--
      allEnterpriseOptions: [],
      enterpriseFlag: false
    };
  },
  props: ["audioId", "opFlag"],
  watch: {
    opFlag: function() {
      this.intiParam();
      this.getList();
    },
    audioId: function() {
      this.intiParam();
      this.getList();
    }
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
    this.height = loadGridHeight();
  },
  computed: {
    ...mapGetters(["staticData", "user"])
  },
  methods: {
    intiParam() {
      let _this = this;

      if (_this.user.userType == 4) {
        _this.listQuery.enterpriseId = _this.user.enterpriseId;
        _this.enterpriseFlag = true;
      }

      if (this.opFlag == 1) {
        this.listQuery.audioId = this.audioId;
      } else {
        // this.listQuery.nobindAudioId = this.audioId;
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
            message: "用户信息获取失败。"
          });
        });
    },
    getList() {
      // this.listLoading = true;
      if (this.opFlag == 1) {
        //查看绑定数据
        getBindCameraByAudioId(this.listQuery).then(response => {
          this.tableList = response.rows;
          this.total = response.total;
          // this.listLoading = false;
        });
      } else {
        getNoBindCameraByAudioId(this.listQuery).then(response => {
          this.tableList = response.rows;
          this.total = response.total;
          // this.listLoading = false;
        });
      }
    },
    //查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    //重置查询条件
    resetQuery() {
      this.listQuery = {
        equipmentNoLike: undefined,
        enterpriseId: undefined,
        status: "1"
      };
      if (_this.user.userType == 4) {
        _this.listQuery.enterpriseId = _this.user.enterpriseId;
        _this.enterpriseFlag = true;
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

    //绑定摄像头
    handleBindCamera() {
      if (!this.userSelection.length) {
        this.$message({
          showClose: true,
          message: "请先选择一条信息！"
        });
        return;
      }
      const idArr = [];
      this.userSelection.forEach(item => {
        idArr.push(item.equipmentId);
      });
      bindCamera(this.audioId, { ids: idArr.join(",") }).then(() => {
        this.$emit("closeDialog");
      });
    },
    bindRow(row) {
      bindCamera(this.audioId, { ids: row.equipmentId }).then(() => {
        this.$emit("closeDialog");
      });
    },
    //解绑摄像头
    unBindRow(row){
      this.$confirm("此操作将解绑摄像头, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delObjs(row.equipmentAudioId)
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
    handleUnBindCamera() {
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
            idArr.push(item.equipmentAudioId);
          });
          delObjs(idArr.join(","))
            .then(() => {
              this.$message({
                showClose: true,
                message: "解绑成功",
                type: "success",
                duration: 2000
              });
              this.getList();
            })
            .catch(() => {});
        })
        .catch(() => {});
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
    closeDialog() {}
  } //end methods
};
</script>

