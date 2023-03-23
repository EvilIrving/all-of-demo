<template>
  <div>
    <van-list
      v-model="loading"
      :finished="finished"
      :finished-text="finishedText"
      @load="onLoad"
      offset="50"
    >
      <table
        class="table-box"
        :class="{ 'old-wrap-table': addRouteType === 'older' }"
      >
        <thead class="table-title">
          <th>序号</th>
          <th v-for="item in theadList" :key="item.prop">{{ item.label }}</th>
        </thead>
        <tbody class="table-body" v-if="list.length > 0">
          <tr
            v-for="(item, index) in list"
            :key="index"
            @click="rowClick(item, index)"
          >
            <td>{{ index + 1 }}</td>
            <td v-for="ite in theadList" :key="ite.prop">
              <template v-if="ite.slot">
                <slot :name="ite.prop" :data="item"></slot>
              </template>
              <span v-else>{{ item[ite.prop] ? item[ite.prop] : "-" }}</span>
            </td>
          </tr>
        </tbody>
        <tbody class="table-body" v-else>
          <tr>
            <td :colspan="theadList.length + 1">暂无数据~</td>
          </tr>
        </tbody>
      </table>
    </van-list>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  //除去theadList和list，其余属性参考http://vant-contrib.gitee.io/vant/v2/#/zh-CN/list文档
  props: {
    theadList: Array, //表头数据
    list: Array, //表格数据
    finished: {
      //数据是否已加载完成
      type: Boolean,
      default: true,
    },
    finishedText: {
      //数据加载完成后的提示文案
      type: String,
      default: "",
    },
  },
  computed: {
    ...mapGetters(["addRouteType"]),
  },
  data() {
    return {
      loading: false, //数据是否处于加载状态，加载过程中不触发load事件
    };
  },
  watch: {
    list: {
      handler: function (val) {
        if (val) {
          this.loading = false;
        }
      },
      deep: true,
    },
  },
  methods: {
    //触底加载
    onLoad() {
      this.$emit("onLoad");
    },
    rowClick(data, index) {
      this.$emit("row-click", data, index);
    },
  },
};
</script>

<style lang="scss" scoped>
table,
table thead th,
table tr td {
  border: 1px solid #e6e6e6;
}

.table-box {
  width: 100%;
  // height: 100px;
  .table-title {
    th {
      padding: 14px 0;
      text-align: center;
      font-weight: 600;
      color: #666666;
      font-size: 12px;
      background-color: #ddecff;
      &:first-child {
        width: 49px;
      }
    }
  }
  .table-body {
    tr {
      td {
        padding: 14px 0;
        text-align: center;
        font-size: 12px;
        color: #666666;
      }
      &:nth-child(2n) {
        td {
          background-color: #f4f8fb;
        }
      }
    }
  }
  &.old-wrap-table {
    .table-title {
      th {
        line-height: 27px;
        text-align: center;
        font-weight: 500;
        color: #666666;
        font-size: 20px;
        background-color: #ddecff;
        vertical-align: middle;
        padding: 5px;
        box-sizing: border-box;
        &:first-child {
          width: 52px;
        }
      }
    }
    .table-body {
      tr {
        td {
          height: 48px;
          line-height: 27px;
          text-align: center;
          font-size: 20px;
          color: #666666;
          vertical-align: middle;
          padding: 5px;
          box-sizing: border-box;
        }
        &:nth-child(2n) {
          td {
            background-color: #f4f8fb;
          }
        }
      }
    }
  }
}
</style>