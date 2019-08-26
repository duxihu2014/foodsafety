<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item label="证照类型" prop="certificateType" class="filter-item">
            <el-select  v-model="listQuery.certificateType" placeholder="请选择" clearable>
              <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  :row-class-name="tableRowClassName" :height="height">
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="证照名称" width="150" prop="certificateName"></el-table-column>
      <el-table-column align="center" label=证照号 width="150" prop="certificateNo"></el-table-column>
      <el-table-column align="center" label="证照类型" width="120" prop="certificateType" :formatter="certificateTypeFormatter"></el-table-column>
      <el-table-column align="center" label="发证单位" width="150" prop="issuingUnit"></el-table-column>
      <el-table-column align="center" label="登记范围" width="200" prop="registerScope"></el-table-column>
      <el-table-column align="center" label="证照状态" width="100" prop="certificateStatus" :formatter="certificateStatusFormatter"></el-table-column>
      <el-table-column align="center" label="发证日期" width="150" prop="issuingDate" :formatter="issuingDateFormatter"></el-table-column>
      <el-table-column align="center" label="有效期" width="100" prop="validDate" :formatter="validDateFormatter"></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="100" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <!--修改，查看，新增表单对话框-->
    <el-dialog :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div>
        <template >
          <i class="el-icon-document">详情</i>
        </template>
      </div>
      <div style="height: 650px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="证照信息">
            <el-form :model="form"  ref="form" label-width="100px">
            <el-row>
              <el-col :span="10">
                  <el-form-item label="证照名称:" prop="certificateName">
                    <el-input v-model.trim="form.certificateName"  placeholder="请输入证照名称" disabled></el-input>
                  </el-form-item>
              </el-col>
              <el-col :span="10" :offset="2">
                <el-form-item label="证照编号:" prop="certificateNo">
                  <el-input v-model.trim="form.certificateNo" placeholder="请输入证照编号" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="证照类型:" prop="certificateType">
                    <el-select  v-model="form.certificateType" placeholder="请选择证照类型"  disabled>
                      <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="证照状态:" prop="certificateStatus">
                    <el-select  v-model="form.certificateStatus" placeholder="请选择证照类型" disabled>
                      <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="发证单位:" prop="issuingUnit" >
                <el-input v-model.trim="form.issuingUnit" placeholder="请输入发证单位" disabled></el-input>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="发证日期:" prop="issuingDate" >
                    <el-date-picker type="date" placeholder="请选择" v-model="form.issuingDate" disabled></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="有效期:" prop="validDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.validDate" disabled></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="登记范围:" >
                <el-input type="textarea" v-model.trim="form.registerScope"  :rows="3" placeholder="请输入登记范围" disabled></el-input>
              </el-form-item>
              <el-form-item label="证照图片:" prop="imgFile"  ref="imgFile">
                <el-upload
                  action="https://jsonplaceholder.typicode.com/posts/"
                  list-type="picture-card"
                  :file-list="imgFile"
                  :on-preview="handlePictureCardPreview"
                  :limit="1"
                  :disabled="true"
                  :auto-upload="false">
                  <i class="el-icon-plus"></i>
                </el-upload>
                <el-dialog  :visible.sync="dialogImageVisible" size="tiny"  :close-on-click-modal="false" append-to-body>
                  <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getResource}from "api/admin/register/index";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import {page,getObj,changeEnterpriseCertificate,addEnterpriseCertificate,getChangeByCertificateId}from "api/admin/enterprise/certificate/index";
  import { loadGridHeight } from "api/screen";
  import queryConditions from "components/QueryConditions/index";
  export default {
    name: "index",
    components: {
      queryConditions
    },
    props: {
      tabName:{
        default: undefined
      }
    },
    watch:{
      total(val){
        this.$emit('setCount',val,1);
      }
    },
    data(){
      return {
        tabPosition:'0',
        height:undefined,
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          certificateType: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined,
          certificateStatus:undefined,
        },
        dialogStatus: '',
        dialogFormVisible: false,
        certificate_update: false,
        needFixedRight:false,
        serverImageUrl:process.env.SERVERIMAGEURL,
        imgFile:[],
        dialogImageVisible:false,//图片预览窗口是否显示
        dialogImageUrl:undefined,//预览图片的地址
        fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
        form: {},
      }
    },
    computed: {
      ...mapGetters(["user", "staticData", "areaData"]),
    certificateTypeOptions(){
      return this.staticData["证照类型"]
    },
    statusOptions(){
      return this.staticData["企业非基本信息状态"]
    },
  },
  created() {
    this.getList();
  },
  mounted() {
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
  },
    updated () {
      this.height=loadGridHeight(this.tabName);
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
      this.listQuery.validStatus="4";
      if (this.listQuery.areaId == undefined)
        this.listQuery.areaId = this.user.areaId;
      if (this.user.userType == 4)
        this.listQuery.enterpriseId = this.user.enterpriseId;
      page(this.listQuery).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.listLoading = false;
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
    handleFilter() {
      this.listQuery.page=1;
      this.getList();
    },
    resetQuery() {
      this.listQuery.certificateType = undefined
      this.listQuery.certificateStatus = undefined;
      this.listQuery.areaId = undefined;
      this.listQuery.gridId = undefined;
      this.listQuery.enterpriseId = undefined;
    },
    handleView(row) {//查看
      getObj(row.certificateId).then(response => {
        this.form = response.data;
        let resourceId = this.form.certificatePhoto;
        getResource(resourceId).then(response => {
          this.imgFile=[];
          this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
          $(".el-upload--picture-card").hide();
        });
        this.dialogFormVisible = true;
        this.certificate_update = false;
        this.dialogStatus = 'view';
      });
    },
    tableRowClassName({row, rowIndex}) {
      if (row.certificateStatus === '0') {
        return 'warning-row';
      } else if (row.certificateStatus === '1') {
        return 'success-row';
      }
      return '';
    },
    // 点击"+"图标时显示弹出框
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogImageVisible = true;
    },
    closeDialog(){
      this.form={};
      this.imgFile=[];
      $(".el-upload--picture-card").show();
      this.$refs['form'].resetFields();
    },
    issuingDateFormatter(row, column, cellValue){
      return parseTime(cellValue,'{y}-{m}-{d}');
    },
    validDateFormatter(row, column, cellValue){
      return parseTime(cellValue,'{y}-{m}-{d}');
    },
    certificateTypeFormatter(row, column, cellValue){
      return parseValueToText(cellValue, this.staticData["证照类型"]);
    },
    certificateStatusFormatter(row, column, cellValue){
      return parseValueToText(cellValue, this.staticData["企业非基本信息状态"]);
    },
  },

  }
</script>

<style scoped>

</style>
