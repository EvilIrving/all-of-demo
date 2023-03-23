<!--
 * @Author: dyn
 * @Date: 2021-07-22 17:50:54
 * @LastEditTime: 2021-07-23 14:51:50
 * @LastEditors: Please set LastEditors
 * @Description: 应急救援弹框
 * @FilePath: \synthesizeAmap\src\views\hydraulic_amap\components\dialog\RescueDialog.vue
-->
<template>
  <div class="rescue_dialog">
    <table class="rescue_table">
      <tr v-for="(item, index) in warehouseArr" :key="index">
        <td class="rescue_label">{{ item.label }}</td>
        <td class="rescue_value">
          {{ info[item.prop] ? info[item.prop] : "-"
          }}{{ item.unit ? item.unit : "" }}
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: "RescueDialog",
  data() {
    return {
      warehouseArr: [
        {
          label: "行政区划",
          prop: "areaName",
        },
        {
          label: "地址",
          prop: "address",
        },
        {
          label: "仓库面积",
          prop: "acreage",
          unit: "㎡",
        },
        {
          label: "物资价值",
          prop: "materialPrice",
          unit: "万元",
        },
        {
          label: "联系人",
          prop: "chargePerson",
        },
        {
          label: "联系方式",
          prop: "chargePhone",
        },
      ],
      ranksArr: [
        {
          label: "行政区划",
          prop: "areaName",
        },
        {
          label: "地址",
          prop: "address",
        },
        {
          label: "编制人数",
          prop: "area",
        },
        {
          label: "现有人数",
          prop: "funds",
          unit: "人",
        },
        {
          label: "联系人",
          prop: "chargePerson",
        },
        {
          label: "联系方式",
          prop: "chargePhone",
        },
      ],
    };
  },
  props: {
    info: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  components: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.rescue_dialog {
  padding-top: 12px;
  .rescue_table {
    width: 100%;
    border: 1px solid #00c1ff;
    td {
      height: 44px;
      line-height: 44px;
      border: 1px solid #00c1ff;
      color: #fff;
      &.rescue_label {
        width: 160px;
        text-align: center;
        background-image: linear-gradient(
          90deg,
          rgba(44, 158, 191, 0.3) 3%,
          rgba(41, 147, 180, 0.1) 100%
        );
        color: #56fefe;
      }
      &.rescue_value {
        padding-left: 18px;
      }
    }
  }
}
</style>