<template>
  <div>
    <div v-if='listLoading'>
      <el-form :model="scaleInfoForm" :disabled="true" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业名称" prop="enterpriseName">
              <el-input v-model="scaleInfoForm.enterpriseName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业规模" prop="enterpriseScale">
              <el-select v-model="scaleInfoForm.enterpriseScale" placeholder="企业规模" clearable>
                <el-option v-for="item in  enterpriseScaleOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="企业人数" prop="enterpriseEmployees">
              <el-input v-model="scaleInfoForm.enterpriseEmployees"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业技术人数" prop="enterpriseTechnicians">
              <el-input v-model="scaleInfoForm.enterpriseTechnicians"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="占地面积" prop="areaFloor">
              <el-input v-model="scaleInfoForm.areaFloor">
                <template slot="append">(万平方米)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="建筑面积" prop="areaBuilding">
              <el-input v-model="scaleInfoForm.areaBuilding">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="固定资产" prop="fixedAssets">
              <el-input v-model="scaleInfoForm.fixedAssets">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流动资金" prop="floatingCapital">
              <el-input v-model="scaleInfoForm.floatingCapital">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="年总产值" prop="grossAnnualOutputValue">
              <el-input v-model="scaleInfoForm.grossAnnualOutputValue">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年销售额" prop="annualSales">
              <el-input v-model="scaleInfoForm.annualSales">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="年缴税金额" prop="annualTaxPayment">
              <el-input v-model="scaleInfoForm.annualTaxPayment">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利润" prop="annualProfit">
              <el-input v-model="scaleInfoForm.annualProfit">
                <template slot="append">(万元)</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
        <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    </div>
    <div v-if='!listLoading'>
      无规模数据
    </div>
  </div>
</template>
<script>
import { getEnterpriseScale } from "api/admin/enterprise/index";
import { getResource } from "api/admin/resource/index";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";
import { loadGridHeight } from "api/screen";
export default {
  props: {
    enterpriseId: {
      default: undefined
    },
    enterpriseName: {
      default: undefined
    }
  },
  watch: {
    'enterpriseId': function() {
      this.getList();
    }
  },
  data() {
    return {
      list: null,
      height: undefined,
      listLoading: true,
      dialogImageVisible: false, //图片预览窗口是否显示
      dialogImageUrl: undefined, //预览图片的地址
      tableKey: 0,
      scaleInfoForm: {},
      needFixedRight: false,
      isDisabled: false,
      serverImageUrl: process.env.SERVERIMAGEURL,
      fileFormat: ['image/jpeg', 'image/png', 'image/bmp', 'image/gif'], // 允许上传的文件格式
    };
  },
  mounted() {
    /* //首次整个视图都渲染完毕后执行
     this.$nextTick(function () {
       let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
       this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
     });
     //监听屏幕的改变
     window.onresize = () => {
       let tableDiv=  this.$el.querySelector('.el-table__body-wrapper');
       this.needFixedRight =!($(tableDiv).attr("class").indexOf("is-scrolling-none")>=0);
     };*/
    this.height = this.loadGridHeight;

  },
  created() {
    this.getList();
  },
  computed: {
    ...mapGetters(["staticData"]),
    enterpriseScaleOptions() {
      return this.staticData['企业规模'];
    }

  },
  methods: {

    getList() {
      getEnterpriseScale(this.enterpriseId).then(response => {

        if (response.data.enterpriseId != 0) {
          this.scaleInfoForm = response.data;
          this.scaleInfoForm.enterpriseName = this.enterpriseName
          this.listLoading = true;
        } else {
          this.listLoading = false;
        }


      })

    },
    enterpriseScaleFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData['企业规模']);
    }
  }
};

</script>
