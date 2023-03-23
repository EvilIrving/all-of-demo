<script>
import { defineComponent, watch, ref } from '@vue/composition-api';
export default defineComponent({
  name: 'CapsuleTabs',
  model: { prop: 'value', event: 'input' },
  props: {
    value: { type: Number, default: 0 },
    tabs: { type: Array, default: () => [] }
  },
  setup(props, { emit }) {
    const activeIndex = ref(0);
    watch(
      () => props.value,
      (val) => {
        activeIndex.value = val;
      },
      { deep: true, immediate: true }
    );
    return (
      <div class="capsule-tabs">
        {props.tabs.map((item, index) => {
          const tabClass = ['capsule-tabs__item', index == activeIndex.value && 'active'];
          return (
            <div
              class={tabClass}
              onClick={() => {
                if (item.disabled) return;
                activeIndex.value = index;
                emit('input', index);
                emit('change', index);
              }}
              style={{ cursor: item.disabled && 'not-allowed' }}
            >
              <span>{item.label || item}</span>
            </div>
          );
        })}
      </div>
    );
  }
});
</script>

<style lang="scss" scpoed>
.capsule-tabs {
  display: inline-flex;
  margin-top: 4px;
  width: 200%;
  // overflow: auto;
  .capsule-tabs__item {
    height: 24px;
    line-height: 24px;
    cursor: pointer;
    user-select: none;
    padding: 2px 5px;
    border-radius: 16px;
    color: #fff;
    font-size: 14px;
    &.active {
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    }
  }
}
</style>
