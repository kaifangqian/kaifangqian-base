/**
 * @description : 发送手机验证码
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
async function sendPhoneCode() {
	    try {
	        await authForm.value.validate("phone");
	        if (duration.value === initDuration) {
	            duration.value = initDuration;
	            const params = {
	                phone: formData.value.phone,
	                type: "login1",
	            }
	            const response = await Api.sendCode(params)
	            if (response.code == 200) {
	                formData.value.captchaKey = response.result;
	                console.log("send res:", response);
	                Notify({ type: 'success', message: '短信发送成功', duration: 1000 });
	                delayCountDown();
	            } else {
	                Notify({ type: 'warning', message: '获取短信验证码失败', duration: 1000 });
	            }
	        } else {
	            return;
	        }
	    } catch (e) {
	        console.error("form field error:", e);
	    }
	}
	const initDuration = 3;
	const duration = ref(3)
	const countdownText = ref("获取验证码");
	const interval = ref<any>(null);
	function delayCountDown() {
	    if (duration.value == 0) {
	        clearTimeout(interval.value);
	        duration.value = 3;
	        countdownText.value = "获取验证码"
	    } else if (duration.value > 0) {
	        clearTimeout(interval.value);
	        countdownText.value = duration.value + "秒后重新获取"
	        duration.value--;
	        interval.value = setTimeout(function () {
	            delayCountDown()
	        }, 1000)
	    }
	}