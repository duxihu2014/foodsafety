<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" :clearable="true" v-model.trim="listQuery.templateName_like"> </el-input>
          </el-form-item>
          <el-form-item label="适用范围" class="filter-item">
            <el-select  v-model="listQuery.applicationScope" filterable placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in applicationScopeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.templateStatus" placeholder="请选择" clearable>
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
    <el-tabs v-model="tabPosition">
      <el-tab-pane label="模板信息">
        <div class="head-box">
          <div class="item-box">
            <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
            <!--<span class="mg-r-15 head-item" @click.stop.prevent="handleScore"><i class="fa fa-pencil-square menu-icon vam" aria-hidden="true"></i>记分</span>-->
            <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
            <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
          </div>
        </div>
        <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
                  border fit highlight-current-row style="width: 100%" highlight-current-row
                  @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
          <el-table-column  type="selection"  width="55">
          </el-table-column>
          <el-table-column align="center" label="编号" width="100" prop="templateId">

          </el-table-column>
          <el-table-column width="200px" align="center" label="名称" prop="templateName">

          </el-table-column>

          <el-table-column width="300px" align="center" label="描述" prop="templateDesc"></el-table-column>

          <el-table-column width="100px" align="center" label="适用范围" prop="applicationScope" :formatter="applicationScopeFormatter">

          </el-table-column>

          <el-table-column width="120px" align="center" label="截至日期" prop="applicationDate">

          </el-table-column>

          <el-table-column width="150px" align="center" label="编制机构" prop="workoutInstitution" :formatter="institutionFormatter">

          </el-table-column>

          <el-table-column width="180px" align="center" label="编制时间" prop="workoutTime">

          </el-table-column>

          <el-table-column width="110px" align="center" label="状态" prop="templateStatus" :formatter="statusFormatter"></el-table-column>


          <el-table-column align="center" label="操作" width="150" fixed="right" v-if="needFixedRight">
            <template slot-scope="scope">
              <!--<el-button  size="mini" type="primary" @click="handleView(scope.row)">查看
              </el-button>-->
              <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
              </el-button>
              <el-button size="mini" type="warning" @click="handleProject(scope.row)">项目
              </el-button>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="150" v-else>
            <template slot-scope="scope">
              <!--<el-button  size="mini" type="primary" @click="handleView(scope.row)">查看
              </el-button>-->
              <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
              </el-button>
              <el-button size="mini" type="warning" @click="handleProject(scope.row)">项目
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-show="!listLoading" class="pagination-container">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="项目信息">
        <template-project :height="height" :currentId="currentId"></template-project>
      </el-tab-pane>
    </el-tabs>

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
                <el-form-item label="名称" prop="templateName">
                  <el-input v-model.trim="form.templateName" placeholder="" :clearable="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="适用范围" prop="applicationScope">
                  <el-select  v-model="form.applicationScope" filterable placeholder="请选择" clearable filterabler>
                    <el-option v-for="(item, index) in applicationScopeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="截至日期" prop="applicationDate">
                  <el-date-picker v-model="form.applicationDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="24">
                <el-form-item label="描述" placeholder="" prop="templateDesc">
                  <el-input  v-model.trim="form.templateDesc" :clearable="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="templateManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { page, addObj, getObj, putObj, changeStatus, checkTemplateName } from "api/admin/safety/inspection/template/index";
  import { getInstitutions } from "api/admin/institution/index";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "template",
    components: {
      "template-project": () => import("./components/project"),
    },
    data() {
      const tempateNameValidator = (rule, value, callback) => {
        //debugger
        if(this.templateNameSelected!==value){
          checkTemplateName({"templateName":value}).then(response => {
            if(!response.rel){
              return callback(new Error("模板名称重复"));
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
        templateNameSelected:'',
        allInstitution: [],
        form: {
          templateId: undefined,
          templateName: undefined,
          templateDesc: undefined,
          applicationScope: undefined,
          applicationDate: undefined
        },
        rules: {
          templateName: [{required: true, message: "请输入模板名称", trigger: "blur"},
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"},
           { validator: tempateNameValidator, trigger: "blur"}
          ],
          templateDesc: [{max: 64, message: "长度不能超过64个字符", trigger: "blur"}],
          applicationScope: [{required: true, message: "请选择适用范围", trigger: "change"}],
          applicationDate: [{required: true, message: "请选择截至日期", trigger: "change"}]
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          templateName_like: undefined,
          applicationScope: undefined,
          templateStatus:undefined
        },
        dialogFormVisible: false,
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        templateManager_update: true,
        tableKey: 0,
        templateSelection: [],
        needFixedRight:false,
        currentId: undefined,
        tabPosition: "0",
        pickerOptions1: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        }
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
      this.getInstitution();
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"]),
      applicationScopeOptions() {
        return this.staticData["适用范围"];
      },
      statusOptions() {
        return this.staticData["状态"];
      }
    },
    methods: {
      institutionFormatter(row, column, cellValue) {
        if(!cellValue){return ''}
        if(!this.allInstitution.length){return cellValue}
        return this.allInstitution.filter((item)=>{
          return item.institutionId === cellValue;
        })[0].institutionName;
      },
      tableRowClassName({row, rowIndex}) {
        if (row.templateStatus === '0') {
          return 'warning-row';
        } else if (row.templateStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      //获取项目分类信息
      getInstitution(){
        getInstitutions(/*{institutionStatus:'1'}*/).then((response)=>{
          this.allInstitution = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '机构信息获取失败。'
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
        this.tabPosition = "0";
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
      /*handleScore() {
        if(!this.templateSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        //
      },*/
      handleCreate() {
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.templateManager_update = true;
        //   this.resetTmp();
      },
      handleUpdate(row) {
        getObj(row.templateId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.templateNameSelected=response.data.templateName;
          this.dialogStatus = "update";
          this.templateManager_update = true;
        });
      },
      handleView(row) {
        getObj(row.templateId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
          this.templateManager_update = false;
        });
      },
      handleProject(row) {
        this.currentId = row.templateId;
        this.tabPosition = "1";
      },
      closeDialog(){
        this.form={
          templateId: undefined,
          templateName: undefined,
          templateDesc: undefined,
          applicationScope: undefined,
          applicationDate: undefined};
        this.templateManager_update = true;
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
            putObj(this.form.templateId, this.form).then(() => {
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
      applicationScopeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["适用范围"]);
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["状态"]);
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit, templateName: undefined,
          applicationScope: undefined,
          templateStatus:undefined };
      },
      getSelection(selection) {
        this.templateSelection = selection;
      },
      tableStop: function () {  // 0停用
        this.stopOrStart(0);
      },
      tableStart: function () {  // 1启用
        this.stopOrStart(1);
      },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.templateSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.templateSelection.forEach(template => {
            if(template.templateStatus==1){
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
          this.templateSelection.forEach(template => {
            if(template.templateStatus==0){
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
          this.templateSelection.forEach((item)=>{
            ids.push(item.templateId);
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
