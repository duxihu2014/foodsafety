<template>
  <div class="app-container calendar-list-container">
    <!-- <div class="filter-container"> -->
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">

          <el-form-item label="所属地区">
            <el-cascader
              :options="areaOptions"
              v-model="querySelectedAreas"
              change-on-select
              @change="queryAreaChange">
            </el-cascader>
          </el-form-item>

           <el-form-item label="所属机构">
            <el-select  v-model="listQuery.institutionId" placeholder="请选择" clearable filterable>
              <el-option v-for="item in queryInstitutionList" :key="item.institutionId" :label="item.institutionName" :value="item.institutionId"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="企业名称" >
            <!--<el-select  v-model="listQuery.enterpriseId" placeholder="请选择" clearable filterable>-->
              <!--<el-option v-for="item in queryEnterpriseList" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId"> </el-option>-->
            <!--</el-select>-->

            <el-autocomplete
              class="inline-input"
              v-model="enterprise"
              :fetch-suggestions="getEnterpriseByName"
              :trigger-on-focus="true"
              :placeholder="placeholder"
              @select="enterpriseChange"
              clearable
            ></el-autocomplete>

          </el-form-item>

          <el-form-item label="用户名" prop="userName"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;"  placeholder=""  v-model.trim="listQuery.userNameLike"> </el-input>
          </el-form-item>
          <el-form-item label="状态" prop="userStatus" class="filter-item">
            <el-select  v-model="listQuery.userStatus" placeholder="请选择" clearable>
              <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户级别" prop="userType" class="filter-item">
            <el-select  v-model="listQuery.userType" placeholder="请选择" clearable>
              <el-option v-for="item in  userTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
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
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="用户编号" width="100" prop="userId"></el-table-column>

      <el-table-column width="110px" align="center" label="用户名" prop="userName"></el-table-column>

      <el-table-column width="110px" align="center" label="用户级别" prop="userType" :formatter="userTypeFormatter"></el-table-column>

      <el-table-column width="120px" align="center" label="角色" prop="roleId" :formatter="roleFormatter"> </el-table-column>

      <el-table-column width="180px" align="center" label="所属机构" prop="institutionName"></el-table-column>

      <el-table-column width="250px" align="center" label="所属企业" prop="enterpriseName" :show-overflow-tooltip="true"></el-table-column>

      <el-table-column width="80px" align="center" label="状态" :formatter="statusFormatter" prop="userStatus"></el-table-column>

      <el-table-column  align="center"  label="操作" width="200" fixed="right"  v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button size="mini"  type="primary" @click="restpassword(scope.row)">重置密码</el-button>
        </template>
      </el-table-column>
      <el-table-column  align="center" label="操作" width="200"   v-else>
        <template slot-scope="scope">
          <el-button v-if="userManager_btn_edit" size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button size="mini"  type="primary" @click="restpassword(scope.row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>


    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible"  @close="closeDialog" :close-on-click-modal="false">
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
        <el-tabs>
          <el-tab-pane label="基本信息">

            <el-row>
              <el-col :span="12">
                <el-form-item label="用户名" prop="userName" >
                  <el-input v-if="dialogStatus == 'create'" v-model.trim="form.userName" placeholder=""></el-input>
                  <el-input v-else v-model.trim="form.userName" placeholder="" readonly></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用户级别" prop="userType">
                  <el-select  v-model="form.userType" placeholder="请选择" clearable  @change="selectUserType">
                    <el-option v-for="item in  userTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="12">
                <el-form-item label="角色" prop="roleId">
                  <el-select  v-model="form.roleId" placeholder="请选择" clearable>
                    <el-option v-for="item in  roleOptions" :key="item.value" :label="item.roleName" :value="item.roleId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>


              <el-col :span="12"  v-if=" form.userType && form.userType != 1 ">
                <el-form-item label="所属地区" prop="areaId">
                  <el-cascader
                    :options="areaOptions"
                    v-model="selectedAreas"
                    change-on-select
                    @change="areaChange">
                  </el-cascader>
                </el-form-item>
              </el-col>

            </el-row>

            <el-row >
                <el-col :span="12"  v-if=" form.userType && form.userType != 1 && form.userType != 4 ">
                  <el-form-item label="所属机构"  prop="institutionId">
                    <el-select  v-model="form.institutionId" placeholder="请选择" clearable filterable>
                      <el-option v-for="item in institutionList" :key="item.institutionId" :label="item.institutionName" :value="item.institutionId"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              <el-col :span="12"  v-if="form.userType == 4 ">
                <el-form-item label="所属企业"  prop="enterpriseId">
                  <el-select  v-model="form.enterpriseId" placeholder="请选择" clearable filterable>
                    <el-option v-for="item in enterpriseList" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId"> </el-option>
                  </el-select>
                </el-form-item>
              </el-col>

            </el-row>

          </el-tab-pane>
        </el-tabs>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="userManager_update">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {fetchTree,getParentsByAreaId} from 'api/admin/area/index';
  import {getEnterprise} from 'api/admin/enterprise/index';
  import { userPage, addObj, getObj, delObjs, putObj ,changeStatus,checkUserName,resetUserPwd} from "api/admin/user/index";
  import { getInstitutions} from "api/admin/institution/index";
  import { getAllRole} from "api/admin/role/index";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  import { loadGridHeight } from "api/screen";
  import {  getEnterpriseByName } from "api/admin/enterprise/base/index";

  export default {
    name: "user",
    components: {
      "user-role": () => import("./components/role"),
      "user-institution": () => import("./components/institution")
    },
    data() {
      const emailValidator = (rule, value, callback) => {
        const myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        if (value && !myreg.test(value)) {
          return callback(new Error("邮箱格式不正确"));
        } else {
          callback();
        }
      };
      const userNameValidator = (rule, value, callback) => {
        if(this.userNameSelected!==value){
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
      const mobileValidator = (rule, value, callback) => {
        const mymobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
        if (value && !mymobile.test(value)) {
          return callback(new Error("手机号格式不正确"));
        } else {
          callback();
        }
      };
      return {
        height:undefined,
        userNameSelected:'',
        institutionList:[],
        role:undefined,
        form: {
          userName: undefined,
          realName: undefined,
          mobile: undefined,
          email: undefined,
          roleId:undefined,
          institutionId:undefined,
          areaId:undefined,
          enterpriseId:undefined,
          userType:undefined
        },
        rules: {
          userName: [{required: true, message: "请输入用户名", trigger: "blur"}, {validator: userNameValidator, trigger: "blur" },{max: 32, message: "长度不能超过32个字符", trigger: "blur"}],
          userType: [{required: true, message: "请选择用户级别", trigger: "blur"}],
          roleId: [{required: true, message: "请选择角色", trigger: "blur"}],
          areaId:[{required: true , message: "请选择地区", trigger: "blur"}],
          enterpriseId:[{required: true, message: "请选择企业", trigger: "blur"}],
          institutionId:[{required:  true, message: "请选择机构", trigger: "blur"}]
        },
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          userNameLike: undefined,
          userType: undefined,
          userStatus: "",
          institutionId:undefined
        },
        querySelectedAreas:[],
        queryInstitutionList:[],
        queryEnterpriseList:[],
        dialogFormVisible: false,
        dialogStatus: "",
        userManager_btn_edit: true,
        userManager_btn_del: true,
        userManager_btn_add: true,
        textMap: {
          update: "编辑",
          create: "创建",
          view: "查看"
        },
        tableKey: 0,
        dialogRoleVisible: false,
        viewReadOnly:false, //控制查看时的按钮显示
        dialogRoleName: "绑定角色",
        dialoginstitutionName: "绑定部门",
        dialoginstitutionVisible:false,
        currentId: -1,
        userManager_update: true,
        userSelection: [],
        needFixedRight:false,
        areaOptions:[],
        selectedAreas:[],
        roleOptions:[],
        enterpriseList:[],//企业列表
        institutionList:[],

        enterpriseOptions: [],
        placeholder: '请输入企业名称',
        enterprise:undefined,
      };
    },
    created() {
      this.roleInit();
      this.getList();
      fetchTree().then(data => {
        //console.log(data)
        this.areaOptions = data;
      });
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
      ...mapGetters(["staticData"]),
      userTypeOptions() {
        return this.staticData["用户级别"];
      },
      statusOptions() {
        return this.staticData["状态"];
      }

    },
    methods: {
      enterpriseChange(item){
         this.listQuery.enterpriseId=item.key;
      },
      getEnterpriseByName(enterpriseName, cb){
        let areaId=undefined;
        if(this.querySelectedAreas.length>0)
          areaId=this.querySelectedAreas[this.querySelectedAreas.length-1];
        getEnterpriseByName({enterpriseName:enterpriseName, finalAreaId:areaId, enterpriseStatus:3}).then((response)=>{
          let results = [];
          this.enterpriseOptions = response;
          this.enterpriseOptions.forEach((item) => {
            results.push({"value": item.enterpriseName, "key": item.enterpriseId});
          });
          if(results.length==0 && this.enterprise && this.enterprise.length>0){
            this.enterprise=this.enterprise.substring(0,this.enterprise.length-1);
            this.getEnterpriseByName(this.enterprise, cb);
          }else
          // 调用 callback 返回建议列表的数据
            cb(results);
        }).catch((e)=>{
          // console.log(e)
          this.$message({
            type:'error',
            showClose: true,
            message: '企业信息获取失败。'
          });
        });
      },
      queryAreaChange(value){
        this.listQuery.institutionId=undefined;
        this.queryInstitutionList=[];
        getInstitutions({areaId:value[value.length-1],institutionStatus:'1'}).then((response)=>{
          this.queryInstitutionList = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '机构信息获取失败。'
          });
        });
        getEnterprise({finalAreaId:value[value.length-1],enterpriseStatus:'3'}).then((response)=>{
          this.queryEnterpriseList = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '企业信息获取失败。'
          });
        });
      },
      areaChange(value){
           getInstitutions({areaId:value[value.length-1],institutionStatus:'1'}).then((response)=>{
            this.institutionList = response;
          }).catch(()=>{
            this.$message({
              type:'error',
              showClose: true,
              message: '机构信息获取失败。'
            });
          });
           getEnterprise({finalAreaId:value[value.length-1],enterpriseStatus:'3'}).then((response)=>{
            this.enterpriseList = response;
          }).catch(()=>{
            this.$message({
              type:'error',
              showClose: true,
              message: '企业信息获取失败。'
            });
          });
       },
      selectUserType(){
        this.selectedAreas=[];
        this.form.institutionId=undefined;
        this.form.enterpriseId=undefined;
        switch (this.form.userType){
          case 1:
            this.rules.areaId[0].required=false;
            this.rules.institutionId[0].required=false;
            this.rules.enterpriseId[0].required=false;
            break;
          case 2 || 3:
            this.rules.areaId[0].required=true;
            this.rules.institutionId[0].required=true;
            this.rules.enterpriseId[0].required=false;
            break;
          case 4:
            this.rules.areaId[0].required=true;
            this.rules.institutionId[0].required=false;
            this.rules.enterpriseId[0].required=true;
            break;
        }

        this.form.roleId="";
        getAllRole({roleStatus:'1',roleType:this.form.userType}).then((response)=>{
          this.roleOptions = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '角色信息获取失败。'
          });
        });
      },
      tableRowClassName({row, rowIndex}) {
        if (row.userStatus === '0') {
          return 'warning-row';
        } else if (row.userStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      getCurrentId() {
        return this.currentId;
      },
      getList() {
        this.listLoading = true;
        if(this.querySelectedAreas.length>0)
          this.listQuery.areaId=this.querySelectedAreas[this.querySelectedAreas.length-1];
        userPage(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
      },
      //获取所有角色
      roleInit(){
        getAllRole({roleStatus:'1'}).then((response)=>{
          this.role = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '角色信息获取失败。'
          });
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
        this.userManager_update = true;
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.userNameSelected="";
        this.roleOptions = [];
      },
      handleView(row) {
        getObj(row.userId).then(response => {
          this.form = response.data;
          this.userManager_update = false;
          this.dialogFormVisible = true;
          this.userNameSelected=response.data.userName;
          this.dialogStatus = "view";
        });
      },
      handleUpdate(row) {

        getObj(row.userId).then(response => {
          this.userManager_update = true;
          this.form = response.data;

          if(this.form.userType!=1)
            getParentsByAreaId(this.form.areaId).then(response=>{
              response.data.forEach(item=>{
                this.selectedAreas.push(item.areaId+"");
              });
              this.areaChange(this.selectedAreas);
            });

          this.userNameSelected=response.data.userName;
          this.dialogFormVisible = true;
          this.dialogStatus = "update";
        });

        getAllRole({roleStatus:'1',roleType:row.userType}).then((response)=>{
          this.roleOptions = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '角色信息获取失败。'
          });
        });


      },
      closeDialog(){
        this.viewReadOnly=false;
        this.form={
            userName: undefined,
            realName: undefined,
            mobile: undefined,
            email: undefined,
            roleId:undefined,
            institutionId:undefined,
            areaId:undefined,
            enterpriseId:undefined,
            userType:undefined
        };
        this.selectedAreas=[];
        this.institutionManager_update = true;
        this.$nextTick(function() {
          this.$refs['form'].clearValidate();
        });
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
      },
      handleDelete() {
        if (!this.userSelection.length) {
          this.$message({
            showClose: true,
            message: "请先选择一条信息！"
          });
          return;
        }
        this.$confirm("此操作将永久删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            const idArr = [];
            this.userSelection.forEach(user => {
              idArr.push(user.userId);
            });
            delObjs(idArr.join(","))
              .then(() => {
                this.$message({
                  showClose: true,
                  message: "删除成功",
                  type: "success",
                  duration: 2000
                });
                this.getList();
              })
              .catch(() => {});
          })
          .catch(() => {});
      },
      create(formName) {
        const set = this.$refs;
        if(this.selectedAreas.length>0){
          this.form.areaId=this.selectedAreas[this.selectedAreas.length-1];
        }
        set[formName].validate(valid => {
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
      cancel(formName) {
        this.dialogFormVisible = false;
        this.$refs[formName].resetFields();
      },
      update(formName) {
        const set = this.$refs;
        //console.log(this.selectedAreas)
        if(this.selectedAreas.length>0){
          this.form.areaId=this.selectedAreas[this.selectedAreas.length-1];
        }
        set[formName].validate(valid => {
          if (valid) {
            putObj(this.form.userId, this.form).then(() => {
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
      resetTmp() {
        this.form = {
          userId: undefined,
          userName: undefined,
          realName: undefined,
          sex: "1",
          email: undefined,
          mobile: undefined
        };
        //  this.$refs["form"].resetFields();
        this.$nextTick(function() {
          this.$refs["form"].resetFields();
          //console.log(this.form.userPwd);
        });

        /*  if (this.$refs["form"]) {
          this.$refs["form"].resetFields();
        } */
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["状态"]);
      },
      sexFormatter: function(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["性别"]);
      },
      roleFormatter(row, column, cellValue){
        let result ="";
        this.role.forEach((item)=>{
          if(cellValue==item.roleId){
            result=item.roleName;
          }
        })
        return result;
      },
      userTypeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue,  this.userTypeOptions);
      },
      institutionFormatter(row, column, cellValue){
        let result ="";
        this.institution.forEach((item)=>{
          if(cellValue==item.institutionId){
            result=item.institutionName;
          }
        })
        return result;
      },
      closeRoleDialog() {
        this.dialogRoleVisible = false;
      },
      closeinstitutionDialog(){
        this.dialoginstitutionVisible = false;
      },
      resetQuery() {
        this.listQuery.userNameLike= "";
        this.listQuery.userStatus= "";
        this.listQuery.userType="";
        this.listQuery.areaId=undefined;
        this.querySelectedAreas=[];
        this.listQuery.institutionId=undefined;
        this.listQuery.enterpriseId=undefined;
        this.enterprise=undefined;
        this.queryInstitutionList=[];
        this.queryEnterpriseList=[];
      },
      handlerRole() {
        if (this.userSelection.length === 0 || this.userSelection.length > 1) {
          this.$message({
            showClose: true,
            message: "只能选择一条信息！"
          });
          return;
        }

        this.currentId = this.userSelection[0].userId;
        if (this.currentId > -1) {
          this.dialogRoleVisible = true;
        } else {
          this.$message({
            showClose: true,
            message: "请先选择一个用户",
            type: "error"
          });
        }
      },
      handlerinstitution(){
        if (this.userSelection.length === 0 || this.userSelection.length > 1) {
          this.$message({
            showClose: true,
            message: "只能选择一条信息！"
          });
          return;
        }

        this.currentId = this.userSelection[0].userId;
        if (this.currentId > -1) {
          this.dialoginstitutionVisible = true;
        } else {
          this.$message({
            showClose: true,
            message: "请先选择一个用户",
            type: "error"
          });
        }
      },
      getSelection(selection) {
        this.userSelection = selection;
      },
      tableStop: function () {  // 0停用
        this.stopOrStart(0);
      },
      tableStart: function () {  // 1启用
        this.stopOrStart(1);
      },
      stopOrStart: function (num) { // 1启用，0停用
        if(!this.userSelection.length){
          this.$message({
            showClose: true,
            message: '请先选择一条信息！'
          });
          return;
        }
        let flag=true;
        if(num==1){//启用
          this.userSelection.forEach(user => {
            if(user.userStatus==1){
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
          }else{

          }
        }else{//停用
          this.userSelection.forEach(user => {
            if(user.userStatus==0){
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
          this.userSelection.forEach((item)=>{
            ids.push(item.userId);
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
                type:'success',
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
            /*this.$alert('网络错误，请稍后重试。', '消息', {
              type:'error',
              confirmButtonText: '确定'
            });*/
          });
        }).catch(() => {});
      }
    }
  };
</script>

