export function getWindowHeight() {
	var h = document.documentElement.clientHeight || document.body.clientHeight;
	window.sessionStorage.setItem("clientHeight", h);
	return h;
}


export function saveBlob(data, filename) {
	const url = URL.createObjectURL(new Blob([data]));
	const link = document.createElement("a");
	link.href = url;
	link.setAttribute("download", filename);
	link.download = filename;
	link.hidden = true;
	document.body.appendChild(link);
	link.click();
	setTimeout(() => {
		document.body.removeChild(link);
		window.URL.revokeObjectURL(url);
	}, 0);
}

// base64 转 Blob
export function base64ToBlob(base64) {
    let binary = atob(base64);
    let array = [];
    for (let i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], { type: "image/png" });
}