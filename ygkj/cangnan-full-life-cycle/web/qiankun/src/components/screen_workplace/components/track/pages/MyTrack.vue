<template>
  <div class="my-track">
    <div class="track-item" v-for="item in trackList" :key="item.title">
      <div class="track-title">{{ item.title }}</div>
      <el-row>
        <el-col
          :span="24 / item.props.length"
          v-for="(child, idx) in item.props"
          :key="idx"
        >
          <span class="item-prop">{{ child.title }}:</span>
          <span class="item-value">{{ child.value}}</span>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { collectApi } from "@/api/collect";
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'my-track',
  components: {
  },
  data() {
    return {
      trackList: [{
        title: '最近登录',
        props: [
          { title: '登录时间', value: '2020-08-24 10:31' },
          { title: '登录IP', value: 'IP202008-24' },
        ]
      }, {
        title: '登录排名',
        props: [
          { title: '登陆次数', value: 16 },
          { title: '部门排名', value: 2 },
          { title: '单位排名', value: 6 },
        ]
      }, {
        title: '数据维护',
        props: [
          { title: '数据维护及时率', value: '76%' },
          { title: '部门排名', value: 2 },
          { title: '单位排名', value: 6 },
        ]
      }, {
        title: '访问功能',
        props: [
          { title: '最常访问功能', value: '水灾害防御->业务场景' },
        ]
      }, {
        title: '数据责任',
        props: [
          { title: '统计项', value: '共12类计800条' },
        ]
      }, {
        title: '收藏资料',
        props: [
          { title: '统计项', value: '123个' },
        ]
      }, {
        title: '个人角色',
        props: [
          { title: '具体角色', value: '财务负责人、超级管理员、数据录入员' },
        ]
      }]
    }
  },
  mounted() {
  },
  methods: {
    ...mapActions([]),
  }
}
</script>

<style lang="scss" scoped>
.my-track {
  height: 100%;
  overflow: auto;
  padding: 0 12px;
  .track-item {
    position: relative;
    padding-left: 180px;
    height: 70px;
    line-height: 70px;
    background: url(~assets/images/ScreenAsset/track-item-bg.png) no-repeat
      center center;
    background-size: 100% 100%;
    margin-bottom: 16px;
    .el-col{
      height: 70px;
      line-height: 70px;
    }
    .track-title {
      width: 130px;
      height: 36px;
      line-height: 36px;
      position: absolute;
      left: 24px;
      top: 18px;
      font-size: 22px;
      color: #ffffff;
      font-weight: 400;
      font-family: YouSheBiaoTiHei;
      background: url(~assets/images/ScreenAsset/track-title.png) no-repeat
        center center;
      background-size: 100% 100%;
      text-align: center;
    }
    .item-prop {
      font-family: PingFangSC-Semibold;
      font-size: 24px;
      color: #ffffff;
      font-weight: 600;
      padding-right: 8px;
    }
    .item-value {
      font-family: PingFangSC-Semibold;
      font-size: 24px;
      color: #1ef8f2;
      font-weight: 600;
    }
  }
  .list-item-opearte {
    cursor: pointer;
  }
  .my-track__content {
    .main-search-box {
      padding-bottom: 16px;
    }
  }
}
</style>