<template>
  <div>
    <van-popup @click-overlay="onCancel" v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @confirm="onConfirm"
        @cancel="onCancel"
        :default-index="defaultIndex"
      />
    </van-popup>
  </div>
</template>

<script>
export default {
  props: {
    columns: {
      type: Array,
      default: () => {
        return [];
      },
    },
    showPicker: {
      type: Boolean,
      default: false,
    },
    defaultIndex: {
      type: Number,
      default: 0,
    }
  },
  methods: {
    /**
     * 
     * @param {*} data 选中值
     * @param {*} index 选中值对应的索引
     */
    onConfirm(data,index) {
      this.$emit("onConfirm", data, index);
    },
    onCancel() {
      this.$emit("onCancel");
    },
  },
};
</script>