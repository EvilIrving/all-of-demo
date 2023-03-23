<!--
 * @Date: 2022-06-08 14:30:47
 * @Author: 
 * @Description: 物业督办-督查检查-详情页
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-16 16:49:02
 * @FilePath: \lucheng-app\src\views\appView\propertysupervision\RelayTask\TaskDetail.vue
-->
<template>
  <div class="task_detail">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="'详情页'"  :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <van-cell-group>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="工程名称"
        :value="form.projectName"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="检查时间"
        :value="form.inspectionTime"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="责任单位"
        :value="form.responsibleUnitName"
      />
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="责任人"
        :value="form.responsiblePeopleName"
      />
      <van-cell
        value-class="cell-value"
        :is-link="form.supervisionStatus == 1"
        title-class="cell-title"
        title="督办状态"
      >
        <template #default>
          <span @click="showPickerClick">{{
            form.supervisionStatus | supervisionStatus
          }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="处理状态"
      >
        <template #default>
          <span>{{ form.handlingStatus == 1 ? "未处理" : "已处理" }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="归档状态"
      >
        <template #default>
          <span>{{ form.archivingStatus == 1 ? "未归档" : "已归档" }}</span>
        </template>
      </van-cell>
      <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="存在问题"
        :value="form.problems"
      />
    </van-cell-group>
    <van-cell
      value-class="cell-value"
      title-class="cell-title"
      title="隐患图片"
    >
      <template #default>
        <div class="upload-preview-box">
          <div v-for="item in form.problemsPictureList" :key="item.id">
            <van-image
              width="70"
              height="70"
              @click="imagePreview(item.fileUrl)"
              :src="item.fileUrl"
            />
          </div>
        </div>
      </template>
    </van-cell>
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="onConfirm"
      >
        <template #option="option">
          <div>{{ option.name }}</div>
        </template>
      </van-picker>
    </van-popup>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/projectDetail";
import { filterParams } from "@/utils/utils";
import { handingInspectionApi } from "@/api/rainfall";
export default {
  name: "TaskDetail",
  props: {},
  filters: {
    supervisionStatus: (value) => {
      switch (value) {
        case "1":
          return "未督促";
        case "2":
          return "已督促";
        default:
          return "-";
      }
    },
  },
  data() {
    return {
      form: {},
      showPicker: false,
      columns: [
        {
          name: "已督促",
          value: 2,
        },
        {
          name: "未督促",
          value: 1,
        },
      ],
    };
  },
  mounted() {
    this.addZwlog(this)
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.list({ id: this.$route.query.id });
      if (res.code == 0) {
        this.form = filterParams(res.data.list[0]);
      }
    },
    async onConfirm(data) {
      if (data.value == 2) {
        let res = await handingInspectionApi({ id: this.form.id, handType: 1 });
        if (res.code == 0) {
          this.$toast.success("已督促成功~");
          this.getList();
        } else {
          this.$toast.fail(res.message);
        }
        this.showPicker = false;
      }
    },
    showPickerClick() {
      if (this.form.supervisionStatus == 1) {
        this.showPicker = true;
      }
    },
    imagePreview(src) {
      this.$imagePreview([src]);
    },
  },
};
</script>

<style scoped lang="scss">
.task_detail {
  width: 100%;
  min-height: 100%;
  background: #f2f6f9;
  .cell-value {
    color: #333333;
    font-size: 14px;
    font-weight: 500;
    flex: 2.2;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 14px;
  }
}
.default-box {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
  .img-box {
    width: 20px;
    margin-right: 5px;
  }
  span {
    max-width: calc(100% - 26px);
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}
.upload-preview-box {
  display: flex;
  width: 100%;
  flex-flow: wrap;
  > div {
    margin: 0 9px 9px 0;
    position: relative;
  }
}
</style>
<style lang="scss">
.van-cell {
  padding-left: 10px;
  padding-right: 10px;
  &::after {
    left: 0;
    right: 0;
    border-color: #e7e7ec;
  }
}
</style>