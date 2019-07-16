
----

Android项目实战示例

Login Component

----

###	登陆模块

####	1.	涉及的技术
    RxJava
    Retrofit
    OKHttp
    Glide

    MVP

    other:

####	2.	描述

	1.	使用:

		RxJava
		Retrofit
		OKHttp

		MVP
		DesignPatterns

		SharedPreference
		LoadingStatusLayout
		CheckNet

	2.	业务流程

		1. 通过手机号+验证码的形式登陆（注册需要另外的客户端或途径）
		2. 登陆成功后，本地序列化数据保存Token，以及登陆状态
		3. 登陆成功后的每次网络请求都需携带“Token”作为请求头（此操作全局统一处理）
		4. 网络的连接状态、网络的交互全局统一处理
		5. 网络响应中显示页面加载状态
		6. 页面加载失败后，可重试相关的操作
		7. 步骤3中的每次网络交互都携带“Token”，若“Token”过期，则会在网络交互的响应结果中获取到Token过期，然后全局处理Token过期的下一步操作，本项目处理操作是“将App重置为未登录状态，并进入登陆功能”




####	3.	支持




    1. 网络支持：SSL、网络连接检查、响应失败、响应成功、加载中、当Token过期重置为未登录状态并跳转到登陆页面
    2. 本地存储：数据序列化存储、自动登陆


#### 4.	项目效果

![图片地址；http://pupu8os0u.bkt.clouddn.com/android_project_sample.gif](http://pupu8os0u.bkt.clouddn.com/android_project_sample.gif)


#### 5.	项目地址


#### 6.	TODO

	1. 所涉及技术的细分、讲解
	2. 项目功能的完善
	3. 更多数据的填充







----