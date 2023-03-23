<template>
  <div class="workplace-collect">
    <div class="workplace-collect__content">
      <CollectTable/>
    </div>
  </div>
</template>

<script>
import CollectTable from "./pages/CollectTable.vue";
export default {
  name: 'Collect',
  data() { 
    return {

    }
  },
  components:{
    CollectTable
  }
 }
</script>

<style lang="scss" scoped>
  .workplace-collect{
    width: 100%;
    height: 620px;
    .workplace-collect__content{
      width: 100%;
      height: 100%;
    }
  }
</style>