<template>
  <div class='preview_bg previewbgs' v-loading="loading" element-loading-background="rgba(0, 0, 0, .4)">
    <i class="el-icon-close" @click="closeTitlePhotosPopup()"></i>
    <div class="preview_box" v-if="!loading" @click.stop>
      <el-carousel ref="carousel" class="carousel_box" indicator-position="inside" arrow="always" :autoplay="false" @change="changeItem">
        <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
          <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div>
        </el-carousel-item>
      </el-carousel>
      <!-- <div class="preview_list">
        <div class="preview_item" v-for="(item, index) in photoList" :key="index">
          <div :style="{backgroundImage:'url('+item.smallUrl+')'}" :class="['img', imgIndex == index ? 'active' : '']" :title="item.name" @click.stop="chooseItem(index)"></div>
        </div>
      </div> -->
      <!-- <div class="preview_words"><span>{{imgIndex+1}}</span> / {{photoList.length}}</div> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'CarouselPhoto',
  props: {
    loading: {
      type: Boolean,
      default: true
    },
    photoList: {
      type: Array,
      required: true
    },
  },
  components: {

  },
  data() {
    return {
      imgIndex: 0
    };
  },
  watch: {

  },
  mounted() {
  },
  methods: {
    closeTitlePhotosPopup() {
      this.$emit('closeTitlePhotosPopup')
    },
    chooseItem(index) {
      this.imgIndex = index
      this.$refs.carousel.setActiveItem(index)
    },
    changeItem(index) {
      this.imgIndex = index
    }
  }
}
</script>

<style lang='scss' scoped>
.preview_bg {
  position: absolute;
  left: 0%;
  top: 0%;
  z-index: 9999;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  .el-icon-close {
    position: absolute;
    top: 14%;
    right: 23.5%;
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    z-index: 9;
  }
  .preview_box {
    position: relative;
    width: 85%;
    height: 90%;
    margin: 3% auto;
    border-radius: 4px;
    overflow: hidden;
    .title {
      height: 60px;
      line-height: 60px;
      font-size: 30px;
      letter-spacing: 3px;
      color: #fff;
      text-align: center;
      padding-bottom: 15px;
    }
    .carousel_box {
      height: 100%;
      .carousel_item {
        height: 100%;
      }
    }
    .swiper_img {
      padding: 20px;
      background: rgba($color: #0067e9, $alpha: 0.6);
      width: 100%;
      height: 90%;
      // margin: 40px auto 0;
      background-repeat: no-repeat;
      background-size: 90%;
      background-position: center;
      border-radius: 4px;
    }
    // .preview_list{
    //   margin-top: 20px;
    //   overflow: auto;
    //   @include flexbox();
    //   .preview_item{
    //     .img{
    //       margin-right: 10px;
    //       width: 200px;
    //       height: 200px;
    //       background-repeat: no-repeat;
    //       background-size: cover;
    //       background-position: center;
    //       box-sizing: border-box;
    //       cursor: pointer;
    //       border-radius: 4px;
    //       &.active{
    //         border: 4px solid #1158f0;
    //         @include shadow(#1158f0, -1px, 0px, 10px);
    //       }
    //     }
    //   }
    // }
    .preview_words {
      padding-top: 16px;
      text-align: center;
      color: #fff;
      font-size: 26px;
      font-weight: 500;
      span {
        font-weight: 500;
        font-size: 30px;
      }
    }
  }
}
</style>
<style lang='scss'>
.el-carousel__container {
  height: 100%;
}
.previewbgs .el-carousel__arrow {
  width: 45px  !important;
  height: 45px !important;
  i {
    color: #1158f0;
    font-size: 21px;
  }
}

.previewbgs {
  .el-carousel__arrow {
    width: 45px  !important;
  height: 45px !important;
    top: 50%;
  }
  .el-carousel__arrow--left {
    left: 100px;
  }
  .el-carousel__arrow--right {
    right: 100px;
  }
}
</style>