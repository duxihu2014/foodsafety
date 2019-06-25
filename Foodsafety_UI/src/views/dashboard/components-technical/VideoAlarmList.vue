<template>
    <div class="table_container">
        <div class="table_title">视频报警列表</div>
        <div class="scroll_div">
            <table frame="void" class="table">
                <template v-for="(item, index) in tableData">
                    <tr @click="clickRow(item.enterpriseId)">
                        <td>{{index+1}}</td>
                      <td><div class="ename">{{item.equName}}</div></td>
                        <td>{{item.content}}</td>
                        <td>{{eventStatusFormatter(item.status)}}</td>
                        <td >{{item.alarmTime}}</td>
                    </tr>
                </template>
            </table>
        </div>
    </div>
</template>

<script>
    import fetch from 'utils/fetch';
    import { mapGetters } from "vuex";
    export default {
        name: "VideoAlarmList",
        mounted (){
            this.getData();
        },

        data(){
            return {
                tableData: [],
            }
        },
        computed: {
            ...mapGetters(["user"]),
        },
        methods: {
            getData() {
                let isEnt = this.user['enterpriseId'];
                let url = isEnt
                    ?`/api/admin/alert/list?eventType=1&page=1&limit=20&enterpriseId=${this.user['enterpriseId']}`
                    :'/api/admin/alert/list?eventType=1&page=1&limit=20';
                fetch({
                    url,
                    method: 'get',
                }).then(data => {
                    this.tableData = data.rows;
                }).catch(err =>{
                    this.$message.error('视频报警列表数据获取失败！');
                });
            },
            eventStatusFormatter(status){
                return status==='1'? '未处理': '已处理';
            },
            clickRow(id){
                // alert(id);
              this.$emit("showComp", id); //取消
            }
        },
    }
</script>

<style scoped>
    .table_container {
        height: 100%;
        width: 90%;
        text-align: center;
        margin: 10px auto 0;
        color: #2d8ac7;
    }

    .table_title {
        font-weight: bolder;
        color:#3aaec2;
        text-align: center;
        line-height: 200%;
        background-color: transparent;
    }

    .table {
        font-size: 14px;
        color: white;
        text-align: center;
        width: 100%;
        height: 80%;
        margin-top: 0;
    }

    .scroll_div{
        height: 76.5%;
        overflow-y: scroll;
        overflow-x: hidden;
    }

    /*.scroll_div::-webkit-scrollbar-thumb {
          background-color: #a1a3a9;
          border-radius: 3px;
    }

    .scroll_div::-webkit-scrollbar {
        width: 10px;
        height: 10px;
        !*display: none;*!
    }*/

    .scroll_div::-webkit-scrollbar {
        display: none;
    }

    .table tr {
        height: 40px;
        line-height: 38px;
        border-bottom: 1px rgba(1,57,104, .3) solid;
        border-left: 1px #013968 solid;
        border-right: 1px #013968 solid;
    }

    .table tr:first-child{
        border-top: 1px #013968 solid;
    }

    table td:first-child {
        color: white;
        font-size: 20px;
        font-weight: 700;
        width: 5%;
    }
    table td:nth-child(2) {
      width: 5%;
      text-align: center;
      max-width: 60px;
    }
    table td:nth-child(3) {
        text-align: left;
        line-height: 24px;
        padding-left: 8px;
        width: 8%;
    }
    table td:nth-child(3):first-line {
        color: #0af;
    }
    table td:nth-child(4) {
        width: 5%;
        text-align: center;
    }
    table td:nth-child(5) {
        width: 10%;
    }
    .ename {
      max-width: 150px;
      white-space:nowrap;
      overflow:hidden;
      text-overflow:ellipsis;
    }

</style>
