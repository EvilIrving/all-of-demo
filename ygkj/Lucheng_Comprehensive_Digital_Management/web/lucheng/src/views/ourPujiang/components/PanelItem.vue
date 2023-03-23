<template>
  <div class='wrap_box'>
      <div class='wrap_box__title drag' v-if="title">
       <div class='box_title_text'>
          <span class='label_text' >
              <slot name='title_label'>{{title}}</slot>
          </span>
       </div>
      </div>

    <div class='wrap_box__content'>
      <slot name='content-tabs' style='height: 50px'>
        <div class='wrap_box__content--tabs' v-if='tabs'>
          <span
            v-for="(item, index) in tabs"
            :key="index"
            :class="[index == activeActionTab && 'active', 'tab-button']"
            @click="handleActionTabChange(index)"
          >
            {{ item }}
          </span>
        </div>
        <div class='wrap_box__content--select' v-if='selects'>
            <span>
              {{selects}}
            </span>
        </div>
      </slot>
      <slot></slot>
    </div>

  </div>
</template>

<script>

export default {
  name: 'PanelItem',
  props:{
    title:{
      type:[Object,String],
      default:()=>{
        return { text:'标题' }
      }
    },
    count:{
      type:Number
    },
    tabs:{
      type: Array
    },
    selects:{
      type:Array
    }

  },
  components:{

  },
  data(){
    return{
      activeActionTab: 0,
      tabArray:[],
    }
  },
  mounted() {

  },
  methods:{
    handleActionTabChange(index) {
      this.activeActionTab = index;
      this.$emit('change', index, this.tabs[index]);
    },
  }
};
</script>

<style scoped lang='scss'>
.wrap_box{
  display: flex;
  flex-direction: column;
 // background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
  height: 100%;
  width: 500px;

  .wrap_box__title{
    background: url(~assets/images/ScreenAsset/title-icon.png) no-repeat;
    height: 35px;
    width: 100%;
    background-size: 100% 100%;
    margin-top: 13px;

    color: #FFFFFF;
    span{
      font-size: 20px;
      margin-left:40px;

    }
  }
  .wrap_box__content{
    height: 100%;
    .wrap_box__content--tabs{
      //height: 50px;
      margin: 10px;
      .tab-button {
        cursor: pointer;
        user-select: none;
        height: 24px;
        line-height: 24px;
        padding: 0 3px;
        margin-bottom: 5px;
        margin-left: 5px;
        border-radius: 10px;
        color: #fff;
        font-size: 14px;
        &.active {
          padding: 2px 5px;
          // background: #00c1ff;
          background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
        }
      }
    }


  }

}
</style>