<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" :clearable="true" v-model.trim="listQuery.projectName_like"> </el-input>
          </el-form-item>
          <el-form-item label="类型" class="filter-item">
            <el-select  v-model="listQuery.projectCategory" filterable placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in validCategory" :key="item.index" :label="item.categoryName" :value="item.categoryId"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="重要程度" class="filter-item">
            <el-select  v-model="listQuery.importanceDegree" filterable placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in importanceDegreeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态" class="filter-item">
            <el-select  v-model="listQuery.projectStatus" placeholder="请选择" clearable>
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
      <el-table-column align="center" label="编号" width="100" prop="projectId">

      </el-table-column>
      <el-table-column width="200px" align="center" label="名称" prop="projectName">

      </el-table-column>
      <el-table-column width="150px" align="center" label="类型" prop="projectCategory" :formatter="categoryFormatter">

      </el-table-column>
      <el-table-column width="100px" align="center" label="重要程度" prop="importanceDegree" :formatter="importanceDegreeFormatter">

      </el-table-column>
      <!--<el-table-column width="300px" align="center" label="检查内容" prop="projectContent"></el-table-column>
      <el-table-column width="300px" align="center" label="检查方法" prop="inspectionMethod"></el-table-column>
      <el-table-column width="200px" align="center" label="判定标准" prop="criterion"></el-table-column>-->

      <el-table-column width="200px" align="center" label="检查结果" prop="resultsQualified"></el-table-column>
      <el-table-column width="300px" align="center" label="不合格选项" :show-overflow-tooltip="true"  prop="resultsUnqualified"></el-table-column>

      <el-table-column width="110px" align="center" label="状态" prop="projectStatus" :formatter="statusFormatter"></el-table-column>

      <el-table-column align="center" label="操作" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
           <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
           </el-button>
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
              <el-col :span="12">
                <el-form-item label="名称" prop="projectName">
                  <el-input v-model.trim="form.projectName" placeholder="" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="类型" prop="projectCategory">
                  <el-select  v-model="form.projectCategory" filterable placeholder="请选择" clearable filterabler :disabled="!projectManager_update">
                    <el-option v-for="(item, index) in projectManager_update ? validCategory : allCategory" :key="item.index" :label="item.categoryName" :value="item.categoryId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="重要程度" prop="importanceDegree">
                  <el-select  v-model="form.importanceDegree" filterable placeholder="请选择" clearable filterabler :disabled="!projectManager_update">
                    <el-option v-for="(item, index) in importanceDegreeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查内容" prop="projectContent">
                  <el-input type="textarea"  :rows="2"  v-model.trim="form.projectContent" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查方法" prop="inspectionMethod">
                  <el-input type="textarea"  :rows="3"  v-model.trim="form.inspectionMethod" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="判定标准" prop="criterion">
                  <el-input v-model.trim="form.criterion" placeholder="" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查结果" prop="resultsQualified">
                  <el-input v-model.trim="form.resultsQualified"  placeholder="多个选项用空格隔开" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="不合格选项" prop="resultsUnqualified">
                  <el-input v-model.trim="form.resultsUnqualified" placeholder="多个选项用空格隔开" :clearable="true" :readonly="!projectManager_update"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="projectManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { page, addObj, getObj, putObj, changeStatus, checkProjectName } from "api/admin/safety/inspection/template/project";
  import { getAllCategory } from "api/admin/safety/inspection/template/category";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "project",
    data() {
      const projectNameValidator = (rule, value, callback) => {
        //debugger
        if(this.projectNameSelected!==value){
          checkProjectName({"projectName":value}).then(response => {
            if(!response.rel){
              return callback(new Error("项目名称重复"));
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
        projectNameSelected:'',
        allCategory: [],
        validCategory: [],
        form: {
          projectId: undefined,
          projectCategory: undefined,
          projectName: undefined,
          projectContent: undefined,
          importanceDegree: undefined,
          inspectionMethod: undefined,
          criterion: undefined,
          resultsQualified: undefined,
          resultsUnqualified: undefined
        },
        rules: {
          projectCategory: [{required: true, message: "请选择项目类型", trigger: "change"}],
          projectName: [{required: true, message: "请输入项目名称", trigger: "blur"},
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"},
            { validator: projectNameValidator, trigger: "blur"}
          ],
          projectContent: [{required: true, message: "请输入检查内容", trigger: "blur"},
            {max: 64, message: "长度不能超过64个字符", trigger: "blur"}],
          importanceDegree: [{required: true, message: "请选择重要程度", trigger: "change"}],
          inspectionMethod: [{required: true, message: "请输入检查方法", trigger: "blur"},
            {max: 128, message: "长度不能超过128个字符", trigger: "blur"}],
          criterion: [{required: true, message: "请输入判定标准", trigger: "blur"},
            {max: 64, message: "长度不能超过64个字符", trigger: "blur"}],
          resultsQualified: [{required: true, message: "请输入检查结果", trigger: "blur"},
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"}],
          resultsUnqualified: [{required: true, message: "请输入不合格选项", trigger: "blur"},
            {max: 128, message: "长度不能超过128个字符", trigger: "blur"}],
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          projectName_like: undefined,
          projectCategory: undefined,
          importanceDegree: undefined,
          projectStatus: undefined
        },
        dialogFormVisible: false,
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        projectManager_update: true,
        tableKey: 0,
        projectSelection: [],
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
      this.getCategory();
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"]),
      importanceDegreeOptions() {
        return this.staticData["重要程度"];
      },
      statusOptions() {
        return this.staticData["状态"];
      }
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        if (row.projectStatus === '0') {
          return 'warning-row';
        } else if (row.projectStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      //获取项目分类信息
      getCategory(){
        getAllCategory(/*{projectStatus:'1'}*/).then((response)=>{
          this.allCategory = response;
          this.validCategory = this.allCategory.filter((item)=>{
            return item.categoryStatus === '1';
          });
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '项目分类信息获取失败。'
          });
        });
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
        this.projectManager_update = true;
        //   this.resetTmp();
      },
      handleUpdate(row) {
        getObj(row.projectId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.projectNameSelected=response.data.projectName;
          this.dialogStatus = "update";
          this.projectManager_update = true;
        });
      },
      handleView(row) {
        getObj(row.projectId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
          this.projectManager_update = false;
        });
      },
      closeDialog(){
        this.form={
          projectId: undefined,
          projectCategory: undefined,
          projectName: undefined,
          projectContent: undefined,
          importanceDegree: undefined,
          inspectionMethod: undefined,
          criterion: undefined,
          resultsQualified: undefined,
          resultsUnqualified: undefined,
          projectStatus: undefined};
        this.projectManager_update = true;
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
            putObj(this.form.projectId, this.form).then(() => {
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
      categoryFormatter(row, column, cellValue) {
        if(!cellValue){return ''}
        if(!this.allCategory.length){return cellValue}
        return this.allCategory.filter((item)=>{
          return item.categoryId === cellValue;
        })[0].categoryName;
      },
      importanceDegreeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["重要程度"]);
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["状态"]);
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit, projectName: undefined,
          projectCategory: undefined,
          importanceDegree: undefined,
          projectStatus: undefined };
      },
      getSelection(selection) {
        this.projectSelection = selection;
      },
      tableStop: function () {  // 0停用
        this.stopOrStart(0);
      },
      tableStart: function () {  // 1启用
        this.stopOrStart(1);
      },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.projectSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.projectSelection.forEach(project => {
            if(project.projectStatus==1){
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
          this.projectSelection.forEach(project => {
            if(project.projectStatus==0){
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
          this.projectSelection.forEach((item)=>{
            ids.push(item.projectId);
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
