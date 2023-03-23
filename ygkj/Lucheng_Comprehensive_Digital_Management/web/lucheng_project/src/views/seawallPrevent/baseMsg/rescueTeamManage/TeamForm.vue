<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-07 09:37:55
 * @Description: 抢险队伍管理-表单
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueTeamManage\TeamForm.vue
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
      <el-form-item label="名称：" prop="name">
        <el-input
          v-model="formData.name"
          placeholder="请输入名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="队伍类型：" prop="typeId">
        <el-select
          v-model="formData.typeId"
          clearable
          placeholder="请选择队伍类型"
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
      <el-form-item label="抢险专业：" prop="rescueSpecialty">
        <el-select
          v-model="formData.rescueSpecialty"
          placeholder="请选择抢险专业"
          clearable
        >
          <el-option
            v-for="item in rescueSpecialtyOptions"
            :key="item.label"
            :value="item.label"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单位性质：" prop="unitNature">
        <el-select
          v-model="formData.unitNature"
          placeholder="请选择单位性质"
          clearable
        >
          <el-option
            v-for="item in unitNatureOptions"
            :key="item.label"
            :value="item.label"
            :label="item.label"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="编制人数：" prop="organizationNumber">
        <el-input
          v-model="formData.organizationNumber"
          type="number"
          placeholder="请输入编制人数"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="现有人数：" prop="existingNumber">
        <el-input
          v-model="formData.existingNumber"
          type="number"
          placeholder="请输入现有人数"
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
      <el-form-item label="负责人：" prop="chargePerson">
        <el-input
          v-model="formData.chargePerson"
          placeholder="请输入负责人"
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
import { rescueTeamApi } from '@/api/seawallPrevent'
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
      rescueSpecialtyOptions: [
        { label: '防汛救灾', value: 1 },
        { label: '水利抢险', value: 1 },
        { label: '工程救援', value: 1 },
        { label: '水利专业', value: 1 },
        { label: '水上救援', value: 1 },
        { label: '水利工程抢险', value: 1 },
        { label: '水上抢险', value: 1 },
        { label: '其他应急抢险', value: 1 },
        { label: '堤防抢险', value: 1 }
      ],
      unitNatureOptions: [
        { label: '水利局机关单位抢险组织', value: 1 },
        { label: '社会救援机构', value: 1 },
        { label: '自收自支事业单位', value: 1 },
        { label: '差额事业单位', value: 1 },
        { label: '全额事业单位', value: 1 }
      ],
      typeOptions: [
        { label: '学校', value: '75658751076098048' },
        { label: '成员单位', value: '75658766452416512' },
        { label: '工管单位', value: '75658800942178304' },
        { label: '村居', value: '75658910992326656' },
        { label: '本级', value: '75658937433219072' },
        { label: '指挥部', value: '75658937433219073' },
        { label: '企业', value: '75658937433219074' },
        { label: '社区', value: '75658937433219075' },
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
      let res = await rescueTeamApi.list({
        id: this.id,
      })
      if (res.code == 0) {
        this.formData = res.data.list[0]
      }
    },

    submitForm(formName) {
      let that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.save(that.formData)
        } else {
          return false
        }
      })
    },

    async save(data) {
      let res = {}
      if(this.id){
        res = await rescueTeamApi.update(data)
      }else{
        res = await rescueTeamApi.insert(data)
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