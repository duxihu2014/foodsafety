<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="名称"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" :clearable="true" v-model.trim="listQuery.supplierName_like"> </el-input>
          </el-form-item>
          <!--<el-form-item label="类别" class="filter-item">
            <el-select  v-model="listQuery.supplierCategory" filterable placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in supplierCategoryOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>-->
          <el-form-item label="类型" class="filter-item">
            <el-select  v-model="listQuery.supplierType" filterable placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in supplierTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.supplierStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
              @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
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

      <el-table-column align="center" label="操作" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="150" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看
          </el-button>
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
      <div slot="title">
        <template v-if="textMap[dialogStatus] === '创建'">
          <i class="el-icon-document"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else-if="textMap[dialogStatus] === '编辑'">
          <i class="el-icon-edit"> {{textMap[dialogStatus]}}</i>
        </template>
        <template v-else="textMap[dialogStatus] === '查看'">
          <i class="el-icon-view"> {{textMap[dialogStatus]}}</i>
        </template>
      </div>

      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
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
            <!--<el-tab-pane label="生产许可证信息">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="许可证编号" prop="productionCertificateNo" ref="productionCertificateNo">
                    <el-input v-model.trim="form.productionCertificateNo" placeholder="" :clearable="true" :readonly="!supplierManager_update"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="有效期" prop="productionCertificateValid" ref="productionCertificateValid">
                    <el-date-picker v-model="form.productionCertificateValid" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="许可证照片" prop="productionCertificatePhoto" ref="productionCertificatePhoto">
                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :file-list="productionCertificateImgFile"
                      :on-preview="handlePictureCardPreview"
                      :on-remove="handleProductionCertificateImageRemove"
                      :on-change="addProductionCertificateImgFile"
                      :limit="1"
                      :auto-upload="false">
                      <i class="el-icon-plus"></i>
                    </el-upload>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>-->
          </el-tabs>
        </div>
      </el-form>

      <div slot="footer" class="dialog-footer" v-if="supplierManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </div>
</template>

<script>
  import { page, addObj, getObj, putObj, changeStatus, checkSupplierName } from "api/admin/purchase/materials/supplier/index";
  import { getResource } from "api/admin/resource/index";
  import { mapGetters } from "vuex";
  import idCardNoUtil from 'utils/idCardNoUtil';
  import { parseValueToText, parseForm } from "utils/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "disinfection",
    data() {
      const supplierNameValidator = (rule, value, callback) => {
        //debugger
        if(this.supplierNameSelected!==value){
          checkSupplierName({"supplierName":value}).then(response => {
            if(!response.rel){
              return callback(new Error("餐具消毒企业名称重复"));
            }else{
              callback();
            }
          });
        }else{
          callback();
        }
      };
      const cardNoValidator = (rule, value, callback) => {
        //15位和18位证件号码的基本校验
        if(!value) callback() ;
        let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(value);
        if(!check) return callback(new Error('请输入正确的证件号码'));
        //判断长度为15位或18位
        if(value.length==15){
          if(!idCardNoUtil.check15IdCardNo(value)){
            callback(new Error('请输入正确的证件号码'));
          }else callback();

        }else if(value.length==18){
          if(!idCardNoUtil.check18IdCardNo(value))
            callback(new Error('请输入正确的证件号码'));
          else callback();
        }else{
          callback(new Error('请输入正确的证件号码'));
        }
      };
      const mobileValidator = (rule, value, callback) => {
        const mymobile =  /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
        if (value && !mymobile.test(value)) {
          return callback(new Error("电话号码格式不正确"));
        } else {
          callback();
        }
      };
      // const businessLicenceValidator =(rule, value, callback) =>{
      //   if(this.businessLicenceImgFile.length<1){
      //     callback(new Error('请上传营业执照照片'));
      //   }else{
      //     callback();
      //   }
      // };
      // const productionCertificateValidator=(rule, value, callback)=>{
      //   if(this.productionCertificateImgFile.length<1){
      //     callback(new Error('请上传生产许可证照片'));
      //   }else{
      //     callback();
      //   }
      // };
      return {
        idCardFrontImgFile: [],
        idCardBackImgFile: [],
        businessLicenceImgFile: [],
        // productionCertificateImgFile: [],
        dialogImageVisible:false,//图片预览窗口是否显示
        dialogImageUrl:undefined,//预览图片的地址
        fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
        height: undefined,
        supplierNameSelected:'',
        form: {
          supplierId: undefined,
          supplierName: undefined,
          supplierCategory: '3',// 餐饮用具消毒
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
          // productionCertificateNo: undefined,
          // productionCertificateValid: undefined,
          // productionCertificatePhoto: undefined
        },
        rules: {
          supplierName: [{required: true, message: "请输入名称", trigger: "blur"},
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"},
            { validator: supplierNameValidator, trigger: "blur"}
          ],
          /*supplierCategory: [{required: true, message: "请选择类别", trigger: "change"}],*/
          supplierType: [{required: true, message: "请选择类型", trigger: "change"}],
          leader: [{required: true, message: "请输入负责人", trigger: "blur"},
            {max: 16, message: "长度不能超过16个字符", trigger: "blur"}],
          idCardNo: [{required: true, message: "请输入证件号码", trigger: "blur"},
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"},
            { validator: cardNoValidator, trigger: "blur" }],
          contactNumber: [{required: true, message: "请输入联系电话", trigger: "blur"},
            {max: 16, message: "长度不能超过16个字符", trigger: "blur"},
            { validator: mobileValidator, trigger: "blur" }],
          /*operatingAddress: [{required: true, message: "请输入经营地址", trigger: "blur"},
            {max: 64, message: "长度不能超过64个字符", trigger: "blur"}],
          operationScope: [{required: true, message: "请输入经营范围", trigger: "blur"},
            {max: 128, message: "长度不能超过128个字符", trigger: "blur"}],*/
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          supplierName_like: undefined,
          supplierCategory: '3',// 餐饮用具消毒
          supplierType: undefined,
          supplierStatus:undefined
        },
        dialogFormVisible: false,
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
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
      // handleProductionCertificateImageRemove(file){
      //   this.productionCertificateImgFile=[];
      //   if( this.productionCertificateImgFile.length==0){
      //     $("#pane-3 .el-upload--picture-card").show();
      //   }
      // },
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
      // addProductionCertificateImgFile(file){
      //   if(this.fileFormat.indexOf(file.raw.type) < 0 ){
      //     this.$message({
      //       type:'error',
      //       showClose: true,
      //       message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
      //     });
      //     return false;
      //   }
      //   this.$refs["productionCertificatePhoto"].resetField();//当添加图片后，重置图片验证功能。
      //   this.productionCertificateImgFile.push(file);
      //   if( this.productionCertificateImgFile.length>0){
      //     $("#pane-3 .el-upload--picture-card").hide();
      //   }
      // },
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
        page(this.listQuery).then(response => {
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
      handleCreate() {
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.supplierManager_update = true;
        //   this.resetTmp();
      },
      handleUpdate(row) {
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
          this.supplierNameSelected=response.data.supplierName;
          this.dialogStatus = "update";
          this.supplierManager_update = true;
        });
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
      create(formName) {
        const set = this.$refs;
        set[formName].validate((valid, errors) => {
          if (valid) {
            let param = new FormData();

            // param.append("formData", this.form);
            param.append('formData', parseForm(this.form));

            if(this.idCardFrontImgFile.length>0)
              param.append("idCardFrontImgFile", this.idCardFrontImgFile[0].raw);
            if(this.idCardBackImgFile.length>0)
              param.append("idCardBackImgFile", this.idCardBackImgFile[0].raw);
            if(this.businessLicenceImgFile.length>0)
              param.append("businessLicenceImgFile", this.businessLicenceImgFile[0].raw);
            // if(this.productionCertificateImgFile.length>0)
            //   param.append("productionCertificateImgFile", this.productionCertificateImgFile[0].raw);

            addObj(param).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$message({
                showClose: true,
                message: "创建成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            for (var key in errors){
              this.tabPosition = this.$refs[key].$attrs['tab'];
              break;
            };
            return false;
          }
        });
      },
      cancel(formName) {
        this.dialogFormVisible = false;
        this.$refs[formName].resetFields();
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate((valid, errors) => {
          if (valid) {
            let param = new FormData();

            param.append('formData', parseForm(this.form));

            if(this.idCardFrontImgFile.length>0) {
              if (this.idCardFrontImgFile[0].raw) {
                param.append("idCardFront", "1"); // 修改过照片，需要更新。
                param.append("idCardFrontImgFile", this.idCardFrontImgFile[0].raw);
              } else {
                param.append("idCardFront", "0"); // 未修改照片，不需要更新。
              }
            } else {
              param.append("idCardFront", "-1"); // 删除照片
            }
            if(this.idCardBackImgFile.length>0) {
              if (this.idCardFrontImgFile[0].raw) {
                param.append("idCardBack", "1"); // 修改过照片，需要更新。
                param.append("idCardBackImgFile", this.idCardBackImgFile[0].raw);
              } else {
                param.append("idCardBack", "0"); // 未修改照片，不需要更新。
              }
            } else {
              param.append("idCardBack", "-1"); // 删除照片
            }
            if(this.businessLicenceImgFile.length>0) {
              if (this.businessLicenceImgFile[0].raw) {
                param.append("businessLicence", "1"); // 修改过照片，需要更新。
                param.append("businessLicenceImgFile", this.businessLicenceImgFile[0].raw);
              } else {
                param.append("businessLicence", "0"); // 未修改照片，不需要更新。
              }
            } else {
              param.append("businessLicence", "-1"); // 删除照片
            }

            putObj(param).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$message({
                showClose: true,
                message: "更新成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            for (var key in errors){
              this.tabPosition = this.$refs[key].$attrs['tab'];
              break;
            };
            return false;
          }
        });
      },
      resetTmp() {
        this.form={
          supplierId: undefined,
          supplierName: undefined,
          supplierCategory: '3',
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
        this.listQuery = {page: page, limit: limit,
          supplierName_like: undefined,
          supplierCategory: "3",
          supplierType: undefined,
          supplierStatus:undefined };
      },
      getSelection(selection) {
        this.supplierSelection = selection;
      },
      tableStop: function () {  // 0停用
        this.stopOrStart(0);
      },
      tableStart: function () {  // 1启用
        this.stopOrStart(1);
      },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.supplierSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.supplierSelection.forEach(supplier => {
            if(supplier.supplierStatus==1){
              flag=false;
            }
          });
          if(!flag){
            this.$message({
              showClose: true,
              type:'warning',
              message: "您选择的数据中有启用的数据，不能重复启用！"
            });
            return;
          }
        }else{//停用
          this.supplierSelection.forEach(supplier => {
            if(supplier.supplierStatus==0){
              flag=false;
            }
          });
          if(!flag){
            this.$message({
              showClose: true,
              type:'warning',
              message: "您选择的数据中有停用的数据，不能重复停用！"
            });
            return;
          }
        }
        let operation = num?'启用':'停用';
        this.$confirm(`确定要${operation}该数据吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = [];
          this.supplierSelection.forEach((item)=>{
            ids.push(item.supplierId);
          });
          ids= ids.length>1?ids.join(','):ids[0];
          let param = {"ids":ids, "status":num};
          changeStatus(param).then((response) => {
            if(response.rel){
              this.$message({
                type:'success',
                showClose: true,
                message: `${operation}成功！`
              });
            }else {
              this.$message({
                type:'error',
                showClose: true,
                message: `${operation}失败！`
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
