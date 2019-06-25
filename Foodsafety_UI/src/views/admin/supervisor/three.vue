<template>
  <div class="app-container calendar-list-container">

    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="姓名"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.personnelName"> </el-input>
          </el-form-item>

          <el-form-item label="身份证号"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.idCardNo"> </el-input>
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

    <el-table   :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="人员编号" width="80" prop="personnelId"></el-table-column>
      <el-table-column align="center" label="人员姓名" width="150" prop="personnelName"></el-table-column>
      <el-table-column align="center" label="人员类型" width="150" prop="personnelType" :formatter="personnelTypeFormatter"></el-table-column>
      <el-table-column align="center" label="身份证号" width="200" prop="idCardNo" ></el-table-column>
      <el-table-column align="center" label="工作单位" width="200" prop="unit" ></el-table-column>
      <el-table-column align="center" label="人员状态" width="100" prop="personnelStatus" :formatter="personnelStatusFormatter"></el-table-column>

      <el-table-column  align="center" fixed="right" label="操作" width="250"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)"  >编辑</el-button>
          <el-button size="mini"  type="primary" @click="restpassword(scope.row)">重置密码</el-button>

        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="250"  v-else>
        <template slot-scope="scope">
          <el-button size="mini"  type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)" >编辑</el-button>
          <el-button size="mini"  type="primary" @click="restpassword(scope.row)">重置密码</el-button>
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
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="人员类型" prop="personnelType"   >
                  <el-select  v-model="form.personnelType" placeholder="请选择" clearable :disabled="viewReadOnly">
                    <el-option v-for="item in  personnelTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="人员姓名" prop="personnelName" >
                  <el-input v-model.trim="form.personnelName" placeholder=""  :readonly="viewReadOnly" ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>

              <el-col :span="12">
                <el-form-item label="身份证号" prop="idCardNo"  >
                  <el-input   v-model.trim="form.idCardNo"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactNumber"  >
                  <el-input   v-model.trim="form.contactNumber"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="工作单位" prop="unit"  >
                  <el-input   v-model.trim="form.unit"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="通讯地址" prop="postalAddress"  >
                  <el-input   v-model.trim="form.postalAddress"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                 <el-form-item label="登录用户名" prop="userName" >
                    <el-input v-if="dialogStatus == 'create'" v-model.trim="form.userName" placeholder=""></el-input>
                    <el-input v-else v-model.trim="form.userName" placeholder="" readonly></el-input>
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




  </div>
</template>

<script>

  import { page, addObj, putObj, getObj,changeStatus} from "api/admin/supervisory/three";
  import { parseValueToText ,parseForm} from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import {getToken} from 'utils/auth';
  import idCardNoUtil from 'utils/idCardNoUtil';
  import { checkUserName} from "api/admin/user/index";
  import { resetUserPwd} from "api/admin/user/index";

  export default {
    name: "index",
    data(){
       let vaildateIdCard=(rule, idCardNo, callback)=>{
        //15位和18位身份证号码的基本校验
        if(!idCardNo) callback() ;
        let check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
        if(!check) return callback(new Error('请输入正确的身份证号码'));
        //判断长度为15位或18位
        if(idCardNo.length==15){
          if(!idCardNoUtil.check15IdCardNo(idCardNo)){
            callback(new Error('请输入正确的身份证号码'));
          }else callback();

        }else if(idCardNo.length==18){
          if(!idCardNoUtil.check18IdCardNo(idCardNo))
            callback(new Error('请输入正确的身份证号码'));
          else callback();
        }else{
          callback(new Error('请输入正确的身份证号码'));
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
      const userNameValidator = (rule, value, callback) => {
        if(this.dialogStatus == "create" ){
          checkUserName({"userName":value}).then(response => {
            if( !response.rel){
              return callback(new Error("用户名重复"));
            }else{
              callback();
            }
          })
        }else{
          callback();
        }
      };
      return{
        height:undefined,
        list: null,
        total: null,
        listLoading: false,
        suppliersList:[],//供货商
        listQuery: {
          page: 1,
          limit: 20,
          personnelName: "",
          idCardNo: ""
        },
        form: {
          personnelName:undefined
        },
        rules: {
          personnelName: [{required: true, message: "请输入人员姓名", trigger: "blur"}],
          personnelType: [{required: true, message: "请选择人员类型", trigger:  "change"}],
          idCardNo: [{required: true, message: "请输入身份证号", trigger: "blur"},
            { validator: vaildateIdCard, trigger: "blur" }],
          unit: [{required: true, message: "请输入单位", trigger: "blur"}],
          contactNumber: [{required: true, message: "请输入联系电话", trigger: "blur"},
            {max: 16, message: "长度不能超过16个字符", trigger: "blur"},
            { validator: mobileValidator, trigger: "blur" }],
          userName: [{required: true, message: "请输入用户名", trigger: "blur"},
            {validator: userNameValidator, trigger: "blur" },
            {max: 32, message: "长度不能超过32个字符", trigger: "blur"}]
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
      personnelTypeOptions(){
        return this.staticData["三员类型角色编号设置"];
      },
      personnelStatusOptions(){
        return this.staticData["状态"];
      }
    },
    methods: {
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
        this.listQuery.idCardNo="";
        this.listQuery.personnelName = "";
      },
      create(formName) {
        const set = this.$refs;
        set[formName].validate((valid,errors) => {
          if (valid) {
            addObj(this.form).then(() => {
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
            putObj(this.form.personnelId,this.form).then(() => {
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
        getObj(row.personnelId).then(response => {
          this.form = response.data;
          this.form.userName=row.userName;
          this.manager_update = false;
          this.viewReadOnly=true;
          this.dialogFormVisible = true;
          this.dialogStatus = "view";
        });

      },
      handleUpdate(row) {
        getObj(row.personnelId).then(response => {
          this.manager_update = true;
          this.form = response.data;
          this.form.userName=row.userName;
          this.viewReadOnly=false;
          this.dialogFormVisible = true;
          this.dialogStatus = "update";
        });
      },
      handleCreate(row) {
        this.manager_update = true;
        this.dialogStatus = "create";
        this.viewReadOnly=false;
        this.dialogFormVisible = true;
      },
      //关闭弹出框时，清除表单验证，并且设置表单元素为空
      closeDialog(){
        this.resetTmp();
      },
      personnelStatusFormatter( row, column, cellValue ) {
        return parseValueToText(cellValue, this.personnelStatusOptions);
      },
      personnelTypeFormatter( row, column, cellValue ) {
        return parseValueToText(cellValue, this.personnelTypeOptions);
      },

      resetTmp() {
        this.form={ personnelName:undefined};
        this.$nextTick(function() {
          this.$refs['form'].clearValidate();
        });
      },
      getSelection(selection) {
        this.selection = selection;
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
            if(item.personnelStatus==1){
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
            if(item.personnelStatus==0){
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
          let userIds = [];
          this.selection.forEach((item)=>{
            ids.push(item.personnelId);
            userIds.push(item.userId);
          });
          ids= ids.length>1?ids.join(','):ids[0];
          userIds= userIds.length>1?userIds.join(','):userIds[0];
          let param = {"ids":ids, "status":num,"userIds":userIds};
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
      },
      restpassword(row){
        this.$confirm('确认重置此用户密码吗?', '确认信息', {
          distinguishCancelAndClose: true,
          confirmButtonText: '确认',
          cancelButtonText: '放弃'
        }).then(() => {
          resetUserPwd({userId:row.userId}).then(response=>{
            if(response.rel){
              this.$message({
                type:'success',
                showClose: true,
                message: "重置密码成功！"
              });
            }else{
              this.$message({
                type:'error',
                showClose: true,
                message: "重置密码失败！"
              });
            }
          });
        })
          .catch(action => { });
      }
    }
  }
</script>
