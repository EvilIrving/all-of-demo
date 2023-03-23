<template>
  <div class="sidebar" :style="{ width: '250px' }">
    <div class="catalog-title">
      <div class="catalog-title-primary">
        <div class="panel-item__title--light" />
        <span>图层目录</span>
      </div>
    </div>
    <div class="layer-tree">
      <div class="tree-shadow" v-show="minimizeStatus"></div>
      <el-tree
        ref="layerTree"
        @check-change="checkChange"
        :data="layerData"
        show-checkbox
        :default-expanded-keys="defaultExpandedNode"
        :default-checked-keys="defaultCheckedNode"
        node-key="label"
        :props="{
          children: 'children',
          label: 'label',
        }"
        :render-content="renderContent"
      />
    </div>
  </div>
</template>
<script>
export default {
  name: "LayerTree",
  methods: {},
};
</script>
