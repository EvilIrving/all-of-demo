<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-02 13:49:34
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\SluiceDialogForSchedule\components\components\VTable.vue
-->
<template>
  <div class="table-box" ref="tableDom">
    <el-table :data="tableData" style="width: 100%" :row-class-name="rowClassName" @row-click="rowClick">
      <el-table-column type="index" label="序号" width="55" />
      <el-table-column
        v-for="column in dynamicColumnSetting"
        show-overflow-tooltip
        :label="column.label"
        :key="column.prop"
        :prop="column.prop"
      >
        <template slot-scope="scope">
          <span v-if="!column.slot">
            {{ scope.row[column.prop] ? scope.row[column.prop] : '-' }}
          </span>
          <slot v-else :name="column.prop" :data="scope.row"></slot>
        </template>
      </el-table-column>
      <el-table-column v-if="operation" label="操作">
        <template slot-scope="scope">
          <slot name="operation" :data="scope.row"></slot>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    dynamicColumnSetting: {
      type: Array,
    },
    tableData: {
      type: Array,
    },
    operation: {
      type: Boolean,
      default: false
    },
  },
  methods: {
    sortChange() {},
     rowClick(row) {
      this.$emit('rowClick', row);
    },
    rowClassName({ row, rowIndex }) {
      return rowIndex % 2 == 1 ? 'table-warm-row' : '';
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
<style lang='scss'>
.table-box .el-table .el-table__row:hover {
  background: url(~assets/images/WaterDisastersImage/seawall-table-row-icon.png) no-repeat;
  background-size: 100% 100%;
}
.table-box .el-table .el-table__row {
  box-sizing: border-box;
  height: 44px;
}
.table-box .el-table .el-table__row.table-warm-row {
  height: 44px;
  background: url(~assets/images/WaterDisastersImage/seawall-table-row-icon.png) no-repeat;
  background-size: 100% 100%;
}
</style>
