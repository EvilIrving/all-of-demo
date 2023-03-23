<!--
 * @Author: dyn
 * @Date: 2020-11-13 15:59:33
 * @LastEditTime: 2021-07-23 18:37:33
 * @LastEditors: Please set LastEditors
 * @Description: 地图列表搜索
 * @FilePath: \pingHuWaterPlatform\src\components\primary_search\index.vue
-->
<template>
  <div class="primary-select primary-select-wrap">
    <el-row :gutter="10">
      <el-col :span="8">
        <el-select
          v-model="valueType"
          class="primary-select"
          @change="typeHandle"
        >
          <el-option
            v-for="item in optionsType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="8">
        <el-select
          v-model="value"
          class="primary-select"
          @change="selectHandle"
          v-if="type == 'area'"
        >
          <el-option
            v-for="item in optionsArea"
            :key="item.adcd"
            :label="item.adnm"
            :value="item.adcd"
          >
          </el-option>
        </el-select>
        <el-select
          v-model="value"
          class="primary-select"
          @change="selectHandle"
          v-if="type == 'basin'"
        >
          <el-option
            v-for="item in optionsBasin"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getArea, getBasin } from "@/api/system";
export default {
  name: "PrimarySelect",
  props: {},
  data() {
    return {
      optionsType: [
        {
          value: "area",
          label: "行政区划",
        },
        {
          value: "basin",
          label: "流域",
        },
      ],
      valueType: "行政区划",
      options: [],
      optionsArea: [],
      optionsBasin: [
        {
          value: "全部",
          label: "全部",
        },
        {
          value: "鳌江",
          label: "鳌江",
        },
        {
          value: "瓯江",
          label: "瓯江",
        },
        {
          value: "飞云江",
          label: "飞云江",
        },
        // {
        //   value: "椒江",
        //   label: "椒江",
        // },
        // {
        //   value: "其他",
        //   label: "其他",
        // },
      ],
      value: "",
      type: "area",
      label: "",
    };
  },
  components: {},
  created() {},
  mounted() {
    this.$nextTick(() => {
      this._getArea();
      // this._getBasin();
    });
  },
  methods: {
    _getArea() {
      getArea({
        areaCode: 3303,
        level: 3,
      }).then((res) => {
        if (res.code == 0) {
          this.optionsArea = res.data;
          if (this.options.length == 0) {
            this.options = this.optionsArea;
            this.options.unshift({
              adnm: "全部",
              adcd: "",
            });
            this.value = this.optionsArea[0].adcd;
            this.label = this.optionsArea[0].adnm;
            // console.log(this.value);
            this.$emit("selectHandle", this.value, this.type, this.label);
          }
        }
      });
    },
    _getBasin() {
      getBasin().then((res) => {
        if (res.code == 0) {
          this.optionsBasin = res.data;
        }
      });
    },
    typeHandle(val) {
      this.type = val;
      this.value =
        val == "area" ? this.optionsArea[0].adcd : this.optionsBasin[0].value;
      this.$emit("selectHandle", this.value, this.type);
    },
    selectHandle(val) {
      this.options.map((item) => {
        if (item.adcd == val) {
          this.label = item.adnm;
        }
      });
      this.$emit("selectHandle", val, this.type, this.label);
    },
    initArea() {
      this.value = "";
      this.selectHandle(this.value);
    },
  },
};
</script>
