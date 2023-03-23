<!--
 * @Author: dyn
 * @Date: 2021-10-09 10:29:35
 * @LastEditTime: 2021-10-09 17:11:13
 * @LastEditors: Please set LastEditors
 * @Description: 经验库
 * @FilePath: \cangnan-app\src\views\projectDetail\diagnosis\components\Experience.vue
-->
<template>
  <div class="wrap">
    <div class="know_top">
      <div
        style="display: inline-block; padding: 8px 0 0 0"
        @click="showTypePicker = true"
      >
        {{ typeValueLabel ? typeValueLabel : "工程分类" }}
        <van-icon name="arrow-down" />
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
          <div class="know_right">
            <p class="txt">工程类型：{{ item.typeValue }}</p>
            <p class="txt">问题描述：{{ item.problemDescription }}</p>
            <p class="txt">解决方法：{{ item.solution }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { experienceApi } from "@/api/projectDetail";
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
          value: "1",
        },
        {
          label: "堤防",
          value: "5",
        },
        {
          label: "海塘",
          value: "2",
        },
        {
          label: "水闸",
          value: "4",
        },
        {
          label: "泵站",
          value: "6",
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
      this.typeValueLabel = this.typeOptions[index].label;
      this._loadPage();
      this.showTypePicker = false;
    },
    // 列表
    async _loadPage() {
      let res = await experienceApi.loadPage({
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