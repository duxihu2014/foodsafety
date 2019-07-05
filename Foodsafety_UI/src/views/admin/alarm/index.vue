<template>
  <div class="app-container calendar-list-container">
    <!-- <div class="filter-container"> -->
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId">
          </queryConditions>
          <el-form-item label="视频/传感器编码" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="设备编码" v-model="listQuery.indexCodeLike"> </el-input>
          </el-form-item>
          <el-form-item label="状态" class="filter-item">
            <el-select v-model="listQuery.status" placeholder="请选择">
              <el-option label="未处理" value="1"> </el-option>
              <el-option label="已处理" value="2"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-tabs v-model="tabPosition" @tab-click="handleClick">
      <el-tab-pane :label="'摄像头报警('+cemareTotal+')'" name="first">
      </el-tab-pane>
      <el-tab-pane :label="'传感器报警('+sensorTotal+')'" name="second">
      </el-tab-pane>
    </el-tabs>

    <el-table :data="list" tooltip-effect="dark" v-loading.body="listLoading" border highlight-current-row style="width: 100%" :height="height" :header-cell-style="{'background-color': '#fafafa'}">
      <el-table-column width="80px" align="center" label="编号" prop="id"></el-table-column>
      <el-table-column v-if="listQuery.eventType == 1" width="150" align="center" label="视频编码" prop="indexCode"></el-table-column>
      <el-table-column v-else width="150" align="center" label="传感器编码" prop="indexCode"></el-table-column>
      <el-table-column width="250px" align="center" label="企业名称" prop="enterpriseName"></el-table-column>
      <el-table-column width="250px" align="center" label="报警来源" prop="alarmSource"></el-table-column>
      <el-table-column width="100px" align="center" label="报警类型" prop="eventId" :formatter="alertTypeFormatter"></el-table-column>
      <el-table-column width="400px" align="center" label="报警内容" prop="content" show-overflow-tooltip></el-table-column>
      <el-table-column width="200px" align="center" label="报警时间" prop="alarmTime"></el-table-column>
      <el-table-column width="100px" align="center" label="状态" prop="status" >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status==1" type="info">{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==2" type="success">{{statusFormatter(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column width="400px" align="center" label="处理结果" prop="processingResult" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column width="200px" align="center" label="处理时间" prop="processingTime"></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="200" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">详情</el-button>
          <el-button v-if="scope.row.status==1" size="mini" type="primary" @click="processingView(scope.row)">处理</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">详情</el-button>
          <el-button v-if="scope.row.status==1" size="mini" type="primary" @click="processingView(scope.row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog title="详情" :visible.sync="dialogFormVisible" height="750">
      <div slot="title">
        <template>
          <i class="el-icon-view"> 详情</i>
        </template>
      </div>
      <alert-detail ref="alarmDiv" :alarmEventData="alarmEvent" :equType="equType" @dialogClose="dialogClose" v-if="dialogFormVisible"></alert-detail>
    </el-dialog>


    <el-dialog title="处理" :visible.sync="processingDialogFormVisible" height="750">
      <div slot="title">
        <template>
          <i class="el-icon-view">处理</i>
        </template>
      </div>

      <el-form ref="processingForm" :model="processingForm" label-width="100px"  :rules="processingRules">
        <el-form-item label="处理结果" prop="processingResult">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入处理结果"
            v-model="processingForm.processingResult">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" >
        <el-button @click="processingDialogFormVisible=false">取 消</el-button>
        <el-button   type="primary" @click="submitProcessing('processingForm')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { alertPage, getAlertObj, getCount ,processing} from "api/admin/alarm/index";
import alertConst from "api/admin/alarm/alertType";
import { parseValueToText, parseTime } from "utils/index";
import { loadGridHeight } from "api/screen";
import queryConditions from "components/QueryConditions/index";

export default {
  name: "region",
  components: {
    "alert-detail": () => import("./alarmDetail"),
    queryConditions
  },
  data() {
    return {
      list: null,
      total: null,
      listLoading: true,
      selection: [],
      listQuery: {
        indexCodeLike: undefined,
        status: "",
        enterpriseId: undefined,
        areaId: undefined,
        gridId: undefined,
        alermStartDate: undefined,
        alermEndDate: undefined,
        eventType: "1", ////1:摄像头 2:传感器
        page: 1,
        limit: 20
      },
      dialogFormVisible: false,
      alarmEvent: {
        id: undefined,
        indexCode: undefined,
        alarmSource: undefined,
        alarmType: undefined,
        content: undefined,
        equName: undefined,
        enterpriseName: undefined,
        alarmTime: undefined,
        videoUrl: undefined,
        imgUrl: undefined
      },
      cameraDialogVisible: false,
      alertFlag: true,
      equipmentNo: undefined,
      needFixedRight: false,
      tabPosition: "first",
      cemareTotal: "0",
      sensorTotal: "0",
      equType: "1",
      height: undefined,
      processingRules:{
        processingResult:[{required: true, message: "请输入处理结果", trigger: "blur"}]
      },
      processingForm:{processingResult:undefined},
      processingDialogFormVisible:false
    };
  },
  created() {
    this.initParam();
    this.getCount();
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData", "user"])
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
    initParam() {
      if (this.$route.query.queryTime) {
        this.listQuery.alermStartDate = parseTime(this.$route.query.queryTime);
      }
    },

    //tab转换查询
    handleClick() {
      if (this.tabPosition == "first") {
        this.listQuery.eventType = "1";
        this.equType = "1";
      } else {
        this.listQuery.eventType = "2";
        this.equType = "2";
      }
      this.getList();
    },
    getCount() {
      getCount(this.listQuery).then(response => {
        this.cemareTotal = response.count_cemare;
        this.sensorTotal = response.count_sensor;
      });
    },
    getList() {
      this.listLoading = true;
      this.listQuery.enterpriseId = this.user.enterpriseId;
      alertPage(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;

        this.listLoading = false;
      });
    },
    handleFilter() {
      this.listQuery.alermStartDate = undefined;
      this.getCount();
      //查询按钮点击事件
      this.getList();
    },
    handleSizeChange(val) {
      //每页数量变化
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      //当前页变化
      this.listQuery.page = val;
      this.getList();
    },
    handleView(row) {
      this.dialogFormVisible = true;
      getAlertObj(row.id).then(response => {
        let alarmData = response.dara;
        this.alarmEvent.id = alarmData.id;
        this.alarmEvent.indexCode = alarmData.indexCode;
        this.alarmEvent.alarmSource = alarmData.alarmSource;
        this.alarmEvent.eventId = alarmData.eventId;
        this.alarmEvent.alarmType = parseValueToText(
          alarmData.eventId,
          this.staticData["报警类型"]
        ); //报警类型
        this.alarmEvent.content = alarmData.content;
        this.alarmEvent.equName = alarmData.equName;
        this.alarmEvent.enterpriseName = alarmData.enterpriseName;
        this.alarmEvent.alarmTime = alarmData.alarmTime;
        if (
          this.alarmEvent.eventId == "10001" ||
          this.alarmEvent.eventId == "10002"
        ) {
          this.alarmEvent.sensorAlarmType = "wd";
        } else {
          this.alarmEvent.sensorAlarmType = "sd";
        }
        if (this.tabPosition == "first") {
          this.alarmEvent.imgUrl = alarmData.imgUrl;
          this.alarmEvent.videoUrl = alarmData.videoUrl;
        }
        this.$nextTick(() => {
          this.$refs["alarmDiv"].initAlarm(this.alarmEvent);
        });
      });
    },
    handleEQU(obj) {
      this.equipmentNo = obj.indexCode;
      this.cameraDialogVisible = true;
    },
    resetQuery() {
      this.listQuery.indexCodeLike = undefined;
      this.listQuery.enterpriseId = undefined;
      this.listQuery.areaId = undefined;
      this.listQuery.gridId = undefined;
      this.listQuery.alermStartDate = undefined;
      this.listQuery.alermEndDate = undefined;
      this.listQuery.status = "";
    },
    dialogClose() {
      this.dialogFormVisible = false;
    },
    // alertColse() {
    //   this.detailDialogVisible = false;
    //   this.cameraDialogVisible = false;
    // },
    //事件类型
    alertTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["报警类型"]);
    },
    //事件来源
    eventSourceFormatter(row, column, cellValue) {
      if (!cellValue) return "";
      if (cellValue == 1) {
        return "摄像头";
      } else if (cellValue == 2) {
        return "传感器";
      }
    },
    eventLevelFormatter(param) {
      if (!param) return "";
      if (param == 1) {
        return "中";
      } else if (param == 2) {
        return "高";
      } else {
        return "低";
      }
    },
    statusFormatter(cellValue)
    {
      if (!cellValue) return "";
      if (cellValue == "1") {
        return "未处理";
      } else if (cellValue == "2") {
        return "已处理";
      }
    },
    processingView(row){
      this.processingForm.alarmId=row.id;
      this.processingDialogFormVisible = true;
      this.processingForm.processingResult=undefined;
      this.$refs["processingForm"].resetFields();
    },
    submitProcessing(formName){
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          let param = new FormData();
          param.append("processingResult",this.processingForm.processingResult);
          processing(this.processingForm.alarmId,param).then(() => {
            this.processingDialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
              message: "提交处理结果成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style scoped>
</style>

