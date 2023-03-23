<template>
  <div class="responsible-list">
    <project-item title="责任人">
      <el-row v-if="responsibleList.length > 0" :gutter="12">
        <el-col
          v-for="(item, index) in responsibleList"
          :key="index"
          :span="12"
        >
          <responsible-list-item :flag='index' :data="item" />
        </el-col>
      </el-row>
      <div v-else class="no-responsible">暂无责任人</div>
    </project-item>
  </div>
</template>

<script>
import ProjectItem from "./ProjectItem";
import ResponsibleListItem from "./ResponsibleListItem";
export default {
  name: "ResponsibleList",
  props: {
    responsibleList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {};
  },
  components: { ProjectItem, ResponsibleListItem },
};
</script>

<style lang="scss" scoped>
.no-responsible {
  user-select: none;
  height: 400px;
  text-align: center;
  line-height: 430px;
  color: #fff;
  background-image: url(~assets/images/no-responsible.png);
  background-repeat: no-repeat;
  background-position-x: center;
  background-position-y: 36%;
}
</style>