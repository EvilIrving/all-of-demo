<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-03-08 09:24:37
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\warehouseCardList\warehouseIndex.vue
-->
<template>
  <div class="person">
    <div class="person-title">
      <StatisticsBox title="救援仓库总数" icon="icon-warehouse">
        <span>{{ detail.total }}</span> 个
      </StatisticsBox>
      <StatisticsBox title="仓库总面积" icon="icon-warehouse">
        <span>{{ detail.totalArea }}</span> ㎡
      </StatisticsBox>
      <StatisticsBox title="仓库总价值" icon="icon-warehouse">
        <span>{{ detail.materialPrice.toFixed(2) }}</span> 元
      </StatisticsBox>
    </div>
    <div class="person-box">
      <div class="person-search">
        <LabelFormItem label="仓库类型:" style="margin-right: 20px">
          <el-select v-model="unitNatureType" size="small" clearable @change="search">
            <el-option v-for="item in unitNature" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem label="名称:">
          <el-input v-model="name" placeholder="请输入内容" clearable style="width: 180px" @change="search" />
        </LabelFormItem>
        <DataDetailButton @click="exportData" style="margin-left: auto">导 出</DataDetailButton>
      </div>
      <div class="person-table-box">
        <VTable :dynamicColumnSetting="dynamicColumnSetting" :tableData="tableData" />
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
    </div>
  </div>
</template>

<script>
import StatisticsBox from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/StatisticsBox.vue';
import LabelFormItem from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/LabelFormItem.vue';
import DataDetailButton from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/DataDetailButton.vue';
import VTable from '../../../components/table/VTable.vue';
import { getWaterWarehouses, getWaterWarehousesCount } from '@/api/flow';
import { exportWareHouses } from '@/api/micro-service/project-manage/export';
export default {
  components: {
    StatisticsBox,
    LabelFormItem,
    DataDetailButton,
    VTable
  },
  props: {
    code: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      name: '',
      dynamicColumnSetting: [
        { prop: 'name', label: '仓库名称' },
        { prop: 'type', label: '仓库类型' },
        { prop: 'acreage', label: '仓库面积(㎡)' },
        { prop: 'materialPrice', label: '仓库总价值(元)' },
        { prop: 'address', label: '详细地址' },
        { prop: 'chargePerson', label: '联系人' },
        { prop: 'chargePhone', label: '联系方式' }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 0,
      pageNum: 1,
      unitNature: [
        {
          label: '省供销社',
          value: '省供销社'
        },
        {
          label: '社会厂家',
          value: '社会厂家'
        },
        {
          label: '县级仓库',
          value: '县级仓库'
        },
        {
          label: '市级仓库',
          value: '市级仓库'
        },
        {
          label: '省级仓库',
          value: '省级仓库'
        }
      ],
      unitNatureType: '',
      name: '',
      detail: {}
    };
  },
  mounted() {
    this.getList();
    this.getDeviceType();
  },
  methods: {
    async getList() {
      let res = await getWaterWarehouses({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        type: this.unitNatureType,
        name: this.name
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    exportData() {
      let obj = {
        type: this.unitNatureType,
        name: this.name
      };
      exportWareHouses(obj)
    },
    async getDeviceType() {
      let res = await getWaterWarehousesCount();
      if (res.code == 0) {
        this.detail = res.data;
      }
    },
    search() {
      this.pageNum = 1;
      this.getList();
    },
    onSizeChange(size) {
      this.pageNum = 1;
      this.pageSize = size;
      this.getList();
    },
    onCurrentChange(page) {
      this.pageNum = page;
      this.getList();
    }
  }
};
</script>

<style lang="scss" scoped>
.person {
  height: 100%;
}
.person-title {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid rgba(0, 193, 255, 0.2);
}
.person-box {
  padding-top: 16px;
  height: calc(100% - 90px);
  .person-table-box {
    margin-top: 24px;
    height: calc(100% - 90px);
    .bottom__pagination {
      padding-top: 10px;
      text-align: right;
    }
  }
}
</style>

<style lang="scss">
.person-search {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .label-form-item {
    .label-form-item__Label {
      color: #fff;
    }
    .el-input__inner {
      border: 1px solid #fff;
    }
  }
}
.person-table-box {
  .table-wrap {
    .el-table {
      .table-header {
        background: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
        th {
          background: transparent;
          color: #00baff;
        }
      }
    }
  }
}
.person-box {
  .table-box {
    .bottom__pagination {
      .el-pagination__total {
        color: #fff;
      }
      .el-pagination {
        .el-pagination__sizes {
          .el-input__inner {
            border: 1px solid #fff;
          }
          .el-input__suffix {
            .el-input__icon {
              color: #fff;
            }
          }
        }
        .el-pager {
          li {
            color: #fff;
            &.active {
              background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
              border-radius: 0.125rem;
            }
          }
        }
      }
    }
  }
}
</style>