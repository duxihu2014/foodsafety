/**
*
* @authors minwei (minwei@chinaotec.com)
* @date 2018-10-11 13:25:11
* @version $Id$
*/
<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <!--           <el-form-item label="企业" prop="enterpriseId" class="filter-item">
  <el-select v-model="listQuery.enterpriseId" placeholder="请选择企业" clearable :filterable="true">
    <el-option v-for="item in  enterpriseMap" :key="item.value" :label="item.text" :value="item.value"> </el-option>
  </el-select>
</el-form-item> -->
          <el-form-item label="消毒时间:" prop="disinfectionTime" class="filter-item">
            <el-date-picker v-model="listQuery.disinfectionTime" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" :picker-options="pickerOptions">
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <!--span class="mg-r-15 head-item"  v-if="delFlag" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
        <span class="mg-r-15 head-item" v-if="recoverFlag" @click.stop.prevent="handleRevocer"><i class="fa el-icon-circle-check  menu-icon vam" aria-hidden="true"></i>恢复</span> -->
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :height="height">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <!--<el-table-column align="center" label="编号" width="100" prop="recordId">
      </el-table-column>-->
      <!--       <el-table-column width="200px" align="center" label="企业名称" prop="enterpriseId" :formatter="enterpriseFormatter">
</el-table-column> -->
      <el-table-column width="210px" align="center" label="消毒物品" prop="disinfectionObject">
      </el-table-column>
      <el-table-column width="150px" align="center" label="消毒件数" prop="disinfectionAmount">
      </el-table-column>
      <el-table-column width="150px" align="center" label="消毒方式" prop="disinfectionMethod" :formatter="disinfectionMethodFormatter">
      </el-table-column>
      <el-table-column width="150px" align="center" label="消毒剂" prop="disinfectant">
      </el-table-column>
      <el-table-column width="150px" align="center" label="消毒剂用量" prop="disinfectantDosage">
      </el-table-column>
      <el-table-column width="250px" align="center" label="消毒单位" prop="disinfectionUnit" :formatter="disinfectionUnitFoematter">
      </el-table-column>
      <el-table-column width="150px" align="center" label="消毒人" prop="disinfectionPerson">
      </el-table-column>
      <el-table-column width="180px" align="center" label="消毒时间" prop="disinfectionTime">
      </el-table-column>
      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">
      </el-table-column>
      <el-table-column width="180px" align="center" label="更新时间" prop="updateTime">
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="150" v-if="needFixedRight">>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :before-close="beforeClose" :close-on-click-modal="false">
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
                <el-form-item label="消毒物品:" prop="disinfectionObject">
                  <el-input v-model="form.disinfectionObject" :disabled="isEdit" placeholder="请输入消毒物品"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="消毒件数:" prop="disinfectionAmount">
                  <el-input-number v-model="form.disinfectionAmount" :disabled="isEdit" :min="1" placeholder="请输入消毒件数"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="消毒方式:" prop="disinfectionMethod">
                  <el-select v-model="form.disinfectionMethod" :disabled="isEdit" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in disinfectionMethodOptions" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="消毒剂用量:" prop="disinfectantDosage">
                  <el-input v-model="form.disinfectantDosage" :disabled="isEdit" placeholder="请输入消毒剂用量"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="消毒时间:" prop="disinfectionTime">
                  <el-date-picker type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" v-model="form.disinfectionTime" :disabled="isEdit" style="width: 100%;"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="消毒单位:" prop="disinfectionUnit">
                  <el-select v-model="form.disinfectionUnit" :disabled="isEdit" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in disinfectionUnitMap" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="企业:" prop="enterpriseId" :hidden="true">
                  <el-select v-model="form.enterpriseId" :disabled="true" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in enterpriseMap" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="消毒剂:" prop="disinfectant">
                  <el-input v-model="form.disinfectant" :disabled="isEdit" placeholder="请输入消毒剂"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="消毒人:" prop="disinfectionPerson">
                  <el-input v-model="form.disinfectionPerson" :disabled="isEdit" placeholder="请输入消毒人"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="创建时间:" prop="createTime" v-if="isCreate">
                  <el-input v-model="form.createTime" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="equModel_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getEnterpriseBaseMap, getSupplierMap } from 'api/admin/safety/record/recyle/index';
import { page, getRecordDisinfection, addRecordDisinfection, updateRecordDisinfection } from 'api/admin/safety/record/disinfection/index';
import { mapGetters } from 'vuex';
import { parseValueToText } from 'utils/index';
import { spacelValidator } from 'utils/validate';
import { loadGridHeight } from "api/screen";
export default {
  name: 'recordWasteSituationModel',
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      height: undefined,
      list: null,
      total: null,
      listLoading: true,
      status: '',
      listQuery: {
        page: 1,
        limit: 20,
        disinfectionTime: [],
        disinfectionTimeBegin: undefined,
        disinfectionTimeEnd: undefined,
        enterpriseId: undefined
      },
      userManager_btn_edit: true,
      tableKey: 0,
      equModelSelection: [],
      enterpriseMap: [],
      disinfectionUnitMap: [],
      dialogFormVisible: false,
      needFixedRight: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        view: '查看'
      },
      form: {
        enterpriseId: undefined,
        disinfectionObject: undefined,
        disinfectionAmount: 1,
        disinfectionMethod: undefined,
        disinfectant: undefined,
        disinfectantDosage: undefined,
        disinfectionUnit: undefined,
        disinfectionPerson: undefined,
        disinfectionTime: undefined
      },
      rules: {
        enterpriseId: [{
          required: true,
          message: '请选择企业',
          trigger: 'blur'
        }],
        disinfectionObject: [{
          required: true,
          message: '请输入消毒物品',
          trigger: 'blur'
        }, {
          max: 32,
          message: '消毒物品最大为32个字符'
        }, {
          validator: spacelValidator,
          trigger: 'blur',
          message: '消毒物品不能为空'
        }],
        disinfectionMethod: [{
          required: true,
          message: '请选择消毒方式',
          trigger: 'blur'
        }],
        disinfectionTime: [{
          required: true,
          message: '请选择消毒时间',
          trigger: 'blur'
        }],
      },
      equModel_update: true,
      selected: '1',
      isEdit: true,
      isCreate: true,
      delFlag: true,
      recoverFlag: false
    }
  },
  created() {
    this.getEnterpriseBaseMap();
    this.getSupplierMap();
    this.getList();
  },
  computed: {
    ...mapGetters(['staticData', 'user']),
    statusOptions() {
      return this.staticData['状态'];
    },
    disinfectionMethodOptions() {
      return this.staticData['消毒方式'];
    }
  },
  mounted() {
    //首次整个视图都渲染完毕后执行
    this.$nextTick(function() {
      let tableDiv = this.$el.querySelector('.el-table__body-wrapper');
      this.needFixedRight = !($(tableDiv).attr("class").indexOf("is-scrolling-none") >= 0);
    });
    //监听屏幕的改变
    window.onresize = () => {
      let tableDiv = this.$el.querySelector('.el-table__body-wrapper');
      this.needFixedRight = !($(tableDiv).attr("class").indexOf("is-scrolling-none") >= 0);
    }
    this.height = loadGridHeight();
  },
  methods: {
    initParam() {
      let _this = this;
      if (_this.user.userType == 4) {
        _this.form.enterpriseId = _this.user.enterpriseId + "";
      }
    },
    handleFilter() {
      this.getList();
    },
    getEnterpriseBaseMap() {
      getEnterpriseBaseMap().then(response => {
        this.enterpriseMap = response['enterprise'];
      });
    },
    getSupplierMap() {
      getSupplierMap(3).then(response => {
        this.disinfectionUnitMap = response['supplier'];
      });
    },
    getList() {
      this.listQuery.disinfectionTimeBegin = this.listQuery.disinfectionTime == null ? '' : this.listQuery.disinfectionTime[0]
      this.listQuery.disinfectionTimeEnd = this.listQuery.disinfectionTime == null ? '' : this.listQuery.disinfectionTime[1]
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      })
    },
    resetQuery() {
      this.listQuery = { disinfectionTime: [], enterpriseId: undefined };
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['状态']);
    },
    disinfectionMethodFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['消毒方式']);
    },
    enterpriseFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.enterpriseMap);
    },
    disinfectionUnitFoematter(row, column, cellValue) {
      return parseValueToText(cellValue, this.disinfectionUnitMap);
    },
    handleView(row) {
      this.resetForm();
      getRecordDisinfection(row.recordId).then(response => {
        this.form = response.data;
        this.equModel_update = false;
        this.dialogFormVisible = true;
        this.dialogStatus = 'view';
        this.isEdit = true;
        this.isCreate = false;

      });
    },
    handleUpdate(row) {
      getRecordDisinfection(row.recordId).then(response => {
        this.form = response.data;
        this.equModel_update = true;
        this.dialogFormVisible = true;
        this.dialogStatus = 'update';
        this.isEdit = false;
        this.isCreate = false;

      });
    },
    handleCreate() {
      this.resetForm();
      this.initParam();
      this.equModel_update = true;
      this.dialogFormVisible = true;
      this.dialogStatus = 'create';
      this.isEdit = false;
      this.isCreate = false;
    },
    handleDelete() {
      if (!this.equModelSelection.length) {
        this.$message({
          showClose: true,
          message: '请先选择一条信息！',
          type: 'error'
        });
        return;
      }
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const idArr = [];
        this.equModelSelection.forEach(equipmentModel => {
          idArr.push(equipmentModel.equModelId);
        });
        delEquipmentModel(idArr.join(',')).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          });
          this.getList();
        }).catch(() => {});
      }).catch(() => {});
    },
    handleRevocer() {
      if (!this.equModelSelection.length) {
        this.$message({
          showClose: true,
          message: '请先选择一条信息！'
        });
        return;
      }
      this.$confirm('此操作将恢复数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const idArr = [];
        this.equModelSelection.forEach(equipmentModel => {
          idArr.push(equipmentModel.equModelId);
        });
        recoverEquipmentModel(idArr.join(',')).then(() => {
          this.$notify({
            title: '成功',
            message: '恢复成功',
            type: 'success',
            duration: 2000
          });
          this.getList();
        }).catch(() => {});
      }).catch(() => {});
    },
    getSelection(selection) {
      this.equModelSelection = selection;
    },
    create(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          addRecordDisinfection(this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            });
          })
        } else {
          return false;
        }
      });
    },
    update(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          updateRecordDisinfection(this.form.recordId, this.form).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
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
    resetForm() {
      this.form = {
        enterpriseId: undefined,
        disinfectionObject: undefined,
        disinfectionAmount: 1,
        disinfectionMethod: undefined,
        disinfectant: undefined,
        disinfectantDosage: undefined,
        disinfectionUnit: undefined,
        disinfectionPerson: undefined,
        disinfectionTime: undefined
      }
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList();
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.getList();
    },
    beforeClose(done) {
      done();
      this.$refs['form'].resetFields();
    }
  }
}

</script>
