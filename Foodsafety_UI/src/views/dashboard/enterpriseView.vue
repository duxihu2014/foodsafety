<template>
  <div>
    <el-row>
      <el-col :span="15">
        <el-table :data="list" v-loading="listLoading" element-loading-text="数据加载中" border height="480">
         <!-- <el-table-column align="center" label="企业状态" width="100" prop="enterpriseStatus" fixed="left">
            <template slot-scope="scope">
              <el-tag :type="getEnterpriseStatus(scope.row.enterpriseStatus)" >
                {{enterpriseStatusFormatter(scope.row.enterpriseStatus)}}
              </el-tag>
            </template>
          </el-table-column>-->
          <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" label="社会信用代码" width="180" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" label="主体分类" width="180" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
          <el-table-column align="center" label="联系人" width="100" prop="contacts" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" label="联系电话" width="150" prop="contactNumber" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" label="注册地址" width="300" prop="registerAddress" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column align="center" label="生产地址" width="300" prop="productionAddress" :show-overflow-tooltip="true"></el-table-column>

          <el-table-column align="center" label="操作" width="100" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="success" @click="handleView(scope.row)">查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <div v-show="!listLoading" class="pagination-container">
          <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="listQuery.currPage"
            :page-size="listQuery.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total"
            style="margin: 10px 0"
          >
          </el-pagination>
        </div>
      </el-col>
      <el-col :span="8" :offset="1">
        <div ref="charts1" style="width: 100%; height: 250px; margin-top: 20px;"></div>
        <div ref="charts2" style="width: 100%; height: 250px; margin-top: 20px;"></div>
      </el-col>
    </el-row>
    <el-dialog  title="档案详情" :visible.sync="dialogVisible" fullscreen append-to-body="" @close="closeDialog"  :closeOnClickModal="false">
      <div slot="title">
        <i class="el-icon-document">档案详情</i>
      </div>
      <el-tabs v-model="tabPosition">
        <el-tab-pane label="基本信息">
          <baseDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></baseDetail>
        </el-tab-pane>
        <el-tab-pane label="企业规模">
          <scaleDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></scaleDetail>
        </el-tab-pane>
        <el-tab-pane label="证照信息">
          <certificateDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></certificateDetail>
        </el-tab-pane>
        <el-tab-pane label="产品信息">
          <productDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></productDetail>
        </el-tab-pane>
        <el-tab-pane label="原料信息">
          <materialDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></materialDetail>
        </el-tab-pane>
        <el-tab-pane label="设备信息">
          <equipmentDetail :enterpriseId="enterpriseId" :enterpriseName="enterpriseName"></equipmentDetail>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>

</template>

<script>
  import {getEnterpriseByGrid,getSupervisionByGrid,getSubjectClassificationByGrid} from 'api/admin/dashboard/index';
  import { parseValueToText, parseTime, findParentNode } from "utils/index";
  import { mapGetters } from "vuex";
    export default {
        name: "enterprise-view",
        props: [
          'currAreaInfo'
        ],
        components: {
          "baseDetail": () => import("views/admin/enterprise/components/baseDetail"),
          "certificateDetail": () => import("views/admin/enterprise/components/certificateDetail"),
          "scaleDetail": () => import("views/admin/enterprise/components/scaleDetail"),
          "equipmentDetail": () => import("views/admin/enterprise/components/equipmentDetail"),
          "productDetail": () => import("views/admin/enterprise/components/productDetail"),
          "materialDetail": () => import("views/admin/enterprise/components/materialDetail"),
        },
        data(){
          return {

            // 表格和分页相关
            listLoading: false, //是否显示表格的正在加载层
            list: [], //表格数据
            total:null,
            listQuery:{
              currPage: 1, // 当前第几页
              pageSize: 8, // 当前每页显示几条数据
              gridId:undefined,
            },
            enterpriseId: '',
            enterpriseName: '',
            dialogVisible: false,
            tabPosition:'0',
            supervisionTotal:[],
            subjectName:[],
            subjectClassificationEchartsDate:[],

          }
        },
      computed: {
        ...mapGetters(["user", "staticData"]),
        subjectClassificationOptions() {
          return this.staticData["企业主体分类"];
        },
        superviseClassificationOptions() {
          return this.staticData["监管级别"];
        },
      },
      mounted () {
          this.getList();
      },
      methods:{
        getList() {
          this.listLoading = true;
          this.listQuery.gridId=this.currAreaInfo.gridId;
          getEnterpriseByGrid(this.listQuery).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
          });
          getSubjectClassificationByGrid({gridId:this.currAreaInfo.gridId}).then(response => {
              response.data.forEach(item => {
                let subjectName=this.subjectClassificationFormatter(item.subjectClassification);
                this.subjectName.push(subjectName);
                this.subjectClassificationEchartsDate.push(item.total);
              })
            this.initCharts1();
          })
          getSupervisionByGrid({gridId:this.currAreaInfo.gridId}).then(response => {
          response.data.forEach(item => {
           let result = {};
            if(item.regulatoryLevel=='1'){
              result.value = item.total;
              result.name = "A"
            }else if(item.regulatoryLevel=='2'){
              result.value = item.total;
              result.name = "B"
            }else if(item.regulatoryLevel=='3'){
              result.value = item.total;
              result.name = "C"
            }else if(item.regulatoryLevel=='4'){
              result.value = item.total;
              result.name = "D"
            }
            this.supervisionTotal.push(result)
          });
            this.initCharts2();
          });
        },
        handleSizeChange(val) {
          this.listQuery.limit = val;
          this.getList();
        },
        handleCurrentChange(val) {
          this.listQuery.page = val;
          this.getList();
        },
        //审核查看
        handleView(row) {
          this.dialogVisible = true;
          this.enterpriseId = row.enterpriseId;
          this.enterpriseName = row.enterpriseName;
        },
        closeDialog(){
          this.tabPosition='0';
        },
        getEnterpriseStatus(enterpriseStatus){
          switch (enterpriseStatus){
            case '0':
              return 'primary';
            case '1':
              return 'success';
            case '2':
              return 'danger';
          }
        },
        //企业主体分类格式化
        subjectClassificationFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
        },
        registerStatusFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.staticData["企业注册状态"]);
        },
        enterpriseStatusFormatter(cellValue) {
          return parseValueToText(cellValue, this.staticData['企业状态']);
        },
        subjectClassificationFormatter( cellValue){
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
        },
        initCharts1(){          // 初始化企业主体分类统计柱状图
          var _this = this;
          var lineEChart1 = this.echarts.init(this.$refs['charts1'], 'dark');
          var option = {
            title : {
              text: '按企业主体分类划分',
              subtext: '',
              x:'center'
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow'
              }
            },
            xAxis: {
              type: 'category',
              data: this.subjectName
            },
            yAxis: {
              type: 'value'
            },
            series: [{
              data: this.subjectClassificationEchartsDate,
              type: 'bar',
              itemStyle:{
                normal:{
                  //颜色渐变函数 前四个参数分别表示四个位置依次为右/下/左/上
                  color: new _this.echarts.graphic.LinearGradient(0, 1, 1, 0, [{
                    offset: 0,
                    color: '#508DFF'
                  }, {
                    offset: 0.5,
                    color: '#26C5FE'
                  },{
                    offset: 1,
                    color: '#17fee8'
                  }])
                }
              },
            }]
          };
          lineEChart1.setOption(option);
        },
        initCharts2(){          // 初始化企业监管等级饼状图
          var _this = this;
          var lineEChart2 = this.echarts.init(this.$refs['charts2'], 'dark');
          var option = {
            title : {
              text: '按企业监管等级划分',
              subtext: '',
              x:'center'
            },
            tooltip : {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            /*visualMap: {
              show: false,
              min: 80,
              max: 600,
              inRange: {
                colorLightness: [0, 1]
              }
            },*/
            series : [
              {
                name: '监管等级',
                type: 'pie',
                radius : '65%',
                center: ['45%', '50%'],
                data: this.supervisionTotal,
                itemStyle: {
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          };
          lineEChart2.setOption(option);
        }
      },
    }
</script>

<style scoped>

</style>
