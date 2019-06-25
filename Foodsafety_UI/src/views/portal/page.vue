<template>
  <div class="app-container">
    <div class="column">
      <div class="column_title">
        <span>
          <i class="fa fa-newspaper-o"> {{ this.columnParams.columnName }}</i>
        </span>
      </div>
      <div class="column_content">
        <ul style="overflow: auto" ref="content">
          <li v-for="content in contentArray" @click.stop.prevent="handlerView(content)">
            <span class="spanTitle">{{ content.contentTitle }}</span>
            <span class="spanTime">{{ parseDate(content.publishTime) }}</span>
          </li>
        </ul>
      </div>
    </div>

    <div style="text-align: center">
      <el-pagination background layout="prev, pager, next" :pager-count="10" @current-change="handleCurrentChange" :page-count="page.totalPage" :current-page.sync="page.currentPage">
      </el-pagination>
    </div>

    <el-dialog fullscreen :title="contentHeader" :visible.sync="dialogTextVisible" @close="" :close-on-click-modal="false">
      <div class="contentBody" v-html="contentBody">
      </div>
    </el-dialog>

    <el-dialog :title="contentHeader" :visible.sync="dialogVideoVisible" @close="handleStopVideo" :close-on-click-modal="false">
      <div class="contentBody">
        <video ref="video" v-if="dialogVideoVisible" width="auto" height="auto" autoplay loop controls>
          <source :src="contentURL" type="video/mp4">
          您的浏览器不支持Video标签。
        </video>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  import { loadGridHeight } from "api/screen";
  import { parseTime } from "utils/index";
  import { getContentPage, getContentBody } from "api/portal/index";

  export default {
    name: "portal-page",
    data() {
      return {
        height: undefined,
        dialogTextVisible: false,
        dialogVideoVisible: false,
        columnId: undefined,
        contentArray : [],
        contentHeader: undefined,
        contentBody: undefined,
        contentURL: undefined,
        page: {
          currentPage: 1,
          totalPage: undefined,
          pageLimit: undefined
        }
      }
    },
    created() {

    },
    mounted() {
      this.height = loadGridHeight() - 80;
      this.columnId = this.columnParams.columnId;
      this.$refs['content'].style.height = this.height+"px";
      this.init();
    },
    computed: {
      ...mapGetters(["columnParams", "user"])
    },
    methods: {
      init() {
        this.pageContent();
      },
      pageContent() {
        getContentPage('publish/'+this.columnId+'/'+this.page.currentPage+'.json').then(data => {
          if (data) {
            this.contentArray = data.content;
            this.page.currentPage = data.page.currentPage;
            this.page.totalPage = data.page.totalPage;
            this.page.pageLimit = data.page.pageLimit;
          }
        });
      },
      parseDate(dateTime) {
        return parseTime(dateTime, '{y}-{m}-{d}');
      },
      handlerView(content) {
        this.contentHeader = content.contentTitle;
        if (content.forwardUrl.length > 0) {
          this.dialogTextVisible = true;
          getContentBody(content.forwardUrl).then(data => {
            // let regex = /[^\u4e00-\u9fa5\w]/g;
            // let formatContentBody = $(data).text().replace(regex,""); //去掉特殊字符后的内容
            this.contentBody = data;
          });
        } else {
          this.dialogVideoVisible = true;
          this.contentURL = content.mediaUrl;
        }
      },
      handleStopVideo() {
        // debugger
        this.$refs['video'].pause();
      },
      handleCurrentChange(val) {
        this.page.currentPage = val;
        this.pageContent();
      }
    }
  }
</script>
<style scoped>
  .column {
    width: 100%;
    padding-top: 10px;
    padding-right: 10px;
    padding-bottom: 0px;
  }
  .column .column_title {
    padding-left: 5px;
    color: #F0F0F0;
    background-color: rgb(32, 160, 255);
    font-size: 16px;
    height: 40px;
    line-height: 40px;
  }
  .column .column_content ul {
    padding: 0px 5px 0px 5px;
    margin-top: 0px;
    border-left: 1px #dcdfe6 solid;
    border-right: 1px #dcdfe6 solid;
    border-bottom: 1px #dcdfe6 solid;
  }
  .column .column_content ul li {
    list-style: none;
    height: 38px;
    line-height: 38px;
    cursor: pointer;
  }
  .column .column_content ul li:nth-child(-n+15) {
    border-bottom: 1px #dcdfe6 dashed;
  }
  .spanTitle {
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    font-size: 14px;
    width: 93%;
  }
  .spanTime {
    font-size: 14px;
    width: 7%;
    float: right;
    color: #696969;
  }
</style>
