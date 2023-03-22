<script setup lang="ts">
import { ref, reactive, onMounted, onUpdated, defineEmits } from 'vue';

const num1 = ref(2)
const num2 = ref(2)

onMounted(() => {
  console.log('mounted');
})

onUpdated(() => {
  console.log('updated');
})

setTimeout(() => {
  num1.value = 999
}, 1000);

var timer = setTimeout(() => {
  num2.value = 2323
}, 1000)

console.log('create');

</script>

<template>
  <div>
    {{ num1 }}
    <p>
      {{ num2 }}
    </p>
  </div>

</template>

<style lang="scss" scoped>
</style>