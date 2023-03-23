<!--
 * @Author: zg
 * @LastEditTime: 2021-10-07 10:17:40
 * @Description: 工程详情-诊
 * @FilePath: /cangnan-app/src/views/projectDetail/diagnosis/Diagnosis.vue
-->
<template>
  <div class="wrap">
    <div class="diagnosis_top">
      <div
        style="display: inline-block; padding: 8px 0 0 0"
        @click="showTypePicker = true"
      >
        {{ typeValue ? typeValue : "工程分类" }} <van-icon name="arrow-down" />
      </div>
      <van-popup v-model="showTypePicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="typeColumns"
          @confirm="onConfirmType"
          @cancel="showTypePicker = false"
        />
      </van-popup>
      <div
        style="display: inline-block; padding: 8px 0 0 0; margin-left: 16px"
        @click="showProPicker = true"
        v-if="typeValue != ''"
      >
        {{ proValue ? proValue : "工程问题" }} <van-icon name="arrow-down" />
      </div>
      <van-popup v-model="showProPicker" position="bottom">
        <van-picker
          show-toolbar
          :columns="proColumns"
          @confirm="onConfirmPro"
          @cancel="showProPicker = false"
        />
      </van-popup>
    </div>

    <div class="diagnosis_content">
      <p class="section_title">人员列表</p>
      <div class="person_list">
        <div class="person_list_item" v-for="item in personList" :key="item.id">
          <div class="person_left">
            <img src="../../../assets/images/person-head.png" alt="" />
            <div>
              <h3>{{ item.workUnit }}</h3>
              <p>
                {{ item.name }}<span>{{ item.job }}</span>
              </p>
            </div>
          </div>
          <div class="person_right">
            {{ item.phone }}
            <span @click="callPhone(item.phone)"></span>
            <!-- <a href="tel:item.phone"></a> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { diagnosisApi } from "@/api/projectDetail";
export default {
  name: "Diagnosis",
  props: {},
  components: {},
  data() {
    return {
      typeValue: "",
      showTypePicker: false,
      typeColumns: ["全部", "水库", "堤防", "海塘", "水闸", "泵站"],
      typeOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "水库",
          value: "水库",
        },
        {
          label: "堤防",
          value: "堤防",
        },
        {
          label: "海塘",
          value: "海塘",
        },
        {
          label: "水闸",
          value: "水闸",
        },
        {
          label: "泵站",
          value: "泵站",
        },
      ],
      proValue: "",
      proValues: "",
      showProPicker: false,
      proColumns: [],
      proOptions: [],
      reservoirColumns: [
        "全部",
        "大坝渗漏",
        "大坝裂缝",
        "溢洪道冲刷",
        "阻水障碍物",
        "涵管（洞）裂缝",
        "闸门门体变形",
        "闸门螺杆弯曲",
        "闸门启闭机老化",
        "近岸坝坡崩塌",
      ],
      reservoirOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "大坝渗漏",
          value: "1",
        },
        {
          label: "大坝裂缝",
          value: "2",
        },
        {
          label: "溢洪道冲刷",
          value: "3",
        },
        {
          label: "阻水障碍物",
          value: "4",
        },
        {
          label: "涵管（洞）裂缝",
          value: "5",
        },
        {
          label: "闸门门体变形",
          value: "6",
        },
        {
          label: "闸门螺杆弯曲",
          value: "7",
        },
        {
          label: "闸门启闭机老化",
          value: "8",
        },
        {
          label: "近岸坝坡崩塌",
          value: "9",
        },
      ],
      dikeColumns: [
        "全部",
        "堤防渗水",
        "堤防管涌",
        "堤防滑坡",
        "堤防横向裂缝",
        "堤防漫溢",
      ],
      dikeOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "堤防渗水",
          value: "10",
        },
        {
          label: "堤防管涌",
          value: "11",
        },
        {
          label: "堤防滑坡",
          value: "12",
        },
        {
          label: "堤防横向裂缝",
          value: "13",
        },
        {
          label: "堤防漫溢",
          value: "14",
        },
      ],
      seawallColumns: ["全部", "海塘渗水", "海塘滑坡", "海塘沉降", "塘前冲刷"],
      seawallOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "海塘渗水",
          value: "15",
        },
        {
          label: "海塘滑坡",
          value: "16",
        },
        {
          label: "海塘沉降",
          value: "17",
        },
        {
          label: "塘前冲刷",
          value: "18",
        },
      ],
      sluiceColumns: [
        "全部",
        "闸室裂缝",
        "消能防冲设施出现冲刷",
        "闸门变形",
        "止水橡皮老化",
        "闸门螺杆变形",
      ],
      sluiceOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "闸室裂缝",
          value: "19",
        },
        {
          label: "消能防冲设施出现冲刷",
          value: "20",
        },
        {
          label: "闸门变形",
          value: "21",
        },
        {
          label: "止水橡皮老化",
          value: "22",
        },
        {
          label: "闸门螺杆变形",
          value: "23",
        },
      ],
      pumpColumns: [
        "全部",
        "水泵运转震动",
        "水泵不出水",
        "轴承过热",
        "绝缘电阻降低",
      ],
      pumpOptions: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "水泵运转震动",
          value: "24",
        },
        {
          label: "水泵不出水",
          value: "25",
        },
        {
          label: "轴承过热",
          value: "26",
        },
        {
          label: "绝缘电阻降低",
          value: "27",
        },
      ],
      personList: [],
    };
  },
  mounted() {
    //console.log(this.$localData("get", "projectCode"));
    this._loadPage();
  },
  methods: {
    //工程类型
    onConfirmType(value, index) {
      // console.log(index);
      if (value == "水库") {
        this.proColumns = this.reservoirColumns;
        this.proOptions = this.reservoirOptions;
      } else if (value == "水闸") {
        this.proColumns = this.sluiceColumns;
        this.proOptions = this.sluiceOptions;
      } else if (value == "海塘") {
        this.proColumns = this.seawallColumns;
        this.proOptions = this.seawallOptions;
      } else if (value == "堤防") {
        this.proColumns = this.dikeColumns;
        this.proOptions = this.dikeOptions;
      } else if (value == "泵站") {
        this.proColumns = this.pumpColumns;
        this.proOptions = this.pumpOptions;
      } else {
        this.proColumns = [];
      }
      this.typeValue = this.typeOptions[index].value;
      this.proValues = "";
      this._loadPage();
      this.showTypePicker = false;
    },
    //工程问题
    onConfirmPro(value, index) {
      // console.log(index);
      this.proValue = value;
      this.proValues = this.proOptions[index].value;
      this._loadPage();
      this.showProPicker = false;
    },

    // 人员列表
    async _loadPage() {
      let res = await diagnosisApi.diagnosisList({
        projectType: this.typeValue,
        projectProblem: this.proValues,
      });
      if (res.code == 0) {
        this.personList = res.data.list;
      }
    },

    //打电话
    callPhone(phone) {
      var a = document.createElement("a");
      a.href = "tel:" + phone;
      var event;
      if (window.MouseEvent) {
        event = new MouseEvent("click");
      } else {
        event = document.createEvent("MouseEvents");
        event.initMouseEvent(
          "click",
          true,
          false,
          window,
          0,
          0,
          0,
          0,
          0,
          false,
          false,
          false,
          false,
          0,
          null
        );
      }
      a.dispatchEvent(event);
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  // background: #eee;
  // height: 800px;
  box-sizing: border-box;
  width: 100%;
  padding: 0 16px 16px;

  .diagnosis_content {
    // padding: 12px 24px 24px 24px;
    .section_title {
      font-size: 18px;
      font-weight: 500;
      color: #000;
      line-height: 32px;
      margin-top: 12px;
    }
    .person_list {
      .person_list_item {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        padding: 16px 0;
        border-bottom: 1px solid #e5e5e5;
        .person_left {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(center);
          img {
            margin-right: 16px;
          }
          & > div {
            h3 {
              font-size: 14px;
              color: rgba(31, 31, 31, 0.48);
              font-weight: 400;
              margin-bottom: 12px;
            }
            p {
              font-size: 20px;
              color: #1f1f1f;
              font-weight: 500;
              span {
                font-size: 14px;
                font-weight: 400;
                margin-left: 8px;
              }
            }
          }
        }
        .person_right {
          font-size: 14px;
          color: #1f1f1f;
          font-weight: 400;
          span {
            display: inline-block;
            width: 16px;
            height: 16px;
            vertical-align: middle;
            margin: -2px 0 0 3px;
            cursor: pointer;
            background: url("../../../assets/images/phone.png") no-repeat center;
          }
        }
      }
    }
  }
}
</style>