<template>
  <el-dialog
    title="任务发送"
    width="880px"
    top="10vh"
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目录名称:">
              <el-input disabled :value="form.directoryName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据项:">
              <el-input disabled :value="form.dataItem" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详细描述:">
              <el-input disabled :value="form.description" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前数据来源:">
              <el-input disabled :value="form.source" />
            </el-form-item>
          </el-col>
        </el-row>
        <p class="form-group-title">任务发送</p>
        <el-form-item label="任务内容" prop="content">
          <el-input type="textarea" :rows="4" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="上传图片" prop="pictures">
          <el-upload
            :action="$config.uploadUrl"
            class="custom-upload"
            list-type="picture-card"
            :http-request="onUpload"
            accept="image/*"
          >
            <div class="upload-inner">
              <i class="el-icon-plus"></i>
              <span>上传图片</span>
            </div>
            <div slot="tip" class="el-upload__tip">只支持图片格式</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="发送人员" v-if="data.transManageDataType == 1">
          <el-checkbox-group v-model="checked">
            <el-checkbox v-if="detail.competentPeople" label="competent">
              行政负责人-主管部门（产权人）责任人:{{
                detail.competentPeople.unitDirector
              }}({{ detail.competentPeople.unitJob }})
            </el-checkbox>
            <el-checkbox v-if="detail.propertyPeople" label="property">
              物业单位管理负责人:{{ detail.propertyPeople.unitDirector }}({{
                detail.propertyPeople.unitJob
              }})
            </el-checkbox>
            <el-checkbox v-if="detail.technologyPeople" label="technology">
              技术责任人:{{ detail.technologyPeople.unitDirector }}({{
                detail.technologyPeople.unitJob
              }})
            </el-checkbox>
            <el-checkbox v-if="detail.patrolPeople" label="patrol">
              巡查责任人:{{ detail.patrolPeople.unitDirector }}({{
                detail.patrolPeople.unitJob
              }})
            </el-checkbox>
          </el-checkbox-group>
          <div>
            <el-checkbox-group v-model="thirdPartyUnitChecked">
              <el-checkbox
                :label="index"
                v-for="(item, index) in data.thirdPartyUnitInfos"
                :key="index"
              >
                数据维护负责人:{{ item.unitDirector }}({{ item.unitDirector }})
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
        <el-form-item label="发送人员" v-if="data.transManageDataType != 1">
          <p>责任单位:{{ data.unitName }}</p>
          <el-checkbox-group v-model="checked">
            <el-checkbox v-if="data.unitDirector" label="unitDirector">
              主管部门(产权人)责任人:{{ data.unitDirector }}
            </el-checkbox>
            <el-checkbox v-if="data.dataMaintainName" label="dataMaintain">
              数据维护负责人:{{ data.dataMaintainName }}
            </el-checkbox>
          </el-checkbox-group>
          <div v-for="(item, index) in data.thirdPartyUnitInfos" :key="index">
            <p>责任单位:{{ data.unitName }}</p>
            <el-checkbox-group v-model="thirdPartyUnitChecked">
              <el-checkbox :label="index"
                >技术负责人:{{ item.unitDirector }}</el-checkbox
              >
            </el-checkbox-group>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <el-row slot="footer" type="flex" justify="end">
      <el-button size="small" @click="onClose">取消</el-button>
      <el-button
        size="small"
        type="primary"
        :loading="submitLoading"
        @click="onSubmit"
        >发送</el-button
      >
    </el-row>
  </el-dialog>
</template>
<script>
import axios from "axios";
import { insertSendInfo } from "@/api/dataMaintenance";
export default {
  props: ["visible"],
  emits: ["update:visible"],
  data() {
    return {
      data: { thirdPartyUnitInfos: [] },
      form: {
        directoryName: "",
        dataItem: "",
        description: "",
        source: "",
        content: "",
        pictures: [],
      },
      detail: {
        patrolPeople: {},
        technologyPeople: {},
        propertyPeople: {},
        competentPeople: {},
      },
      rules: Object.freeze({
        content: [{ required: true, message: "任务内容不能为空!" }],
      }),
      submitLoading: false,
      /**
       * @type {['unitDirector'|'dataMaintain','unitDirector'|'dataMaintain']}
       * 用于判断选中的单位负责人和数据负责人
       */
      checked: ["unitDirector", "dataMaintain"],
      /** 选中的下标 */
      thirdPartyUnitChecked: [],
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
          const { pictures, ...restForm } = this.form;
          const { id, thirdPartyUnitInfos, ...rest } = this.data;
          const thirdPartyUnit = this.thirdPartyUnitChecked.map(
            (item) => this.data.thirdPartyUnitInfos[item]
          );
          let payload = {};
          if (this.data.transManageDataType == 1) {
            this.checked.forEach((element) => {
              if (this.detail[`${element}People`]) {
                let arrData = {
                  projectType: "project",
                  ...this.detail[`${element}People`],
                };
                thirdPartyUnit.push(arrData);
              }
            });
            payload = {
              infoId: id,
              pictures: pictures.join(","),
              thirdPartyUnit: JSON.stringify(thirdPartyUnit),
              transManageDataType: this.data.transManageDataType,
              ...restForm,
            };
          } else {
            payload = Object.assign(
              {
                infoId: id,
                pictures: pictures.join(","),
                thirdPartyUnit: JSON.stringify(thirdPartyUnit),
                transManageDataType: this.data.transManageDataType,
                ...rest,
                ...restForm,
              },
              this.checked.reduce(
                (map, item) => {
                  return {  
                    ...map,
                    [`${item}Phone`]: this.data[`${item}Phone`],
                  };
                },
                /** @type {[key:string]:string} */ {}
              )
            );
          }

          insertSendInfo(payload).then((res) => {
            if (res.code === 0) {
              this.$message.success("操作成功!");
              this.onClose();
            }
          });
        } else {
          return;
        }
      });
    },
    resetForm() {
      this.$refs.form.resetFields();
      this.checked = ["unitDirector", "maintainer"];
      this.thirdPartyUnitChecked = [];
      this.form.pictures = [];
    },
    async onUpload({ action, file, onSuccess, onError }) {
      const formData = new FormData();
      formData.append("uploadFile", file);
      try {
        const { data: res } = await axios.post(action, formData, {
          headers: {
            Authorization: this.$sessionData("get", "userInfo").accessToken,
          },
        });
        if (res.code === 0) {
          this.form.pictures = [...this.form.pictures, res.data.filePath];
          onSuccess();
        }
      } catch (error) {
        onError();
      }
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
  height: 600px;
}
.custom-upload {
  ::v-deep {
    .el-upload--picture-card {
      width: 100px;
      height: 100px;
      line-height: 100px;
    }
    .el-upload-list--picture-card .el-upload-list__item {
      width: 100px;
      height: 100px;
      line-height: 100px;
    }
    .el-upload-list--picture-card .el-progress {
      transform: translate(-50%, -50%) scale(0.7);
    }
  }
  .upload-inner {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    > * {
      line-height: 26px;
    }
  }
}
</style>
