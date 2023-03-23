<!--
 * @Author: wqn
 * @Date: 2023-02-15 11:12:31
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 13:39:02
 * @Description: 管控清单弹窗内的区域选择器
-->
<template>
  <el-select class="list-dialog-admn-select" v-model="value_" placeholder="请选择" @change="value_change" clearable>
    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
</el-select>
</template>

<script>



import { getAdcdTree } from "@/api/api_utils";
export default {
  name: "ListDialogAdmnSelect",
  props: {
    value: {
      type: String,
      default: "",
    },
    projectCategory: {
      type: String,
      default: '海塘',
    }
  },
  components: {},
  data() {
    return {
      value_: "",
      options: [],
      userAdcd: this.$localData("get", "userInfo").adcd,
    };
  },
  computed: {},
  watch: {
    value() {
      this.value_ = this.value;
    },
  },
  created() {
    this.value_ = this.value;
    this.getAdcdOptions();
  },
  mounted() { },
  destroyed() { },
  methods: {
    async getAdcdOptions() {
      let res = await getAdcdTree({
        adcd: this.userAdcd,
        fetchAll: true,
        projectCategory: this.projectCategory
      });
      if (res.success) {
        let adcdAllName = "";
        if (this.userAdcd.endsWith("0000000000")) {
          adcdAllName = "浙江省";
        } else if (this.userAdcd.endsWith("00000000")) {
          adcdAllName = "全市";
        } else {
          adcdAllName = "全区/县";
        }
        let temp = [
          {
            label: adcdAllName,
            value: this.userAdcd,
            checked: true,
          },
        ];
        this.options = [...temp, ...res.adcdTree.children];
      }
    },
    value_change(value_) {
      this.$emit("change", value_);
    },
  },
};
</script>
<style lang='scss' scoped>
.list-dialog-admn-select {
  width: 231px;

  ::v-deep .el-input.el-input--suffix {
    .el-input__inner {
      height: 32px;
      background-color: transparent;
      border: none;
      box-shadow: 0 0 20px 0 inset #1c415b;
      color: #b0d9e8;
    }

    .el-input__suffix {
      .el-input__suffix-inner {
        .el-select__caret.el-input__icon {
          line-height: 32px;
          color: #fff;

          &.el-icon-arrow-up {
            font-family: "icomoon" !important;
            font-size: 12px;
            transform: rotateZ(0);

            &.is-reverse {
              transform: rotateZ(-180deg);
            }

            &:before {
              content: "\e900";
            }
          }
        }
      }
    }
  }
}</style>