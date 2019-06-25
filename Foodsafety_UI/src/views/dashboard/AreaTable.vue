<template>
    <el-row>
        <el-table :data="tableData" v-loading="uTableLoading" element-loading-text="数据加载中" border height="460">
            <el-table-column prop="areaId" label="区域编号" width="120"></el-table-column>
            <el-table-column prop="areaName" label="区域名称" width="120"></el-table-column>
            <el-table-column prop="areaDesc" label="区域描述" width="130"></el-table-column>
            <el-table-column prop="principal" label="负责人" width="120"></el-table-column>
            <el-table-column prop="phone" label="负责人电话" width="140"></el-table-column>
            <el-table-column prop="remark" label="备注" width="140"></el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <el-row>
            <el-col :offset="6">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page.sync="currPage"
                        :page-size="pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="totalCount"
                        style="margin: 10px 0"
                >
                </el-pagination>
            </el-col>
        </el-row>
    </el-row>
</template>


<script>
    import fetch from 'utils/fetch.js'
    export default {
        name: 'AreaTable',
        props: [
            'currAreaInfo'
        ],
        data(){
            return {

                // 表格和分页相关
                uTableLoading: false, //是否显示表格的正在加载层
                tableData: [], //表格数据
                currPage: 1, // 当前第几页
                pageSize: 8, // 当前每页显示几条数据
                totalCount: 200, //总数据条数

            }
        },
        mounted () {
            // console.log(this.currAreaInfo);

            this.uTableLoading = true;

            fetch({
                url: 'https://www.easy-mock.com/mock/5b0e01738f54a257757e130d/api/getList',
                method: 'get'
            }).then(data=>{
                this.tableData = data.userList;
                this.uTableLoading = false;
            }).catch(e=>{
                this.uTableLoading = false;
                // console.log(e);
            });

        },
        methods: {

            handleCurrentChange(pageNum){  // 表格分页改变
                this.uTableLoading = true;
                fetch({
                    url: 'https://www.easy-mock.com/mock/5b0e01738f54a257757e130d/api/getList',
                    method: 'get'
                }).then(data=>{
                    this.tableData = data.userList;
                    this.uTableLoading = false;
                }).catch(e=>{
                    this.uTableLoading = false;
                    // console.log(e);
                });

            }

        }

    }
</script>

<style>


</style>
