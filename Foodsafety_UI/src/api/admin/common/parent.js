import { mapGetters } from "vuex";
const parent = {
  data() {
    return {
      list: [],
      total: null,
      selection: [],
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20
      }
    }
  },
  computed: {
    ...mapGetters(["staticData"]),
    statusOptions() {
      return this.staticData["状态"];
    }
  },
  methods: {
    handleFilter() {   //查询按钮点击事件
      this.getList();
    },
    handleSizeChange(val) {  //每页数量变化
      this.listQuery.limit = val;
      this.getList();
    },
    handleCurrentChange(val) {//当前页变化
      this.listQuery.page = val;
      this.getList();
    },
    getSelection(rows) {  //选择数量时触发
      this.selection = rows;
    },
    cancel(formName) { //弹出框取消按钮
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    afterQuery(response) {
      this.list = response.rows;
      this.total = response.total;
      this.listLoading = false;
    }

  }
};


export default parent;
