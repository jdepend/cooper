package jdepend.framework.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class BundleUtil {

	public static final String ClientWin_Title = "ClientWin_Title";// 客户端窗口标题
	public static final String ClientWin_Menu_File = "ClientWin_Menu_File";// 客户端窗口菜单文件
	public static final String ClientWin_Menu_Setting = "ClientWin_Menu_Setting";// 客户端窗口菜单设置
	public static final String ClientWin_Menu_Service = "ClientWin_Menu_Service";// 客户端窗口菜单服务
	public static final String ClientWin_Menu_Data = "ClientWin_Menu_Data";// 客户端窗口菜单数据
	public static final String ClientWin_Menu_Help = "ClientWin_Menu_Help";// 客户端窗口菜单帮助

	public static final String ClientWin_Menu_AddGroup = "ClientWin_Menu_AddGroup";// 客户端窗口菜单增加组
	public static final String ClientWin_Menu_Exit = "ClientWin_Menu_Exit";// 客户端窗口菜单退出
	public static final String ClientWin_Menu_ParamSetting = "ClientWin_Menu_ParamSetting";// 客户端窗口菜单管理参数
	public static final String ClientWin_Menu_ChangeWorkspace = "ClientWin_Menu_ChangeWorkspace";// 客户端窗口菜单切换工作区
	public static final String ClientWin_Menu_SetClassRelationMgr = "ClientWin_Menu_SetClassRelationMgr";// 客户端窗口菜单设置类关系管理器
	public static final String ClientWin_Menu_ServiceParamSetting = "ClientWin_Menu_ServiceParamSetting";// 客户端窗口菜单服务参数设置
	public static final String ClientWin_Menu_Login = "ClientWin_Menu_Login";// 客户端窗口菜单登陆
	public static final String ClientWin_Menu_Logout = "ClientWin_Menu_Logout";// 客户端窗口菜单注销
	public static final String ClientWin_Menu_ScoreList = "ClientWin_Menu_ScoreList";// 客户端窗口菜单分数列表
	public static final String ClientWin_Menu_ImportResult = "ClientWin_Menu_ImportResult";// 客户端窗口菜单导入外部分析结果
	public static final String ClientWin_Menu_Introduce = "ClientWin_Menu_Introduce";// 客户端窗口菜单介绍
	public static final String ClientWin_Menu_MetricsExplain = "ClientWin_Menu_MetricsExplain";// 客户端窗口菜单指标说明
	public static final String ClientWin_Menu_ScoreExplain = "ClientWin_Menu_ScoreExplain";// 客户端窗口菜单分数说明
	public static final String ClientWin_Menu_ScoreAndMetrics = "ClientWin_Menu_ScoreAndMetrics";// 客户端窗口菜单分数和指标体系
	public static final String ClientWin_Menu_About = "ClientWin_Menu_About";// 客户端窗口菜单关于

	public static final String ClientWin_Culture_DesignPrinciple = "ClientWin_Culture_DesignPrinciple";// 客户端窗口设计原则
	public static final String ClientWin_Culture_Analyzer = "ClientWin_Culture_Analyzer";// 客户端窗口分析器

	public static final String ClientWin_Culture_DesignPrinciple_Package = "ClientWin_Culture_DesignPrinciple_Package";// 客户端窗口设计原则
	public static final String ClientWin_Culture_DesignPrinciple_Class = "ClientWin_Culture_DesignPrinciple_Class";// 客户端窗口设计原则
	public static final String ClientWin_Culture_DesignPrinciple_GRASP = "ClientWin_Culture_DesignPrinciple_GRASP";// 客户端窗口设计原则

	public static final String ClientWin_Culture_Analyzer_AntiPattern = "ClientWin_Culture_Analyzer_AntiPattern";// 客户端窗口分析器反模式
	public static final String ClientWin_Culture_Analyzer_Attention = "ClientWin_Culture_Analyzer_Attention";// 客户端窗口分析器关注

	public static final String ClientWin_Property_TODOList = "ClientWin_Property_TODOList";// 客户端窗口属性部分待做事项
	public static final String ClientWin_Property_ClassList = "ClientWin_Property_ClassList";// 客户端窗口属性部分类列表
	public static final String ClientWin_Property_Memento = "ClientWin_Property_Memento";// 客户端窗口属性部分类列表
	public static final String ClientWin_Property_ReportHistroy = "ClientWin_Property_ReportHistroy";// 客户端窗口属性部分类列表
	public static final String ClientWin_Property_ExecuteHistroy = "ClientWin_Property_ExecuteHistroy";// 客户端窗口属性部分类列表
	public static final String ClientWin_Property_Knowledge = "ClientWin_Property_Knowledge";// 客户端窗口属性部分类列表
	public static final String ClientWin_Property_Log = "ClientWin_Property_Log";// 客户端窗口属性部分日志
	public static final String ClientWin_Property_Log_System = "ClientWin_Property_Log_System";// 客户端窗口属性部分日志
	public static final String ClientWin_Property_Log_Business = "ClientWin_Property_Log_Business";// 客户端窗口属性部分日志

	public static final String ClientWin_Result_Summary = "ClientWin_Result_Summary";// 客户端窗口分析结果
	public static final String ClientWin_Result_Relation = "ClientWin_Result_Relation";// 客户端窗口分析结果
	public static final String ClientWin_Result_Coupling = "ClientWin_Result_Coupling";// 客户端窗口分析结果
	public static final String ClientWin_Result_Cohesion = "ClientWin_Result_Cohesion";// 客户端窗口分析结果
	public static final String ClientWin_Result_Pattern = "ClientWin_Result_Pattern";// 客户端窗口分析结果
	public static final String ClientWin_Result_Capacity = "ClientWin_Result_Capacity";// 客户端窗口分析结果
	public static final String ClientWin_Result_Notice = "ClientWin_Result_Notice";// 客户端窗口分析结果
	
	public static final String ClientWin_Result_System = "ClientWin_Result_System";// 客户端窗口分析结果
	public static final String ClientWin_Result_Component = "ClientWin_Result_Component";// 客户端窗口分析结果
	public static final String ClientWin_Result_Class = "ClientWin_Result_Class";// 客户端窗口分析结果
	public static final String ClientWin_Result_Method = "ClientWin_Result_Method";// 客户端窗口分析结果

	public static final String ClientWin_ScorePanel_TotalScore = "ClientWin_ScorePanel_TotalScore";
	public static final String ClientWin_ScorePanel_Score = "ClientWin_ScorePanel_Score";
	public static final String ClientWin_ScorePanel_ScoreDifference = "ClientWin_ScorePanel_ScoreDifference";
	public static final String ClientWin_ScorePanel_FullScore = "ClientWin_ScorePanel_FullScore";
	public static final String ClientWin_ScorePanel_OtherMetrics = "ClientWin_ScorePanel_OtherMetrics";
	public static final String ClientWin_ScorePanel_ExistingScoreScope = "ClientWin_ScorePanel_ExistingScoreScope";
	public static final String ClientWin_ScorePanel_RelationNormal = "ClientWin_ScorePanel_RelationNormal";
	public static final String ClientWin_ScorePanel_ElementChangeTip = "ClientWin_ScorePanel_ElementChangeTip";
	public static final String ClientWin_ScorePanel_ElementChangeTip_This = "ClientWin_ScorePanel_ElementChangeTip_This";
	public static final String ClientWin_ScorePanel_Value = "ClientWin_ScorePanel_Value";

	public static final String ClientWin_Group_Name = "ClientWin_Group_Name";
	public static final String ClientWin_Group_Path = "ClientWin_Group_Path";
	public static final String ClientWin_Group_SrcPath = "ClientWin_Group_SrcPath";
	public static final String ClientWin_Group_FilteredPackages = "ClientWin_Group_FilteredPackages";
	public static final String ClientWin_Group_Attribute = "ClientWin_Group_Attribute";

	public static final String ClientWin_ComponentModel_Name = "ClientWin_ComponentModel_Name";
	public static final String ClientWin_ComponentModel_PackageModel = "ClientWin_ComponentModel_PackageModel";
	public static final String ClientWin_ComponentModel_FilterExt = "ClientWin_ComponentModel_FilterExt";
	public static final String ClientWin_ComponentModel_PackageListFilter = "ClientWin_ComponentModel_PackageListFilter";
	public static final String ClientWin_ComponentModel_ComponentList = "ClientWin_ComponentModel_ComponentList";
	public static final String ClientWin_ComponentModel_ElementList = "ClientWin_ComponentModel_ElementList";
	
	public static final String ClientWin_ClassListDialog_Caller = "ClientWin_ClassListDialog_Caller";
	public static final String ClientWin_ClassListDialog_Callee = "ClientWin_ClassListDialog_Callee";
	
	public static final String ClientWin_MethodListDialog_Name = "ClientWin_MethodListDialog_Name";

	public static final String Command_View = "Command_View";// 查看
	public static final String Command_Save = "Command_Save";// 保存
	public static final String Command_SaveAs = "Command_SaveAs";// 另存为
	public static final String Command_Add = "Command_Add";// 增加
	public static final String Command_Update = "Command_Update";// 修改
	public static final String Command_Delete = "Command_Delete";// 删除
	public static final String Command_Close = "Command_Close";// 关闭
	public static final String Command_OK = "Command_OK";// 确定
	public static final String Command_Cancel = "Command_Cancel";// 取消
	public static final String Command_Login = "Command_Login";// 登陆
	public static final String Command_Logout = "Command_Logout";// 注销
	public static final String Command_VirtualExecute = "Command_VirtualExecute";// 虚拟执行
	public static final String Command_Compare = "Command_Compare";// 比较
	public static final String Command_Run = "Command_Run";// 运行
	public static final String Command_AddCommand = "Command_AddCommand";// 增加命令
	public static final String Command_EditCommand = "Command_EditCommand";// 编辑命令
	public static final String Command_DeleteCommand = "Command_DeleteCommand";// 删除命令
	public static final String Command_CreateCommand = "Command_CreateCommand";// 创建命令
	public static final String Command_ViewReportHistory = "Command_ViewReportHistory";// 查看报告历史
	public static final String Command_ViewExecuteHistory = "Command_ViewExecuteHistory";// 查看执行历史
	public static final String Command_Refresh = "Command_Refresh";// 刷新
	public static final String Command_AddIgnoreList = "Command_AddIgnoreList";// 加入到忽略列表
	public static final String Command_AddIgnoreListWithChildren = "Command_AddIgnoreListWithChildren";// 加入到忽略列表（包括下级）
	public static final String Command_ViewIgnoreList = "Command_ViewIgnoreList";// 查看忽略列表
	public static final String Command_Unite = "Command_Unite";// 合并
	public static final String Command_CreateComponent = "Command_CreateComponent";// 创建组件
	public static final String Command_CreateComponentWithPackages = "Command_CreateComponentWithPackages";// 创建组件
	public static final String Command_JoinComponent = "Command_JoinComponent";// 加入组件
	public static final String Command_DeleteComponent = "Command_DeleteComponent";// 删除组件
	public static final String Command_ViewSourceCode = "Command_ViewSourceCode";// 查看源代码
	public static final String Command_ViewDetail = "Command_ViewDetail";// 查看明细
	public static final String Command_ViewMethodList = "Command_ViewMethodList";// 查看方法列表
	public static final String Command_Move = "Command_Move";// 移动
	public static final String Command_MoveToOtherComponent = "Command_MoveToOtherComponent";
	public static final String Command_ZoomToFit = "Command_ZoomToFit";// 适应屏幕
	public static final String Command_ShowProblemRelation = "Command_ShowProblemRelation";// 仅显示问题关系
	public static final String Command_UnStepHide = "Command_UnStepHide";// 恢复上一步隐藏
	public static final String Command_NextStepHide = "Command_NextStepHide";// 前进下一步隐藏
	public static final String Command_UnHide = "Command_UnHide";// 恢复全部隐藏
	public static final String Command_RadialTreeLayout = "Command_RadialTreeLayout";// 圆型布局
	public static final String Command_NodeLinkTreeLayout = "Command_NodeLinkTreeLayout";// 树型布局
	public static final String Command_SpecifiedLayout = "Command_SpecifiedLayout";// 上次位置布局
	public static final String Command_ViewPackageTree = "Command_ViewPackageTree";// 浏览包结构
	public static final String Command_ClosePackageTree = "Command_ClosePackageTree";// 关闭包结构
	public static final String Command_SaveAsPic = "Command_SaveAsPic";// 另存为图片
	public static final String Command_SaveAsTxt = "Command_SaveAsTxt";
	public static final String Command_SaveAsHTML = "Command_SaveAsHTML";
	public static final String Command_ExportList = "Command_ExportList";
	public static final String Command_Export = "Command_Export";//导出
	public static final String Command_AddGroup = "Command_AddGroup";
	public static final String Command_DeleteGroup = "Command_DeleteGroup";
	public static final String Command_RefreshGroup = "Command_RefreshGroup";
	public static final String Command_ViewGroup = "Command_ViewGroup";
	public static final String Command_HideGroup = "Command_HideGroup";
	public static final String Command_SelectShowGroup = "Command_SelectShowGroup";
	public static final String Command_ManageComponentModel = "Command_ManageComponentModel";
	public static final String Command_Clear = "Command_Clear";
	public static final String Command_PriorStep = "Command_PriorStep";
	public static final String Command_NextStep = "Command_NextStep";
	public static final String Command_Finish = "Command_Finish";
	public static final String Command_SkipStep = "Command_SkipStep";
	public static final String Command_RepeatStep = "Command_RepeatStep";
	public static final String Command_ViewClassList = "Command_ViewClassList";
	public static final String Command_ViewGobalIgnorePackages = "Command_ViewGobalIgnorePackages";
	public static final String Command_ViewResult = "Command_ViewResult";
	public static final String Command_Setting = "Command_Setting";
	public static final String Command_Explain = "Command_Explain";
	public static final String Command_Upload = "Command_Upload";
	public static final String Command_Download = "Command_Download";
	public static final String Command_ShowLineChart = "Command_ShowLineChart";
	public static final String Command_Disable = "Command_Disable";
	public static final String Command_Enable = "Command_Enable";
	public static final String Command_Copy = "Command_Copy";
	public static final String Command_ReCalculate = "Command_ReCalculate";// 重新计算
	public static final String Command_Analyse = "Command_Analyse";// 分析
	public static final String Command_Ca = "Command_Ca";// 传入
	public static final String Command_Ce = "Command_Ce";// 传出
	
	public static final String RunningModel_Local = "RunningModel_Local";// 单机模式
	public static final String RunningModel_Remote = "RunningModel_Remote";// 联机模式

	public static final String State_Logined = "State_Logined";// 已登录
	public static final String State_unLogin = "State_unLogin";// 未登录

	public static final String Analysis_Time = "Analysis_Time";

	public static final String Metrics_TotalScore = "Metrics_TotalScore";
	public static final String Metrics_D = "Metrics_D";// 抽象程度合理性
	public static final String Metrics_Balance = "Metrics_Balance";// 内聚性
	public static final String Metrics_Encapsulation = "Metrics_Encapsulation";// 封装性
	public static final String Metrics_RelationRationality = "Metrics_RelationRationality";// 关系合理性

	public static final String Metrics_Place = "Metrics_Place";
	public static final String Metrics_Name = "Metrics_Name";
	public static final String Metrics_Area = "Metrics_Area";
	public static final String Metrics_Title = "Metrics_Title";
	public static final String Metrics_LC = "Metrics_LC";
	public static final String Metrics_CN = "Metrics_CN";
	public static final String Metrics_CC = "Metrics_CC";
	public static final String Metrics_AC = "Metrics_AC";
	public static final String Metrics_Ca = "Metrics_Ca";
	public static final String Metrics_Ce = "Metrics_Ce";
	public static final String Metrics_A = "Metrics_A";
	public static final String Metrics_I = "Metrics_I";
	public static final String Metrics_V = "Metrics_V";
	public static final String Metrics_Coupling = "Metrics_Coupling";
	public static final String Metrics_Cohesion = "Metrics_Cohesion";
	public static final String Metrics_Cycle = "Metrics_Cycle";
	public static final String Metrics_State = "Metrics_State";
	public static final String Metrics_Stable = "Metrics_Stable";
	public static final String Metrics_isPrivateElement = "Metrics_isPrivateElement";
	public static final String Metrics_isExt = "Metrics_isExt";
	public static final String Metrics_ClassType = "Metrics_ClassType";

	public static final String Metrics_ComponentCount = "Metrics_ComponentCount";
	public static final String Metrics_RelationCount = "Metrics_RelationCount";
	public static final String Metrics_RelationComponentScale = "Metrics_RelationComponentScale";
	public static final String Metrics_Capacity = "Metrics_Capacity";
	public static final String Metrics_AttentionRelationScale = "Metrics_AttentionRelationScale";

	public static final String Metrics_Complexity = "Metrics_Complexity";
	public static final String Metrics_Skill = "Metrics_Skill";
	public static final String Metrics_PatternCount = "Metrics_PatternCount";
	public static final String Metrics_AverageClassSize = "Metrics_AverageClassSize";

	public static final String Metrics_OO_Desc = "Metrics_OO_Desc";

	public static final String Relation_ClassDependType = "Relation_ClassDependType";
	public static final String Relation_CurrentElement = "Relation_CurrentElement";
	public static final String Relation_DependElement = "Relation_DependElement";
	public static final String Relation_Intensity = "Relation_Intensity";
	public static final String Relation_CurrentCohesion = "Relation_CurrentCohesion";
	public static final String Relation_DependCohesion = "Relation_DependCohesion";
	public static final String Relation_Balance = "Relation_Balance";
	public static final String Relation_AttentionType = "Relation_AttentionType";
	public static final String Relation_AttentionLevel = "Relation_AttentionLevel";
	public static final String Relation_isProblem = "Relation_isProblem";

	public static final String Advise_D_Small = "Advise_D_Small";
	public static final String Advise_D_Big = "Advise_D_Big";
	public static final String Advise_Cohesion_Small = "Advise_Cohesion_Small";
	public static final String Advise_Encapsulation_Small = "Advise_Encapsulation_Small";
	public static final String Advise_Relation_AttentionLevel_Big = "Advise_Relation_AttentionLevel_Big";
	public static final String Advise_LessScoreItem = "Advise_LessScoreItem";

	public static final String TableHead_DependInterface = "TableHead_DependInterface";
	public static final String TableHead_CurrentJC_Place = "TableHead_CurrentJC_Place";
	public static final String TableHead_CurrentJC = "TableHead_CurrentJC";
	public static final String TableHead_DependJC_Place = "TableHead_DependJC_Place";
	public static final String TableHead_DependJC = "TableHead_DependJC";
	public static final String TableHead_Desc = "TableHead_Desc";
	public static final String TableHead_According = "TableHead_According";
	public static final String TableHead_TableName = "TableHead_TableName";
	public static final String TableHead_AppearCount = "TableHead_AppearCount";
	public static final String TableHead_Operation = "TableHead_Operation";
	public static final String TableHead_ComponentName = "TableHead_ComponentName";
	public static final String TableHead_ClassName = "TableHead_ClassName";
	public static final String TableHead_PackageName = "TableHead_PackageName";
	public static final String TableHead_ClassCount = "TableHead_ClassCount";
	public static final String TableHead_Scale = "TableHead_Scale";
	public static final String TableHead_CreateTime = "TableHead_CreateTime";
	public static final String TableHead_ActionList = "TableHead_ActionList";
	public static final String TableHead_GroupName = "TableHead_GroupName";
	public static final String TableHead_CommandName = "TableHead_CommandName";
	public static final String TableHead_Place = "TableHead_Place";
	public static final String TableHead_Name = "TableHead_Name";
	public static final String TableHead_ExecuteDate = "TableHead_ExecuteDate";
	public static final String TableHead_Version = "TableHead_Version";
	public static final String TableHead_CreateDate = "TableHead_CreateDate";
	public static final String TableHead_Explain = "TableHead_Explain";
	public static final String TableHead_State = "TableHead_State";

	public static String getString(String name) {
		ResourceBundle bundle = ResourceBundle.getBundle("base", Locale.getDefault());
		return bundle.getString(name);
	}
}
