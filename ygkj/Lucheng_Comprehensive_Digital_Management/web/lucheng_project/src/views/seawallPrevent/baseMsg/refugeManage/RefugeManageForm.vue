<!--
 * @Author: hanyu
 * @LastEditTime: 2022-05-31 16:55:40
 * @Description: 避灾点管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\refugeManage\RefugeManageForm.vue
-->
<template>
  <div class="form">
    <el-form :model="formData" status-icon :rules="rules" ref="ruleForm" :label-width="labelWidth">
      <el-form-item label="名称：" prop="pointName">
        <el-input
          v-model="formData.pointName"
          placeholder="请输入名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="地址：" prop="pointAddress">
        <el-input
          v-model="formData.pointAddress"
          placeholder="请输入地址"
        ></el-input>
      </el-form-item>
      <el-form-item label="可容纳人数：" prop="pointCapacity">
        <el-input
          type="number"
          v-model="formData.pointCapacity"
          placeholder="请输入可容纳人数"
        ></el-input>
      </el-form-item>
      <el-form-item label="现容纳人数：" prop="pointNowCapacity">
        <el-input
          type="number"
          v-model="formData.pointNowCapacity"
          placeholder="请输入现容纳人数"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系人：" prop="contacts">
        <el-input
          v-model="formData.contacts"
          placeholder="请输入联系人"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系电话：" prop="phone">
        <el-input
          type="number"
          v-model="formData.phone"
          placeholder="请输入联系电话"
        ></el-input>
      </el-form-item>
      <el-form-item label="经度：" prop="longitude">
        <el-input
          type="number"
          v-model="formData.longitude"
          placeholder="请输入经度"
        ></el-input>
      </el-form-item>
      <el-form-item label="纬度：" prop="latitude">
        <el-input
          type="number"
          v-model="formData.latitude"
          placeholder="请输入纬度"
        ></el-input>
      </el-form-item>
      <el-form-item label="地图选点：" prop="phone">
        <img src="../../../../assets/images/mapsearch.png" style="cursor:pointer" @click="mapSelectPoint()" alt="">
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
        <el-button @click="closeEditDialog()">取消</el-button>
      </div>
    </el-form>
    <el-dialog
      title="地图选点"
      :visible.sync="showMapDialog"
      :close-on-click-modal="false"
      width="800px"
      append-to-body
    >
      <map-select-point
        v-if="showMapSelect"
        :pointLng="formData.longitude"
        :pointLat="formData.latitude"
        @closeMapSelectDialog="closeMapSelectDialog"
        @selectPoint="selectPoint"
      ></map-select-point>
    </el-dialog>
  </div>
</template>

<script>
import { basMsgBzdApi } from '@/api/seawallPrevent'
import mapSelectPoint from "@/components/mapSelectPoint";
export default {
  name: "RefugeManageForm",
  props: {
    id: [String, Number],
  },
  components: {
    mapSelectPoint
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectDetail;
    },
  },
  data() {
    return {
      labelWidth: "25%",
      formData: {},
      rules: {
        pointName: [{ required: true, message: "必填项", trigger: "blur" }]
      },
      showMapDialog: false,
      showMapSelect: true,
    };
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    mapSelectPoint() {
      this.showMapDialog = true;
      this.showMapSelect = false;
      this.$nextTick(() => {
        this.showMapSelect = true;
      });
    },
    selectPoint(lng, lat) {
      this.showMapDialog = false;
      this.showMapSelect = false;
      this.formData.longitude = lng;
      this.formData.latitude = lat;
    },

    async getDetail() {
      let res = await basMsgBzdApi.list({
        id: this.id,
      });
      if (res.code == 0) {
        this.formData = res.data.list[0];
      }
    },

    submitForm(formName) {
      let that = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.save(this.formData);
        } else {
          return false;
        }
      });
    },

    async save(data) {
      let res = await basMsgBzdApi.save(data);
      if (res.code == 0) {
        this.$message({
          message: "保存成功",
          type: "success",
          duration: "1000",
          onClose: () => {
            this.closeEditDialog();
          },
        });
      } else {
        this.$message.error("保存失败！");
      }
    },
    
    closeEditDialog() {
      this.$emit("closeEditDialog");
    },

    closeMapSelectDialog() {
      this.showMapDialog = false;
      this.showMapSelect = false;
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
}
</style>