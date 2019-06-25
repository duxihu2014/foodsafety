<template>
  <div>
    <div  v-if="dataFlag">
      <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%"  height="75vh">
        <el-table-column width="100px" align="center" label="编号" prop="planId">
        </el-table-column>
        <el-table-column width="100px" align="center" label="回访来源" prop="revisitSource" :formatter="revisitSourceFormatter">
        </el-table-column>
     <!--   <el-table-column width="170px" align="center" label="企业名称" prop="enterpriseName" v-if="this.user.userType!=4">
        </el-table-column>-->
        <el-table-column width="170px" align="center" label="主体分类" prop="subjectClassification" v-if="this.user.userType!=4" :formatter="subjectClassFormatter">
        </el-table-column>
        <el-table-column width="170px" align="center" label="监督员" prop="monitors" :formatter="personnelFormatter">
        </el-table-column>
        <el-table-column width="100px" align="center" label="协管员" prop="assistants" :formatter="personnelFormatter">
        </el-table-column>
        <el-table-column width="200px" align="center" label="检验员" prop="standards" :formatter="personnelFormatter">
        </el-table-column>
        <el-table-column width="150px" align="center" label="回访状态" prop="planStatus" :formatter="planStatusFormatter">
        </el-table-column>
        <el-table-column width="170px" align="center" label="上次检查时间" prop="inspectedTime">
        </el-table-column>
        <el-table-column width="150px" align="center" label="上次检查人员" prop="inspectedUser" :formatter="personnelFormatter">
        </el-table-column>
        <el-table-column width="150px" align="center" label="上次检查机构" prop="inspectedInstitution" :formatter="institutionFormatter">
        </el-table-column>
        <el-table-column width="180px" align="center" label="上次检查问题" prop="mainProblems" show-overflow-tooltip>
        </el-table-column>
        <el-table-column width="170px" align="center" label="回访时间" prop="finishTime">
        </el-table-column>
        <el-table-column align="center" fixed="right" label="操作" width="100" v-if="needFixedRight">>
          <template slot-scope="scope">
            <el-button size="mini"  type="primary"  @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100" v-else>
          <template slot-scope="scope">
            <el-button size="mini" type="primary"  @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-show="!listLoading" class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
      </div>

      <el-dialog  title="回访信息" :visible.sync="detailVisible " fullscreen :close-on-press-escape="false" :closeOnClickModal="false" append-to-body>
        <div slot="title">
          <i class="el-icon-document">回访信息</i>
        </div>
        <inspectionDetail v-if="detailVisible" @closeDialog="closeDialog" :revisit-plan-id="revisitPlanId"></inspectionDetail>
      </el-dialog>

    </div>
    <div  v-if="!dataFlag">
      无回访信息数据
    </div>
  </div>
</template>
<script>
  import { getEnterpriseBaseMap, addRecordWaste, updateRecordWaste } from 'api/admin/safety/record/recyle/index'
  import { getPersonnelMap, getInstitutionMap,page } from "api/admin/safety/revisit/index";
  import { mapGetters } from 'vuex';
  import { parseValueToText } from 'utils/index';
  import inspectionDetail from "views/admin/safety/revisit/components/inspectionDetail";

  export default {
    name: 'recordWasteSituationModel',
    components: {
      inspectionDetail
    },
    props: ["enterpriseId"],
    watch: {
      'enterpriseId': function() {
        this.getList();
      }
    },
    data() {
      return {
        dataFlag:true,
        revisitPlanId:'',
        tabPosition:'0',
        list: null,
        total: null,
        listLoading: true,
        status: '',
        listQuery: {
          page: 1,
          limit: 20,
          revisitSource:undefined,
          deliveryTime: [],
          deliveryTimeBegin: undefined,
          deliveryTimeEnd: undefined,
          enterpriseId: undefined,
          areaId: undefined,
          gridId: undefined
        },
        userManager_btn_edit: true,
        tableKey: 0,
        equModelSelection: [],
        enterpriseMap: [],
        personnelMap: [],
        institutionMap:[],
        dialogFormVisible: false,
        needFixedRight: false,
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
        },
        equModel_update: true,
        selected: '1',
        isEdit: true,
        isCreate: true,
        delFlag: true,
        recoverFlag: false,
        detailVisible:false,
        currentRow:{}
      }
    },
    created() {
      this.getEnterpriseBaseMap();
      this.getPersonnelMap();
      this.getInstitutionMap();
      this.getList();
    },
    computed: {
      ...mapGetters(['staticData','user']),
      statusOptions() {
        return this.staticData['状态'];
      },
      revisitSourceOptions() {
        return this.staticData['回访来源'];
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
    },
    methods: {
      initParam() {
        let _this = this;
        if (_this.user.userType == 4) {
          _this.form.enterpriseId = _this.user.enterpriseId+"";
        }
      },
      handleFilter() {
        this.getList();
      },
      getEnterpriseBaseMap() {
        getEnterpriseBaseMap().then(response => {
          this.enterpriseMap = response['enterprise'];
        });
      },
      getPersonnelMap() {
        getPersonnelMap().then(response => {
          this.personnelMap =response['personnel'];
        });
      },
      getInstitutionMap() {
        getInstitutionMap().then(response => {
          this.institutionMap =response['institution'];
        });
      },
      getList() {
        this.listQuery.enterpriseId = this.enterpriseId;
        // console.log(this.listQuery.enterpriseId)
        page(this.listQuery).then(response => {
          if(response.total>0){
            // console.log(response.rows)
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
            this.dataFlag=true;
          }else{
            this.listLoading = false;
            this.dataFlag=false;
          }
        })
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit, deliveryTime: [], enterpriseId: undefined,  areaId: undefined, gridId: undefined,revisitSource:undefined};
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['状态']);
      },
      planStatusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['工单状态']);
      },
      revisitSourceFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['回访来源']);
      },
      subjectClassFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['企业主体分类']);
      },
      personnelFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.personnelMap);
      },
      enterpriseFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.enterpriseMap);
      },
      institutionFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.institutionMap);
      },
      handleView(row) {
        this.revisitPlanId=row.planId;
        if(row.revisitSource=='1'){//日常检查
          this.detailVisible=true;
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
      },
      closeDialog(){

      }
    }
  }

</script>
