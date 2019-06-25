<template>

  <div class="app-container calendar-list-container">
    <div ref="dictDiv" v-if="dictLoading">
      <fieldset class="fieldset">
        <legend class="legend">查询条件</legend>
        <div class="field-box">
          <el-form   :inline="true">
            <el-form-item label="名称" >
              <el-input @keyup.enter.native="handleFilter" v-model="listQuery.dictNameLike" placeholder="" style="width:200px"></el-input>

            </el-form-item>

            <el-form-item>
              <el-button @click="handleFilter"    type="primary" v-waves >搜索</el-button>
              <el-button    @click="resetQuery"    >重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </fieldset>
      <div>
        <div class="head-box">
           <div class="item-box">
                <span class="mg-r-15 head-item" @click.stop.prevent="handleAdd('form')"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
                <!--<span class="mg-r-15 head-item" @click.stop.prevent="handleUpdate"><i class="fa fa-pencil menu-icon vam" aria-hidden="true"></i>编辑</span>-->
                <span class="mg-r-15 head-item" @click.stop.prevent="handleDelete"><i class="fa fa-window-close menu-icon vam" aria-hidden="true"></i>删除</span>
           </div>
        </div>
        <el-table ref="multipleTable" :data="tableData" border  highlight-current-row  style="width: 100%" @selection-change="handleSelectionChange"  :height="height">
          <el-table-column  type="selection"  prop="dictId"></el-table-column>
          <el-table-column prop="dictName" label="字典名称" align="center"> </el-table-column>
          <el-table-column prop="dictDesc" label="字典描述" align="center" :show-overflow-tooltip="true"> </el-table-column>
          <!--<el-table-column prop="dictStatus" label="状态" align="center"  width="100" :formatter="statusFormatter"> </el-table-column>-->
          <el-table-column prop="createTime" label="创建时间" align="center"> </el-table-column>
          <el-table-column prop="updateTime" label="更新时间" align="center" > </el-table-column>
          <el-table-column  align="center"  label="操作" width="150" >
            <!--<template slot-scope="scope">
              &lt;!&ndash;<el-button type="text" size="small" @click="handleDetailOpertion(scope.row)">添加字典详情</el-button>&ndash;&gt;
              <span class="mg-r-15 head-item" @click.stop.prevent="handleDetailOpertion(scope.row)"><i class="fa fa-gear menu-icon vam" aria-hidden="true"></i></span>
              <span class="mg-r-15 head-item" @click.stop.prevent="handleUpdate(scope.row)"><i class="fa fa-pencil menu-icon vam" aria-hidden="true"></i></span>
            </template>-->
            <template slot-scope="scope">
              <el-button size="mini" @click="handleDetailOpertion(scope.row)">查看
              </el-button>
              <el-button size="mini" type="success" @click="handleUpdate(scope.row)">编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div  class="pagination-container">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="pageSizes" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"></el-pagination>
        </div>
      </div>
      <el-dialog  title="编辑" :visible.sync="dialogFormVisible" @close="closeDialog" :close-on-click-modal="false">
        <el-form :model="form" :rules="rules" ref="form">
          <el-tabs>
            <el-tab-pane label="基本信息">
              <el-row>
                <el-col :span="12">
          <el-form-item label="字典名称" prop="dictName" :label-width="formLabelWidth">
            <el-input v-model="form.dictName" size="large" auto-complete="off"></el-input>
          </el-form-item>
                </el-col>
                <el-col :span="12">
          <el-form-item label="字典描述" prop="dictDesc" :label-width="formLabelWidth">
            <el-input v-model="form.dictDesc" auto-complete="off"></el-input>
          </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="resetForm('form')">取 消</el-button>
          <el-button v-if="dialogStatus=='add'" type="primary" @click="addItem('form')">确 定</el-button>
          <el-button v-else type="primary" @click="update('form')">确 定</el-button>
        </div>
      </el-dialog>
    </div>
    <!--详情-->
    <div ref="detailDiv" v-else>
      <detailDiv :dictId="dictId" @listenDictLoading="showDictDiv"></detailDiv>
    </div>
  </div>
</template>
<script>
import { page, addObj, getObj, delObj, putObj } from "api/admin/dict/index";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";
import detailDiv from "./detailIndex.vue";
import { loadGridHeight } from "api/screen";
export default {
  name: "dict",
  data() {
    return {
      height:undefined,
      dictLoading: true,
      pageSizes: [10, 20, 30, 50],
      listQuery: {
        dictName: "",
        page: 1,
        limit: 20
      },
      tableData: null,
      total: 0,
      multipleSelection: [],
      selectionDictIds: "",
      dialogFormVisible: false, // 编辑表单是否显示标识，默认为false不显示
      form: {
        dictName: "",
        dictDesc: ""
      },
      formLabelWidth: "80px",
      dialogStatus: "",
      rules: {
        dictName: [
          { required: true, message: "请输入字典名称", trigger: "blur" },
          { max: 32, message: "长度不能超过32个字符", trigger: "blur" }
        ]
      },
      dictId: undefined
    };
  },
  components: {
    detailDiv
  },
  mounted () {
    this.height=loadGridHeight();
  },
  created() {
    //生命周期钩子，vue实例一创建好就立即执行
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData"]),

  },
  methods: {
    // then相当 于ajax中的sucess方法
    // 查询历史数据
    getList() {
      page(this.listQuery).then(response => {
        this.tableData = response.rows;
        this.total = response.total;
        // this.listLoading = false;
      });
    },

    handleSizeChange(val) {
      //根据选择显示的条数，显示数据
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {
      // 显示选择显示的页面显示所显页面的数据
      this.listQuery.page = val;
      this.getList();
    },
    handleFilter() {
      this.listQuery.page=1;
      this.getList();
    },
    statusFormatter(row, column, cellValue) {
      // 格式化表格中的值
      return parseValueToText(cellValue, this.staticData["状态"]);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleAdd(formName) {
      this.dialogFormVisible = true;
      this.dialogStatus = "add";
    },
    closeDialog(){
      this.resetTmp();
    },
    //重置对话框表单
    resetTmp() {
      this.$refs["form"].resetFields();
      this.form={
        dictName: "",
        dictDesc: ""
      };
    },
    handleUpdate(row) {
      //if (this.multipleSelection.length == 1) {
        getObj(row.dictId).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.dialogStatus = "update";
        });
      /*} else {
         this.$message({
          showClose: true,
          message: "请选择且只能选择一条记录"
          // type: 'warning'
        });
      }*/
    },

    handleDelete() {
      if (this.multipleSelection.length > 0) {
        this.$confirm("此操作将永久删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.getDictIds();
            delObj(this.selectionDictIds).then(() => {
              this.$message({
                showClose: true,
                message: "删除成功",
                type: "success",
                duration: 2000
              });
              this.getList();
            });
          })
          .catch(() => {
            /** this.$message({
             type: 'info',
             message: '已取消删除'
           });*/
          });
      } else {
         this.$message({
          showClose: true,
          message: "请至少选择一条记录"
          // type: 'warning'
        });
      }
    },
    handleDetailOpertion(row) {
      this.dictLoading = false;
      this.dictId = row.dictId;
    },
    getDictIds() {
      let dictIds = "";
      this.multipleSelection.forEach(dict => {
        dictIds += "," + dict.dictId;
      });
      this.selectionDictIds = dictIds.substring(1, dictIds.length);
    },
    addItem(formName) {
      this.$refs[formName].validate(valid => {
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
          //console.log("error submit!!");
          return false;
        }
      });
    },
    update(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          putObj(
            this.form.dectId,
            this.form
          ).then(() => {
            this.dialogFormVisible = false;
            this.getList();
            this.$message({
              showClose: true,
              message: "修改成功",
              type: "success",
              duration: 2000
            });
          });
        } else {
          //console.log("error update!!");
          return false;
        }
      });
    },
    resetQuery(){
      this.listQuery.dictNameLike='';
    },
    resetForm(formName) {
      this.dialogFormVisible = false;
      this.$nextTick(function() {
        this.$refs[formName].resetFields();
      });
    },
    showDictDiv(data) {
      this.dictLoading = data;
    }
  }
};
</script>


