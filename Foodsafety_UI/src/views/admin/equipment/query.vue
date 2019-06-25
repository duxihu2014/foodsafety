<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">

          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>

          <el-form-item label="摄像头名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.equipmentNameLike"> </el-input>
          </el-form-item>

          <el-form-item label="型号" class="filter-item">
            <el-select v-model="listQuery.equModelId" placeholder="请选择" clearable>
              <el-option v-for="item in modelOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>

          </el-form-item>

          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>

    <el-table :data="list" v-loading.body="listLoading" tooltip-effect="dark" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column type="selection" align="center" width="50"></el-table-column>
      <el-table-column width="200" align="center" label="设备名称" prop="equipmentName">
      </el-table-column>
      <el-table-column width="150" align="center" label="设备编码" prop="equipmentNo">
      </el-table-column>
      <el-table-column width="120" align="center" label="安装时间" prop="installTime" :formatter="dateFormatter">
      </el-table-column>
      <el-table-column width="120" align="center" label="创建时间" prop="createTime" :formatter="dateFormatter">
      </el-table-column>
      <el-table-column width="230" align="center" label="企业" prop="enterpriseName" show-overflow-tooltip>
      </el-table-column>
      <el-table-column width="150" align="center" label="分组" prop="enterpriseGroupName">
      </el-table-column>
      <el-table-column width="150" align="center" label="型号" prop="equModel">
      </el-table-column>
      <el-table-column width="150" align="center" label="音频" prop="audioName">
      </el-table-column>

      <el-table-column align="center" fixed="right" label="操作" width="180" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <a :href="playUrl" id="playHref" style="display: none;background-color: #67c23a;border-color: #67c23a;" class="sel_btn">
            播放
          </a>
          <a href="javascript:void(0)" @blur="cancelTimer()" @click="playVideo(scope.row)" class="sel_btn" style="margin-right: 10px;background-color: #67c23a;border-color: #67c23a;">播放</a>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="180" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <a :href="playUrl" id="playHref" style="display: none;background-color: #67c23a;border-color: #67c23a;" class="sel_btn">
            播放
          </a>
          <a href="javascript:void(0)" @blur="cancelTimer()" @click="playVideo(scope.row)" class="sel_btn" style="margin-right: 10px;background-color: #67c23a;border-color: #67c23a;">播放</a>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog :title="'请安装播放插件'" :visible.sync="dialogDownloadVisible" width="20%" style="font-size:15px;">
      <div style="height: 120px;text-align: center;padding-top: 20px;">
        <span style="margin: auto 50px;font-size:18px;">
          <a id="downloadBtn" :href="downloadUrl" style="text-decoration: underline">点击下载</a>
        </span>
      </div>
    </el-dialog>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
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
      <el-form :model="equipmentForm" ref="equipmentForm" label-width="120px" :inline="false">
        <el-tabs>
          <el-tab-pane label="基本信息">
        <el-form-item label="设备型号" prop="equModelId">
          <el-input v-model="equipmentForm.equModel" :disabled="isView" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="名称:" prop="equipmentName">
          <el-input v-model="equipmentForm.equipmentName" :disabled="isView" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="编号:" prop="equipmentNo">
          <el-input v-model="equipmentForm.equipmentNo" :disabled="isView" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="描述:" prop="equipmentDescribe">
          <el-input v-model="equipmentForm.equipmentDescribe" :disabled="isView" placeholder=""></el-input>
        </el-form-item>

        <el-form-item label="音频设备" prop="audioId">
          <el-input v-model="equipmentForm.audioName" :disabled="isView" placeholder=""></el-input>
        </el-form-item>

        <el-form-item label="云台控制URL:" prop="cloudControlUrl">
          <el-input v-model="equipmentForm.cloudControlUrl" :disabled="isView" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="录像播放URL:" prop="videotapeUrl">
          <el-input v-model="equipmentForm.videotapeUrl" :disabled="isView" placeholder=""></el-input>
        </el-form-item>
        <el-form-item label="管理URL:" prop="namagerUrl">
          <el-input v-model="equipmentForm.namagerUrl" :disabled="isView" placeholder=""></el-input>
        </el-form-item>

        <!--<el-form-item label="设备型号:" prop="equModelId" >
                <el-input v-model="equipmentForm.equModelId" :disabled="isEdit" placeholder="请输入管理员电话"></el-input>
              </el-form-item>
              <el-form-item label="设备型号:" prop="equModelId" >
                <el-input v-model="equipmentForm.equModelId" :disabled="isEdit" placeholder="请输入管理员电话"></el-input>
              </el-form-item>-->
          </el-tab-pane>
        </el-tabs>

      </el-form>
      <div slot="footer" class="dialog-footer" v-if="enterprise_update">
        <el-button @click="cancel('equipmentForm')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('equipmentForm')">确 定</el-button>
        <el-button v-else type="primary" @click="update('equipmentForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, play } from "api/admin/equipment/index";
import { getModelMap } from "api/admin/equipmentModel/index";
import { parseValueToText, parseForm, parseTime } from "utils/index";
import { mapGetters } from "vuex";
import { loadGridHeight } from "api/screen";
import { getToken } from "utils/auth";
import queryConditions from "components/QueryConditions/index";
import { getAudioMap } from "api/admin/equipment/audio";

export default {
  name: "index",
  components: {
    queryConditions
  },
  data() {
    return {
      height: undefined,
      list: null,
      total: null,
      listLoading: false,
      suppliersList: [], //供货商
      modelOptions: [],
      listQuery: {
        page: 1,
        limit: 20,
        areaId: undefined,
        gridId: undefined,
        equipmentNameLike: undefined,
        enterpriseId: undefined,
        enterpriseGroupId: undefined
      },
      form: {
        certificateNumber: undefined
      },
      dialogFormVisible: false,
      viewReadOnly: false, //控制查看时的按钮显示
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        view: "查看"
      },
      manager_update: true,
      isView: true,
      selection: [],
      needFixedRight: false,
      productionCertificateFile: [],
      serverImageUrl: process.env.SERVERIMAGEURL,
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      fileFormat: ["image/jpeg", "image/png", "image/bmp", "image/gif"], // 允许上传的文件格式
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now();
        }
      },
      playUrl: "",
      dialogDownloadVisible: false,
      downloadUrl: process.env.SERVERIMAGEURL + "/vlcplay.rar",
      downloadTimer: null,
      equipmentForm: {
        equipmentName: undefined,
        equipmentNo: undefined,
        equipmentDescribe: undefined,
        equipmentType: "1", //默认级别1
        cloudControlUrl: undefined,
        videoUrlHigh: undefined,
        videoUrlCentre: undefined,
        videoUrlLow: undefined,
        videotapeUrl: undefined,
        namagerUrl: undefined,
        equModelId: undefined,
        enterpriseId: undefined,
        enterpriseGroupId: undefined,
        audioId: undefined
      },
      isEdit: false,
      isShowPeople: false, //是否显示设备管理员
      enterprise_update: false,
      audioOptions: []
    };
  },
  created() {
    this.initModelOptions();
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
  methods: {
    initModelOptions() {
      getModelMap().then(response => {
        this.modelOptions = response.data;
      });
      this.initAudioOptions();
    },
    initAudioOptions() {
      getAudioMap().then(response => {
        this.audioOptions = response.data;
      });
    },
    setAreaId(data) {
      this.listQuery.areaId = data;
    },
    setGridId(data) {
      this.listQuery.gridId = data;
    },
    setEnterpriseId(data) {
      this.listQuery.enterpriseId = data;
    },
    getValidDateType(validStatus) {
      switch (validStatus) {
        case "2":
          return "warning";
        case "3":
          return "danger";
      }
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.staffStatus === "0") {
        return "warning-row";
      } else if (row.staffStatus === "1") {
        return "success-row";
      }
      return "";
    },
    getList() {
      this.listLoading = true;
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    resetQuery() {
      this.listQuery.barcodeCoding = "";
      this.listQuery.equipmentNameLike = "";
      this.listQuery.enterpriseId = undefined;
      this.listQuery.areaId = undefined;
      this.listQuery.gridId = undefined;
    },

    cancel() {
      this.dialogFormVisible = false;
      this.resetTmp();
    },
    handleView(row) {
      this.equipmentForm = row;

      if (this.equipmentForm.equModelId) {
        this.equipmentForm.equModelId = this.equipmentForm.equModelId + "";
      }
      if (this.equipmentForm.audioId) {
        this.equipmentForm.audioId = this.equipmentForm.audioId + "";
      }
      // console.log(this.equipmentForm);
      this.enterprise_update = false;
      this.dialogFormVisible = true;
      this.isView = true;
      this.isShowPeople = false;
      this.dialogStatus = "view";
    },

    //关闭弹出框时，清除表单验证，并且设置表单元素为空
    closeDialog() {
      this.resetTmp();
    },

    cancelTimer() {
      // console.log('blur event');
      clearTimeout(this.downloadTimer);
      // this.dialogDownloadVisible = false;
      this.downloadTimer = null;
    },
    showDownload() {
      this.dialogDownloadVisible = true;
      // console.log("弹出下载窗口");
    },
    playVideo(row) {
      play(row.equipmentId).then(response => {
        if (response.data.result == "success") {
          this.playUrl = "VLCPLAY://" + response.data.url;
          document.getElementById("playHref").href = this.playUrl;
          document.getElementById("playHref").click();
          if (!this.downloadTimer) {
            this.downloadTimer = setTimeout(this.showDownload, 1000);
          }
        } else {
          this.$notify({
            title: "提示",
            message: "播放失败",
            type: "error",
            duration: 2000
          });
        }
      });
    },
    resetTmp() {
      this.form = { equipmentNameLike: undefined };
      this.productionCertificateFile = [];
      $(".el-upload--picture-card:eq(0)").show();
      $(".el-icon-delete:eq(0)").show();
      this.$nextTick(function() {
        this.$refs["form"].clearValidate();
      });
    },
    getSelection(selection) {
      this.selection = selection;
    },
    dateFormatter(row, column, cellValue) {
      if (cellValue) {
        return parseTime(cellValue, "{y}-{m}-{d}");
      } else {
        return "";
      }
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["状态"]);
    },
    stopOrStart: function(num) {
      // 1启用，0停用
      if (!this.selection.length) {
        this.$message({
          showClose: true,
          message: "请先选择一条信息！"
        });
        return;
      }
      let flag = true;
      if (num == 1) {
        //启用
        this.selection.forEach(item => {
          if (item.materialStatus == 1) {
            flag = false;
          }
        });
        if (!flag) {
          this.$message({
            showClose: true,
            type: "warning",
            message: "您选择的数据中有启用的数据，不能重复启用！"
          });
          return;
        }
      } else {
        //停用
        this.selection.forEach(item => {
          if (item.materialStatus == 0) {
            flag = false;
          }
        });
        if (!flag) {
          this.$message({
            showClose: true,
            type: "warning",
            message: "您选择的数据中有停用的数据，不能重复停用！"
          });
          return;
        }
      }
      let operation = num ? "启用" : "停用";
      this.$confirm(`确定要${operation}该数据吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let ids = [];
          this.selection.forEach(item => {
            ids.push(item.materialId);
          });
          ids = ids.length > 1 ? ids.join(",") : ids[0];
          let param = { ids: ids, status: num };
          changeStatus(param)
            .then(response => {
              if (response.rel) {
                this.$message({
                  type: "success",
                  showClose: true,
                  message: `${operation}成功！`
                });
              } else {
                this.$message({
                  type: "error",
                  showClose: true,
                  message: `${operation}失败！`
                });
              }
              this.getList();
            })
            .catch(() => {
              this.$message({
                type: "error",
                showClose: true,
                message: "网络错误，数据请求失败。"
              });
            });
        })
        .catch(() => {});
    }
  }
};
</script>
<style scoped>
.sel_btn {
  height: 25px;
  line-height: 25px;
  padding: 0 15px;
  background: #409eff;
  border: 1px #409eff solid;
  border-radius: 3px;
  color: #fff;
  display: inline-block;
  text-decoration: none;
  font-size: 12px;
  outline: none;
}
</style>
