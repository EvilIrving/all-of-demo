<template>
  <div class="progress_detail">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <van-cell-group>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="工程名称"
        :value="form.projectName"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="检查时间"
        :value="form.inspectionTime"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="责任单位"
        :value="form.responsibleUnitName"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="责任人"
        :value="form.responsiblePeopleName"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="督办状态"
      >
        <template #default>
          <span>{{ form.supervisionStatus | supervisionStatus }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="处理状态"
      >
        <template #default>
          <span>{{ form.handlingStatus == 1 ? "未处理" : "已处理" }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="归档状态"
      >
        <template #default>
          <span>{{ form.archivingStatus == 1 ? "未归档" : "已归档" }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="存在问题"
        :value="form.problems"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="隐患图片"
      >
        <template #default>
          <div class="upload-preview-box" v-if="form.problemsPicture">
            <div v-for="item in form.problemsPictureList" :key="item.id">
              <van-image
                width="70"
                height="70"
                @click="imagePreview(item.fileUrl)"
                :src="item.fileUrl"
              />
            </div>
          </div>
          <span v-else>暂无隐患图片</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="处理后说明"
        :value="form.handlingInstruction"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="处理后图片"
      >
        <template #default>
          <div class="upload-preview-box" v-if="form.handlingPicture">
            <div v-for="item in form.handlingPictureList" :key="item.id">
              <van-image
                width="70"
                height="70"
                @click="imagePreview(item.fileUrl)"
                :src="item.fileUrl"
              />
            </div>
          </div>
          <span v-else>暂无处理后图片</span>
        </template>
      </van-cell>
    </van-cell-group>
    <div class="worksheet">
      <div class="worksheet-title">进度查询</div>
      <div class="block">
        <el-timeline>
          <el-timeline-item
            :timestamp="form.createTime"
            color="#036EFF"
            class="active-timeline"
            placement="top"
          >
            <el-card class="card-box">
              <p>工程名称： {{ form.projectName }}</p>
              <p>隐患信息： {{ form.problems }}</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            :timestamp="form.updateTime"
            color="#036EFF"
            class="active-timeline"
            placement="top"
          >
            <el-card class="card-box">
              <p>责任单位： {{ form.responsibleUnitName }}</p>
              <p>
                处理状态：
                <span class="blue-color">已处理</span>
              </p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item color="#1492FF">
            <div class="pigeonhole-box">
              归档 <span>{{ form.updateTime }}</span>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    <van-button type="info" block @click="linkTask">评价</van-button>
    <van-popup
      v-model="showPopup"
      position="bottom"
      :style="{ height: '100%' }"
    >
      <div class="files-look">
        <div class="files-look-title">
          <h3>
            {{ iframeTitle }}
          </h3>
          <van-icon class="close-box" @click="showPopup = false" name="close" />
        </div>
        <iframe class="iframe-box" :src="iframeSrc" frameborder="0"></iframe>
      </div>
    </van-popup>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/projectDetail";
import { handingInspectionApi } from "@/api/rainfall";
export default {
  name: "ProgressDetail",
  props: {},
  components: {},
  data() {
    return {
      form: {},
      showPopup: false,
      iframeSrc: "",
      iframeTitle: "",
    };
  },
  filters: {
    supervisionStatus: (value) => {
      switch (value) {
        case "1":
          return "未督促";
        case "2":
          return "已督促";
        default:
          return "-";
      }
    },
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.list({ id: this.$route.query.id });
      if (res.code == 0) {
        this.form = res.data.list[0];
      }
    },
    linkTask() {
      this.$router.push("/propertySupervision/evaluateArchive?id=" + this.form.id);
    },
    lookFile(data) {
      this.iframeSrc = data.fileUrl;
      this.iframeTitle = data.fileName;
      this.showPopup = true;
    },
    imagePreview(src) {
      this.$imagePreview([src]);
    },
  },
};
</script>

<style scoped lang="scss">
.upload-preview-box {
  display: flex;
  width: 100%;
  flex-flow: wrap;
  > div {
    margin: 0 9px 9px 0;
    position: relative;
  }
}
.progress_detail {
  width: 100%;
  min-height: 100%;
  background: #f2f6f9;
  .cell-value {
    color: #333333;
    font-size: 14px;
    font-weight: 500;
    flex: 2.2;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 14px;
  }
  .worksheet {
    margin-top: 6px;
    background-color: #ffffff;
    .worksheet-title {
      padding: 15px 0 0 11px;
      font-size: 16px;
      font-weight: 600;
      color: #333333;
    }
    .block {
      padding: 25px 12px 20px 27px;
      .card-box {
        box-shadow: none;
        border: none;
        background-color: #e9f4ff;
        position: relative;
        p {
          font-size: 12px;
          color: #333333;
          padding-bottom: 6px;
          &:last-child {
            padding-bottom: 0;
          }
        }
        .card-btn {
          position: absolute;
          font-size: 14px;
          color: #036eff;
          font-weight: 500;
          right: 15px;
          top: 50%;
          margin-top: -8px;
        }
      }
    }
  }
}
.van-button--block {
  width: 94%;
  border-radius: 4px;
  margin: auto;
}
.van-button--primary {
  width: 94%;
  border: #036eff solid 1px;
}
.van-button::before {
  background-color: #fff;
}
.default-box {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
  .img-box {
    width: 20px;
    margin-right: 5px;
  }
  span {
    max-width: calc(100% - 26px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
.files-look {
  height: 100%;
  .files-look-title {
    height: 44px;
    background-color: #036eff;
    color: #ffffff;
    position: relative;
    > h3 {
      line-height: 44px;
      text-align: center;
      font-size: 16px;
      padding: 0 30px;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .close-box {
      position: absolute;
      right: 10px;
      top: 12px;
      font-size: 20px;
    }
  }
  .iframe-box {
    width: 100%;
    height: calc(100% - 48px);
  }
}
</style>
<style lang="scss">
.van-cell {
  padding-left: 10px;
  padding-right: 10px;
  &::after {
    left: 0;
    right: 0;
    border-color: #e7e7ec;
  }
}
.block {
  .active-timeline {
    .el-timeline-item__tail {
      border-color: #036eff;
    }
  }
  .el-timeline-item__timestamp {
    color: #333333;
    font-size: 14px;
  }
  .card-box {
    .el-card__body {
      padding: 8px 15px 8px 10px;
    }
  }
}
</style>