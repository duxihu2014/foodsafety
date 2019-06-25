<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  :height="height">
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="企业名称" width="250" prop="companyName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象社会信用代码" width="180" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象主体分类" width="180" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="供应对象联系人" width="120" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象注册地址" width="300" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="供应对象生产地址" width="300" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
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
  import {getPage} from 'api/admin/delivery/index';
    export default {
        name: "query",
      components:{
        "supplyObject-detail": () => import("./components/detail"),
        "queryConditions":()=> import ("components/QueryConditions/index"),
      },
      data(){
        return{
          supplyObjectId:undefined,
          height:undefined,
          list: null,
          total: null,
          listLoading: false,
          listQuery: {
            page: 1,
            limit: 20,
            areaId: undefined,
            gridId: undefined,
            enterpriseId: undefined,
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
      },
      updated () {
        this.height = loadGridHeight();
      },
      created(){
        this.getList();
      },
      methods:{
        setAreaId(data){
          this.listQuery.areaId = data;
        },
        setGridId(data){
          this.listQuery.gridId = data;
        },
        setEnterpriseId(data){
          this.listQuery.enterpriseId = data;
        },
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
            areaId: undefined,
            gridId: undefined,
            enterpriseId: undefined,
          };
        },
        handleView(row){//查看
          this.supplyObjectId = row.supplyObjectId;
          this.dialogStatus = "view";
          this.dialogFormVisible = true;
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

<style scoped>

</style>
