<template>
  <div class="area-select">
    <el-select
      v-model="type"
      placeholder="请选择"
      @change="typeChange"
    >
      <el-option label="行政区划">行政区划</el-option>
      <el-option label="流域">流域</el-option>
    </el-select>

    <el-select
      v-model="typeVal"
      placeholder="请选择"
      @change="typeValChange"
    >
      <el-option label="行政区划">行政区划</el-option>
    </el-select>
  </div>
</template>

<script>
export default {
  name: 'AreaSelect',
  data() { 
    return {
      type:"行政区划",
      typeVal:"",

    }
  },
  methods: {
    typeChange(val){

    },
    typeValChange(val){

    }
  }
 }
</script>

<style lang="scss" scoped>
  .area-select{

  }
</style>