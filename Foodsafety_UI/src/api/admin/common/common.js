import { mapGetters } from "vuex";
const _import = require('./requireUtil');

export function options(path) {
  const js = _import(path);
  const page = js.page;
  const addObj = js.addObj,
    getObj = js.getObj,
    delObjs = js.delObjs,
    putObj = js.putObj;
  return {
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20
        },
        selection: [],
        dialogStatus: "",
        dialogFormVisible: false,
        textMap: {
          update: "编辑",
          create: "创建"
        }

      }
    },
    created() {
      this.getList();
    },
    computed: {
      ...mapGetters(["staticData"])
    },
    methods: {
      getList() {  //查询方法
        this.listLoading = true;
        page(this.listQuery).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.listLoading = false;
        });
        this.afterQuery();
      },
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
      afterQuery() {

      },
      resetTmp() {
        for (let attr in this.form) {
          form[atrr] = undefined;
        }
        this.$nextTick(() => {
          this.$refs["form"].resetFields();
        });
      },
      handleCreate() {//打开新增弹出框
        this.dialogStatus = "create";
        this.dialogFormVisible = true;
        this.resetTmp();
        this.handleCreateEx();
      },
      handleUpdate(row, idName) {//打开编辑弹出框
        getObj(row[idName]).then(response => {
          this.form = response.data;
          this.dialogFormVisible = true;
          this.dialogStatus = "update";
          this.handleUpdateEx(row);
        });
      },
      handleDelete(idName) {//删除记录
        if (!this.selection.length) {
          this.$message({
            showClose: true,
            message: "请先选择一条信息！"
          });
          return;
        }
        this.$confirm("此操作将永久删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            const idArr = [];
            // JSON.stringify(this.regionSelection);
            this.selection.forEach(row => {
              idArr.push(row[idName]);
            });
            delObjs(idArr.join(","))
              .then(() => {
                this.$notify({
                  title: "成功",
                  message: "删除成功",
                  type: "success",
                  duration: 2000
                });
                this.getList();
              })
              .catch(() => { });
          })
          .catch(() => { });
      },
      create(formName) {//保存
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            if (!this.preValid()) { //预检
              return;
            }
            addObj(this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "创建成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      update(formName, idName) {
        const set = this.$refs;
        set[formName].validate(valid => {
          if (valid) {
            this.dialogFormVisible = false;
            if (!this.preValid()) { //预检
              return;
            }
            putObj(this.form[idName], this.form).then(() => {
              this.dialogFormVisible = false;
              this.getList();
              this.$notify({
                title: "成功",
                message: "创建成功",
                type: "success",
                duration: 2000
              });
            });
          } else {
            return false;
          }
        });
      },
      preValid() {
        return true;
      },
      resetQuery() {
        for (let attr in this.listQuery) {
          this.listQuery[attr] = undefined;
        }
      },
      handleCreateEx() { },
      handleUpdateEx() { }
    }

  }
}
