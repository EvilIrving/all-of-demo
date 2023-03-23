<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-23 18:54:50
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\PageGreatDanger.vue
-->
<template>
  <div class="page-patrol">
    <v-table :tableData="tableData" :operation="true" :dynamicColumnSetting="dynamicColumnSetting">
      <template v-slot:name="row">
        <span>{{ row.data.name }}</span>
      </template>
      <template slot="operation">
        <span>123456</span>
      </template>
    </v-table>
    <div class="bottom__pagination">
      <el-pagination
        :current-page.sync="pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="totalPage"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { getSeawallPageGreatDanger } from '@/api/seawall.js';
import VTable from '../../../../table/VTable.vue';
export default {
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dynamicColumnSetting: [
        {
          prop: 'date',
          label: '时间'
        },
        {
          prop: 'name',
          label: '姓名',
          slot: true
        },
        {
          prop: 'address',
          label: '地址'
        }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1
    };
  },
  components: {
    VTable
  },
  mounted() {
    this.getPagePatrol();
  },
  methods: {
    async getPagePatrol() {
      let res = await getSeawallPageGreatDanger({
        projectCode: this.code,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      });
      console.log(res, '12312312');
    },
    onSizeChange() {},
    onCurrentChange() {}
  }
};
</script>

<style lang="scss" scoped>
.page-patrol {
  .bottom__pagination {
    padding-top: 10px;
    text-align: right;
  }
}
</style>
