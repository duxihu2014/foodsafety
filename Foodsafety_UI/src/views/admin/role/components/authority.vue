<template>
  <div style="overflow: hidden;">
    <el-input placeholder="输入关键字进行过滤" v-model="filterText"> </el-input>
    <div style="height: 600px;overflow: auto;margin: 10px 0px">
      <el-tree class="filter-tree"
        :data="treeData"
        show-checkbox node-key="id"
        highlight-current :props="defaultProps"
        :filter-node-method="filterNode"
        ref="menuTree"
        default-expand-all>
      </el-tree>
    </div>
    <el-button type="primary" v-if="groupManager_menu" @click="update" style="float:right;margin: 5px">保存</el-button>
  </div>
</template>
<script>
import {
  fetchTree
} from 'api/admin/menu/index';
import { getMenuAuthority, modifyMenuAuthority } from 'api/admin/role/index';
import { mapGetters } from 'vuex';

export default {
  name: 'authority',
  props: {
    roleId: {
      default: -1
    },
    menuType:{
      default:null
    }
  },
  watch: {
    filterText(val) {
      this.$refs.menuTree.filter(val);
    },
    'menuType': function() {
      this.getList();
    },
    'roleId': function() {
       this.getList();
    },


  },
  created() {
    this.getList();
  },
  computed: {
    ...mapGetters([
      'staticData'
    ])
  },
  data() {
    return {
      filterText: '',
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      groupManager_menu: true
    };
  },
  methods: {
    update() {
      this.$emit('closeAuthorityDialog');
      const nodes = this.$refs.menuTree.getCheckedNodes();
      const dirNodes = this.$refs.menuTree.getHalfCheckedNodes();
      const ids = [];
      nodes.push(dirNodes);

      for (let i = 0; i < nodes.length; i++) {
        ids.push(nodes[i].id);
      }
      // for (let i = 0; i < dirNodes.length; i++) {
      //   ids.push(dirNodes[i].id);
      // }
      //console.log("---set role menu--"+ids.join());
      modifyMenuAuthority(this.roleId, {
        menuIds: ids.join()
      }).then(() => {
        this.$message({
          showClose: true,
          message: '保存成功',
          type: 'success',
          duration: 2000
        });
      });
    },
    getList() {
      fetchTree({menuType:this.menuType}).then(data => {
        this.treeData = data;
        this.initAuthoritys();
      });
    },
    initAuthoritys() {
      getMenuAuthority(this.roleId).then(response => {
        const result = [];
        for (let i = 0; i < response.data.length; i++) {
          result.push(response.data[i].menuId);
        }
        //console.log('---set checked menu--'+result.join());
        this.$refs.menuTree.setCheckedKeys(result);
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.text.indexOf(value) !== -1;
    }
  }
}
</script>
