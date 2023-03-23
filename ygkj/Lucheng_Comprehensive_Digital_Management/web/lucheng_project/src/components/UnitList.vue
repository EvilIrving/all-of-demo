<template>
  <div class="unit-list-box">
    <div class="item_box_title">
      <p>{{ title }}</p>
    </div>
    <div class="unit-list">
      <!-- <div class="search-box">
        <el-input
          v-model="unitName"
          size="small"
          placeholder="请输入单位名称"
        ></el-input>
      </div> -->
      <ul class="list-box">
        <li
          :class="{ active: activeId == item.id }"
          v-for="item in unitList"
          :key="item.id"
          @click="checkItem(item)"
        >
          {{ item.companyName }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { propertyUnitApi } from "@/api/propertyManage";
export default {
  props: {
    title: {
      type: String,
      default: "单位列表",
    },
  },
  data() {
    return {
      unitName: "",
      unitList: [],
      activeId: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await propertyUnitApi.list({
        companyName: this.unitName,
        pageNum: 1,
        pageSize: 999999,
      });
      if (res.code == 0) {
        this.unitList = res.data.list;
        if (this.unitList.length > 0) {
          this.activeId = this.unitList[0].id;
          this.$emit("checkItem", this.unitList[0]);
        }
      }
    },
    checkItem(item) {
      if (this.activeId == item.id) {
        return false;
      }
      this.activeId = item.id;
      this.$emit("checkItem", item);
    },
  },
};
</script>

<style lang="scss" scoped>
.unit-list-box {
  @include contentBox();
  height: 100%;
  .unit-list {
    padding: 15px 0;
    .search-box {
      padding: 0 15px 15px;
    }
    .list-box {
      max-height: 65vh;
      min-height: 55vh;
      overflow: auto;
      padding: 0 15px;
      li {
        padding: 10px;
        line-height: 18px;
        font-size: 14px;
        cursor: pointer;
        &.active {
          background: rgba(20, 146, 255, 0.2);
          color: rgba(39, 106, 240, 1);
        }
      }
    }
  }
}
</style>