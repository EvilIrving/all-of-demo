import { ref, reactive, defineComponent } from 'vue';

const a = ref('feelo')

const arr = reactive([1, 2, 3, 4, 5])

function Test() {
    return <div>
        <h1>{a}</h1>
        {
            arr.forEach((item, index) => {
                if (index % 2)
                    return <h2>{item}</h2>
            })
        }
    </div>
}

export default Test

// defineComponent({
//     setup() {
//         defineProps({
//             msg: String
//         }),
//         const c = computed(() => { })

//     }
// }) 
