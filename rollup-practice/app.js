import f1 from './model.js'
import { compact, isEqual } from 'lodash-es'
compact([1, 2, 3, 4, 5])
let bo = isEqual(1, true)
console.log({
    res, bo
});
const res = f1(3)