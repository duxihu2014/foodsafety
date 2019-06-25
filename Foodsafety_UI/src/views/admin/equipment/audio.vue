<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="音频编码" prop="userName" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.audioNoLike"> </el-input>
          </el-form-item>
          <el-form-item label="音频名称" prop="userStatus" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.audioNamelike"> </el-input>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate">
          <i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete">
          <i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
         <span class="mg-r-15 head-item" @click.stop.prevent="handleBindCamera">
          <i class="fa fa-link menu-icon vam" aria-hidden="true"></i>绑定摄像头</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="tableList" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column width="150px" align="center" label="音频编码" prop="audioNo"></el-table-column>
      <el-table-column width="150px" align="center" label="音频名称" prop="audioName"></el-table-column>
      <!-- <el-table-column width="110px"   align="center" label="所属区域"    prop="userType" :formatter="userTypeFormatter"></el-table-column> -->
      <el-table-column width="200px" align="center" label="管理URL" prop="namagerUrl" show-overflow-tooltip></el-table-column>
      <!-- <el-table-column width="120px"   align="center" label="音频规则型号" prop="equModelId"></el-table-column> -->
      <el-table-column width="120px" align="center" label="是否专用" prop="specialStatus" :formatter="specialFormatter"></el-table-column>
      <el-table-column width="120px" align="center" label="安装时间" prop="installTime" :formatter="installTimeFormatter"></el-table-column>
      <el-table-column width="110px" align="center" label="设备描述" prop="audioDescribe"></el-table-column>
      <el-table-column width="80px" align="center" label="设备状态" prop="equipmentStatus" :formatter="equStatusFormatter"></el-table-column>
      <el-table-column width="200px" align="center" label="所属企业" prop="enterpriseId" :formatter="function(row,column,cellValue){return enterpriseformatter(row.enterpriseId)}"></el-table-column>
      <!-- <el-table-column width="80px"    align="center" label="状态"        prop="status" :formatter="statusFormatter"></el-table-column> -->
      <el-table-column width="120px" align="center" label="创建时间" prop="createTime"></el-table-column>
      <el-table-column width="120px" align="center" label="创建工号" prop="createOpId" :formatter="function(row,column,cellValue){return opUserformatter(row.createOpId)}"></el-table-column>
      <el-table-column width="120px" align="center" label="修改时间" prop="modifyTime"></el-table-column>
      <el-table-column width="120px" align="center" label="修改工号" prop="modifyOpId" :formatter="function(row,column,cellValue){return opUserformatter(row.modifyOpId)}"></el-table-column>
      <!-- <el-table-column align="center" label="操作" width="300" fixed="right" v-if="needFixedRight"> -->
      <el-table-column align="center" label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" @click="handleUnBindCamera(scope.row)">摄像头</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
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

      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-tabs>
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="音频编码" prop="audioNo">
                  <el-input v-if="dialogStatus == 'create'" v-model.trim="form.audioNo" placeholder=""></el-input>
                  <el-input v-else v-model.trim="form.audioNo" placeholder="" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="音频名称" prop="audioName">
                  <el-input v-model.trim="form.audioName" placeholder=""></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所属企业" prop="enterpriseId">
                  <el-select v-model="form.enterpriseId" placeholder="请选择" clearable :disabled="enterpriseFlag">
                    <el-option v-for="item in allEnterpriseOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否专用" prop="specialStatus">
                  <el-select v-model="form.specialStatus" placeholder="请选择" clearable>
                    <el-option label="否" value="0"> </el-option>
                    <el-option label="是" value="1"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="安装时间" prop="installTime">
                  <el-date-picker v-model="form.installTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备状态" prop="equipmentStatus">
                  <el-select v-model="form.equipmentStatus" placeholder="请选择" clearable>
                    <el-option label="未知" value="0"> </el-option>
                    <el-option label="在线" value="1"> </el-option>
                    <el-option label="离线" value="2"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="管理URL" prop="namagerUrl">
                  <el-input v-model.trim="form.namagerUrl" placeholder=""></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="设备描述" prop="audioDescribe">
                  <el-input v-model.trim="form.audioDescribe" placeholder=""></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="userManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog   :title="textMap[dialogStatus]" :visible.sync="dialogCameraFormVisible" @close="closeDialog" :close-on-click-modal="false">
       <div slot="title">
        <template v-if="textMap[dialogStatus] === '绑定'">
          <i class="el-icon-unlink"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '解绑'">
          <i class="el-icon-unlink"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <camera :audioId="selectAudioId" :opFlag="bindOperFlag" @closeDialog="closeDialog"></camera>
    </el-dialog>

  </div>
</template>

<script>
import {
  page,
  getAudio,
  addAudio,
  putAudio,
  delObjs,
  checkAudioNo,
  checkAudioName,
  getAllEnterpriseMap
} from "api/admin/equipment/audio";
import { mapGetters } from "vuex";
import { parseValueToText, parseTime } from "utils/index";
import { loadGridHeight } from "api/screen";
import { spacelValidator } from "utils/validate";
import { getAllUserMap } from "api/admin/user/index";
import camera from "./components/camera.vue";
export default {
  name: "audio",
  components: {camera},
  data() {
    const userNoValidator = (rule, value, callback) => {
      if (this.audioNoSelected !== value) {
        checkAudioNo({ audioNO: value.trim() }).then(response => {
          if (!response.rel) {
            return callback(new Error("音频编码不能重复"));
          } else {
            callback();
          }
        });
      } else {
        callback();
      }
    };
    return {
      height: undefined,
      needFixedRight: false,
      //--查询
      listQuery: {
        page: 1,
        limit: 20,
        audioNoLike: undefined,
        audioNamelike: undefined,
        enterpriseId: undefined,
        status: "1"
      },
      //--表格
      tableKey: 0,
      tableList: null,
      total: null,
      listLoading: true,
      userSelection: [],
      //--对话框、 按钮
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        view: "查看",
        bind: "绑定",
        unbind: "解绑"
      },
      userManager_btn_edit: true,
      userManager_btn_del: true,
      userManager_btn_add: true,
      userManager_update: true,
      //--表单
      audioNoSelected: "",
      institutionList: [],
      role: undefined,
      form: {
        audioId: undefined,
        areaId: undefined, //行政区_区域编号
        audioNo: undefined,
        audioName: undefined,
        audioDescribe: undefined,
        namagerUrl: undefined,
        equModelId: undefined,
        installTime: undefined,
        enterpriseId: undefined,
        equipmentStatus: "1", //设备状态 0未知 1在线 2离线
        specialStatus: "0" ////是否专用 1是 0否
      },
      rules: {
        audioNo: [
          {
            required: true,
            message: "音频编码不能为空",
            validator: spacelValidator,
            trigger: "blur"
          },
          { validator: userNoValidator, trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        audioName: [
          {
            required: true,
            message: "音频名称不能为空",
            validator: spacelValidator,
            trigger: "blur"
          },
          { max: 64, message: "长度不能超过64个字符", trigger: "blur" }
        ],
        namagerUrl: [
          {
            required: true,
            message: "管理URL不能为空",
            validator: spacelValidator,
            trigger: "blur"
          },
          { max: 64, message: "长度不能超过64个字符", trigger: "blur" }
        ],
        // installTime: [{ required: true, message: "安装时间不能为空" }],
        audioDescribe: [
          { max: 64, message: "长度不能超过64个字符", trigger: "blur" }
        ]
      },
      //--
      allUserOptions: [],
      allEnterpriseOptions: [],
      enterpriseFlag: false,
      //--
      bindOperFlag: undefined,//绑定数据标识 1：查询绑定数据  2 查询未绑数据
      selectAudioId: undefined, //
      dialogCameraFormVisible:false
    };
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
    ...mapGetters(["staticData","user"])
  },
  methods: {
    intiParam() {
      let _this = this;
      if(_this.user.userType==4){
        _this.listQuery.enterpriseId =_this.user.enterpriseId;
        _this.form.enterpriseId = _this.user.enterpriseId+"";
        _this.enterpriseFlag = true;
      }
      getAllUserMap()
        .then(function(response) {
          _this.allUserOptions = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "用户信息获取失败。"
          });
        });
        //企业信息
      getAllEnterpriseMap()
        .then(function(response) {
          _this.allEnterpriseOptions = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "企业信息获取失败。"
          });
        });
    },
    getList() {
      this.listLoading = true;
      page(this.listQuery).then(response => {
        this.tableList = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    //查询
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    //重置查询条件
    resetQuery() {
      this.listQuery = {
        audioNoLike: undefined,
        audioNamelike: undefined,
        enterpriseId: undefined,
        status: "1"
      };
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
    //
    handleCreate() {
      this.userManager_update = true;
      this.dialogFormVisible = true;
      this.dialogStatus = "create";
      this.userNameSelected = "";
    },
    handleView(row) {
      this.form = row;

      this.userManager_update = false;
      this.dialogFormVisible = true;
      this.userNameSelected = row.audioNo;
      this.dialogStatus = "view";
    },
    handleUpdate(row) {
      this.form = row;
      this.form.enterpriseId = this.form.enterpriseId+"";
      this.userManager_update = true;
      this.dialogFormVisible = true;
      this.dialogStatus = "update";
      this.userNameSelected = row.audioNo;
    },
    //绑定摄像头
    handleBindCamera() {
      if (!this.userSelection.length && this.userSelection.length!=1) {
        this.$message({
          showClose: true,
          message: "请先选择一条信息！"
        });
        return;
      }
      this.dialogCameraFormVisible = true;
      this.selectAudioId = this.userSelection[0].audioId;
      this.dialogStatus="bind";
      this.bindOperFlag = 2;//查询未绑定数据
    },
    handleUnBindCamera(row) {
      this.dialogCameraFormVisible = true;
      this.selectAudioId = row.audioId;
      this.dialogStatus="unbind";
      this.bindOperFlag = 1;//查询绑定数据

    },
    handleDelete() {
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
            idArr.push(item.audioId);
          });
          delObjs(idArr.join(","))
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
    create(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          addAudio(this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
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
          putAudio(this.form.userId, this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
              message: "更新成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          return false;
        }
      });
    },
    cancel(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    //重置表单
    resetTmp() {
      this.form = {
        audioId: undefined,
        areaId: undefined, //行政区_区域编号
        audioNo: undefined,
        audioName: undefined,
        audioDescribe: undefined,
        namagerUrl: undefined,
        equModelId: undefined,
        installTime: undefined,
        equipmentStatus: "1", //设备状态 0未知 1在线 2离线
        specialStatus: "0" ////是否专用 1是 0否
      };
      this.$nextTick(function() {
        this.$refs["form"].resetFields();
      });
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
    //工号格式化
    opUserformatter(param) {
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
    closeDialog() {
      this.dialogCameraFormVisible = false;
      this.getList();
    }
  } //end methods
};
</script>

