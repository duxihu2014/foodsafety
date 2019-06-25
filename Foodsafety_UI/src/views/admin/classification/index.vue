<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>
          <el-form-item label="监管级别" class="filter-item">
            <el-select  v-model="listQuery.superviseClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="主体分类" class="filter-item">
            <el-select  v-model="listQuery.subjectClassification" placeholder="请选择" clearable filterabler>
              <el-option v-for="(item, index) in subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="营业执照" prop="certificateNo" class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.certificateNoLike"> </el-input>
          </el-form-item>-->
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves @click="handleFilter">搜索</el-button>
            <el-button v-waves @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border highlight-current-row style="width: 100%" @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="监管等级" width="100" prop="superviseClassification"  >
        <template slot-scope="scope">
          <el-tag >
            {{superviseClassificationFormatter(scope.row.superviseClassification)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180" prop="organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="主体分类" width="180" prop="subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="营业执照编号" width="180" prop="certificateNo" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="企业状态" width="100" prop="enterpriseStatus" :formatter="enterpriseStatusFormatter"></el-table-column>

      <el-table-column align="center" label="操作" width="200"   :fixed="needFixedRight?'right':null">
        <template slot-scope="scope">
          <el-button  size="mini" type="primary" @click="handleUpdate(scope.row)">变更</el-button>
          <el-button  size="mini" type="success" @click="handleShow(scope.row)">变更记录</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  title="编辑" :visible.sync="dialogVisible" @close="close" :close-on-click-modal="false">
      <div slot="title">
           <i class="el-icon-edit">编辑</i>
      </div>

      <el-form :model="form" :rules="rules" ref="form" label-width="120px"  >
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="enterpriseName" >
             {{form.enterpriseName}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前监管等级" prop="beforeClassification" >
              {{superviseClassificationFormatter(form.beforeClassification)}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="变更监管等级" prop="afterClassification">
              <el-select  v-model="form.afterClassification" placeholder="请选择" clearable filterabler>
                <el-option v-for="(item, index) in superviseClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="变更原因" prop="cause" >
              <el-input type="textarea" :rows="3"  v-model="form.cause"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="依据" prop="gist">
              <el-input type="textarea" :rows="3"  v-model="form.gist"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="图片依据" prop="imageFile"  ref="imageFile" >

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

        <el-row>
          <el-col :span="24">
            <el-form-item label="变更说明" prop="comment" >
              <el-input type="textarea" :rows="3"  v-model="form.comment"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer" >
        <enterpriseNameel-button @click="cancel('form')">取 消</enterpriseNameel-button>
         <el-button  type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  title="监管等级变更记录" :visible.sync="changeRecordDialogVisible"  center>
      <spen style="font-size: 16px;">{{enterpriseName}}</spen>

      <div v-if="changeRecords.length==0" style="height: 50px;line-height: 50px; color: #aaa;text-align: center;border-top: 1px solid #b1b1b1">
             无变更记录
      </div>

      <div v-for="(record, index) in changeRecords" style="padding-bottom: 10px;padding-top: 10px;border-top: 1px solid #b1b1b1">
           <el-row>
             <el-col :span="12"> 日期：{{record.operTime}}</el-col>
            <el-col :span="12"> 操作人：{{record.userName}}</el-col>
          </el-row>
          <el-row>
            <el-col :span="12">变更前监管等级：{{superviseClassificationFormatter(record.beforeClassification)}}</el-col>
            <el-col :span="12">变更后监管等级：{{superviseClassificationFormatter(record.afterClassification)}}</el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              变更原因：{{record.cause}}
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">变更依据：{{record.gist}}</el-col>
          </el-row>
          <el-row v-if="record.imgFile && record.imgFile.length>0">
            <el-col :span="24">
                 图片依据:  <el-button size="mini" type="primary" plain @click="handlePictureCardPreview(record.imgFile[0])">点击查看</el-button>
             </el-col>
          </el-row>

        <el-row>
          <el-col :span="24">变更说明：{{record.comment}}</el-col>
        </el-row>

      </div>

    </el-dialog>


    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </div>
</template>
<script>
  import { changeClassification,changeRecord } from "api/admin/classification/index";

  import { getEnterpriseAllInfo, getResource } from "api/admin/enterprise/index";
  import queryConditions from "components/QueryConditions/index";
  import { mapGetters } from "vuex";
  import { parseValueToText, parseTime, findParentNode,parseForm } from "utils/index";
  import { loadGridHeight } from "api/screen";
  export default {
    name: "verify",
    components: {
      queryConditions,

    },
    data() {
      const vailClassification = (rule, value, callback) => {
        if (this.form.beforeClassification==value) {
          return callback(new Error("监管等级与原等级相同，无需变更！"));
        } else {
          callback();
        }
      };
      return {
        height: undefined,
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          superviseClassification: undefined,
          subjectClassification: undefined,
          certificateNoLike: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined,
          page: 1,
          limit: 20,
        },
        tableKey: 0,
        enterpriseSelection: [],
        form: {
          cause:undefined,
          gist:undefined,
          comment: undefined,
          beforeClassification:undefined,
          afterClassification:undefined
        },
        rules:{
          cause:[{required: true, message: '请输入变更原因',trigger: 'blur'},{ max: 128, message: "长度不能超过128个字符", trigger: "blur"}],
          gist:[{required: true, message: '请输入变更依据',trigger: 'blur'},{ max: 128, message: "长度不能超过128个字符", trigger: "blur"}],
          afterClassification:[{required: true, message: '请选择变更等级',trigger: 'blur'},{validator:vailClassification,message: '监管等级与原等级相同，无需变更！',trigger:["blur","change" ] }],
          comment:[{ max: 128, message: "长度不能超过128个字符", trigger: "blur"}]
        },
        dialogVisible: false,
        needFixedRight:false,
        listFile:[],
        fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
        serverImageUrl:process.env.SERVERIMAGEURL,
        changeRecords:[],
        changeRecordDialogVisible:false,
        dialogImageUrl: '',
        dialogImageVisible: false,
        enterpriseName:""
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
      /*economicNatureOptions() {
        return this.staticData["企业经济性质"];
      }*/
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
      this.height = loadGridHeight();
    },
    created() {
      this.getList();
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
      getList() {
        if (this.listQuery.areaId == undefined)
          this.listQuery.areaId = this.user.areaId;
        getEnterpriseAllInfo(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      handleShow(row){
        this.enterpriseName=row.enterpriseName;
         changeRecord(row.enterpriseId).then(response => {
           this.changeRecords = response.data;
           this.changeRecords.forEach(item=>{
             if(item.resourceId){
               item.imgFile=[{"url":this.serverImageUrl+'/'+item.resourcePath}];
             }
           });
           this.changeRecordDialogVisible = true;

         });
      },
      handleUpdate(row) {
        this.form.enterpriseId=row.enterpriseId;
        this.form.enterpriseName=row.enterpriseName;
        this.form.beforeClassification=row.superviseClassification;
        this.dialogVisible=true;
      },
      handleFilter() {
        this.listQuery.page = 1;
        this.getList();
      },
      resetQuery() {
        let page = this.listQuery.page;
        let limit = this.listQuery.limit;
        this.listQuery = { page: page, limit: limit,
          superviseClassification: undefined,
          subjectClassification: undefined,
          certificateNoLike: undefined,
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined };
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      getSelection(selection) {
        this.enterpriseSelection = selection;
      },
      tableRowClassName({ row, rowIndex }) {
        if (row.compStatus === '0') {
          return 'warning-row';
        } else if (row.compStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      //取消提交
      cancel(formName) {
        this.dialogVisible = false;
        this.$refs[formName].resetFields();
      },
      close() {
        this.listFile=[];
        this.form={};
        $(".el-upload--picture-card:eq(0)").show();
        this.$nextTick(function () {
          this.$refs['form'].clearValidate();
        });
      },
      update(formName){
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {

            let param = new FormData();
            if(this.listFile.length>0){
              param.append("photoImage", this.listFile[0].raw);
            }else{
              this.form.photoImage=undefined;
            }
            param.append("detail", parseForm(this.form));

            changeClassification(param).then(() => {
              this.dialogVisible = false;
              this.getList();
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
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
        this.listFile.push(file);
        if( this.listFile.length>0){
          $(".el-upload--picture-card:eq(0)").hide();
        }
      },

      //企业主体分类格式化
      subjectClassificationFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["企业主体分类"]);
      },
      registerStatusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["企业注册状态"]);
      },
      enterpriseStatusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['企业状态']);
      },
      superviseClassificationFormatter(cellValue) {
        return parseValueToText(cellValue, this.superviseClassificationOptions);
      },
    }
  }

</script>
<style>
  .enterpriseDialog>.el-dialog--center .el-dialog__body {
    text-align: initial;
    padding: 10px 25px 30px;
  }

  .map_container {
    position: relative;
    width: 100%;
    height: 400px;
  }

</style>
