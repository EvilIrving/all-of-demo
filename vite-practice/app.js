import('./model').then((res) => {
    console.log(res.default(3));
})
import { compact, isEqual } from 'lodash-es'
compact([1, 2, 3, 4, 5])
let bo = isEqual(1, true)

import { createApp } from 'vue'
import App from './App.vue'
const app = createApp(App);
app.mount("#app");
console.log(bo);