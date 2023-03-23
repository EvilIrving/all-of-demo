<template>
  <div class="select-person">
    <p>指定巡查人员</p>
    <el-button
      v-for="(item, i) in selectPersonList"
      :key="i"
      type="primary"
      size="small"
    >
      <i class="el-icon-user"></i>
      {{ item.userName }}
    </el-button>
    <el-button
      :icon="hasSelect ? 'el-icon-edit' : 'el-icon-plus'"
      size="small"
      :disabled="disabled"
      @click="selectPerson()"
      circle
    />

    <PersonnelListDialog
      v-if="selectDialogVisible"
      ref="Configuration"
      :PersonnelType="PersonnelType"
      :visible="selectDialogVisible"
      :selectPersonList="selectPersonIdList"
      @saveEmit="saveEmit"
      @close="closePersonnel"
    />
  </div>
</template>

<script>
import PersonnelListDialog from "../personnelListDailog.vue";
export default {
  name: "SelectPerson",
  props: {
    PersonnelType: String,
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      selectDialogVisible: false,
      selectPersonIdList: [],
      selectPersonList: [],
    };
  },
  components: {
    PersonnelListDialog,
  },
  computed: {
    /** @returns {boolean} */
    hasSelect() {
      return this.selectPersonList.length > 0;
    },
  },
  methods: {
    selectPerson() {
      this.selectDialogVisible = true;
    },
    saveEmit(val) {
      this.selectPersonList = val;
      this.selectPersonIdList = val.map((item) => item.id);
    },
    // 关闭人员列表弹窗
    closePersonnel() {
      this.selectDialogVisible = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.select-person {
  > p:nth-child(1) {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.48);
    letter-spacing: 0;
    font-weight: 600;
    margin-bottom: 10px;
  }
  .inspectors-sty {
    background: #1492ff;
    padding: 5px 12px;
    color: #fff;
    margin-right: 10px;
  }
}
</style>