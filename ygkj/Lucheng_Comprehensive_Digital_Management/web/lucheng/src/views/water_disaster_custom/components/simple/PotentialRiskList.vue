<template>
  <div
    ref="dragCard"
    class="drag-card collapse-panel__border drag-box"
    :class="{ 'is-big-screen': isBigScreen, 'drag-risk-card': riskIcon }"
  >
    <div class="drag-crad__content">
      <div class="tab-box">
        <div
          @click="checkTab(index)"
          v-for="(item, index) in options"
          :key="index"
          :class="{ active: index == optionsIndex }"
        >
          <span>{{ item }}</span>
          <span @click="clearItem(index)" class="el-icon-close icon-close"></span>
        </div>
      </div>
      <div class="tab-center">
        <SeawallAlarm v-if="tabOptions == 1" :type="simpleItem" />
        <TyphoonList v-if="tabOptions == 2" />
        <SeawallList v-if="tabOptions == 3" />
      </div>
    </div>
    <div class="risk-left-icon" :class="{ 'risk-icon': riskIcon }" @click="riskChange()"></div>
  </div>
</template>

<script>
import PanelItem from '../PanelItem.vue';
import SeawallAlarm from './components/SeawallAlarm.vue';
import TyphoonList from './components/TyphoonList.vue';
import SeawallList from './components/SeawallList.vue';
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'DragCard',
  components: {
    PanelItem,
    SeawallAlarm,
    TyphoonList,
    SeawallList
  },
  data() {
    return {
      riskIcon: false,
      tabOptions: 0,
      optionsIndex: 0,
      options: []
    };
  },
  mounted() {
    this.SetTempZindex();
    this.$nextTick(() => {
      this.$refs.dragCard.style.zIndex = this.TempZIndex;
    });
    if (this.simpleItem) {
      if (this.simpleItem == '台风影响') {
        this.tabOptions = 2;
      } else {
        this.tabOptions = 1;
      }
      this.options.push(this.simpleItem);
    }
    if (this.simpleData.name) {
      this.tabOptions = 3;
      this.options.push('海塘');
    }
  },
  watch: {
    simpleData: {
      handler: function (val) {
        if (val.name) {
          let flag = this.options.includes('海塘');
          if (!flag) {
            this.tabOptions = 3;
            this.options.push('海塘');
            this.optionsIndex = this.options.findIndex((item) => item == '海塘');
          }
        }
      },
      deep: true
    },
    simpleItem: function (val) {
      if (val) {
        if (val == '台风影响') {
          this.tabOptions = 2;
        } else {
          this.tabOptions = 1;
        }
        let data = this.options.filter((item) => item == '海塘');
        this.options = data;
        this.options.push(val);
        this.optionsIndex = this.options.findIndex((item) => item == val);
      } else {
        let data = this.options.filter((item) => item == '海塘');
        this.options = data;
      }
    },
    options: {
      handler: function (val) {
        if (val.length == 0) {
          this.setSimpleRightType(false);
        }
      },
      deep: true
    }
  },
  computed: {
    ...mapGetters(['TempZIndex', 'isBigScreen', 'simpleData', 'simpleItem'])
  },
  methods: {
    ...mapActions(['SetTempZindex', 'setSimpleRightType', 'setSimpleItem']),
    riskChange() {
      this.riskIcon = !this.riskIcon;
    },
    checkTab(index) {
      this.optionsIndex = index;
      if (this.options[index] == '海塘') {
        this.tabOptions = 3;
      } else if (this.options[index] == '台风影响') {
        this.tabOptions = 2;
      } else {
        this.tabOptions = 1;
      }
    },
    clearItem(index) {
      this.options.splice(index, 1);
      let arr = this.options.filter((item) => item != '海塘') || [];
      if (arr.length == 0) {
        this.setSimpleItem('');
      }
      if (this.options.length == 0) {
        this.setSimpleRightType(false);
      } else {
        this.optionsIndex = this.options.length - 1;
        if (this.options[this.optionsIndex] == '海塘') {
          this.tabOptions = 3;
        } else if (this.options[this.optionsIndex] == '台风影响') {
          this.tabOptions = 2;
        } else {
          this.tabOptions = 1;
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.drag-crad__content {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: scroll;
  padding: 0 16px;
  ::v-deep .panel-item__content {
    display: flex;
    flex-direction: column;
    &::-webkit-scrollbar {
      display: none;
    }
  }

  &::-webkit-scrollbar {
    display: none;
  }
}

.drag-card {
  z-index: 1000;
  width: 400px;
  color: #fff;
  position: relative;
  background: url(~assets/images/ScreenAsset/drag-big-card-icon.png) no-repeat;
  background-size: 100% 100%;
  background-color: rgba($color: #031a2b, $alpha: 0.9);
  top: 134px;
  right: 0;
  height: 75%;
  .risk-left-icon {
    position: absolute;
    width: 37px;
    height: 69px;
    background: url(~assets/images/ScreenAsset/risk-right-icon.png) no-repeat;
    background-size: 100% 100%;
    left: -37px;
    top: 50%;
    margin-top: -35px;
    cursor: pointer;
    &::after {
      content: '';
      width: 10px;
      height: 18px;
      background: url(~assets/images/ScreenAsset/risk-right-array-icon.png) no-repeat;
      background-size: 100% 100%;
      position: absolute;
      top: 50%;
      left: 50%;
      margin: -9px 0 0 -2px;
    }
    &.risk-icon {
      &::after {
        content: '';
        background: url(~assets/images/ScreenAsset/risk-left-array-icon.png) no-repeat;
        background-size: 100% 100%;
      }
    }
  }
}
.drag-risk-card {
  width: 0;
}

.tab-box {
  padding: 16px 0 15px;
  display: flex;
  position: relative;
  &::after {
    content: '';
    position: absolute;
    width: 100%;
    height: 3px;
    left: 0;
    bottom: 0;
    background: url(~assets/images/ScreenAsset/tab-options-bottom.png) no-repeat;
    background-size: 100% 100%;
  }
  > div {
    width: 97px;
    height: 34px;
    background: url(~assets/images/ScreenAsset/tab-options.png) no-repeat;
    background-size: 100% 100%;
    text-align: center;
    line-height: 34px;
    position: relative;
    margin-right: 17px;
    cursor: pointer;
    font-weight: 500;
    color: #ffffff;
    .icon-close {
      position: absolute;
      right: 2px;
      top: 2px;
      color: #ffffff;
      font-size: 12px;
      display: none;
    }
    &.active {
      background: url(~assets/images/ScreenAsset/tab-options-active.png) no-repeat;
      background-size: 100% 100%;
      color: #23e3fb;
    }
    &:hover {
      .icon-close {
        display: block;
      }
    }
  }
}
.tab-center {
  padding-top: 20px;
  height: calc(100% - 80px);
  box-sizing: border-box;
}
</style>

<style lang="scss">
</style>
