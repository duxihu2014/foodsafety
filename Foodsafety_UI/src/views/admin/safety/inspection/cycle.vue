<template>
  <div class="app-container calendar-list-container" style="margin: 10px 0px">
    <el-row>
      <el-col :span="8">
        <el-tree class="filter-tree"
                 :data="treeData"
                 node-key="id"
                 highlight-current
                 default-expand-all
                 :expand-on-click-node="false"
                 :props="defaultProps"
                 ref="menuTree"
                 accordion
                 @node-click="getNodeData">
        </el-tree>
      </el-col>

      <el-col :span="16">
        <el-card class="box-card">
          <el-form :model="form" :rules="rules" ref="form" label-width="130px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="设置机构" prop="settingInstitutionName">
                  <el-input v-model="settingInstitutionName" :disabled="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="初次检查日期" prop="initialInspection">
                  <el-date-picker v-model="form.initialInspection" type="date" value-format="yyyy-MM-dd" placeholder="请选择" clearable :picker-options="pickerOptions1" :disabled="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="提前生成天数" prop="aheadDays">
                  <el-input-number size="small" :min="1" :max="14" v-model="form.aheadDays" :clearable="true" :disabled="viewReadOnly"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="A级企业检查周期" prop="inspectionCycleA">
                  <el-input-number size="small" :min="1" :max="12" v-model="form.inspectionCycleA" :clearable="true" :disabled="viewReadOnly"></el-input-number>
                  <el-tag>月/次</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col>
                <el-form-item label="B级企业检查周期" prop="inspectionCycleB">
                  <el-input-number size="small" :min="1" :max="12" v-model="form.inspectionCycleB" :clearable="true" :disabled="viewReadOnly"></el-input-number>
                  <el-tag>月/次</el-tag>
                </el-form-item>
              </el-col>
            </el-row><el-row>
            <el-col>
              <el-form-item label="C级企业检查周期" prop="inspectionCycleC">
                <el-input-number size="small" :min="1" :max="12" v-model="form.inspectionCycleC" :clearable="true" :disabled="viewReadOnly"></el-input-number>
                <el-tag>月/次</el-tag>
              </el-form-item>
            </el-col>
          </el-row><el-row>
            <el-col>
              <el-form-item label="D级企业检查周期" prop="inspectionCycleD">
                <el-input-number size="small" :min="1" :max="12" v-model="form.inspectionCycleD" :clearable="true" :disabled="viewReadOnly"></el-input-number>
                <el-tag>月/次</el-tag>
              </el-form-item>
            </el-col>
          </el-row>
          </el-form>
          <div style="text-align: center">
            <el-button @click="cancel()" :disabled="viewReadOnly">重 置</el-button>
            <el-button type="primary" @click="update('form')" :disabled="viewReadOnly">确 定</el-button>
          </div>
        </el-card>

      </el-col>
    </el-row>

  </div>
</template>

<script>
  import { fetchTree } from 'api/admin/institution/index';
  import { addObj, putObj, getInspectionCycle } from 'api/admin/safety/inspection/cycle';

  export default {
    name: "index",
    data(){
      return{
        form: {
          cycleId: undefined,
          areaId: undefined,
          settingInstitution: undefined,
          initialInspection: undefined,
          aheadDays: undefined,
          inspectionCycleA: undefined,
          inspectionCycleB: undefined,
          inspectionCycleC: undefined,
          inspectionCycleD: undefined
        },
        rules: {

        },
        institutionId: this.$store.getters.user.institutionId,
        settingInstitutionName: undefined,
        viewReadOnly:false,
        treeData:[],
        defaultProps: {
          children: 'children',
          label: 'text'
        },
        pickerOptions1: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        }
      }
    },
    created() {
      fetchTree().then(data => {
        this.treeData = data;
      });
    },
    mounted() {

    },
    computed: {

    },
    methods: {
      getNodeData(data) {
        getInspectionCycle({settingInstitution:data.id}).then((response) => {
          if (response.rel) {
            this.form = response.data;
            this.settingInstitutionName = data.text;
            if (this.institutionId == data.id) {
              this.viewReadOnly = false;
            } else {
              this.viewReadOnly = true;
            }
          } else {
            this.viewReadOnly = true;
            this.$message({
              type:'error',
              showClose: true,
              message: `机构默认周期设置缺失！`
            });
          }
        });
      },
      /*create(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            addObj(this.form).then(() => {
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
      },*/
      cancel() {
        //this.resetTmp();
        getInspectionCycle({settingInstitution:this.form.settingInstitution}).then((response) => {
          this.form = response.data;
          if(this.institutionId == this.form.settingInstitution){
            this.viewReadOnly = false;
          }else{
            this.viewReadOnly = true;
          }
        });
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            putObj(this.form.cycleId, this.form).then(() => {
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
        if (this.$refs["form"]) {
          this.$refs["form"].resetFields();
          this.form={};
        }
      }
    }
  }
</script>
