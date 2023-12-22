<template>
  <div class="el-button" @click="handleClick" :disabeld='buttonDisabeld || loading'
  
  >
   
  </div>
</template>

<script>
export default {
  name: 'Button',
  props: {
    type: {
      type: String,
      default: 'default'
    },
    size: String,
    icon: {
      type: String,
      default: ''
    },
    nativeType: {
      type: String,
      default: 'button'
    },
    loading: Boolean,
    disabled: Boolean,
    plain: Boolean,
    autofocus: Boolean,
    round: Boolean,
    circle: Boolean,
  },
  inject: {
    elForm: {
      default: ""
    },
    elFormItem: {
      default: ''
    }
  },
  data() {
    return {

    };
  },
  computed: {
    _elFormItemSize() {
      return (this.elFormItem || {}).elFormItemSize
    },
    buttonSize() {
      return this.size || this._elFormItemSize || (this.$ELEMENT || {}).size
    },
    buttonDisabled() {
      return this.$options.propsData.hasOwnProperty('disabled') ? this.disabled : (this.elForm || {}).disabled
    }
  },
  methods: {

  },
};
</script>

<style scoped lang="scss">
</style>
