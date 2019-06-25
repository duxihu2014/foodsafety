<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="合同编号" prop="certificateType" class="filter-item">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="产品分类" prop="certificateStatus" class="filter-item">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="产品名称" prop="certificateType" class="filter-item">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="被委托企业名称" prop="certificateType" class="filter-item">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="产品规格" prop="certificateType" class="filter-item">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="受理日期" prop="certificateType" class="filter-item">
            <el-input></el-input>
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
      </div>
    </div>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="委托合同编号" width="150" prop="contractCode"></el-table-column>
      <el-table-column align="center" label="被委托企业名称" width="150" prop="certificateName"></el-table-column>
      <el-table-column align="center" label="合同签订日期" width="150" prop="contractDate" :formatter="timeFormatter"></el-table-column>
      <el-table-column align="center" label="产品分类" width="150" prop="certificateName"></el-table-column>
      <el-table-column align="center" label="产品名称" width="150" prop="certificateName"></el-table-column>
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
        <commission-detail v-if="dialogFormVisible"></commission-detail>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { mapGetters } from "vuex";
  import {page,getCommissionEnterprise} from 'api/admin/commission/index';
  import { loadGridHeight } from "api/screen";
  export default {
    name: "index",
    components:{
      "commission-detail": () => import("./components/detail"),
    },
    data(){
      return{
        height:undefined,
        list: null,
        total: null,
        listLoading: false,
        listQuery: {
          page: 1,
          limit: 20,
          clientEnterpriseId: undefined,
        },
        textMap: {
          update: '修改',
          create: '新增',
          view: '查看'
        },
        dialogStatus: '',
        dialogFormVisible: false,
        certificate_update: false,
        isView:false,//是否是查看状态
        isUpdate:false,//是否是编辑状态，编辑状态的证照类型不可以修改
        needFixedRight:false,
      }
    },
    computed: {
      ...mapGetters(["user","staticData","areaData"]),
    },
    mounted(){
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
    created(){
      this.getList();
    },
    methods:{
      getList() {
        this.listLoading = true;
        if(this.user.enterpriseId){
          getCommissionEnterprise({"enterpriseId":this.user.enterpriseId}).then(response=>{
            if(response.data.length>0){
              this.listQuery.clientEnterpriseId=response.data[0].clientEnterpriseId;
              page(this.listQuery).then(response => {
                this.list = response.rows;
                this.total = response.total;
                this.listLoading = false;
              });
            }else{
              this.listLoading = false;
            }

          })
        }else{
          page(this.listQuery).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
          });
        }

      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      handleFilter() {
        this.listQuery.page=1;
        this.getList();
      },
      resetQuery() {
        this.listQuery.certificateType= "";
        this.listQuery.certificateStatus='1';
      },
      handleFilter(){},
      resetQuery(){},
      handleCreate() {//新增
        this.certificate_update = true;
        this.tabPosition='0';
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
      },
      tableRowClassName({row, rowIndex}) {
        if (row.certificateStatus === '0') {
          return 'warning-row';
        } else if (row.certificateStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      timeFormatter(row, column, cellValue){
        return parseTime(cellValue,'{y}-{m}-{d}');
      },
    },
  }
</script>

<style scoped>

</style>
