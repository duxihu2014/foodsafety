<template>
  <div>
    <el-form :model="baseInfoForm" :disabled="true" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="baseInfoForm.enterpriseName"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册日期" prop="registerDate">
            <el-date-picker v-model="baseInfoForm.registerDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="注册地址" prop="registerAddress">
            <el-input v-model="baseInfoForm.registerAddress"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生产地址" prop="productionAddress">
            <el-input v-model="baseInfoForm.productionAddress"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="经营期限" prop="operatingTerm">
            <el-date-picker v-model="baseInfoForm.operatingTerm" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" disabled clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="统一信用代码" prop="organizingInstitutionBarCode">
            <el-input v-model="baseInfoForm.organizingInstitutionBarCode"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="主体分类" prop="subjectClassification">
            <el-select v-model="baseInfoForm.subjectClassification" placeholder="主体分类" clearable>
              <el-option v-for="item in  subjectClassificationOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监管分类" prop="superviseClassification">
            <el-select v-model="baseInfoForm.superviseClassification" placeholder="监管分类" clearable>
              <el-option v-for="item in  superviseClassOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="corporateRepresentative">
            <el-input v-model="baseInfoForm.corporateRepresentative"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册资金" prop="registeredCapital">
            <el-input v-model="baseInfoForm.registeredCapital"> <template slot="append">(万元)</template></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="经济性质" prop="economicNature">
            <el-select v-model="baseInfoForm.economicNature" placeholder="经济性质" clearable>
              <el-option v-for="item in  economicNatureOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营范围" prop="operationScope">
            <el-input  type="textarea" :rows="2"  disabled v-model="baseInfoForm.operationScope"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contacts">
            <el-input v-model="baseInfoForm.contacts"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactNumber">
            <el-input v-model="baseInfoForm.contactNumber"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
       <el-row>
        <el-col :span="12">
          <el-form-item label="电子邮件" prop="email">
            <el-input v-model="baseInfoForm.email"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="传真" prop="fax">
            <el-input v-model="baseInfoForm.fax"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="手机" prop="contactMobile">
            <el-input v-model="baseInfoForm.contactMobile"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="企业状态" prop="enterpriseStatus">
            <el-select v-model="baseInfoForm.enterpriseStatus" placeholder="企业状态" clearable>
              <el-option v-for="item in  enterpriseStatusOptions" :key="item.value" :label="item.text" :value="item.value"> </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <div id="qrcode" ref="qrcode" style="float: right;"></div>
        </el-col>
      </el-row>
    </el-form>
    <el-dialog  :visible.sync="dialogImageVisible" size="tiny" :close-on-click-modal="false" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>
<script>
import { getEnterpriseDetail } from "api/admin/enterprise/index";
import { getResource } from "api/admin/resource/index";
import { mapGetters } from "vuex";
import { parseValueToText } from "utils/index";
import { loadGridHeight } from "api/screen";
import QRCode from 'qrcodejs2';
export default {
  props: {
    enterpriseId: {
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
      baseInfoForm: {},
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
    subjectClassificationOptions() {
      return this.staticData["企业主体分类"];
    },
    economicNatureOptions() {
      return this.staticData["企业经济性质"];
    },
    enterpriseStatusOptions() {
      return this.staticData['企业状态'];
    },
    superviseClassOptions() {
      return this.staticData['监管级别'];
    },

  },
  methods: {

    getList() {
      getEnterpriseDetail(this.enterpriseId).then(response => {
        this.baseInfoForm = response.data;
        this.qrcode();
      })

    },
    qrcode () {
      new QRCode('qrcode', {
        text:this.enterpriseId+"", // 二维码内容
        //render: 'canvas', // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
        colorDark : "#000",
        colorLight : "#fff",
      })
    },
    subjectClassFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData['企业主体分类']);
    },
    superviseClassFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData['监管级别']);
    },

    economicNatureFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData['企业经济性质']);
    },

    enterpriseStatusFormatter(cellValue) {
      return parseValueToText(cellValue, this.staticData['企业状态']);
    },
  }
};

</script>
