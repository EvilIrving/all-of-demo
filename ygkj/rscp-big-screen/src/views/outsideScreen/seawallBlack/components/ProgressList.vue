<!--
 * @Author: hanyu
 * @LastEditTime: 2021-08-13 17:48:54
 * @Description: 进度条列表
 * @FilePath: /rscp-big-screen/src/components/ProgressList.vue
-->
<template>
  <div class="progress_wrap">
    <div class="chart_title_black" v-if="!noTitle">
      {{ title }}
      <span v-if="unit" class="right unit">{{ unit }}</span>
    </div>
    <div class="progress_box" v-if="progressData.length">
      <div
        class="progress_item"
        v-for="(item, index) in progressData"
        :key="index"
        @click="selectAdcd(item, index)"
      >
        <p>{{ item.title }}</p>
        <el-progress
          :class="item.isSelected ? 'isSelected' : ''"
          :percentage="item.percentage"
          color="#6ad5fb"
        ></el-progress>
        <span>{{ item.nowNum }}</span>
      </div>
    </div>
    <div v-else class="noData">
      <img src="@/assets/images/noData-black.png" alt="" />
    </div>
  </div>
</template>

<script>
export default {
  name: "",
  props: {
    title: {
      type: String,
      required: true,
    },
    unit: {
      type: String,
      default: "",
    },
    height: {
      type: String,
      default: "300px",
    },
     progressData:  {
        type: Array,
        required: true,
        default: [
          {
            title: "市本级",
            allNum: 200,
            nowNum: 120,
            percentage: 60,
          },
        ],
      },
    // progressData: () => {
    //   return {
    //     type: Array,
    //     required: true,
    //     default: [
    //       {
    //         title: "市本级",
    //         allNum: 200,
    //         nowNum: 120,
    //         percentage: 60,
    //       },
    //     ],
    //   };
    
    noTitle: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  data() {
    return {};
  },
  mounted() {
    for (let i = 0; i < this.progressData.length; i++) {
      this.progressData[i].isSelected = false;
    }
  },
  methods: {
    selectAdcd(item, index) {
      if (!item.isSelected) {
        for (let i = 0; i < this.progressData.length; i++) {
          this.progressData[i].isSelected = false;
        }
        this.progressData[index].isSelected = true;
        this.$set(this.progressData, index, this.progressData[index]);
        this.$emit("selectAdcd", item);
      } else {
        this.progressData[index].isSelected = false;
        this.$set(this.progressData, index, this.progressData[index]);
        this.$emit("selectAdcd", {
          adcd: this.$localData("get", "userInfo").adcd,
          title: this.$localData("get", "userInfo").adnm,
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.progress_wrap {
  // background: #6F9DF2;
  height: 100%;
}
.progress_box {
  height: calc(100% - 42px);
  box-sizing: border-box;
  padding: 0 16px;
  overflow: auto;
  background: rgba($color: #000000, $alpha: 0.6);
  .progress_item {
    padding: 8px 16px;
    background: rgba($color: #000000, $alpha: 0.6);
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > p {
      @include flex(1);
      line-height: 24px;
      color: #999;
      font-size: 14px;
    }
    .el-progress {
      @include flex(4);
    }
    & > span {
      @include flex(0.8);
      font-size: 16px;
      color: #b5d7d7;
      line-height: 24px;
      font-weight: 500;
      text-align: right;
    }
    &:nth-child(2n) {
      background: rgba($color: #000000, $alpha: 0.6);
    }
    &:hover {
      cursor: pointer;
      background: #284e53;
      border: 1px solid rgba(84, 249, 252, 0.8);
      box-shadow: inset 0 0 16px 0 rgba(84, 249, 252, 0.6);
    }
  }
}
.noData {
  @include flexbox();
  @include flexAC();
  @include flexJC(center);
  width: 100%;
  height: 100%;
  img {
    width: 35%;
  }
}
</style>
<style lang="scss">
.isSelected {
  .el-progress-bar__inner {
    background: rgb(106, 213, 251) !important;
  }
}
.el-progress-bar__inner {
  border: none !important;
  border-radius: 0px !important;
}
.el-progress-bar__outer {
  border-radius: 0px !important;
  border: none !important;
  background: #11171b !important;
}
</style>
