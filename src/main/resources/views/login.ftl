<!DOCTYPE html>
<#--引入公共的css，js-->
<#include "common/base.ftl">
<html>
    <head>
        <script type="text/javascript" src="${ctx}/js/bootstrapValidator/bootstrapValidator.min.js"></script>
        <script type="text/javascript" src="${ctx}/js/md5.js"></script>

        <link rel="stylesheet" href="${ctx}/js/bootstrapValidator/bootstrapValidator.min.css">

    </head>
    <body>
        <div class="container">
            <div class="form row">
                <div class="form-horizontal col-md-offset-3" id="login_form">
                    <h3 class="form-title">请登录</h3>
                    <div class="col-md-9">
                        <div class="form-group">
                            <i class="fa fa-user fa-lg"></i>
                            <input class="form-control required" type="text" placeholder="Username" id="username" name="username" autofocus="autofocus" maxlength="20"/>
                        </div>
                        <div class="form-group">
                            <i class="fa fa-lock fa-lg"></i>
                            <input class="form-control required" type="password" placeholder="Password" id="password" name="password"/>
                        </div>
                        <div class="form-group">
                            <label class="checkbox">
                                <input type="checkbox" name="remember" value="1"/>记住我
                            </label>
                        </div>
                        <div class="form-group col-md-offset-9">
                            <button type="button" class="btn btn-success pull-right" name="submit" id="btn_submit">登录</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
<style>
    body{
        background: url("${ctx}/images/1.jpg");
        animation-name:myfirst;
        animation-duration:12s;
        /*变换时间*/
        animation-delay:2s;
        /*动画开始时间*/
        animation-iteration-count:infinite;
        /*下一周期循环播放*/
        animation-play-state:running;
        /*动画开始运行*/
    }
    @keyframes myfirst
    {
        0%   {background:url("${ctx}/images/1.jpg");}
        34%  {background:url("${ctx}/images/2.jpg");}
        67%  {background:url("${ctx}/images/3.jpg");}
        100% {background:url("${ctx}/images/1.jpg");}
    }
    .form{background: rgba(255,255,255,0.2);width:400px;margin:120px auto;}
    /*阴影*/
    .fa{display: inline-block;top: 27px;left: 6px;position: relative;color: #ccc;}
    input[type="text"],input[type="password"]{padding-left:26px;}
    .checkbox{padding-left:21px;}

</style>

<script>
   $(function(){

       //表单校验
       $("#login_form").bootstrapValidator({
           // live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
           // excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
           // submitButtons: '#btn_submit',//指定提交按钮，如果验证失败则变成disabled，但我没试成功，反而加了这句话非submit按钮也会提交到action指定页面
           // message: '通用的验证失败消息',//好像从来没出现过
           feedbackIcons: {//根据验证结果显示的各种图标
               valid: 'glyphicon glyphicon-ok',
               invalid: 'glyphicon glyphicon-remove',
               validating: 'glyphicon glyphicon-refresh'
           },
           //校验字段
           fields: {
               username:{
                   message: '请输入用户名',
                   validators: {
                       notEmpty: {//检测非空,radio也可用
                           message: '请输入用户名'
                       },
                       stringLength: {
                           min: 4,
                           max: 8,
                           message: '用户名长度为4-8位'
                       },
                       regexp: {//正则验证
                           regexp: /^[a-zA-Z0-9_\.]+$/,
                           message: '用户名不符要求'
                       }
                       // emailAddress: {//验证email地址
                       //     message: '不是正确的email地址'
                       // },
                   }
               },
               password:{
                   message: '密码不能为空',
                   validators: {
                       notEmpty: {//检测非空,radio也可用
                           message: '密码不能为空'
                       },
                       stringLength: {
                           min: 4,
                           max: 20,
                           message: '密码长度不够'
                       }
                       // emailAddress: {//验证email地址
                       //     message: '不是正确的email地址'
                       // },
                   }
               },
               text: {
                   validators: {
                       notEmpty: {//检测非空,radio也可用
                           message: '文本框必须输入'
                       },
                       stringLength: {//检测长度
                           min: 6,
                           max: 30,
                           message: '长度必须在6-30之间'
                       },
                       regexp: {//正则验证
                           regexp: /^[a-zA-Z0-9_\.]+$/,
                           message: '所输入的字符不符要求'
                       },
                       remote: {//将内容发送至指定页面验证，返回验证结果，比如查询用户名是否存在
                           url: '指定页面',
                           message: 'The username is not available'
                       },
                       different: {//与指定文本框比较内容相同
                           field: '指定文本框name',
                           message: '不能与指定文本框内容相同'
                       },
                       emailAddress: {//验证email地址
                           message: '不是正确的email地址'
                       },
                       identical: {//与指定控件内容比较是否相同，比如两次密码不一致
                           field: 'confirmPassword',//指定控件name
                           message: '输入的内容不一致'
                       },
                       date: {//验证指定的日期格式
                           format: 'YYYY/MM/DD',
                           message: '日期格式不正确'
                       },
                       choice: {//check控件选择的数量
                           min: 2,
                           max: 4,
                           message: '必须选择2-4个选项'
                       }
                   }
               }
           }

       });

       $("button[name='submit']").click(function(){
           $("#login_form").bootstrapValidator('validate');//提交验证
           if ($("#login_form").data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
               var userName = $("#username").val();
               var password = $("#password").val();
               password = md5(password);
               var postJSON = JSON.stringify({userName: userName, password: password})
               $.ajax({
                   type: "POST",
                   url: "${ctx}/user/login",
                   contentType: "application/json; charset=utf-8",
                   data: postJSON,
                   dataType: "json",
                   success: function (data) {
                       var responseCode = data.responseCode;
                       var responseMsg = data.responseMsg;
                       var isSuccess = data.isSuccess;
                       if (isSuccess && responseCode == 0) {
                           //请求成功
                           window.location.href = "${ctx}/index";
                       } else {
                           layer.alert(responseMsg);
                       }
                   },
                   error: function (message) {
                       $("#request-process-patent").html("提交数据失败！");
                   }
               });
           }



       });
   });
</script>
</html>
