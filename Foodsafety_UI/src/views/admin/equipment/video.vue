<template>
    <div class="app-container calendar-list-container">
        <el-row style="margin-top:0px" :gutter="20">
            <el-col :span="4" style="padding-left: 0px;padding-right: 0px;width: 23%;">
                <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span>设备分组</span>
                </div>
                <div  class="text item">
                    <equipmentGroup @selectNodeData="getSelectNodeData"></equipmentGroup>
                </div>
              </el-card>
            </el-col>
            <el-col :span="20" style="width:77%;">
              <el-card class="box-card">
                <div slot="header" class="clearfix">
                  <span>设备</span>
                </div>
                <div  class="text item">
                   <tabs ref="tablePanel"></tabs>
                </div>
              </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  page,
  groupTree
} from "api/admin/equipment/index";
import { getAllUserMap } from "api/admin/user/index";
import equipmentGroup from "./components/equipmentGroup.vue";
import tabs from "./components/tabs.vue";

export default {
  name: "",
  components: {
    equipmentGroup, tabs
  },
  data() {
    return {};
  },
  created() {},
  computed: {
    ...mapGetters(["staticData"])
  },
  watch: {},
  methods: {
    getSelectNodeData(data) {
      this.$refs["tablePanel"].getSelectNodeData(data.id, data.level, data.pid);
    }
  } //end methods
};
</script>
<style scoped>
.filter-container {
  padding-bottom: 10px;
  margin: 0 auto;
  text-align: center;
}
.main-container {
  position: relative;
  min-height: 100%;
  height: 96vh;
  padding-top: 110px;
  transition: margin-left 0.28s ease-out;
  margin-left: 12.5rem;
  background-color: #e4e7ed;
}
</style>


