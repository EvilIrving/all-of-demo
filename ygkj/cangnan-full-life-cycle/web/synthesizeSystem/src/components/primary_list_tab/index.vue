<template>
  <div class="primary-tabs">
    <div class="tabs-label" v-if="label">{{ label }}</div>
    <div ref="tabsContent" class="tabs-content" v-if="!isMultiple">
      <!-- 单选 -->
      <div
        v-for="(item, index) in tabArray"
        :key="index"
        :title="item.label"
        v-show="index < showLength - 1"
        @click="tabChange(index)"
        :class="[selectedTabIndex === index ? 'active' : '', 'tab-item']"
      >
        {{ item.label }}
      </div>
      <div class="tab-item more-item" v-show="moreTag" @click="operaMore()">
        {{ moreText }}
      </div>
    </div>
    <div class="tabs-content" v-else>
      <!-- 多选 -->
      <template v-for="(item, index) in tabArray">
        <div
          @click="mutiTabChange(index)"
          :key="index"
          :class="[{ active: multiSelectedArr[index] }, 'tab-item']"
        >
          {{ item.label }}
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: "PrimaryTabs",
  props: {
    tabArray: Array,
    label: String,
    isMultiple: Boolean,
    defaultSelectValue: Array, //默认选中的索引数组
  },
  data() {
    return {
      selectedTabIndex: 0,
      multiSelectedArr: [],
      showLength: 99,
      moreTag: false, //更多按钮显隐控制
      moreText: "更多",
    };
  },
  components: {},
  computed: {
    selectedValue() {
      let value;
      if (!this.isMultiple) {
        value = this.tabArray[this.selectedTabIndex]
          ? this.tabArray[this.selectedTabIndex].value
          : "";
      }
      return value;
    },
  },
  mounted() {
    if (this.isMultiple) {
      this.initMultipleArr();
    }
    this.setMoretab();
    if (this.tabArray.length > this.showLength - 1) {
      this.moreTag = true;
    }
    // window.onresize = () => {
    //   this.setMoretab();
    // }
    // console.log(this.$refs.tabsContent.offsetWidth, 222);
  },
  watch: {
    tabArray: function (val) {
      if (val.length > this.showLength - 1) {
        this.moreTag = true;
      }
    },
    showLength: function (val) {
      if (this.tabArray.length > val - 1) {
        this.moreTag = true;
      } else if (this.tabArray.length < val - 1 && this.showLength != 99) {
        this.moreTag = false;
      }
    },
  },
  methods: {
    tabChange(index) {
      this.selectedTabIndex = index;
      this.$emit("tabChange", index);
    },
    // 重置单选选项
    initSingle(index) {
      this.selectedTabIndex = index;
    },
    //设置更多按钮
    setMoretab() {
      const tabWidth = this.$refs.tabsContent.offsetWidth;
      // console.log(tabWidth);
      this.showLength = parseInt(tabWidth / 100);
      // console.log(this.showLength)
    },
    //点击更多展开 收回
    operaMore() {
      if (this.showLength != 99) {
        this.showLength = 99;
        this.moreText = "收回";
      } else {
        this.moreText = "更多";
        this.setMoretab();
      }
    },
    initMultipleArr() {
      this.tabArray.map(() => {
        this.multiSelectedArr.push(false);
      });
      if (this.defaultSelectValue) {
        this.defaultSelectValue.map((item) => {
          this.multiSelectedArr[item] = true;
        });
        this.$emit("mutiTabChange", this.multiSelectedArr);
      }
    },
    mutiTabChange(index) {
      if (this.multiSelectedArr[index]) {
        this.multiSelectedArr.splice(index, 1, false);
      } else {
        this.multiSelectedArr.splice(index, 1, true);
      }
      this.$emit("mutiTabChange", this.multiSelectedArr, this.tabArray[index]);
    },
    returnMutiValue() {
      let resultList = [];
      this.multiSelectedArr.map((item, index) => {
        if (item) {
          resultList.push(this.tabArray[index].value);
        }
      });
      return resultList;
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/element.scss";
@import "@/style/theme.scss";
$tabHeight: 28px;
$tabColor: #333;
.primary-tabs {
  overflow: auto;
  display: flex;
  color: #666666;
  font-weight: normal;
  .tabs-label {
    width: 80px;
    height: $tabHeight;
    line-height: $tabHeight;
    font-size: 14px;
    color: $tabColor;
  }
  .tabs-content {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 24px;
    .tab-item {
      height: $tabHeight;
      line-height: $tabHeight;
      font-size: 14px;
      margin-right: 40px;
      text-align: center;
      color: $tabColor;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      padding: 0 16px;
      cursor: pointer;
      &.active {
        // border: 1px solid $menuActiveBorderColor;
        // color: $menuActiveBorderColor;
        background-color: #328173;
        color: #fff;
        border-radius: 4px;
      }
      &:last-child {
        margin-right: 0;
      }
      &.more-item {
        border: none;
        color: #036eff;
      }
    }
  }
}
</style>