package cn.com.authDemo.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Package: cn.com.bluemoon.common.util
 * File: CommonFunction.java
 * Author: nj
 * Date: 2017年3月22日
 * Description:
 * 封装 方法的开始（beforeProcess）、结束（afterProcess）、 异常抛出（genErrorMessage）
 * 调用方式
 * JSONObject json = new JSONObject();
 json.put("1", "2");
 json.put("3", "2");
 json.put("2", "2");

 Object[] paraObjs = CommonFunction.beforeProcess(logger, json);

 try {
 int i = 1;
 int k = i / 0;
 } catch (Exception e) {
 String message = CommonFunction.genErrorMessage(logger, e, paraObjs);
 //System.out.println(message);
 }

 CommonFunction.afterProcess(logger, json);
 */
public class CommonFunction {

    static SimpleDateFormat genernalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * @description 输出"方法名 start"，标示调用方法开始，Object...参数将转换为Object[]用于打印Exception
     * @param myLogger myLogger 指定logger
     * @param obj Object... obj 参数n
     * @return Object[]
     */
    public static Object[] beforeProcess(Logger myLogger, Object... obj) {
        String methodName = getRunningMethodName("beforeProcess");
        myLogger.info(getThreadId() + methodName + " start ["
                + parserObjectsToString(obj) + "]");

        if (obj != null) {
            Object[] retObj = new Object[obj.length];
            int i = 0;
            for (Object v : obj) {
                retObj[i] = v;
                i++;
            }
            return retObj;
        }

        return null;
    }


    /**
     * @description 输出"方法名 end"，标示调用方法结束，Object...参数将转换为Object[]用于打印Exception
     * @param myLogger Logger myLogger 指定logger
     * @param obj Object... obj 参数n
     * @return Object[]
     */
    public static void afterProcess(Logger myLogger, Object... obj) {
        String methodName = getRunningMethodName("afterProcess");
        myLogger.info(getThreadId() + methodName + " end");
    }


    /**
     * @description 获取线程Id
     * @return
     */
    public static String getThreadId() {
        return "(" + Thread.currentThread().getName() + ") ";

    }

    /**
     * @description 获取当前运行方法的名字
     * @param level Integer level 获取方法名在trace中所在的层
     * @return String
     */
    public static String getRunningMethodName(Integer level) {
        return Thread.currentThread().getStackTrace()[level].getMethodName();
    }

    /**
     * @description 获取当前运行方法的名字
     * @param currentMethodName String level 获取方法名在trace中所在的层
     * @return String
     */
    public static String getRunningMethodName(String currentMethodName) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        for (int i=0; i < stackTraceElement.length; i++) {
            if (currentMethodName != null && currentMethodName.equals(stackTraceElement[i].getMethodName())) {
                //避免beforeProcess(Object... obj)调用beforeProcess(Logger myLogger, Object... obj)多了一个子方法的情况
                if (!currentMethodName.equals(stackTraceElement[i + 1].getMethodName()))
                    return stackTraceElement[i + 1].getMethodName();
            }
        }
        return "[Unknow Method Name]";
    }

    /**
     * @description 获取当前运行方法的名字
     * @return String
     */
    public static String getRunningMethodName() {
        return getRunningMethodName(3);
    }

    /**
     * @description 输出业务日志信息
     * @param myLogger Logger myLogger 指定logger
     * @param errorMessage String message
     */
    public static void genBusinessMessage(Logger myLogger, String errorMessage, String errorCode) {
        String methodName = getRunningMethodName("genBusinessMessage");
        myLogger.info(getThreadId() + methodName + ": " + errorMessage + "|" + errorCode);
    }



    /**
     * @description 将parameters和参数值格式化字符串后输出到日志
     * @return String
     */
    public static String getParameterToStringWithParaName(String[] paraNames, Object... object) {
        String paraStr = "error with parameters = {";
        paraStr = paraStr + parserObjectsToStringWithParaName(paraNames, object);
        return paraStr + "}";
    }

    /**
     * @description 将参数值字符串格式化后输出到日志
     * @return String
     */
    public static String getParameterToString(Object... object) {
        String paraStr = getRunningMethodName("getParameterToString") + " processes with parameters = {";
        paraStr = paraStr + parserObjectsToString(object);
        return paraStr + "}";
    }

    /**
     * @description 将parameters和参数值转为字符串形式返回
     * @return String
     */
    public static String parserObjectsToStringWithParaName(String[] paraNames, Object... object) {
        if (paraNames ==null || object == null)
            return "";

        if (paraNames.length != object.length) {
            return "Set up parameters issue or interface not found: parameters counts: " + object.length
                    + "/@WebParam counts:" + paraNames.length + "." + getParameterToString(object);
        }

        String paraStr = "";
        int j = 0;
        for (Object i : object) {
            //判断String对象
            if (i == null)
                paraStr = paraStr + paraNames[j] + "=null, ";
            else if (i instanceof String)
                paraStr = paraStr + paraNames[j] + "=" + i + ", ";
                //判断Integer对象
            else if (i instanceof Integer)
                paraStr = paraStr + paraNames[j] + "=" + String.valueOf(i) + ", ";
            else if (i instanceof Long)
                paraStr = paraStr + paraNames[j] + "=" + String.valueOf(i) + ", ";
                //判断Calendar对象
            else if (i instanceof Calendar) {
                Calendar cal = (Calendar) i;
                paraStr = paraStr + paraNames[j] + "=" + genernalDateFormat.format(cal.getTime()) + ", ";
                //判断List对象
            } else if (i instanceof List) {
                List<?> list = (List<?>) i;
                if (i != null && list.size() > 0) {
                    paraStr = paraStr+ "[";
                    for (int p=0; p<list.size(); j++) {
                        paraStr = paraStr + paraNames[p] + "=" + parserObjectsToString(list.get(j)) + ", ";
                    }
                    paraStr = removeRightStr(paraStr, ", ", 2);
                    paraStr = paraStr+ "], ";
                } else {
                    paraStr = paraStr + paraNames[j] + "=" + "null, ";
                }
                //判断为null
            } else if (i instanceof JSONObject) {
                paraStr = paraStr + paraNames[j] + "=" + ((JSONObject) i).toString() + ", ";
            } else if (i instanceof JSONArray) {
                paraStr = paraStr + paraNames[j] + "=" + ((JSONArray) i).toString() + ", ";
            } else if (i instanceof Map) {
                JSONObject json = JSONObject.fromObject(i);
                paraStr = paraStr + json.toString() + ", ";
            } else {
                JSONObject json =JSONObject.fromObject(i);
                paraStr = paraStr + paraNames[j] + "=" + json.toString() + ", ";
            }
            j++;
        }
        paraStr = removeRightStr(paraStr, ", ", 2);
        return paraStr;
    }

    public static String removeRightStr(String source, String removeStr, Integer len) {
        if (source != null && removeStr != null
                && (removeStr.equals(source.substring(source.length()-len, source.length()))))
            source = source.substring(0, source.length() - len);
        return source;
    }


    /**
     * @description 将输入参数以字符串形式返回
     * @return String
     */
    public static String parserObjectsToString(Object... object) {
        String paraStr = "";
        if(object != null && object.length > 0){
            for (Object i : object) {
                //判断String对象
                if (i == null)
                    paraStr = paraStr + "null, ";
                else if (i instanceof String)
                    paraStr = paraStr + i + ", ";
                    //判断Integer对象
                else if (i instanceof Integer)
                    paraStr = paraStr + String.valueOf(i) + ", ";
                else if (i instanceof Long)
                    paraStr = paraStr + String.valueOf(i) + ", ";
                else if (i instanceof BigInteger)
                    paraStr = paraStr + String.valueOf(i) + ", ";
                    //判断Calendar对象
                else if (i instanceof Calendar) {
                    Calendar cal = (Calendar) i;
                    paraStr = paraStr+ genernalDateFormat.format(cal.getTime()) + ", ";
                    //判断List对象
                } else if (i instanceof List) {
                    List<?> list = (List<?>) i;
                    if (i != null && list.size() > 0) {
                        paraStr = paraStr+ "[";
                        for (int j=0; j<list.size(); j++) {
                            paraStr = paraStr + parserObjectsToString(list.get(j)) + ", ";
                        }
                        paraStr = removeRightStr(paraStr, ", ", 2);
                        paraStr = paraStr+ "], ";
                    } else {
                        paraStr = paraStr + "null, ";
                    }
                    //判断为null
                } else if (i instanceof JSONObject) {
                    paraStr = paraStr + ((JSONObject) i).toString() + ", ";
                } else if (i instanceof JSONArray) {
                    paraStr = paraStr + ((JSONArray) i).toString() + ", ";
                } else {
                    JSONObject json = JSONObject.fromObject(i);
                    paraStr = paraStr + json.toString() + ", ";
                }
            }
            paraStr = removeRightStr(paraStr, ", ", 2);
        }
        return paraStr;
    }

    /**
     * @description 输出异常信息，包括参数（参数=xxx)和异常详细堆栈
     * @param myLogger Logger myLogger 指定logger
     * @param e Exception e
     * @param obj Object... obj 参数n
     * @return Object[]
     */
    public static String genErrorMessage(Logger myLogger, Exception e, Object... obj) {
        String methodName = getRunningMethodName("genErrorMessage");
        String[] interfaceParamNames = null;
        interfaceParamNames = getInterfaceParamNames("genErrorMessage");

        if (interfaceParamNames != null )
            myLogger.error(getThreadId() + methodName + " " + getParameterToStringWithParaName(interfaceParamNames, obj));
        else
            myLogger.error(getThreadId() + methodName + " error and " + getParameterToString(obj));

        String errorMessage = "";

        if (e != null) {
            if (e instanceof JSONException) {
                errorMessage = methodName + " processes with JSONException:\n" + e.getMessage();
            } else
            if (e.getClass().getName().indexOf("DataServiceFault") > -1) {
                errorMessage = methodName + " processes with DataServiceFault:\n" + e.getMessage();
            } else
            if (e instanceof IllegalArgumentException) {
                errorMessage = methodName + " processes with IllegalArgumentException:\n" + e.getMessage();
            } else
            if (e instanceof ParseException) {
                errorMessage = methodName + " processes with ParseException:\n" + e.getMessage();
            } else
            {
                errorMessage = methodName + " processes with Exception:\n" + e.getMessage();
            }
        }
        myLogger.error(getThreadId() + errorMessage, e);
        return errorMessage;
    }

    /**
     * 传入全类名获得对应类中所有方法名和参数名
     * @param currentMethodName
     */
    private static String[] getInterfaceParamNames(String currentMethodName) {
        String[] stringList = null;
        try {
            StackTraceElement stackTraceElement = null;
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (int i=0; i < stackTraceElements.length; i++) {
                if (currentMethodName != null && currentMethodName.equals(stackTraceElements[i].getMethodName())) {
                    //避免beforeProcess(Object... obj)调用beforeProcess(Logger myLogger, Object... obj)多了一个子方法的情况
                    if (!currentMethodName.equals(stackTraceElements[i + 1].getMethodName())) {
                        stackTraceElement = stackTraceElements[i + 1];
                        break;
                    }
                }
            }

            if (stackTraceElement != null) {
                String implClassName = stackTraceElement.getClassName();
                String methodName = stackTraceElement.getMethodName();
                Class<?> implClazz = Class.forName(implClassName);
                @SuppressWarnings("rawtypes")
                Class[] interfaces = implClazz.getInterfaces();
                if (interfaces.length > 0) {
                    Class<?> interfaceClazz = interfaces[0];
                    Method[] methods = interfaceClazz.getMethods();
                    for (Method method : methods) {
                        if (methodName!=null && methodName.equals(method.getName())) {
                            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                            if (parameterAnnotations.length > 0) {
                                stringList = new String[parameterAnnotations.length];
                                int i = 0;
                                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                                    for (Annotation parameterAnno : parameterAnnotation) {
                                        //WebParam webParam = (WebParam) parameterAnno;
                                        stringList[i] = parameterAnno.toString();
                                        i++;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        return stringList;
    }
}
