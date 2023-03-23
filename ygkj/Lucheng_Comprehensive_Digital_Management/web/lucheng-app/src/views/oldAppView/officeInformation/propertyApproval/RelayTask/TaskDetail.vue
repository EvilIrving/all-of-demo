<template>
  <view-page>
    <div class="task_detail">
      <van-cell-group>
        <van-cell
          value-class="cell-value"
          title-class="cell-title"
          title="工程名称"
          :value="form.projectName"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="工程类型"
          :value="form.projectType"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="问题类型"
          :value="form.problemType"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="时间"
          :value="form.tm"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="任务简述"
          :value="form.taskDescribe"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="具体隐患"
          :value="form.hiddenDanger"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="隐患图片(视频)"
          @click="imageView"
        >
          <template #default>
            <img
              src="../../../../../assets/images/c_img.png"
              v-if="imgs.length !== 0"
              class="images_box"
            />
          </template>
        </van-cell>
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="处理方式"
          :value="form.processWay"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="计划完成时间"
          :value="form.planTm"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="问题来源"
          :value="form.problemSource"
        />
        <van-cell
          value-class="cell-value"
          is-link
          title-class="cell-title"
          title="实施单位"
          :value="form.implementUnit"
        />
      </van-cell-group>
    </div>
  </view-page>
</template>

<script>
import { propertyMt } from "@/api/appView";
import { ImagePreview } from "vant";

export default {
  name: "TaskDetail",
  props: {},
  components: {},
  data() {
    return {
      id: "",
      form: {},
      imgs: [],
      click: true,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.id = this.$route.query.id;
    this.getDetail();
  },
  methods: {
    async getDetail() {
      let res = await propertyMt.DailyTask({
        id: this.id,
      });
      if (res.code === 0) {
        this.form = res.data.list[0];
        for (var item of this.form.images) {
          this.imgs.push(item.fileUrl);
        }
      }
    },
    imageView() {
      if (this.imgs.length > 0) {
        ImagePreview({
          images: this.imgs,
        });
      }
    },
  },
};
</script>

<style scoped lang="scss">
.task_detail {
  width: 100%;
  min-height: 100%;
  background: #f2f6f9;
  .cell-value {
    color: #333333;
    font-size: 20px;
    font-weight: 500;
    flex: 1.5;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 20px;
  }
  .images_box {
    width: 20px;
    height: 17px;
    margin-top: 2px;
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
</style>