<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
           <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId"
          :enterpriseId="listQuery.enterpriseId">
          </queryConditions>
            <el-form-item label="原料名称"  class="filter-item">
              <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.materialName"> </el-input>
            </el-form-item>

          <el-form-item label="条形码/自编码"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.barcodeCoding"> </el-input>
          </el-form-item>

           <el-form-item class="filter-item">
              <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
              <el-button  v-waves  @click="resetQuery()">重置</el-button>
           </el-form-item>
        </el-form>
    </div>
     </fieldset>

   <!--  <div class="head-box">
     <div class="item-box">
       <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
       <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
       <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
     </div>
   </div>
    -->
    <el-table   :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>

      <el-table-column align="center" label="检验编号" width="100" prop="inspectionReportId"></el-table-column>
       <el-table-column align="center" label="企业名称" width="250" prop="enterpriseId" :formatter="enterpriseFormatter"></el-table-column>
       <el-table-column align="center" label="供货商名称" width="200" prop="supplierName" ></el-table-column>
      <el-table-column align="center" label="原料类型" width="150" prop="materialCategory" :formatter="materialCategoryFormatter"></el-table-column>
      <el-table-column align="center" label="原材料名称" width="150" prop="materialName" ></el-table-column>
      <el-table-column align="center" label="条码" width="150" prop="barcodeCoding" ></el-table-column>
      <el-table-column align="center" label="生产日期" width="180" prop="productionDate" ></el-table-column>
      <el-table-column align="center" label="有效期" width="180" prop="validDate" ></el-table-column>
      <el-table-column align="center" label="采购编号" width="100" prop="purchaseId" ></el-table-column>
      <el-table-column align="center" label="质检报告图片" width="100" prop="inspectionReportPhoto">
        <template slot-scope="scope">
          <el-button v-show="scope.row.inspectionReportPhoto"
                     size="mini" type="primary" plain
                     @click="handlePhoto(scope.row.inspectionReportPhoto)">图片</el-button>
        </template>

      </el-table-column>
      <el-table-column  align="center" fixed="right" label="操作" width="100"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handleView(scope.row)">查看</el-button>

        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="100"  v-else>
        <template slot-scope="scope">
         <el-button size="mini"  type="success" @click="handleView(scope.row)">查看</el-button>

         </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container" >
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
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" disabled>
        <el-tabs >
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="原料名称" prop="materialName" >
                  <el-input v-model.trim="form.materialName" placeholder=""  :readonly="viewReadOnly" ></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="原料类型" prop="materialCategory"  ref="certificateNumber" tab="1">
                  <el-select  v-model="form.materialCategory" placeholder="请选择" clearable :disabled="viewReadOnly">
                    <el-option v-for="item in  materialCategoryOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
           </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="条形码/自编码" prop="barcodeCoding"  >
                  <el-input   v-model.trim="form.barcodeCoding"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>


               <el-col :span="12">
                <el-form-item label="生产日期" prop="productionDate" >
                  <el-date-picker v-model="form.productionDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="质检照片" prop="imgFile" ref="imgFile">
                  <el-upload
                    action="https://jsonplaceholder.typicode.com/posts/"
                    list-type="picture-card"
                    :file-list="imgFile"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleImageRemove"
                    :on-change="addImgFile"
                    :limit="1"
                    :auto-upload="false">
                    <i class="el-icon-plus"></i>
                  </el-upload>
                </el-form-item>
              </el-col>

             <el-col :span="12">
                <el-form-item label="有效期" prop="validDate" >
                  <el-date-picker v-model="form.validDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>



            </el-row>

          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="manager_update">
        <el-button @click="cancel()">取 消</el-button>
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

  import {  addObj, updateObj,getSuppliers,changeStatus} from "api/admin/purchase/materials";
  import {  getEnterpriseBaseMap} from 'api/admin/safety/record/recyle/index'
  import { page, getObj } from "api/admin/purchase/materials/report";
  import { parseValueToText ,parseForm} from "utils/index";
  import { getResource } from "api/admin/resource/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import {getToken} from 'utils/auth';
  import queryConditions from "components/QueryConditions/index";

  export default {
        name: "index",
        components: {
          queryConditions
        },
        data(){
          return{
            enterpriseMap:[],
            height:undefined,
            list: null,
            total: null,
            listLoading: false,
            listQuery: {
              page: 1,
              limit: 20,
              barcodeCoding: "",
              materialName: "",
              enterpriseId: undefined,
              areaId: undefined,
              gridId: undefined
            },
            form: {
              certificateNumber:undefined
            },
            rules: {
              supplierId:[{required: true, message: "请选择供货商", trigger:["blur","change"]}],
              materialName: [{required: true, message: "请输入原料名称", trigger: "blur"}],
              materialCategory: [{required: true, message: "请输入原料类型", trigger:  "change"}],
              specification: [{required: true, message: "请输入有规格", trigger: "blur"}],
              unit: [{required: true, message: "请输入单位", trigger: "blur"}],
              shelfLife: [{required: true, message: "请输入保质期", trigger: "change"}],
              originPlace: [{required: true, message: "请输入产地", trigger: "blur"}],
              brand: [{required: true, message: "请输入品牌", trigger: "blur"}]
             },
            dialogFormVisible: false,
            viewReadOnly:false, //控制查看时的按钮显示
            dialogStatus: "",
            textMap: {
              update: "编辑",
              create: "创建",
              view: "查看"
            },
            manager_update:true,
            selection: [],
            needFixedRight:false,
            productionCertificateFile:[],
            imgFile:[],
            serverImageUrl:process.env.SERVERIMAGEURL,
            dialogImageVisible:false,//图片预览窗口是否显示
            dialogImageUrl:undefined,//预览图片的地址
            fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
            pickerOptions: {
              disabledDate(time) {
                return time.getTime() < Date.now();
              },
            }
          }
        },
      created() {
        this.getList();
        this.getEnterpriseBaseMap();
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
        }

      },
    updated () {
      this.height=loadGridHeight();
    },
      computed: {
        ...mapGetters(["staticData","user"]),
        materialCategoryOptions(){
          return this.staticData["原料类型"];
        }
      },
      methods: {
        setAreaId(data){
        this.listQuery.areaId = data;
       },
        setGridId(data){
        this.listQuery.gridId = data;
       },
        setEnterpriseId(data){
         this.listQuery.enterpriseId = data;
        },
            getEnterpriseBaseMap() {
      getEnterpriseBaseMap().then(response => {
        this.enterpriseMap = response['enterprise'];
        //this.receivingUnitMap =response['supplier'];
      });
    },
         getValidDateType(validStatus){
          switch (validStatus){
            case '2':
              return 'warning';
            case '3':
              return 'danger';
          }
         },
        tableRowClassName({row, rowIndex}) {
          if (row.staffStatus === '0') {
            return 'warning-row';
          } else if (row.staffStatus === '1') {
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
        resetQuery() {
          let page = this.listQuery.page;
          let limit = this.listQuery.limit;
          this.listQuery = {page: page, limit: limit, barcodeCoding: "",materialName:"", enterpriseId: undefined, areaId: undefined, gridId: undefined };
        },
        create(formName) {
          const set = this.$refs;
          set[formName].validate((valid,errors) => {
            if (valid) {
              this.dialogFormVisible = true;
              let param = new FormData();
              param.append('formData', parseForm(this.form));
              if(this.productionCertificateFile.length>0)
                param.append("productionCertificateFile", this.productionCertificateFile[0].raw);

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
              return false;
            }
          });
        },
        cancel() {
          this.dialogFormVisible = false;
          this.resetTmp();
        },
        update(formName) {
          const set = this.$refs;
          set[formName].validate((valid,errors) => {
            if (valid) {
              this.dialogFormVisible = true;
              let param = new FormData();
              param.append('formData', parseForm(this.form));
              if(this.productionCertificateFile.length>0 && this.productionCertificateFile[0].raw )
                param.append("productionCertificateFile", this.productionCertificateFile[0].raw);

              updateObj(param).then(() => {
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
              return false;
            }
          });
        },
        handleView(row) {
          this.form=row;
        //  getObj(row.inspectionReportId).then(response => {
           // this.form = response.data;
             let resourceId = this.form.inspectionReportPhoto;
              getResource(resourceId).then(response => {
                this.imgFile=[];
                this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
                $(".el-upload--picture-card").hide();
              })
             this.manager_update = false;
            this.viewReadOnly=true;
            this.dialogFormVisible = true;
            this.dialogStatus = "view";
        //  });

        },
        handleUpdate(row) {
          getObj(row.materialId).then(response => {
            this.manager_update = true;
            this.form = response.data;
              let resourceId = this.form.inspectionReportPhoto;
              getResource(resourceId).then(response => {
                this.imgFile=[];
                this.imgFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
                $(".el-upload--picture-card").hide();
              })
             this.viewReadOnly=false;
            this.dialogFormVisible = true;
            this.dialogStatus = "update";
          });
        },
        handleCreate(row) {
          this.form.staffName=row.staffName;
          this.form.employeeNumber=row.employeeNumber;
          this.manager_update = true;
          this.dialogStatus = "create";
          this.viewReadOnly=false;
          this.dialogFormVisible = true;
        },
        //关闭弹出框时，清除表单验证，并且设置表单元素为空
        closeDialog(){
          this.resetTmp();
        },
        materialCategoryFormatter( row, column, cellValue ) {
          return parseValueToText(cellValue, this.materialCategoryOptions);
        },
        enterpriseFormatter( row, column, cellValue ) {
          return parseValueToText(cellValue, this.enterpriseMap);
        },
        resetTmp() {
          this.form={ materialName:undefined};
          this.productionCertificateFile=[];
          $(".el-upload--picture-card:eq(0)").show();
          $(".el-icon-delete:eq(0)").show();
          this.$nextTick(function() {
            this.$refs['form'].clearValidate();
          });
        },
        getSelection(selection) {
          this.selection = selection;
        },
        //删除图片
        handleImageRemove(file){
          this.productionCertificateFile=[];
          if( this.productionCertificateFile.length==0){
            $(".el-upload--picture-card:eq(0)").show();
          }
        },
        // 点击"+"图标时显示弹出框
        handlePictureCardPreview(file) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
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
          this.productionCertificateFile.push(file);
          if( this.productionCertificateFile.length>0){
            $(".el-upload--picture-card:eq(0)").hide();
          }
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
        tableStop: function () {  // 0停用
          this.stopOrStart(0);
        },
        tableStart: function () {  // 1启用
          this.stopOrStart(1);
        },
        statusFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.staticData["状态"]);
        },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.selection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.selection.forEach(item => {
            if(item.materialStatus==1){
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
          this.selection.forEach(item => {
            if(item.materialStatus==0){
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
          this.selection.forEach((item)=>{
            ids.push(item.materialId);
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
    }
</script>
