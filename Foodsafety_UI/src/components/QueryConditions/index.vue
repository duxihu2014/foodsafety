<template>
  <span>
    <el-form-item label="行政区域" v-if="condition">
      <el-cascader
        v-model="area"
        :options="areaOptions"
        :show-all-levels="true"
        expand-trigger="hover"
        :change-on-select="true"
        :clearable="true"
        @change="areaChange"
        :props="{label:'text', value: 'id'}">
      </el-cascader>
    </el-form-item>
    <el-form-item label="责任区域" class="filter-item" v-if="condition">
      <el-select  v-model="grid" placeholder="请选择" @change="gridChange" clearable filterabler>
        <el-option v-for="(item, index) in gridOptions" :key="item.gridId" :label="item.gridName" :value="item.gridId"> </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="企业名称" class="filter-item" v-if="condition">
      <!--<el-select  v-model="enterprise" placeholder="请选择" @change="enterpriseChange" clearable filterabler>
        <el-option v-for="(item, index) in enterpriseOptions" :key="item.enterpriseId" :label="item.enterpriseName" :value="item.enterpriseId"> </el-option>
      </el-select>-->
       <el-autocomplete
         class="inline-input"
         v-model="enterprise"
         :fetch-suggestions="queryEnterprise"
         :trigger-on-focus="triggerOnFocus"
         :readonly="triggerOnFocus"
         :placeholder="placeholder"
         @select="enterpriseChange"
         @blur="enterpriseBlur"
         clearable
       ></el-autocomplete>
    </el-form-item>
  </span>
</template>

<script>
  import { mapGetters } from "vuex";
  import { childTree } from "api/admin/area/index";
  import { getAreaGrid } from "api/admin/supervisory/grid";
  import { getEnterpriseByGrid, getEnterpriseByName } from "api/admin/enterprise/base/index";
  export default {
    props: {
      areaId: {
        default: undefined
      },
      gridId: {
        default: undefined
      },
      enterpriseId: {
        default: undefined
      }
    },
    watch: {
      'areaId': function() {
        if (this.areaId) {
          //console.log("areaId setting");
        } else {
          this.area = undefined;
          //console.log("areaId reset");
        }
        this.gridOptions = [];
        this.grid = undefined;
        this.enterpriseOptions = [];
        this.enterprise = undefined;
      },
      'gridId': function() {
        if (this.gridId) {
          //console.log("gridId setting");
        } else {
          this.grid = undefined;
          this.triggerOnFocus = false;
          //console.log("gridId reset");
        }
        this.enterpriseOptions = [];
        this.enterprise = undefined;
      },
      'enterpriseId': function() {
        if (this.enterpriseId) {
          //console.log("enterpriseId setting");
        } else {
          this.enterprise = undefined;
          //console.log("enterprise reset");
        }
      },
      'grid': function() {
        if (this.grid){
          this.triggerOnFocus = true;
          this.placeholder = '请选择企业';
        } else {
          this.triggerOnFocus = false;
          this.placeholder = '请输入企业名称';
        }
      }
    },
    data() {
      return {
        condition: undefined,
        area: [],
        grid: undefined,
        enterprise: undefined,
        areaOptions:[],
        gridOptions: [],
        enterpriseOptions: [],
        triggerOnFocus: false,
        placeholder: '请输入企业名称',
        results: undefined
      };
    },
    mounted () {
      if (this.user.userType === '4') {
        this.condition = false;
      } else {
        this.condition = true;
      }
    },
    created() {
      this.initData();
    },
    computed: {
      ...mapGetters(["user"])
    },
    methods: {
      initData(){
        childTree({areaId:this.user.areaId}).then(response=> {
          this.areaOptions = response;
        })
        //this.areaOptions = [{"text":"江岸区","expanded":true,"children":undefined,"id":"1072","pid":"1070","level":"3","value":"1072","label":"江岸区","longitude":0.0,"latitude":0.0,"root":true}];
      },
      areaChange(value){
        this.$emit('setAreaId', value[value.length-1]);
        if(value.length > 0) {
          this.getGrid(value[value.length - 1]);
        }
      },
      gridChange(value){
        this.$emit('setGridId', value);
        if (value) {
          this.getEnterprise(value);
        }
      },
      enterpriseChange(item){
        this.$emit('setEnterpriseId', item.key);
      },
      enterpriseBlur(){
        let bool = false;
        this.enterpriseOptions.forEach((item) => {
          if (this.enterprise === item.enterpriseName) {
            bool = true;
          }
        });
        if (!bool)
          this.enterprise = undefined;
      },
      getGrid(value){
        getAreaGrid({areaId:value, gridStatus:'1'}).then((response)=>{
          this.gridOptions = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '责任区域信息获取失败。'
          });
        });
      },
      queryEnterprise(queryString, cb) {
          this.results = [];
          if (!this.triggerOnFocus) {
            this.getEnterpriseByName(queryString, cb);
          }else{
            this.enterpriseOptions.forEach((item) => {
              this.results.push({"value": item.enterpriseName, "key": item.enterpriseId});
            });
            // 调用 callback 返回建议列表的数据
            cb(this.results);
          }

      },
      getEnterprise(value){
        getEnterpriseByGrid({gridId:value}).then((response)=>{
          this.enterpriseOptions = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '企业信息获取失败。'
          });
        });
      },
      getEnterpriseByName(enterpriseName, cb){
        let areaId = this.areaId ? this.areaId : this.user.areaId;
        getEnterpriseByName({enterpriseName:enterpriseName, finalAreaId:areaId, enterpriseStatus:3}).then((response)=>{
          this.results = [];
          this.enterpriseOptions = response;
          this.enterpriseOptions.forEach((item) => {
            this.results.push({"value": item.enterpriseName, "key": item.enterpriseId});
          });
          if(this.results.length==0) {
            if (this.enterprise.length > 1) {
              this.enterprise = this.enterprise.substring(0,this.enterprise.length - 1);
              this.getEnterpriseByName(this.enterprise, cb);
            } else {
              this.enterprise = undefined;
              cb(this.results);
            }
          } else
          // 调用 callback 返回建议列表的数据
            cb(this.results);
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '企业信息获取失败。'
          });
        });
      }
    }
  };
</script>
