<template>
  <div class="workplace-collect">
    <div class="workplace-collect__content">
      <MyTrack />
    </div>
  </div>
</template>

<script>
import MyTrack from "./pages/MyTrack.vue";
export default {
  name: 'Track',
  data() {
    return {
      
    }
  },
  components: {
    MyTrack
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
    padding-bottom: 16px;
  }
}
</style>