<template>
  <div >
    <div v-if='dataFlag'>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  :row-class-name="tableRowClassName" height="75vh">
   <!--   <el-table-column type="selection" width="55" ></el-table-column> -->
      <el-table-column align="center" label="证照名称" width="150" prop="certificateName"></el-table-column>
      <el-table-column align="center" label=证照号 width="200" prop="certificateNo"></el-table-column>
      <el-table-column align="center" label="证照类型" width="150" prop="certificateType" :formatter="certificateTypeFormatter"></el-table-column>
      <el-table-column align="center" label="发证单位" width="200" prop="issuingUnit"></el-table-column>
      <el-table-column align="center" label="登记范围" width="200" prop="registerScope"></el-table-column>
      <el-table-column align="center" label="证照状态" width="100" prop="certificateStatus" :formatter="certificateStatusFormatter"></el-table-column>
      <el-table-column align="center" label="变更状态" width="100" prop="changeStatus">
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.changeStatus=='1'">审核中</el-tag>
          <el-tag type="success" v-else-if="scope.row.changeStatus=='2'||scope.row.changeStatus=='3'">完成</el-tag>
          <el-tag type="info" v-else>未变更</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发证日期" width="150" prop="issuingDate" :formatter="issuingDateFormatter"></el-table-column>
      <el-table-column align="center" label="有效期" width="150" prop="validDate" :formatter="validDateFormatter"></el-table-column>
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
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false" :modal="false">
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
      <div style="height: 600px;">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="证照信息">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px">
              <el-form-item label="证照名称:" prop="certificateName">
                <el-input v-model.trim="form.certificateName"  placeholder="请输入证照名称" :disabled="isView"></el-input>
              </el-form-item>
              <el-form-item label="证照编号:" prop="certificateNo">
                <el-input v-model.trim="form.certificateNo" placeholder="请输入证照编号" :disabled="isView"></el-input>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <!--判断是否是编辑，编辑时证照类型不可修改；其他情况下查看时证照类型不可修改-->
                  <el-form-item label="证照类型:" prop="certificateType">
                    <el-select  v-model="form.certificateType" placeholder="请选择证照类型" clearable :disabled="true" v-if="isUpdate">
                      <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                    <el-select  v-model="form.certificateType" placeholder="请选择证照类型" clearable :disabled="isView" v-else>
                      <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="证照状态:" prop="certificateStatus"  v-if="isView" >
                    <el-select  v-model="form.certificateStatus" placeholder="请选择证照类型" clearable :disabled="isView">
                      <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="证照状态:" prop="certificateStatus" v-if="isUpdate">
                    <el-select  v-model="form.certificateStatus" placeholder="请选择证照类型" clearable :disabled="isUpdate">
                      <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="发证单位:" prop="issuingUnit" >
                <el-input v-model.trim="form.issuingUnit" placeholder="请输入发证单位" :disabled="isView"></el-input>
              </el-form-item>
              <el-row>
                <el-col :span="10">
                  <el-form-item label="发证日期:" prop="issuingDate" >
                    <el-date-picker type="date" placeholder="请选择" v-model="form.issuingDate" :disabled="isView"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="10" :offset="2">
                  <el-form-item label="有效期:" prop="validDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.validDate" :disabled="isView"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="登记范围:" >
                <el-input type="textarea" v-model.trim="form.registerScope"  :rows="3" placeholder="请输入登记范围" :disabled="isView"></el-input>
              </el-form-item>
              <el-form-item label="证照图片:" prop="imgFile"  ref="imgFile">
                <el-upload
                  action="https://jsonplaceholder.typicode.com/posts/"
                  list-type="picture-card"
                  :file-list="imgFile"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleImageRemove"
                  :on-change="addImgFile"
                  :limit="1"
                  :disabled="isView"
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
     <div v-if='!dataFlag'>
      无证照数据
    </div>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getResource}from "api/admin/register/index";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { getCertificateDetail } from "api/admin/enterprise/index";
  import {page,getObj,getChangeByCertificateId}from "api/admin/enterprise/certificate/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "index",
    data(){

      return {
        dataFlag:true,
        tabPosition:'0',
        height:undefined,
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          certificateType: undefined,
          enterpriseId: undefined,
          certificateStatus:undefined,
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
        serverImageUrl:process.env.SERVERIMAGEURL,
        imgFile:[],
        dialogImageVisible:false,//图片预览窗口是否显示
        dialogImageUrl:undefined,//预览图片的地址
        fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
        form: {},
        formStr:'',//表单初始数据字符串，用于判断表单数据是否被修改
        rules:{

        },
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
    props: {
    enterpriseId: {
      default: undefined
    }
  },
  watch: {
    'enterpriseId': function() {
      this.getList();
    }
  },
    created(){
      this.getList();
    },
    mounted(){
      //首次整个视图都渲染完毕后执行
      this.$nextTick(function() {
        let tableDiv = this.$el.querySelector('.el-table__body-wrapper');
        if (tableDiv) {
          this.needFixedRight = !($(tableDiv).attr("class").indexOf("is-scrolling-none") >= 0);
        }
      })
      //监听屏幕的改变
      window.onresize = () => {
        let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
        if(tableDiv){
          this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
        }
      }
      this.height=loadGridHeight();
    },
    methods:{
      getList() {
        this.listLoading = true;
        this.listQuery.enterpriseId=this.enterpriseId;
        page(this.listQuery).then(response => {
          if(response.total>0){
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
            this.dataFlag=true;
          }else{
            this.listLoading = false;
            this.dataFlag=false;
          }

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
        this.listQuery.certificateType= "";
        this.listQuery.certificateStatus=undefined;
      },
      handleCreate() {//新增
        this.form.enterpriseId=this.user.enterpriseId;
        this.certificate_update = true;
        this.tabPosition='0';
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
      },
      handleView(row) {//查看
        getObj(row.certificateId).then(response => {
          this.form = response.data;
          let resourceId = this.form.certificatePhoto;
          getResource(resourceId).then(response => {
            this.imgFile=[];
            this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
            $(".el-upload--picture-card").hide();
          })
          this.isView=true;
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
      cancel() {
        this.dialogFormVisible = false;
      },

      // 点击"+"图标时显示弹出框
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
      },
      //删除图片
      handleImageRemove(file){
        this.imgFile=[];
        if( this.imgFile.length==0){
          $(".el-upload--picture-card").show();
        }
      },
      addImgFile(file) { // 添加图片回调
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.$refs["imgFile"].resetField();//当添加图片后，重置图片验证功能。
        this.imgFile.push(file);
        if( this.imgFile.length>0){
          $(".el-upload--picture-card").hide();
        }
      },
      closeDialog(){
        this.form={};
        this.imgFile=[];
        $(".el-upload--picture-card").show();
        this.isView=false;
        this.isUpdate=false;
        this.$refs['form'].resetFields();
        this.verifyResultReason={};

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
