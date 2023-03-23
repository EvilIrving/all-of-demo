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
        </van-dropdown-menu>
        <van-field
          class="van-field"
          v-model="name"
          left-icon="search"
          placeholder="请输入工程名称"
          @blur="getList"
        />
      </div>
      <div class="table-wrap">
        <table-list :theadList="theadList" :list="list" @row-click="rowClick">
          <template v-slot:projectName="item">
            <span class="blue-color">{{ item.data.projectName }}</span>
          </template>
          <template v-slot:handlingStatus="item">
            <span>{{
              item.data.handlingStatus == 1 ? "待处理" : "已处理"
            }}</span>
          </template>
        </table-list>
      </div>
    </div>
  </view-page>
</template>

<script>
import TableList from "@/components/tableList";
import { filterParams } from "@/utils/utils";
import { supervisedCheck } from "@/api/projectDetail";
export default {
  name: "Index",
  props: {},
  components: { TableList },
  data() {
    return {
      typeValue: "",
      // 处理状态（未归档、归档）
      typeOptions: [
        { text: "全部", value: "" },
        { text: "待处理", value: 1 },
        { text: "已处理", value: 2 },
      ],
      name: "",
      theadList: [
        {
          label: "年份",
          prop: "year",
          slot: false,
        },
        {
          label: "工程名称",
          prop: "projectName",
          slot: true,
        },
        {
          label: "责任单位",
          prop: "responsibleUnitName",
          slot: false,
          width: '95px'
        },
        {
          label: "处理状态",
          prop: "handlingStatus",
          slot: true,
        },
      ],
      list: [],
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.list({
        projectName: this.name,
        year: this.year,
        handlingStatus: this.typeValue,
        pageNum: 1,
        pageSize: 9999,
        type: 3,
      });
      if (res.code == 0) {
        this.list = res.data.list.map((item) => filterParams(item));
        // 加载状态结束
        this.loading = false;
        this.finished = true;
      }
    },
    rowClick(data, index) {
      this.$router.push("/propertySupervision/scheduleDetail?id=" + data.id);
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
      width: 220px;
    }
  }
  .table-wrap {
    height: calc(100% - 120px);
  }
}
</style>