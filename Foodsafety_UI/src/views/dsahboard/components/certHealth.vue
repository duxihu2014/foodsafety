<template>
  <div >
    <el-table :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%"   :row-class-name="tableRowClassName" height="60vh"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="员工编号" width="100" prop="staffId"></el-table-column>
      <el-table-column align="center" label="企业名称" width="250" prop="enterpriseName"></el-table-column>
      <el-table-column align="center" label="姓名" width="150" prop="staffName"></el-table-column>
      <el-table-column align="center" label="证件号码" width="200" prop="idCardNo" ></el-table-column>
      <el-table-column align="center" label="所属部门" width="150" prop="department" ></el-table-column>
      <el-table-column align="center" label="健康证号" width="150" prop="certificateNumber" ></el-table-column>
      <el-table-column align="center" label="有效截至日期" width="180" prop="validDate" >
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.validDate }}</span>
          <el-tag size="mini" :type="getValidDateType(scope.row.validStatus)" prop="validDate"  v-show="scope.row.validStatus!=1">{{statusFormatter(scope.row.validStatus)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column  align="center" fixed="right" label="操作" width="100"  v-if="needFixedRight" >
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
         </template>
      </el-table-column>
      <el-table-column  align="center"   label="操作" width="100"  v-else>
        <template slot-scope="scope">
         <el-button size="mini" type="primary"  @click="handleView(scope.row)">查看</el-button>
          </template>
      </el-table-column>

    </el-table>
    <div v-show="!listLoading" class="pagination-container" >
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>

    <el-dialog  title="查看" :visible.sync="dialogFormVisible" :close-on-click-modal="false" append-to-body>
      <div slot="title">
        <template >
          <i class="el-icon-view"> 查看</i>
        </template>
      </div>
      <el-form :model="form"   ref="form" label-width="120px">
        <el-tabs >
          <el-tab-pane label="健康证信息">
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="staffName" >
                  <el-input v-model.trim="form.staffName" placeholder=""   :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工号" prop="employeeNumber" >
                  <el-input  v-model.trim="form.employeeNumber"  :readonly="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="健康证号" prop="certificateNumber"  ref="certificateNumber" tab="1">
                  <el-input   v-model.trim="form.certificateNumber"    :readonly="viewReadOnly"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="体检时间" prop="examinationDate"  ref="examinationDate" tab="1">
                  <el-date-picker v-model="form.examinationDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
               <el-col :span="12">
                <el-form-item label="有效期" prop="validDate"  ref="validDate" tab="1">
                  <el-date-picker v-model="form.validDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions2"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发证时间" prop="issuingDate"  ref="issuingDate" tab="1">
                  <el-date-picker v-model="form.issuingDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" :picker-options="pickerOptions"  :readonly="viewReadOnly"></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item label="发证机构" prop="issuingUnit"  ref="issuingUnit" tab="1">
                    <el-input   v-model.trim="form.issuingUnit"    :readonly="viewReadOnly"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="24">
                  <el-form-item label="健康证照片" prop="imageFile"  ref="imageFile" tab="1">

                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :file-list="listFile"
                      :on-preview="handlePictureCardPreview"
                      :limit="1"
                      :auto-upload="false">
                      <i class="el-icon-plus"></i>
                    </el-upload>

                  </el-form-item>
                </el-col>
              </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-dialog>

    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>

  import { page, add,update, getObj} from "api/admin/staff/certificate/index";
  import { parseValueToText } from "utils/index";
  import { mapGetters } from "vuex";
  import {getToken} from 'utils/auth';

  export default {
    name: "health",
    props: ["gridId"],
    data(){
          return{
            list: null,
            total: null,
            listLoading: false,
            listQuery: {
              page: 1,
              limit: 20,
              staffName: "",
              idCardNo: "",
              validStatus:"4",
              areaId: undefined,
              gridId: undefined,
              enterpriseId: undefined
            },
            form: {
              staffId:undefined,
              imageFile:undefined
            },

            dialogFormVisible: false,
            viewReadOnly:false, //控制查看时的按钮显示
            dialogStatus: "",
            textMap: {
              update: "编辑",
              create: "创建",
              view: "查看"
            },
            manager_update:true,
            selection: [],
            needFixedRight:false,
            listFile:[],
            serverImageUrl:process.env.SERVERIMAGEURL,
            tabPosition: "0",
            dialogImageUrl: '',
            dialogImageVisible: false,
            fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
            pickerOptions: {
              disabledDate(time) {
                return time.getTime() > Date.now();
              },
            },
            pickerOptions2: {
              disabledDate(time) {
                return time.getTime() < Date.now();
              },
            }
          }
        },
      created() {
        this.getList();
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
      },
      computed: {
        ...mapGetters(["staticData","user"]),
        statusOptions() {
          return this.staticData["健康证状态"];
        },
        educationOptions(){
          return this.staticData["学历"];
        },
        sexOptions(){
          return this.staticData["性别"];
        },
        workTypeOptions(){
          return this.staticData["行业工种"];
        }
      },
      methods: {
        setAreaId(data){
          this.listQuery.areaId = data;
        },
        setGridId(data){
          this.listQuery.gridId = data;
        },
        setEnterpriseId(data){
          this.listQuery.enterpriseId = data;
        },
        imageClick(){
          this.dialogVisible=true;
          this.dialogImageUrl=this.imageUrl;
        },
         getValidDateType(validStatus){
          switch (validStatus){
            case '2':
              return 'warning';
            case '3':
              return 'danger';
          }
         },
        tableRowClassName({row, rowIndex}) {
          if (row.staffStatus === '0') {
            return 'warning-row';
          } else if (row.staffStatus === '1') {
            return 'success-row';
          }
          return '';
        },
        getList() {
          this.listLoading = true;
          this.listQuery.gridId=this.gridId;
          page(this.listQuery).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.listLoading = false;
          });

        },
        handleSizeChange(val) {
          this.listQuery.limit = val;
          this.getList();
        },
        handleCurrentChange(val) {
          this.listQuery.page = val;
          this.getList();
        },
        handleView(row) {
          getObj(row.staffId).then(response => {
            this.form = response.data;
            if(response.data.certificatePhoto) {
              this.listFile.push({"url":this.serverImageUrl+'/'+response.data.resourcePath});
              this.$nextTick(function () {
                $(".el-upload--picture-card:eq(0)").hide();
                $(".el-icon-delete:eq(0)").hide();
              });
            }
            this.manager_update = false;
            this.viewReadOnly=true;
            this.dialogFormVisible = true;
            this.dialogStatus = "view";
          });

        },
        statusFormatter( cellValue) {
          return parseValueToText(cellValue, this.statusOptions);
        },
        sexFormatter(row, column, cellValue) {
          return parseValueToText(cellValue, this.sexOptions);
        },
        // 点击"+"图标时显示弹出框
        handlePictureCardPreview(file) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
        }
      }
    }
</script>

