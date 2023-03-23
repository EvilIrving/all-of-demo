<template>
  <div class="progress_query">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="'进度查询'" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="top_box">
      <van-dropdown-menu z-index="2333" class="van-dropdown-menu" active-color="#276AF0">
        <van-dropdown-item title-class="title-class-name" v-model="statusValue" :options="statusOption" @change="search" />
      </van-dropdown-menu>
      <van-field class="van-field" v-model="dispatchName" left-icon="search" placeholder="请输入合同名称" @change="search" />
    </div>
    <div class="table_box" v-loading="loading" ref="map_box">
      <table-list :theadList="theadList" :list="list" :finished="finished" @onLoad="onLoad" @row-click="rowClick">
        <template v-slot:status="item">
          <span :class="{
              'blue-color': item.data.status == 3,
              'red-color': item.data.status == 2,
              'orange-color': item.data.status == 1,
            }">{{
              item.data.status == 1
                ? "审核中"
                : item.data.status == 2
                ? "已驳回"
                : "已通过"
            }}</span>
        </template>
      </table-list>
    </div>
  </div>
</template>

<script>
import { contractApi } from "@/api/rainfall.js";
import TableList from "@/components/tableList";
export default {
  name: "Index",
  props: {},
  components: { TableList },
  data() {
    return {
      statusValue: "",
      statusOption: [
        { text: "全部", value: "" },
        { text: "审批中", value: "1" },
        { text: "已通过", value: "3" },
        { text: "已驳回", value: "2" },
      ],
      dispatchName: "",
      theadList: [
        {
          label: "合同名称",
          prop: "contractName",
          slot: false,
        },
        {
          label: "审核状态",
          prop: "status",
          slot: true,
        },
      ],
      list: [],
      finished: false,
      loading: false,
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await contractApi.list({
        pageNum: this.pageNum,
        pageSize: 10,
        status: this.statusValue,
        propertyUnitName: this.dispatchName,
      });
      if (res.code == 0) {
        this.totalPages = res.data.totalPages;
        if (this.pageNum == 1) {
          this.list = res.data.list;
        } else {
          this.list = [...this.list, ...res.data.list];
        }
      }
      this.loading = false;
    },
    search() {
      this.loading = true;
      this.$refs.map_box.scrollTop = 0;
      this.pageNum = 1;
      this.getList();
    },
    onLoad() {
      if (this.totalPages == this.pageNum) {
        this.loading = false;
        this.finished = true;
        return false;
      }
      this.pageNum += 1;
      this.getList();
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      // setTimeout(() => {
      //   for (let i = 0; i < 10; i++) {
      //     this.list.push(this.list.length + 1);
      //   }
      //   // 加载状态结束
      //   this.loading = false;
      //   // 数据全部加载完成
      //   if (this.list.length >= 40) {
      //     this.finished = true;
      //   }
      // }, 1000);
    },
    rowClick(data, index) {
      this.$router.push("/acceptanceParticulars?id=" + data.id);
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
  .table_box {
    height: calc(100% - 120px);
  }
}
</style>