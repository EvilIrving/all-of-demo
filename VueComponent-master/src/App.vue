<template>
  <div id="app">
    <div class="box">
      <div class="title">srcollTab：</div>
      <scroll-tab :tabList="tabList" :start-index="4" @click="onConfirm">
        <template v-slot="{ item, activiteIndex, index }">
          <div
            class="tab-item"
            :class="{ 'tab-item-choose': index === activiteIndex }"
          >
            {{ item }}
          </div>
        </template>
      </scroll-tab>
    </div>
    <div class="box">
      <div class="title">popup:</div>
      <div class="box-content">
        <div
          class="btn"
          @click="
            () => {
              showBottom = true;
            }
          "
        >
          bottom
        </div>
        <div
          class="btn"
          @click="
            () => {
              showTop = true;
            }
          "
        >
          top
        </div>
        <div
          class="btn"
          @click="
            () => {
              showRight = true;
            }
          "
        >
          right
        </div>
        <div
          class="btn"
          @click="
            () => {
              showLeft = true;
            }
          "
        >
          left
        </div>
        <div
          class="btn"
          @click="
            () => {
              showCenter = true;
            }
          "
        >
          center
        </div>
      </div>
    </div>
    <div class="box">
      <div class="title">swipe：</div>
      <swipe
        :list="urlList"
        @change="onConfirm"
        :continuous="true"
        :start-index="2"
        :show-indicators="true"
        :auto-play="false"
        :wrapper-padding="0"
      >
        <template v-slot="{ item, id }">
          <div class="swipe-item" :style="{ backgroundColor: item }">
            {{ id | fixId }}
          </div>
        </template>
      </swipe>
    </div>
    <div class="box">
      <div class="title">regionPicker：</div>
      <div class="box-content">
        <div
          class="btn"
          @click="
            () => {
              visible1 = true;
            }
          "
        >
          regionPicker：
        </div>
      </div>
    </div>
    <div class="box">
      <div class="title">preview:</div>
      <picture-view :list="previewList"></picture-view>
    </div>
    <picker
      v-model="value1"
      :visible="visible1"
      :showTitle="true"
      :close="
        () => {
          visible1 = false;
        }
      "
      :is-link="true"
      @confirm="onConfirm"
    />
    <div class="box">
      <div class="title">steps:基础用法</div>
      <steps :active="stepActive" @click-step='handleStep'>
        <step>步骤1</step>
        <step>步骤2</step>
        <step>步骤3</step>
        <step>步骤4</step>
      </steps>
    </div>
    <div class="box">
      <div class="title">steps:自定义颜色</div>
      <steps :active="stepActive" activeColor="success" inactiveColor="warning">
        <step>步骤1</step>
        <step>步骤2</step>
        <step>步骤3</step>
        <step>步骤4</step>
      </steps>
    </div>
    <div class="box">
      <div class="title" @click="clickStep" style="textAlign:center;border:1px solid var(--border)">步进</div>
    </div>
    <div class="box">
      <div class="title">steps:自定义图标</div>
      <steps
        :active="stepActive"
        activeColor="success"
        finishIcon="thumbs_up_line"
        activeIcon="fire_line"
        inactiveIcon="close_line"
      >
        <step>步骤1</step>
        <step>步骤2</step>
        <step>步骤3</step>
        <step>步骤4</step>
      </steps>
    </div>
    <div class="box">
      <div class="title">steps:自定义插槽</div>
      <steps
        :active="stepActive"
        activeColor="success"
        finishIcon="thumbs_up_line"
        activeIcon="fire_line"
        inactiveIcon="close_line"
      >
        <step>
          <div
            slot="active-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--danger)',
            }"
          ></div>
          <div
            slot="finish-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--success)',
            }"
          ></div>
          <div
            slot="inactive-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--body)',
            }"
          ></div>
          步骤1
        </step>
        <step>
          <div
            slot="active-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--danger)',
            }"
          ></div>
          <div
            slot="finish-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--success)',
            }"
          ></div>
          <div
            slot="inactive-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--body)',
            }"
          ></div>
          步骤2
        </step>
        <step>
          <div
            slot="active-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--danger)',
            }"
          ></div>
          <div
            slot="finish-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--success)',
            }"
          ></div>
          <div
            slot="inactive-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--body)',
            }"
          ></div>
          步骤3
        </step>
        <step>
          <div
            slot="active-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--danger)',
            }"
          ></div>
          <div
            slot="finish-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--success)',
            }"
          ></div>
          <div
            slot="inactive-icon"
            :style="{
              width: '20px',
              height: '20px',
              backgroundColor: 'var(--body)',
            }"
          ></div>
          步骤4
        </step>
      </steps>
    </div>
    <div class="box">
      <div class="title">steps:垂直</div>
      <steps :active="stepActive" direction="vertical">
        <step>步骤1</step>
        <step>步骤2</step>
        <step>步骤3</step>
        <step>步骤4</step>
      </steps>
    </div>
    <popup v-model="showBottom" :close="closePopup" :popupPosition="'bottom'">
      <div class="popup-content">bottom</div>
    </popup>
    <popup v-model="showTop" :close="closePopup" :popupPosition="'top'">
      <div class="popup-content">top</div>
    </popup>
    <popup v-model="showRight" :close="closePopup" :popupPosition="'right'">
      <div class="popup-content">right</div>
    </popup>
    <popup v-model="showLeft" :close="closePopup" :popupPosition="'left'">
      <div class="popup-content">left</div>
    </popup>
    <popup v-model="showCenter" :close="closePopup" :popupPosition="'center'">
      <div class="popup-content">center</div>
    </popup>
  </div>
</template>
  
<script>
import ScrollTab from "@/components/ScrollTab.vue";
import Popup from "@/components/Popup.vue";
import Swipe from "@/components/Swipe.vue";
import Picker from "@/components/picker/Picker.vue";
import PictureView from "./components/PictureView.vue";
import { PREVIEW } from "@/assets/js/const";
import Steps from "@/components/Steps";
import Step from "@/components/Step";
export default {
  name: "App",
  data() {
    return {
      tabList: [
        "星星点点",
        "一表人才",
        "万里长城",
        "省得惹我",
        "热火朝天",
        "千军万马",
        "万水千山",
        "众多非一",
      ],
      urlList: ["#EC7063", "#48C9B0", "#5DADE2", "#A569BD"],
      previewList: PREVIEW,
      showBottom: false,
      showTop: false,
      showRight: false,
      showLeft: false,
      showCenter: false,
      visible1: false,
      value1: ["北京P", "北京市C", "朝阳区R"],
      stepActive: 2,
    };
  },
  methods: {
    onConfirm(val) {
      console.log(val);
    },
    closePopup() {
      this.showBottom = false;
      this.showTop = false;
      this.showRight = false;
      this.showLeft = false;
      this.showCenter = false;
    },
    clickStep() {
      this.stepActive++;
      if(this.stepActive>=4){
        this.stepActive = 0
      }
    },
    handleStep(index){
      console.log('step',index);
    }
  },
  filters: {
    fixId(id) {
      if (id === 0) {
        return 4;
      } else if (id === 5) {
        return 1;
      }
      return id;
    },
  },
  components: {
    ScrollTab,
    Popup,
    Swipe,
    Picker,
    PictureView,
    Steps,
    Step,
  },
};
</script>

<style scope lang="scss">
body {
  margin: 0;
}
#app {
  padding: 0 $px-10;
  .box {
    .title {
      font-size: $px-20;
      margin: $px-8 0;
    }
    .box-content {
      display: flex;
      flex-wrap: wrap;

      .btn {
        width: 30%;
        background-color: $c-primary;
        display: inline-block;
        text-align: center;
        padding: $px-8;
        color: $c-white;
        margin: $px-8 $px-8 0 0;
        box-sizing: border-box;
      }
    }
  }
  .popup-content {
    text-align: center;
    font-size: $px-30;
    margin: 0 auto;
  }
  .tab-item {
    text-align: center;
    border: 1px solid $c-primary;
    border-radius: $px-8;
    padding: $px-8 0;
    font-size: $px-16;
  }

  .tab-item-choose {
    background-color: $c-primary;
    color: $c-white;
  }

  .swipe-item {
    width: 100%;
    height: 120px;
    font-size: $px-30;
    line-height: 120px;
    color: $c-white;
    text-align: center;
    // border-radius: $px-8;
  }
}
</style>
