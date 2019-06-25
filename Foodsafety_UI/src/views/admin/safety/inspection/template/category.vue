<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="名称"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" :clearable="true" v-model.trim="listQuery.categoryName_like"> </el-input>
          </el-form-item>
          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.categoryStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <div class="head-box">
      <div class="item-box">
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
              @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="编号" width="100" prop="categoryId">

      </el-table-column>
      <el-table-column width="200px" align="center" label="名称" prop="categoryName">

      </el-table-column>

      <el-table-column width="300px" align="center" label="描述" prop="categoryDesc"></el-table-column>
      <el-table-column width="110px" align="center" label="状态" prop="categoryStatus" :formatter="statusFormatter"></el-table-column>

      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">

      </el-table-column>
      <el-table-column width="180px" align="center" label="更新时间" prop="updateTime">
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <!--<el-button size="mini" @click="handleView(scope.row)">查看
         </el-button>-->
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <!-- <el-button size="mini" @click="handleView(scope.row)">查看
           </el-button>-->
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
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
              <el-col :span="24">
                <el-form-item label="名称" prop="categoryName">
                  <el-input v-model.trim="form.categoryName" placeholder="" :clearable="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="描述" placeholder="" prop="categoryDesc">
                  <el-input  v-model.trim="form.categoryDesc" :clearable="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="categoryManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { page, addObj, getObj, putObj, changeStatus, checkCategoryName } from "api/admin/safety/inspection/template/category";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";
import { loadGridHeight } from "api/screen";
export default {
  name: "category",
  data() {
  const categoryNameValidator = (rule, value, callback) => {
    //debugger
    if(this.categoryNameSelected!==value){
      checkCategoryName({"categoryName":value}).then(response => {
        if(!response.rel){
          return callback(new Error("类型名称重复"));
        }else{
          callback();
        }
      });
    }else{
      callback();
    }
  };
  return {
    height: undefined,
    categoryNameSelected:'',
    form: {
      categoryId: undefined,
      categoryName: undefined,
      categoryDesc: undefined
    },
    rules: {
      categoryName: [{required: true, message: "请输入类型名称", trigger: "blur"},
        {max: 16, message: "长度不能超过16个字符", trigger: "blur"},
        { validator: categoryNameValidator, trigger: "blur"}
      ],
      categoryDesc: [{max: 32, message: "长度不能超过32个字符", trigger: "blur"}]
    },
    list: null,
    total: null,
    listLoading: true,
    listQuery: {
      page: 1,
      limit: 20,
      categoryName_like: undefined,
      categoryStatus:undefined
    },
    dialogFormVisible: false,
    dialogStatus: "",
    textMap: {
      update: "编辑",
      create: "创建",
      view: "查看"
    },
    categoryManager_update: true,
    tableKey: 0,
    categorySelection: [],
    needFixedRight:false
  };
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
  };
  this.height = loadGridHeight();
},
  created() {
    this.getList();
  },
  computed: {
...mapGetters(["staticData"]),
    statusOptions() {
    return this.staticData["状态"];
  }
},
  methods: {
  tableRowClassName({row, rowIndex}) {
    if (row.categoryStatus === '0') {
      return 'warning-row';
    } else if (row.categoryStatus === '1') {
      return 'success-row';
    }
    return '';
  },
  getList() {
    this.listLoading = true;
    page(this.listQuery).then(response => {
      this.list = response.rows;
    this.total = response.total;
    this.listLoading = false;
  });
  },
  handleFilter() {
    this.listQuery.page=1;
    this.getList();
  },
  handleSizeChange(val) {
    this.listQuery.limit = val;
    this.getList();
  },
  handleCurrentChange(val) {
    this.listQuery.page = val;
    this.getList();
  },
  handleCreate() {
    this.dialogStatus = "create";
    this.dialogFormVisible = true;
    this.categoryManager_update = true;
    //   this.resetTmp();
  },
  handleUpdate(row) {
    getObj(row.categoryId).then(response => {
      this.form = response.data;
    this.dialogFormVisible = true;
    this.categoryNameSelected=response.data.categoryName;
    this.dialogStatus = "update";
    this.categoryManager_update = true;
  });
  },
  handleView(row) {
    getObj(row.categoryId).then(response => {
      this.form = response.data;
    this.dialogFormVisible = true;
    this.dialogStatus = "view";
    this.categoryManager_update = false;
  });
  },
  closeDialog(){
    this.form={
      categoryId: undefined,
      categoryName: undefined,
      categoryDesc: undefined};
    this.categoryManager_update = true;
    this.$refs['form'].resetFields();
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
  cancel(formName) {
    this.dialogFormVisible = false;
    this.$refs[formName].resetFields();
  },
  update(formName) {
    const set = this.$refs;
    set[formName].validate(valid => {
      if (valid) {
        putObj(this.form.categoryId, this.form).then(() => {
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
  resetTmp() {
    if (this.$refs["form"]) {
      this.$refs["form"].resetFields();
    }
  },
  statusFormatter(row, column, cellValue) {
    return parseValueToText(cellValue, this.staticData["状态"]);
  },
  resetQuery() {
    let page = this.listQuery.page;
    let limit = this.listQuery.limit;
    this.listQuery = {page: page, limit: limit, categoryName: undefined, categoryStatus:undefined };
  },
  getSelection(selection) {
    this.categorySelection = selection;
  },
  tableStop: function () {  // 0停用
    this.stopOrStart(0);
  },
  tableStart: function () {  // 1启用
    this.stopOrStart(1);
  },
  stopOrStart: function (num) { // 1启用，0停用
    if(!this.categorySelection.length){
      this.$message({
        showClose: true,
        message: '请先选择一条信息！'
      });
      return;
    }
    let flag=true;
    if(num==1){//启用
      this.categorySelection.forEach(category => {
        if(category.categoryStatus==1){
          flag=false;
        }
      });
      if(!flag){
        this.$message({
          showClose: true,
          type:'warning',
          message: "您选择的数据中有启用的数据，不能重复启用！"
        });
        return;
      }
    }else{//停用
      this.categorySelection.forEach(category => {
        if(category.categoryStatus==0){
          flag=false;
        }
      });
      if(!flag){
        this.$message({
          showClose: true,
          type:'warning',
          message: "您选择的数据中有停用的数据，不能重复停用！"
        });
        return;
      }
    }
    let operation = num?'启用':'停用';
    this.$confirm(`确定要${operation}该数据吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      let ids = [];
    this.categorySelection.forEach((item)=>{
      ids.push(item.categoryId);
    });
    ids= ids.length>1?ids.join(','):ids[0];
    let param = {"ids":ids, "status":num};
    changeStatus(param).then((response) => {
      if(response.rel){
      this.$message({
        type:'success',
        showClose: true,
        message: `${operation}成功！`
      });
    }else {
      this.$message({
        type:'success',
        showClose: true,
        message: `${operation}失败！`
      });
    }
    this.getList();
  }).catch(() => {
      this.$message({
      type: 'error',
      showClose: true,
      message: '网络错误，数据请求失败。'
    });
  });
  }).catch(() => {});
  }
}
};
</script>
