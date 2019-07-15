<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <queryConditions @setAreaId="setAreaId" @setGridId="setGridId" @setEnterpriseId="setEnterpriseId" :areaId="listQuery.areaId" :gridId="listQuery.gridId" :enterpriseId="listQuery.enterpriseId"></queryConditions>


          <el-form-item label="姓名"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.staffName"> </el-input>
          </el-form-item>

          <el-form-item label="证件号码"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.idCardNo"> </el-input>
          </el-form-item>

          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.staffStatus" placeholder="请选择" clearable>
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
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="员工编号" width="100" prop="staffId"></el-table-column>
      <el-table-column align="center" label="姓名" width="150" prop="staffName"></el-table-column>
      <el-table-column align="center" label="性别" width="100" prop="sex" :formatter="sexFormatter"></el-table-column>
      <el-table-column align="center" label="证件类型" width="150" prop="idType" :formatter="idTypeFormatter"></el-table-column>
      <el-table-column align="center" label="证件号码" width="200" prop="idCardNo" ></el-table-column>
      <el-table-column align="center" label="所属部门" width="150" prop="department" ></el-table-column>
      <el-table-column align="center" label="工号" width="150" prop="employeeNumber" ></el-table-column>
      <el-table-column align="center" label="员工状态" width="100" prop="staffStatus" :formatter="statusFormatter"></el-table-column>
      <el-table-column  align="center" fixed="right" label="操作" width="100"  v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="100"  v-else>
        <template slot-scope="scope">

          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
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
      <el-form :model="form"  ref="form" label-width="120px">
        <el-tabs v-model="tabPosition">
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="staffName" ref="staffName" tab="0">
                  <el-input v-model.trim="form.staffName" placeholder=""   :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="性别" prop="sex"  ref="sex" tab="0">
                  <el-radio-group v-model="form.sex"  :disabled="viewReadOnly"  >
                    <el-radio v-for="item in  sexOptions"  :key="item.value" :label="item.value" >{{item.text}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="证件类型" prop="idType">
                  <el-select  v-model="form.idType" placeholder="请选择" :clearable="true">
                    <el-option v-for="item in idTypeOptions" :readonly="viewReadOnly" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证件号码" prop="idCardNo" ref="idCardNo" tab="0">
                  <el-input  v-model.trim="form.idCardNo"  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="出生日期" prop="birthDate"   ref="birthDate" tab="0">
                  <el-date-picker
                    v-model="form.birthDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    placeholder="选择日期"
                    :picker-options="pickerOptions"  :readonly="viewReadOnly">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="住址" prop="homeAddress">
                  <el-input v-model.trim="form.homeAddress" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="学历" prop="education"  >
                  <el-select  v-model="form.education" placeholder="请选择" clearable   :readonly="viewReadOnly" >
                    <el-option v-for="item in  educationOptions" :key="item.value" :label="item.text" :value="item.value"  :disabled="viewReadOnly"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="专业" prop="major">
                  <el-input v-model.trim="form.major" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>


            <el-row>
              <el-col :span="12">
                <el-form-item label="部门" prop="department">
                  <el-input v-model.trim="form.department" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工号" prop="employeeNumber" ref="employeeNumber" tab="0" >
                  <el-input   v-model.trim="form.employeeNumber"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="工种" prop="workType"  ref="workType" tab="0">
                  <el-select  v-model="form.workType" placeholder="请选择" clearable   :disabled="viewReadOnly" >
                    <el-option v-for="item in  workTypeOptions" :key="item.value" :label="item.text" :value="item.value" > </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="入职日期" prop="employmentDate"  ref="employmentDate" tab="0">

                  <el-date-picker v-model="form.employmentDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>

                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactNumber">
                  <el-input v-model.trim="form.contactNumber" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="员工状态" prop="staffStatus" ref="staffStatus" tab="0">
                  <el-select  v-model="form.staffStatus" placeholder="请选择" clearable   :disabled="viewReadOnly">
                    <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>


            </el-row>

          </el-tab-pane>
          <el-tab-pane label="健康证信息">
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
                    :limit="1"
                    :auto-upload="false">
                    <i class="el-icon-plus"></i>
                  </el-upload>

                  <el-dialog  :visible.sync="dialogVisible"   append-to-body  >
                    <img width="100%" height="100%" :src="dialogImageUrl" ref="imgDialog" >
                  </el-dialog>

                </el-form-item>
              </el-col>
            </el-row>
            </el-row>

          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="institutionManager_update">
        <el-button @click="cancel()">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import { page, save, getObj, changeStatus, putObj} from "api/admin/staff/index";
  import { parseValueToText } from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import idCardNoUtil from 'utils/idCardNoUtil';
  import {getToken} from 'utils/auth';
  import queryConditions from "components/QueryConditions/index";

  export default {
    name: "index",
    components: {
      queryConditions
    },
    data(){
      const mobileValidator = (rule, value, callback) => {
        const mymobile = /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
        if (value && !mymobile.test(value)) {
          return callback(new Error("电话号格式不正确"));
        } else {
          callback();
        }
      };
      let vaildateIdCard=(rule, idCardNo, callback)=>{
        //15位和18位证件号码的基本校验
        if(!idCardNo) callback() ;
        let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
        if(!check) return callback(new Error('请输入正确的证件号码'));
        //判断长度为15位或18位
        if(idCardNo.length==15){
          if(!idCardNoUtil.check15IdCardNo(idCardNo)){
            callback(new Error('请输入正确的证件号码'));
          }else callback();

        }else if(idCardNo.length==18){
          if(!idCardNoUtil.check18IdCardNo(idCardNo))
            callback(new Error('请输入正确的证件号码'));
          else callback();
        }else{
          callback(new Error('请输入正确的证件号码'));
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
          staffStatus:"",
          areaId: undefined,
          gridId: undefined,
          enterpriseId: undefined
        },
        form: {
          staffId:undefined,
          imageFile:undefined
        },

        dialogFormVisible: false,
        viewReadOnly:false, //控制查看时的按钮显示
        dialogStatus: "",
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        institutionManager_update:true,
        institutionSelection: [],
        needFixedRight:false,
        imageUrl:undefined,
        listFile:[],
        serverImageUrl:process.env.SERVERIMAGEURL,
        tabPosition: "0",
        dialogImageUrl: '',
        dialogVisible: false,
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
    },
    updated () {
      this.height = loadGridHeight();
    },
    computed: {
      ...mapGetters(["staticData","user"]),
      statusOptions() {
        return this.staticData["员工状态"];
      },
      educationOptions(){
        return this.staticData["学历"];
      },
      sexOptions(){
        return this.staticData["性别"];
      },
      workTypeOptions(){
        return this.staticData["行业工种"];
      },
      idTypeOptions(){
        return this.staticData["证件类型"];
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
        this.listQuery.staffStatus="";
        this.listQuery.enterpriseId=undefined;
        this.listQuery.areaId=undefined;
        this.listQuery.gridId=undefined;
      },


      cancel() {
        this.dialogFormVisible = false;
        this.resetTmp();
      } ,
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
          this.institutionManager_update = false;
          this.viewReadOnly=true;
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
        });

      },

      //关闭弹出框时，清除表单验证，并且设置表单元素为空
      closeDialog(){
        this.resetTmp();
        this.tabPosition='0';
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.statusOptions);
      },
      sexFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.sexOptions);
      },
      getAreaName(){
        return this.listQuery.areaName;
      },
      idTypeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.idTypeOptions);
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
        this.institutionSelection = selection;
      },

      // 点击"+"图标时显示弹出框
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
      }
    }
  }
</script>

