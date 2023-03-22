import "./app.css"
import img from './img/IU_Pics (9).jpg'
import num from './index.tsx'
import ls from 'lodash'
new Image().src = img;

let r = require.context('./mode', false, /.js/)
r.keys().forEach(item => {
    console.log(item.default);
})
    (() => {
        // console.log(num);
        console.log(123);
    })