/*
 * @description 开放签
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
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

/*
 * @Author: ningw 
 * @Date: 2022-06-28 14:32:55 
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2024-07-24 15:56:14

*/
import { defHttp } from '/@/utils/http/axios';

enum Api {
    //业务线发起相关
    // ContractList = '/sign/re/list',
    ContractList = '/sign/re/useList',
    ContractReInfo = '/sign/re/info/all/ru',
    ContractRuInfo = '/sign/ru/info/base',
    LinkCode = '/sign/ru/copy/link',
    SignBaseInfo = '/sign/ru/info/link',
    DeleteRu = '/sign/ru/delete',
    RevokeRu = '/sign/ru/revoke',
    RestoreRu = '/sign/ru/restore',
    TemplateDocImageIds = '/sign/template/info',

    BaseInfoSave = '/sign/ru/start/save/base',
    SealList = '/sign/ru/entSeal/enabled/list',
    IsWrite = '/sign/ru/start/is/write',
    setExpireDate = '/sign/ru/start/save/expireDate',

    DocFiles = '/sign/ru/list/file',
    DocImgs = '/sign/ru/list/image',
    DocAnnexImgs = '/sign/file/list/image/annex',
    ControlList = '/sign/ru/list/control',
    SignerList = '/sign/ru/list/signer',
    PosAndParamsSave = '/sign/ru/start/save/sign',
    BusinessStart = '/sign/ru/start',
    TplList = '/sign/template/useList',
    WriteStatus = '/sign/ru/list/operator',
    OperatorStatus = '/sign/ru/list/operator/status',
    rejectWriteOp = '/sign/ru/run/reject/write',
    submitWriteOp = '/sign/ru/run/submit/write',
    saveWriteOp = '/sign/ru/start/save/write',
    submitSignOp = '/sign/ru/run/submit/sign',
    rejectSignOp = '/sign/ru/run/reject/sign',
    HasSeal = '/sign/ru/run/get/seal',
    HasSignature = '/sign/ru/run/get/seal',
    signatureList = '/sign/person/seal/list',
    SignControlType = '/sign/ru/run/signer/identify',
    SignSealVerify = '/sign/ru/run/seal/verify',

    ListMySignJob = '/company/task/listMySignJob',
    ListMyFillInJob = '/company/task/listMyFillInJob',
    MyStastics = '/company/task/myStastics',
    VerifyCertificate = '/sign/ru/run/certificate/verify',
    UpdateCertificate = '/sign/ru/run/certificate/update',
    ImgBase64 = '/file/downloadFileBase/',
    SealInfo = '/sign/ent/seal/info',
    SealUserlist = '/sign/ru/entSeal/authorized/list',
    operatorStatus = '/sign/ru/info/operator/status',
    getSignQRKey = '/sign/ru/sign/getKey',
    checkSignQRKey = '/sign/ru/sign/checkKey',
    submitSignatureBase64AndKey = '/sign/ru/sign/signature',
    SignLinkParams = '/mes/message_3rdRecord/getParaByCode',
    SmsAuthInfo = '/sign/ru/info/operator/check',
    SignSealIds = '/sign/ru/entSeal/enabled/list/authorized',
    UserConfirmOrderNo = '/user/confirm/getOrderNo',
    UserConfirmType = '/sign/ru/run/sign/confirm',
    CallbackPage = '/sign/ru/get/callbackPageUrl',
    UserConfirmParams = '/user/confirm/confirmPara',
    ConfirmPassword = '/user/confirm/confirmByPassword',
    ConfirmByDoubleAndSms= '/user/confirm/confirmByPhoneEmail',
    ConfirmSmsCode = '/user/confirm/sendMessage',
    ConfirmEmailCode = '/user/confirm/sendEmail',
    RecordList = '/sign/ru/list/operate/record',
    RecordReportExist= '/sign/ru/report/file/exist',
    CleanUpAll= '/sign/ru/cleanup',
    DeleteComplete = '/sign/ru/completely/delete',
    ConfrimSign = '/sign/ru/run/completed/sign',
}




/**
 * @description:操作记录
 */
export function getRecordList(params) {
  return defHttp.get({ url: Api.RecordList,params }, { errorMessageMode: 'none' });
}
/**
 * @description:操作记录是否存在
 */
export function getRecordExist(params) {
  return defHttp.get({ url: Api.RecordReportExist,params }, { errorMessageMode: 'none' });
}
/**
 * @description:意愿校验手机短信
 */
export function getCodeBySms(params) {
  return defHttp.get({ url: Api.ConfirmSmsCode,params }, { errorMessageMode: 'none' });
}
/**
 * @description:意愿校验邮箱
 */
export function getCodeByEmail(params) {
  return defHttp.get({ url: Api.ConfirmEmailCode,params }, { errorMessageMode: 'none' });
}
/**
 * @description:意愿校验信息完善
 */
export function submitConfirmParams(params) {
  return defHttp.post({ url: Api.UserConfirmParams,params }, { errorMessageMode: 'none' });
}

/**
 * @description:意愿校验密码方式
 */
export function submitConfirmByPassword(params) {
  return defHttp.post({ url: Api.ConfirmPassword,params }, { errorMessageMode: 'none' });
}
/**
 * @description:意愿校验手机和密码double
 */
export function submitConfirmByDoubleAnd(params) {
  return defHttp.post({ url: Api.ConfirmByDoubleAndSms,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 根据接口订单号获取回调地址
 */
export function getCallbackPage(params) {
    return defHttp.get({ url: Api.CallbackPage, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 发起方复制链接获取短信链接code
 */
export function getCopyLinkCode(params) {
    return defHttp.get({ url: Api.LinkCode, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取订单号
 */
export function getConfirmNo(params) {
    return defHttp.get({ url: Api.UserConfirmOrderNo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取意愿校验类型
 */
export function getConfirmType(params) {
    return defHttp.get({ url: Api.UserConfirmType, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 根据key解析链接参数
 */
export function getLinkParams(params) {
    return defHttp.get({ url: Api.SignLinkParams, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 合同无权限页面获取无权限类型
 */
export function getContractNoAuthType(params) {
    return defHttp.get({ url: Api.SmsAuthInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取手写二维码key
 */
export function getSignKey(params) {
    return defHttp.get({ url: Api.getSignQRKey, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 校验手写二维码key
 */
export function getCheckSignKey(params) {
    return defHttp.get({ url: Api.checkSignQRKey, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 提交图片和二维码key
 */
export function submitSignature(params) {
    return defHttp.get({ url: Api.submitSignatureBase64AndKey, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 首页统计
 */
export function getMyStastics(params) {
    return defHttp.get({ url: Api.MyStastics, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 待我填写
 */
export function getListMyFillInJob(params) {
    return defHttp.get({ url: Api.ListMyFillInJob, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 待我签署
 */
export function getListMySignJob(params) {
    return defHttp.get({ url: Api.ListMySignJob, params }, { errorMessageMode: 'none' });
}



/**
 * @description: 判断当前操作人是否办理过以及当前文档状态
 */
export function checkOperatorStatus(params) {
    return defHttp.get({ url: Api.operatorStatus, params }, { errorMessageMode: 'none' });
}


/**
 * @description: 判断当前操作人是否可以查看文档详情
 */
export function getViewCheck(params) {
    return defHttp.get({ url: "/sign/ru/info/view/check", params }, { errorMessageMode: 'none', isTransformResponse: true });
}


/**
 * @description: 判断是否需要填写
 */
export function getIsWrite(params) {
    return defHttp.get({ url: Api.IsWrite, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取签署控件类型
 */
export function getSignControlType(params) {
    return defHttp.get({ url: Api.SignControlType, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 验证企业签章是否有效
 */
export function verifySignSeal(params) {
    return defHttp.get({ url: Api.SignSealVerify, params }, { errorMessageMode: 'none', isReturnNativeResponse: true });
}
/**
 * @description: 获取个人签名
 */
export function getSignatureList(params) {
    return defHttp.get({ url: Api.signatureList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取业务线列表
 */
export function getBusinessLine(params) {
    return defHttp.get({ url: Api.ContractList, params }, { errorMessageMode: 'none' });
}
export function getBusinessLineList(params) {
    return defHttp.get({ url: "/sign/re/queryUseList", params }, { errorMessageMode: 'none' });
}
/**
 * @description: 删除业务线实例
 */
export function deleteSignRu(params) {
    return defHttp.delete({ url: Api.DeleteRu, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 撤回
 */
export function revokeRuSignRu(params) {
    return defHttp.post({ url: Api.RevokeRu, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 恢复
 */
export function restoreRu(params) {
    return defHttp.post({ url: Api.RestoreRu, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取业务线配置详情
 */
export function getBusinessReInfo(params) {
    return defHttp.get({ url: Api.ContractReInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取业务线实例配置详情
 */
export function getBusinessRuInfo(params) {
    return defHttp.get({ url: Api.ContractRuInfo, params }, { errorMessageMode: 'none' });
}



/**
 * @description: 获取文件列表
 */
export function getDocFiles(params) {
    return defHttp.get({ url: Api.DocFiles, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取可用签章
 */
export function getEfficientSignSeal(params) {
    return defHttp.get({ url: Api.HasSeal, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取可用签名
 */
export function getEfficientSignatureList(params) {
    return defHttp.get({ url: Api.HasSignature, params }, { errorMessageMode: 'none' });
}

/**
 * @description: 获取模板文件列表
 */
export function getTplList(params) {
    return defHttp.get({ url: Api.TplList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 根据文件id获取图标id列表
 */
export function getDocImgs(params) {
    return defHttp.get({ url: Api.DocImgs, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 根据真实文件id获取图片id列表
 */
export function getAnnexImgs(params) {
    return defHttp.get({ url: Api.DocAnnexImgs, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 根据文件id获取模板id列表
 */
export function getTemplateDocImgs(params) {
    return defHttp.get({ url: Api.TemplateDocImageIds, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取控件列表
 */
export function getControlList(params) {
    return defHttp.get({ url: Api.ControlList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取参与人
 */
export function getSigners(params) {
    return defHttp.get({ url: Api.SignerList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取操作人
 */
export function getOperator(params) {
    return defHttp.get({ url: Api.WriteStatus, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取操作人(带状态)
 */
export function getOperatorStatus(params) {
    return defHttp.get({ url: Api.OperatorStatus, params }, { errorMessageMode: 'none' });
}
export function getScheduleStatus(params) {
    return defHttp.get({ url: "/sign/ru/schedule", params }, { errorMessageMode: 'none' });
}
/**
 * @description: 设置截止日期
 */
export function setRuExpireDate(params) {
    return defHttp.post({ url: Api.setExpireDate, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取签章
 */
export function getSealList(params) {
    return defHttp.get({ url: Api.SealList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 拒绝填写
 */
export function rejectWrite(params) {
    return defHttp.post({ url: Api.rejectWriteOp, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 接收方提交填写
 */
export function submitWrite(params) {
    return defHttp.post({ url: Api.submitWriteOp, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 保存填写
 */
export function saveWrite(params) {
    return defHttp.post({ url: Api.saveWriteOp, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 拒绝签署
 */
export function rejectSign(params) {
    return defHttp.post({ url: Api.rejectSignOp, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 提交签署
 */
export function submitSign(params) {
    return defHttp.post({ url: Api.submitSignOp, params }, { errorMessageMode: 'none' });
}
export function confirmSignRu(params) {
    return defHttp.post({ url: Api.ConfrimSign,params }, { errorMessageMode: 'none',isReturnNativeResponse: true });
  }
/**
 * @description: 业务线实例保存基本数据
 */
export function saveBaseInfo(params) {
    return defHttp.post({ url: Api.BaseInfoSave, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 业务线实例保存基文档位置和参数
 */
export function savePosAndParams(params) {
    return defHttp.post({ url: Api.PosAndParamsSave, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 业务线发起
 */
export function startContract(params) {
    return defHttp.post({ url: Api.BusinessStart, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 证书验证
 */
export function getVerifyCertificate(params) {
    return defHttp.get({ url: Api.VerifyCertificate, params }, { errorMessageMode: 'none'});
}
/**
 * @description: 更新证书
 */
export function getUpdateCertificate(params) {
    return defHttp.post({ url: Api.UpdateCertificate, params }, { errorMessageMode: 'none', isReturnNativeResponse: true });
}
/**
 * @description: 获取base
 */
export function getImgBase64ById(params) {
    return defHttp.get({ url: Api.ImgBase64 + params.id, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取签章name
 */
export function getSealInfo(params) {
    return defHttp.get({ url: Api.SealInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取签章对应权限人员
 */
export function getSealUserList(params) {
    return defHttp.get({ url: Api.SealUserlist, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取签署时接收方企业签章列表
 */
export function getSignSealIds(params) {
    return defHttp.get({ url: Api.SignSealIds, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 短信链接页合同详情
 */
export function getBaseRuInfo(params) {
    return defHttp.get({ url: Api.SignBaseInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 清空回收站
 */
export function cleanupAll(params) {
    return defHttp.post({ url: Api.CleanUpAll, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 彻底删除
 */
export function deleteCompleteRu(params) {
    return defHttp.post({ url: Api.DeleteComplete, params }, { errorMessageMode: 'none' });
}
/**
 * @description
 */ 
export function getMenuStastics(params) {
    return defHttp.get({ url: "/company/task/menuStastics", params }, { errorMessageMode: 'none' });
}

/**
 * @description
 */ 
export function getSignNodeConfig(params) {
    return defHttp.get({ url: "/sign/ru/run/sign/nodeConfig", params }, { errorMessageMode: 'none' });
}
