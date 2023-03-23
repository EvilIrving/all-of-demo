<!--
 * @Author: wqn
 * @Date: 2023-02-14 14:55:24
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-17 09:57:18
 * @Description: 行政区划&流域选择器
-->
<template>
  <div class="admn-basin-select" v-clickOutside="closeOptions">
    <div class="checked" @click="openOptionsShow">
      <i class="icon-content-icon-dingwei"></i>
      <span :title="checked.name">{{ checked.name }}</span>
      <i class="icon-content-ibtn-xiala" :class="{ rotate: openOptions }"></i>
    </div>
    <div v-show="openOptions" class="option-box">
      <ul>
        <li v-for="item in adcdList" :key="item.value" @click="checkedOption(item)" @mouseenter="mouseOption(item)" :class="{
            active: active === item.value || checked.code === item.value,
            selectActive: checked.code === item.value,
          }">
          {{ item.label }}
        </li>
      </ul>
      <ul v-show="openChildShow">
        <li v-for="item in adcdChildList" :key="item.value" @click="checkedOption(item)" :class="{
            active: childActive === item.value || checked.code === item.value,
            selectActive: checked.code === item.value,
          }">
          {{ item.label }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>



// import GeoJSON from "ol/format/GeoJSON";
// import cutZjsGeoJson from "geoJson/cutZjsGeoJson.json";
// import zjsGeoJson from "geoJson/zjsGeoJson.json";
import { getAdcdTree } from "@/api/api_utils";
export default {
  name: "AdmnBasinSelect",
  props: {
    value: {
      type: Object,
      default: () => { },
    },
    projectCategory: {
      type: String,
      default: '海塘',
    }
  },
  data() {
    return {
      checked: {
        name: "",
        code: "",
      },
      options: [],
      openOptions: false,
      openChildShow: false,
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      adcdList: [],
      adcdChildList: [],
      active: '',
      childActive: "",
    };
  },
  computed: {
    selectAdcds() {
      return this.$store.state.selectAdcd;
    },
  },
  watch: {
    selectAdcds(val) {
      if (val === this.userAdcd) {
        this.checked = {
          code: this.userAdcd,
          name: this.userAdnm,
        };
      }
    },
  },
  mounted() {
    this.checked = {
      code: this.userAdcd,
      name: this.userAdnm,
    };
    this.getAdcdOptions();
  },
  methods: {
    // 行政区划
    async getAdcdOptions() {
      let res = await getAdcdTree({
        adcd: this.userAdcd,
        fetchAll: true,
        projectCategory: this.projectCategory
      });
      if (res.success) {
        this.isDisabled = false;
        let adcdAllName = "";
        if (this.userAdcd.endsWith("0000000000")) {
          adcdAllName = "浙江省";
        } else if (this.userAdcd.endsWith("00000000")) {
          adcdAllName = "全市";
        } else {
          adcdAllName = "全区/县";
        }
        let temp = [
          {
            label: adcdAllName,
            value: this.userAdcd,
            checked: true,
          },
        ];
        this.adcdList = [...temp, ...res.adcdTree.children];
      }
    },
    //关闭行政区划选择框
    closeOptions() {
      this.openOptions = false
    },
    //打开or关闭行政区划选择框
    openOptionsShow() {
      this.openOptions = !this.openOptions;
      if (this.checked.code && this.openOptions) {
        const index = this.adcdList.findIndex(
          (item) => item.value === this.checked.code
        );
        if (index != -1) {
          this.openChildShow = false;
          this.active = "";
        } else {
          for (let i = 1; i < this.adcdList.length; i++) {
            const item = this.adcdList[i];
            const index = item.children.findIndex(
              (ite) => ite.value === this.checked.code
            );
            if (index != -1) {
              this.mouseOption(item);
              break;
            }
          }
        }
      }
    },
    //确定行政区划
    checkedOption(item) {
      if (item.children) {
        this.openChildShow = false;
      }
      this.checked = {
        code: item.value,
        name: item.label,
      };
      this.$store.commit("setSelectAdnm", this.checked.name);
      this.$store.commit("setSelectAdcd", this.checked.code);
      this.closeOptions();
    },
    //选择行政区划
    mouseOption(item) {
      if (item.children) {
        this.active = item.value;
        this.adcdChildList = item.children.filter(
          (ite) => ite.label !== "江干区"
        );
        this.openChildShow = true;
      } else {
        this.active = "";
        this.openChildShow = false;
      }
    },
  },
};
</script>
<style lang='scss' scoped>
.admn-basin-select {
  position: relative;
  width: 125px;
  height: 48px;
  background-image: url("./img/select-bg.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
  .checked {
    display: flex;
    align-items: center;
    padding: 0 10px;
    height: 48px;
    > i {
      display: inline-block;
      color: #228ae2;
      &.icon-content-ibtn-xiala {
        font-size: 12px;
        transition: transform 0.2s linear;
        &.rotate {
          transform: rotateZ(-180deg);
        }
      }
    }
    > span {
      display: inline-block;
      width: 80px;
      opacity: 0.75;
      font-size: 15px;
      padding: 0 5px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
  }
  .option-box {
    position: absolute;
    left: 2px;
    top: 55px;
    border-radius: 4px;
    display: flex;
    ul {
      padding: 10px 0;
      width: 125px;
      margin-right: 2px;
      background-color: rgba(7, 47, 108, 0.75);
      overflow: auto;
      li {
        color: #b1dcff;
        width: 100%;
        text-align: center;
        font-size: 14px;
        line-height: 20px;
        padding: 6px 0;
        &:not(:hover).active {
          color: #00b9fe;
        }
        &.selectActive,
        &:hover {
          background-image: linear-gradient(
              180deg,
              rgba($color: #2f7cfd, $alpha: 0.5) 0%,
              rgba($color: #0a4185, $alpha: 0.5) 100%
            ),
            linear-gradient(
              rgba($color: #072f6c, $alpha: 0.5),
              rgba($color: #072f6c, $alpha: 0.5)
            );
          background-blend-mode: normal, normal;
          color: #00b9fe;
        }
      }
    }
  }
}
</style>