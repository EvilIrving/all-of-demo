import { ref, reactive, onMounted, onUpdated, provide } from 'vue';
type propType = {
    toTsxSon: String
}
function SendValue(props: propType, context: any) {
    context.emit('tsxSon', 'iamtsSon')
    return <div>

    </div>
}


export default SendValue