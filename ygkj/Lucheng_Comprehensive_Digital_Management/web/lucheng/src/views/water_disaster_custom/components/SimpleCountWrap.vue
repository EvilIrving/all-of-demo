<template>
  <div :class="['count-card', !title && 'no-title']">
    <div v-if="title" :class="['drag', 'count-title']">
      <span class="title-name">
        <i :class="['icon-count-title-left']" />
        {{ title }}
        <i :class="['icon-count-title-right']" />
      </span>
      <span class="title-right">
        <slot name="title-right"></slot>
      </span>
      <div class="risk-top">
        <el-date-picker class="risk-daterange" :picker-options="pickerOptions" v-model="alarmTimes" size="mini" type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" :clearable="false" @change="handleChangeTimes" value-format="yyyy-MM-dd">
        </el-date-picker>
        <i class="reset-btn" @click="resetTimes"></i>
      </div>
      <!-- <span class="title-btn" @click="hideCount"> <i class="icon-hide" />隐藏 </span> -->
    </div>
    <div class="count-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'SimpleCountWrap',
  data() {
    return {
      alarmTimes: '',
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > new Date().getTime();
        },
      }, //限制日期选择范围
    };
  },
  props: {
    title: { type: String, default: '' },
    warn: { type: Boolean, default: false }
  },
  created() { },
  computed: {
    ...mapGetters([])
  },
  methods: {
    ...mapActions(['SetTempZindex','setPreviewTime', 'setSimpleRightType', 'setSimpleItem', 'setSimpleTimes', 'setSimplePotenData', 'SetActiveCount', 'SetAlarmDialog', 'titleType', 'SetStatisticFlag']),
    handleChangeTimes(val) {
      this.setSimpleTimes(val);
    },
    resetTimes() {
      this.alarmTimes = '';
      this.setSimpleTimes('');
      this.setPreviewTime('');
    },
  }
};
</script>

<style lang="scss" scoped>
@import '../countCards/style/CountCard';
.title-right {
  right: 80px;
  position: absolute;
  top: 0;
}
.risk-top {
  position: absolute;
  left: 70%;
  top: 11px;
  display: flex;
  color: #ffffff;
  font-size: 14px;
  align-items: center;
  justify-content: flex-start;
  .risk-daterange {
    width: 180px;
    height: 30px;
    border: 1px solid #08749a;
    background-color: transparent;
  }
  .reset-btn {
    margin-left: 10px;
    background: center/100% url(~assets/images/reset_icon.png) no-repeat;
    width: 30px;
    height: 30px;
    cursor: pointer;
  }
}
</style>
