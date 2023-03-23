<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-02 15:38:36
 * @Description: 工程信息
 * @FilePath: \lucheng_project\src\views\projectDetail\baseMsg\ProjectMsg.vue
-->
<template>
  <div class="wrap">
    <div class="wrap_slide">
      <div class="menu_item" v-for="(item, index) in urlList" :key="item.name">
        <p class="first_title">
          <img :src="item.icon" alt="" />
          <span>{{ item.name }}</span>
        </p>
        <div
          v-for="(ite, idx) in item.children"
          :key="ite.name"
          @click="clickMenu(index, idx)"
          :class="['second_title', ite.select ? 'active' : '']"
        >
          {{ ite.name }}
        </div>
      </div>
      <div
        class="menu_item"
        v-for="(item, index) in urlListSecond"
        :key="item.name"
      >
        <p class="first_title">
          <img :src="item.icon" alt="" />
          <span>{{ item.name }}</span>
        </p>
        <div
          v-for="(ite, idx) in item.children"
          :key="ite.name"
          @click="clickMenuSecond(index, idx)"
          :class="['second_title', ite.select ? 'active' : '']"
        >
          {{ ite.name }}
        </div>
      </div>
    </div>
    <div class="wrap_right" v-show="!showBuilding">
      <iframe
        :src="iframeUrl"
        frameborder="0"
        width="100%"
        height="100%"
      ></iframe>
    </div>
    <div class="wrap_right" v-show="showBuilding">
      <div class="building_wrap">
        <div class="building">
          <div>
            <img src="@/assets/images/no-responsible.png" alt="" />
            <p>建设中...</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectMsg",
  props: {},
  components: {},
  data() {
    return {
      showBuilding: false,
      iframeUrl: "",
      menuIndex: 0,
      urlList: [
        {
          icon: require("../../../assets/images/htzt.png"),
          name: "海塘主体",
          children: [
            {
              name: "基础信息",
              select: true,
              path: "/mgt/lucheng/seawallct/form",
            },
            {
              name: "海塘主体",
              select: false,
              path: "/mgt/lucheng/sluicespage/mainbody",
            },
          ],
        },
      ],
      urlListSecond: [
        {
          icon: require("../../../assets/images/fsjz.png"),
          name: "附属设施",
          children: [
            {
              name: "水闸",
              select: false,
              path: "/mgt/lucheng/sluicespage/tabs?type=sluice",
            },
            {
              name: "泵站",
              select: false,
              path: "/mgt/lucheng/sluicespage/tabs?type=pump",
            },
            // {
            //   name: "管涵",
            //   select: false,
            //   path: "/mgt/lucheng/sluicespage/tabs?type=pipe",
            // },
            {
              name: "倒虹吸",
              select: false,
              path: "/mgt/lucheng/sluicespage/tabs?type=siphon",
            },
            {
              name: "管理房",
              select: false,
              path: "/mgt/bm/accbuild?proType=management&isJump=jump",
            },
             {
              name: "测站平台",
              select: false,
              path: "/mgt/bm/accbuild?proType=station&isJump=jump",
            },
          ],
        },
        {
          icon: require("../../../assets/images/jcjzw.png"),
          name: "交叉建筑物",
          children: [
            {
              name: "码头（渡口）",
              select: false,
              path: "/mgt/bm/accbuild?proType=ferry&isJump=jump",
            },
            {
              name: "桥梁",
              select: false,
              path: "/mgt/bm/accbuild?proType=bridge&isJump=jump",
            },
            {
              name: "海塘道路交叉出入口",
              select: false,
              path: "/mgt/bm/accbuild?proType=entrance&isJump=jump",
            },
            {
              name: "旱闸",
              select: false,
              // isBuilding: true,
              path: "/mgt/bm/accbuild?proType=drygate&isJump=jump",
            },
            {
              name: "箱涵",
              select: false,
              path: "/mgt/bm/accbuild?proType=boxculvert&isJump=jump",
            },
              {
              name: "规模以下水闸",
              select: false,
              path: "/mgt/bm/accbuild?proType=waterGate&isJump=jump",
            },
          ],
        },
      ],
    };
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail;
    },
  },
  mounted() {
    this.iframeUrl =
      this.$config.rscpHost +
      this.urlList[0].children[0].path +
      "?outsideToken=" +
      this.seawallDetail.outsideToken;
  },
  methods: {
    clickMenu(index, idx) {
      this.urlList.map((v) => {
        v.children.map((y) => {
          y.select = false;
        });
      });
      this.urlListSecond.map((v) => {
        v.children.map((y) => {
          y.select = false;
        });
      });
      this.urlList[index].children[idx].select = true;
      this.iframeUrl =
        this.$config.rscpHost +
        this.urlList[index].children[idx].path +
        "?outsideToken=" +
        this.seawallDetail.outsideToken;
      // this.menuIndex = index
    },
    clickMenuSecond(index, idx) {
      this.urlListSecond.map((v) => {
        v.children.map((y) => {
          y.select = false;
        });
      });
      this.urlList.map((v) => {
        v.children.map((y) => {
          y.select = false;
        });
      });
      this.urlListSecond[index].children[idx].select = true;
      if (this.urlListSecond[index].children[idx].isBuilding) {
        this.showBuilding = true;
      } else {
        this.showBuilding = false;
        this.iframeUrl =
          this.$config.rscpHost +
          this.urlListSecond[index].children[idx].path +
          "&outsideToken=" +
          this.seawallDetail.outsideToken;
        // this.menuIndex = index
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  height: 100%;
  @include flexbox();
  @include flexJC(space-between);
  .wrap_slide {
    width: 230px;
    margin-right: 20px;
    @include contentBox();
    padding: 16px 0;
    .menu_item {
      margin-bottom: 10px;
      .first_title {
        padding: 8px 16px;
        img {
          margin-right: 8px;
        }
        span {
          font-weight: 600;
          color: #333333;
          font-size: 18px;
        }
      }
      .second_title {
        font-weight: 500;
        color: #666666;
        font-size: 16px;
        padding: 13px 61px;
        cursor: pointer;
        &.active {
          background: #eaf5fe;
          color: #1492ff;
        }
      }
    }
  }
  .wrap_right {
    @include contentBox();
    width: calc(100% - 250px);
    height: 100%;
  }
}
</style>
