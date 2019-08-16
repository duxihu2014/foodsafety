<template>
  <div>
    <el-row>
      <el-col>
        <div>
          <el-form :inline="true" :model="listQuery">
            <el-form-item label="名称" prop="equipmentName">
              <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="摄像头名称" v-model="listQuery.equipmentNameLike"> </el-input>
            </el-form-item>
            <el-form-item class="filter-item">
              <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
              <el-button v-waves @click="resetQuery()">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="head-box">
          <div class="item-box">
            <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate">
              <i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
            <!--<span class="mg-r-15 head-item" @click.stop.prevent="handleUpdate"><i class="fa fa-pencil menu-icon vam" aria-hidden="true"></i>编辑</span>-->
            <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete">
              <i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
          </div>
        </div>
        <el-table :key='tableKey' :data="list" v-loading.body="listLoading" tooltip-effect="dark" border highlight-current-row style="width: 100%;margin-top:0px;" @selection-change="getSelection" height="440" :header-cell-style="{'background-color': '#fafafa'}">
          <el-table-column type="selection" align="center" width="50"></el-table-column>
          <el-table-column width="200" align="center" label="设备名称" prop="equipmentName">
          </el-table-column>
          <el-table-column width="150" align="center" label="设备编码" prop="equipmentNo">
          </el-table-column>
          <el-table-column width="120" align="center" label="安装时间" prop="installTime" :formatter="dateFormatter">
          </el-table-column>
          <el-table-column width="120" align="center" label="创建时间" prop="createTime" :formatter="dateFormatter">
          </el-table-column>
          <el-table-column width="200" align="center" label="企业" prop="enterpriseName" show-overflow-tooltip>
          </el-table-column>
          <el-table-column width="150" align="center" label="分组" prop="enterpriseGroupName">
          </el-table-column>
          <el-table-column width="150" align="center" label="型号" prop="equModel">
          </el-table-column>
          <el-table-column width="150" align="center" label="音频" prop="audioName">
          </el-table-column>
          <el-table-column align="center" label="操作" fixed="right" width="180">
            <template slot-scope="scope">
               <a :href="playUrl" id="playHref" style="display: none"  class="sel_btn">
            播放
          </a>
       
          <a href="javascript:void(0)" @blur="cancelTimer()" @click="playVideo(scope.row)"   class="sel_btn" style="margin-right: 10px">播放</a>
              <el-button v-if="userManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">编辑
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :before-close="cancel">>
      <div style="margin-bottom: 20px;margin-top: -15px"><hr/>
        <p/>
      </div>
      <el-form :model="equipmentForm" :rules="formRules" ref="equipmentForm" label-width="120px" :inline="false">
        <el-form-item label="设备型号" prop="equModelId">
          <el-select v-model="equipmentForm.equModelId" placeholder="请选择" clearable>
            <el-option v-for="item in modelOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备用途" prop="specialStatus">
          <el-select v-model="equipmentForm.specialStatus" placeholder="请选择" clearable>
            <el-option v-for="item in specialStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备名称:" prop="equipmentName">
          <el-input v-model="equipmentForm.equipmentName" :disabled="isEdit" placeholder="设备名称"></el-input>
        </el-form-item>
        <el-form-item label="设备编码:" prop="equipmentNo">
          <el-input v-model="equipmentForm.equipmentNo" :disabled="isEdit" placeholder="请输入设备编码"></el-input>
        </el-form-item>
        <el-form-item label="设备描述:" prop="equipmentDescribe">
          <el-input v-model="equipmentForm.equipmentDescribe" :disabled="isEdit" placeholder="请输入设备描述"></el-input>
        </el-form-item>

        <el-form-item label="音频设备" prop="audioId">
          <el-select v-model="equipmentForm.audioId" placeholder="请选择" clearable>
            <el-option v-for="item in audioOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="云台控制URL:" prop="cloudControlUrl">
          <el-input v-model="equipmentForm.cloudControlUrl" :disabled="isEdit" placeholder="请输入云台控制URL"></el-input>
        </el-form-item>
        <el-form-item label="录像播放URL:" prop="videotapeUrl">
          <el-input v-model="equipmentForm.videotapeUrl" :disabled="isEdit" placeholder="请输入录像播放URL"></el-input>
        </el-form-item>
        <el-form-item label="管理URL:" prop="namagerUrl">
          <el-input v-model="equipmentForm.namagerUrl" :disabled="isEdit" placeholder="请输入管理URL"></el-input>
        </el-form-item>
        <el-form-item label="推流设备编号:" prop="equipmentPushId">
          <el-select v-model="equipmentForm.equipmentPushId" placeholder="请选择" clearable>
            <el-option v-for="item in pushDeviceOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
          </el-select>
        </el-form-item>
        <!--<el-form-item label="设备型号:" prop="equModelId" >
                <el-input v-model="equipmentForm.equModelId" :disabled="isEdit" placeholder="请输入管理员电话"></el-input>
              </el-form-item>
              <el-form-item label="设备型号:" prop="equModelId" >
                <el-input v-model="equipmentForm.equModelId" :disabled="isEdit" placeholder="请输入管理员电话"></el-input>
              </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="enterprise_update">
        <el-button @click="cancel('equipmentForm')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('equipmentForm')">确 定</el-button>
        <el-button v-else type="primary" @click="update('equipmentForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'请安装播放器'" :visible.sync="dialogDownloadVisible" width="20%">
      <div style="height: 120px;text-align: center;padding-top: 20px;">
        <span style="margin: auto 50px">
          <a id="downloadBtn" :href="downloadUrl" style="text-decoration: underline">点击下载</a>
        </span>
      </div>
    </el-dialog>
    <!-- vidio -->
    <div class="videobg" v-if="isVideoShow">
      <div class="videobox">
        <div class="iconCose">
          <div class="el-icon-circle-close  iconStyle" @click="handleCloseVideo()"><span>关闭</span></div>
        </div>
      <videoStream  :vurl="videoObj"  ></videoStream>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  page,
  addEquip,
  putEquip,
  delEquip,
  delObjs,
  checkEquNo,
  play
} from "api/admin/equipment/index";
import { getModelMap } from "api/admin/equipmentModel/index";
import { getPushModelMap } from "api/admin/equipmentModel/index"; 
import { getAudioMap } from "api/admin/equipment/audio";
import { getAllUserMap } from "api/admin/user/index";
import { spacelValidator } from "utils/validate";
import { parseValueToText, parseTime } from "utils/index";
import videoStream from "../videoStream";
export default {
  name: "",
  components: {
    videoStream,
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
      if(!this.isEdit && this.oldEquipmentNo){
        if(this.oldEquipmentNo == value){
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
        checkEquNo({ equipmentNo: value }).then(response => {
          if (response.data) {
            return callback(new Error("设备编码不能重复"));
          } else {
            callback();
          }
        });
      }
    };
    return {
      allUserOptions: [], //所有用户
      currOrgUserOptions: [], //当前设备用户
      areaOptions: [],
      selectAreaOptions: [],
      clearVisible: true,
      userSelection: [],
      modelOptions: [],
      pushDeviceOptions: [],
      audioOptions: [],
      unitId: "",
      listQuery: {
        page: 1,
        limit: 10,
        equipmentNameLike: undefined,
        enterpriseId: undefined,
        enterpriseGroupId: undefined,
        status: "1"
      },
      tableKey: 0,
      list: null,
      total: null,
      listLoading: false,
      userManager_btn_edit: true,

      dialogFormVisible: false,
      dialogVideoVisible: false,
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
        specialStatus: '1',
        audioId: undefined,
        equipmentPushId:undefined
      },
      specialStatusOptions: [{
        value: '1',
        text: '无特殊用途'
      }, {
        value: '2',
        text: '放弃物专用'
      }],
      compId: undefined,
      groupId: undefined,
      formRules: {
        equipmentNo: [
          {
            max: 32,
            message: "长度不能超过32个字符",
            trigger: "blur"
          },
          { validator: equNoValidator, trigger: "blur" },
          {
            required: true,
            message: "请输入编号",
            validator: spacelValidator,
            trigger: "blur"
          }
        ],
        equipmentName: [
          {
            max: 128,
            message: "长度不能超过32个字符",
            trigger: "blur"
          },
          {
            required: true,
            message: "请输入名称",
            validator: spacelValidator,
            trigger: "blur"
          }
        ]
      },
      oldEquipmentNo:undefined,
      playUrl: "",
      dialogDownloadVisible: false,
      downloadUrl: process.env.SERVERIMAGEURL + "/vlcplay.rar",
      downloadTimer: null,
      isVideoShow:false,
      videoObj:{},
      videoId:0,
    };
  },
  props: ["paramCompId", "paramGroupId"],
  watch: {
    paramCompId: function() {
      this.compId = this.paramCompId;
      this.listQuery.enterpriseId = this.paramCompId;
      this.getList();
    },
    paramGroupId: function() {
      this.groupId = this.paramGroupId;
      this.listQuery.enterpriseGroupId = this.paramGroupId;
      this.getList();
    }
  },
  created() {
    this.init();
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData", "user"])
  },
  methods: {
    init() {
      let _this = this;
      //初始化地区信息
      //初始化用户信息
      this.getUserMap(null);
      this.initModelOptions();
      this.initAudioOptions();
      this.initPushDeviceOptions();
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
    initPushDeviceOptions() {
      getPushModelMap().then(response => {
        this.pushDeviceOptions = response.data;
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
      if (level == "1") {
        this.listQuery.enterpriseId = id;
        this.compId = id;
      } else {
        this.listQuery.enterpriseGroupId = id;
        this.compId = pid;
        this.groupId = id;
      }
      this.getList();
    },
    getList() {
      // console.log(this.listQuery);
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
      if (!this.compId) {
        this.$message({
          showClose: true,
          message: "请选择公司或分组！"
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
    handleUpdate(data) {
      this.equipmentForm = data;
      this.oldEquipmentNo = data.equipmentNo;
      if (this.equipmentForm.equModelId) {
        this.equipmentForm.equModelId = this.equipmentForm.equModelId + "";
      }
      if (this.equipmentForm.audioId) {
        this.equipmentForm.audioId = this.equipmentForm.audioId + "";
      }
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
        idArr.push(item.equipmentId);
      });
      //设备下是否有人员
      const delId = idArr.join(",");
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delObjs(delId).then(response => {
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
          putEquip(this.equipmentForm.equipmentId, this.equipmentForm).then(
            () => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "编辑成功",
                type: "success",
                duration: 2000
              });
            }
          );
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
    handleCloseVideo(){
      this.isVideoShow=false
    },
    resetQuery() {
      this.listQuery = {
        equipmentNameLike: "",
        enterpriseId: undefined,
        enterpriseGroupId: undefined
      };
    },
    resetForm() {
      (this.equipmentForm = {
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
        specialStatus: '1',
        audioId: undefined,
        equipmentPushId:undefined
      }),(this.oldEquipmentNo = undefined),
        (this.selectAreaOptions = []);
    },
    dateFormatter(row, column, cellValue) {
      if (cellValue) {
        return parseTime(cellValue, "{y}-{m}-{d}");
      } else {
        return "";
      }
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
    cancelTimer() {
      // console.log('blur event');
      clearTimeout(this.downloadTimer);
      this.dialogDownloadVisible = false;
      this.downloadTimer = null;
    },
    showDownload() {
      this.dialogDownloadVisible = true;
      // console.log("弹出下载窗口");
    },
    playVideo(row) {
      this.isVideoShow=false;
      play(row.equipmentId).then(response => {
        if (response.data.result == "success") {
          // this.playUrl ="VLCPLAY://rtsp://admin:Otec_123@192.168.1.192:1554/cam/realmonitor?channel=1^&subtype=0";
          this.videoId++
          this.videoObj={
              vid:this.videoId,
              isShow:true,
              url:response.data.url
          }
          this.isVideoShow=true 
          // this.playUrl = "VLCPLAY://" + response.data.url;
          // document.getElementById("playHref").href = this.playUrl;
          // document.getElementById("playHref").click();
          // if (!this.downloadTimer) {
          //   this.downloadTimer = setTimeout(this.showDownload, 1000);
          // }
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
  }, //end methods
  beforeDestroy() {
    this.videoId=0;
  },

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
.videobg{
    /* position: fixed; */
    /* width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background:rgba(0,0,0,0);
    z-index: 100; */
}
.videobox{
    position: fixed;
    top: 14%;
    left: 10%;
    /* margin-top: -300px;
    margin-left: -300px; */
    z-index: 100;
}
.iconCose{
  width: 100%;
  box-sizing: border-box;
  background:#409EFF;
  color:#fff;
}
.iconStyle{
  font-size:23px;
  padding: 3px 8px;
  cursor:pointer;
}
.iconStyle>span{
  font-size: 18px;
  vertical-align: middle;
}
</style>
