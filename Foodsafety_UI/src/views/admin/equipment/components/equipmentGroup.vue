<template>
    <div style="margin:0 auto;text-align:center;    width: 200px;">
        <!-- <el-row style="margin-top:20px">
            <el-col >    -->
                <div class="filter-container">
                        <el-button size="mini" type="primary"  @click="handleAdd">添加</el-button>
                        <el-button size="mini" type="success"  @click="handleUpdate">修改</el-button>
                        <el-button size="mini" type="danger"  @click="handleDelete">删除</el-button>
                </div>
                <el-input placeholder="输入关键字进行过滤" v-model="filterText" style="width:200px;"></el-input>
                <el-tree style="height:580px;" class="filter-tree"
                       :data="treeData"
                       node-key="id"
                       highlight-current
                       default-expand-all
                       :props="defaultProps"
                       :filter-node-method="filterNode"
                       :expand-on-click-node="false"
                       ref="menuTree"
                       accordion
                       @node-click="clickTreeNode" >
                </el-tree>

           <!--  </el-col>
        </el-row> -->
        <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogGroupFormVisible" style="text-align:left" center>
            <div style="margin-bottom: 20px;margin-top: -10px"><hr/><p/></div>
            <el-form :model="groupForm" :rules="groupRules" ref="groupForm" label-width="100px">
                <el-form-item label="分组名称" prop="enterpriseGroupName">
                <el-input v-model="groupForm.enterpriseGroupName" placeholder="请输入分组名称"></el-input>
                </el-form-item>
            </el-form>
            <div style="margin-bottom: 20px;margin-top: -30px"><hr/><p/></div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel('groupForm')">取 消</el-button>
                <el-button v-if="dialogStatus=='create'" type="primary" @click="create('groupForm')">确 定</el-button>
                <el-button v-else type="primary" @click="update('groupForm')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  page
} from "api/admin/equipment/index";
import {groupTree, addGroup, putGroup, delGroup, checkGroupVideo, checkGroupName} from "api/admin/equipment/group";
import { spacelValidator } from "utils/validate";
export default {
  name: "",
  components: {},
  data() {
    const groupNameValidator = (rule, value, callback) => {
      checkGroupName({ enterpriseGroupName: value.trim(), compId: this.selectCompId }).then(response => {
          if (response.data) {
          return callback(new Error("分组名称不能重复"));
        } else {
          callback();
        }
      });
    };
    return {
      listQuery: {
        page: 1,
        limit: 10,
        enterpriseGroupId: undefined,
        enterpriseGroupName: undefined
      },
      filterText: "",
      treeData: [], //区域
      expandNodes: [],
      defaultProps: {
        children: "children",
        label: "text"
      },
      dialogGroupFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "➢  编辑分组",
        create: "➢  新建分组"
      },
      groupForm: {
        enterpriseGroupId: undefined,
        enterpriseGroupName: undefined,
        enterpriseId: undefined,
        status: "1"
      },
      groupRules: {
        enterpriseGroupName: [
          { max: 50, message: "长度不能超过50个字符", trigger: "blur" },
          { validator: groupNameValidator, trigger: "blur" },
          {
            required: true,
            validator: spacelValidator,
            message: "请输入分组名称",
            trigger: "blur"
          }
        ]
      },
      currLevel: "",
      currTreeNode: undefined,
      selectCompId: ""
    };
  },
  created() {
    this.getTree();
  },
  computed: {
    ...mapGetters(["staticData"])
  },
  watch: {
    filterText(val) {
      this.$refs.menuTree.filter(val);
    }
  },
  methods: {
    getTree() {
      groupTree(this.listQuery).then(data => {
        this.treeData = data;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.text.indexOf(value) !== -1;
    },
    clickTreeNode(data) {
      this.listQuery.enterpriseGroupId = data.id;
      this.listQuery.status = this.status;
      this.selectCompId = data.id;
      //   this.getList();
      this.currLevel = data.level;
      this.currTreeNode = data;
      this.$emit("selectNodeData", data); //取消
    },
    handleAdd() {
      let errMsg;
      if (this.currLevel == 1) {
        this.resetForm();
        this.dialogGroupFormVisible = true;
        this.dialogStatus = "create";
      } else {
        errMsg = "请选公司节点";
      }
      if (errMsg) {
        this.$message({
          showClose: true,
          message: errMsg,
          type: "error"
        });
      }
    },
    handleUpdate() {
      if (this.currLevel == 2) {
        this.dialogGroupFormVisible = true;
        this.dialogStatus = "update";
        this.groupForm.enterpriseGroupId = this.currTreeNode.id;
        this.groupForm.enterpriseGroupName = this.currTreeNode.text;
        this.groupForm.enterpriseId = this.currTreeNode.pid;

      } else {
        this.$message({
          showClose: true,
          message: "请选择分组节点",
          type: "error"
        });
      }
    },
    handleDelete() {
      if (this.currLevel == 2) {

        //---
        checkGroupVideo({ groupId: this.selectCompId }).then(response => {
          if (response.data) {
            this.$message({
              showClose: true,
              message: "分组下存在摄像头信息，不允许删除",
              type: "error"
            });
          }else{
            this.$confirm("此操作将永久删除, 是否继续?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(() => {
              this.deleteGroup();
            });
          }

        });

        //---
        // this.deleteGroup();
      } else {
        this.$message({
          showClose: true,
          message: "请选择分组节点",
          type: "error"
        });
      }

    },
    deleteGroup(){
      delGroup(this.selectCompId).then(() => {
          this.$notify({
          title: "成功",
          message: "删除成功",
          type: "success",
          duration: 2000
        });
        this.getTree();
        // this.getList();
      });
    },
    handleDelete2() {
      if (this.currLevel == 2) {
        checkGroupVideo({ enterpriseGroupId: this.selectCompId }).then(response => {
            if (response.data) {
              this.$message({
                showClose: true,
                message: "分组下存在、摄像头信息"
                // type: 'warning'
              });
            } else {
              //
              this.$confirm("此操作将永久删除, 是否继续?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                delGroup(this.selectCompId).then(() => {
                      this.$notify({
                        title: "成功",
                        message: "删除成功",
                        type: "success",
                        duration: 2000
                      });
                      this.getTree();
                      // this.getList();
                    });
                });
            }
          });
      } else {
        this.$message({
          showClose: true,
          message: "请选择分组节点",
          type: "error"
        });
      }
    },
    create(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          this.groupForm.enterpriseId = this.selectCompId;
          addGroup(this.groupForm).then(() => {
            this.dialogGroupFormVisible = false;
            this.getTree();
            // this.getList();
            this.$notify({
              title: "成功",
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
    update(formName) {
      const set = this.$refs;
      set[formName].validate(valid => {
        if (valid) {
          this.dialogGroupFormVisible = false;
          putGroup(this.groupForm.enterpriseGroupId, this.groupForm).then(() => {
            this.dialogGroupFormVisible = false;
            this.getTree();
            // this.getList();
            this.$notify({
              title: "成功",
              message: "修改成功",
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
      this.dialogGroupFormVisible = false;
    },
    resetForm() {
      this.groupForm = {
        enterpriseGroupName: undefined,
        enterpriseId: undefined,
        status: "1"
      };
    }
  } //end methods
};
</script>
<style scoped>
.filter-container {
  padding-bottom: 10px;
  margin: 0 auto;
  /* text-align: center; */
}
.el-tree-node__expand-icon {
  border: 0;
  /*自定义，必要时用!important*/
}
.el-tree-node__expand-icon.expanded {
  /*自定义，必要时用!important*/
}
</style>


