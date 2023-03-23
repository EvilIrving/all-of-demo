<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-22 15:34:10
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\defectTreatment\components\baseInfo.vue
-->
<template>
  <div class="wrap">
    <div class="wrap_box_title">
      <p>基本信息</p>
    </div>
    <div>
      <div class="info-one padding-22">
        <div>
          <div class="lable-item">工程名称</div>
          <div class="value-item">{{ infoDetail.projectName }}</div>
        </div>
        <div>
          <div class="lable-item">工程类型</div>
          <div class="value-item">{{ infoDetail.projectType }}</div>
        </div>
        <div>
          <div class="lable-item">来源</div>
          <div class="value-item">{{ infoDetail.problemSource }}</div>
        </div>
        <div>
          <div class="lable-item">上报人</div>
          <div class="value-item">{{ infoDetail.creator }}</div>
        </div>
      </div>
      <div class="padding-22">
        <div class="lable-item">任务描述</div>
        <div class="value-item">
          {{ infoDetail.taskDescribe }}
        </div>
      </div>
      <div class="padding-22">
        <div class="lable-item">具体隐患</div>
        <div class="value-item">
          {{ infoDetail.instructions }}
        </div>
      </div>
      <div>
        <div class="lable-item">隐患图片</div>
        <div class="imgae-item">
          <el-image
            v-for="item in infoDetail.images"
            :key="item.id"
            class="image-box"
            :src="item.fileUrl"
            :preview-src-list="[item.fileUrl]"
          >
          </el-image>
        </div>
      </div>
      <div class="info-one padding-22">
        <div>
          <div class="lable-item">处理方式</div>
          <div class="value-item">{{ infoDetail.processWay }}</div>
        </div>
        <div>
          <div class="lable-item">计划完成时间</div>
          <div class="value-item">{{ infoDetail.planTm }}</div>
        </div>
        <div>
          <div class="lable-item">实施单位</div>
          <div class="value-item">{{ infoDetail.implementUnit }}</div>
        </div>
        <div>
          <div class="lable-item">实施人员</div>
          <div class="value-item">{{ infoDetail.implementUser }}</div>
        </div>
      </div>
      <div class="padding-22">
        <div class="lable-item">问题来源</div>
        <div class="value-item">
          {{ infoDetail.problemSource }}
        </div>
      </div>
      <div class="padding-22">
        <div class="lable-item">指导意见</div>
        <div class="value-item">
          {{ infoDetail.instructions }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  props: {
    infoDetail: Object,
  },
  mounted() {
    console.log(this.infoDetail, "infoDetailinfoDetailinfoDetail");
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  padding-top: 16px;
  height: 100%;
  box-sizing: border-box;
  .wrap_box_title {
    position: relative;
    line-height: 25px;
    padding: 0 10px 16px 24px;
    border-bottom: 1px solid #eeeeee;
    &::after {
      content: "";
      position: absolute;
      width: 4px;
      height: 20px;
      background: #1492ff;
      left: 10px;
      top: 2px;
    }
    > p {
      font-size: 18px;
      font-weight: bold;
      color: #333333;
    }
  }
  > div:nth-child(2) {
    padding: 24px;
  }
}
.lable-item {
  font-size: 16px;
  color: #999999;
  padding-bottom: 10px;
}
.value-item {
  font-weight: 500;
  color: #333333;
  line-height: 18px;
  font-size: 16px;
}
.padding-22 {
  padding-bottom: 22px;
}
.info-one {
  display: flex;
  > div {
    flex: 1;
  }
}
.imgae-item {
  display: flex;
  flex-wrap: wrap;
  .image-box {
    width: 80px;
    height: 80px;
    margin: 0 20px 20px 0;
  }
}
</style>