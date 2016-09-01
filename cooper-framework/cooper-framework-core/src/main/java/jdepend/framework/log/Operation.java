package jdepend.framework.log;

import java.io.Serializable;

public enum Operation implements Serializable {

	startCooper, // 启动客户端
	login, // 登陆服务器
	logout, // 登出服务器
	passworderror, // 密码错误
	usernameerror, // 用户名不存在

	connectfailure, // 连接远程服务失败
	servicenoavailable, // 服务不可用

	uploadAnalyzer, // 上传分析器
	downloadAnalyzer, // 下载分析器
	executeAnalyzer, // 执行分析器

	executeCommand, // 执行命令
	createGroup, // 创建命令组
	createCommand, // 创建命令
	deleteCommand, // 删除命令
	deleteGroup, // 删除组

	createComponentModel, // 创建组件模型
	deleteComponentModel, // 删除组件模型

	saveServiceConf, // 保存服务配置
	saveParseConf, // 保存解析配置
	saveReportConf, // 保存报告配置
	saveCommandTemplate, // 保存命令模版
	saveUIConf, // 保存UI配置

	moveToClass, // 移动JavaClass
	uniteComponent, // 合并组件
	createComponent, // 创建组件
	deleteComponent, // 删除组件
	saveTextReport, // 保存报告

	viewTextReportHis, // 查看报告历史
	viewExecuteHis, // 查看执行历史

	viewCycle, // 查看循环依赖
	viewCa, // 查看传入
	viewCe, // 查看传出
	viewCoupling, // 查看耦合值
	viewCohesion, // 查看内聚值
	viewBalance, // 查看内聚性指数
	

	viewRelationGraph, // 查看关系图
	viewTheRelation, // 查看具体关系
	viewRelationTable, // 查看关系表格

	viewCooper, // 查看Cooper介绍
	viewDesignPrinciple, // 查看设计原则
	
	executeMotive,//执行猜测设计动机
}
