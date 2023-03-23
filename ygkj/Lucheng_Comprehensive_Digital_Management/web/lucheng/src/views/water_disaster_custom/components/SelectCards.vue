<template>
  <div class="select-cards">
    <el-row class="search-area" :gutter="30">
      <el-col :span="4">
        <el-input class="input-box" v-model="searchName" prefix-icon="el-icon-search" size="small" placeholder="请输入您要查找的内容" @change="searchComponent"></el-input>
      </el-col>
      <el-col :span="20" class="cards-tabs">
        <div :class="['tab-item', tabActive == -1 ? 'active' : '']" @click="doSelectClick()">
          <p>已选</p>
          <p>{{ checkList.length }}</p>
        </div>
        <div v-for="(item, idx) in this.statisticList" :key="idx" :class="['tab-item', tabActive == idx ? 'active' : '']" @click="changeStatistic(item, idx)">
          <p>{{ item.key }}</p>
          <p>{{ item.value }}</p>
        </div>
      </el-col>
    </el-row>
    <div class="card-box clearfix">
      <div class="card-item-box fl" v-for="(item, idx) in cardList" :key="idx" @click="selectCardComponent(item.unitName)">
        <div class="card-tag">
          <i v-if="item.drill" class="icon-drill"></i>
          <i :class="item.per == 'one' ? 'icon-one-one' : 'icon-one-third'"></i>
        </div>
        <div :class="['card-item', checkList.includes(item.unitName) ? 'active' : '']">
          <i v-if="checkList.includes(item.unitName)" class="icon-card-selected"></i>
          <img :src="item.imgSrc" />
          <div class="card-title">{{ item.unitName }}</div>
          <div class="order-num">
            {{ checkList.indexOf(item.unitName) == -1 ? '' : checkList.indexOf(item.unitName) + 1 }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/';

export default {
  name: 'SelectCards',
  data() {
    return {
      checkList: [],
      searchName: '',
      statisticList: [{ key: '全部', value: 0 }],
      tabActive: 0,
      cardList: [],
      allList: [] //接口获取的组件列表
    };
  },
  computed: {
    ...mapGetters(['SelectList', 'CheckedArr', 'BsName']),
    //判断微应用模式还是直接访问模式
    qiankunModule() {
      return window.__POWERED_BY_QIANKUN__;
    }
  },
  watch: {},
  mounted() {
    this.checkList = Array.from(this.CheckedArr);
    this.listUnit();
  },
  methods: {
    ...mapActions([]),
    //搜索组件
    searchComponent() {
      this.cardList = this.allList.filter((item) => {
        return item.unitName.indexOf(this.searchName) != -1;
      });
    },
    //已选tab选中
    doSelectClick() {
      this.tabActive = -1;
      this.cardList = this.allList.filter((item) => {
        return this.checkList.includes(item.unitName);
      });
    },
    //统计tab切换
    changeStatistic(item, idx) {
      this.tabActive = idx;
      this.cardList = this.allList;
      this.listUnit();
    },
    //各个大屏组件统计
    // async unitNumStatistic() {
    //   let res = await disasterPreventionApi.unitNumStatistic();
    //   if (res.code == 0) {
    //     this.statisticList = res.data;
    //   }
    // },
    //查询大屏组件信息
    async listUnit() {
      const oneRegExp = /[^数据.{2}$]/; // 高度1卡片正则表达式
      const oneList = ['台风']; // 高度1卡片数组
      const drillList = ['海塘', '台风']; // 能演练的卡片
      const mode = this.qiankunModule ? 2 : 1;
      const opt = { bsName: '', mode:mode };
      let res = await disasterPreventionApi.listUnit(opt);
      if (res.code == 0) {
        if (res.data.list) {
          this.statisticList[0].value = res.data.list.length
          res.data.list.map((item) => {
            if (oneList.includes(item.unitName) || item.unitName.match(oneRegExp)) {
              item.per = 'one';
            }
            if (drillList.includes(item.unitName)) {
              item.drill = true;
            }
          });
        }
        this.allList = res.data.list;
        this.allList.map((item) => {
          let imgSrc;
          try {
            imgSrc = require(`@/assets/images/component_card_img/${item.unitName}.png`);
          } catch (error) {
            console.log(error);
          }
          item.imgSrc = imgSrc || '';
        });
        this.cardList = this.allList;
      }
    },
    //选中组件卡片
    selectCardComponent(item) {
      let idx = this.checkList.indexOf(item);
      if (idx != -1) {
        this.checkList.splice(idx, 1);
      } else {
        this.checkList.push(item);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.fl {
  float: left;
}
.select-cards {
  width: 100%;
  height: 600px;
  margin-top: 16px;
  @include flexbox;
  @include flexflow(column, nowrap);
  .search-area {
    @include flexbox;
    @include flexAC;
    .input-box {
      border: 1px solid rgba(255, 255, 255, 0.4);
      border-radius: 4px;
      font-size: 14px;
    }
    .cards-tabs {
      @include flexbox;
      @include flexAC;
      .tab-item {
        padding: 14px 23px;
        margin-right: 12px;
        cursor: pointer;
        &.active {
          background: url(~assets/images/ScreenAsset/select-card-icon.png) no-repeat;
          background-size: 100% 100%;
        }
        p {
          font-size: 16px;
          color: #ffffff;
          letter-spacing: 0;
          text-align: center;
          line-height: 24px;
        }
      }
    }
  }
  .card-box {
    @include flex(1);
    overflow-y: auto;
    padding: 16px 0;
    .card-item-box {
      flex: 1;
      width: 300px;
      height: 260px;
      position: relative;
      margin: 0 16px 16px 0;
      &:nth-child(4n) {
        margin-right: 0;
      }
      .card-tag {
        position: absolute;
        right: 8px;
        top: 6px;
        z-index: 10;
      }
    }
    .card-item {
      width: 100%;
      height: 100%;
      position: relative;
      border-radius: 4px;
      opacity: 0.6;
      > i {
        position: absolute;
        right: 0;
        top: 0;
        z-index: 100;
      }
      img {
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        border-radius: 6px;
      }
      .card-title {
        width: 100%;
        height: 64px;
        background-image: linear-gradient(180deg, rgba(0, 0, 0, 0.27) 0%, #000000 50%);
        border-radius: 0 0 4px 6px;
        color: #fff;
        z-index: 1;
        position: absolute;
        bottom: 0;
        left: 0;
        font-size: 20px;
        letter-spacing: 0;
        line-height: 32px;
        @include flexbox;
        @include flexAC;
        @include flexJC(center);
      }
      &.active {
        opacity: 1;
        box-shadow: #00baff 0 0 6px 2px;
        .order-num {
          width: 64px;
          height: 64px;
          border-radius: 0 0 4px 4px;
          bottom: 0;
          position: absolute;
          left: 0;
          @include flexbox;
          @include flexAC;
          padding-left: 15px;
          box-sizing: border-box;
          font-size: 32px;
          color: #00baff;
          z-index: 2;
          background: url(~assets/images/ScreenAsset/select-num-icon.png) no-repeat;
          background-size: 100% 100%;
        }
      }

      &.high-height {
        height: 736px;
      }
    }
  }

  ::v-deep .el-checkbox-group {
    width: 100%;
    @include flexbox;
  }
}
</style>
