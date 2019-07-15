/**
 * Created by jiachenpan on 16/11/18.
 */
import idCardNoUtil from 'utils/idCardNoUtil'

/* 是否是公司邮箱*/
export function isWscnEmail(str) {
  const reg = /^[a-z0-9](?:[-_.+]?[a-z0-9]+)*@wallstreetcn\.com$/i;
  return reg.test(str.trim());
}

/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
  return urlregex.test(textval);
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/;
  return reg.test(str);
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/;
  return reg.test(str);
}

/* 大小写字母*/
export function validatAlphabets(str) {
  const reg = /^[A-Za-z]+$/;
  return reg.test(str);
}


export function validateNumber(str) {
  const reg = /^\+?[1-9][0-9]*$/;
  return reg.test(str);
}

/* 验证小数 */
export function checkDecimals(rule, value, callback) {
  if(!value){callback()}
  let reg = /^[-+]?\d+\.\d+$/;
  if (!reg.test(value)) {
    callback(new Error('请输入正确的小数'))
  }else{
    callback()
  }
  return reg.test(value);
}

/* 验证是否是正整数或小数 */
export function validateDecimals(rule, value, callback) {
  if(!value){callback()}
  let reg =/^[-+]?\d+\.\d+$/ ;
  let reg2 = /^\+?[1-9][0-9]*$/;
  if (!reg.test(value) && !reg2.test(value) ) {
    callback(new Error('请输入正确的数值'))
  }else{
    callback()
  }
  return reg.test(value);
}

/* 验证经纬度 */
export function checkLonAndLat(rule, value, callback) {
  if(!value){callback()}
  // 经度： -180.0～+180.0（整数部分为0～180，必须输入1到6位小数）
  let lng = /^[-+]?(0?\d{1,2}\.\d{1,6}|1[0-7]?\d{1}\.\d{1,6}|180\.0{1,6})$/;
  // 纬度： -90.0～+90.0（整数部分为0～90，必须输入1到6位小数）
  let lat = /^[-+]?([0-8]?\d{1}\.\d{1,6}|90\.0{1,6})$/;
  if (!value || value.indexOf(',') < 0 || !lng.test(value.split(',')[0]) || !lat.test(value.split(',')[1])) {
    callback(new Error('坐标不正确，格式:180.000000,90.000000'))
  }else{
    callback()
  }
}

export function validateEmail(str) {

  const myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
  return myreg.test(str);
}

export function validateMobile(str) {

  const mymobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
  return mymobile.test(str);
}


//验证手机号(手机号不是必填项时)
export function validatePhone2(rule, value, callback) {
  let reg = /^1[3|4|5|7|8][0-9]\d{8}$/
  if (!value) {
     callback()
  } else if (!reg.test(value)) {
    callback(new Error('请输入正确的11位手机号码'))
  } else {
    callback()
  }
  return reg.test(value);
}

//验证身份证(不是必填时)
export function vaildateIdCard(rule, idCardNo, callback) {
  //15位和18位证件号码的基本校验
  if(!idCardNo) return ;
  var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
  if(!check) return callback(new Error('请输入正确的证件号码'));
  //判断长度为15位或18位
  if(idCardNo.length==15){
    if(!idCardNoUtil.check15IdCardNo(idCardNo)){
      callback(new Error('请输入正确的证件号码'));
    }

  }else if(idCardNo.length==18){
     if(!idCardNoUtil.check18IdCardNo(idCardNo))
       callback(new Error('请输入正确的证件号码'));
  }else{
     callback();
  }
}

//验证姓名
export function vaildateName(rule, val, callback){
  var reg =/^[\u4E00-\u9FA5\uf900-\ufa2d·s]{2,20}$/;// /^[\一-\龥]*$/g , reg2 = /^[A-Za-z]+$/;

  if(!reg.test(val)){
    callback(new Error('请输入正确的中文姓名'));
  }else{
    callback();
  }
}
//校验企业统一信用代码
export function CheckSocialCreditCode(rule, Code, callback) {
  var patrn = /^[0-9A-Z]+$/;
  //18位校验及大写校验
  if ((Code.length != 18) || (patrn.test(Code) == false)) {
    callback(new Error('请输入正确的统一社会信用编码'));
  } else {
    var Ancode;//统一社会信用代码的每一个值
    var Ancodevalue;//统一社会信用代码每一个值的权重
    var total = 0;
    var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子
    var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
    //不用I、O、S、V、Z
    for (var i = 0; i < Code.length - 1; i++) {
      Ancode = Code.substring(i, i + 1);
      Ancodevalue = str.indexOf(Ancode);
      total = total + Ancodevalue * weightedfactors[i];
      //权重与加权因子相乘之和
    }
    var logiccheckcode = 31 - total % 31;
    if (logiccheckcode == 31) {
      logiccheckcode = 0;
    }
    var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
    var Array_Str = Str.split(',');
    logiccheckcode = Array_Str[logiccheckcode];
    var checkcode = Code.substring(17, 18);
    if (logiccheckcode != checkcode) {
      callback(new Error('请输入正确的统一社会信用编码'));
    } else {
      callback();
    }
  }
}
//校验邮政编码
export function postalCodeValidator(rule, value, callback){
  var reg =/^[1-9]\d{5}(?!\d)$/;//
  if(value && !reg.test(value)){
    callback(new Error('请输入正确的邮政编码'));
  }else{
    callback();
  }
}

//值 不能为空格或空串
export function spacelValidator(rule, value, callback) {
  if (!value) {
    callback("不能为空");
  } else if (/^\s+$/gi.test(value)) {
    return callback("不能为空");
  } else {
    callback();
  }
}
