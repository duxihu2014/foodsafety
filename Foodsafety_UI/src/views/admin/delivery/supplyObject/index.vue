<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="企业名称" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" placeholder="" :clearable="true" v-model.trim="listQuery.enterpriseName"> </el-input>
          </el-form-item>
          <el-form-item label="主体分类" class="filter-item">
            <el-select v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>新增</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
      </div>
    </div>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  @selection-change="handleSelectionChange" :height="height">
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="供应对象" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="主体分类" width="180" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="联系人" width="100" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册地址" width="300" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="生产地址" width="300" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="操作" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="Boolean(scope.row.enterpriseId)">修改
          </el-button>
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="Boolean(scope.row.enterpriseId)">修改
          </el-button>
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <!--修改，查看，新增表单对话框-->
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" fullscreen :close-on-click-modal="false">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '新增'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '修改'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>
      <div>
        <supplyObject-detail v-if="dialogFormVisible" @closeDialog="closeDialog" :dialogStatus="dialogStatus" :supplyObjectId="supplyObjectId"></supplyObject-detail>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { loadGridHeight } from "api/screen";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { mapGetters } from "vuex";
  import {getPage,delObjs} from 'api/admin/delivery/index';
  export default {
    name: "index",
    components:{
      "supplyObject-detail": () => import("./components/detail"),
    },
    data(){
      return{
        supplyObjectSelection:[],
        supplyObjectId:undefined,
        height:undefined,
        list: null,
        total: null,
        listLoading: false,
        listQuery: {
          page: 1,
          limit: 20,
          enterpriseName: undefined,
          subjectClassification:undefined,
        },
        textMap: {
          update: '修改',
          create: '新增',
          view: '查看'
        },
        dialogStatus: '',
        dialogFormVisible: false,
        needFixedRight:false,
      }
    },
    computed: {
      ...mapGetters(["staticData", "areaData", "user"]),
      subjectClassificationOptions() {
        return this.staticData["企业主体分类"];
      },
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
    created(){
      this.getList();
    },
    methods:{
      getList() {
        this.listLoading = true;
        getPage(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      handleFilter() {
        this.listQuery.page = 1;
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
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {
          page: page,
          limit: limit,
          enterpriseName: undefined,
          subjectClassification: undefined,
        };
      },
      handleSelectionChange(val) {
        this.supplyObjectSelection = val;
      },
      handleCreate() {//新增
        this.supplyObjectId=undefined;
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
      },
      handleView(row){//查看
        this.supplyObjectId = row.supplyObjectId;
        this.dialogStatus = "view";
        this.dialogFormVisible = true;
      },
      handleUpdate(row){//修改
        // console.log(row.enterpriseId)
        this.supplyObjectId = row.supplyObjectId;
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
      },
      handleDelete() {//删除
        if (!this.supplyObjectSelection.length) {
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
        }).then(() => {
          const idArr = [];
          this.supplyObjectSelection.forEach(supplyObject => {
            idArr.push(supplyObject.enterpriseSupplyObjectId);
          });
          delObjs(idArr.join(",")).then(() => {
            this.$message({
              showClose: true,
              message: "删除成功",
              type: "success",
              duration: 2000
            });
            this.getList();
          })
        })
      },
      closeDialog(){
        this. getList();
        this.dialogFormVisible = false;
      },
      //企业主体分类格式化
      subjectClassificationFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["企业主体分类"]);
      },
    },
  }
</script>
<style>

</style>
