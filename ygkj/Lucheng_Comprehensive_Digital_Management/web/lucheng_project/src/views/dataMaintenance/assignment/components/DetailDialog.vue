<template>
  <el-dialog
    append-to-body
    title="任务详情"
    top="8vh"
    :close-on-click-modal="false"
    :visible="visible"
    @update:visible="onClose"
  >
    <div class="wrap scroll-content">
      <el-form
        ref="form"
        :model="form"
        label-width="120px"
        label-position="right"
        size="small"
      >
        <p class="form-group-title">基础信息</p>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目录名称" prop="dataMaintainFeedbackInfo">
              <el-input disabled :value="form.directoryName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据项" prop="unitFeedbackInfo">
              <el-input disabled :value="form.dataItem" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="详细描述" prop="thirdPartyUnitInfos">
              <el-input disabled :value="form.description" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="当前数据来源">
              <el-input disabled :value="form.source" />
            </el-form-item>
          </el-col>
        </el-row>
        <p class="form-group-title">任务信息</p>
        <el-form-item label="任务内容">
          <el-input :rows="5" type="textarea" disabled :value="form.content" />
        </el-form-item>
        <el-form-item label="图片">
          <ul v-if="pictureUrls.length > 0" class="images-list">
            <li v-for="url in pictureUrls" :key="url" class="images-list__item">
              <el-image
                fit="cover"
                style="width: 96px; height: 96px"
                :src="url"
                :preview-src-list="pictureUrls"
              />
              <div class="image-list__item-cover">
                <div>查看</div>
              </div>
            </li>
          </ul>
          <div v-else>
            <span>暂无图片</span>
          </div>
        </el-form-item>
        <p class="form-group-title">我的处理信息</p>
        <div v-for="(item, index) in myDuties" :key="index">
          <el-row class="responsibilities-title">
            <el-col :span="8"> 责任单位: {{ item.dept }} </el-col>
            <el-col :span="8"> 负责人: {{ item.name }} </el-col>
            <el-col :span="8">
              处理进度: {{ item.status === "0" ? "未处理" : "已处理" }}</el-col
            >
          </el-row>
          <el-form-item label="反馈信息" required>
            <el-input
              type="textarea"
              :value="item.feedback"
              @input="onFeedbackInput"
        
            />
          </el-form-item>
        </div>
        <p class="form-group-title">其他处理信息</p>
        <div v-for="(item, index) in otherDuties" :key="`${index}_index`">
          <el-row class="responsibilities-title">
            <el-col :span="8"> 责任单位: {{ item.dept }} </el-col>
            <el-col :span="8"> 负责人: {{ item.name }} </el-col>
            <el-col :span="8">
              处理进度: {{ item.status === "0" ? "未处理" : "已处理" }}</el-col
            >
          </el-row>
          <el-form-item label="反馈信息">
            <el-input type="textarea" disabled :value="item.feedback" />
          </el-form-item>
        </div>
      </el-form>
    </div>
    <el-row slot="footer" type="flex" justify="end">
      <el-button size="small" @click="onClose">取消</el-button>
      <el-button size="small" type="primary" @click="onSubmit">确认</el-button>
    </el-row>
  </el-dialog>
</template>
<script>
import { updateSendInfo } from "@/api/dataMaintenance";

export default {
  props: ["visible"],
  emits: ["update:visible", "change"],
  data() {
    return {
      form: {
        content: "",
        dataItem: "",
        dataMaintainFeedbackInfo: "",
        dataMaintainName: "",
        dataMaintainPhone: "",
        dataMaintainStatus: "",
        description: "",
        directoryId: "",
        directoryName: "",
        endTime: "",
        handleUserId: "",
        id: "",
        infoId: "",
        pageNum: "",
        pageSize: "",
        paged: "",
        pictures: "",
        sendTime: "",
        source: "",
        startTime: "",
        status: "",
        thirdPartyUnit: "",
        thirdPartyUnitInfos: [],
        unitDirector: "",
        unitDirectorPhone: "",
        unitFeedbackInfo: "",
        unitHandleStatus: "",
        unitName: "",
      },
    };
  },
  computed: {
    responsibilities() {
      const {
        dataMaintainFeedbackInfo,
        dataMaintainName,
        dataMaintainPhone,
        dataMaintainStatus,
        thirdPartyUnitInfos,
        unitDirector,
        unitDirectorPhone,
        unitHandleStatus,
        unitFeedbackInfo,
        unitName,
      } = this.form;
      /** @type {{type:string;name:string;contact:string;dept:string;status:'1'|'0',feedback:string}[]} */
      const thirdInfos = (thirdPartyUnitInfos ?? []).map((item) => {
        return {
          type: "第三方责任人",
          name: item.unitDirector,
          contact: item.unitDirectorPhone,
          dept: item.unitName,
          status: item.status,
          feedback: item.feedbackInfo,
        };
      });
      if (this.form.transManageDataType == 1) {
        return thirdInfos;
      } else {
        return thirdInfos.concat([
          {
            type: "责任人",
            name: unitDirector,
            contact: unitDirectorPhone,
            dept: unitName,
            status: unitHandleStatus,
            feedback: unitFeedbackInfo,
          },
          {
            type: "数据维护负责人",
            name: dataMaintainName,
            contact: dataMaintainPhone,
            dept: unitName,
            status: dataMaintainStatus,
            feedback: dataMaintainFeedbackInfo,
          },
        ]);
      }
    },
    myDuties() {
      const currentUser = this.$sessionData("get", "userInfo");
      const { phone } = currentUser;
      return this.responsibilities.filter((item) => item.contact === phone);
    },
    otherDuties() {
      const currentUser = this.$sessionData("get", "userInfo");
      const { phone } = currentUser;
      return this.responsibilities.filter((item) => item.contact !== phone);
    },
    /**
     * 当前用户需要修改的属性
     * @returns {'dataMaintainFeedbackInfo'|'unitFeedbackInfo'|'feedbackInfo'}
     */
    feedbackProperty() {
      const propertiesMap = {
        数据维护负责人: "dataMaintainFeedbackInfo",
        责任人: "unitFeedbackInfo",
        第三方责任人: "feedbackInfo",
      };
      return propertiesMap[this.myDuties?.[0]?.type] ?? null;
    },
    pictureUrls() {
      return this.form.pictures === "" ? [] : this.form.pictures.split(",");
    },
  },
  methods: {
    onFeedbackInput(input) {
      const currentUser = this.$sessionData("get", "userInfo");
      const { phone } = currentUser;
      if (this.form.transManageDataType == 1) {
        this.form.thirdPartyUnitInfos.forEach((item) => {
          if (item.unitDirectorPhone === phone) {
            item.feedbackInfo = input;
          }
        });
      } else {
        if (this.form.dataMaintainPhone === phone) {
          this.form.dataMaintainFeedbackInfo = input;
        } else if (this.form.unitDirectorPhone === phone) {
          this.form.unitFeedbackInfo = input;
        } else {
          this.form.thirdPartyUnitInfos.forEach((item) => {
            if (item.unitDirectorPhone === phone) {
              item.feedbackInfo = input;
            }
          });
        }
      }
    },
    onClose() {
      this.$refs.form.resetFields();
      this.$emit("update:visible", false);
    },
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.feedbackProperty === "feedbackInfo") {
            // 第三方
            this.form.thirdPartyUnitInfos.map((item) => {
              if (item.unitDirectorPhone === this.myDuties[0].contact) {
                return { ...item, feedbackInfo: this.feedback };
              }
              return item;
            });
          }
          const { id, thirdPartyUnitInfos } = this.form;

          const payload = {
            id,
            transManageDataType: this.form.transManageDataType,
            [this.feedbackProperty]:
              this.feedbackProperty === "feedbackInfo"
                ? JSON.stringify(thirdPartyUnitInfos)
                : this.form[this.feedbackProperty],
          };
          console.log(payload, 'payloadpayloadpayload')
          updateSendInfo(payload).then((res) => {
            if (res.code === 0) {
              this.$message.success("操作成功!");
              this.$emit("change");
              this.onClose();
            }
          });
        } else return;
      });
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
  overflow-y: scroll;
}
.responsibilities-title {
  line-height: 48px;
}
.images-list {
  display: flex;
  .images-list__item {
    position: relative;
    margin: 0 8px;
    &:hover {
      .image-list__item-cover {
        opacity: 0.8;
      }
    }
    &:has(.el-image__error) {
      .image-list__item-cover {
        display: none;
      }
    }
    .image-list__item-cover {
      position: absolute;
      cursor: pointer;
      background-color: #000000aa;
      opacity: 0;
      pointer-events: none;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      z-index: 100;
      text-align: center;
      line-height: 96px;
      border-radius: 0.5rem;
      color: #fff;
    }
  }
}
</style>
