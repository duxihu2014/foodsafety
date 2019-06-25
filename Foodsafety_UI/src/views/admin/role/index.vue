<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
            <el-form-item label="名称"  class="filter-item">
              <el-input @keyup.enter.native="handleFilter" style="width: 200px;" placeholder="" v-model.trim="listQuery.roleNameLike"> </el-input>
            </el-form-item>
            <el-form-item label="状态"  class="filter-item">
                <el-select  v-model="listQuery.roleStatus" placeholder="请选择" clearable>
                <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="角色级别" prop="roleType" class="filter-item">
            <el-select  v-model="listQuery.roleType" placeholder="请选择" clearable>
              <el-option v-for="item in  roleTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
           <el-form-item class="filter-item">
              <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
              <el-button  v-waves  @click="resetQuery()">重置</el-button>
           </el-form-item>
    <!--  <el-button class="filter-item" v-if="roleManager_btn_add" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
      <el-button type="primary" v-if="roleManager_btn_resourceManager" @click="handlerAuthority" class="filter-item" style="margin-left: 10px;">
        <icon-svg icon-class="weibiaoti103"></icon-svg>权限分配</el-button>
      <el-button type="primary" v-if="roleManager_btn_userManager" @click="handlerUser" class="filter-item" style="margin-left: 10px;">
        <icon-svg icon-class="quanxian"></icon-svg>关联用户</el-button>-->
        </el-form>
    </div>
     </fieldset>
    <div class="head-box">
          <div class="item-box">
            <span class="mg-r-15 head-item" @click.stop.prevent="handleCreate"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
          <!--  <span class="mg-r-15 head-item" @click.stop.prevent="handlerAuthority"><i class="fa fa-key menu-icon vam" aria-hidden="true"></i>权限分配</span>
            <!--<span class="mg-r-15 head-item" @click.stop.prevent="handlerUser"><i class="fa fa-user-plus menu-icon vam" aria-hidden="true"></i>关联操作员</span>
            <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>-->
            <span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
            <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
          </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%" highlight-current-row
    @selection-change="getSelection" :row-class-name="tableRowClassName" :height="height">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="编号" width="100" prop="roleId">

      </el-table-column>
      <el-table-column width="200px" align="center" label="名称" prop="roleName">

      </el-table-column>
      <el-table-column width="110px" align="center" label="角色级别" prop="roleType" :formatter="roleTypeFormatter"></el-table-column>

      <el-table-column width="300px" align="center" label="描述" prop="roleDesc"></el-table-column>
      <el-table-column width="110px" align="center" label="状态" prop="roleStatus" :formatter="statusFormatter"></el-table-column>

      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">

      </el-table-column>
      <el-table-column width="180px" align="center" label="更新时间" prop="updateTime">
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
           <!--<el-button size="mini" @click="handleView(scope.row)">查看
          </el-button>-->
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button  size="mini" type="primary" @click="handlerAuthority(scope.row)">权限分配
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200" v-else>
        <template slot-scope="scope">
         <!-- <el-button size="mini" @click="handleView(scope.row)">查看
          </el-button>-->
          <el-button  size="mini" type="success" @click="handleUpdate(scope.row)">编辑
          </el-button>
          <el-button  size="mini" type="primary" @click="handlerAuthority(scope.row)">权限分配
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
          <el-tabs>
            <el-tab-pane label="基本信息">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="名称" prop="roleName">
                    <el-input v-model.trim="form.roleName" placeholder=""></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="角色级别" prop="roleType">
                    <el-select  v-model="form.roleType" placeholder="请选择" clearable >
                      <el-option v-for="item in  roleTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="描述" placeholder="" prop="roleDesc">
                    <el-input  v-model.trim="form.roleDesc"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-form>
     <div slot="footer" class="dialog-footer" v-if="roleManager_update">
       <el-button @click="cancel('form')">取 消</el-button>
       <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
       <el-button v-else type="primary" @click="update('form')">确 定</el-button>
     </div>
   </el-dialog>

   <el-dialog  :title="dialogUserName" size="large" :visible.sync="dialogUserVisible" @close="currentId=0" :close-on-click-modal="false">
     <role-user :roleId="currentId"  @closeUserDialog="closeUserDialog" ref="roleUser"></role-user>
   </el-dialog>
   <el-dialog  :title="dialogAuthorityName" size="large" :visible.sync="dialogAuthorityVisible" @close="currentId=0" :close-on-click-modal="false">
     <div slot="title">
       <template>
         <i class="fa fa-chain "> {{dialogAuthorityName}}</i>
       </template>
     </div>
     <role-authority :roleId="currentId" :menuType="currentRoleType"  @closeAuthorityDialog="closeAuthorityDialog" ref="roleAuthority"></role-authority>
   </el-dialog>
 </div>
</template>

<script>
import { page, addObj, getObj, delObjs, putObj,changeStatus } from "api/admin/role/index";
import { getAllRole} from "api/admin/role/index";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";
import { validateNumber } from "utils/validate";
import { loadGridHeight } from "api/screen";
export default {
 name: "role",
 components: {
   "role-user": () => import("./components/user"),
   "role-authority": () => import("./components/authority")
 },
 data() {
   /*
   const roleOrderValidator = (rule, value, callback) => {
     if (value !== undefined && !validateNumber(value)) {
       callback(new Error("请输入数字"));
     } else {
       callback();
     }
   };*/
   const roleNameValidator = (rule, value, callback) => {
     let arr = this.roleNameArr;
     if(this.roleNameSelected!==value){
       let flag =false;
      for(var i=0;i<arr.length;i++){
        if(arr[i].toString().trim()==value){
          flag=true;
          break;
        }
      }
      if(flag){
        callback(new Error("角色名称重复"));
      }else{
        callback();
      }
     }else{
       callback();
     }
   };
   return {
     height:undefined,
     roleNameSelected:'',
     roleNameArr:[],
     form: {
       roleName: undefined,
       roleDesc: undefined,
       roleType:undefined
     },
     rules: {
       roleName: [{required: true, message: "请输入角色名称", trigger: "blur"},
         {max: 32, message: "长度不能超过32个字符", trigger: "blur"},
         { validator: roleNameValidator, trigger: "blur"}
         ],
       roleDesc: [{max: 64, message: "长度不能超过64个字符", trigger: "blur"}],
    //   roleOrder: [{ validator: roleOrderValidator, trigger: "blur" }]
     },
     list: null,
     total: null,
     listLoading: true,
     listQuery: {
       page: 1,
       limit: 20,
       roleNameLike: undefined,
       roleType:undefined
     },
     dialogFormVisible: false,
     dialogStatus: "",
     textMap: {
       update: "编辑",
       create: "创建",
       view: "查看"
     },
     roleManager_update: true,
     tableKey: 0,
     dialogUserVisible: false,
     dialogAuthorityVisible: false,
     dialogUserName: "绑定用户",
     dialogAuthorityName: "绑定权限",
     currentId: -1,
     roleSelection: [],
     needFixedRight:false,
     currentRoleType:null
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
    }
    this.height=loadGridHeight();
  },
 created() {
   this.getAllRoleName();
   this.getList();
 },
 computed: {
   ...mapGetters(["staticData"]),
   statusOptions() {
     return this.staticData["状态"];
   },
   roleTypeOptions() {
     return this.staticData["用户级别"];
   }

 },
 methods: {
   tableRowClassName({row, rowIndex}) {
     if (row.roleStatus === '0') {
       return 'warning-row';
     } else if (row.roleStatus === '1') {
       return 'success-row';
     }
     return '';
   },
   //获取所有角色名称
   getAllRoleName(){
     this.roleNameArr=[];
     getAllRole().then(response => {
         response.forEach(role => {
           this.roleNameArr.push(role.roleName)
         })
     })
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
     this.roleManager_update = true;
  //   this.resetTmp();
   },
   handleUpdate(row) {
     getObj(row.roleId).then(response => {
       this.form = response.data;
       this.form.roleStatus += "";
       this.dialogFormVisible = true;
       this.roleNameSelected=response.data.roleName;
       this.dialogStatus = "update";
       this.roleManager_update = true;
     });

     this.getAllRoleName();
   },
   handleView(row) {
     getObj(row.roleId).then(response => {
       this.form = response.data;
       this.form.roleStatus += "";
       this.dialogFormVisible = true;
       this.dialogStatus = "view";
       this.roleManager_update = false;
     });
   },
   closeDialog(){
     this.viewReadOnly=false;
     this.form={roleName: undefined,
       roleDesc: undefined};
     this.deptManager_update = true;
     this.roleNameSelected="";
     this.$refs['form'].resetFields();
   },
   handleDelete() {
     if (!this.roleSelection.length) {
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
         this.roleSelection.forEach(role => {
           idArr.push(role.roleId);
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
             this.getAllRoleName();
           })
           .catch(() => {});
       })
       .catch(() => {});
   },
   handlerAuthority(row) {
     this.currentId = row.roleId;
     this.currentRoleType =row.roleType;
     this.dialogAuthorityVisible = true;
     this.dialogUserVisible = false;
   },
   handlerUser() {
     if (this.roleSelection.length === 0 || this.roleSelection.length > 1) {
       this.$message({
         showClose: true,
         message: "只能选择一条信息！"
       });
       return;
     }

     this.currentId = this.roleSelection[0].roleId;
     if (this.currentId > -1) {
       this.dialogUserVisible = true;
       this.dialogAuthorityVisible = false;
     } else {
       this.$message({
         showClose: true,
         message: "请先选择一个角色",
         type: "error"
       });
     }
   },
   create(formName) {
     const set = this.$refs;
     set[formName].validate(valid => {
       if (valid) {
         addObj(this.form).then(() => {
           this.dialogFormVisible = false;
           this.getList();
           this.getAllRoleName();
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
     set[formName].validate(valid => {
       if (valid) {
         putObj(this.form.roleId, this.form).then(() => {
           this.dialogFormVisible = false;
           this.getList();
           this.getAllRoleName();
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
   resetTmp() {
     if (this.$refs["form"]) {
       this.$refs["form"].resetFields();
     }
   },
   statusFormatter(row, column, cellValue) {
     return parseValueToText(cellValue, this.staticData["状态"]);
   },
   roleTypeFormatter(row, column, cellValue) {
     return parseValueToText(cellValue,  this.roleTypeOptions);
   },
   closeUserDialog() {
     this.dialogUserVisible = false;
   },
   closeAuthorityDialog() {
     this.dialogAuthorityVisible = false;
   },
   handleSelect(val) {
     this.currentId = val.roleId;
   },
   resetQuery() {
     let page = this.listQuery.page;
     let limit = this.listQuery.limit;
     this.listQuery = {page: page, limit: limit, roleNameLike: undefined };
   },
   getSelection(selection) {
     this.roleSelection = selection;
   },
   tableStop: function () {  // 0停用
     this.stopOrStart(0);
   },
   tableStart: function () {  // 1启用
     this.stopOrStart(1);
   },
   stopOrStart: function (num) { // 1启用，0停用
     if(!this.roleSelection.length){
       this.$message({
         showClose: true,
         message: '请先选择一条信息！'
       });
       return;
     }
     let flag=true;
     if(num==1){//启用
       this.roleSelection.forEach(role => {
         if(role.roleStatus==1){
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
       this.roleSelection.forEach(role => {
         if(role.roleStatus==0){
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
       this.roleSelection.forEach((item)=>{
         ids.push(item.roleId);
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
         });/*this.$alert('网络错误，请稍后重试。', '消息', {
           type:'error',
           confirmButtonText: '确定'
         });*/
       });
     }).catch(() => {});
   }
 }
};
</script>
