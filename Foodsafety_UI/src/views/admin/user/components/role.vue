<template>
  <div style="padding-bottom: 10px">
    <el-table ref="elementTable" :data="list" border fit highlight-current-row @row-click="rowClick" @selection-change="handleSelectionChange" style="width: 100%;">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="角色编号" width="100px" prop="roleId">

      </el-table-column>
      <el-table-column width="200px" align="center" label="角色名称" prop="roleName">

      </el-table-column>

      <el-table-column width="240px" align="center" label="描述" prop="roleDesc">
      </el-table-column>
      <el-table-column width="180px" align="center" label="创建时间" prop="createTime">

      </el-table-column>
      <el-table-column width="180px" align="center" label="更新时间" prop="updateTime">
      </el-table-column>
    </el-table>
    <div style="margin-top: 10px;">
      <el-button type="primary" @click="update()">保存</el-button>
    </div>
  </div>
</template>

<script>
import {
  getRoles,modifyRoles
} from 'api/admin/user/index';
import {
  page,
  getUsers
} from 'api/admin/role/index';

import { parseValueToText } from 'utils/index';
import { mapGetters } from 'vuex';
export default {
  props: {
     userId:{
      default:'-1'
    }
  },
  data() {
    return {
      list: [],
      selection: []
    }
  },
  created() {
    this.getList();
    this.initUsers();
  },
  computed: {
    ...mapGetters([
      'staticData'
    ])
  },
  watch: {
    'userId': function() {
      this.getList();
      this.initUsers();
    }
  },
  updated() {
    this.initUsers();
  },
  methods: {
    getList() {
      page({ roleStatus: 1 })
        .then(response => {
          this.list = response.rows;
        })
    },
    update() {

      if(this.selection.length===0){
        this.$message({
          showClose: true,
          message: '请选择一个角色！'
        });
        return;
      }
      if(this.selection.length>1){
        this.$message({
          showClose: true,
          message: '请仅选择一个角色！'
        });
        return;
      }

      const roleId=this.selection[0].roleId;

      modifyRoles(this.userId,roleId).then(() => {
        this.$emit('closeRoleDialog');
        this.$message({
          showClose: true,
          message: '保存成功',
          type: 'success',
          duration: 2000
        });
      });
    },
    initUsers() {
      getRoles(this.userId).then(response => {
        const roles = response.data;
        if (roles && roles.length > 0) {
          roles.forEach(role => {
            this.selectRow(role);
          });
        }
      });
    },
    selectRow(role) {
      this.list.forEach(row => {
        if (row.roleId === role.roleId) {
          this.$refs.elementTable.toggleRowSelection(row, true);
        }
      });
    },
    handleSelectionChange(selection, row) {
      this.selection=selection;
    },
    rowClick: function (row, event, column) {  // row点击事件：切换checkbox选中状态
      this.$refs['elementTable'].toggleRowSelection(row);
    }
  }
}
</script>
