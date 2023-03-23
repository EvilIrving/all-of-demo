<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-07 09:38:15
 * @Description: 抢险队伍管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueWarehouseManage\WarehouseForm.vue
-->
<template>
  <div class="form">
    <el-form
      :model="formData"
      status-icon
      :rules="rules"
      ref="ruleForm"
      :label-width="labelWidth"
    >
      <el-form-item label="仓库名称：" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入仓库名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="仓库类型：" prop="type">
        <el-select
          v-model="formData.type"
          clearable
          placeholder="请选择仓库类型"
          @change="typeChange"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :value="item.value"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="仓库面积：" prop="acreage">
        <el-input
          v-model="formData.acreage"
          placeholder="请输入仓库面积"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="物资总价值：" prop="materialPrice">
        <el-input
          v-model="formData.materialPrice"
          placeholder="请输入物资总价值"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="详细地址：" prop="address">
        <el-input
          v-model="formData.address"
          placeholder="请输入详细地址"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="联系人：" prop="chargePerson">
        <el-input
          v-model="formData.chargePerson"
          placeholder="请输入联系人"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="联系方式：" prop="chargePhone">
        <el-input
          type="number"
          v-model="formData.chargePhone"
          placeholder="请输入联系方式"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="经度：" prop="longitude">
        <el-input
          type="number"
          v-model="formData.longitude"
          placeholder="请输入经度"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="纬度：" prop="latitude">
        <el-input
          type="number"
          v-model="formData.latitude"
          placeholder="请输入纬度"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="地图选点：" prop="phone">
        <img src="../../../../assets/images/mapsearch.png" style="cursor:pointer" @click="clickMapSelectPoint()" alt="">
      </el-form-item>
      <div class="footer_btn">
        <el-button type="primary" @click="submitForm('ruleForm')"
          >保存</el-button
        >
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
import { rescueWarehouseApi } from '@/api/seawallPrevent'
import mapSelectPoint from "@/components/mapSelectPoint";
export default {
  name: 'TeamForm',
  props: {
    id: [String, Number],
  },
  components: {
    mapSelectPoint
  },
  data() {
    return {
      typeOptions: [
        { label: '省级仓库', value: '省级仓库' },
        { label: '市级仓库', value: '市级仓库' },
        { label: '县级仓库', value: '县级仓库' },
        { label: '省供销社', value: '省供销社' },
        { label: '社会厂家', value: '社会厂家' },
      ],
      labelWidth: '25%',
      formData: {},
      rules: {
        name: [{ required: true, message: '必填项', trigger: 'blur' }],
        latitude: [{ required: true, message: '必填项', trigger: 'change' }],
        longitude: [{ required: true, message: '必填项', trigger: 'change' }],
      },
      showMapDialog: false,
      showMapSelect: true,
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    clickMapSelectPoint() {
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
    closeMapSelectDialog() {
      this.showMapDialog = false;
      this.showMapSelect = false;
    },

    typeChange(val) {
      this.formData.typeName = this.typeOptions.find((item) => item.value == val).label
    },
    async getDetail() {
      let res = await rescueWarehouseApi.list({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data.list[0]
      }
    },

    submitForm(formName) {
      let that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.save(this.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      let res = {}
      if(this.id){
        res = await rescueWarehouseApi.update(data)
      }else{
        res = await rescueWarehouseApi.insert(data)
      }
      if (res.code == 0) {
        this.$message({
          message: '保存成功',
          type: 'success',
          duration: '1000',
          onClose: () => {
            this.closeEditDialog()
          },
        })
      } else {
        this.$message.error('保存失败！')
      }
    },

    closeEditDialog() {
      this.$emit('closeEditDialog')
    },
  },
}
</script>

<style lang='scss' scoped>
.wrap {
}
</style>