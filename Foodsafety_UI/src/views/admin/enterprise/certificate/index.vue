<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="证照类型" prop="certificateType" class="filter-item">
            <el-select  v-model="listQuery.certificateType" placeholder="请选择" clearable>
              <el-option v-for="item in  certificateTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
            <el-form-item label="状态" prop="certificateStatus" class="filter-item">
              <el-select  v-model="listQuery.certificateStatus" placeholder="请选择" clearable>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>新增</span>
      </div>
    </div>
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"  :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="证照名称" width="150" prop="certificateName"></el-table-column>
      <el-table-column align="center" label=证照号 width="150" prop="certificateNo"></el-table-column>
      <el-table-column align="center" label="证照类型" width="120" prop="certificateType" :formatter="certificateTypeFormatter"></el-table-column>
      <el-table-column align="center" label="发证单位" width="150" prop="issuingUnit"></el-table-column>
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
      <el-table-column align="center" label="有效期" width="100" prop="validDate" :formatter="validDateFormatter"></el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="250" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.changeStatus == '1'||scope.row.certificateStatus=='0'">修改</el-button>
          <el-button  size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.changeStatus == '1'||scope.row.certificateStatus=='0'" >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" v-else>
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)" :disabled="scope.row.changeStatus == '1'||scope.row.certificateStatus=='0'" >修改</el-button>
          <el-button  size="mini" type="danger" @click="handleDelete(scope.row)" :disabled="scope.row.changeStatus == '1'||scope.row.certificateStatus=='0'" >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <!--修改，查看，新增表单对话框-->
    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
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
      <div style="height: 650px;">
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
          <el-tab-pane label="变更原因" v-if="certificate_update">
            <el-form :model="changeVerifyForm"  :rules="rules" ref="changeReasonForm" >
              <el-form-item  prop="reason">
                <el-input type="textarea" :rows="5" v-model.trim="changeVerifyForm.reason"></el-input>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div slot="footer" class="dialog-footer" v-if="certificate_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create()">确 定</el-button>
        <el-button v-else type="primary" @click="update()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import {getResource}from "api/admin/register/index";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import {page,getObj,changeEnterpriseCertificate,addEnterpriseCertificate, delObj,getChangeByCertificateId}from "api/admin/enterprise/certificate/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "index",
    data(){
      const fileValidator =(rule, value, callback) =>{
        if(this.imgFile.length<1){
          callback(new Error('请选择营业执照附件'));
        }else{
          callback();
        }
      };
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
          enterpriseId: undefined,
          certificateStatus:'1',
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
        changeVerifyForm:{reason:undefined},
        rules:{
          certificateName:[{required: true, message: "请输入证照名称", trigger: "blur"}],
          certificateType:[{required: true, message: "请输入证照类型", trigger: "blur"}],
          certificateNo:[{required: true, message: "请输入证照编号", trigger: "blur"}],
          issuingUnit:[{required: true, message: "请输入发证单位", trigger: "blur"}],
          issuingDate:[{required: true, message: "请输入发证日期", trigger: "blur"}],
          registerScope:[{required: true, message: "请输入登记范围", trigger: "blur"}],
          validDate:[{required: true, message: "请输入有效日期", trigger: "blur"}],
          imgFile:[{required: true, validator: fileValidator, trigger: "change"}],
          reason:[{required: true, message: "请输入理由", trigger: "blur"}],
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
    created(){
      this.getList();
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
    methods:{
      getList() {
        this.listLoading = true;
        this.listQuery.enterpriseId=this.user.enterpriseId;
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
        this.listQuery.certificateType= "";
        this.listQuery.certificateStatus='1';
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
      handleUpdate(row) {//修改
        getObj(row.certificateId).then(response => {
          this.form = response.data;
          this.formStr=JSON.stringify(this.form);
          let resourceId = this.form.certificatePhoto;
          getResource(resourceId).then(response => {
            this.imgFile=[];
            this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
            $(".el-upload--picture-card").hide();
          })
          this.isUpdate=true;
          this.dialogFormVisible = true;
          this.tabPosition='0';
          this.certificate_update = true;
          this.dialogStatus = 'update';
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
      //修改提交
      update() {
        let _self = this;
        let flag = true;
        _self.$refs["form"].validate((valid,errors) => {
          if(!valid){
            flag=false;
            _self.tabPosition = '0';
          }else{
            if(_self.imgFile[0].raw==undefined&&_self.formStr==JSON.stringify(_self.form)){//未修改
              flag=false;
              this.$message("您还未修改数据哦！")
              _self.tabPosition = '0';
            }
          }
        });
        _self.$refs["changeReasonForm"].validate((valid,errors) => {
          if(!flag){
            _self.tabPosition = '0';
          }else {
            if(!(flag&&valid)){
              flag=false;
              _self.tabPosition = '1';
            }
          }
        });
        if(flag){
          _self.form.validDate=parseTime( _self.form.validDate);
          _self.form.issuingDate=parseTime( _self.form.issuingDate);
          let param = new FormData();
          param.append("enterpriseCertificate",JSON.stringify(_self.form))
          param.append("reason",_self.changeVerifyForm.reason);
          if(_self.imgFile[0].raw!=undefined){//判断图片是否被更新
            param.append("file",_self.imgFile[0].raw);
          }else {
            param.append("file",null);
          }
          changeEnterpriseCertificate(param).then((response)=>{
            _self.dialogFormVisible=false;
            _self.getList();
            if(response.rel){
              _self.$message({
                type:'success',
                showClose: true,
                message: `操作成功！`
              });
            }else {
              _self.$message({
                type:'error',
                showClose: true,
                message: `操作失败！`
              });
            }
          })
        }
      },
      //新增提交
      create(){
        let _self = this;
        let flag = true;
        _self.$refs["form"].validate((valid,errors) => {
          if(!valid){
            flag=false;
            _self.tabPosition = '0';
          }
        });
        _self.$refs["changeReasonForm"].validate((valid,errors) => {
          if(!flag){
            _self.tabPosition = '0';
          }else {
            if(!(flag&&valid)){
              flag=false;
              _self.tabPosition = '1';
            }
          }
        });
        if(flag){
          _self.form.validDate=parseTime( _self.form.validDate);
          _self.form.issuingDate=parseTime( _self.form.issuingDate);
          let param = new FormData();
          param.append("enterpriseCertificate",JSON.stringify(_self.form))
          param.append("reason",_self.changeVerifyForm.reason);
          if(_self.imgFile[0].raw!=undefined){//判断图片是否被更新
            param.append("file",_self.imgFile[0].raw);
          }else {
            param.append("file",null);
          }
          addEnterpriseCertificate(param).then((response)=>{
            _self.dialogFormVisible=false;
            _self.getList();
            if(response.rel){
              _self.$message({
                type:'success',
                showClose: true,
                message: `操作成功！`
              });
            }else {
              _self.$message({
                type:'error',
                showClose: true,
                message: `操作失败！`
              });
            }
          })
        }
      },
      handleDelete(row) {
        this.$confirm("此操作会将证照永久停用, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            delObj(row.certificateId)
              .then(() => {
                this.$message({
                  showClose: true,
                  message: "操作成功",
                  type: "success",
                  duration: 2000
                });
                this.getList();
              })
              .catch(() => {});
          })
          .catch(() => {});
      },
      //变更记录查询
      changeView(row){
        getChangeByCertificateId({"certificateId":row.certificateId}).then((response)=>{
          // console.log(response)
        })
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
        this.$refs['changeReasonForm'].resetFields();
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
