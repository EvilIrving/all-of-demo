<template>
  <el-dialog
    custom-class="data-dialog"
    :title="title"
    width="880px"
    top="4vh"
    append-to-body
    :visible="visible"
    :close-on-click-modal="false"
    @update:visible="onClose"
  >
    <div class="wrap scroll-content">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        label-position="right"
        size="small"
      >
        <p class="form-group-title">基础信息</p>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目录名称" prop="directoryName">
              <el-input
                placeholder="请输入"
                disabled
                :value="form.directoryName"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据项" prop="dataItem">
              <el-input placeholder="请输入" v-model="form.dataItem" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详细描述" prop="description">
              <el-input placeholder="请输入" v-model="form.description" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前数据来源" prop="source">
              <el-input placeholder="请输入" v-model="form.source" />
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="transManageDataType == 1">
          <p class="form-group-title">行政负责人-主管部门（产权人）责任人</p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.competentPeople.unitName"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.competentPeople.unitDirector"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="unitDirector">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.competentPeople.unitDirectorPhone"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务" prop="unitDirectorPhone">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.competentPeople.unitJob"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="form-group-title">物业单位管理负责人</p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.propertyPeople.unitName"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.propertyPeople.unitDirector"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="unitDirector">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.propertyPeople.unitDirectorPhone"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务" prop="unitDirectorPhone">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.propertyPeople.unitJob"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="form-group-title">技术责任人</p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.technologyPeople.unitName"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.technologyPeople.unitDirector"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="unitDirector">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.technologyPeople.unitDirectorPhone"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务" prop="unitDirectorPhone">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.technologyPeople.unitJob"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <p class="form-group-title">巡查责任人</p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.patrolPeople.unitName"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="unitName">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.patrolPeople.unitDirector"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="unitDirector">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.patrolPeople.unitDirectorPhone"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务" prop="unitDirectorPhone">
                <el-input
                  placeholder="请输入"
                  disabled
                  v-model="detail.patrolPeople.unitJob"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-if="transManageDataType != 1">
          <p class="form-group-title">责任单位</p>
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称" prop="unitName">
                <el-input placeholder="请输入" v-model="form.unitName" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="负责人" prop="unitDirector">
                <el-input placeholder="请输入" v-model="form.unitDirector" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="unitDirectorPhone">
                <el-input
                  placeholder="请输入"
                  v-model="form.unitDirectorPhone"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数据维护负责人" prop="dataMaintainName">
                <el-input
                  placeholder="请输入"
                  v-model="form.dataMaintainName"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式" prop="dataMaintainPhone">
                <el-input
                  placeholder="请输入"
                  v-model="form.dataMaintainPhone"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <p class="form-group-title">数据维护</p>
        <div v-for="(item, index) in unitInfos" :key="index">
          <el-row>
            <el-col :span="12">
              <el-form-item label="单位名称">
                <el-input
                  v-model="unitInfos[index].unitName"
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="职务">
                <el-input
                  v-model="unitInfos[index].unitJob"
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="负责人">
                <el-input
                  v-model="unitInfos[index].unitDirector"
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式">
                <el-input
                  v-model="unitInfos[index].unitDirectorPhone"
                  placeholder="请输入"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row type="flex" justify="between">
            <el-col>
              <el-form-item>
                <el-button
                  v-if="index === unitInfos.length - 1"
                  type="primary"
                  plain
                  @click="
                    unitInfos.push({
                      unitDirector: '',
                      unitDirectorPhone: '',
                      unitName: '',
                    })
                  "
                  >添加第三方单位</el-button
                >
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-button
                  v-if="unitInfos.length !== 1"
                  type="danger"
                  plain
                  @click="unitInfos.splice(index, 1)"
                  >删除</el-button
                >
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </div>
    <el-row slot="footer" type="flex" justify="end">
      <el-button size="small" @click="onClose">取消</el-button>
      <el-button
        size="small"
        type="primary"
        :loading="submitLoading"
        @click="onSubmit"
        >确定</el-button
      >
    </el-row>
  </el-dialog>
</template>
<script>
import { insertInfo, updateInfo } from "@/api/dataMaintenance";
export default {
  name: "DataDialog",
  props: ["visible", "title"],
  emits: ["update:visible", "change"],
  data() {
    const rules = {
      dataItem: [
        { required: true, message: "请输入数据项名称", trigger: "blur" },
      ],
    };
    return {
      id: "",
      detail: {
        patrolPeople: {},
        technologyPeople: {},
        propertyPeople: {},
        competentPeople: {},
      },
      form: {
        dataItem: "",
        description: "",
        directoryId: "",
        directoryName: "",
        source: "",
        unitName: "",
        unitDirector: "",
        unitDirectorPhone: "",
        dataMaintainName: "",
        dataMaintainPhone: "",
      },
      transManageDataType: 0,
      rules: Object.freeze(rules),
      unitInfos: [
        {
          unitDirector: "",
          unitDirectorPhone: "",
          unitName: "",
          unitJob: "",
        },
      ],
      submitLoading: false,
    };
  },
  methods: {
    onClose() {
      this.$emit("update:visible", false);
      this.resetForm();
    },
    onSubmit() {
      this.submitLoading = true;
      this.$refs.form.validate((valid) => {
        if (valid) {
          const data = Object.assign({}, this.form, {
            thirdPartyUnit: JSON.stringify(this.unitInfos),
          });
          if (this.id) {
            updateInfo({ id: this.id, ...data }).then((res) => {
              if (res.code === 0) {
                this.$message.success("操作成功!");
                this.submitLoading = false;
                this.$emit("change");
                this.onClose();
              }
            });
          } else {
            insertInfo(data).then((res) => {
              if (res.code === 0) {
                this.$message.success("操作成功!");
                this.submitLoading = false;
                this.onClose();
              }
            });
          }
        } else {
          return;
        }
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
      this.unitInfos = [
        {
          unitDirector: "",
          unitDirectorPhone: "",
          unitName: "",
          unitJob: "",
        },
      ];
    },
  },
};
</script>
<style lang="scss" scoped>
.form-group-title {
  padding: 1px 1px 1px 12px;
  margin-bottom: 12px;
  border-left: 3px solid #1492ff;
}
.scroll-content {
  height: 70vh;
  padding-right: 6px;
  overflow-y: auto;
}
</style>
