<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="设备名称" prop="equModel" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="设备型号" v-model="listQuery.name"> </el-input>
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
      <el-table-column align="center" label="编号" width="100" prop="id">
      </el-table-column>
      <el-table-column width="200px" align="center" label="推流唯一标识" prop="videoInfoId">
      </el-table-column>
      <el-table-column width="200px" align="center" label="设备ID" prop="deviceId">
      </el-table-column>
      <el-table-column width="210px" align="center" label="设备名称" prop="name">
      </el-table-column>
      <el-table-column width="210px" align="center" label="投放地址" prop="deliveryAddress">
      </el-table-column>
      <!-- <el-table-column width="110px" align="center" label="状态" :formatter="statusFormatter" prop="status"> -->
      <!-- </el-table-column> -->
      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">
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
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-tabs>
          <el-tab-pane label="基本信息">
            <el-form-item label="推流唯一标识:" prop="videoInfoId">
              <el-input v-model="form.videoInfoId" :disabled="isEdit||isDisable" placeholder="请输入推流唯一标识"></el-input>
            </el-form-item>
            <el-form-item label="设备ID:" prop="deviceId">
              <el-input v-model="form.deviceId" :disabled="isEdit" placeholder="请输入设备ID"></el-input>
            </el-form-item>
            <el-form-item label="设备名称:" prop="name">
              <el-input v-model="form.name" :disabled="isEdit" placeholder="请输入设备名称"></el-input>
            </el-form-item>
            <el-form-item label="投放地址:" prop="deliveryAddress">
              <el-input v-model="form.deliveryAddress" :disabled="isEdit" placeholder="请输入投放地址"></el-input>
            </el-form-item>
            <el-form-item label="创建时间:" prop="createTime" v-if="isCreate">
              <el-input v-model="form.createTime" disabled></el-input>
            </el-form-item>
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
import {
  page,
  beforeGetEquipmentModel,
  getEquipmentModel,
  putEquipmentModel,
  addEquipmentModel,
  delEquipmentModel,
  recoverEquipmentModel,
  getVenderMap,
  isExistEquModel
} from 'api/admin/pushingDevice/index';
import { mapGetters } from 'vuex';
import { parseValueToText } from 'utils/index';
import { loadGridHeight } from 'api/screen';
export default {
  name: 'pushingDevice',
  data() {
    // const checkEquModel = (rule, value, callback) => {
    //   isExistEquModel(value + ',' + this.form.equModelId).then(response => {
    //     if (response.data) {
    //       return callback(new Error('设备型号已经存在'));
    //     } else {
    //       callback();
    //     }
    //   });
    // };
    return {
      height:undefined,
      list: null,
      total: null,
      listLoading: true,
      status: '',
      listQuery: {
        page: 1,
        limit: 20,
        name: undefined,
        status: ''
      },
      userManager_btn_edit: true,
      tableKey: 0,
      equModelSelection: [],
      venderMap: [],
      dialogFormVisible: false,
      needFixedRight: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        view: '查看'
      },
      form: {
        deviceId: undefined,
        name: undefined,
        deliveryAddress: undefined,
        videoInfoId:undefined  
      },
      rules: {
        videoInfoId: [{
          max: 32,
          message: '长度不能超过32个字符',
          trigger: 'blur'
        }],
        deliveryAddress: [{
          max: 32,
          message: '长度不能超过32个字符',
          trigger: 'blur'
        }],
        name: [{
          max: 32,
          message: '长度不能超过32个字符',
          trigger: 'blur'
        }],
        deviceId: [{
          max: 32,
          message: '长度不能超过32个字符',
          trigger: 'blur'
        }],
      },
      equModel_update: true,
      selected: '1',
      isEdit: true,
      isCreate: true,
      delFlag: true,
      recoverFlag: false,
      isDisable:false
    }
  },
  created() {
    // this.getVenderMap();
    this.getList();
  },
  computed: {
    ...mapGetters(['staticData']),
    statusOptions() {
      return this.staticData['状态'];
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
    handleFilter() {
      this.getList();
    },
    getVenderMap() {
      getVenderMap().then(response => {
        this.venderMap = response['厂商'];
      });
    },
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
      })
    },
    resetQuery() {
      this.listQuery = { name: '' };
      this.status = '';
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['状态']);
    },
    verderFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.venderMap);
    },
    handleView(row) {
      this.resetForm();
      getEquipmentModel(row.id).then(response => {
        this.form = response.data;
        this.equModel_update = false;
        this.dialogFormVisible = true;
        this.dialogStatus = 'view';
        this.isEdit = true;
        this.isCreate = true;
        this.selected = String(this.form.id);
      });
    },
    handleUpdate(row) {
        getEquipmentModel(row.id).then(response => {
            this.form = response.data;
            this.equModel_update = true;
            this.dialogFormVisible = true;
            this.dialogStatus = 'update';
            this.isEdit = false;
            this.isCreate = true;
            this.selected = String(this.form.id);
            this.isDisable=true;
        });
    },
    handleCreate() {
      this.resetForm();
      this.equModel_update = true;
      this.dialogFormVisible = true;
      this.dialogStatus = 'create';
      this.isEdit = false;
      this.isCreate = false;
      this.isDisable=true;
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
          idArr.push(equipmentModel.id);
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
          idArr.push(equipmentModel.id);
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
      let option={
          id:'',
          deviceId:this.form.deviceId,
          name:this.form.name
      }
      beforeGetEquipmentModel(option).then(response=>{
        const set = this.$refs;
        this.form.videoInfoId = response.data.result.id;
        set[formName].validate(valid => {
          if (valid&&response.data.error.code===Number(1000)) {
            this.form.id = this.selected;
            addEquipmentModel(this.form).then(() => {
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
      });
    },
    update(formName) {
      let option={
          id:this.form.videoInfoId,
          deviceId:this.form.deviceId,
          name:this.form.name
      }
      beforeGetEquipmentModel(option).then(response=>{
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid&&response.data.error.code===Number(1000)) {  //如果数据不为空且第一次请求成功
            this.form.id = this.selected;
            putEquipmentModel(this.form.id, this.form).then(() => {
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
      });

    },
    cancel(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    resetForm() {
      this.form = {
        equModel: undefined,
        otherParam: undefined,
        status: '1'
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
