<template>
  <div class="app-container calendar-list-container">
    <fieldset class="fieldset">
      <legend class="legend">查询条件</legend>
      <div class="field-box">
        <el-form :inline="true" :model="listQuery">
          <el-form-item label="企业名称" prop="enterpriseName"  class="filter-item">
          <el-input @keyup.enter.native="handleFilter" style="width: 200px;"  placeholder=""  v-model.trim="listQuery.enterpriseNameLike"> </el-input>
        </el-form-item>
          <el-form-item label="营业执照编号" prop="certificateNo"  class="filter-item">
            <el-input @keyup.enter.native="handleFilter" style="width: 200px;"  placeholder=""  v-model.trim="listQuery.certificateNoLike"> </el-input>
          </el-form-item>
          <el-form-item label="注册状态" prop="registerStatus" class="filter-item">
            <el-select placeholder="请选择" v-model.trim="listQuery.registerStatus">
              <!--<el-option label="待审核" value="0"> </el-option>-->
              <!--<el-option label="已通过" value="1"> </el-option>-->
              <!--<el-option label="未通过" value="2"> </el-option>-->
              <el-option v-for="item in  registerStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="filter-item">
            <el-button type="primary" v-waves  @click="handleFilter">搜索</el-button>
            <el-button  v-waves  @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </fieldset>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border  highlight-current-row style="width: 100%" @selection-change="getSelection"  :row-class-name="tableRowClassName" :height="height"	>
      <el-table-column  type="selection"  width="55"></el-table-column>
      <el-table-column align="center" label="注册编号" width="100" prop="registerId"></el-table-column>
      <el-table-column align="center" label="企业名称" width="250" prop="registerBase.enterpriseName" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="社会信用代码" width="180" prop="registerBase.organizingInstitutionBarCode" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="主体分类" width="180" prop="registerBase.subjectClassification" :show-overflow-tooltip="true" :formatter="subjectClassificationFormatter"></el-table-column>
      <el-table-column align="center" label="联系人" width="100" prop="registerBase.contacts" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="联系电话" width="150" prop="registerBase.contactNumber" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册地址" width="300" prop="registerBase.registerAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="生产地址" width="300" prop="registerBase.productionAddress" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="营业执照编号" width="180" prop="registerCertificate.certificateNo" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column align="center" label="注册状态" width="100" prop="registerStatus" :formatter="registerStatusFormatter"></el-table-column>
      <el-table-column  align="center"  label="操作" width="100" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.registerStatus === 0" size="mini" type="success" @click="handleAudit(scope.row)">审核</el-button>
          <el-button v-else size="mini" :disabled="true">审核</el-button>
          <!-- <el-button size="mini" type="success" @click="handleAudit(scope.row)">审核</el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total"> </el-pagination>
    </div>
    <el-dialog  :title="dialogTitle" :visible.sync="dialogVisible" width="80%" center class="enterpriseDialog">
      <div>
        <el-tabs v-model="activeName">
          <el-tab-pane label="企业基本信息" name="first">
            <el-form label-width="120px" :model="form.registerBase" >
              <el-row>
                <el-col :span="22">
                  <el-form-item label="企业名称" prop="enterpriseName">
                    <el-input v-model="form.registerBase.enterpriseName" placeholder="企业名称" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="法人代表" prop="corporateRepresentative">
                    <el-input v-model="form.registerBase.corporateRepresentative" placeholder="法人代表" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="社会信用代码" prop="organizingInstitutionBarCode">
                    <el-input v-model="form.registerBase.organizingInstitutionBarCode" placeholder="社会信用代码" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="证件类型" prop="idType">
                    <el-select  v-model="form.registerBase.idType" placeholder="请选择" :clearable="true">
                      <el-option v-for="item in idTypeOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="证件号码" prop="idCardNo">
                    <el-input v-model.trim="form.registerBase.idCardNo" placeholder="证件号码" :clearable="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item label="主体分类" prop="subjectClassification">
                    <el-select  v-model="form.registerBase.subjectClassification" placeholder="请选择" :disabled="true">
                      <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="11" >
                  <el-form-item label="所属区域" prop="areaOptions">
                    <el-cascader
                      v-model="areaOptions"
                      :options="area"
                      :show-all-levels="true"
                      expand-trigger="hover"
                      :change-on-select="true"
                      :disabled="true"
                      :props="{label:'text', value: 'id'}">
                    </el-cascader>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="注册资金" prop="registeredCapital">
                    <el-input v-model="form.registerBase.registeredCapital" placeholder="注册资金" :readonly="true">
                      <template slot="append">(万元)</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="经济性质" prop="economicNature">
                    <el-select  v-model="form.registerBase.economicNature" placeholder="请选择"  :disabled="true">
                      <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="联系人" prop="contacts">
                    <el-input v-model="form.registerBase.contacts" placeholder="联系人" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="联系电话" prop="contactNumber">
                    <el-input v-model="form.registerBase.contactNumber" placeholder="联系电话" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="联系人手机号" prop="contactMobile">
                    <el-input v-model="form.registerBase.contactMobile" placeholder="联系人手机号" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="检验员手机" prop="inspectorMobile">
                    <el-input v-model="form.registerBase.inspectorMobile" placeholder="检验员手机" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="邮政编码" prop="postalCode">
                    <el-input v-model="form.registerBase.postalCode" placeholder="邮政编码" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="企业注册日期" prop="registerDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerBase.registerDate" style="width: 100%;" :readonly="true" :clearable="true"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="注册地址" prop="registerAddress">
                    <el-input v-model="form.registerBase.registerAddress" placeholder="注册地址" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="生产地址" prop="productionAddress">
                    <el-input v-model="form.registerBase.productionAddress" placeholder="生产地址" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row >
                <el-col :span="22">
                  <el-form-item label="经营范围" prop="operationScope">
                    <el-input type="textarea" :rows="2" v-model="form.registerBase.operationScope" placeholder="经营范围" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="营业执照信息" name="second">
            <el-form :model="form.registerCertificate" status-icon label-width="100px" >
              <el-row >
                <el-col :span="11">
                  <el-form-item label="营业执照号"  prop="certificateNo">
                    <el-input v-model="form.registerCertificate.certificateNo" placeholder="营业执照号" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="发证单位" prop="issuingUnit">
                    <el-input v-model="form.registerCertificate.issuingUnit" placeholder="发证单位" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="11">
                  <el-form-item label="发证日期" prop="issuingDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerCertificate.issuingDate" style="width: 100%;" :readonly="true"></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="有效期" prop="validDate">
                    <el-date-picker type="date" placeholder="请选择" v-model="form.registerCertificate.validDate" style="width: 100%;" :readonly="true"></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="22">
                  <el-form-item label="登记范围" prop="registerScope">
                    <el-input type="textarea" :rows="5" v-model="form.registerCertificate.registerScope" placeholder="登记范围" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row >
                <el-col :span="22">
                  <el-form-item label="营业执照">
                    <el-upload
                      action="https://jsonplaceholder.typicode.com/posts/"
                      list-type="picture-card"
                      :file-list="imgFile"
                      :on-preview="handlePictureCardPreview"
                      :limit="1"
                      :disabled="true"
                      :auto-upload="false">
                      <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog  :visible.sync="dialogImageVisible" size="tiny"  :close-on-click-modal="false" append-to-body>
                      <img width="100%" :src="dialogImageUrl" alt="">
                    </el-dialog>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
        <span slot="footer" class="dialog-footer">
            <el-button type="primary" icon="el-icon-check" @click="enterpriseAudit(1)">通 过</el-button>
            <el-button type="primary" icon="el-icon-close" @click="enterpriseAudit(0)">不通过</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="enterpriseAudit(-1)">作 废</el-button>
        </span>
    </el-dialog>

    <el-dialog  title="填写不通过原因" width="50%" :visible.sync="auditDialogVisible" append-to-body @close="closeDialog">
      <div>
        <el-form :model="verifyForm" :rules="rules" ref="verifyForm">
          <el-form-item label="不通过原因:" prop="reason">
            <el-input type="textarea" :rows="5" v-model.trim="verifyForm.reason"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary"  @click="confirm()">确 认</el-button>
      </span>
    </el-dialog>

    <el-dialog  title="选择责任区域" width="50%" :visible.sync="mapDialogVisible" append-to-body @close="closeDialog">
      <div class="map_container">
        <el-amap
          ref="map"
          :center="center"
          :zoom="zoom"
          :events="mapEvents"
          :resizeEnable="true" >

          <template v-for="(grid, index) in otherGrids">
            <!-- 其它网格多边形 -->
            <el-amap-polygon v-if="grid.maps.length>0"
                             :ref="`polygon_${index}`"
                             :path="formatGrid(grid.maps)"
                             strokeColor="green"
                             strokeOpacity=".8"
                             :strokeWeight="2"
                             fillColor="#0AB5F5"
                             fillOpacity=".5"
                             :events="otherGridEvents"
                             :extData="grid.grid">
            </el-amap-polygon>
          </template>

          <!-- 网格信息窗口 -->
          <el-amap-info-window
            :content="mapWindowContent"
            :position="mapWindowPosition"
            :visible="mapWindowShow"
          ></el-amap-info-window>

        </el-amap>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary"  @click="confirm()">确 认</el-button>
      </span>
    </el-dialog>

  </div>

</template>

<script>
  import {getRegisterEnterprisePage,getResource,registerEnterpriseAudit} from "api/admin/enterprise/index";
  import { getOtherGrids } from "api/admin/supervisory/grid";
  import {getAreaTree} from "api/admin/register/index"
  import { mapGetters } from "vuex";
  import { parseValueToText ,parseTime,findParentNode} from "utils/index";
  import { loadGridHeight } from "api/screen";
  import { createDistrictPolygon } from 'api/model';
    export default {
      name: "verify",
      data(){
          return {
            height: undefined,
            serverImageUrl:process.env.SERVERIMAGEURL,
            imgFile:[],
            dialogImageVisible:false,//图片预览窗口是否显示
            dialogImageUrl:undefined,//预览图片的地址
            area:null,
            areaOptions:[],
            list: null,
            total: null,
            listLoading: true,
            listQuery:{
              enterpriseNameLike:undefined,
              certificateNoLike:undefined,
              registerStatus:"0",
              page: 1,
              limit: 20,
            },
            rules:{
              reason:[{required: true, message: "请输入不通过理由", trigger: "blur"}]
            },
            tableKey: 0,
            enterpriseSelection:[],
            form:{
              registerBase:{},
              registerCertificate:{},
            },
            dialogVisible:false,
            dialogTitle:"注册企业审核",
            activeName:"first",
            auditDialogVisible:false,
            mapDialogVisible: false,
            verifyForm:{
              registerId:undefined,
              reason:undefined,
              gridId:undefined
            },
            // 地图相关
            zoom: process.env.MAP_ZOOM, // 缩放等级
            center: process.env.CENTER_POSITION, // 初始位置
            mapEvents: { // 地图事件
              zoomchange : this.zoomChange,
              complete : this.loadComplete
            },
            position: ["0","0"], // 标记位置
            otherGrids:[],
            otherGridEvents: {
              click: this.gridClick,
              mouseover: this.mapWindowMouseover,
              mouseout: this.mapWindowMouseout,
            },
            mapWindowShow:false,
            mapWindowPosition:[0,0],
            mapWindowContent:"",
            map:undefined
          }
      },
      computed: {
        ...mapGetters(["user","staticData"]),
        subjectClassificationOptions(){
          return this.staticData["企业主体分类"];
        },
        registerStatusOptions(){
          return this.staticData["企业注册状态"];
        },
        economicNatureOptions(){
          return this.staticData["企业经济性质"];
        },
        idTypeOptions(){
          return this.staticData["证件类型"];
        }
      },
      mounted () {
        this.height = loadGridHeight();
      },
      created() {
        this.getList();
        this.getAreaTreeData();
        this.getGrid();
      },
      methods:{
        loadComplete(e) { // 地图加载完成事件
          this.map = this.$refs['map'].$$getInstance();
          createDistrictPolygon(process.env.CURRENT_CITY,this.map);
        },
        gridClick(e){
          let grid = e.target.getExtData();
          if(grid){
            this.verifyForm.gridId=grid.gridId;
          }
          for(let i=0;i<this.otherGrids.length;i++){
            let obj = this.$refs['polygon_'+i][0].$$getInstance();
            let options = obj.getOptions();
            options.strokeColor='green';
            options.fillColor='#0AB5F5';
            obj.setOptions(options);
          }
          let options = e.target.getOptions();
          options.strokeColor='red';
          options.fillColor='#FF5733';
          e.target.setOptions(options);
        },
        mapWindowMouseover(e){ // 其它网格多边形鼠标移入事件
          let grid = e.target.getExtData();
          if(grid){
            this.mapWindowPosition = [e.lnglat.lng, e.lnglat.lat];
            let showContent=`<div class="info_window_msg">
                    <p><span>区域名称：</span><span>${grid.gridName}</span></p>
                    <p><span>乡镇(街道)：</span><span>${grid.townStreet}</span></p>
                    <p><span>居委会(村)：</span><span>${grid.committeeVillage}</span></p>
                    <p><span>监督员：</span><span>${grid.monitorsName}</span></p>
                    <p><span>协管员：</span><span>${grid.assistantsName}</span></p>
                    <p><span>检验员：</span><span>${grid.standardsName}</span></p>
                    </div>`;
            this.mapWindowContent =`<div style="color: #3399FF;font-size: 12px;">${showContent}</div>`;

            this.$nextTick(function () {
              this.mapWindowShow = true;
            });
          }
        },
        mapWindowMouseout(){ // 其它网格多边形鼠标移出事件
          this.mapWindowShow = false;
        },
        zoomChange(e){ // 地图缩放事件
          this.zoom = this.$refs['map'].$$getInstance().getZoom();
        },
        getGrid() {
          getOtherGrids({institutionId:this.user.institutionId}).then((response)=>{
            this.otherGrids=this.mapGrouping(response.data);
          });
        },
        mapGrouping(mapdata){
          let a={};
          let b=[];
          mapdata.forEach((item)=>{
            if (typeof (a[item.gridId]) == 'undefined') {
              a[item.gridId] = b.length;
              b[a[item.gridId]]={grid:item,maps:[]};
            }
            b[a[item.gridId]].maps.push(item);
          });
          return b;
        },
        formatGrid(grid){ // 对象转数组
          return grid.map(item=>{
            return  [item.lng, item.lat];
          });
        },
        getList(){
          console.log(this.user.areaId)
            getRegisterEnterprisePage(this.listQuery).then(response => {
              this.list = response.rows;
              this.total = response.total;
              this.listLoading = false;
            });

          },
        getAreaTreeData(){
          getAreaTree().then(response=> {
            this.area=response;
          })
        },
        handleFilter(){
          this.listQuery.page=1;
          this.getList();
        },
        resetQuery() {
          let page = this.listQuery.page;
          let limit = this.listQuery.limit;
          this.listQuery = { page: page,limit: limit, enterpriseNameLike: undefined, certificateNoLike:undefined, registerStatus: "0"};
        },
        handleSizeChange(val) {
          this.listQuery.limit = val;
          this.getList();
        },
        handleCurrentChange(val) {
          this.listQuery.page = val;
          this.getList();
        },
        getSelection(selection) {
          this.enterpriseSelection = selection;
        },
        tableRowClassName({row, rowIndex}) {
          if (row.compStatus === '0') {
            return 'warning-row';
          } else if (row.compStatus === '1') {
            return 'success-row';
          }
          return '';
        },
        //审核查看
        handleAudit(row){
          let _self = this;
          _self.activeName="first";
          let param ={"registerId":row.registerId};
          _self.dialogVisible=true;
          getRegisterEnterprisePage(param).then(response => {
            _self.form=response.rows[0];
            let str = findParentNode(_self.area,response.rows[0].registerBase.areaId);
            _self.areaOptions=str.split(",");
            _self.areaOptions.push(response.rows[0].registerBase.areaId+'')
            let resourceId = response.rows[0].registerCertificate.certificatePhoto;
            getResource(resourceId).then(response =>{
              _self.imgFile=[];
              _self.imgFile.push({"url":_self.serverImageUrl+'/'+response.data.resourcePath});
              $(".el-upload--picture-card").hide();
            })
          })
        },
        //审核 1.审核通过  0.审核不通过  -1.作废
        enterpriseAudit(value){
          this.verifyForm.registerId=this.form.registerId;
          this.verifyForm.verifyResult=value;
          if(value==1){
            //this.confirm();
            this.mapDialogVisible=true;
          }else if(value==0){
            this.auditDialogVisible=true;
          }else if(value==-1){
            this.deleteForm();
          }
        },
        //取消提交
        cancel(){
          this.auditDialogVisible=false;
          this.mapDialogVisible=false;
        },
        //确认提交
        confirm(){
          let flag = true;
          if(this.auditDialogVisible){
            this.$refs["verifyForm"].validate((valid) => {
              if(!valid){
                flag=false;
              }
            })
          }
          if(this.mapDialogVisible){
            if(!this.verifyForm.gridId){
              flag=false;
              this.$message({
                showClose: true,
                message: "请选择一个责任区域"
                // type: 'warning'
              });
            }
          }
          if(flag){
           registerEnterpriseAudit(this.verifyForm).then(response =>{
              this.auditDialogVisible=false;
              this.mapDialogVisible=false;
              this.dialogVisible=false;
              if(response.rel){
                this.$message({
                  type:'success',
                  showClose: true,
                  message: `操作成功！`
                });
              }else {
                this.$message({
                  type:'error',
                  showClose: true,
                  message: `操作失败！`
                });
              }
              this.getList();
            })
          }
        },
        closeDialog(){
          this.verifyForm.reason=undefined;
          this.verifyForm.gridId=undefined;
          this.center=process.env.CENTER_POSITION;
          this.zoom=process.env.MAP_ZOOM;
          this.$nextTick(function() {
            this.$refs['verifyForm'].clearValidate();
          });
        },
        //作废
        deleteForm(){
          this.$confirm('此操作将永久删除数据, 是否继续?', '提示',{
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.confirm();
          }).catch(() => {
            /*this.$message({
              type: 'info',
              message: '已取消操作'
            });*/
          });
        },
        // 点击"+"图标时显示弹出框
        handlePictureCardPreview(file) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
        },

        //企业主体分类格式化
        subjectClassificationFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业主体分类"]);
        },
        registerStatusFormatter(row, column, cellValue){
          return parseValueToText(cellValue, this.staticData["企业注册状态"]);
        },
      }
    }
</script>

<style >
  .enterpriseDialog >.el-dialog--center .el-dialog__body {
    text-align: initial;
    padding: 10px 25px 30px;
  }
  .map_container{
    position: relative;
    width: 100%;
    height: 400px;
  }
</style>
