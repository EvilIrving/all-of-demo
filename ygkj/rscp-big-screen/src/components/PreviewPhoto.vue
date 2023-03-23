<!--
 * @Author: hanyu
 * @LastEditTime: 2021-07-28 17:03:15
 * @Description: PreviewPhoto
 * @FilePath: /rscp-big-screen/src/components/PreviewPhoto.vue
-->
<template>
  <div class='preview_bg' v-loading="loading" element-loading-background="rgba(0, 0, 0, .4)">
    <i class="el-icon-close" @click="closeLookPhotosPopup()"></i>
    <div class="preview_box" v-if="!loading" @click.stop>
      <p class="title">{{seawallName}}</p>
      <el-carousel ref="carousel" class="carousel_box" indicator-position="none" arrow="always" :autoplay="false" @change="changeItem">
        <el-carousel-item class="carousel_item" v-for="(item, index) in photoList" :key="index">
          <div class="swiper_img" :style="{backgroundImage:'url('+item.url+')'}" :title="item.name"></div>
        </el-carousel-item>
      </el-carousel>
      <div class="preview_list">
        <div class="preview_item" v-for="(item, index) in photoList" :key="index">
          <div :style="{backgroundImage:'url('+item.smallUrl+')'}" :class="['img', imgIndex == index ? 'active' : '']" :title="item.name" @click.stop="chooseItem(index)"></div>
        </div>
      </div>
      <div class="preview_words"><span>{{imgIndex+1}}</span> / {{photoList.length}}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PreviewPhoto',
  props: {
    loading: {
      type: Boolean,
      default: true
    },
    photoList: {
      type: Array,
      required: true
    },
    seawallName: {
      type: String,
      default: ''
    }
  },
  components: {
    
  },
  data () {
    return {
      imgIndex: 0
    };
  },
  watch:{

  },
  mounted() {
    
  },
  methods: {
    closeLookPhotosPopup(){
      this.$emit('closeLookPhotosPopup')
    },
    chooseItem(index){
      this.imgIndex = index
      this.$refs.carousel.setActiveItem(index)
    },
    changeItem(index){
      this.imgIndex = index
    }
  }
}
</script>

<style lang='scss' scoped>
  .preview_bg{
    position: absolute;
    top: 0;
    left: 0;
    z-index: 9999;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,.6);
    .el-icon-close{
      position: absolute;
      top: 40px;
      right: 20px;
      color: #fff;
      font-size: 40px;
      cursor: pointer;
      z-index: 9;
    }
    .preview_box{
      position: relative;
      width: 80%;
      height: 90%;
      margin: 2% auto;
      border-radius: 4px;
      overflow: hidden;
      .title{
        height: 60px;
        line-height: 60px;
        font-size: 30px;
        letter-spacing: 3px;
        color: #fff;
        text-align: center;
        padding-bottom: 15px;
      }
      .carousel_box{
        height: calc(100% - 350px);
        .carousel_item{
          height: 100%;
        }
      }
      .swiper_img{
        background: rgba(0,0,0,.6);
        width: 100%;
        height: 100%;
        background-repeat: no-repeat;
        background-size: auto 100%;
        background-position: center;
        border-radius: 4px;
      }
      .preview_list{
        margin-top: 20px;
        // width: 100%;
        overflow: auto;
        @include flexbox();
        .preview_item{
          .img{
            margin-right: 10px;
            width: 200px;
            height: 200px;
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center;
            box-sizing: border-box;
            cursor: pointer;
            border-radius: 4px;
            &.active{
              border: 4px solid #1158f0;
              @include shadow(#1158f0, -1px, 0px, 10px);
            }
          }
        }
      }
      .preview_words{
        padding-top: 16px;
        text-align: center;
        color: #fff;
        font-size: 26px;
        font-weight: 500;
        span{
          font-weight: 500;
          font-size: 30px;
        }
      }
    }
  }
</style>
<style lang='scss'>
  .el-carousel__container{
    height: 100%;
  }
  .preview_bg .el-carousel__arrow{
    width: 60px;
    height: 60px;
    i{
      font-size: 30px;
    }
  }
</style>