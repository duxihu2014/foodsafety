<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId">
          </queryConditions>
          <el-form-item label="内容关键字" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="内容关键字" v-model="listQuery.writingLike"> </el-input>
          </el-form-item>
          <el-form-item label="状态" class="filter-item">
           <el-select v-model="listQuery.status" placeholder="状态" >
              <el-option label="待处理" value="2"></el-option>
              <el-option label="已处理" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button class="filter-item" type="primary" v-waves icon="search" @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>

    <el-tabs v-model="tabPosition" @tab-click="handleClick">
      <el-tab-pane label="平台上报" name="first">
      </el-tab-pane>
      <el-tab-pane label="公众上报" name="second">
      </el-tab-pane>
    </el-tabs>
    <el-table :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column width="200px" align="center" label="投诉企业" prop="enterpriseName"></el-table-column>
      <el-table-column width="250px" align="center" label="投诉描述" prop="writing" show-overflow-tooltip></el-table-column>
      <el-table-column width="200px" align="center" label="提交时间" prop="createTime"></el-table-column>
      <el-table-column width="200px" align="center" label="提交人姓名" prop="submitName"></el-table-column>
      <el-table-column width="200px" align="center" label="提交者电话" prop="submitTel"></el-table-column>
      <el-table-column width="200px" align="center" label="状态" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status==2" >{{statusFormatter(scope.row.status)}}</el-tag>
          <el-tag v-if="scope.row.status==3" type="">{{statusFormatter(scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="center" label="处理结果" prop="handleResult" show-overflow-tooltip></el-table-column>
      <el-table-column width="200px" align="center" label="责任人" prop="personLiable" ></el-table-column>
      <el-table-column width="200px" align="center" label="责任人电话" prop="personLiableTel"></el-table-column>
      <el-table-column width="200px" align="center" label="处理人" prop="handleManId" :formatter="userFormatter"></el-table-column>
      <el-table-column width="200px" align="center" label="处理时间" prop="handleTime"></el-table-column>

      <el-table-column align="center" label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button size="small" v-if="scope.row.status==3"   @click="handleView(scope.row)">查看</el-button>
          <el-button size="small" v-if="scope.row.status==2" type="primary" @click="handleView(scope.row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <div style="height: 550px;">
        <el-tabs v-model="viewTabPanl">
          <el-tab-pane label="投诉信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px">
              <el-form-item v-if="isView" label="投诉企业" prop="enterpriseName">
                <el-input v-model="form.enterpriseName" disabled></el-input>
              </el-form-item>
              <el-form-item v-if="!isView" label="投诉企业" prop="enterprisId">
                <el-select v-model="enterpriseId" placeholder="请选择" clearable @change="handleSelectChange">
                  <el-option v-for="item in  enterpriseMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="投诉描述" prop="writing">
                <el-input type="textarea" :rows="2" v-model="form.writing" :disabled="isView"></el-input>
              </el-form-item>
              <el-form-item label="图片" prop="imgFile" ref="imgFile">
                <el-upload action="https://jsonplaceholder.typicode.com/posts/" list-type="picture-card" multiple :file-list="imgFile" :on-preview="handlePictureCardPreview" :on-remove="handleImageRemove" :on-change="addImgFile" :limit="4" :disabled="isView" :auto-upload="false" :on-exceed="handleExceed">
                  <i class="el-icon-plus"></i>
                </el-upload>
              </el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="提交人" prop="submitName">
                    <el-input v-model="form.submitName" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="提交人电话" prop="submitTel">
                    <el-input v-model="form.submitTel" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交平台" prop="submitPlatformDesc">
                    <el-input v-model="form.submitPlatformDesc" disabled></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item v-if="isView" label="提交时间" prop="createTime">
                    <el-input v-model="form.createTime" disabled></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane  label="处理信息">
            <el-form :model="solutionform" :rules="solutionRules" ref="solutionform" label-width="100px">
              <el-row v-if="form.status == 3">
                <el-col :span="12">
                  <el-form-item v-if="isView" label="处理人" prop="handleManName">
                    <el-input v-model="solutionform.handleManName" :disabled="isView" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item  label="处理时间" prop="handleTime">
                    <el-input v-model="solutionform.handleTime" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="责任人" prop="personLiable">
                    <el-input v-if="form.status == 3" v-model="solutionform.personLiable" :disabled="isView"></el-input>
                    <el-input v-else v-model="solutionform.personLiable" ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="责任人电话" prop="personLiableTel">
                    <el-input  v-if="form.status == 3" v-model="solutionform.personLiableTel" :disabled="isView"></el-input>
                    <el-input  v-else v-model="solutionform.personLiableTel"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
               <el-form-item  label="处理结果" prop="handleResult">
                <el-input v-if="form.status == 3" type="textarea" :rows="3" v-model="solutionform.handleResult" :disabled="isView"></el-input>
                <el-input v-else type="textarea" :rows="3" v-model="solutionform.handleResult" ></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
       <div v-if="form.status == 2 && viewTabPanl=='0'" slot="footer" class="dialog-footer">
            <el-button type="primary" @click="handleResultView(form.complainEvaluateId)">下一步</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="userManager_update && form.status == 2 && viewTabPanl=='1'">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button type="primary" @click="faultEdit('solutionform')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getObj,
  putObj,
  addObj,
  delObj,
  evaluateEdit,
  acceptance,
  handleEdit
} from "api/admin/evaluate/index";
import parent from "api/admin/common/parent";
import { parseValueToText,parseTime } from "utils/index";
import { getAllUserMap } from "api/admin/user/index";
import { getMulResource } from "api/admin/register/index";
import queryConditions from "components/QueryConditions/index";
import { getEnterpriseBaseMap } from "api/admin/safety/record/recyle/index";
import { loadGridHeight } from "api/screen";

export default {
  name: "event",
  components: {
    queryConditions
  },
  mixins: [parent],
  data() {
    const gradeValidator = (rule, value, callback) => {
      if (!this.gradeStar || this.gradeStar == 0) {
        callback(new Error("请设置评价分数"));
      } else {
        callback();
      }
    };
    return {
      height: undefined,
      needFixedRight: false,
      enterpriseId: undefined,
      enterpriseMap: [],
      equOptions: [],
      listQuery: {
        page: 1,
        limit: 20,
        writingLike: undefined,
        typeId: "1",
        submitPlatform: "1", //1平台 2微信
        enterpriseId: undefined,
        areaId: undefined,
        gridId: undefined,
        status: undefined,
        someStatus: "2,3",
      },
      form: {
        complainEvaluateId: undefined,
        typeId: "1",
        writing: undefined,
        imgPath: undefined,
        grade: undefined,
        submitName: undefined,
        submitTel: undefined,
        submitPlatform: "1", //1app 2微信
        enterpriseId: undefined
      },
      solutionform: {
        complainEvaluateId: undefined,
        handleManId: undefined,
        handleManName: undefined,
        handleResult: undefined,
        handleTime: undefined,
        personLiable: undefined,
        personLiableTel: undefined
      },
      rules: {
      },
      solutionRules: {
        handleResult: [
          { required: true, message: "请输入处理结果描述", trigger: "blur" }
        ],
        personLiable: [
          { required: true, message: "请输入负责人姓名", trigger: "blur" }
        ],
        personLiableTel: [
          { required: true, message: "请输入负责人电话", trigger: "blur" }
        ]
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        view: "详情"
      },
      userManager_update: true,
      isView: false,
      httpUrl: "", //文件显示URL

      imgFile: [],
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      fileFormat: ["image/jpeg", "image/png", "image/bmp", "image/gif"], // 允许上传的文件格式
      allUserOptions: [],
      tabPosition: "first",
      viewTabPanl: "0",
      gradeShow: "",
      gradeStar: undefined
    };
  },
  created() {
    this.init();
    this.getList();
  },
  computed: {},
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
    //表格样式
    tableRowClassName({ row, rowIndex }) {
      if (row.userStatus === "0") {
        return "warning-row";
      } else if (row.userStatus === "1") {
        return "success-row";
      }
      return "";
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

    init() {
      let _this = this;
      _this.httpUrl = process.env.SERVERIMAGEURL;

      getAllUserMap()
        .then(response => {
          _this.allUserOptions = response.data;
        })
        .catch(() => {});
      //
      getEnterpriseBaseMap().then(response => {
        this.enterpriseMap = response["enterprise"];
      });
    },
    handleSelectChange(val) {
      this.form.enterpriseId = val;
    },
    //tab转换查询
    handleClick() {
      if (this.tabPosition == "first") {
        this.listQuery.submitPlatform = "1";
      } else {
        this.listQuery.submitPlatform = "2";
      }
      this.getList();
    },
    getList() {
      this.listLoading = true;
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      });
    },
    eventTypeFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      if (cellValue == "1") {
        return "投诉";
      } else if (cellValue == "2") {
        return "评价";
      }
    },
    userFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.allUserOptions);
    },
    submitPlatformFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["上报来源"]);
    },
    statusFormatter(cellValue) {
      if (!cellValue) {
        return "";
      }
      if (cellValue == "2") {
        return "待处理";
      } else if (cellValue == "3") {
        return "已处理";
      }else{
        return "";
      }
    },
    resetQuery() {
      this.listQuery.writingLike = undefined;
      this.listQuery.typeId = "1";
      this.listQuery.submitPlatform = "1"; //1平台 2微信
      this.listQuery.enterpriseId = undefined;
      this.listQuery.areaId = undefined;
      this.listQuery.gridId = undefined;
      this.listQuery.status  = undefined;
      this.listQuery.someStatus = "2,3";
    },
    handleView(row) {
      this.imgFile = [];
      getObj(row.complainEvaluateId).then(response => {
        this.form = response.data;
        this.enterpriseId = response.data.enterpriseId + "";
        this.gradeShow = this.form.grade + "";
        this.gradeStar = this.form.grade / 2;
        this.form.submitPlatformDesc = parseValueToText(
          response.data.submitPlatform,
          this.staticData["上报来源"]
        );
        this.form.createOpName1 = parseValueToText(
          response.data.createOp1,
          this.allUserOptions
        );

        if (this.form.imgPath) {
          let mulResourceIds = this.form.imgPath;
          getMulResource(mulResourceIds).then(response => {
            let imgArr = response.data;
            this.imgFile = [];
            for (let i = 0; i < imgArr.length; i++) {
              this.imgFile.push({
                url: this.httpUrl + "/" + imgArr[i].resourcePath,
                id: imgArr[i].resourceId + ""
              });
            }
            $(".el-upload--picture-card").hide();
          });
        }
        if(this.form.status==3){
          this.solutionform.handleManId = response.data.handleManId;
          this.solutionform.handleManName = parseValueToText(response.data.handleManId, this.allUserOptions);
          this.solutionform.handleResult = response.data.handleResult;
          this.solutionform.handleTime = parseTime(response.data.handleTime,'{y}-{m}-{d}');
          this.solutionform.personLiable = response.data.personLiable;
          this.solutionform.personLiableTel = response.data.personLiableTel;

          this.userManager_update = false;
          this.viewTabPanl = "0";
        }else if(this.form.status==2){
          this.handleResultView(response.data.complainEvaluateId);
        }

        this.dialogStatus = "view";
        this.isView = true;
        this.dialogFormVisible = true;

      });
    },
    handleCreate() {
    },
    handleUpdate(row) {

    },

    //10分制
    handleGradeChange(val) {
      this.gradeShow = val * 2 + "";
      this.form.grade = val * 2;
    },
    handleResultView(id) {

      this.viewTabPanl = "1"
      this.userManager_update = true;

      this.solutionform.complainEvaluateId = id;
      this.solutionform.handleManId= "";
      this.solutionform.handleResult= "";
      this.solutionform.handleTime= "";
      this.solutionform.personLiable= "";
      this.solutionform.personLiableTel= "";
    },

    //投诉处理
    faultEdit(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          handleEdit(this.solutionform.complainEvaluateId, this.solutionform).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: "成功",
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
    //受理
    // handleAcceptance(id){
    //    acceptance(id).then(() => {
    //         this.dialogFormVisible = false;
    //         this.getList();
    //         this.$notify({
    //           title: "成功",
    //           message: "操作成功",
    //           type: "success",
    //           duration: 2000
    //         });
    //       });
    // },

    cancel() {
      this.dialogFormVisible = false;

    },
    resetTmp() {
      this.form = {
        complainEvaluateId: undefined,
        typeId: "1",
        writing: undefined,
        imgPath: undefined,
        grade: undefined,
        submitName: undefined,
        submitTel: undefined,
        submitPlatform: "1" //1app 2微信
      };
      this.imgFile = [];

      this.$nextTick(function() {
        this.$refs["form"].resetFields();
      });
    },

    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 4 个文件，本次选择了 ${fileList.length} 个文件`
      );
    },

    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    //删除图片
    handleImageRemove(file, fileList) {
      if (file.id) {
        let imgArr = this.form.imgPath.split(",");
        let index = imgArr.indexOf(file.id);
        if (index > -1) {
          imgArr.splice(index, 1);
        }
        this.form.imgPath = imgArr.join(",");
      }
      this.imgFile = fileList;
      if (this.imgFile.length == 0) {
        $(".el-upload--picture-card").show();
      }
    },
    addImgFile(file) {
      // 添加图片回调
      if (this.fileFormat.indexOf(file.raw.type) < 0) {
        this.$message({
          type: "error",
          showClose: true,
          message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
        });
        return false;
      }
      this.$refs["imgFile"].resetField(); //当添加图片后，重置图片验证功能。
      this.imgFile.push(file);
      if (this.imgFile.length > 4) {
        $(".el-upload--picture-card").hide();
      }
    },
    closeDialog() {
      this.form = {};
      this.imgFile = [];
      $(".el-upload--picture-card").show();
      this.isView = false;
      this.isUpdate = false;
      this.$refs["form"].resetFields();
      this.verifyResultReason = {};
    }
  }
};
</script>
<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>


