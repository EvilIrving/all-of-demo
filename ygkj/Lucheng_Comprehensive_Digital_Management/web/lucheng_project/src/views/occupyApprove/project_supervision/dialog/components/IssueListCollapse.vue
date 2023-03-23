<script>
//@ts-check
/**
 * @typedef {Object} IssueItem
 * @property {number} status
 * @property {string} createId
 * @property {string} id
 * @property {string} entryName
 * @property {string} problemDescrip
 * @property {number} rectify
 * @property {string[]} urls
 */
export default {
  props: {
    /** @type {import('vue/types/options').PropValidator<IssueItem[]>} */
    list: { type: Array, default: () => [] },
  },
  methods: {
    /** @param {IssueItem} item */
    getStatus(item) {
      return item.status == 1;
    },
  },
};
</script>

<template>
  <el-collapse accordion>
    <el-collapse-item v-for="(item, i) in list" :key="item.id">
      <template #title>
        <p>
          <el-tag
            size="small"
            :type="['info', 'success', 'danger'][item.status]"
          >
            {{ ["巡查中", "正常", "异常"][item.status] }}
          </el-tag>
          问题{{ i + 1 }}: {{ item.entryName }}
        </p>
      </template>
      <el-descriptions size="small" direction="vertical" :column="1" border>
        <el-descriptions-item label="问题描述">
          {{ item.problemDescrip || "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="相关照片">
          <template v-if="!item.urls || !item.urls.length">
            {{ "无相关照片" }}
          </template>
          <el-image
            v-else
            v-for="url in item.urls"
            :key="url"
            :src="url"
            style="width: 80px; height: 80px"
            :preview-src-list="[url]"
          />
        </el-descriptions-item>
      </el-descriptions>
    </el-collapse-item>
  </el-collapse>
</template>