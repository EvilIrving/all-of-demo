<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-13 15:35:33
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\supervise\superviseIndex.vue
-->
<template>
  <div class="danger-box">
    <div class="danger-tap">
      <div
        @click="checkTab(index)"
        :class="{ active: index == tabAction }"
        v-for="(item, index) in tabList"
        :key="item"
      >
        {{ item }}
      </div>
    </div>
    <div class="table-box">
      <AKeySupervisory :detail='detail' :code="code" v-if="tabAction == 0" />
      <SpecialistList :code="code" v-if="tabAction == 1" />
      <Repository :code="code" v-if="tabAction == 2" />
    </div>
  </div>
</template>

<script>
import AKeySupervisory from './components/AKeySupervisory.vue';
import Repository from './components/Repository.vue';
import SpecialistList from './components/SpecialistList.vue';
export default {
  props: {
    code: {
      type: String,
      default: ''
    },
    detail: {
      type: Object,
      default: {}
    }
  },
  components: {
    AKeySupervisory,
    Repository,
    SpecialistList
  },
  data() {
    return {
      tabList: ['一键督办', '专家库', '知识库'],
      tabAction: 0
    };
  },
  methods: {
    checkTab(index) {
      this.tabAction = index;
    }
  }
};
</script>

<style lang="scss" scoped>
.danger-box {
  padding-top: 22px;
  height: 720px;
  .danger-tap {
    display: flex;
    > div {
      width: 114px;
      height: 32px;
      border: 1px solid rgba(255, 255, 255, 0.6);
      line-height: 32px;
      text-align: center;
      margin-right: 16px;
      color: rgba(255, 255, 255, 0.8);
      cursor: pointer;
      &.active {
        color: #00baff;
        background: rgba(72, 152, 251, 0.1);
        box-shadow: 0px 0px 5px 0px #00baff inset;
        border-color: rgba(0, 186, 255, 0.6);
      }
    }
  }
}
.table-box {
  margin-top: 24px;
}
</style>