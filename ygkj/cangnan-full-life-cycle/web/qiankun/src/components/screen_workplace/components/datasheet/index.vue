<template>
  <div class="workplace-collect">
    <div class="workplace-collect__content">
      <div class="left-area">
        <SingleSelectTree @nodeClick="nodeClick"></SingleSelectTree>
      </div>
      <div class="right-area">
        <DataTable ref="dataTable" />
      </div>
    </div>
  </div>
</template>

<script>
import DataTable from "./pages/DataTable.vue";
import SingleSelectTree from "./components/SingleSelectTree.vue";
export default {
  name: 'DataSheet',
  data() {
    return {

    }
  },
  components: {
    DataTable,
    SingleSelectTree
  },
  methods: {
    nodeClick(data) {
      this.$refs.dataTable.tableId = data.id;
      this.$refs.dataTable.tableName = data.categoryName;
    }
  }
}
</script>

<style lang="scss" scoped>
.workplace-collect {
  width: 100%;
  height: 620px;
  .workplace-collect__content {
    width: 100%;
    height: 100%;
    @include flexbox;
    @include flexflow(row, nowrap);
    .left-area {
      width: 250px;
      height: 95%;
    }
    .right-area {
      width: calc(100% - 250px);
      height: 100%;
      padding-left: 20px;
      ::v-deep .vxe-table--render-default {
        .vxe-table--fixed-right-wrapper {
          &.scrolling--middle {
            box-shadow: -4px -2px 7px 0px rgb(0 0 0 / 12%);
          }
        }
      }
    }
  }
}
</style>