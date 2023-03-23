<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-13 16:36:00
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\components\VantTable.vue
-->
<template>
  <div class="vant-table">
    <table cellspacing="0" :style="bgcolor" style="width: 100%" class="table">
      <tr>
        <th class="th" v-for="(item, index) in option.column" :key="index">
          {{ item.label }}
        </th>
      </tr>
      <tr v-for="(item, index) in tableData" :key="index" class="list-tr">
        <td v-for="(context, i) in option.column" :key="i">
          <span
            v-if="context.prop == 'name'"
            :style="
              item.status == '0' ? 'color: #276af0' : 'color: #FF0000'
            "
            @click.stop="skip()"
            >{{ item[context.prop] }}</span
          >
          <span v-else>{{ i == 0 ? index + 1 : item[context.prop] }}</span>
        </td>
      </tr>
    </table>
  </div>
</template>
<script>
export default {
  props: {
    bgcolor: Object,
    tableData: Array,
    option: Object,
  },
  created() {},
  mounted() {},
  methods: {
    skip() {
    },
  },
}
</script>

<style lang="scss" scoped>
.vant-table {
  .table {
    border-radius: 0.185185rem;
    .th {
      height: 1.074074rem;
      line-height: 1.074074rem;
      background-color: #ddecff;
      text-align: center;
      //   border-top-left-radius: .185185rem;
      //   border-top-right-radius: .185185rem;
    }
    .list-tr {
      height: 1.074074rem;
      line-height: 1.074074rem;
      background-color: #f6faff;
      text-align: center;
    }
    .list-tr:nth-child(2n) {
      background-color: #ffffff;
    }
  }
}
</style>