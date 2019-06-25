<template>
  <div>
       <div style="height:460Px;">
          <el-table :data="equList" border  highlight-current-row  style="width: 100%"  @selection-change="handleSelectionChange" height="400" :header-cell-style="{'background-color': '#fafafa'}">
              <!--<el-table-column align="center" prop="equipmentId" type="selection" width="50"> </el-table-column>-->
              <el-table-column align="center" prop="equipmentNo" label="设备编号" style="width: 100%"></el-table-column>
              <el-table-column align="center" prop="equipmentName" label="设备名称" style="width: 100%"></el-table-column>
              <!--<el-table-column align="center" prop="equipmentType" label="设备类型" width="120px" :formatter="equtypeFormatter"></el-table-column>-->
              <!--<el-table-column align="center" prop="specialStatus" label="是否专用" width="100" :formatter="specialStatusFormatter"></el-table-column>
              <el-table-column align="center" prop="equModelId" label="设备规格型号" width="180" :formatter="equmodeFormatter"></el-table-column>
              <el-table-column align="center" prop="equipmentDescribe" label="设备描述" width="150"></el-table-column>  -->
              <!--<el-table-column align="center" prop="instalLatitude" label="安装位置" width="180"></el-table-column>
              <el-table-column align="center" prop="installTime" label="安装时间" width="180"></el-table-column>
              <el-table-column align="center" prop="createOpId" label="创建人工号" width="180"></el-table-column>
              <el-table-column align="center" prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column align="center" prop="modifyOpId" label="修改人工号" width="180"></el-table-column>
              <el-table-column align="center" prop="modifyTime" label="修改时间" width="180"></el-table-column> -->
            <el-table-column  align="center" label="操作" width="120px">
                    <template slot-scope="scope">
                        <a :href="playUrl" id="playHref" class="sel_btn" style="display: none">
                          播放
                        </a>
                      <a href="javascript:void(0)" @blur="cancelTimer()" @click="playVideo(scope.row)" class="sel_btn" style="">播放</a>
                        <!--<el-button size="small" type="success"  ><a href="javascript:void(0)" @blur="cancelTimer()" @click="play(scope.row)">播放</a></el-button>-->
                       <!--  <el-button v-if="scope.row.alertFlag" size="small" type="success" @click="handleAlert(scope.row)">报警信息</el-button> -->
                    </template>
              </el-table-column>
          </el-table>
          <div  class="pagination-container">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30,40]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"></el-pagination>
          </div>
      </div>
     <!--  <el-dialog title="报警信息" :visible.sync="dialogFormVisible" append-to-body>
          <alert-detail :alarmEventData="alarmEvent"  @dialogClose="dialogClose" ></alert-detail>
      </el-dialog>  -->
    <el-dialog :title="'请安装播放插件'" :visible.sync="dialogDownloadVisible" width="20%" style="font-size:15px;" :modal="false" append-to-body>
      <div style="height: 120px;text-align: center;padding-top: 20px;">
        <span style="margin: auto 50px;font-size:18px;">
          <a id="downloadBtn" :href="downloadUrl" style="text-decoration: underline">点击下载</a>
        </span>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import {
  play
} from "api/admin/equipment/index";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";

export default {
  name: "equipment",
  data() {
    return {
      listQuery: {
        page: 1,
        limit: 10,
        equipmentNo: "",
        poleId: undefined,
        equipmentType: undefined,
        equModelId: undefined,
        controltId: undefined,
        areaId: undefined,
        areaIds: []
      },
      total: 0,
      equList: null, //表格内容
      multipleSelection: [], // 选中的对象
      selectionIds: "", // 选中对象的ID,以逗号间隔
      equTypeOpts: null,
      equModelOpts: null,
      controlOpts: null,
      specialStatusOpts: null,
      areaOptions: [],
      areaProps: {
        label: "text",
        value: "id"
      },
      operateStatus: "",
      textMap: {
        update: "编辑",
        add: "创建"
      },
      dialogFormVisible: false,
      editData: null,
      alertDialogVisible: false,
      alertEquipmentNo: undefined,
      playUrl: "",
      dialogDownloadVisible: false,
      downloadUrl: process.env.SERVERIMAGEURL + "/vlcplay.rar",
      downloadTimer: null
    };
  },
  computed: {
    ...mapGetters(["staticData"])
  },
  created: function() {
    this.equList = this.listData;
    // this.initParam();
  },
  props: ["compId", "listData"],
  watch: {
  compId: function() {
      this.equList = this.listData;
    }
  },

  methods: {
    //初始化参数
    initParam() {
      this.equList = null;
      if (this.seatchKey) {
        if (this.alertFlag) {//报警查询
          this.listQuery.equipmentNo = this.seatchKey;
        } else {//监控点位查询
          this.listQuery.poleId = this.seatchKey;
        }
      }
      this.equTypeOpts = this.staticData["设备类型"];
      this.controlOpts = this.staticData["受控属性"];
      this.specialStatusOpts = this.staticData["专用标识"];

      this.getEquMode();
      this.getArea();
      this.getList();
    },
    equtypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["设备类型"]);
    },
    controltFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["受控属性"]);
    },
    specialStatusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["专用标识"]);
    },
    equmodeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.equModelOpts);
    },
    //得到设备规格型号信息
    //设备规格型号
    getEquMode() {
      findEquModelObj().then(response => {
        this.equModelOpts = response;
      });
    },
    //得到区域地址信息
    getArea() {
      getArea().then(response => {
        this.areaOptions = response;
      });
    },
    setAreaId(currentArea) {
      if (currentArea || currentArea.length > 0) {
        this.listQuery.areaId = currentArea[currentArea.length - 1];
      }
    },
    alertColse() {
      this.dialogFormVisible = false;
      this.alertDialogVisible = false;
    },
    closeDialog(val) {
      this.dialogFormVisible = false;
      this.getList();
      switch (val) {
        case 1:
          this.$notify({
            title: "成功",
            message: "创建成功",
            type: "success",
            duration: 2000
          });
          break;
        case 2:
          this.$notify({
            title: "成功",
            message: "修改成功",
            type: "success",
            duration: 2000
          });
          break;
      }
    },
    //得到设备信息列表
    getList() {
      page(this.listQuery).then(response => {
        this.total = response.total;
        this.equList = response.rows;
      });
    },
    handleSizeChange(val) {
      //根据选择显示的条数，显示数据
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      // 显示选择显示的页面显示所显页面的数据
      this.listQuery.page = val;
      this.getList();
    },
    handleFilter() {
      // console.log("d:" + JSON.stringify(this.listQuery));
      this.getList();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.operateStatus = "add";
    },
    handleBatchImp() {
      batchImpEquipment()
        .then(response => {
          const data = JSON.parse(response);
          if (data.code == "0000") {
            this.$message("批量导入成功！");
          } else {
            this.$message("批量导入失败，请稍后再试！");
          }
        })
        .catch(() => {
          this.$message("批量导入失败！");
        });
    },
    handleUpdate(row) {
      getObj(row.equipmentId).then(response => {
        this.editData = response.data;
        this.editData.areaId = String(response.data.areaId);
        this.editData.controltId = String(response.data.controltId);
        if (response.data.equModelId) {
          this.editData.equModelId = String(response.data.equModelId);
        }
        this.dialogFormVisible = true;
        this.operateStatus = "update";
      });
    },
    handleAlert(row) {
      this.alertDialogVisible = true;
      this.dialogFormVisible = false;
      this.alertEquipmentNo = row.equipmentNo;
    },
    handleDelete() {
      if (this.multipleSelection.length > 0) {
        this.$confirm("此操作将永久删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.getIds();
          //检查信息是否被其它功能引用
          checkDel(this.selectionIds).then(response => {
            const data = JSON.parse(response);
            if (data.success) {
              this.delete();
            } else {
              this.$notify({
                title: "提示",
                message: data.msg,
                type: "success",
                duration: 2000
              });
            }
          });
        });
      } else {
        this.$message({
          showClose: true,
          message: "请至少选择一条记录"
          // type: 'warning'
        });
      }
    },
    delete() {
      delObj(this.selectionIds).then(() => {
        this.$notify({
          title: "成功",
          message: "删除成功",
          type: "success",
          duration: 2000
        });
        this.getList();
      });
    },
    getIds() {
      let Ids = "";
      this.multipleSelection.forEach(equ => {
        Ids += "," + equ.equipmentId;
      });
      this.selectionIds = Ids.substring(1, Ids.length);
    },
    resetQuery() {
      this.listQuery.equipmentNo = "";
      this.listQuery.equipmentType = undefined;
      this.listQuery.equModelId = undefined;
      this.listQuery.controltId = undefined;
      this.listQuery.areaId = undefined;
      this.listQuery.areaIds = [];
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



