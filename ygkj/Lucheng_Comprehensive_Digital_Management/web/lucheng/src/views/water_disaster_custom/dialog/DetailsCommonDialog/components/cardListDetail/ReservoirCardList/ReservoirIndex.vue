<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-10-26 19:06:37
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\dialog\DetailsCommonDialog\components\cardListDetail\ReservoirCardList\ReservoirIndex.vue
-->
<template>
  <div class="person">
    <div class="person-title">
      <StatisticsBox title="救援队伍总数" icon="icon-detail-stat-water-res">
        <span>{{ detail.totalTeam }}</span> 个
      </StatisticsBox>
      <StatisticsBox :title="detail.maxExistingName + '(最多)'" icon="icon-detail-stat-water-res">
        <span>{{ detail.maxExistingNum }}</span> 人
      </StatisticsBox>
      <StatisticsBox :title="detail.minExistingName + '(最少)'" icon="icon-detail-stat-water-res">
        <span>{{ detail.minExistingNum }}</span> 人
      </StatisticsBox>
    </div>
    <div class="person-box">
      <div class="person-search">
        <LabelFormItem label="抢险专业:" style="margin-right: 20px">
          <el-select v-model="rescueSpecialtyType" size="small" clearable @change="search">
            <el-option v-for="item in rescueSpecialty" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem label="单位性质:" style="margin-right: 20px">
          <el-select v-model="unitNatureType" size="small" clearable @change="search">
            <el-option v-for="item in unitNature" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </LabelFormItem>
        <LabelFormItem label="名称:">
          <el-input v-model="name" placeholder="请输入内容" clearable style="width: 180px" @change="search" />
        </LabelFormItem>
        <DataDetailButton style="margin-left: auto">导 出</DataDetailButton>
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
import { getTeamList, getTeamCountList } from '@/api/flow';
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
        { prop: 'name', label: '名称' },
        { prop: 'rescueSpecialty', label: '抢险专业' },
        { prop: 'unitNature', label: '单位性质' },
        { prop: 'organizationNumber', label: '编制人数' },
        { prop: 'existingNumber', label: '现有人数' },
        { prop: 'address', label: '详细地址' },
        { prop: 'chargePhone', label: '联系方式' }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 0,
      pageNum: 1,
      rescueSpecialty: [
        {
          label: '防汛救灾',
          value: '防汛救灾'
        },
        {
          label: '水利抢险',
          value: '水利抢险'
        },
        {
          label: '工程救援',
          value: '工程救援'
        },
        {
          label: '水利专业',
          value: '水利专业'
        },
        {
          label: '水上救援',
          value: '水上救援'
        },
        {
          label: '水利工程抢险',
          value: '水利工程抢险'
        },
        {
          label: '其他应急抢险',
          value: '其他应急抢险'
        },
        {
          label: '水上抢险',
          value: '水上抢险'
        },
        {
          label: '堤防抢险',
          value: '堤防抢险'
        }
      ],
      rescueSpecialtyType: '',
      unitNature: [
        {
          label: '水利局机关单位抢险组织',
          value: '水利局机关单位抢险组织'
        },
        {
          label: '社会救援机构',
          value: '社会救援机构'
        },
        {
          label: '自收自支事业单位',
          value: '自收自支事业单位'
        },
        {
          label: '差额事业单位',
          value: '差额事业单位'
        },
        {
          label: '全额事业单位',
          value: '全额事业单位'
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
      let res = await getTeamList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        unitNature: this.unitNatureType,
        rescueSpecialty: this.rescueSpecialtyType,
        name: this.name
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.totalPage = res.data.totalRows;
      }
    },
    async getDeviceType() {
      let res = await getTeamCountList();
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