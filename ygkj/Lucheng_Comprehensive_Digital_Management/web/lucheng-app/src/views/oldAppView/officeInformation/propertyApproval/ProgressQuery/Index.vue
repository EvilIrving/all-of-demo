<template>
  <view-page>
    <div class="progress_query">
      <div class="top_box">
        <van-dropdown-menu
          z-index="2333"
          class="van-dropdown-menu"
          active-color="#276AF0"
        >
          <van-dropdown-item
            title-class="title-class-name"
            v-model="typeValue"
            :options="typeOptions"
            @change="getList"
          />
          <van-dropdown-item
            title-class="title-class-name"
            v-model="stateValue"
            :options="stateOptions"
            @change="getList"
          />
        </van-dropdown-menu>
        <van-field
          class="van-field"
          v-model="value3"
          left-icon="search"
          placeholder="请输入"
        />
      </div>
      <div class="table_box">
        <table-list
          :theadList="theadList"
          :list="list"
          :finished="finished"
          @onLoad="getList"
          @row-click="rowClick"
        >
          <template v-slot:hiddenDangerStatus="item">
            <span v-if="item.data.hiddenDangerStatus === 0">未处理</span>
            <span v-if="item.data.hiddenDangerStatus === 1">已处理</span>
          </template>
        </table-list>
      </div>
    </div>
  </view-page>
</template>

<script>
import TableList from "@/components/tableList";
import { propertyMt } from "@/api/appView";
export default {
  name: "Index",
  props: {},
  components: { TableList },
  data() {
    return {
      typeValue: "",
      typeOptions: [
        { text: "工程类型", value: "" },
        { text: "水库", value: "水库" },
        { text: "堤防", value: "堤防" },
        { text: "水闸", value: "水闸" },
        { text: "海塘", value: "海塘" },
      ],
      stateValue: "",
      stateOptions: [
        { text: "处理状态", value: "" },
        { text: "已处理", value: 1 },
        { text: "未处理", value: 0 },
      ],
      value3: "",
      theadList: [
        {
          label: "工程名称",
          prop: "projectName",
          slot: false,
          width: "100px",
        },
        {
          label: "工程类型",
          prop: "projectType",
          slot: false,
        },
        {
          label: "处理状态",
          prop: "hiddenDangerStatus",
          slot: true,
        },
        // {
        //   label: "时间",
        //   prop: "tm",
        //   slot: false,
        // },
      ],
      list: [],
      finished: false,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    rowClick(data, index) {
      console.log(data, index);
      this.$router.push("/progressDetail?id=" + data.id);
    },
    async getList() {
      let res = await propertyMt.DailyTask({
        //year:this.
        projectType: this.typeValue,
        hiddenDangerStatus: this.stateValue,
      });
      if (res.code === 0) {
        this.list = res.data.list;
        this.loading = false;
        this.finished = true;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.progress_query {
  width: 100%;
  height: 100%;
  .top_box {
    height: 43px;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    .van-dropdown-menu {
      flex: 1;
    }
    .van-field {
      width: 155px;
    }
  }
  .table_box {
    height: calc(100% - 120px);
  }
}
</style>