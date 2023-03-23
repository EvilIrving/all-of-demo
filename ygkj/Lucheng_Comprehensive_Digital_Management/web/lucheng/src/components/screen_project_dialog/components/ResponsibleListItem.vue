<template>
  <div class="project-responsible-item">
    <div class="item-title">{{ Info.title }}</div>
    <div class="item-contact">
      <div class="contact-avatar">
        <el-avatar shape="square" :src="squareUrl"></el-avatar>
      </div>
      <div class="contact-content">
        <div class="contact-name">
          <span>{{ Info.name }}</span>
          <span>
            <el-tooltip content="信息" placement="top">
              <i class="icon-proj-message" @click="visible = true" />
            </el-tooltip>

            <!-- <el-tooltip content="通话" placement="top">
              <i class="icon-proj-viedo-call" />
            </el-tooltip> -->
            <!-- 隐藏钉钉按钮-10.07 -->
            <!-- <el-tooltip content="钉钉" placement="top">
              <i class="icon-proj-dingtalk" />
            </el-tooltip> -->
          </span>
        </div>
        <div class="contact-position">{{ Info.position }}</div>
        <div class="contact-phone">{{ Info.phone }}</div>
      </div>
    </div>
    <div class="primary-dialog" v-show="visible">
      <el-dialog top="30vh" width="600px" destroy-on-close :before-close="() => (visible = false)" :modal="false" :visible.sync="visible">
        <!-- 弹窗标题 -->
        <div class="primary-dialog__title" slot="title">
          <p class="primary-dialog__title--label">发送信息</p>
        </div>
        <!-- 内容区域 -->
        <div class="primary-dialog__content">
          <el-row :gutter="14">
            <el-col :span="4" style="line-height: 2.75rem; color: #fff">预警内容：</el-col>
            <el-col :span="20">
              <el-input type="textarea" v-model="content" />
            </el-col>
          </el-row>
          <el-row :gutter="14">
            <el-col :span="4" style="line-height: 2.75rem; color: #fff">发送类型：</el-col>
            <el-col :span="20" style="line-height: 2.75rem">
              <el-radio-group v-model="sendType">
                <el-radio :label="1">短信</el-radio>
                <el-radio :label="2">浙政钉</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row type="flex" style="justify-content: flex-end">
            <div class="send_btn" @click="sendContent">发送</div>
          </el-row>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { reservoirRegulationApi } from '@/api/dataScreen/ReservoirRegulation';
export default {
  name: 'ResonsibleListItem',
  props: {
    data: {
      type: Object
    },
    flag: {
      type: Number
    }
  },

  data() {
    return {
      squareUrl: require('@/assets/images/WaterDisastersImage/seawall-user-icon.png'),
      visible: false,
      content: null,
      sendType: 2,
      Info: []
    };
  },
  mounted() {
    this.Info = this.data
    this.$nextTick(() => {
      this.flag < 3 ? this.Info.phone = this.Info.phone.substring(0, 3) + '****' + this.Info.phone.substring(7) : ''
    })
  },
  methods: {
    sendContent() {
      let obj = {
        context: this.content,
        phone: this.data.phone,
        type: this.sendType
      };
      reservoirRegulationApi.sendMsg(obj).then((res) => {
        if (res.code == 0 && res.data.result === 0) {
          this.content = '';
          this.sendType = 2;
          this.visible = false;
          this.$message({
            type: 'success',
            message: res.message
          });
        } else {
          this.$message({
            type: 'error',
            message: `发送失败: ${res.data.errMsg}`
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.project-responsible-item {
  margin-bottom: 16px;
  .item-title {
    color: #00baff;
    font-size: 14px;
    margin-bottom: 10px;
  }
  .item-contact {
    width: 186px;
    border-radius: 4px;
    padding: 6px;
    border: 1px solid rgba(0, 193, 255, 0.2);
    display: flex;
    justify-content: space-between;
    .contact-avatar {
      height: 64px;
      width: 64px;
      ::v-deep .el-avatar {
        width: 100%;
        height: 100%;
        background: transparent;
      }
    }
    .contact-content {
      width: calc(100% - 70px);
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      color: rgba(255, 255, 255, 0.2);
      font-size: 14px;
      .contact-name {
        color: #fff;
        font-size: 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        i {
          cursor: pointer;
          margin: 0 5px;
          &:first-child {
            margin: 0;
          }
          &:last-child {
            margin: 0;
          }
        }
      }
      .contact-position {
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }
}
.send_btn {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  line-height: 30px;
  float: right;
  color: #fff;
  cursor: pointer;
  border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
  background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
}
::v-deep .primary-dialog .el-dialog .el-dialog__body {
  padding: 1.125rem;
}
::v-deep .el-textarea .el-textarea__inner {
  background-color: transparent;
  color: #fff;
}
</style>