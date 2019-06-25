<template>
  <div class="app-container calendar-list-container">
     <el-row>
      <el-col :span="4">
        <el-tree style="height:100px;"
                 class="filter-tree"
                 :data="treeData"
                 node-key="id"
                 highlight-current
                 :expand-on-click-node="false"
                 :props="defaultProps"
                 ref="menuTree"
                 accordion
                 @node-click="getNodeData">
        </el-tree>

       </el-col>

    <el-col :span="20" >
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">

          <el-form-item label="地区"  class="filter-item">
            <el-input  v-model.trim="listQuery.areaName"  :readonly="true"></el-input>
          </el-form-item>


          <el-form-item label="机构名称"  class="filter-item">
         <el-input style="width: 200px;"  placeholder="" v-model.trim="listQuery.institutionNameLike"></el-input>
          </el-form-item>

          <el-form-item label="机构类型" class="filter-item">
            <el-select  v-model="listQuery.institutionType" placeholder="请选择" clearable>
              <el-option v-for="item in  institutionTypeOptions" :key="item.value" :label="item.text" :value="item.value"  :disabled="viewReadOnly"> </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="状态"  class="filter-item">
            <el-select  v-model="listQuery.institutionStatus" placeholder="请选择" clearable>
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
        <span class="mg-r-15 head-item" @click.stop.prevent="handleChangeStatus(1)"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>
        <span class="mg-r-15 head-item" @click.stop.prevent="handleChangeStatus(0)"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
      </div>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="机构名称" width="150" prop="institutionName"></el-table-column>
      <el-table-column align="center" label="上级机构名称" width="150" prop="pInstitutionId" :formatter="getInstitutionName"></el-table-column>
      <el-table-column align="center" label="上级类型" width="150" prop="institutionType" :formatter="institutionTypeFormatter"></el-table-column>
      <el-table-column align="center" label="所属地区" width="100" prop="areaId"  :formatter="getAreaName"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="contactPhone"></el-table-column>
      <el-table-column align="center" label="联系地址" width="300" prop="contactAddress"></el-table-column>
      <el-table-column align="center" label="机构状态" width="100" prop="institutionStatus" :formatter="statusFormatter"></el-table-column>
      <el-table-column align="center" label="机构描述" width="250" prop="institutionDesc" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="创建时间" width="180" prop="createTime"></el-table-column>
      <el-table-column align="center" label="更新时间" width="180" prop="updateTime"></el-table-column>
      <el-table-column  align="center" fixed="right" label="操作" width="150"  v-if="needFixedRight">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="150"  v-else>
        <template slot-scope="scope">
         <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="success"  @click="handleUpdate(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container" >
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

       </el-col>
     </el-row>

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
        <el-tabs>
          <el-tab-pane label="基本信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="所属地区" prop="areaName">
                  <el-input  v-model.trim="listQuery.areaName"  :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上级机构" prop="pInstitutionId">
                  <el-select  v-model="form.pInstitutionId" placeholder="请选择" clearable  :disabled="viewReadOnly">
                    <el-option v-for="item in formInstitutionList" :key="item.institutionId" :label="item.institutionName" :value="item.institutionId"  :disabled="viewReadOnly"> </el-option>
                  </el-select>
                 </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="机构名称" prop="institutionName">
                  <el-input  v-model.trim="form.institutionName" placeholder=""  :readonly="viewReadOnly" ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                    <el-form-item label="机构类型" prop="institutionType">
                      <el-select  v-model="form.institutionType" placeholder="请选择" clearable  :disabled="viewReadOnly">
                        <el-option v-for="item in  institutionTypeOptions" :key="item.value" :label="item.text" :value="item.value" > </el-option>
                      </el-select>
                    </el-form-item>
              </el-col>

            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input v-model.trim="form.contactPhone" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="详细地址" prop="contactAddress">
                  <el-input v-model.trim="form.contactAddress" placeholder=""  :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="机构描述" prop="institutionDesc">
                  <el-input  type="textarea" v-model.trim="form.institutionDesc"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
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
  import { fetchTree } from 'api/admin/area/index';
  import { page, addObj, getObj, changeStatus,getInstitutions, putObj} from "api/admin/institution/index";
  import { parseValueToText } from "utils/index";
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";

  export default {
        name: "index",
        data(){
          const mobileValidator = (rule, value, callback) => {
            const mymobile = /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$)/;
            if (value && !mymobile.test(value)) {
              return callback(new Error("电话号格式不正确"));
            } else {
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
              institutionNameLike: "",
              institutionType: "",
              institutionStatus:""
            },
            form: {
              pInstitutionId:"",
              pInstitutionName:"",
              institutionName: "",
              institutionType: "",
              institutionDesc: "",
              areaId:"",
              contactAddress:"",
              contactPhone:""
            },
            rules: {
              institutionName: [{required: true, message: "请输入机构名称", trigger: "blur"},
                {required: true, max: 32, message: "长度不能超过32个字符", trigger: "blur"}],
              institutionType: [{required: true, message: "请选择机构类型", trigger: ["blur","change"]}],
              areaId:[{required: true, message: "请选择所在地区", trigger: "blur"}],
              contactPhone: [{validator: mobileValidator, trigger: "blur" }]
            },
            dialogFormVisible: false,
            viewReadOnly:false, //控制查看时的按钮显示
            dialogStatus: "",
            textMap: {
              update: "编辑",
              create: "创建",
              view: "查看"
            },
            treeData:[],
            defaultProps: {
              children: 'children',
              label: 'text'
            },
            tableKey: 0,
            institutionManager_update:true,
            institutionSelection: [],
            needFixedRight:false,
            institutionList:[],
            formInstitutionList:[]
          }
        },
      created() {
      //  this.getList();
        fetchTree().then(data => {
          this.treeData = data;
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
        statusOptions() {
          return this.staticData["状态"];
        },
        institutionTypeOptions(){
          return this.staticData["机构类型"];
        }

      },
      methods: {
        getNodeData(data) {
          this.listQuery.areaId = data.id;
          this.listQuery.pAreaId = data.pid;
          this.listQuery.areaName = data.text;
          getInstitutions({areaId:this.listQuery.areaId,institutionStatus:'1'}).then(response => {
            this.institutionList = response;
          });
          this.getList();
        },
        tableRowClassName({row, rowIndex}) {
          if (row.institutionStatus === '0') {
            return 'warning-row';
          } else if (row.institutionStatus === '1') {
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
          this.listQuery.institutionNameLike="";
          this.listQuery.institutionStatus = "";
          this.listQuery.institutionType="";
        },
        handleCreate() {
          this.form.areaId =  this.listQuery.areaId;
          if( this.form.areaId ){
            this.institutionManager_update = true;
            this.dialogStatus = "create";
            this.viewReadOnly=false;
            this.dialogFormVisible = true;
            //查出当前地区的上级地区的所有机构
            //this.formInstitutionList=this.institutionList;
            getInstitutions({areaId:this.listQuery.pAreaId,institutionStatus:'1'}).then(response => {
              this.formInstitutionList = response;
            });
          }else{
            this.$message({
              showClose: true,
              message: "请先选择地区！"
            });
          }

        },
        handleChangeStatus(value) {
          if (!this.institutionSelection.length) {
            this.$message({
              showClose: true,
              message: "请先选择一条信息！"
            });
            return;
          }
          let flag=true;
          if(value==1){//启用
            this.institutionSelection.forEach(institution => {
              if(institution.institutionStatus==1){
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
            this.institutionSelection.forEach(institution => {
              if(institution.institutionStatus==0){
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
          let operation = value?'启用':'停用';
          this.$confirm(`确定要${operation}该数据吗?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = [];
            this.institutionSelection.forEach((institution)=>{
              ids.push(institution.institutionId);
            });
            ids= ids.length>1?ids.join(','):ids[0];
            let param = {"ids":ids, "status":value};
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
            });
          }).catch(() => {});
        },
        create(formName) {
          const set = this.$refs;
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
        cancel() {
          this.dialogFormVisible = false;
          this.resetTmp();
        },
        update(formName) {
          const set = this.$refs;
          set[formName].validate(valid => {
            if (valid) {
              putObj(this.form.institutionId, this.form).then(() => {
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
          //查出当前地区上级地区的所有机构
          getInstitutions({areaId:this.listQuery.pAreaId,institutionStatus:'1',notInstitutionId:row.institutionId}).then(response => {
            this.formInstitutionList = response;
          });

          getObj(row.institutionId).then(response => {
            this.form = response.data;
            if(this.form.pInstitutionId==0){
              this.form.pInstitutionId=undefined;
            }
            this.institutionManager_update = false;
            this.viewReadOnly=true;
            this.dialogFormVisible = true;
            this.dialogStatus = "view";
          });

        },
        handleUpdate(row) {
          //查出当前地区上级地区的所有机构
          getInstitutions({areaId:this.listQuery.pAreaId,institutionStatus:'1',notInstitutionId:row.institutionId}).then(response => {
            this.formInstitutionList = response;
          });
          getObj(row.institutionId).then(response => {
            this.institutionManager_update = true;
            this.form = response.data;
            if(this.form.pInstitutionId==0){
              this.form.pInstitutionId=undefined;
            }
            this.viewReadOnly=false;
            this.dialogFormVisible = true;
            this.dialogStatus = "update";
          });

        },
        //关闭弹出框时，清除表单验证，并且设置表单元素为空
        closeDialog(){
          this.resetTmp();
        },
        statusFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.staticData["状态"]);
        },
        institutionTypeFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.institutionTypeOptions);
        },
        getAreaName(){
          return this.listQuery.areaName;
        },
        getInstitutionName(row, column, cellValue){
           for(var i=0;i<this.institutionList.length;i++){
            if(this.institutionList[i].institutionId==row.pInstitutionId)
              return this.institutionList[i].institutionName;
          }
        },
        resetTmp() {
          if (this.$refs["form"]) {
            this.$refs["form"].resetFields();
           this.form={ pInstitutionId:"",
             pInstitutionName:"",
             institutionName: "",
             institutionType: "",
             institutionDesc: "",
             contactAddress:"",
             contactPhone:""};
          }
        },
        getSelection(selection) {
          this.institutionSelection = selection;
        }
      }
    }
</script>
