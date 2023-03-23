<template>
  <div :class="['statistics-box', ...boxClass]">
    <div class="statistics-box__icon">
      <i :class="icon" />
    </div>
    <div class="statistics-box__content">
      <div class="content-title">{{ title }}</div>
      <div class="content-data">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StatisticsBox",
  props: {
    icon: { type: [String, Array], required: true },
    title: { type: String, required: true },
    boxClass: { type: [String, Array] },
  },
};
</script>

<style lang="scss">
.statistics-box {
  display: flex;
  flex-direction: row;
  padding: 10px 0;
  margin-right: 20px;
  justify-content: space-between;
  .statistics-box__icon {
    padding-top: 5px;
    margin-right: 15px;
    .icon-jiuyuanduiwu {
      width: 48px;
      height: 48px;
      background: center url(~assets/images/resuceteam-big.png);
      background-size: 100%;
    }
    .icon-reservoir-person {
      background-image: url(~assets/images/icon-reservoir-person.png);
      display: inline-block;
      width: 48px;
      height: 48px;
      background-size: 100% 100%;
    }
    .icon-reservoir-facility {
      background-image: url(~assets/images/icon-reservoir-facility.png);
      display: inline-block;
      width: 48px;
      height: 48px;
      background-size: 100% 100%;
    }
  }
  .statistics-box__content {
    font-family: PingFangSC-Regular;
    color: #ffffff;
    .content-title {
      font-size: 15px;
      line-height: 24px;
      font-weight: 400;
      margin-bottom: 5px;
    }
    .content-data {
      font-size: 14px;
      line-height: 24px;
      font-weight: 500;
      > span {
        font-size: 24px;
        color: #56fefe;
      }
    }
  }
}
</style>
