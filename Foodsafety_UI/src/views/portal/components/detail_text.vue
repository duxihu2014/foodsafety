<template>
  <div style="margin-top: 15px">
    <el-row>
      <el-col :span="16" class="contentBody">
        <div>
          <div class="contentTitle" v-html="contentHeader"></div>
          <div v-html="contentBody"></div>
        </div>
      </el-col>
      <el-col :span="8" style="padding-left: 20px">
        <div class="subColumn">
          <div class="subColumn_title">
              <span>
                最近更新
              </span>
          </div>
          <div class="subColumn_content">
            <ul>
              <li v-for="content in contentArray" @click.stop.prevent="handlerView(content)">
                <span class="spanTitle">{{ content.contentTitle }}</span>
                <span class="spanTime">{{ parseDate(content.publishTime) }}</span>
              </li>
            </ul>
          </div>

          <el-carousel trigger="click" arrow="never" height="240px">
            <el-carousel-item v-for="item in hotContentArray" :key="item.contentId">
              <div style="cursor: pointer" @click="handlerView(item)">
                <img width="100%" :src="httpUrl+'/'+item.hotImg" alt="">
                <span class="hotContentTitle">{{ item.contentTitle }}</span>
              </div>
            </el-carousel-item>
          </el-carousel>

        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getContentPage, getContentBody, getHotContent, getImage } from "api/portal/index";
  import { parseTime } from "utils/index";
  export default {
    name: "text",
    props: {
      columnId: {
        type: Number,
        default: undefined,
        required: true
      },
      subColumnId: {
        type: Number,
        default: undefined,
        required: true
      },
      contentUrl: {
        type: String,
        default: undefined,
        required: true
      },
      forwardUrl: {
        type: String,
        default: undefined,
        required: true
      },
      contentHeader: {
        type: String,
        default: undefined,
        required: true
      }
    },
    data(){
      return{
        httpUrl: undefined,
        contentBody: undefined,
        contentArray: [],
        hotContentArray: []
      }
    },
    created() {
      this.httpUrl = process.env.CMSIMAGEURL;
      this.init();
    },
    mounted () {

    },
    computed: {

    },
    methods: {
      init() {
        this.getContentDetail();
        this.getColumnContent();
        this.getHotContent();
      },
      parseDate(dateTime) {
        return parseTime(dateTime, '{y}-{m}-{d}');
      },
      getContentDetail() {
        getContentBody(this.forwardUrl).then(data => {
          // let regex = /[^\u4e00-\u9fa5\w]/g;
          // let formatContentBody = $(data).text().replace(regex,""); //去掉特殊字符后的内容
          this.contentBody = data;
        });
      },
      getColumnContent() {
        getContentPage(this.contentUrl).then(data => {
          if (data) {
            this.contentArray = data.content.slice(0, 8);
          }
        });
      },
      getHotContent() {
        getHotContent(this.columnId).then(data => {
          if (data) {
            this.hotContentArray = data.slice(0, 5);;
          }
        });
      },
      handlerView(content) {
        this.contentHeader = content.contentTitle;
        if (content.forwardUrl.length > 0) {
          getContentBody(content.forwardUrl).then(data => {
            // let regex = /[^\u4e00-\u9fa5\w]/g;
            // let formatContentBody = $(data).text().replace(regex,""); //去掉特殊字符后的内容
            this.contentBody = data;
          });
        }
      }
    }
  }
</script>
<style scoped>
  .contentTitle {
    text-align: center;
    font-size: 24px;
    font-weight: bold;
    padding-bottom: 10px;
  }
  .contentBody {
    padding-bottom: 10px;
    line-height: 30px;
  }
  .subColumn {
    float: left;
    width: 100%;
    padding-right: 10px;
    padding-bottom: 10px;
    height: 304px;
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
    height: 304px;
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
  .subColumn .subColumn_content ul li:nth-child(-n+7) {
    border-bottom: 1px #dcdfe6 dashed;
  }
  .spanTitle {
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
    font-size: 14px;
    width: 82%;
  }
  .spanTime {
    font-size: 14px;
    width: 18%;
    float: right;
    color: #696969;
  }
  .hotContentTitle {
    display: inline-block;
    position: absolute;
    width: 95%;
    height: 2rem;
    bottom: 0;
    vertical-align: middle;
    text-align: center;
    color: #fff;
    background: url("../../../assets/over.png");
    font-size: 1.1em;
    padding: .8rem;
    letter-spacing: .1rem;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
