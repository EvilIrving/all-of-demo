<!--
 * @Author: wqn
 * @Date: 2023-02-14 11:42:01
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-15 13:33:19
 * @Description: 沿塘管控
-->
<template>
  <ModuleLayout title="沿塘管控" disabled>
    <div class='seawall-control'>
      <!-- <p class="lg-title">口门管控</p> -->
      <section class="control-list">
        <div class="control-item" v-for="(item, index) in twoList" :key="index">
          <div class="name">{{ item.name }}</div>
          <div>
            总数<span>{{ item.total }}</span>处
          </div>
          <div>
            关闭<span>{{ item.close  }}</span>处
          </div>
        </div>
      </section>
    </div>
  </ModuleLayout>
</template>

<script>
import { zhSeawall } from "@/api/api_seawall";
export default {
  name: "SeawallControl",
  props: {},
  components: {},
  data() {
    return {
      twoList: [
        {
          name: "旱闸",
          total: 0,
          close: 0,
        },
        {
          name: "缺口",
          total: 0,
          close: 0,
        },
        {
          name: "道口",
          total: 0,
          close: 0,
        },
      ],
    };
  },
  computed: {
    selectAdcds() {
      return this.$store.state.selectAdcd;
    },
  },
  watch: {
    selectAdcds() {
      this.getKouMenData()
    },
  },
  created() { },
  mounted() {
    this.getKouMenData()
   },
  destroyed() { },
  methods: {
    async getKouMenData() {
      let res = await zhSeawall.seawallDoor({
        adcd: this.selectAdcds
      });
      if (res.success) {
        this.twoList[0].total = res.data[0].dryGate;
        this.twoList[0].close = res.data[0].dryCloseNum || 0;
        this.twoList[1].total = res.data[0].gapNum;
        this.twoList[1].close = res.data[0].gapCloseNum || 0;
        this.twoList[2].total = res.data[0].crossNum;
        this.twoList[2].close = res.data[0].crossCloseNum || 0;
      }
    },
  },
};
</script>
<style lang='scss' scoped>
.seawall-control {
  width: 96%;
  margin: 0 auto;
  height: 100%;
  p.lg-title {
    width: 30%;
    height: 31px;
    line-height: 30px;
    background: url($ypImageUrl + "KmControl-bg.png") no-repeat center;
    background-size: 100% 100%;
    text-align: center;
    margin: 6px 10px;
  }
  .control-list {
    width: 96%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    .control-item {
      background: url($ypImageUrl + "KmControlitem-bg.png") no-repeat center;
      background-size: cover;
      padding: 6px 10px;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(space-around);
      > div {
        display: inline-block;
        color: #b1dcff;
        font-size: 18px;
        text-align: center;
        font-family: "fzzyjw";
        span {
          font-size: 28px;
          color: #ffffff;
          padding: 0 8px 0 13px;
          font-family: "yahei";
        }
        &:nth-child(1) {
          flex: 1;
          font-family: "youshebiaotihei";
          line-height: 24px;
          color: #48d2ff;
          font-size: 20px;
        }
        &:nth-child(2) {
          flex: 1.5;
          text-align: center;
          border-right: 1px solid #546f9f;
        }
        &:nth-child(3) {
          flex: 1.5;
          text-align: center;
        }
      }
      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>