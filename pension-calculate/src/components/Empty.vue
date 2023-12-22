<template>
  <div class="c">
    <div class="el-empty__image" :style="imageStyle">
      <img v-if="image" :src="image" alt="" draggable="false">
      <slot v-else name='image'>
        <img src="" alt="">
      </slot>
    </div>
    <div class="el-empty__description">
      <slot name='description' v-if="$slots.description"></slot>
      <p v-else>{{emptyDescription}}</p>
    </div>
    <div v-if="$slot.default" class="el-empty__bottom">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Empty',
  props: {
    image: {
      type: String,
      default: ''
    },
    imageSize: Number,
    description: {
      type: String,
      default: ''
    },

  },
  components: {

  },
  computed: {
    emptyDescription() {
      return this.description || 'kong kong'
    },
    imageStyle() {
      return this.imageSize ? `${this.imageSize}px` : ''
    }
  },

};
</script>

<style scoped lang="scss">
.el-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  text-align: center;
  box-sizing: border-box;
  padding: 40px 0;
  .el-empty__image {
    width: 160px;
    img {
      user-select: none;
      object-fit: contain;
    }
    svg {
      fill: #dcdde0;
    }
    img,
    svg {
      width: 100%;
      height: 100%;
      vertical-align: top;
    }
  }
  .el-empty__description {
    margin-top: 20px;
    p {
      margin: 0;
      font-size: 14px;
      color: #999;
    }
  }
  .el-empty__bottom {
    margin-top: 20px;
  }
}
</style>
