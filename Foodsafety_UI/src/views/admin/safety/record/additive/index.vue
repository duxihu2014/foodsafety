<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <!-- <el-form-item label="使用人" prop="userStatus" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.usePersonLike"> </el-input>
          </el-form-item> -->
          <el-form-item label="使用日期" prop="useDate" class="filter-item">
            <el-date-picker v-model="listQuery.useDateArr" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="采购日期" prop="purchaseDate" class="filter-item">
            <el-date-picker v-model="listQuery.purchaseDateArr" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd">
            </el-date-picker>
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
      </div>
    </div>
    <el-table :key='tableKey' :data="tableList" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height" tooltip-effect="dark">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column width="200px" align="center" label="原料名称" prop="materialName" show-overflow-tooltip></el-table-column>
      <el-table-column width="250px" align="center" label="供货商名称" prop="supplierName" show-overflow-tooltip></el-table-column>
      <el-table-column width="120px" align="center" label="采购日期" prop="purchaseDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column width="120px" align="center" label="使用日期" prop="useDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column width="120px" align="center" label="使用人" prop="usePerson"></el-table-column>
      <el-table-column width="110px" align="center" label="使用规定量(克/千克)" prop="requisiteAmount"></el-table-column>
      <el-table-column width="110px" align="center" label="实例使用量(克/千克)" prop="useAmount"></el-table-column>
      <el-table-column width="150px" align="center" label="使用用途" prop="purpose"></el-table-column>
      <el-table-column width="180px" align="center" label="创建时间" prop="createTime"></el-table-column>
      <el-table-column width="180px" align="center" label="修改时间" prop="updateTime"></el-table-column>
      <el-table-column align="center" label="操作" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑</el-button>
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
                <el-form-item label="所属企业" prop="enterpriseId">
                  <el-select v-model="form.enterpriseId" placeholder="请选择" :disabled="enterpriseFlag" clearable @change="handleChangeEnterprise">
                    <el-option v-for="item in allEnterpriseOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="采购编号" prop="purchaseId">
                  <el-select v-model="form.purchaseId" placeholder="请选择" clearable>
                    <el-option v-for="item in purchaseCombo" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="使用日期" prop="useDate">
                  <el-date-picker v-model="form.useDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="使用人" prop="usePerson">
                  <el-input v-model.trim="form.usePerson" placeholder=""></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="使用规定量(克/千克)" prop="requisiteAmount">
                  <!-- <el-input v-model.trim="form.requisiteAmount" placeholder=""></el-input> -->
                  <el-input-number v-model="form.requisiteAmount" :min="1" :max="500" label=""></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实例使用量(克/千克)" prop="useAmount">
                  <!-- <el-input v-model.trim="form.useAmount" placeholder=""></el-input> -->
                  <el-input-number v-model="form.useAmount" :min="1" :max="500" label=""></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="使用用途" prop="purpose">
                  <el-input v-model.trim="form.purpose" placeholder=""></el-input>
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

  </div>
</template>

<script>
import {
  page,
  getObj,
  addObj,
  putObj,
  delObjs,
  getPurchaseCombo
} from "api/admin/safety/record/additive/index";
import { getAllEnterpriseMap } from "api/admin/equipment/audio";
import { mapGetters } from "vuex";
import { parseTime } from "utils/index";
import { loadGridHeight } from "api/screen";
import { spacelValidator } from "utils/validate";

export default {
  name: "audio",
  components: {},
  data() {
    return {
      height: undefined,
      needFixedRight: false,
      //--查询
      listQuery: {
        page: 1,
        limit: 20,
        // purchaseIdLike: undefined,
        // usePersonLike: undefined,
        useDateArr: [],
        useDateStart: undefined,
        useDateEnd: undefined,
        purchaseDateArr: [],
        purchaseDateStart: undefined,
        purchaseDateEnd: undefined
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
        view: "查看"
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
        useageId: undefined,
        enterpriseId: undefined,
        purchaseId: undefined,
        useDate: undefined,
        usePerson: undefined,
        requisiteAmount: undefined,
        useAmount: undefined,
        purpose: undefined
      },
      rules: {
        purchaseId: [
          {
            required: true,
            message: "采购编号不能为空",
            validator: spacelValidator
          }
        ],
        useDate: [{ required: true, message: "使用日期不能为空" }],
        usePerson: [
          { required: true, message: "使用人不能为空", trigger: "blur" },
          { max: 8, message: "长度不能超过8个字符", trigger: "blur" }
        ],
        requisiteAmount: [{ required: true, message: "规格使用数量不能为空" }],
        useAmount: [{ required: true, message: "实际使用数量不能为空" }],
        purpose: [
          { required: true, message: "使用用途不能为空", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ]
      },
      //--
      allEnterpriseOptions: [],
      purchaseCombo: [],
      enterpriseFlag: false
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
    ...mapGetters(["staticData", "user"])
  },
  methods: {
    intiParam() {
      let _this = this;
      if (_this.user.userType == 4) {
        _this.listQuery.enterpriseId = _this.user.enterpriseId;
        _this.form.enterpriseId = _this.user.enterpriseId + "";
        _this.enterpriseFlag = true;
        _this.getPurcharseInfo(_this.user.enterpriseId);
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
            message: "企业信息获取失败。"
          });
        });
    },
    // 选择企业时,采购编号变化
    handleChangeEnterprise(val) {
      this.purchaseCombo = [];
      this.getPurcharseInfo(val);
    },
    getPurcharseInfo(eId) {
      let _this = this;
      let params;
      if (eId) {
        params = { enterpriseId: eId };
      }

      getPurchaseCombo(params)
        .then(function(response) {
          _this.purchaseCombo = response.data;
        })
        .catch(() => {
          this.$message({
            type: "error",
            showClose: true,
            message: "采购编号信息获取失败。"
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
      if (this.listQuery.useDateArr.length > 0) {
        this.listQuery.useDateStart = this.listQuery.useDateArr[0];
        this.listQuery.useDateEnd = this.listQuery.useDateArr[1];
      }
      if (this.listQuery.purchaseDateArr.length > 0) {
        this.listQuery.purchaseDateStart = this.listQuery.purchaseDateArr[0];
        this.listQuery.purchaseDateEnd = this.listQuery.purchaseDateArr[1];
      }
      this.listQuery.page = 1;
      this.getList();
    },
    //重置查询条件
    resetQuery() {
      this.listQuery = {
        // purchaseIdLike: undefined,
        // usePersonLike: undefined,
        useDateArr: [],
        useDateStart: undefined,
        useDateEnd: undefined,
        purchaseDateArr: [],
        purchaseDateStart: undefined,
        purchaseDateEnd: undefined
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
      getObj(row.useageId).then(response => {
        this.form = response.data;
        this.userManager_update = true;
        this.dialogFormVisible = true;
        this.dialogStatus = "update";
        this.userNameSelected = row.audioNo;
      });
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
            idArr.push(item.useageId);
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
          addObj(this.form).then(() => {
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
          putObj(this.form.useageId, this.form).then(() => {
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
        useageId: undefined,
        enterpriseId: undefined,
        purchaseId: undefined,
        useDate: undefined,
        usePerson: undefined,
        requisiteAmount: undefined,
        useAmount: undefined,
        purpose: undefined
      };
      this.$nextTick(function() {
        this.$refs["form"].resetFields();
      });
    },
    timeFormatter(row, column, cellValue) {
      if (!cellValue) {
        return "";
      }
      return parseTime(cellValue, "{y}-{m}-{d}");
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

