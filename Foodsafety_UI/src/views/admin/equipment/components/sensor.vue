<template>
    <div>
        <el-row>
            <el-col>
                 <div >
                      <el-form :inline="true" :model="listQuery">
                        <el-form-item label="名称" prop="equipmentName">
                              <el-input @keyup.enter.native="handleFilter" style="width: 200px;"  placeholder="传感器名称"  v-model="listQuery.equipmentNameLike"> </el-input>
                        </el-form-item>
                        <el-form-item label="用途类型" prop="usesType">
                          <el-select  v-model="listQuery.usesType" placeholder="请选择">
                            <el-option v-for="item in  usesTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item class="filter-item">
                          <el-button type="primary"   v-waves  @click="handleFilter">搜索</el-button>
                          <el-button  v-waves     @click="resetQuery()">重置</el-button>
                        </el-form-item>
                    </el-form>
                 </div>
                 <div class="head-box">
                      <div class="item-box">
                          <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
                          <!--<span class="mg-r-15 head-item" @click.stop.prevent="handleUpdate"><i class="fa fa-pencil menu-icon vam" aria-hidden="true"></i>编辑</span>-->
                          <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
                      </div>
                  </div>
                  <el-table :key='tableKey' :data="list" v-loading.body="listLoading" tooltip-effect="dark" border  highlight-current-row style="width: 100%;margin-top:0px;" @selection-change="getSelection" height="440" :header-cell-style="{'background-color': '#fafafa'}">
                    <el-table-column  type="selection" align="center" width="50"></el-table-column>
                    <el-table-column width="200" align="center" label="设备名称" prop="sensorName">
                    </el-table-column>
                    <el-table-column width="210"   align="center" label="设备编码"  prop="sensorNo">
                    </el-table-column>
                    <el-table-column width="150"   align="center" label="用途类型"  prop="usesType" :formatter="usesTypeFormatter">
                    </el-table-column>

                    <el-table-column width="120" align="center" label="创建时间" prop="createTime" :formatter="dateFormatter" >
                    </el-table-column>
                    <el-table-column width="200" align="center" label="企业" prop="enterpriseName"  show-overflow-tooltip>
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
                    <el-table-column width="150" align="center" label="设备型号" prop="equModel">
                    </el-table-column>
                    <el-table-column  align="center"  label="操作" fixed="right" width="200">
                      <template slot-scope="scope">
                           <el-button  v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">编辑
                          </el-button>
                        <el-button  size="small" type="success" @click="handleReport(scope.row)">查看
                        </el-button>
                              <!--<el-button  v-if="userManager_btn_edit" size="small" type="success" @click="showDept(scope.row)">部门
                          </el-button>
                              <el-button  v-if="userManager_btn_edit" size="small" type="success" @click="handleVideo(scope.row)">视频权限
                          </el-button>-->
                              <!-- <el-button  v-if="userManager_btn_edit" size="small" type="success" @click="handleDelete(scope.row)">删除
                          </el-button> -->
                      </template>
                    </el-table-column>
                </el-table>
                <div v-show="!listLoading" class="pagination-container">
                    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
                </div>

              <!-- -->
            </el-col>
        </el-row>
        <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :before-close="cancel">
          <div style="margin-bottom: 20px;margin-top: -15px"><hr/><p/></div>
            <el-form :model="equipmentForm" :rules="formRules" ref="equipmentForm" label-width="120px" :inline="false">
              <el-form-item label="设备型号" prop="equModelId">
                <el-select  v-model="equipmentForm.equModelId" placeholder="请选择" clearable>
                  <el-option v-for="item in modelOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="用途类型" prop="usesType">
                <el-select  v-model="equipmentForm.usesType" placeholder="请选择">
                  <el-option v-for="item in  usesTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="设备名称:" prop="sensorName">
                  <el-input v-model="equipmentForm.sensorName"   :disabled="isEdit" placeholder="设备名称"></el-input>
              </el-form-item>
              <el-form-item label="设备编码:" prop="sensorNo" >
                <el-input v-model="equipmentForm.sensorNo" :disabled="isEdit" placeholder="请输入设备编码"></el-input>
              </el-form-item>
              <el-form-item label="设备描述:" prop="sensorDescribe" >
                <el-input v-model="equipmentForm.sensorDescribe" :disabled="isEdit" placeholder="请输入设备描述"></el-input>
              </el-form-item>

              <!--<el-form-item label="云台控制URL:" prop="cloudControlUrl" >
                <el-input v-model="equipmentForm.cloudControlUrl" :disabled="isEdit" placeholder="请输入云台控制URL"></el-input>
              </el-form-item>-->
              <el-form-item label="最低温度:" prop="humidityLow" >
                <el-input v-model="equipmentForm.humidityLow" :disabled="isEdit" placeholder="最低温度" style="width:150px"></el-input>℃
              </el-form-item>
              <el-form-item label="最高温度:" prop="humidityHigh" >
                <el-input v-model="equipmentForm.humidityHigh" :disabled="isEdit" placeholder="最低温度" style="width:150px"></el-input>℃
              </el-form-item>
              <el-form-item label="最低湿度:" prop="dampnessLow" >
                <el-input v-model="equipmentForm.dampnessLow" :disabled="isEdit" placeholder="最低湿度" style="width:150px"></el-input>%
              </el-form-item>
              <el-form-item label="最高湿度:" prop="dampnessHigh" >
                <el-input v-model="equipmentForm.dampnessHigh" :disabled="isEdit" placeholder="最高湿度" style="width:150px"></el-input>%
              </el-form-item>



            </el-form>
            <div slot="footer" class="dialog-footer" v-if="enterprise_update">
                <el-button @click="cancel('equipmentForm')">取 消</el-button>
                <el-button v-if="dialogStatus=='create'" type="primary" @click="create('equipmentForm')">确 定</el-button>
                <el-button v-else type="primary" @click="update('equipmentForm')">确 定</el-button>
            </div>
        </el-dialog>

      <el-dialog   width="90%"  size="full" title="查看" :visible.sync="dialogReportVisible" v-if="dialogReportVisible" ><report :no="reportNo" ref="report" v-if="dialogReportVisible" ></report></el-dialog>

    </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  page, addEquip, putEquip, delEquip, delObjs, checkEquNo
} from "api/admin/equipment/sensor";
import { getModelMap } from "api/admin/equipmentModel/index";
// import { getAudioMap } from "api/admin/equipment/audio";
import { getAllUserMap } from "api/admin/user/index";
import { spacelValidator } from "utils/validate";
import { parseValueToText, parseTime } from "utils/index";
import report from "./report";

export default {
  name: "",
  components: {
    report
  },
  data() {
    const mobileValidator = (rule, value, callback) => {
      const mymobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
      if (value && !mymobile.test(value)) {
        return callback(new Error("手机号格式不正确"));
      } else {
        callback();
      }
    };
    const equNoValidator = (rule, value, callback) => {
      if(!this.isEdit && this.oldSensorNo){
          if(this.oldSensorNo == value){
            callback();
          }else{
            checkEquNo({ sensorNo: value}).then(response => {
              if (response.data) {
              return callback(new Error("设备编码不能重复"));
            } else {
              callback();
            }
          });
          }
      }else{
        checkEquNo({ sensorNo: value}).then(response => {
          if (response.data) {
            return callback(new Error("设备编码不能重复"));
          } else {
            callback();
          }
        });
      }

    };
    return {
      reportNo: undefined,
      allUserOptions: [], //所有用户
      currOrgUserOptions: [], //当前设备用户
      areaOptions: [],
      selectAreaOptions: [],
      clearVisible: true,
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
        usesType: undefined
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
      dialogStatus: "",
      textMap: {
        update: "➢  编辑",
        create: "➢  创建"
      },
      isEdit: false,
      isShowPeople: false, //是否显示设备管理员
      enterprise_update: false,
      equipmentForm: {
        equModelId: undefined,
        sensorName: undefined,
        sensorNo: undefined,
        usesType: undefined,
        equipmentType: "1", //默认级别1
        sensorDescribe: undefined,
        cloudControlUrl: undefined,
        humidityLow: undefined,
        humidityHigh: undefined,
        dampnessLow: undefined,
        dampnessHigh: undefined,
      },
      oldSensorNo: undefined,
      compId: undefined,
      groupId: undefined,
      formRules: {
        sensorNo: [
          {
            max: 32,
            message: "长度不能超过32个字符",
            trigger: "blur"
          },
          { validator: equNoValidator, trigger: "blur" },
          {
            required: true,
            message: "请输入设备编码",
            validator: spacelValidator,
            trigger: "blur"
          }
        ],
        sensorName: [
          {
            max: 128,
            message: "长度不能超过32个字符",
            trigger: "blur"
          },
          {
            required: true,
            message: "请输入设备名称",
            validator: spacelValidator,
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.init();
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData", "user"]),
    usesTypeOptions(){
       return this.staticData["IOT设备用途类型"];
    }
  },
  watch: {},
  methods: {
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
    getSelectNodeData(id, level, pid) {
      this.resetQuery();
      this.compId = "";
      this.groupId = "";
      if(level == "1"){
        this.listQuery.enterpriseId = id;
        this.compId = id;
      }else {
        this.listQuery.enterpriseGroupId = id;
        this.compId = pid;
        this.groupId = id;
      }
      this.getList();
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
      /*if (!this.listQuery.equipmentNameLike) {
        this.resetQuery();
      }*/
      this.getList();
    },
    handleVideo(row) {
      this.videoReset = true;
      this.unitId = row.equipmentId;
      this.dialogVideoVisible = true;
    },
    handleCreate() {
      if(!this.groupId){
        this.$message({
          showClose: true,
          message: "请选择分组！"
        });
        return;
      }
      this.resetForm();
      this.enterprise_update = true;
      this.dialogFormVisible = true;
      this.isEdit = false;
      this.isShowPeople = false;
      this.dialogStatus = "create";
    },
    handleReport(data) {
      this.reportNo = data.sensorNo;
      // console.log("----set no= "+this.reportNo);
      this.dialogReportVisible = true;
    },
    closeReport(){
      this.$refs["report"].clearTimer();
      this.dialogReportVisible = false;
    },
    handleUpdate(data) {
      this.equipmentForm = data;
      this.oldSensorNo = data.sensorNo;
      if(this.equipmentForm.equModelId){
        this.equipmentForm.equModelId = this.equipmentForm.equModelId + "";
      }
      if(this.equipmentForm.audioId){
        this.equipmentForm.audioId = this.equipmentForm.audioId + "";
      }
      // console.log(this.equipmentForm);
      this.enterprise_update = true;
      this.dialogFormVisible = true;
      this.isEdit = false;
      this.isShowPeople = false;
      this.dialogStatus = "update";
    },
    handleDelete() {
      if (!this.userSelection.length) {
        this.$message({
          showClose: true,
          message: "请先选择一条信息！"
        });
        return;
      }
      const idArr = [];
      this.userSelection.forEach(item => {
        idArr.push(item.sensorId);
      });
      //设备下是否有人员
      const delId = idArr.join(",");
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delObjs(delId)
            .then(response => {
            this.$notify({
            title: "成功",
            message: "删除成功",
            type: "success",
            duration: 2000
          });
          this.getList();
        });
      });
    },
    create(formName) {
      const set = this.$refs;
      this.equipmentForm.enterpriseId = this.compId;
      this.equipmentForm.enterpriseGroupId = this.groupId;
      set[formName].validate(valid => {
        if (valid) {
          addEquip(this.equipmentForm).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: "成功",
              message: "创建成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    update(formName) {
      const set = this.$refs;

      set[formName].validate(valid => {
        if (valid) {
          putEquip(this.equipmentForm.equipmentId, this.equipmentForm).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: "成功",
              message: "编辑成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    //跳转到部门
    showDept(obj) {
      this.$router.push({
        path: "deptIndex",
        query: {
          equipmentId: obj.equipmentId
        }
      });
    },
    cancel() {
      this.dialogFormVisible = false;
      this.getList();
    },
    closeDialog() {
      this.dialogVideoVisible = false;
      this.videoReset = false;
    },
    closeVideo() {
      this.dialogVideoVisible = false;
      this.videoReset = false;
    },
    resetQuery() {
      this.listQuery = { equipmentNameLike: "", enterpriseId: undefined, enterpriseGroupId: undefined, usesType: undefined };
    },
    resetForm() {
      (this.equipmentForm = {
        sensorName: undefined,
        sensorNo: undefined,
        sensorDescribe: undefined,
        sensorType: "1", //默认级别1
        sensorUrl: undefined,
        humidityHigh: undefined,
        humidityLow: undefined,
        dampnessHigh: undefined,
        dampnessLow: undefined
      }),(this.oldSensorNo =  undefined),
        (this.selectAreaOptions = []);

    },
    dateFormatter(row, column, cellValue) {
      if(cellValue){
        return parseTime(cellValue, "{y}-{m}-{d}");
      }else{
        return "";
      }

    },
    wdFormatter(row, column, cellValue) {
      return cellValue+"℃";

    },
    sdFormatter(row, column, cellValue) {
      return cellValue+"%";

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
    },
    usesTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['IOT设备用途类型']);
    },
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


