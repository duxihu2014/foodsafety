/**
*
* @authors minwei (minwei@chinaotec.com)
* @date 2018-10-10 15:03:11
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
          <el-form-item label="收运时间:" prop="deliveryTime" class="filter-item">
            <el-date-picker v-model="listQuery.deliveryTime" type="daterange" align="right" unlink-panels range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" :picker-options="pickerOptions">
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
      <el-table-column align="center" label="编号" width="100" prop="situationId">
      </el-table-column>
      <!--       <el-table-column width="200px" align="center" label="企业" prop="enterpriseId" :formatter="enterpriseFormatter">
</el-table-column> -->
      <el-table-column width="210px" align="center" label="废弃物种类" prop="wasteCategory" :formatter="wasteCategoryFormatter">
      </el-table-column>
      <el-table-column width="100px" align="center" label="数量(单位:千克)" prop="wasteAmount">
      </el-table-column>
      <el-table-column width="150px" align="center" label="存放容器" prop="storageContainer">
      </el-table-column>
      <el-table-column width="150px" align="center" label="发放人" prop="disposalPerson">
      </el-table-column>
      <el-table-column width="150px" align="center" label="处理方式" prop="disposalMethod" :formatter="disposalMethodFormatter">
      </el-table-column>
      <el-table-column width="250px" align="center" label="接收单位" prop="receivingUnit" :formatter="receivingUnitFormatter">
      </el-table-column>
      <el-table-column width="150px" align="center" label="收运人" prop="deliveryPerson">
      </el-table-column>
      <el-table-column width="180px" align="center" label="收运时间" prop="deliveryTime">
      </el-table-column>
      <el-table-column width="150px" align="center" label="回收用途" prop="purpose">
      </el-table-column>
      <el-table-column width="200px" align="center" label="创建时间" prop="createTime">
      </el-table-column>
      <el-table-column width="200px" align="center" label="更新时间" prop="updateTime">
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="240" v-if="needFixedRight">>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <a href="javascript:void(0)" @click="playVideo(scope.row)" class="sel_btn" style="margin-right: 10px;background-color: #67c23a;border-color: #67c23a;">播放</a>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="240" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <a href="javascript:void(0)" @click="playVideo(scope.row)" class="sel_btn" style="margin-right: 10px;background-color: #67c23a;border-color: #67c23a;">播放</a>
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
                <el-form-item label="废弃物种类:" prop="wasteCategory">
                  <el-select v-model="form.wasteCategory" :disabled="isEdit" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in wasteCategoryOptions" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="接收单位:" prop="receivingUnit">
                  <el-select v-model="form.receivingUnit" :disabled="isEdit" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in receivingUnitMap" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="数量(千克):" prop="wasteAmount">
                  <el-input-number v-model="form.wasteAmount" :disabled="isEdit" :min="1" :max="999" placeholder="请输入数量"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="存放容器:" prop="storageContainer">
                  <el-input v-model="form.storageContainer" :disabled="isEdit" placeholder="请输入存放容器"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="处理方式:" prop="disposalMethod">
                  <el-select v-model="form.disposalMethod" :disabled="isEdit" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in disposalMethodOptions" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发放人:" prop="disposalPerson">
                  <el-input v-model="form.disposalPerson" :disabled="isEdit" placeholder="请输入发放人"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="收运时间:" prop="deliveryTime">
                  <el-date-picker type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" v-model="form.deliveryTime" :disabled="isEdit" style="width: 100%;"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收运人:" prop="deliveryPerson">
                  <el-input v-model="form.deliveryPerson" :disabled="isEdit" placeholder="请输入收运人"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="回收用途:" prop="purpose">
                  <el-input v-model="form.purpose" :disabled="isEdit" placeholder="请输入回收用途"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="企业:" prop="enterpriseId" :hidden="true">
                  <el-select v-model="form.enterpriseId" :disabled="true" :filterable="true" placeholder="请选择">
                    <el-option v-for="item in enterpriseMap" :key="item.value" :label="item.text" :value="item.value">
                    </el-option>
                  </el-select>
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
            <el-row>
              <el-col :span="24">
                <el-form-item label="现场照片" prop="imageFile"  ref="imageFile" tab="1">

                  <el-upload
                    action="https://jsonplaceholder.typicode.com/posts/"
                    list-type="picture-card"
                    :file-list="listFile"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleImageRemove"
                    :on-change="addImgFile"
                    :limit="1"
                    :auto-upload="false">
                    <i class="el-icon-plus"></i>
                  </el-upload>

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

    <el-dialog title="摄像头信息" :visible.sync="cameraDialogVisible" >
      <div slot="title">
        <i class="el-icon-view"> 摄像头信息</i>
      </div>
      <camera :compId="compId" :listData="equipmentList"></camera>
    </el-dialog>

  </div>
</template>
<script>
import { page, getEnterpriseBaseMap, getRecordWaste, addRecordWaste, updateRecordWaste, getSupplierMap } from 'api/admin/safety/record/recyle/index'
import { mapGetters } from 'vuex';
import { pageEquipment } from "api/admin/equipment/index";
import { parseValueToText,parseForm } from 'utils/index';
import { loadGridHeight } from "api/screen";
import { getResource } from "api/admin/resource/index";
import camera from './components/subcamera.vue';
export default {
  name: 'recordWasteSituationModel',
  components: {
    camera
  },
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
        deliveryTime: [],
        deliveryTimeBegin: undefined,
        deliveryTimeEnd: undefined,
        enterpriseId: undefined
      },
      userManager_btn_edit: true,
      tableKey: 0,
      equModelSelection: [],
      enterpriseMap: [],
      receivingUnitMap: [],
      dialogFormVisible: false,
      needFixedRight: false,
      equipmentList: [],
      compId: undefined,
      cameraDialogVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        view: '查看'
      },
      form: {
        enterpriseId: undefined,
        wasteCategory: undefined,
        wasteAmount: 1,
        storageContainer: undefined,
        disposalPerson: undefined,
        disposalMethod: undefined,
        receivingUnit: undefined,
        deliveryPerson: undefined,
        deliveryTime: undefined,
        purpose: undefined
      },
      rules: {
        enterpriseId: [{
          required: true,
          message: '请选择企业',
          trigger: 'blur'
        }],
        wasteCategory: [{
          required: true,
          message: '请选择废弃物种类',
          trigger: 'blur'
        }],
        wasteAmount: [{
          required: true,
          message: '请输入数量',
          trigger: 'blur'
        }],
        disposalMethod: [{
          required: true,
          message: '请选择处理方式',
          trigger: 'blur'
        }],
        deliveryTime: [{
          required: true,
          message: '请选择收运时间',
          trigger: 'blur'
        }],
      },
      equModel_update: true,
      selected: '1',
      isEdit: true,
      isCreate: true,
      delFlag: true,
      recoverFlag: false,
      listFile:[],
      fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
      serverImageUrl:process.env.SERVERIMAGEURL,
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
    wasteCategoryOptions() {
      return this.staticData['废弃物种类'];
    },
    disposalMethodOptions() {
      return this.staticData['废弃物处理方式'];
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
        //this.receivingUnitMap =response['supplier'];
      });
    },
    getSupplierMap() {
      getSupplierMap(4).then(response => {
        this.receivingUnitMap = response['supplier'];
      });
    },
    getList() {
      this.listQuery.deliveryTimeBegin = this.listQuery.deliveryTime == null ? '' : this.listQuery.deliveryTime[0]
      this.listQuery.deliveryTimeEnd = this.listQuery.deliveryTime == null ? '' : this.listQuery.deliveryTime[1]
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
      })
    },
    playVideo(row) {
      pageEquipment({enterpriseId: row.enterpriseId, specialStatus:'2'}).then(response => {
        this.compId = row.enterpriseId;
      this.equipmentList = response.rows;
      this.cameraDialogVisible = true;
    });
    },
    resetQuery() {
      this.listQuery = { deliveryTime: [], enterpriseId: undefined };
    },
    statusFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['状态']);
    },
    wasteCategoryFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['废弃物种类']);
    },
    disposalMethodFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.staticData['废弃物处理方式']);
    },
    receivingUnitFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.receivingUnitMap);
    },
    enterpriseFormatter(row, column, cellValue) {
      return parseValueToText(cellValue, this.enterpriseMap);
    },
    handleView(row) {
      this.resetForm();
      getRecordWaste(row.situationId).then(response => {
        this.form = response.data;
        this.equModel_update = false;
        this.dialogFormVisible = true;
        this.dialogStatus = 'view';
        this.isEdit = true;
        this.isCreate = false;
        this.listFile=[];
        if(this.form.wastePhoto){
          getResource(this.form.wastePhoto).then(response =>{
            this.listFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
            $(".el-upload--picture-card:eq(0)").hide();
          });
        }
      });
    },
    handleUpdate(row) {
      getRecordWaste(row.situationId).then(response => {
        this.form = response.data;
        this.equModel_update = true;
        this.dialogFormVisible = true;
        this.dialogStatus = 'update';
        this.isEdit = false;
        this.isCreate = false;
        this.listFile=[];
        if(this.form.wastePhoto){
          getResource(this.form.wastePhoto).then(response =>{
            this.listFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
            $(".el-upload--picture-card:eq(0)").hide();
          });
        }

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

          let param = new FormData();
          param.append("rs", parseForm(this.form));

          if(this.listFile.length>0){
              param.append("photo", this.listFile[0].raw);
          }
          addRecordWaste(param).then(() => {
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

          let param = new FormData();
          if(this.listFile.length>0){
            param.append("photo", this.listFile[0].raw);
          }else{
            this.form.wastePhoto=undefined;
          }
          param.append("rs", parseForm(this.form));

          updateRecordWaste(this.form.situationId, param).then(() => {
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
        wasteCategory: undefined,
        wasteAmount: 1,
        storageContainer: undefined,
        disposalPerson: undefined,
        disposalMethod: undefined,
        receivingUnit: undefined,
        deliveryPerson: undefined,
        deliveryTime: undefined,
        purpose: undefined
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
      this.listFile=[];
      $(".el-upload--picture-card:eq(0)").show();
      this.$refs['form'].resetFields();
    },
    //删除图片
    handleImageRemove(file){
      this.listFile=[];
      if( this.listFile.length==0){
        $(".el-upload--picture-card:eq(0)").show();
      }
    },
    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    addImgFile(file) { // 添加图片回调
      if(this.fileFormat.indexOf(file.raw.type) < 0 ){
        this.$message({
          type:'error',
          showClose: true,
          message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
        });
        return false;
      }
      this.listFile.push(file);
      if( this.listFile.length>0){
        $(".el-upload--picture-card:eq(0)").hide();
      }
    }
  }
}

</script>
<style scoped>
  .sel_btn {
    margin-left: 10px;
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
</style>
