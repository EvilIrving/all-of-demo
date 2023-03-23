// const testService = "http://192.168.0.100/api";
// const testService = "http://192.168.31.167:8090/"; //hzh
// const testService = "http://192.168.0.29:8090/"; //lxl
// const testService = "http://192.168.0.32:8090/"; //lxl
const testService = "http://192.168.0.101:8090/"; //hc

// const service = "http://192.168.31.30:8090/";//hzh

const Service = "https://117.149.228.207:8081/api/"; // online

const config = {
	baseUrl: process.env.NODE_ENV === "production" ? '/api/' : Service,
	gaoDeKey: "f549bf6bed485a81e61a4e03117d8630",
	fileUploadUrl: process.env.NODE_ENV === "production" ? "'/api/'auth-service/upload" : Service + "auth-service/upload",
};
export default config;