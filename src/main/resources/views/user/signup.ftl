<!DOCTYPE html>
<#--引入公共的css，js-->
<#include "common/base.ftl">
<html>
<head>


</head>
    <body>
        <section class="content">
            <div class="row">
                <!-- BEGIN SAMPLE TABLE PORTLET-->
                <div class="col-md-12">
                    <!-- BEGIN SAMPLE TABLE PORTLET-->
                    <div class="box-body" style="padding-bottom:0px;">
                        <div class="panel panel-default">
                            <div class="panel-body">
                                <form id="signup_form" class="form-inline" role="form">

                                    <div class="form-group has-feedback" style="margin-top:15px;">
                                        <input class="form-control required" type="text" placeholder="用户昵称" aria-describedby="basic-addon2" name="username"/>

                                    </div>

                                    <div class="form-group" style="margin-top:15px;">

                                        <input class="form-control required" type="text" placeholder="邮箱" name="email"/>
                                    </div>
                                    <div class="form-group" style="margin-top:15px; ">

                                        <input class="form-control required" type="password" placeholder="密码"  name="pwd"/>
                                    </div>
                                    <div class="form-group" style="margin-top:15px; ">
                                        <input class="form-control required" type="password" placeholder="确认密码" name="confirmpwd"/>
                                    </div>

                                    <div class="form-actions">
                                        <div class="row">
                                            <div align="center">
                                                <button type="button" style="margin-left:50px" id="btn_submit" class="btn btn-success"">
                                                <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;&nbsp;确定
                                                </button>
                                                <button type="button" style="margin-left:50px" id="btn_cancel" class="btn btn-warning">
                                                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>&nbsp;&nbsp;取消
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                </form>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </body>


<style>
    #signup_form div{
        display: block;
    }
    /*#signup_form label{*/
        /*padding-left: 50px;*/
        /*float: left;*/
    /*}*/

    /*#signup_form input{*/
        /*padding-left: 50px;*/
        /*float: left;*/
    /*}*/

    /*padding-left:60px   因为图标是有一个宽度，然后图标有一个left:20px   图标的宽度34px， 估计一下  设置差不多60px， 刚好让文字不被图标遮挡*/
    /*.form-center .username input,.form-center .password input{*/
        /*height: 50px;*/
        /*padding-left: 60px;*/
    /*}*/


    /*.form-control-feedback {*/
        /*line-height: 50px;*/
        /*left: 20px;*/
    /*}*/
    .fa{
        display: inline-block;
        top: 27px;
        left: 10px;
        position: relative;
        color: #ccc;
    }

    .form-horizontal .has-feedback .form-control-feedback{
        padding-top: 20px;
    }



</style>
<script>
    $(function () {
        $("#signup_form").bootstrapValidator({
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
                username: {
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


                pwd: {
                    message: '密码不能为空',
                    validators: {
                        notEmpty: {//检测非空,radio也可用
                            message: '密码不能为空'
                        },
                        stringLength: {
                            min: 4,
                            max: 20,
                            message: '密码长度不够'
                        },
                        different: {
                            field: 'username',
                            message: '密码不能与昵称相同'
                        }
                        // emailAddress: {//验证email地址
                        //     message: '不是正确的email地址'
                        // },
                    }
                },
                confirmpwd: {
                    message: '请输入确认密码',
                    validators: {
                        notEmpty: {
                            message: '请输入确认密码'
                        },
                        identical: {
                            field: 'pwd',
                            message: '两次输入的密码不同'
                        },
                        different: {
                            field: 'username',
                            message: '密码不能与昵称相同'
                        }
                    }
                },

                email: {
                    message: '邮箱不能为空',
                    validators: {
                        notEmpty: {//检测非空,radio也可用
                            message: '邮箱不能为空'
                        },

                        emailAddress: {//验证email地址
                            message: '请输入正确的email地址'
                        }
                    }
                }
            }
        });

        $("button[id='btn_submit']").click(function(){
            $("#signup_form").bootstrapValidator('validate');//提交验证
            if ($("#signup_form").data('bootstrapValidator').isValid()) {
                var userName = $("input[name='username']").val();
                var email = $("input[name='email']").val();
                var pwd = $("input[name='pwd']").val();
                var confirmpwd = $("input[name='confirmpwd']").val();
                if (!isNull(pwd) && !isNull(confirmpwd)
                && pwd != confirmpwd) {
                    layer.alert("两次输入的密码不一致");
                    return false;

                }

                var json = JSON.stringify({userName: userName, email : email, pwd: md5(pwd)});
                $.ajax({
                    type: "POST",
                    url: "${ctx}/user/signup",
                    contentType: "application/json; charset=utf-8",
                    data: json,
                    dataType: "json",
                    success: function (data) {
                        var responseCode = data.responseCode;
                        var responseMsg = data.responseMsg;
                        var isSuccess = data.isSuccess;
                        if (isSuccess && responseCode == 0) {
                            layer.alert("注册成功!", function(){
                                var index = parent.layer.getFrameIndex(window.name);
                                parent.isSignUp("1");
                                parent.layer.close(index);
                                //请求成功
                                window.location.href = "${ctx}/index";
                            })

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

    $("#btn_cancel").click(function(){
        var index = parent.layer.getFrameIndex(window.name);
        parent.isSignUp("-1");
        parent.layer.close(index);
    });
</script>

</html>

