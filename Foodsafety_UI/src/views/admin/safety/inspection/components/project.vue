<template>
  <div>
    <el-table :key="tableKey" :data="list" v-loading.body="listLoading"
              border fit highlight-current-row style="width: 100%;margin-bottom: 15px"
              highlight-current-row height="40vh">
      <el-table-column width="80px" align="center" label="序号" prop="inspectionDate">
        <template slot-scope="scope">
          {{scope.$index+1}}
        </template>
      </el-table-column>

      <!--<el-table-column width="150px" align="center" label="项目类型" prop="projectCategory" :formatter="categoryFormatter">

      </el-table-column>-->

      <el-table-column width="100px" align="center" label="重要程度" prop="importanceDegree" :formatter="importanceDegreeFormatter">

      </el-table-column>

      <el-table-column width="250px" align="center" label="检查项目" prop="projectName">

      </el-table-column>

      <el-table-column width="300px" align="center" label="检查内容" prop="projectContent" :show-overflow-tooltip="true">
        <!--<template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            {{ scope.row.projectContent }}
            <div slot="reference" class="name-wrapper">
              {{ scope.row.projectContent }}
            </div>
          </el-popover>
        </template>-->
      </el-table-column>

      <el-table-column width="300px" align="center" label="检查方法" prop="inspectionMethod" :show-overflow-tooltip="true">
        <!--<template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            {{ scope.row.inspectionMethod }}
            <div slot="reference" class="name-wrapper">
              {{ scope.row.inspectionMethod }}
            </div>
          </el-popover>
        </template>-->
      </el-table-column>

      <el-table-column width="250px" align="center" label="判定标准" prop="criterion" :show-overflow-tooltip="true">

      </el-table-column>

      <el-table-column align="center" label="检查结果" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <el-radio-group :disabled="existResult" v-model="result[scope.$index].resultsQualified" size="medium">
              <el-radio-button v-for="(item, index) in scope.row.resultsQualified.split(' ')" :label="index">{{item}}</el-radio-button>
            </el-radio-group>
            <div slot="reference" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" v-if="result[scope.$index].resultsQualified == undefined">
                请选择
              </el-tag>
              <el-tag size="medium" :closable="!existResult" @close="clearQualified(scope.$index)" type="success" v-else>
                {{ scope.row.resultsQualified.split(' ')[result[scope.$index].resultsQualified] }}
              </el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column align="center" label="检查结果" width="150" v-else>
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <el-radio-group :disabled="existResult" v-model="result[scope.$index].resultsQualified" size="medium">
              <el-radio-button v-for="(item, index) in scope.row.resultsQualified.split(' ')" :label="index">{{item}}</el-radio-button>
            </el-radio-group>
            <div slot="reference" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" v-if="result[scope.$index].resultsQualified == undefined">
                请选择
              </el-tag>
              <el-tag size="medium" :closable="!existResult" @close="clearQualified(scope.$index)" type="success" v-else>
                {{ scope.row.resultsQualified.split(' ')[result[scope.$index].resultsQualified] }}
              </el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" label="不合格情况" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <el-checkbox-group :disabled="existResult" v-model="result[scope.$index].resultsUnqualified" size="medium">
              <div style="height: 30px; line-height: 30px" v-for="(item, index) in scope.row.resultsUnqualified.split(' ')" >
                <el-checkbox :label="index">{{item}}</el-checkbox>
              </div>
            </el-checkbox-group>
            <div slot="reference" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" v-if="result[scope.$index].resultsUnqualified.length===0 && !existResult">
                请选择
              </el-tag>
              <el-tag size="medium" v-else-if="result[scope.$index].resultsUnqualified.length===0 && existResult">
                无
              </el-tag>
              <el-tag size="medium" :closable="!existResult" @close="clearUnqualified(scope.$index)" type="danger" v-else>
                {{result[scope.$index].resultsUnqualified.length}}个不合格情况
              </el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column align="center" label="不合格情况" width="150" v-else>
        <template slot-scope="scope">
          <el-popover trigger="click" placement="top">
            <el-checkbox-group :disabled="existResult" v-model="result[scope.$index].resultsUnqualified" size="medium">
              <div style="height: 30px; line-height: 30px" v-for="(item, index) in scope.row.resultsUnqualified.split(' ')" >
                <el-checkbox :label="index">{{item}}</el-checkbox>
              </div>
            </el-checkbox-group>
            <div slot="reference" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" v-if="result[scope.$index].resultsUnqualified.length===0 && !existResult">
                请选择
              </el-tag>
              <el-tag size="medium" v-else-if="result[scope.$index].resultsUnqualified.length===0 && existResult">
                无
              </el-tag>
              <el-tag size="medium" :closable="!existResult" @close="clearUnqualified(scope.$index)" type="danger" v-else>
                {{result[scope.$index].resultsUnqualified.length}}个不合格情况
              </el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" label="现场照片" width="150" fixed="right" v-if="needFixedRight">
        <template slot-scope="scope">
          <div v-show="!existResult">
            <el-upload v-show="result[scope.$index].resultsPhoto.length===0"
                       action="https://jsonplaceholder.typicode.com/posts/"
                       list-type="picture"
                       :file-list="result[scope.$index].resultsPhoto"
                       :on-change="handleChange"
                       :limit="1"
                       :auto-upload="false">
              <span @click="handleSaveIndex(scope.$index)">
                <el-tag size="medium">请上传</el-tag>
              </span>
            </el-upload>
            <div slot="reference" @click="handlePhotoPreview(scope.$index)" class="name-wrapper" style="cursor:pointer" v-show="result[scope.$index].resultsPhoto.length>0">
              <el-tag size="medium" :closable="!existResult" @close="clearPhoto(scope.$index)" type="warning">
                {{result[scope.$index].resultsPhoto.length}}张现场照片
              </el-tag>
            </div>
          </div>
          <div v-show="existResult">
            <div slot="reference" @click="handlePhoto(result[scope.$index].resultsPhoto[0])" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" :closable="!existResult" type="warning">
                {{result[scope.$index].resultsPhoto.length}}张现场照片
              </el-tag>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="现场照片" width="150" v-else>
        <template slot-scope="scope">
          <div v-show="!existResult">
            <el-upload v-show="result[scope.$index].resultsPhoto.length===0"
                       action="https://jsonplaceholder.typicode.com/posts/"
                       list-type="picture"
                       :file-list="result[scope.$index].resultsPhoto"
                       :on-change="handleChange"
                       :limit="1"
                       :auto-upload="false">
              <span @click="handleSaveIndex(scope.$index)">
                <el-tag size="medium">请上传</el-tag>
              </span>
            </el-upload>
            <div slot="reference" @click="handlePhotoPreview(scope.$index)" class="name-wrapper" style="cursor:pointer" v-show="result[scope.$index].resultsPhoto.length>0">
              <el-tag size="medium" :closable="!existResult" @close="clearPhoto(scope.$index)" type="warning">
                {{result[scope.$index].resultsPhoto.length}}张现场照片
              </el-tag>
            </div>
          </div>
          <div v-show="existResult">
            <div slot="reference" @click="handlePhoto(result[scope.$index].resultsPhoto[0])" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" :closable="!existResult" type="warning">
                {{result[scope.$index].resultsPhoto.length}}张现场照片
              </el-tag>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="整改情况" width="150" fixed="right" v-if="needFixedRight && existResult">
        <template slot-scope="scope">
          <div v-if="isRevisit && result[scope.$index].resultsUnqualified.length>0">
            <el-popover trigger="click" placement="top">
              <el-radio-group v-model="result[scope.$index].isRectified" size="medium">
                <el-radio-button v-for="item in isRectifiedOptions" :label="item.value">{{item.text}}</el-radio-button>
              </el-radio-group>
              <div slot="reference" class="name-wrapper" style="cursor:pointer">
                <el-tag size="medium" v-if="result[scope.$index].isRectified == undefined">
                  请选择
                </el-tag>
                <el-tag size="medium" :closable="isRevisit" @close="clearRectified(scope.$index)" type="success" v-else>
                  {{ rectifiedFormatter(result[scope.$index].isRectified) }}
                </el-tag>
              </div>
            </el-popover>
          </div>
          <div v-else-if="!isRevisit && result[scope.$index].isRectified">
            <!--<el-tag size="medium" :closable="false" type="success">
              {{ rectifiedFormatter(result[scope.$index].isRectified) }}
            </el-tag>-->
            <el-popover trigger="click" placement="top">
              <el-radio-group :disabled="existResult" v-model="result[scope.$index].isRectified" size="medium">
                <el-radio-button v-for="item in isRectifiedOptions" :label="item.value">{{item.text}}</el-radio-button>
              </el-radio-group>
              <div slot="reference" class="name-wrapper" style="cursor:pointer">
                <el-tag size="medium" :closable="isRevisit" @close="clearRectified(scope.$index)" type="success">
                  {{ rectifiedFormatter(result[scope.$index].isRectified) }}
                </el-tag>
              </div>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="整改情况" width="150" v-else-if="!needFixedRight && existResult">
        <template slot-scope="scope">
          <div v-if="isRevisit && result[scope.$index].resultsUnqualified.length>0">
            <el-popover trigger="click" placement="top">
              <el-radio-group v-model="result[scope.$index].isRectified" size="medium">
                <el-radio-button v-for="item in isRectifiedOptions" :label="item.value">{{item.text}}</el-radio-button>
              </el-radio-group>
              <div slot="reference" class="name-wrapper" style="cursor:pointer">
                <el-tag size="medium" v-if="result[scope.$index].isRectified == undefined">
                  请选择
                </el-tag>
                <el-tag size="medium" :closable="isRevisit" @close="clearRectified(scope.$index)" type="success">
                  {{ rectifiedFormatter(result[scope.$index].isRectified) }}
                </el-tag>
              </div>
            </el-popover>
          </div>
          <div v-else-if="!isRevisit && result[scope.$index].isRectified">
            <!--<el-tag size="medium" :closable="false" type="success">
            {{ rectifiedFormatter(result[scope.$index].isRectified) }}
            </el-tag>-->
            <el-popover trigger="click" placement="top">
              <el-radio-group :disabled="existResult" v-model="result[scope.$index].isRectified" size="medium">
                <el-radio-button v-for="item in isRectifiedOptions" :label="item.value">{{item.text}}</el-radio-button>
              </el-radio-group>
              <div slot="reference" class="name-wrapper" style="cursor:pointer">
                <el-tag size="medium" :closable="isRevisit" @close="clearRectified(scope.$index)" type="success">
                  {{ rectifiedFormatter(result[scope.$index].isRectified) }}
                </el-tag>
              </div>
            </el-popover>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="整改后照片" width="150" fixed="right" v-if="needFixedRight && existResult">
        <template slot-scope="scope">
          <div v-show="isRevisit && result[scope.$index].resultsUnqualified.length>0">
            <el-upload v-show="result[scope.$index].phoneRectified.length===0"
                       action="https://jsonplaceholder.typicode.com/posts/"
                       list-type="picture"
                       :file-list="result[scope.$index].phoneRectified"
                       :on-change="handleChangeRectified"
                       :limit="1"
                       :auto-upload="false">
              <span @click="handleSaveIndex(scope.$index)">
                <el-tag size="medium">请上传</el-tag>
              </span>
            </el-upload>
            <div slot="reference" @click="handlePhotoRectifiedPreview(scope.$index)" class="name-wrapper" style="cursor:pointer" v-show="result[scope.$index].phoneRectified.length>0">
              <el-tag size="medium" :closable="isRevisit" @close="clearPhotoRectified(scope.$index)" type="warning">
                {{result[scope.$index].phoneRectified.length}}张整改后照片
              </el-tag>
            </div>
          </div>
          <div v-show="!isRevisit && result[scope.$index].phoneRectified.length>0">
            <div slot="reference" @click="handlePhoto(result[scope.$index].phoneRectified[0])" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" :closable="isRevisit" type="warning">
                {{result[scope.$index].phoneRectified.length}}张整改后照片
              </el-tag>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="整改后照片" width="150" v-else-if="!needFixedRight && existResult">
        <template slot-scope="scope">
          <div v-show="isRevisit && result[scope.$index].resultsUnqualified.length>0">
            <el-upload v-show="result[scope.$index].phoneRectified.length===0"
                       action="https://jsonplaceholder.typicode.com/posts/"
                       list-type="picture"
                       :file-list="result[scope.$index].phoneRectified"
                       :on-change="handleChangeRectified"
                       :limit="1"
                       :auto-upload="false">
              <span @click="handleSaveIndex(scope.$index)">
                <el-tag size="medium">请上传</el-tag>
              </span>
            </el-upload>
            <div slot="reference" @click="handlePhotoRectifiedPreview(scope.$index)" class="name-wrapper" style="cursor:pointer" v-show="result[scope.$index].phoneRectified.length>0">
              <el-tag size="medium" :closable="isRevisit" @close="clearPhotoRectified(scope.$index)" type="warning">
                {{result[scope.$index].phoneRectified.length}}张整改后照片
              </el-tag>
            </div>
          </div>
          <div v-show="!isRevisit && result[scope.$index].phoneRectified.length>0">
            <div slot="reference" @click="handlePhoto(result[scope.$index].phoneRectified[0])" class="name-wrapper" style="cursor:pointer">
              <el-tag size="medium" :closable="isRevisit" type="warning">
                {{result[scope.$index].phoneRectified.length}}张整改后照片
              </el-tag>
            </div>
          </div>
        </template>
      </el-table-column>


    </el-table>

    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>

  </div>
</template>

<script>
  import { getTemplateAllProject } from "api/admin/safety/inspection/template/project";
  import { getAllCategory } from "api/admin/safety/inspection/template/category";
  import { getResource } from "api/admin/resource/index";
  import { mapGetters } from "vuex";
  import { parseValueToText } from "utils/index";
  export default {
    props: {
      height: {
        default: undefined
      },
      templateId: {
        default: undefined
      },
      existResult: {
        default: undefined
      },
      setProjectResult: {
        type: Function,
        default: undefined
      },
      isRevisit: {
        default: undefined
      }
    },
    watch: {
      'templateId': function() {
        this.getList();
      }
    },
    data() {
      return {
        list: null,
        listLoading: true,
        dialogImageVisible: false,//图片预览窗口是否显示
        dialogImageUrl: undefined,//预览图片的地址
        tableKey: 0,
        needFixedRight:false,
        allCategory: [],
        result: [],
        currentIndex: undefined,
        serverImageUrl:process.env.SERVERIMAGEURL,
        fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
      };
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
      };
    },
    created() {
      this.getCategory();
    },
    computed: {
      ...mapGetters(["staticData"]),
      isRectifiedOptions() {
        return this.staticData["整改情况"];
      },
    },
    methods: {
      getProjectResult() {
        return this.result;
      },
      getCategory(){
        getAllCategory().then((response)=>{
          this.allCategory = response;
        }).catch(()=>{
          this.$message({
            type:'error',
            showClose: true,
            message: '项目分类信息获取失败。'
          });
        });
      },
      getList() {
        // debugger
        if(this.templateId) {
          this.result.length = 0;
          this.listLoading = true;
          getTemplateAllProject({templateId:this.templateId, projectStatus:1}).then(response => {
            this.list = response;
            this.listLoading = false;
            this.list.forEach(item => {
              let nObj = Object.assign({}, item, {isRectified: undefined, phoneRectified:[], resultsQualified: undefined, resultsUnqualified: [], resultsPhoto: []});
              this.result.push(nObj);
            });
            // 请求父组件获取项目的检查结果
            if (this.setProjectResult) {
              let resultList = this.setProjectResult();
              // console.log(resultList)
              if (resultList.length > 0) {
                this.result.forEach(item => {
                  resultList.forEach(result => {
                    if (item.projectId === result.projectId) {
                      item.itemId = result.itemId;
                      item.isRectified = result.isRectified
                      if(result.phoneRectified.length > 0)
                        item.phoneRectified.push({id: result.phoneRectified});
                      item.resultsQualified = result.resultsQualified;
                      item.resultsUnqualified = result.resultsUnqualified;
                      item.resultsPhoto.push({id: result.resultsPhoto});
                      // return; 无法跳出循环
                    }
                  });
                });
              }
            }
            // console.log(this.result)
          });
        } else {
          this.list.length = 0;
          this.listLoading = false;
        }
      },
      categoryFormatter(row, column, cellValue) {
        if(!cellValue){return ''}
        if(!this.allCategory.length){return cellValue}
        return this.allCategory.filter((item)=>{
          return item.categoryId === cellValue;
        })[0].categoryName;
      },
      importanceDegreeFormatter(row, column, cellValue) {
        return parseValueToText(cellValue, this.staticData["重要程度"]);
      },
      rectifiedFormatter(cellValue) {
        return parseValueToText(cellValue, this.staticData["整改情况"]);
      },
      clearQualified(index) {
        this.result[index].resultsQualified = undefined;
      },
      clearUnqualified(index) {
        this.result[index].resultsUnqualified = [];
      },
      clearPhoto(index) {
        this.result[index].resultsPhoto = [];
      },
      clearPhotoRectified(index){
        this.result[index].phoneRectified = [];
      },
      clearRectified(index) {
        this.result[index].isRectified = undefined;
      },
      handleSaveIndex(index) {
        this.currentIndex = index;
      },
      handleChange(file, fileList) {
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.result[this.currentIndex].resultsPhoto.push(file);
        //console.log(this.result[0].resultsPhoto)
        // if(this.result[this.currentIndex].resultsPhoto.length>0){
        //   $(".el-table__row current-row .el-upload--text").hide();
        // }
      },
      handleChangeRectified(file, fileList) {
        if(this.fileFormat.indexOf(file.raw.type) < 0 ){
          this.$message({
            type:'error',
            showClose: true,
            message: '只支持".jpg、.png、.bmp、.gif"格式的图片。'
          });
          return false;
        }
        this.result[this.currentIndex].phoneRectified.push(file);
      },
      handlePhotoPreview(index) {
        let file = this.result[index].resultsPhoto[0];
        this.dialogImageUrl = file.url;
        this.dialogImageVisible = true;
      },
      handlePhotoRectifiedPreview(index) {
        let file = this.result[index].phoneRectified[0];
        //console.log(file);
        if (file.url) {
          this.dialogImageUrl = file.url;
          this.dialogImageVisible = true;
        } else {
          this.handlePhoto(file);
        }
      },
      handlePhoto(file){
        getResource(file.id).then(response => {
          if (response.rel) {
            this.dialogImageUrl = this.serverImageUrl+'/'+response.data.resourcePath;
            this.dialogImageVisible = true;
          }else{
            this.$message({
              type:'error',
              showClose: true,
              message: `资源获取失败！`
            });
          }
        });
      }
    }
  };
</script>
