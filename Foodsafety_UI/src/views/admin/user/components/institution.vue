<template>
  <div style="padding-bottom: 10px">
    <el-table ref="elementTable" :data="list" border fit highlight-current-row @row-click="rowClick" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column  type="selection"  width="55">
      </el-table-column>
      <el-table-column align="center" label="部门编号" width="100" prop="deptId">

      </el-table-column>
      <el-table-column width="200px" align="center" label="部门名称" prop="deptName">

      </el-table-column>

      <el-table-column width="240px" align="center" label="描述" prop="deptDesc">
      </el-table-column>
      <el-table-column width="240px" align="center" label="负责人名称" prop="contactName">
      </el-table-column>
      <el-table-column width="240px" align="center" label="负责人联系方式" prop="contactPhone">
      </el-table-column>

    </el-table>
    <div style="margin-top: 10px">
      <el-button type="primary" @click="update()">保存</el-button>
    </div>
  </div>
</template>

<script>
import {
  getDepts,modifyDepts
} from 'api/admin/user/index';
import {
  page
} from 'api/admin/institution/index';

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
      page({ deptStatus: 1 })
        .then(response => {
          this.list = response.rows;
        })
    },
    update() {

      if(this.selection.length===0){
        this.$message({
          showClose: true,
          message: '请选择一个部门！'
        });
        return;
      }
      if(this.selection.length>1){
        this.$message({
          showClose: true,
          message: '请仅选择一个部门！'
        });
        return;
      }

      const deptId=this.selection[0].deptId;

      modifyDepts(this.userId,deptId).then(() => {
        this.$emit('closeDeptDialog');
        this.$message({
          message: '保存成功',
          showClose: true,
          type: 'success',
          duration: 2000
        });
      });
    },
    initUsers() {
      getDepts(this.userId).then(response => {
        const depts = response.data;
        if (depts && depts.length > 0) {
          depts.forEach(dept => {
            this.selectRow(institution);
          });
        }
      });
    },
    selectRow(dept) {
      this.list.forEach(row => {
        if (row.deptId === institution.deptId) {
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
