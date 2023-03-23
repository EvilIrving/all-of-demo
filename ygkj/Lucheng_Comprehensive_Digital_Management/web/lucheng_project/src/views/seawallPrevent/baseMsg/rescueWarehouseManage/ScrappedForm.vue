<template>
  <div>
    <div class="dialog_title">
      <div class="pattern"></div>
      <p>基本信息</p>
    </div>
    <div class="form">
      <el-form
          :model="formData"
          status-icon
          :rules="rules"
          ref="ruleForm"
          label-position="right"
          label-width="8.5rem"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="物资仓库名称：" prop="warehouseId">
              <el-select
                  v-model="formData.warehouseId"
                  placeholder="请选择物资仓库"
                  clearable
                  style="width: 100%"
                  :disabled="id?true:false"
              >
                <el-option
                    v-for="item in warehousesList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物资名称：" prop="materialName">
              <el-select
                  v-model="formData.materialName"
                  placeholder="请选择物资"
                  style="width: 100%"
                  clearable
                  :disabled="id?true:false"
              >
                <el-option
                    v-for="item in warehouseMaterialStandardList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数量：" prop="num">
              <el-input
                  v-model="formData.num"
                  onkeyup="value=value.replace(/[^0-9]/g,0)"
                  placeholder="请输入数量"
                  clearable
                  :readonly="id?true:false"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="登记人员：" prop="recorder">
              <el-input
                  v-model="formData.recorder"
                  placeholder="请输入登记人员"
                  clearable
                  :readonly="id?true:false"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="报废时间：" prop="tm">
              <el-date-picker
                  v-model="formData.tm"
                  type="date"
                  format="yyyy-MM-dd"
                  style="width: 100%"
                  placeholder="选择出库时间"
                  :readonly="id?true:false"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <!--    <div class="dialog_title">
          <div class="pattern"></div>
          <p>出库物资</p>
        </div>
        <div class="tb_wrapper">
          <el-table :data="outboundList" border style="width: 100%">
            <el-table-column type="index" label="序号" align="center" width="50"></el-table-column>
            <el-table-column label="物资" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value1" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value1}}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value2" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value2}}</span>
              </template>
            </el-table-column>
            <el-table-column label="单价" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value3" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value3}}</span>
              </template>
            </el-table-column>
            <el-table-column label="单位" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value4" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value4}}</span>
              </template>
            </el-table-column>
            <el-table-column label="申请数量" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value5" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value5}}</span>
              </template>
            </el-table-column>
            <el-table-column label="现有库存" align="center">
              <template slot-scope="scope">
                <el-input v-model="outboundList[scope.$index].value6" placeholder="请输入" v-if="scope.row.switchChange"></el-input>
                <span v-else>{{scope.row.value6}}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="150">
              <template slot-scope="scope">
                <div class="deal_box">
                  <span
                      v-if="!scope.row.switchChange"
                      class="deal_btn deal_btn_look"
                      @click="scope.row.switchChange=true"
                  >编辑</span>
                  <span
                      v-if="scope.row.switchChange"
                      class="deal_btn deal_btn_look"
                      @click="scope.row.switchChange=false"
                  >确认</span>
                  <span
                      class="deal_btn deal_btn_del"
                      @click="deletePlan(scope.$index,outboundList)"
                  >删除</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="button_under">
            <el-button type="text" @click="insertPlan(outboundList.length)">+添加</el-button>
          </div>
        </div>-->
    <el-divider></el-divider>
    <div class="footer_btn">
      <el-button @click="closeEditDialog()">取消</el-button>
      <el-button type="primary" @click="submitForm('ruleForm')"
      >保存
      </el-button>
    </div>
  </div>
</template>

<script>
import {intoOutScrappedApi, rescueWarehouseApi} from '@/api/seawallPrevent'
import mapSelectPoint from "@/components/mapSelectPoint";
import unploadFile from "@/components/uploadFile.vue";

export default {
  name: 'TeamForm',
  props: {
    id: [String, Number],
  },
  components: {
    mapSelectPoint,
    unploadFile,
  },
  data() {
    return {
      typeOptions: [
        {label: '省级仓库', value: '省级仓库'},
        {label: '市级仓库', value: '市级仓库'},
        {label: '县级仓库', value: '县级仓库'},
        {label: '省供销社', value: '省供销社'},
        {label: '社会厂家', value: '社会厂家'},
      ],
      formData: {
        warehouseId: "",
        type: "2",
        materialName: "",
        univalent: 0,
        num: 0,
        recorder: "",
        tm: "",
      },
      rules: {
        warehouseId: [{required: true, message: '必填项', trigger: 'change'}],
        type: [{required: true, message: '必填项', trigger: 'change'}],
        materialName: [{required: true, message: '必填项', trigger: 'change'}],
        univalent: [{required: true, message: '必填项', trigger: 'change'}],
        num: [{required: true, message: '必填项', trigger: 'change'}],
        recorder: [{required: true, message: '必填项', trigger: 'change'}],
        tm: [{required: true, message: '必填项', trigger: 'change'}],
      },
      fileList: [],
      outboundList: [],
      warehousesList: [],
      warehouseMaterialStandardList: [],
    }
  },
  mounted() {
    this.getWarehouseMaterialStandardList()
    this.getWarehousesList()
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getWarehouseMaterialStandardList() {
      await intoOutScrappedApi.warehouseMaterialStandardList().then(res => {
        this.warehouseMaterialStandardList = res.data
      })
    },
    async getWarehousesList() {
      await intoOutScrappedApi.warehousesList().then(res => {
        this.warehousesList = res.data.list
        // console.log(this.warehousesList)
      })
    },
    insertPlan(index) {
      this.outboundList.splice(index + 1, 0,
          {
            id: null,
            item1: "",
            item2: "",
            item3: "",
            item4: "",
            item5: "",
            item6: "",
            switchChange: true,
          }
      )
    },
    deletePlan(index, rows) {
      rows.splice(index, 1);
    },
    handleSuccess(res) {
      if (res.file.code == 0) {
        let file = res.file.data;
        this.form.fileurl = file.filePath;
        this.form.filename = file.originalName;
      }
    },
    handleRemove() {
      this.form.fileurl = "";
      this.form.filename = "";
    },
    typeChange(val) {
      this.formData.typeName = this.typeOptions.find((item) => item.value == val).label
    },
    async getDetail() {
      let res = await intoOutScrappedApi.outList({
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
      this.warehouseMaterialStandardList.map(value => {
        if (data.materialName==value.name)
          data.univalent=value.univalent
      })
      let res =await intoOutScrappedApi.outScrappedAdd(data)
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
.footer_btn {
  float: right;
}

.form {
  margin-top: 10px;
}

.dialog_title {
  @include flexbox;

  .pattern {
    width: 4px;
    height: 21px;
    background: #1492FF;
    border-radius: 2px;
  }

  p {
    margin: 3px 14px;
    font-size: 16px;
    font-weight: 400;
    color: #666666;
  }
}

.button_under {
  text-align: center;
  border: 1px solid #DDDDDD;
}
</style>
