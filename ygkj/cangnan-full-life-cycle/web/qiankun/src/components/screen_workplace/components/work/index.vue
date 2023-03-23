<template>
  <div class="workplace-collect">
    <div class="workplace-collect__content">
      <WorkTable />
    </div>
  </div>
</template>

<script>
import WorkTable from "./pages/WorkTable.vue";
export default {
  name: 'Work',
  data() {
    return {

    }
  },
  components: {
    WorkTable
  }
}
</script>

<style lang="scss" scoped>
.workplace-collect {
  width: 100%;
  height: 620px;
  .workplace-collect__content {
    width: 100%;
    height: 100%;
  }
}
</style>