<!--
 * @Author: zg
 * @LastEditTime: 2021-10-07 10:17:40
 * @Description: 工程详情-知
 * @FilePath: /cangnan-app/src/views/projectDetail/know/Know.vue
-->
<template>
  <div class="wrap">
    <div class="know_top">
      <div
        style="display: inline-block; padding: 8px 0 0 0"
        @click="showTypePicker = true"
      >
        {{ typeValue ? typeValue : "工程分类" }} <van-icon name="arrow-down" />
      </div>
      <van-popup v-model="showTypePicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="typeColumns"
          @confirm="onConfirmType"
          @cancel="showTypePicker = false"
        />
      </van-popup>
    </div>

    <div class="know_content">
      <p class="section_title">列表</p>
      <div class="know_list">
        <div class="know_list_item" v-for="item in knowList" :key="item.id">
          <div class="know_left">
            <img src="../../../assets/images/know_file.png" alt="" />
          </div>
          <div class="know_right">
            <p class="title">{{ item.canonicalName }}</p>
            <p class="txt">工程类型：{{ item.typeValue }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { knowApi } from "@/api/projectDetail";
export default {
  name: "Know",
  props: {},
  components: {},
  data() {
    return {
      typeValue: "",
      showTypePicker: false,
      typeColumns: ["全部", "水库", "堤防", "海塘", "水闸", "泵站"],
      typeOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "水库",
          value: "水库",
        },
        {
          label: "堤防",
          value: "堤防",
        },
        {
          label: "海塘",
          value: "海塘",
        },
        {
          label: "水闸",
          value: "水闸",
        },
        {
          label: "泵站",
          value: "泵站",
        },
      ],
      knowList: [],
    };
  },
  mounted() {
    //console.log(this.$localData("get", "projectCode"));
    this._loadPage();
  },
  methods: {
    //工程类型
    onConfirmType(value, index) {
      // console.log(index);
      this.typeValue = this.typeOptions[index].value;
      this._loadPage();
      this.showTypePicker = false;
    },
    // 列表
    async _loadPage() {
      let res = await knowApi.getKnowList({
        projectType: this.typeValue,
      });
      if (res.code == 0) {
        this.knowList = res.data.list;
        let obj = {
          1: "水库",
          2: "海塘",
          3: "闸站",
          4: "水闸",
          5: "堤防",
          6: "泵站",
          7: "山塘",
          8: "水电站",
          9: "农饮水",
          10: "灌区",
        };

        this.knowList.map((item) => {
          item.typeValue = obj[item.projectType];
        });
      }
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  // background: #eee;
  // height: 800px;
  box-sizing: border-box;
  width: 100%;
  padding: 0 16px 16px;

  .know_content {
    padding: 0 8px;
    .section_title {
      font-size: 18px;
      font-weight: 500;
      color: #000;
      line-height: 32px;
      margin-top: 12px;
    }
    .know_list {
      .know_list_item {
        @include flexbox();
        @include flexAI(center);
        padding: 16px 0;
        border-bottom: 1px solid #e5e5e5;
        .know_left {
          margin-right: 20px;
        }
        .know_right {
          p.title {
            font-size: 18px;
            color: #000;
            font-weight: 500;
            line-height: 32px;
            word-break: break-all;
          }
          p.txt {
            font-size: 16px;
            color: #868686;
            line-height: 24px;
            word-break: break-all;
          }
        }
      }
    }
  }
}
</style>