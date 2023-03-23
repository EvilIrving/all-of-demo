<script>
export default {
  props: { project: { type: Object, default: () => ({}) } },
};
</script>
<template>
  <el-descriptions
    title="基本资料"
    direction="vertical"
    :column="5"
    class="descriptions-box"
  >
    <template #title>
      <p class="">基本资料</p>
    </template>
    <!-- <template slot="extra">
      <el-button type="primary" size="small" @click="$emit('edit')"
        >修改</el-button
      >
      <el-button type="primary" size="small" @click="$emit('view')">
        查看
      </el-button>
    </template> -->
    <el-descriptions-item label="项目名称">
      {{ project.projectName || "-" }}
    </el-descriptions-item>
    <el-descriptions-item label="项目编码">
      {{ project.projectCode || "-" }}
    </el-descriptions-item>
    <el-descriptions-item label="行政区划">
      {{ project.adnm || "-" }}
    </el-descriptions-item>
    <el-descriptions-item label="年份">
      {{ project.year || "-" }}
    </el-descriptions-item>
    <el-descriptions-item label="所属河流">
      {{ project.riverName || "-" }}
    </el-descriptions-item>
  </el-descriptions>
</template>

<style lang="scss" scoped>
.descriptions-box {
  border: 1px solid rgba(94, 91, 91, 0.2);
  padding: 10px;
  ::v-deep .el-descriptions__header {
    padding-bottom: 8px;
    border-bottom: 2px solid rgba(94, 91, 91, 0.2);
  }
}
</style>