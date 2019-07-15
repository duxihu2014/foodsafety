<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
            <el-form-item label="姓名"  class="filter-item">
              <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.staffName"> </el-input>
            </el-form-item>

          <el-form-item label="证件号码"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.idCardNo"> </el-input>
          </el-form-item>

          <el-form-item label="状态"  class="filter-item">
                <el-select  v-model="listQuery.validStatus" placeholder="请选择" clearable>
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

    <el-table   :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="员工编号" width="150" prop="staffId" ></el-table-column>
      <el-table-column align="center" label="姓名" width="150" prop="staffName" ></el-table-column>
      <el-table-column align="center" label="证件号码" width="200" prop="idCardNo" ></el-table-column>
      <el-table-column align="center" label="所属部门" width="150" prop="department" ></el-table-column>
      <el-table-column align="center" label="工号" width="150" prop="employeeNumber" ></el-table-column>
      <el-table-column align="center" label="健康证号" width="150" prop="certificateNumber" ></el-table-column>
      <el-table-column align="center" label="有效截至日期" width="200" prop="validDate" >
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.validDate }}</span>
          <el-tag size="mini" :type="getValidDateType(scope.row.validStatus)" prop="validDate"  v-show="scope.row.validStatus!=1">{{statusFormatter(scope.row.validStatus)}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column  align="center" fixed="right" label="操作" width="250"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)" v-if="scope.row.validStatus!=3">编辑</el-button>
          <el-button size="mini" type="danger"  @click="handleCreate(scope.row)" v-if="scope.row.validStatus!=1">更换</el-button>
        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="250"  v-else>
        <template slot-scope="scope">
         <el-button size="mini" type="primary"  @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)" v-if="scope.row.validStatus!=3">编辑</el-button>
          <el-button size="mini" type="danger"  @click="handleCreate(scope.row)" v-if="scope.row.validStatus!=1">更换</el-button>
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
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-tabs >
          <el-tab-pane label="健康证信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="staffName" >
                  <el-input v-model.trim="form.staffName" placeholder=""   :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工号" prop="employeeNumber" >
                  <el-input  v-model.trim="form.employeeNumber"  :readonly="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="健康证号" prop="certificateNumber"  ref="certificateNumber" tab="1">
                  <el-input   v-model.trim="form.certificateNumber"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="体检时间" prop="examinationDate"  ref="examinationDate" tab="1">
                  <el-date-picker v-model="form.examinationDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
               <el-col :span="12">
                <el-form-item label="有效期" prop="validDate"  ref="validDate" tab="1">
                  <el-date-picker v-model="form.validDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions2"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发证时间" prop="issuingDate"  ref="issuingDate" tab="1">
                  <el-date-picker v-model="form.issuingDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item label="发证机构" prop="issuingUnit"  ref="issuingUnit" tab="1">
                    <el-input   v-model.trim="form.issuingUnit"    :readonly="viewReadOnly"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item label="健康证照片" prop="imageFile"  ref="imageFile" tab="1">

                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :file-list="listFile"
                      :on-preview="handlePictureCardPreview"
                      :on-remove="handleImageRemove"
                      :on-change="addImgFile"
                      :limit="1"
                      :auto-upload="false">
                      <i class="el-icon-plus"></i>
                    </el-upload>

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

  import { page, add,update, getObj} from "api/admin/staff/certificate/index";
  import { parseValueToText } from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import {getToken} from 'utils/auth';

  export default {
        name: "index",
        data(){
          const fileValidator =(rule, value, callback) =>{
            if(this.listFile.length<1){
              callback(new Error('请选择健康证照片'));
            }else{
              callback();
            }
          };
          return{
            height:undefined,
            list: null,
            total: null,
            listLoading: false,
            listQuery: {
              page: 1,
              limit: 20,
              staffName: "",
              idCardNo: "",
              validStatus:""
            },
            form: {
              staffId:undefined,
              imageFile:undefined
            },
            rules: {
                certificateNumber: [{required: true, message: "请输入健康证号", trigger: "blur"}],
                examinationDate: [{required: true, message: "请输入体检时间", trigger: "blur"}],
                validDate: [{required: true, message: "请输入有效截至日期", trigger: "blur"}],
                issuingDate: [{required: true, message: "请输入发证日期", trigger: "blur"}],
                issuingUnit: [{required: true, message: "请输入发证单位", trigger: "blur"}],
                imageFile: [{required: true,validator:fileValidator,message: "请选择健康证照片", trigger: "blur"} ]
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
            listFile:[],
            serverImageUrl:process.env.SERVERIMAGEURL,
            tabPosition: "0",
            dialogImageUrl: '',
            dialogImageVisible: false,
            fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
            pickerOptions: {
              disabledDate(time) {
                return time.getTime() > Date.now();
              },
            },
            pickerOptions2: {
              disabledDate(time) {
                return time.getTime() < Date.now();
              },
            }
          }
        },
      created() {
        this.getList();
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
        this.height=loadGridHeight();
      },
      computed: {
        ...mapGetters(["staticData","user"]),
        statusOptions() {
          return this.staticData["健康证状态"];
        },
        educationOptions(){
          return this.staticData["学历"];
        },
        sexOptions(){
          return this.staticData["性别"];
        },
        workTypeOptions(){
          return this.staticData["行业工种"];
        }

      },
      methods: {
        imageClick(){
          this.dialogVisible=true;
          this.dialogImageUrl=this.imageUrl;
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
          this.listQuery.staffName="";
          this.listQuery.idCardNo = "";
          this.listQuery.validStatus="";
        },

        create(formName) {
          const set = this.$refs;
          set[formName].validate((valid,errors) => {
            if (valid) {
              // console.log(this.form)

              let param = new FormData();
              if(this.form.imageFile){
                  param.append("imageFile", this.form.imageFile);
              }
              for(let item in this.form){
                if(this.form.hasOwnProperty(item) && item !== 'imageFile'){
                  if(this.form[item])
                    param.append(item, this.form[item]);
                }
              }
              param.append("enterpriseId",this.user.enterpriseId);
               add(param).then(() => {
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
               let param = new FormData();
              if(this.form.imageFile){
                param.append("imageFile", this.form.imageFile);
              }
              for(let item in this.form){
                if(this.form.hasOwnProperty(item) && item !== 'imageFile'){
                  if(this.form[item])
                    param.append(item, this.form[item]);
                }
              }
               update(param).then(() => {
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
          getObj(row.staffId).then(response => {
            this.form = response.data;
            if(response.data.certificatePhoto) {
              this.listFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              this.$nextTick(function () {
                $(".el-upload--picture-card:eq(0)").hide();
                $(".el-icon-delete:eq(0)").hide();
              });
            }
            this.manager_update = false;
            this.viewReadOnly=true;
            this.dialogFormVisible = true;
            this.dialogStatus = "view";
          });

        },
        handleUpdate(row) {
          getObj(row.staffId).then(response => {
            this.manager_update = true;
            this.form = response.data;
            if(response.data.certificatePhoto) {
              this.listFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              this.$nextTick(function () {
                $(".el-upload--picture-card:eq(0)").hide();
              });
            }
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
          this.form.staffId=row.staffId;

        },
        //关闭弹出框时，清除表单验证，并且设置表单元素为空
        closeDialog(){
          this.resetTmp();
          this.tabPosition='0';
        },
        statusFormatter( cellValue) {
          return parseValueToText(cellValue, this.statusOptions);
        },
        sexFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.sexOptions);
        },
        resetTmp() {
          if (this.$refs["form"]) {
            this.$refs["form"].resetFields();
            this.form={ staffId:undefined, imageFile:undefined};
            this.dialogImageUrl="";
            this.listFile=[];
            $(".el-upload--picture-card:eq(0)").show();
            $(".el-icon-delete:eq(0)").show();
          }
        },
        getSelection(selection) {
          this.selection = selection;
        },
        //删除图片
        handleImageRemove(file){
          this.listFile=[];
          if( this.listFile.length==0){
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
          this.$refs["imageFile"].resetField();//当添加图片后，重置图片验证功能。
          this.listFile.push(file);
          this.form.imageFile=file.raw;
          if( this.listFile.length>0){
            $(".el-upload--picture-card:eq(0)").hide();
          }
        }
      }
    }
</script>

