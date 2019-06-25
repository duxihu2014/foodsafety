<template>
  <div>
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.projectName_like"> </el-input>
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
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
              @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height*1.5">
      <!--<el-table-column  type="selection"  width="55">
      </el-table-column>-->
      <el-table-column align="center" label="编号" width="100" prop="projectId">

      </el-table-column>
      <el-table-column width="250px" align="center" label="名称" prop="projectName">

      </el-table-column>
      <el-table-column width="150px" align="center" label="类型" prop="projectCategory" :formatter="categoryFormatter">

      </el-table-column>
      <el-table-column width="150px" align="center" label="重要程度" prop="importanceDegree" :formatter="importanceDegreeFormatter">

      </el-table-column>
      <!--<el-table-column width="300px" align="center" label="检查内容" prop="projectContent"></el-table-column>
      <el-table-column width="300px" align="center" label="检查方法" prop="inspectionMethod"></el-table-column>
      <el-table-column width="200px" align="center" label="判定标准" prop="criterion"></el-table-column>-->

      <el-table-column width="150px" align="center" label="检查结果" prop="resultsQualified">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p v-for="(item, index) in scope.row.resultsQualified.split(' ')">{{index+1}}：{{item}}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.resultsQualified.split(' ').length }}个检查结果</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column width="150px" align="center" label="不合格选项" prop="resultsUnqualified">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p v-for="(item, index) in scope.row.resultsUnqualified.split(' ')">{{index+1}}：{{item}}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.resultsUnqualified.split(' ').length }}个不合格选项</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button size="mini" type="warning" @click="handleBanding(scope.row)">绑定
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button size="mini" type="warning" @click="handleBanding(scope.row)">绑定
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <!--<div style="margin-top: 15px;float:right;clear: both;margin-right: 70px;" >
      <el-button  type="primary" @click="">确 定</el-button>
      <el-button  type="danger" @click="">取 消</el-button>
    </div>-->
    <el-dialog  title="查看" append-to-body :visible.sync="dialogFormVisible"  @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <i class="el-icon-view">查看</i>
      </div>

      <el-form :model="form" ref="form" label-width="100px">
        <el-tabs>
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="名称" prop="projectName">
                  <el-input v-model.trim="form.projectName" placeholder="" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="类型" prop="projectCategory">
                  <el-select  v-model="form.projectCategory" filterable placeholder="请选择" clearable filterabler :disabled="readonly">
                    <el-option v-for="(item, index) in allCategory" :key="item.index" :label="item.categoryName" :value="item.categoryId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="重要程度" prop="importanceDegree">
                  <el-select  v-model="form.importanceDegree" filterable placeholder="请选择" clearable filterabler :disabled="readonly">
                    <el-option v-for="(item, index) in importanceDegreeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查内容" prop="projectContent">
                  <el-input type="textarea"  :rows="3"  v-model.trim="form.projectContent" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查方法" prop="inspectionMethod">
                  <el-input type="textarea"  :rows="3"  v-model.trim="form.inspectionMethod" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="判定标准" prop="criterion">
                  <el-input v-model.trim="form.criterion" placeholder="" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="检查结果" prop="resultsQualified">
                  <el-input v-model.trim="form.resultsQualified" placeholder="多个选项用空格隔开" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="不合格选项" prop="resultsUnqualified">
                  <el-input v-model.trim="form.resultsUnqualified" placeholder="多个选项用空格隔开" :readonly="readonly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
  import { getObj, getUnbandingProject } from "api/admin/safety/inspection/template/project";
  import { bandingProject } from "api/admin/safety/inspection/template/index";
  import { getAllCategory } from "api/admin/safety/inspection/template/category";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  export default {
    props: {
      height: {
        default: undefined
      },
      currentId: {
        default: undefined
      }
    },
    watch: {
      'currentId': function() {
        this.listQuery.templateId = this.currentId;
        this.getList();
      }
    },
    data() {
      return {
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
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          templateId: -1,
          projectStatus: 1,
          projectName_like: undefined,
          projectCategory: undefined,
          importanceDegree: undefined
        },
        dialogFormVisible: false,
        readonly: true,
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
    },
    created() {
      this.listQuery.templateId = this.currentId;
      this.getCategory();
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"]),
      importanceDegreeOptions() {
        return this.staticData["重要程度"];
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
        getUnbandingProject(this.listQuery).then(response => {
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
      handleView(row) {
        getObj(row.projectId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
        });
      },
      handleBanding(row){
        bandingProject({templateId:this.currentId, projectId:row.projectId}).then(() => {
          this.getList();
          this.$message({
            showClose: true,
            message: "绑定成功",
            type: "success",
            duration: 2000
          });
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
        this.$refs['form'].resetFields();
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
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit, templateId: this.currentId,
          projectStatus: 1,
          projectName_like: undefined,
          projectCategory: undefined,
          importanceDegree: undefined };
      },
      getSelection(selection) {
        this.projectSelection = selection;
      }
    }
  };
</script>
