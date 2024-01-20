
//设置基础字号
const baseSize = 100
//设置基本缩放比
let scaleRate = 1;

let fontSize = 0;
// 设置 rem 函数
function setRem() {
  // 当前页面宽度相对于 1920 宽的缩放比例，可根据自己需要修改。
  let clientWidth = document.documentElement.clientWidth;
  if(clientWidth < 1366){
	  clientWidth = 1366;
  }
  const scale = clientWidth / 1920
  console.log("setRem",clientWidth,scale);
  
  scaleRate = scale
//   if (scaleRate > 1.5) {
//     store.commit("newScreen/setScaleRate", 1.5)
//   } else if (scaleRate < 0.5) {

//     store.commit("newScreen/setScaleRate", 0.5)
//   } else {
//     store.commit("newScreen/setScaleRate", scaleRate)
//   }
  // 设置页面根节点字体大小
  if (clientWidth > 2880) {
    document.documentElement.style.fontSize = 14 + 'px'
  }   else {
    document.documentElement.style.fontSize = (baseSize * scale) + 'px'
  }
}
// 初始化
setRem()
// 改变窗口大小时重新设置 rem
// window.onresize = function () {
//   setRem()
// }
window.addEventListener('resize',function(){
	setRem()
})
export default scaleRate;