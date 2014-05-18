package jdepend.knowledge;

public enum StructureCategory {

	Inherit, // 继承结构分析
	Summary, // 对分析结果进行建议
	LowScoreItemIdentifier, // 低分项目识别
	DDomainAnalysis, // 抽象程度合理性分析
	CohesionDomainAnalysis, // 内聚性分析
	RelationRationalityDomainAnalysis,//关系合理性分析
	EncapsulationDomainAnalysis,// 封装性分析
	ArchitectPatternDomainAnalysis//架构模式识别
}
