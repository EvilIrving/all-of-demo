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
      <span class="title-btn" @click="hideCount"> <i class="icon-hide" />隐藏 </span>
    </div>
    <div class="count-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
export default {
  name: 'CountWrap',
  data() {
    return {};
  },
  props: {
    title: { type: String, default: '' },
    warn: { type: Boolean, default: false }
  },
  created() {},
  computed: {
    ...mapGetters([])
  },
  methods: {
    ...mapActions(['SetActiveCount', 'SetAlarmDialog', 'titleType', 'SetStatisticFlag']),
    alarmShow(val) {
      this.titleType(val);
      this.SetAlarmDialog(true);
    },
    hideCount() {
      this.SetStatisticFlag(false);
    }
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
</style>
