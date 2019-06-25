<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="厂商名称" prop="venderName" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="厂商名称" v-model="listQuery.venderName"> </el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status" class="filter-item">
            <el-select v-model="status" placeholder="请选择状态" clearable>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <span class="mg-r-15 head-item"  v-if="delFlag" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
        <span class="mg-r-15 head-item" v-if="recoverFlag" @click.stop.prevent="handleRevocer"><i class="fa el-icon-circle-check  menu-icon vam" aria-hidden="true"></i>恢复</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :height="height">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column align="center" label="编号" width="100" prop="venderId">
      </el-table-column>
      <el-table-column width="150px" align="center" label="厂商编码" prop="venderNo">
      </el-table-column>
      <el-table-column width="200px" align="center" label="厂商名称" prop="venderName">
      </el-table-column>
      <el-table-column width="150px" align="center" label="联系人" prop="venderLinkman">
      </el-table-column>
      <el-table-column width="100px" align="center" label="状态" :formatter="statusFormatter" prop="status">
      </el-table-column>
      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="150" v-if="needFixedRight">
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
            <el-form-item label="厂商编码:" prop="venderNo">
              <el-input v-model.number="form.venderNo" :disabled="isEdit" placeholder="请输入厂商编码" type="age"></el-input>
            </el-form-item>
            <el-form-item label="厂商名称:" prop="venderName">
              <el-input v-model="form.venderName" :disabled="isEdit" placeholder="请输入厂商名称"></el-input>
            </el-form-item>
            <el-form-item label="联系人:" prop="venderLinkman">
              <el-input v-model="form.venderLinkman" :disabled="isEdit" placeholder="请输入联系人"></el-input>
            </el-form-item>
            <el-form-item label="创建时间:" prop="createTime" v-if="isCreate">
              <el-input v-model="form.createTime" disabled></el-input>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="vender_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script >
  import { page, getVender, putVender, addVender, delVender, recoverVender, isExistVenderNo } from 'api/admin/vender/index';
  import { mapGetters } from 'vuex';
  import { parseValueToText } from 'utils/index';
  import { loadGridHeight } from "api/screen";
  export default{
    name: 'vender',
    data() {
      const checkVenderNo = (rule, value, callback) => {
        isExistVenderNo(value + ',' + this.form.venderId).then(response => {
          if (response.data) {
            return callback(new Error('厂商编码已经存在'));
          } else {
            callback();
          }
        });
      };
      const	checkMaxLength = (rule, value, callback) => {
        if (String(value).length > rule.maxlength) {
          return callback(new Error('最大不超过' + rule.maxlength + '字符'));
        } else {
          callback();
        }
      };
      const mobileValidator = (rule, value, callback) => {
        const mymobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        if (value && !mymobile.test(value)) {
          return callback(new Error('手机号格式不正确'));
        } else {
          callback();
        }
      };
      return {
        height:undefined,
        status: '',
        listQuery: {
          page: 1,
          limit: 20,
          venderName: undefined,
          status: ''
        },
        listLoading: true,
        tableKey: 0,
        list: null,
        total: null,
        userManager_btn_edit: true,
        delFlag: true,
        recoverFlag: false,
        textMap: {
          update: '编辑',
          create: '创建',
          view: '查看'
        },
        dialogStatus: '',
        dialogFormVisible: false,
        vender_update: false,
        isEdit: true,
        isCreate: true,
        venderSelection: [],
        needFixedRight:false,
        form: {
          venderNo: undefined,
          venderName: undefined,
          venderLinkman: undefined
        },
        rules: {
          venderNo: [
            {
              type: 'number',
              message: '厂商编码必须为数字值'
            },
            {
              validator: checkMaxLength,
              maxlength: 11
            },
            {
              required: true,
              message: '厂商编码为必填项'
            },
            {
              validator: checkVenderNo,
              trigger: 'blur'
            }
          ],
          venderName: [
            {
              max: 32,
              message: '厂商名称长度最大为32个字符'
            }
          ],
          venderLinkman: [
            {
              max: 32,
              message: '联系人长度最大为32个字符'
            }
          ]
        }
      }
    },
    created() {
      this.getList();
    },
    mounted () {
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function () {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      });
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
      }
      this.height=loadGridHeight();
    },
    computed: {
      ...mapGetters(['staticData']),
      statusOptions() {
        return this.staticData['状态'];
      }
    },
    methods: {
      getList() {
        if (this.status === '') {
          this.listQuery.status = 1;
          this.delFlag = true;
          this.recoverFlag = false;
        } else {
          this.listQuery.status = this.status;
          if (this.status === '0') {
            this.delFlag = false;
            this.recoverFlag = true;
          } else {
            this.delFlag = true;
            this.recoverFlag = false;
          }
        }
        page(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      handleFilter() {
        this.getList();
      },
      resetQuery() {
        this.listQuery = { venderName: '' };
        this.status = '';
      },
      getSelection(selection) {
        this.venderSelection = selection;
      },
      handleView(row) {
        this.resetForm();
        getVender(row.venderId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.isEdit = true;
          this.isCreate = true;
          this.vender_update = false;
          this.dialogStatus = 'view';
        });
      },
      handleUpdate(row) {
        getVender(row.venderId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.isEdit = false;
          this.isCreate = false;
          this.vender_update = true;
          this.dialogStatus = 'update';
        });
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['状态']);
      },
      handleCreate() {
        this.resetForm();
        this.dialogFormVisible = true;
        this.dialogStatus = 'create';
        this.vender_update = true;
        this.isEdit = false;
        this.isCreate = false;
      },
      handleDelete() {
        if (!this.venderSelection.length) {
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
          this.venderSelection.forEach(vender => {
            idArr.push(vender.venderId);
          });
          delVender(idArr.join(',')).then(() => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            });
            this.getList();
          }).catch(() => { });
        }).catch(() => {});
      },
      handleRevocer() {
        if (!this.venderSelection.length) {
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
          this.venderSelection.forEach(vender => {
            idArr.push(vender.venderId);
          });
          recoverVender(idArr.join(',')).then(() => {
            this.$notify({
              title: '成功',
              message: '恢复成功',
              type: 'success',
              duration: 2000
            });
            this.getList();
          }).catch(() => { });
        }).catch(() => {});
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      create(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            addVender(this.form).then(() => {
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
      cancel(formName) {
        this.dialogFormVisible = false;
        this.$refs[formName].resetFields();
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            putVender(this.form.venderId, this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              });
            })
          } else {
            return false;
          }
        });
      },
      resetForm() {
        this.form = {
          venderNo: undefined,
          venderName: undefined,
          venderLinkman: undefined,
          status: 1
        }
      },
      beforeClose(done){
        done();
        this.$refs['form'].resetFields();
      }
    }
  }
</script>
