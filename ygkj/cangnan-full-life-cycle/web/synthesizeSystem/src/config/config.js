const testService = 'http://192.168.0.32:8090/';
const Service = 'https://sgpt.wzsl.com:8081/api/';
const config = {
	baseUrl: process.env.NODE_ENV === "production" ? Service : Service,
	gaoDeKey: "f549bf6bed485a81e61a4e03117d8630",
	fileUploadUrl: process.env.NODE_ENV === "production" ? "/api/auth-service/upload" : Service + "auth-service/upload",
};
export default config;
