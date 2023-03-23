<!--
 * @Author: zg
 * @LastEditTime: 2021-10-07 10:17:40
 * @Description: 工程详情-警
 * @FilePath: /cangnan-app/src/views/projectDetail/warn/Warn.vue
-->
<template>
  <div class="wrap">
    <div class="detail-menu">
      <div
        :class="['detail-menu-item', menuIndex === index ? 'active' : '']"
        v-for="(item, index) in menuTab"
        :key="index"
        @click="clickMenu(index)"
      >
        {{ item.title }}
      </div>
    </div>
    <div class="warn_content">
      <!-- 综合码 -->
      <div class="warn_main" v-if="menuIndex == 0">
        <div class="qrcode">
          <img v-if="!codeSrc1" src="@/assets/images/qrcode-green.png" alt="" />
          <img v-else :src="codeSrc1" alt="" />
        </div>
        <div class="pro_list">
          <div>
            <span><img src="@/assets/images/dazs.png" alt="" />档案问题：</span
            >{{ proList1 }}
          </div>
          <div>
            <span
              ><img
                src="@/assets/images/health_xt.png"
                alt=""
              />健康问题：</span
            >{{ proList2 }}
          </div>
          <div>
            <span
              ><img src="@/assets/images/warn_jg.png" alt="" />报警问题：</span
            >{{ proList3 }}
          </div>
        </div>
      </div>
      <!-- 档案码 -->
      <div class="warn_main" v-if="menuIndex == 1">
        <div class="qrcode">
          <img v-if="!codeSrc2" src="@/assets/images/qrcode-green.png" alt="" />
          <img v-else :src="codeSrc2" alt="" />
        </div>
        <div class="pro_lists">
          {{ proList1 }}
        </div>
      </div>
      <!-- 问题码 -->
      <div class="warn_main" v-if="menuIndex == 2">
        <div class="qrcode">
          <img v-if="!codeSrc3" src="@/assets/images/qrcode-green.png" alt="" />
          <img v-else :src="codeSrc3" alt="" />
        </div>
        <div class="pro_lists">
          {{ proList2 }}
        </div>
      </div>
      <!-- 报警码 -->
      <div class="warn_main" v-if="menuIndex == 3">
        <div class="qrcode">
          <img v-if="!codeSrc4" src="@/assets/images/qrcode-green.png" alt="" />
          <img v-else :src="codeSrc4" alt="" />
        </div>
        <div class="pro_lists">
          {{ proList3 }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { healthCodeApi } from "@/api/projectDetail";
export default {
  name: "Warn",
  props: {},
  components: {},
  data() {
    return {
      menuIndex: 0,
      menuTab: [
        {
          title: "综合码",
          type: 4,
        },
        {
          title: "档案码",
          type: 1,
        },
        {
          title: "问题码",
          type: 2,
        },
        {
          title: "报警码",
          type: 3,
        },
      ],
      codeSrc1: "",
      codeSrc2: "",
      codeSrc3: "",
      codeSrc4: "",
      proList1: "",
      proList2: "",
      proList3: "",
    };
  },
  mounted() {
    //console.log(this.$localData("get", "projectCode"));
    this.getCodeSrc();
  },
  methods: {
    clickMenu(index) {
      this.menuIndex = index;
    },

    getCodeSrc() {
      this.menuTab.map((item) => {
        this._healthCodeProList(item.type);
        if (item.type != 4) {
          this._healthProList(item.type);
        }
      });
    },

    // 获取健康码数据
    async _healthCodeProList(type) {
      let res = await healthCodeApi.healthCodeProList({
        codeType: type, // 1档案码,2健康码,3报警码 4综合码
        projectId: this.$localData("get", "projectCode"),
      });
      if (res.code == 0) {
        let codeArr = res.data.list;
        if (codeArr.length > 0) {
          if (type == 4) {
            this.codeSrc1 = codeArr[0].codeUrl;
          } else if (type == 1) {
            this.codeSrc2 = codeArr[0].codeUrl;
          } else if (type == 2) {
            this.codeSrc3 = codeArr[0].codeUrl;
          } else if (type == 3) {
            this.codeSrc4 = codeArr[0].codeUrl;
          }
        }
      }
    },

    // 获取问题列表
    async _healthProList(type) {
      let res = await healthCodeApi.selectqs({
        codeType: type, // 1档案码,2问题码,3报警码
        projectId: this.$localData("get", "projectCode"),
      });
      if (res.code == 0) {
        let proArr = res.data.list;
        let proStr = "";
        if (proArr.length > 0) {
          for (var i = 0; i < proArr.length; i++) {
            if (i == proArr.length - 1) {
              proStr += proArr[i].question + "。";
            } else {
              proStr += proArr[i].question + ";";
            }
          }
          if (type == 1) {
            this.proList1 = proStr;
          } else if (type == 2) {
            this.proList2 = proStr;
          } else if (type == 3) {
            this.proList3 = proStr;
          }
        }
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  // background: #eee;
  // height: 800px;
  width: 100%;
  .detail-menu {
    @include flexbox();
    overflow-x: auto;
    overflow-y: hidden;
    padding: 12px 24px;
    box-sizing: border-box;
    &::-webkit-scrollbar {
      display: none;
    }
    .detail-menu-item {
      white-space: nowrap;
      min-width: 64px;
      padding: 0 8px;
      height: 32px;
      font-size: 16px;
      color: #1e1e1f;
      text-align: center;
      line-height: 32px;
      font-weight: 500;
      background: #fbfbfb;
      border: 1px solid #d6d6d6;
      border-radius: 4px;
      margin-right: 16px;
      &.active {
        background: #036eff;
        color: #fff;
      }
      &:last-child {
        margin-right: 0;
      }
    }
  }
  .warn_content {
    box-sizing: border-box;
    width: 100%;
    padding: 0 16px 16px;
    .warn_main {
      .qrcode {
        text-align: center;
        padding: 16px 0;
        img {
          width: 120px;
          height: 120px;
        }
      }
      .pro_list {
        div {
          color: #000;
          line-height: 28px;
          padding: 5px 0;
          border-bottom: 1px solid #f6f6f6;
          span {
            img {
              width: 23px;
              height: 22px;
              vertical-align: middle;
              margin: -3px 10px 0 0;
            }
          }
        }
      }
      .pro_lists {
        color: #000;
        line-height: 28px;
      }
    }
  }
}
</style>