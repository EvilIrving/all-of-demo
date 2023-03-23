<!--
 * @Author: hanyu
 * @LastEditTime: 2022-08-22 19:16:28
 * @Description: SolveDangerous
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\projectDetail\SolveDangerous.vue
-->
<template>
  <div class='wrap'>
    <section class="page_top" v-loading='loading'>
      <div class="top_left">
        <h3>最新一次鉴定结论：</h3>
        <img v-if="detailData.SA_APP_RST === '一类坝' " :src="$config.imgUrl+ '1lei.png'" alt="">
        <img v-if="detailData.SA_APP_RST === '二类坝'" :src="$config.imgUrl+ '2lei.png'" alt="">
        <img v-if="detailData.SA_APP_RST === '三类坝'" :src="$config.imgUrl+ '3lei.png'" alt="">
      </div>
      <div class="top_right">
        <div class="msg_top" v-if="isEmpty">
          <p>除险加固时间：{{detailData.R_REF_ST_TM?detailData.R_REF_ST_TM:'-'}}</p>
          <p>完工时间：{{detailData.R_REF_COM_TM?detailData.R_REF_COM_TM:'-'}}</p>
          <p>蓄水验收时间：{{detailData.R_REF_WATER_TM?detailData.R_REF_WATER_TM:'-'}}</p>
          <p>竣工验收时间：{{detailData.R_REF_FIN_TM?detailData.R_REF_FIN_TM:'-'}}</p>
        </div>
        <div class="msg_bottom" v-if="isEmpty">
          <h3>主要建设内容</h3>
          <p>{{detailData.MCONT?detailData.MCONT:'-'}}</p>
        </div>
        <img v-if="!isEmpty" class="" :src="$config.imgUrl+ 'nocxjg.png'" alt="">
        <span class="nocxjg" v-if="!isEmpty">无需除险加固</span>
      </div>
    </section>

    <section class="page_bottom">
      <p class="small_title">相关照片</p>
      <div v-if="false" class="img_list">
        <img src="@/assets/images/htgs-bg.png" alt="">
        <img src="@/assets/images/htgs-bg.png" alt="">
        <img src="@/assets/images/htgs-bg.png" alt="">
        <img src="@/assets/images/htgs-bg.png" alt="">
        <img src="@/assets/images/htgs-bg.png" alt="">
        <img src="@/assets/images/htgs-bg.png" alt="">
      </div>
      <div v-else class="img_no">
        <img class="" :src="$config.imgUrl+ 'norelate.png'" alt="">
        <span class="text">暂无相关照片</span>
      </div>
    </section>
  </div>
</template>

<script>
import { cxjgTable, } from "@/api/api_reservoir";
export default {
  name: 'SolveDangerous',
  props: {
    reservoirDetail: {
      type: Object,

    }
  },
  components: {

  },
  data() {
    return {
      detailData: {},
      loading: false
    };
  },
  computed: {
    isEmpty() {
      let result = Object.keys(this.detailData).length === 0 || Object.values(this.detailData).every((item) => item == null || item == "");
      return result
    },
  },
  mounted() {
    this.getCxjgTable(this.reservoirDetail.prcd)
  },
  methods: {
    async getCxjgTable(prcd) {
      this.loading = true
      let res = await cxjgTable({ prcd: prcd })
      if (res.success) {
        this.loading = false
        this.detailData = res.data[0]
      }
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  height: calc(100% - 60px);
  width: 100%;
  .small_title {
    font-size: 20px;
    font-weight: 500;
    color: #ffffff;
    background: url($imageUrl + "small_title.png") no-repeat
      left 25px;
    background-size: 8px 8px;
    padding: 17px 0 10px 20px;
    position: relative;
        &:before {
         top: 22px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
  }
  .page_top {
    @include flexbox();
    @include flexJC(space-between);
    width: 100%;
    .top_left {
      text-align: center;
      width: 156px;
      height: 184px;
      background: #03338d;
      border: 1px solid #0051b5;
      box-sizing: border-box;
      h3 {
        font-size: 14px;
        color: #ffffff;
        margin: 20px 0 35px 0;
      }
      img {
        width: 80px;
        height: 71px;
      }
    }
    .top_right {
      width: calc(100% - 164px);
      position: relative;
      background: #03338d;
      border: 1px solid #0051b5;
      .msg_top {
        @include flexbox();
        @include flexJC(space-between);
        @include flexflow(wrap row);
        p {
          width: calc(50% - 4px);
          box-sizing: border-box;
          background: #03338d;
          border: 1px solid #0051b5;
          height: 40px;
          line-height: 40px;
          padding: 0 10px;
          margin-bottom: 8px;
        }
      }
      .msg_bottom {
        height: 88px;
        box-sizing: border-box;
        background: #03338d;
        border: 1px solid #0051b5;
        padding: 0 8px;
        overflow: auto;
        h3 {
          line-height: 22px;
          margin-top: 4px;
        }
        p {
          line-height: 20px;
        }
      }
      img {
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
        width: 80px;
      }
      .nocxjg {
        width: 90px;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 20px;
        margin: auto;
      }
    }
  }
  .page_bottom {
    .img_list {
      @include flexbox();
      @include flexflow(wrap row);
      @include flexJC(space-between);
      img {
        width: calc(33.33% - 4px);
        height: 120px;
        margin-bottom: 8px;
      }
    }
    .img_no {
      height: 200px;
      position: relative;
      img {
        width: 80px;
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
      }
      .text {
        width: 90px;
        position: absolute;
        left: 0;
        right: 0;
        bottom: 20px;
        margin: auto;
      }
    }
  }
}
</style>