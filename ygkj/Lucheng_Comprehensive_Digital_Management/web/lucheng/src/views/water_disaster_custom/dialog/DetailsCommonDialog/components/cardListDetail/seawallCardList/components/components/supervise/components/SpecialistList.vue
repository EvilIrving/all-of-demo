<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-13 11:45:29
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\seawallCardList\components\components\supervise\components\SpecialistList.vue
-->
<template>
  <div class="page-patrol">
    <div class="search-box">
      <div class="select-box">
        <div class="label">专家类型：</div>
        <div class="item">
          <el-select size="small" class="select" clearable v-model="type" @change="search" placeholder="请选择">
            <el-option label="堤防海塘抢险" value="1"> </el-option>
            <el-option label="水闸泵站抢险" value="2"> </el-option>
          </el-select>
        </div>
      </div>
      <div class="select-box">
        <div class="label">专家级别：</div>
        <div class="item">
          <el-select size="small" class="select" clearable v-model="level" @change="search" placeholder="请选择">
            <el-option label="省级专家" value="1"> </el-option>
            <el-option label="市级专家" value="2"> </el-option>
            <el-option label="县级专家" value="3"> </el-option>
          </el-select>
        </div>
      </div>
      <div class="select-box">
        <div class="label">擅长方向：</div>
        <div class="item">
          <el-select size="small" class="select" clearable v-model="goodAt" @change="search" placeholder="请选择">
            <el-option label="防汛抢险专家" value="1"> </el-option>
            <el-option label="防汛调度专家" value="2"> </el-option>
          </el-select>
        </div>
      </div>
    </div>
    <v-table :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
      <template v-slot:type="row">
        <span>{{ row.data.type == 1 ? '堤防海塘抢险' : '水闸泵站抢险' }}</span>
      </template>
      <template v-slot:goodAt="row">
        <span>{{ row.data.goodAt == 1 ? '防汛抢险专家' : '防汛调度专家' }}</span>
      </template>
      <template v-slot:level="row">
        <span>{{ row.data.level == 1 ? '省级专家' : row.data.level == 2 ? '市级专家' : '县级专家' }}</span>
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
import { specialist } from '@/api/seawall.js';
import VTable from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/table/VTable.vue';
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
          prop: 'name',
          label: '专家名称'
        },
        {
          prop: 'department',
          label: '所在单位'
        },
        {
          prop: 'title',
          label: '职务/职称'
        },
        {
          prop: 'type',
          label: '专家类型',
          slot: true
        },
        {
          prop: 'goodAt',
          label: '擅长方向',
          slot: true
        },
        {
          prop: 'mobile',
          label: '联系方式'
        },
        {
          prop: 'level',
          label: '专家级别',
          slot: true
        }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      type: '',
      level: '',
      goodAt: ''
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
      let res = await specialist({
        type: this.type,
        level: this.level,
        goodAt: this.goodAt,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      });
      console.log(res, '12312312');
      if (res.code == 0 && res.data) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    onSizeChange(size) {
      this.pageSize = size;
      this.pageNum = 1;
      this.getPagePatrol();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getPagePatrol();
    },
    search() {
      this.pageNum = 1;
      this.getPagePatrol();
    }
  }
};
</script>

<style lang="scss" scoped>
.search-box {
  display: flex;
}
.select-box {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  margin-right: 20px;
  .label {
    color: #fff;
    font-size: 14px;
  }
  .item {
    .select {
      border: 1px solid #ffffff;
      border-radius: 4px;
    }
  }
}
.page-patrol {
  .bottom__pagination {
    padding-top: 10px;
    text-align: right;
  }
}
</style>
