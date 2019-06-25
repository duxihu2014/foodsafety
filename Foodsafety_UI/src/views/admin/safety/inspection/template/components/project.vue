<template>
  <div>
    <div class="head-box">
      <div class="item-box">
        <span class="mg-r-15 head-item" @click.stop.prevent="handleBanding"><i class="fa fa-unlink menu-icon vam" aria-hidden="true"></i>绑定项目</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="handleUnBanding"><i class="fa fa-link menu-icon vam" aria-hidden="true"></i>解绑项目</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
              @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="编号" width="100" prop="projectId">

      </el-table-column>
      <el-table-column align="center" label="分值" width="150" prop="score">
        <template slot-scope="scope">
          <el-input-number size="mini" :min="0" :max="99" v-model.trim="score[scope.$index]" v-if="showEdit[scope.$index]"></el-input-number>
          <span v-if="!showEdit[scope.$index]">{{scope.row.score}}</span>
        </template>
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

      <el-table-column align="center" label="操作" width="250" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" v-show="!showBtn[scope.$index]" @click="handleView(scope.row)">查看
          </el-button>
          <el-button size="mini" type="warning" v-show="!showBtn[scope.$index]" @click="handleScore(scope)">分值
          </el-button>
          <el-button  size="mini" type="danger" v-show="!showBtn[scope.$index]" @click="handleDelete(scope.row)">解绑
          </el-button>
          <el-button size="mini" type="primary" v-show="showBtn[scope.$index]" @click="handleScoreCommit(scope)">确定
          </el-button>
          <el-button  size="mini" v-show="showBtn[scope.$index]" @click="handleScoreCancel(scope)">取消
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" v-show="!showBtn[scope.$index]" @click="handleView(scope.row)">查看
          </el-button>
          <el-button size="mini" type="warning" v-show="!showBtn[scope.$index]" @click="handleScore(scope)">分值
          </el-button>
          <el-button  size="mini" type="danger" v-show="!showBtn[scope.$index]" @click="handleDelete(scope.row)">解绑
          </el-button>
          <el-button size="mini" type="primary" v-show="showBtn[scope.$index]" @click="handleScoreCommit(scope)">确定
          </el-button>
          <el-button  size="mini" v-show="showBtn[scope.$index]" @click="handleScoreCancel(scope)">取消
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog  title="查看" :visible.sync="dialogFormVisible"  @close="closeDialog" :close-on-click-modal="false">
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

    <el-dialog  title="绑定项目" fullscreen :visible.sync="bandingDialogVisible" @close="closeBandingDialog" :close-on-click-modal="false">
      <div slot="title">
        <i class="el-icon-view">绑定项目</i>
      </div>
      <template-banding :height="height" :currentId="currentId"></template-banding>
    </el-dialog>

  </div>
</template>

<script>
  import { getObj, delObj, getTemplateProject } from "api/admin/safety/inspection/template/project";
  import { unbandingProject, batchUnbandingProject, setProjectScore } from "api/admin/safety/inspection/template/index";
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
    components: {
      "template-banding": () => import("./banding"),
    },
    data() {
      return {
        allCategory: [],
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
          templateId: -1
        },
        dialogFormVisible: false,
        bandingDialogVisible: false,
        readonly: true,
        tableKey: 0,
        projectSelection: [],
        needFixedRight:false,
        showEdit: [], //显示编辑框
        showBtn: [],
        score: []
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
      resetShow(){
        for(var i = 0; i < this.listQuery.limit; i++) {
          this.showEdit[i] = false;
          this.showBtn[i] = false;
        };
      },
      //获取项目分类信息
      getCategory(){
        getAllCategory(/*{projectStatus:'1'}*/).then((response)=>{
          this.allCategory = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '项目分类信息获取失败。'
          });
        });
      },
      getList() {
        if(this.currentId) {
          this.listLoading = true;
          getTemplateProject(this.listQuery).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
            this.resetShow();
          });
        }
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
      handleDelete(row) {
        this.$confirm(`确定要解除该项目绑定吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          unbandingProject(row.id).then((response) => {
            if(response.rel){
              this.$message({
                type:'success',
                showClose: true,
                message: `解除绑定成功！`
              });
            }else {
              this.$message({
                type:'success',
                showClose: true,
                message: `解除绑定失败！`
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
        /*unbandingProject({"templateId":this.currentId,"ids":row.projectId}).then(response => {
          if(response.rel){
            this.$message({
              type:'success',
              showClose: true,
              message: `解除绑定成功！`
            });
          }else {
            this.$message({
              type:'success',
              showClose: true,
              message: `解除绑定失败！`
            });
          }
          this.getList();
        });*/
      },
      handleScore(scope) {
        this.$set(this.showEdit,scope.$index,true);
        this.$set(this.showBtn,scope.$index,true);
        this.score[scope.$index]=scope.row.score;
      },
      handleScoreCancel(scope) {
        this.$set(this.showEdit,scope.$index,false);
        this.$set(this.showBtn,scope.$index,false);
      },
      handleScoreCommit(scope) {
        setProjectScore({id:scope.row.id, score:this.score[scope.$index]}).then(() => {
          // this.$set(this.showEdit,scope.$index,false);
          // this.$set(this.showBtn,scope.$index,false);
          this.getList();
          this.$message({
            showClose: true,
            message: "分值设置成功",
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
      closeBandingDialog() {
        this.getList();
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
      getSelection(selection) {
        this.projectSelection = selection;
      },
      handleBanding: function () {
        if(!this.currentId){
          this.$message({
            showClose: true,
            message: '请先选择一个模板！'
          });
          return;
        }
        this.bandingDialogVisible = true;
      },
      handleUnBanding: function () {
        if(!this.projectSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一个项目！'
          });
          return;
        }
        this.$confirm(`确定要解除该项目绑定吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = [];
          this.projectSelection.forEach((item)=>{
            ids.push(item.projectId);
          });
          ids= ids.length>1?ids.join(','):ids[0];
          let param = {"templateId":this.currentId, "ids":ids};
          batchUnbandingProject(param).then((response) => {
            if(response.rel){
              this.$message({
                type:'success',
                showClose: true,
                message: `解除绑定成功！`
              });
            }else {
              this.$message({
                type:'success',
                showClose: true,
                message: `解除绑定失败！`
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
