<!--
 * @Author: dyn
 * @Date: 2021-07-22 17:50:54
 * @LastEditTime: 2022-05-31 17:02:07
 * @LastEditors: Please set LastEditors
 * @Description: 应急救援弹框
 * @FilePath: \lucheng_aMap\src\views\hydraulic_amap\components\dialog\RescueDialog.vue
-->
<template>
  <div class="rescue_dialog">
    <table class="rescue_table" v-if="detail.type === '避灾点'">
      <tr v-for="(item, index) in warehouseArr" :key="index">
        <td class="rescue_label">{{ item.label }}</td>
        <td class="rescue_value">
          {{ info[item.prop] ? info[item.prop] : "-"
          }}{{ item.unit ? item.unit : "" }}
        </td>
      </tr>
    </table>
    <warehouse :disasterData="detail" v-if="detail.type === '防汛仓库'" />
    <ReservoirDialog :pointData="detail" v-if="detail.type === '救援队伍'" />
  </div>
</template>

<script>
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import warehouse from "./warehouse.vue";
import ReservoirDialog from "./ReservoirDialog.vue";

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
          label: "避灾点名称",
          prop: "pointName",
        },
        {
          label: "可容纳人数",
          prop: "pointCapacity",
        },
        {
          label: "联系人",
          prop: "contacts",
        },
        {
          label: "联系方式",
          prop: "phone",
        },
        {
          label: "避灾点地址",
          prop: "pointAddress",
        },
        {
          label: "避灾点说明",
          prop: "pointDirections",
        },
      ],
      pointPhotos: [],
      detail: {},
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
  components: { warehouse, ReservoirDialog },
  created() {},
  mounted() {
    this.detail = this.info;
    console.log(this.detail);

    this.info.type === "避灾点" && this.getFlowList();
    this.warehouseArr = this.ranksArr;
  },
  methods: {
    getFlowList() {
      hydraulicApi
        .avoidancePoint({
          id: this.info.code,
        })
        .then((res) => {
          if (res.code === 0) {
            this.pointPhotos =
              res.data.list[0] || res.data.list[0].pointPhotos.split(",");
          }
        });
    },
  },
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