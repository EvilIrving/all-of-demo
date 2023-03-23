 const testService = "http://192.168.2.123:8090";
// const testService = "https://slgh.lucheng.gov.cn/api";
//const testService = "http://192.168.2.210:8090";

const config = {
    baseUrl: process.env.NODE_ENV === "production" ? '/api' : testService,
    gaoDeKey: 'f549bf6bed485a81e61a4e03117d8630',
    fileUploadUrl: process.env.NODE_ENV === "production" ? "/api/upload" : "/api/upload",
};
export default config