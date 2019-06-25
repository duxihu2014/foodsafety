<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="设备名称" prop="equipmentName" class="filter-item">
            <el-input v-model.trim="listQuery.equipmentNameLike" placeholder="请输入设备名称" :disabled="isView"></el-input>
          </el-form-item>
          <el-form-item label="原料类型" prop="equipmentType" class="filter-item">
            <el-select v-model="listQuery.equipmentType" placeholder="请选择" clearable>
              <el-option v-for="item in  equipmentTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="equipmentStatus" class="filter-item">
            <el-select v-model="listQuery.equipmentStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
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
          <i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>新增</span>
      </div>
    </div>
    <el-table :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="设备名称" width="200" prop="equipmentName"></el-table-column>
      <el-table-column align="center" label="设备类型" width="200" prop="equipmentType" :formatter="materialTypeFormatter"></el-table-column>
      <el-table-column align="center" label="规格型号" width="200" prop="specificationModel"></el-table-column>
      <el-table-column align="center" label="精度等级" width="250" prop="precisionGrade"></el-table-column>
      <el-table-column align="center" label="数量" width="150" prop="quantity"></el-table-column>
      <el-table-column align="center" label="完好状态" width="150" prop="perfectState"></el-table-column>
      <el-table-column align="center" label="检定有效期" width="150" prop="verificationPeriod" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="使用场所" width="150" prop="usePlace"></el-table-column>
      <el-table-column align="center" label="生产厂家" width="150" prop="manufacturer"></el-table-column>
      <el-table-column align="center" label="国别" width="150" prop="country"></el-table-column>
      <el-table-column align="center" label="生产日期" width="150" prop="manufactureDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="购置日期" width="150" prop="purchaseDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="变更状态" width="100" prop="verifyStatus">
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.verifyStatus=='1'">审核中</el-tag>
          <el-tag type="success" v-else-if="scope.row.verifyStatus=='2'||scope.row.verifyStatus=='3'">完成</el-tag>
          <el-tag type="info" v-else>未变更</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变更原因" width="200" prop="changeReason" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="设备状态" width="100" prop="equipmentStatus" :formatter="statusFormatter"></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="250" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.verifyStatus == '1'">修改</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.verifyStatus == '1'">报废</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.verifyStatus == '1'">修改</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.verifyStatus == '1'">报废</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <!--修改，查看，新增表单对话框-->
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '新增'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '修改'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <div style="height: 500px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="设备信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="120px">
              <el-row>
                <el-col :span="10">
                  <el-form-item label="设备名称:" prop="equipmentName">
                    <el-input v-model.trim="form.equipmentName" placeholder="请输入设备名称" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="设备类型:" prop="equipmentType">
                    <el-select v-model="form.equipmentType" placeholder="请选择" clearable :disabled="isView">
                      <el-option v-for="item in  equipmentTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="规格型号:" prop="specificationModel">
                    <el-input v-model.trim="form.specificationModel" placeholder="请输入规格型号" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="精度等级:" prop="precisionGrade">
                    <el-input v-model.trim="form.precisionGrade" placeholder="请输入精度等级" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="数量:" prop="quantity">
                    <el-input-number v-model.trim="form.quantity" :min="1" label="请输入数量" :disabled="isView"></el-input-number>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="完好状态:" prop="perfectState">
                    <el-input v-model.trim="form.perfectState" placeholder="请输入完好状态" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="检定有效期:" prop="verificationPeriod">
                <el-date-picker type="date" placeholder="请选择" v-model="form.verificationPeriod" :disabled="isView"></el-date-picker>
              </el-form-item>

              <el-row>
                <el-col :span="10">
                  <el-form-item label="生产厂家:" prop="manufacturer">
                    <el-input v-model.trim="form.manufacturer" placeholder="请输入生产厂家" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="国别:" prop="country">
                    <el-input v-model.trim="form.country" placeholder="请输入国别" :disabled="isView"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="生产日期:" prop="manufactureDate">
                <el-date-picker type="date" placeholder="请选择" v-model="form.manufactureDate" :disabled="isView"></el-date-picker>
              </el-form-item>

              <el-form-item label="购置日期:" prop="purchaseDate">
                <el-date-picker type="date" placeholder="请选择" v-model="form.purchaseDate" :disabled="isView"></el-date-picker>
              </el-form-item>

              <el-form-item label="使用场所:" prop="usePlace">
                <el-input v-model.trim="form.usePlace" placeholder="请输入使用场所" :disabled="isView"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="变更原因">
            <el-form :model="changeVerifyForm" :rules="rules" ref="changeReasonForm">
              <el-form-item prop="reason">
                <el-input type="textarea" :rows="5" v-model.trim="changeVerifyForm.reason" :disabled="isView"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer" v-if="user_btn_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create()">确 定</el-button>
        <el-button v-else type="primary" @click="update()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getResource } from "api/admin/register/index";
import { parseValueToText, parseTime, findParentNode,CompareDateTime } from "utils/index";
import {
  page,
  getObj,
  getEnterpriseEqu,
  addEnterpriseEqu,
  changeEnterpriseEqu,
  delObj
} from "api/admin/enterprise/equipment/index";
import { loadGridHeight } from "api/screen";
export default {
  name: "index",
  data() {
      const checkDateValidator = (rule, value, callback) => {
      if (CompareDateTime(value, this.form.manufactureDate)) {
        callback("购置日期必须在生产日期之后！");
      } else {
        callback();
      }
    };
    return {
      tabPosition: "0",
      height: undefined,
      list: null,
      total: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        equipmentNameLike: undefined,
        equipmentType: undefined,
        enterpriseId: undefined,
        equipmentStatus: undefined
      },
      textMap: {
        update: "修改",
        create: "新增",
        view: "查看"
      },
      dialogStatus: "",
      dialogFormVisible: false,
      user_btn_update: false,
      isView: false, //是否是查看状态
      isUpdate: false, //是否是编辑状态，编辑状态的证照类型不可以修改
      needFixedRight: false,

      form: {},
      formStr: "", //表单初始数据字符串，用于判断表单数据是否被修改
      changeVerifyForm: { reason: undefined },
      rules: {
        equipmentName: [
          { required: true, message: "请输入设备名称", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        equipmentType: [
          { required: true, message: "请输入设备类型", trigger: "change" }
        ],
        specificationModel: [
          { required: true, message: "请输入规格型号", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        quantity: [
          { required: true, message: "请选择数量" }
        ],
        precisionGrade: [
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        perfectState: [
          { required: true, message: "请输入完好状态", trigger: "blur" },
          { max: 16, message: "长度不能超过16个字符", trigger: "blur" }
        ],
        verificationPeriod: [{ required: true, message: "请输入检定有效期" }],
        manufacturer: [
          { required: true, message: "请输入生产厂家", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        country: [
          { required: true, message: "请输入国别", trigger: "blur" },
          { max: 16, message: "长度不能超过16个字符", trigger: "blur" }
        ],
        manufactureDate: [{ required: true, message: "请输入生产日期" }],
        purchaseDate: [{ required: true, message: "请输入购置日期" }, { validator: checkDateValidator }],
        usePlace: [
          { required: true, message: "请输入使用场所", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ],
        reason: [{ required: true, message: "请输入理由", trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(["user", "staticData", "areaData"]),

    equipmentTypeOptions() {
      return this.staticData["设备类型"];
    },
    statusOptions() {
      return this.staticData["企业设备状态"];
    }
  },
  created() {
    this.initParam();
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
  methods: {
    initParam() {},
    getList() {
      this.listLoading = true;
      this.listQuery.enterpriseId = this.user.enterpriseId;
      page(this.listQuery).then(response => {
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
      this.listQuery.equipmentNameLike = undefined;
      this.listQuery.equipmentType = undefined;
      this.listQuery.equipmentStatus = undefined;
    },

    handleCreate() {
      //新增
      this.form.enterpriseId = this.user.enterpriseId;
      this.user_btn_update = true;
      this.tabPosition = "0";
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
    },
    handleView(row) {
      //查看
      getEnterpriseEqu(row.equipmentId).then(response => {
        this.form = response.data;

        if (response.data.changeReason) {
          this.changeVerifyForm.reason = response.data.changeReason;
        } else {
          this.changeVerifyForm.reason = undefined;
        }

        this.isView = true;
        this.dialogFormVisible = true;
        this.user_btn_update = false;
        this.dialogStatus = "view";
      });
    },
    handleUpdate(row) {
      //修改
      getObj(row.equipmentId).then(response => {
        this.form = response.data;
        this.formStr = JSON.stringify(this.form);

        this.isUpdate = true;
        this.dialogFormVisible = true;
        this.tabPosition = "0";
        this.user_btn_update = true;
        this.dialogStatus = "update";
      });
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.certificateStatus === "0") {
        return "warning-row";
      } else if (row.certificateStatus === "1") {
        return "success-row";
      }
      return "";
    },
    cancel() {
      this.dialogFormVisible = false;
    },
    //新增提交
    create() {
      let _self = this;
      let flag = true;
      _self.$refs["form"].validate((valid, errors) => {
        if (!valid) {
          flag = false;
          _self.tabPosition = "0";
        }
      });
      _self.$refs["changeReasonForm"].validate((valid, errors) => {
        if (!flag) {
          _self.tabPosition = "0";
        } else {
          if (!(flag && valid)) {
            flag = false;
            _self.tabPosition = "1";
          }
        }
      });
      if (flag) {
        _self.form.verificationPeriod = parseTime(
          _self.form.verificationPeriod
        );
        _self.form.manufactureDate = parseTime(_self.form.manufactureDate);
        _self.form.purchaseDate = parseTime(_self.form.purchaseDate);
        let param = new FormData();
        param.append("EnterpriseEquipment", JSON.stringify(_self.form));
        param.append("reason", _self.changeVerifyForm.reason);

        addEnterpriseEqu(param).then(response => {
          _self.dialogFormVisible = false;
          _self.getList();
          if (response.rel) {
            _self.$message({
              type: "success",
              showClose: true,
              message: `操作成功！`
            });
          } else {
            _self.$message({
              type: "error",
              showClose: true,
              message: `操作失败！`
            });
          }
        });
      }
    },
    //修改提交
    update() {
      let _self = this;
      let flag = true;
      _self.$refs["form"].validate((valid, errors) => {
        if (!valid) {
          flag = false;
          _self.tabPosition = "0";
        } else {
          _self.form.quantity = _self.form.quantity + "";
          if (_self.formStr == JSON.stringify(_self.form)) {
            //未修改
            flag = false;
            this.$message("您还未修改数据哦！");
            _self.tabPosition = "0";
          }
        }
      });
      _self.$refs["changeReasonForm"].validate((valid, errors) => {
        if (!flag) {
          _self.tabPosition = "0";
        } else {
          if (!(flag && valid)) {
            flag = false;
            _self.tabPosition = "1";
          }
        }
      });
      if (flag) {
        _self.form.verificationPeriod = parseTime(
          _self.form.verificationPeriod
        );
        _self.form.manufactureDate = parseTime(_self.form.manufactureDate);
        _self.form.purchaseDate = parseTime(_self.form.purchaseDate);
        let param = new FormData();
        param.append("EnterpriseEquipment", JSON.stringify(_self.form));
        param.append("reason", _self.changeVerifyForm.reason);

        changeEnterpriseEqu(param).then(response => {
          _self.dialogFormVisible = false;
          _self.getList();
          if (response.rel) {
            _self.$message({
              type: "success",
              showClose: true,
              message: `操作成功！`
            });
          } else {
            _self.$message({
              type: "error",
              showClose: true,
              message: `操作失败！`
            });
          }
        });
      }
    },
    handleDelete(row) {
      this.$confirm("此操作会将设备永久报废, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delObj(row.equipmentId)
            .then(() => {
              this.$message({
                showClose: true,
                message: "操作成功",
                type: "success",
                duration: 2000
              });
              this.getList();
            })
            .catch(() => {});
        })
        .catch(() => {});
    },

    //变更记录查询
    changeView(row) {
      getChangeByCertificateId({ certificateId: row.certificateId }).then(
        response => {
          console.log(response);
        }
      );
    },

    closeDialog() {
      this.form = {};
      this.isView = false;
      this.isUpdate = false;
      this.$refs["form"].resetFields();
      this.verifyResultReason = {};
      this.$refs["changeReasonForm"].resetFields();
    },

    materialTypeFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["设备类型"]);
    },
    timeFormatter(row, column, cellValue) {
      return parseTime(cellValue, "{y}-{m}-{d}");
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData["企业设备状态"]);
    }
  }
};
</script>

<style scoped>
</style>
