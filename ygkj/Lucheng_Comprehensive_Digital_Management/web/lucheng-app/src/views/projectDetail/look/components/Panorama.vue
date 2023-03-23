<template>
  <my-table
    :data="tableData"
    :col-configs="colConfigs">
  </my-table>
</template>
<script>

const PrefixPlusText = {
  props: ['colConfig'],
  template: `
    <el-table-column :label="colConfig.label">
      <span :slot-scope="{ row }">
        {{ parseInt(row[colConfig.prop]) > 0 ? '+' + row[colConfig.prop] : row[colConfig.prop] }}
      </span>
    </el-table-column>
  `
}

export default {
  data () {
    this.colConfigs = [
      { prop: 'change', label: '变化', component: PrefixPlusText },
      { prop: 'name', label: '趋势', component: PrefixPlusText },
    ]
    return {
      tableData: [{
        change: '12%',
        trend: '10%'
      }, {
        change: '-12%',
        trend: '-10%'
      }]
    }
  }
}
</script>