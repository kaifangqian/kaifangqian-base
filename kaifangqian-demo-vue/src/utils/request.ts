import axios from "./axios"

export const requestHeader =()=>{
	return{
		"x-access-token": "",
	}
};
export function baseUrl(){
	return process.env.VUE_APP_BASE_URI;
}
export function doGet(url, params) {
	return axios({
		url: url,
		method: "get",
		params: params,
		headers: requestHeader()
	})
};
export function doPost(url, params) {
	return axios({
		url: url,
		method: "post",
		data: params,
		headers: requestHeader()
	})
}
export function doDelete(url, params) {
	return axios({
		url: url,
		method: "delete",
		params: params,
		headers: requestHeader()
	})
}
export function doPut(url: string, params: any) {
	return axios({
		url: url,
		method: "put",
		data: params,
		headers: requestHeader()
	})
}


export function doPostDowload(url, params) {
	return axios({
		url: url,
		method: "post",
		responseType: "blob",
		data: params,
		headers: requestHeader()
	})
}