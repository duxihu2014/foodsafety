<template>
  <div>
    <div v-if='dataFlag'>
      <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
                border fit highlight-current-row style="width: 100%" highlight-current-row
                @selection-change="getSelection" :row-class-name="tableRowClassName" height="75vh">
        <!--<el-table-column  type="selection"  width="55">
        </el-table-column>-->
        <el-table-column align="center" label="编号" width="100" prop="supplierId">

        </el-table-column>
        <el-table-column width="250px" align="center" label="名称" prop="supplierName">

        </el-table-column>
        <!--<el-table-column width="150px" align="center" label="类别" prop="supplierCategory" :formatter="supplierCategoryFormatter">

        </el-table-column>-->
        <el-table-column width="100px" align="center" label="类型" prop="supplierType" :formatter="supplierTypeFormatter">

        </el-table-column>
        <el-table-column width="100px" align="center" label="负责人" prop="leader">

        </el-table-column>
        <el-table-column width="200px" align="center" label="证件号码" prop="idCardNo">

        </el-table-column>
        <el-table-column width="150px" align="center" label="身份证照片" prop="idCardPhoto">
          <template slot-scope="scope">
            <el-button v-show="scope.row.idCardNoPhotoFront"
                       size="mini" type="primary" plain
                       @click="handlePhoto(scope.row.idCardNoPhotoFront)">正面</el-button>
            <el-button v-show="scope.row.idCardNoPhotoBack"
                       size="mini" type="primary" plain
                       @click="handlePhoto(scope.row.idCardNoPhotoBack)">反面</el-button>
          </template>
        </el-table-column>
        <el-table-column width="150px" align="center" label="联系电话" prop="contactNumber">

        </el-table-column>
        <el-table-column width="300px" align="center" label="经营地址" prop="operatingAddress">

        </el-table-column>
        <!--<el-table-column width="300px" align="center" label="经营范围" prop="operationScope">

        </el-table-column>-->
        <el-table-column width="200px" align="center" label="营业执照编号" prop="businessLicenceNo">

        </el-table-column>

        <el-table-column width="150px" align="center" label="营业执照照片" prop="businessLicencePhoto">
          <template slot-scope="scope">
            <el-button v-show="scope.row.businessLicencePhoto"
                       size="mini" type="primary" plain
                       @click="handlePhoto(scope.row.businessLicencePhoto)">查看</el-button>
          </template>
        </el-table-column>

        <el-table-column width="180px" align="center" label="营业执照有效期" prop="businessLicenceValid">

        </el-table-column>
        <!--<el-table-column width="200px" align="center" label="生产许可证编号" prop="productionCertificateNo">

        </el-table-column>
        <el-table-column width="150px" align="center" label="生产许可证照片" prop="productionCertificatePhoto">

        </el-table-column>
        <el-table-column width="180px" align="center" label="生产许可证有效期" prop="productionCertificateValid">

        </el-table-column>-->

        <el-table-column width="110px" align="center" label="状态" prop="supplierStatus" :formatter="statusFormatter"></el-table-column>

        <el-table-column align="center" label="操作" width="100" fixed="right" v-if="needFixedRight">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100" v-else>
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-show="!listLoading" class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
      </div>
      <el-dialog  title="查看" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false" append-to-body>
        <div slot="title">
          <i class="el-icon-view">查看</i>
        </div>

        <el-form :model="form" ref="form" label-width="100px">
          <div style="height: 330px;">
            <el-tabs v-model="tabPosition">
              <el-tab-pane label="基本信息">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="名称" prop="supplierName" ref="supplierName" tab="0">
                      <el-input v-model.trim="form.supplierName" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="类型" prop="supplierType" ref="supplierType" tab="0">
                      <el-select  v-model="form.supplierType" filterable placeholder="请选择" clearable filterabler :disabled="!supplierManager_update">
                        <el-option v-for="(item, index) in supplierTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <!--<el-row>
                  <el-col :span="12">
                    <el-form-item label="类别" prop="supplierCategory" ref="supplierCategory" tab="0">
                      <el-select  v-model="form.supplierCategory" filterable placeholder="请选择" clearable filterabler :disabled="!supplierManager_update">
                        <el-option v-for="(item, index) in supplierCategoryOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="类型" prop="supplierType" ref="supplierType" tab="0">
                      <el-select  v-model="form.supplierType" filterable placeholder="请选择" clearable filterabler :disabled="!supplierManager_update">
                        <el-option v-for="(item, index) in supplierTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>-->
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="经营地址" prop="operatingAddress">
                      <el-input v-model.trim="form.operatingAddress" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="经营范围" prop="operationScope">
                      <el-input type="textarea"  :rows="2"  v-model.trim="form.operationScope" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="法人信息">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="负责人" prop="leader" ref="leader" tab="1">
                      <el-input v-model.trim="form.leader" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="证件号码" prop="idCardNo" ref="idCardNo" tab="1">
                      <el-input v-model.trim="form.idCardNo" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="联系电话" prop="contactNumber" ref="contactNumber" tab="1">
                      <el-input v-model.trim="form.contactNumber" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row >
                  <el-col :span="12">
                    <el-form-item label="身份证正面" prop="idCardNoPhotoFront" ref="idCardNoPhotoFront">
                      <el-upload
                        action="https://jsonplaceholder.typicode.com/posts/"
                        list-type="picture-card"
                        :file-list="idCardFrontImgFile"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleIdCardFrontImageRemove"
                        :on-change="addIdCardFrontImgFile"
                        :limit="1"
                        :auto-upload="false">
                        <i class="el-icon-plus"></i>
                      </el-upload>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="身份证反面" prop="idCardNoPhotoBack" ref="idCardNoPhotoBack">
                      <el-upload
                        action="https://jsonplaceholder.typicode.com/posts/"
                        list-type="picture-card"
                        :file-list="idCardBackImgFile"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleIdCardBackImageRemove"
                        :on-change="addIdCardBackImgFile"
                        :limit="1"
                        :auto-upload="false">
                        <i class="el-icon-plus"></i>
                      </el-upload>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="营业执照信息">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="执照编号" prop="businessLicenceNo" ref="businessLicenceNo">
                      <el-input v-model.trim="form.businessLicenceNo" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="有效期" prop="businessLicenceValid" ref="businessLicenceValid">
                      <el-date-picker v-model="form.businessLicenceValid" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1"></el-date-picker>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="执照照片" prop="businessLicencePhoto" ref="businessLicence">
                      <el-upload
                        action="https://jsonplaceholder.typicode.com/posts/"
                        list-type="picture-card"
                        :file-list="businessLicenceImgFile"
                        :on-preview="handlePictureCardPreview"
                        :on-remove="handleBusinessLicenceImageRemove"
                        :on-change="addBusinessLicenceImgFile"
                        :limit="1"
                        :auto-upload="false">
                        <i class="el-icon-plus"></i>
                      </el-upload>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-form>

      </el-dialog>

      <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </div>
    <div v-if='!dataFlag'>
      无合作伙伴数据
    </div>
  </div>
</template>

<script>
  import { pageAll, getObj } from "api/admin/purchase/materials/supplier/index";
  import { getResource } from "api/admin/resource/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import { parseValueToText } from "utils/index";
  export default {
    name: "cooperative-partner",
    props: {
      enterpriseId: {
        default: undefined
      },
    },
    watch: {
      'enterpriseId': function() {
        this.getList();
      }
    },
    data() {
      return {
        dataFlag:true,
        idCardFrontImgFile: [],
        idCardBackImgFile: [],
        businessLicenceImgFile: [],
        dialogImageVisible:false,//图片预览窗口是否显示
        dialogImageUrl:undefined,//预览图片的地址
        height: undefined,
        form: {
          supplierId: undefined,
          supplierName: undefined,
          supplierCategory: '1',// 供货商
          supplierType: undefined,
          leader: undefined,
          contactNumber: undefined,
          operatingAddress: undefined,
          operationScope: undefined,
          idCardNo: undefined,
          idCardNoPhotoFront: undefined,
          idCardNoPhotoBack: undefined,
          businessLicenceNo: undefined,
          businessLicenceValid: undefined,
          businessLicencePhoto: undefined,
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          supplierStatus: '1',
          enterpriseId: undefined
        },
        dialogFormVisible: false,
        dialogStatus: "",
        supplierManager_update: true,
        tableKey: 0,
        supplierSelection: [],
        needFixedRight:false,
        tabPosition: "0",
        serverImageUrl:process.env.SERVERIMAGEURL,
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
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"]),
      statusOptions() {
        return this.staticData["状态"];
      },
      supplierCategoryOptions() {
        return this.staticData["供货商类别"];
      },
      supplierTypeOptions() {
        return this.staticData["供货商类型"];
      }
    },
    methods: {
      //删除图片
      handleIdCardFrontImageRemove(file){
        this.idCardFrontImgFile=[];
        if( this.idCardFrontImgFile.length==0){
          $("#pane-1 .el-upload--picture-card:eq(0)").show();
        }
      },
      handleIdCardBackImageRemove(file){
        this.idCardBackImgFile=[];
        if( this.idCardBackImgFile.length==0){
          $("#pane-1 .el-upload--picture-card:eq(1)").show();
        }
      },
      handleBusinessLicenceImageRemove(file){
        this.businessLicenceImgFile=[];
        if( this.businessLicenceImgFile.length==0){
          $("#pane-2 .el-upload--picture-card").show();
        }
      },
      // 点击"+"图标时显示弹出框
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
      },
      handlePhoto(id){
        getResource(id).then(response => {
          if (response.rel) {
            this.dialogImageUrl = this.serverImageUrl+'/'+response.data.resourcePath;
            this.dialogImageVisible = true;
          }else{
            this.$message({
              type:'error',
              showClose: true,
              message: `资源获取失败！`
            });
          }
        });
      },
      addIdCardFrontImgFile(file) { // 添加图片回调
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.$refs["idCardNoPhotoFront"].resetField();//当添加图片后，重置图片验证功能。
        this.idCardFrontImgFile.push(file);
        if( this.idCardFrontImgFile.length>0){
          $("#pane-1 .el-upload--picture-card:eq(0)").hide();
        }
      },
      addIdCardBackImgFile(file) {
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.$refs["idCardNoPhotoBack"].resetField();//当添加图片后，重置图片验证功能。
        this.idCardBackImgFile.push(file);
        if( this.idCardBackImgFile.length>0){
          $("#pane-1 .el-upload--picture-card:eq(1)").hide();
        }
      },
      addBusinessLicenceImgFile(file){
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.$refs["businessLicence"].resetField();//当添加图片后，重置图片验证功能。
        this.businessLicenceImgFile.push(file);
        if( this.businessLicenceImgFile.length>0){
          $("#pane-2 .el-upload--picture-card").hide();
        }
      },
      tableRowClassName({row, rowIndex}) {
        if (row.supplierStatus === '0') {
          return 'warning-row';
        } else if (row.supplierStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      getList() {
        this.listLoading = true;
        this.listQuery.enterpriseId=this.enterpriseId;
        pageAll(this.listQuery).then(response => {
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
        getObj(row.supplierId).then(response => {
          this.form = response.data;
          if(this.form.idCardNoPhotoFront){
            getResource(this.form.idCardNoPhotoFront).then(response =>{
              this.idCardFrontImgFile=[];
              this.idCardFrontImgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              $("#pane-1 .el-upload--picture-card:eq(0)").hide();
            });
          }
          if(this.form.idCardNoPhotoBack){
            getResource(this.form.idCardNoPhotoBack).then(response =>{
              this.idCardBackImgFile=[];
              this.idCardBackImgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              $("#pane-1 .el-upload--picture-card:eq(1)").hide();
            });
          }
          if(this.form.businessLicencePhoto){
            getResource(this.form.businessLicencePhoto).then(response =>{
              this.businessLicenceImgFile=[];
              this.businessLicenceImgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              $("#pane-2 .el-upload--picture-card").hide();
            });
          }
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
          this.supplierManager_update = false;
        });
      },
      closeDialog(){
        this.resetTmp();
      },
      resetTmp() {
        this.form={
          supplierId: undefined,
          supplierName: undefined,
          supplierCategory: '1',
          supplierType: undefined,
          leader: undefined,
          contactNumber: undefined,
          operatingAddress: undefined,
          operationScope: undefined,
          idCardNo: undefined,
          idCardNoPhotoFront: undefined,
          idCardNoPhotoBack: undefined,
          businessLicenceNo: undefined,
          businessLicenceValid: undefined,
          businessLicencePhoto: undefined,
          productionCertificateNo: undefined,
          productionCertificateValid: undefined,
          productionCertificatePhoto: undefined};
        this.$refs["idCardNoPhotoFront"].resetField();//当添加图片后，重置图片验证功能。
        this.$refs["idCardNoPhotoBack"].resetField();//当添加图片后，重置图片验证功能。
        this.$refs["businessLicence"].resetField();//当添加图片后，重置图片验证功能。
        this.idCardFrontImgFile=[];
        this.idCardBackImgFile=[];
        this.businessLicenceImgFile=[];
        $("#pane-1 .el-upload--picture-card:eq(0)").show();
        $("#pane-1 .el-upload--picture-card:eq(1)").show();
        $("#pane-2 .el-upload--picture-card").show();
        this.supplierManager_update = true;
        this.tabPosition ="0";
        this.$nextTick(function() {
          this.$refs['form'].clearValidate();
        });
      },
      supplierCategoryFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["供货商类别"]);
      },
      supplierTypeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["供货商类型"]);
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["状态"]);
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = {page: page, limit: limit};
      },
      getSelection(selection) {
        this.supplierSelection = selection;
      }
    }
  };
</script>
