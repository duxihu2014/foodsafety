<template>
    <div  class="table-size">
      <el-table :data="tableData" border v-loading.body="listLoading" header-cell-class-name="table-header" row-class-name="table-row" :highlight-current-row="false">
        <el-table-column align="center" label="事务类型" prop="name" class-name="column-height" ></el-table-column>
        <el-table-column align="center" label="完成数" prop="finish" class-name="column-height" ></el-table-column>
        <el-table-column align="center" label="总数" prop="total" class-name="column-height"></el-table-column>
        <el-table-column align="center" label="完成率" prop="finish" class-name="column-height" :formatter="rateFormatter"></el-table-column>
      </el-table>
    </div>
</template>

<script>
  import {getSafetyGrid} from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
    export default {
        name: "index-tabel",
      watch: {
        'listLoading':function(value){
          if(!value){
            this.changeResize();
          }
        }
      },
      mounted (){
        this.getData();
        //监听屏幕的改变
        window.onresize = () => {
          this.changeResize();
        }
      },
      computed: {
        ...mapGetters(["user"]),
      },
      data(){
          return{
            listLoading: true,
            tableData: [],
            timer:null
          }
      },
      methods:{
        getData() {
          this.listLoading = true;
          getSafetyGrid({finalAreaId:this.user.areaId,registerStatus:"0"}).then(response=>{
            this.tableData=response.data;
            this.listLoading = false;
          })
        },
        changeResize(){
          let _self = this;
          _self.$nextTick(function() {
            let divHight = $(".table-div").height();
            _self.timer = window.setInterval(function (){
              try {
                if (divHight !== $(".table-div").height()) {
                  divHight = $(".table-div").height();
                } else {
                  var height = $(".table-div").height() - $(".table-title").height() - 7;
                  $(".column-height").css({"height": height / 4});
                  _self.clearTime();
                }
              }catch (e){
                _self.clearTime();
              }
            },100)
          })

        },
        clearTime(){
          let _self = this;
          window.clearInterval(_self.timer);
        },
        rateFormatter(row){
          if(row.total==0){
            return 0+'%';
          }else{
            return ((row.finish/row.total)*100).toFixed(3)+'%';
          }
        }
      },
    }
</script>

<style >
  .table-header {
    background: #021D30 !important;
    color: #018EB7;
  }
  .table-row {
    background: #102133 !important;
    color: #3B948A;
  }
  .table-size .current-row > td {
    background: #018EB7 !important;
    color: #ffffff;
  }
  .table-size .el-table__body tr:hover>td {
    background-color: rgba(34, 32, 51, 0.9) !important;
  }
  .table-size .el-table td, .el-table th{
    padding: 2px 0;
  }
</style>
