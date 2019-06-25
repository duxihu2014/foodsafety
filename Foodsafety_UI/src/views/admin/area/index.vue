<template>
  <div class="app-container calendar-list-container">
    <el-row>
      <el-col :span="4">
        <el-tree style="height:100px;" class="filter-tree"
                 :data="treeData"
                 node-key="id"
                 highlight-current
                 :expand-on-click-node="false"
                 :props="defaultProps"
                 :filter-node-method="filterNode"
                 ref="menuTree"
                 @node-click="getNodeData"
                 accordion>
        </el-tree>
      </el-col>

      <el-col :span="20" >
        <fieldset class="fieldset">
          <legend class="legend">查询条件</legend>
          <div class="field-box">
            <el-form :inline="true" :model="listQuery">

              <el-form-item label="所属地区"  class="filter-item" >
                <el-input v-model.trim="areaName" :readonly="true"> </el-input>
              </el-form-item>

              <el-form-item label="区域名称"  class="filter-item">
                <el-input v-model.trim="filterText" clearable> </el-input>
              </el-form-item>
              <el-form-item label="状态"  class="filter-item">
                <el-select  v-model="listQuery.areaStatus" placeholder="请选择" clearable>
                  <el-option v-for="item in  statusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                </el-select>
              </el-form-item>

              <el-form-item class="filter-item">
                <el-button type="primary" v-waves  @click="getList">搜索</el-button>
                <el-button  v-waves  @click="filterText='';listQuery.areaStatus='';">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </fieldset>


        <div class="head-box">
          <div class="item-box">
            <span class="mg-r-15 head-item" @click.stop.prevent="handlerAdd"><i class="fa fa-plus-square menu-icon vam" aria-hidden="true"></i>添加</span>
            <!--<span class="mg-r-15 head-item" @click.stop.prevent="tableStop"><i class="fa fa-minus-square menu-icon vam" aria-hidden="true"></i>停用</span>
            <span class="mg-r-15 head-item" @click.stop.prevent="tableStart"><i class="fa fa-check-square menu-icon vam" aria-hidden="true"></i>启用</span>-->
          </div>
        </div>



        <el-table :key='tableKey' :data="list" v-loading.body="listLoading"   :height="height"
                  border fit highlight-current-row  :row-class-name="tableRowClassName">
          <el-table-column  type="selection"  width="55"></el-table-column>
          <el-table-column align="center" label="编号" width="90px" prop="areaId">
          </el-table-column>
          <el-table-column width="145px" align="center" label="区域名称" prop="areaName">
          </el-table-column>
          <el-table-column width="150px" align="center" label="区域编码" prop="areaCode">
          </el-table-column>
          <el-table-column width="150px" align="center" label="上级区域编号" prop="pAreaId">
          </el-table-column>
          <el-table-column width="110px" align="center" label="状态" :formatter="statusFormatter" prop="areaStatus">
          </el-table-column>
          <el-table-column width="110px" align="center" label="排序" prop="sortId">
          </el-table-column>
          <el-table-column width="150px" align="center" label="备注" prop="comments">
          </el-table-column>
          <el-table-column width="180px" align="center" label="创建时间" prop="createTime">
          </el-table-column>
          <el-table-column width="110px" align="center" label="创建工号" prop="createUserId">
          </el-table-column>
          <el-table-column width="180px" align="center" label="更新时间" prop="updateTime">
          </el-table-column>
          <el-table-column width="110px" align="center" label="修改工号" prop="updateUserId">
          </el-table-column>

          <el-table-column  fixed="right" align="center" label="操作" width="150px">
            <template slot-scope="scope">
              <el-button v-if="menuManager_btn_edit" size="small" type="success" @click="handleUpdate(scope.row)">编辑
              </el-button>
              <el-button v-if="scope.row.areaStatus==='1'" size="small" type="danger" @click="deleteArea(scope.row)">删除
              </el-button>
              <el-button v-if="scope.row.areaStatus==='0'" size="small" type="warning" @click="recoverArea(scope.row)">恢复
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-show="!listLoading" class="pagination-container">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
        </div>
      </el-col>
    </el-row>

    <el-dialog  :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="请输入区域名称"></el-input>
        </el-form-item>
        <el-form-item label="区域编码" prop="areaCode">
          <el-input v-model="form.areaCode" placeholder="请输入区域编号"></el-input>
        </el-form-item>
        <el-form-item label="排序" placeholder="请输入排序" prop="sortId">
           <el-input-number v-model="form.sortId" controls-position="right"  :min="1" :max="30"></el-input-number>
        </el-form-item>
        <el-form-item label="备注" placeholder="请输入备注" prop="comments">
          <el-input type="textarea"  :rows="3"  v-model.trim="form.comments"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    fetchTwoLevelTree, page, getArea, putArea, addArea, delArea, recoverArea
  } from 'api/admin/area/index';
  import { mapGetters } from 'vuex';
  import { parseValueToText } from 'utils/index';
  import { loadGridHeight } from "api/screen";

  export default {
    name: 'area',
    data() {
      return {
        height:undefined,
        form: {
          areaId: undefined,
          areaName: undefined,
          areaCode: undefined,
          administrativeLevel: '',//行政级别
          pAreaId: undefined,
          areaStatus: undefined,
          sortId: undefined,
          comments:undefined,
          createTime:undefined,
          createUserId:undefined,
          updateTime:undefined,
          updateUserId:undefined
        },
        filterText: '',
        treeData: [],      //所有地区
        defaultProps: {
          children: 'children',
          label: 'text'
        },
        menuManager_btn_add: true,
        menuManager_btn_edit: true,
        menuManager_btn_del: true,
        menuManager_btn_recover: false,
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          areaName: undefined,
          areaId: undefined,
          areaStatus:undefined
        },
        status: '',
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tableKey: 0,
        level: '',//行政级别
        rules: {
          areaName: [
            {
              max: 32,
              message: '长度不能超过32个字符',
              trigger: 'blur'

            },
            {
              required: true,
              message: '请输入区域名称',
              trigger: 'blur'
            }
          ],
          areaCode: [
            {
              max: 16,
              message: '长度不能超过16个字符',
              trigger: 'blur'
            },
            {
              required: true,
              message: '请输入区域编码',
              trigger: 'blur'
            }
          ],
          administrativeLevel:[
            {
              required:true,message:'请输入行政级别'
            }

          ],
          comments: [
            {
              max: 64,
              message: '长度不能超过64个字符',
              trigger: 'blur'
            }
          ],

        }
      }
    },
    mounted () {
      this.height=loadGridHeight();
    },
    watch: {
      // filterText(val) {
      //   this.$refs.menuTree.filter(val);
      // }
    },
    computed: {
      ...mapGetters([
        'staticData','user'
      ]),

      statusOptions() {
        return this.staticData["状态"];
      }
    },
    created() {
      this.getTree();
      this.getList();
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        if (row.areaStatus === '0') {
          return 'warning-row';
        } else if (row.areaStatus === '1') {
          return 'success-row';
        }
        return '';
      },
      getTree() {
        //得到2级地区树
       fetchTwoLevelTree(this.listQuery).then(data => {
          this.treeData = data;
        });
      },
      getList() {
        // if (this.status === '') {
        //   this.listQuery.areaStatus = 1;
        //   this.menuManager_btn_edit = true;
        //   this.menuManager_btn_del = true;
        //   this.menuManager_btn_recover = false;
        // } else {
        //   this.listQuery.areaStatus = this.status;
        //   if (this.status === '0') {
        //     this.menuManager_btn_edit = false;
        //     this.menuManager_btn_del = false;
        //     this.menuManager_btn_recover = true;
        //   } else {
        //     this.menuManager_btn_edit = true;
        //     this.menuManager_btn_del = true;
        //     this.menuManager_btn_recover = false;
        //   }
        // }
        this.listQuery.areaName=this.filterText;
        this.listQuery.pAreaId=this.listQuery.areaId;
        page(this.listQuery)
          .then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
          })
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.text.indexOf(value) !== -1;
      },
      getNodeData(data) {
        this.listQuery.areaId = data.id;
        this.listQuery.areaStatus = this.status;
        this.getList();
        this.level = data.level;
        this.areaName=data.text;
      },
      handlerAdd() {
        let errMsg;
        /**
         * 如果没有区域数据或未选择父级区域时，则点添加时默认父级值为0，行政级别为0(保存到数据库中为1),反之，如果添加行政级别为2的地区则必须选一父级
         */
        if(this.treeData.length==0 || !this.listQuery.areaId){
            this.level = 0;
            this.listQuery.areaId = 0;
        }
        if (this.level || this.level===0) {
          if (this.level < 3) {
            this.resetForm();
            this.dialogFormVisible = true;
            this.dialogStatus = 'create';
          } else {
            errMsg = '无法在最后一级地址上增加新地址';
          }
        } else {
          errMsg = '请选择父级菜单';
        }
        if (errMsg) {
          this.$message({
            showClose: true,
            message: errMsg,
            type: 'error'
          });
        }
      },
      deleteArea(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            delArea(row.areaId)
              .then(() => {
                this.$message({
                  showClose: true,
                  message: '删除成功',
                  type: 'success',
                  duration: 2000
                });
          //      this.getTree();
                this.getList();
              }).catch(() => { });
          }).catch(() => { });
      },
      handleUpdate(row) {
        getArea(row.areaId)
          .then(response => {
            this.form = response.data;

            this.dialogFormVisible = true;
            this.dialogStatus = 'update';
          });
      },
      recoverArea(row) {
        this.$confirm('此操将恢复数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          recoverArea(row.areaId).then(() => {
            this.$message({
              showClose: true,
              message: '恢复成功',
              type: 'success',
              duration: 2000
            });
         //   this.getTree();
            this.getList();
          }).catch(() => { });
        }).catch(() => { });
      },
      statusFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData['状态']);
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      create(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.form.createUserId = this.user.userId;
            addArea(this.form)
              .then(() => {
                this.dialogFormVisible = false;
                this.getTree();
                this.getList();
                this.$message({
                  showClose: true,
                  message: '创建成功',
                  type: 'success',
                  duration: 2000
                });
              })
          } else {
            return false;
          }
        });
      },
      update(formName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.form.updateUserId = this.user.userId;
            putArea(this.form.areaId, this.form).then(() => {
              this.dialogFormVisible = false;
              this.getTree();
              this.getList();
              this.$message({
                showClose: true,
                message: '创建成功',
                type: 'success',
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
      },
      resetForm() {
        this.form = {
          areaId: undefined,
          areaName: undefined,
          areaCode: undefined,
          latitude: undefined,
          administrativeLevel: parseInt(this.level) + parseInt(1),
          pAreaId: this.listQuery.areaId,
          areaStatus: '1',
          sortId: undefined,
          comments:undefined,
          createTime:undefined,
          createUserId:undefined,
          updateTime:undefined,
          updateUserId:undefined
        }
      }
    }
  }
</script>
