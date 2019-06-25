<template>
  <div>
    <el-tabs type="border-card" v-model="tabColumnId" @tab-click="handleClick">
      <el-tab-pane v-for="(column, index) in columnArray" :label="column.columnName" :name="column.columnId+''">
        <div style="overflow: auto" :ref="`subColumn${index}`">
          <div class="subColumn" v-for="(subColumn, index) in subColumnArray">
            <div class="subColumn_title">
              <span>
                <i class="fa fa-book"></i> {{ subColumn.columnName }}
              </span>
              <span class="spanMore" v-show="contentCount[index] > 5" @click.stop.prevent="handlerPage(column, subColumn)">
                more
              </span>
            </div>
            <div class="subColumn_content">
              <ul>
                <li v-for="content in subColumn.contentArray" @click.stop.prevent="handlerView(content, column, subColumn)">
                  <span class="spanTitle">{{ content.contentTitle }}</span>
                  <span class="spanTime">{{ parseDate(content.publishTime) }}</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog fullscreen :visible.sync="dialogTextVisible" @close="" :close-on-click-modal="false">
      <detail_text :columnId="param.columnId" :subColumnId="param.subColumnId" :contentHeader="param.contentHeader" :contentUrl="param.contentUrl" :forwardUrl="param.forwardUrl"></detail_text>
    </el-dialog>

    <el-dialog :title="contentHeader" :visible.sync="dialogVideoVisible" @close="handleStopVideo" :close-on-click-modal="false">
      <div>
        <video ref="video" v-if="dialogVideoVisible" width="auto" height="auto" autoplay loop controls>
          <source :src="contentUrl" type="video/mp4">
          您的浏览器不支持Video标签。
        </video>
      </div>
    </el-dialog>

  </div>

</template>
<script>
  import { getColumn, getContentPage } from "api/portal/index";
  import { loadGridHeight } from "api/screen";
  import { parseTime } from "utils/index";
  import router from 'router/index';
  export default {
    name: "portal",
    components: {
      "detail_text": () => import("./components/detail_text"),
    },
    data() {
      return {
        height: undefined,
        dialogTextVisible: false,
        dialogVideoVisible: false,
        contentCount: [],
        columnArray: [],
        subColumnArray: [],
        subColumnContent: {},
        tabColumnId: '4',
        contentHeader: undefined,
        contentBody: undefined,
        contentUrl: undefined,
        param: {
          columnId: undefined,
          subColumnId: undefined,
          contentUrl: undefined,
          forwardUrl: undefined
        }
      };
    },
    created() {
      this.init();
    },
    mounted() {
      this.height = loadGridHeight() - 31;
    },
    computed: {

    },
    watch: {
      // 'subColumnArray' : function (v1, v2) {
      //   console.log(v1);
      //   console.log(v2);
      // }
    },
    methods: {
      init() {
        this.getRootColumn();
      },
      getRootColumn() {
        // 获取一级栏目
        getColumn('0').then(data => {
          // 执行成功的回调函数
          this.columnArray = data;
          if (this.columnArray) {
            if (this.tabColumnId == undefined)
              this.tabColumnId = this.columnArray[0].columnId + '';
            // 获取二级栏目
            getColumn(this.tabColumnId).then(data => {
              this.$refs['subColumn0'][0].style.height = this.height + "px";
              // console.log(this.subColumnArray);
              data.forEach((subColumn, index) => {
                if (subColumn.contentUrl.length > 0) {
                  // 获取栏目内容列表
                  getContentPage(subColumn.contentUrl).then(data => {
                    if (data) {
                      this.contentCount[index] = data.content.length;
                      this.subColumnContent = subColumn;
                      this.subColumnContent.contentArray = (data.content.slice(0, 5));
                      this.subColumnArray.push(this.subColumnContent);
                    }
                  });
                }
                // console.log(this.contentCount);
              });
              // console.log(this.subColumnArray);
            });
          }
        }, error => {
          // 执行失败的回调函数
        });
      },
      parseDate(dateTime) {
        return parseTime(dateTime, '{y}-{m}-{d}');
      },
      handleClick(tab, event) {
        //console.log(tab, event);
        getColumn(tab.name).then(data => {
          this.subColumnArray.length = 0;
          this.$refs['subColumn' + tab.index][0].style.height = this.height + "px";
          // console.log(this.subColumnArray);
          data.forEach((subColumn, index) => {
            if (subColumn.contentUrl.length > 0) {
              // 获取栏目内容列表
              getContentPage(subColumn.contentUrl).then(data => {
                if (data) {
                  this.contentCount[index] = data.content.length;
                  this.subColumnContent = subColumn;
                  this.subColumnContent.contentArray = (data.content.slice(0, 5));
                  this.subColumnArray.push(this.subColumnContent);
                }
              });
            }
          });
          // console.log(this.subColumnArray);
        });
      },
      handlerView(content, column, subColumn) {
        if (content.forwardUrl.length > 0) {
          this.dialogTextVisible = true;
          this.param.columnId = column.columnId;
          this.param.subColumnId = subColumn.columnId;
          this.param.contentUrl = subColumn.contentUrl;
          this.param.forwardUrl = content.forwardUrl;
          this.param.contentHeader = content.contentTitle;
        } else {
          this.dialogVideoVisible = true;
          this.contentUrl = content.mediaUrl;
          this.contentHeader = content.contentTitle;
        }
      },
      handleStopVideo() {
        // debugger
        this.$refs['video'].pause();
      },
      handlerPage(column, subColumn) {
        this.openTabPage("/portal/page", column, subColumn);
      },
      openTabPage(path, column, subColumn) {
        path=path+'/'+subColumn.columnId;
        //console.log(this.user);
        this.$store.commit("SET_COLUMNPARAMS", subColumn);
        this.$store.dispatch("addVisitedViews", this.generateRouter(path, column, subColumn));
        // this.$router.push({ name: 'portalPage', params: {columnId: column.columnId, columnName: column.columnName} });
        this.$router.push({path: path});
      },
      generateRouter(path, column, subColumn) {
        const _import = require('router/_import_' + process.env.NODE_ENV);

        let router_p = {};
        let router_c = {};
        let children = [];

        router_p.path = '/';
        router_p.component = _import('layout/Layout');
        // router_p.name = '监管门户',
        router_p.hidden = true;

        router_c.path = path;
        router_c.component = _import('portal/page');
        router_c.name = column.columnName+'-'+subColumn.columnName;

        children.push(router_c);
        router_p.children = children;

        this.$store.commit('SET_ROUTERS', [router_p]);
        router.addRoutes([router_p]);

        return router_p;
      }
    }
  }
</script>

<style scoped>
  .subColumn {
    float: left;
    width: 49%;
    padding-right: 10px;
    padding-bottom: 0px;
    height: 246px;
  }
  .subColumn .subColumn_title {
    padding-left: 5px;
    color: #F0F0F0;
    background-color: rgb(32, 160, 255);
    font-size: 16px;
    height: 40px;
    line-height: 40px;
  }
  .subColumn .subColumn_content ul {
    height: 190px;
    padding: 0px 5px 5px 5px;
    margin-top: 0px;
    border-left: 1px #dcdfe6 solid;
    border-right: 1px #dcdfe6 solid;
    border-bottom: 1px #dcdfe6 solid;
  }
  .subColumn .subColumn_content ul li {
    list-style: none;
    height: 38px;
    line-height: 38px;
    cursor: pointer;
  }
  .subColumn .subColumn_content ul li:nth-child(-n+4) {
    border-bottom: 1px #dcdfe6 dashed;
  }
  .spanTitle {
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    font-size: 14px;
    width: 85%;
  }
  .spanTime {
    font-size: 14px;
    width: 15%;
    float: right;
    color: #696969;
  }
  .spanMore {
    float:right;
    padding-right: 5px;
    height: 20px;
    line-height: 20px;
    font-size: 12px;
    padding-top: 15px;
    cursor: pointer;
  }
</style>
