<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-05-31 14:27:45
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\components\ReservoirDialog\reservoirFacility.vue
-->
<template>
  <div class="person">
    <div class="person-title">
      <StatisticsBox title="防汛设备" icon="icon-reservoir-facility">
        <span>{{ floodControEquipment }}</span> 个
      </StatisticsBox>
      <StatisticsBox title="抽水设备" icon="icon-reservoir-facility">
        <span>{{ pumpingEquipment }}</span> 个
      </StatisticsBox>
      <StatisticsBox title="照明设备" icon="icon-reservoir-facility">
        <span>{{ lightingEquipment }}</span> 个
      </StatisticsBox>
    </div>
    <div class="person-box">
      <div class="person-search">
        <LabelFormItem label="名称:">
          <el-input
            size="small"
            placeholder="请输入"
            @change="getTeamPersons"
            v-model="name"
            style="width: 180px"
            clearable
          />
        </LabelFormItem>
        <!-- <DataDetailButton style="margin-left: auto">导 出</DataDetailButton> -->
      </div>
      <div class="person-table-box">
        <primary-map-table :columnList="dynamicColumnSetting" :dataList="tableData"></primary-map-table>
        <!-- <div class="bottom__pagination">
          <el-pagination
            :current-page.sync="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="totalPage"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
          />
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { StatisticsBox, LabelFormItem, DataDetailButton } from '../../DetailsCommonDialog/components';
import VTable from '../../DetailsCommonDialog/components/table/VTable.vue';
import { PrimaryMapTable } from '@/components';
import { getTeamDeviceList, getTeamDeviceByType } from '@/api/flow';
export default {
  components: {
    StatisticsBox,
    LabelFormItem,
    DataDetailButton,
    VTable,
    PrimaryMapTable
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
        {
          prop: 'name',
          label: '设备名称'
        },
        {
          prop: 'type',
          label: '设备类型'
        },
        {
          prop: 'num',
          label: '设备数量'
        },
        {
          prop: 'measure',
          label: '单位'
        }
      ],
      tableData: [],
      lightingEquipment: 0,
      floodControEquipment: 0,
      pumpingEquipment: 0
    };
  },
  mounted() {
    this.getTeamPersons();
    this.getDeviceType();
  },
  methods: {
    async getTeamPersons() {
      let res = await getTeamDeviceList({
        teamId: this.code,
        name: this.name,
        pageNum: 1,
        pageSize: 9999
      });
      console.log(res);
      if (res.code == 0) {
        this.tableData = res.data.list;
      }
    },
    async getDeviceType() {
      let res = await getTeamDeviceByType({
        teamId: this.code
      });
      if (res.code == 0) {
        this.lightingEquipment = res.data.lightingEquipment;
        this.floodControEquipment = res.data.floodControEquipment;
        this.pumpingEquipment = res.data.pumpingEquipment;
      }
    },
    onSizeChange() {},
    onCurrentChange() {}
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