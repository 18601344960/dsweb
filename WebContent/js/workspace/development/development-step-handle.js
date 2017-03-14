/**
 * @description 党员发展步骤JS
 * @author 赵子靖
 * @since 2015-12-03
 */
var DevelopmentStepHandle = function() {
	var t = {
		mainContainer : '',
		paramters : {},
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initEvent();
		},
		initEvent : function() {
		},
		// ------------------------------------------申请人确定为积极分子----------------------------------------------------------
		// 保存“申请人确定为积极分子”信息
		saveOrUpdate0001Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0001-dialog #id").val();// 党员ID
						var activistTime = $("#proposer-0001-dialog #activistTime")
								.val();// 确定积极分子日期
						var trainer = $("#proposer-0001-dialog #trainer").val();// 培养联系人

						if (CheckInputUtils.isEmpty(activistTime)) {
							Notify.notice("请输入确定积极分子日期。");
							$("#proposer-0001-dialog #activistTime").focus();
							return;
						}
						var paramters = {
							processId : '0001',
							id : id,
							activistTime : activistTime,
							trainer : trainer
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0001-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------对入党积极分子进行培养教育和考察----------------------------------------------------------
		// 保存“对入党积极分子进行培养教育和考察”信息
		saveOrUpdate0101Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0101-dialog #partymemberId")
								.val();// 党员ID
						var orgEducationInfo02 = $("#proposer-0101-dialog #orgEducationInfo02")
								.val();// 受组织教育情况
						var workInfo02 = $("#proposer-0101-dialog #workInfo02")
								.val();// 社会工作情况
						var paramters = {
							processId : '0101',
							id : id,
							orgEducationInfo02 : orgEducationInfo02,
							workInfo02 : workInfo02
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0101-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------确定发展对象-听取党内外有关推优意见----------------------------------------------------------
		// 保存“确定发展对象-听取党内外有关推优意见”信息
		saveOrUpdate0102Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0102-dialog #partymemberId")
								.val();// 党员ID
						var massesOpinion03 = $("#proposer-0102-dialog #massesOpinion03")
								.val();// 党内外群众意见
						var youthOpinion03 = $("#proposer-0102-dialog #youthOpinion03")
								.val();// 团组织推优意
						var paramters = {
							processId : '0102',
							id : id,
							massesOpinion03 : massesOpinion03,
							youthOpinion03 : youthOpinion03
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0102-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------确定发展对象-党小组讨论----------------------------------------------------------
		// 保存“确定发展对象-党小组讨论”信息
		saveOrUpdate0103Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0103-dialog #partymemberId")
								.val();// 党员ID
						var groupOpinion04 = $("#proposer-0103-dialog #groupOpinion04")
								.val();// 党小组意见
						var discussDate04 = $("#proposer-0103-dialog #discussDate04")
								.val();// 讨论日期
						var groupLeader04 = $("#proposer-0103-dialog #groupLeader04")
								.val();// 党小组组长
						var paramters = {
							processId : '0103',
							id : id,
							groupOpinion04 : groupOpinion04,
							discussDate04 : discussDate04,
							groupLeader04 : groupLeader04
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0103-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------确定发展对象-支委会（支部大会）讨论----------------------------------------------------------
		// 保存“确定发展对象-支委会（支部大会）讨论”信息
		saveOrUpdate0104Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0104-dialog #partymemberId")
								.val();// 支委会意见
						var branchOpinion05 = $("#proposer-0104-dialog #branchOpinion05")
								.val();// 党小组意见
						var discussDate05 = $("#proposer-0104-dialog #discussDate05")
								.val();// 讨论日期
						var secretary05 = $("#proposer-0104-dialog #secretary05")
								.val();// 党支部书记
						var paramters = {
							processId : '0104',
							id : id,
							branchOpinion05 : branchOpinion05,
							discussDate05 : discussDate05,
							secretary05 : secretary05
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0104-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------为发展对象确定入党介绍人----------------------------------------------------------
		// 保存“为发展对象确定入党介绍人”信息
		saveOrUpdate0201Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0201-dialog #partymemberId")
								.val();// 支委会意见
						var introducer = $("#proposer-0201-dialog #introducer")
								.val();// 入党介绍人
						var paramters = {
							processId : '0201',
							id : id,
							introducer : introducer
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0201-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------为发展对象进行政治审查----------------------------------------------------------
		// 保存“为发展对象进行政治审查”信息
		saveOrUpdate0202Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0202-dialog #partymemberId")
								.val();// 党员ID
						var politicalaAudit07 = $("#proposer-0202-dialog #politicalaAudit07")
								.val();// 政审结果
						var paramters = {
							processId : '0202',
							id : id,
							politicalaAudit07 : politicalaAudit07
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0202-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------为发展对象进行集中培训----------------------------------------------------------
		// 保存“为发展对象进行集中培训”信息
		saveOrUpdate0203Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0203-dialog #partymemberId")
								.val();// 党员ID
						var cultivateDate08 = $("#proposer-0203-dialog #cultivateDate08")
								.val();// 集中培训日期
						var cultivateResult08 = $("#proposer-0203-dialog #cultivateResult08")
								.val();// 培训结果
						var paramters = {
							processId : '0203',
							id : id,
							cultivateDate08 : cultivateDate08,
							cultivateResult08 : cultivateResult08
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0203-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------报基层党委预审----------------------------------------------------------
		// 保存“报基层党委预审”信息
		saveOrUpdate0204Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0204-dialog #partymemberId")
								.val();// 党员ID
						var superiorAuditOrg09 = $("#proposer-0204-dialog #superiorAuditOrg09")
								.val();// 上级预审组织
						var auditDate09 = $("#proposer-0204-dialog #auditDate09")
								.val();// 审核日期
						var auditResult09 = $("#proposer-0204-dialog #auditResult09")
								.val();// 预审结果
						var paramters = {
							processId : '0204',
							id : id,
							superiorAuditOrg09 : superiorAuditOrg09,
							auditDate09 : auditDate09,
							auditResult09 : auditResult09
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0204-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------公示情况----------------------------------------------------------
		// 保存“公示情况”信息
		saveOrUpdate0205Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0205-dialog #partymemberId")
								.val();// 党员ID
						var noticeInfo10 = $("#proposer-0205-dialog #noticeInfo10")
								.val();// 公示情况
						var paramters = {
							processId : '0205',
							id : id,
							noticeInfo10 : noticeInfo10
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0205-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------召开支部大会讨论----------------------------------------------------------
		// 保存“召开支部大会讨论”信息
		saveOrUpdate0206Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0206-dialog #partymemberId")
								.val();// 党员ID
						var branchDiscussDate11 = $("#proposer-0206-dialog #branchDiscussDate11")
								.val();// 支部大会讨论日期
						var branchDiscussResult11 = $("#proposer-0206-dialog #branchDiscussResult11")
								.val();// 支部大会讨论结果
						var joinCcpartyType11 = $("#proposer-0206-dialog #joinCcpartyType11")
								.val();// 加入中共组织类型
						if (CheckInputUtils.isEmpty(branchDiscussDate11)) {
							Notify.notice("请选择讨论结果。");
							$("#proposer-0206-dialog #branchDiscussDate11")
									.focus();
							return;
						}
						if (CheckInputUtils.isEmpty(joinCcpartyType11)) {
							Notify.notice("请选择加入中共组织类型。");
							$("#proposer-0206-dialog #joinCcpartyType11")
									.focus();
							return;
						}
						var paramters = {
							processId : '0206',
							id : id,
							branchDiscussDate11 : branchDiscussDate11,
							branchDiscussResult11 : branchDiscussResult11,
							joinCcpartyType11 : joinCcpartyType11
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0206-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------基层党委派人谈话----------------------------------------------------------
		// 保存“基层党委派人谈话”信息
		saveOrUpdate0207Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0207-dialog #partymemberId")
								.val();// 党员ID
						var talkDate12 = $("#proposer-0207-dialog #talkDate12")
								.val();// 上级谈话日期
						var talker12 = $("#proposer-0207-dialog #talker12")
								.val();// 谈话人
						var paramters = {
							processId : '0207',
							id : id,
							talkDate12 : talkDate12,
							talker12 : talker12
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0207-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ------------------------------------------基层党委审批----------------------------------------------------------
		// 保存“基层党委审批”信息
		saveOrUpdate0208Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0208-dialog #partymemberId")
								.val();// 党员ID
						var superiorAuditOrg13 = $("#proposer-0208-dialog #superiorAuditOrg13")
								.val();// 级审批组织
						var auditDate13 = $("#proposer-0208-dialog #auditDate13")
								.val();// 审查时间
						var auditResult13 = $("#proposer-0208-dialog #auditResult13")
								.val();// 审查结果
						var isSpecialPartymember13 = $("#proposer-0208-dialog #isSpecialPartymember13")
								.val();// 是否为一线特殊发展党员
						var isSpecialMaxPrivilege13 = $("#proposer-0208-dialog #isSpecialMaxPrivilege13")
								.val();// 是否特殊情况下提高审批权限发展
						if (CheckInputUtils.isEmpty(auditDate13)) {
							Notify.notice("请输入审查时间。");
							$("#proposer-0208-dialog #auditDate13").focus();
							return;
						}
						if (CheckInputUtils.isEmpty(auditResult13)) {
							Notify.notice("请选择审查时间。");
							$("#proposer-0208-dialog #auditResult13").focus();
							return;
						}
						var paramters = {
							processId : '0208',
							id : id,
							superiorAuditOrg13 : superiorAuditOrg13,
							auditDate13 : auditDate13,
							auditResult13 : auditResult13,
							isSpecialPartymember13 : isSpecialPartymember13,
							isSpecialMaxPrivilege13 : isSpecialMaxPrivilege13
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0208-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// -------------------------对预备党员进行教育和考察--------------------------------------------
		// 保存“对预备党员进行教育和考察 ”信息
		saveOrUpdate0301Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0301-dialog #partymemberId")
								.val();// 党员ID
						var workInfo14 = $("#proposer-0301-dialog #workInfo14")
								.val();// 社会工作情况
						var paramters = {
							processId : '0301',
							id : id,
							workInfo14 : workInfo14
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0301-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// ---------------------------预备党员转正-党小组讨论----------------------------------------------
		// 保存“预备党员转正-党小组讨论 ”信息
		saveOrUpdate0302Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0302-dialog #partymemberId")
								.val();// 党员ID
						var groupOpinion15 = $("#proposer-0302-dialog #groupOpinion15")
								.val();// 党小组意见
						var discussDate15 = $("#proposer-0302-dialog #discussDate15")
								.val();// 讨论日期
						var groupLeader15 = $("#proposer-0302-dialog #groupLeader15")
								.val();// 党小组组长
						var paramters = {
							processId : '0302',
							id : id,
							groupOpinion15 : groupOpinion15,
							discussDate15 : discussDate15,
							groupLeader15 : groupLeader15
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0302-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// --------------------------------------预备党员转正-征求党内外群众意见------------------------------------
		// 保存“预备党员转正-征求党内外群众意见 ”信息
		saveOrUpdate0303Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0303-dialog #partymemberId")
								.val();// 党员ID
						var massesOpinion16 = $("#proposer-0303-dialog #massesOpinion16")
								.val();// 预备党员转正-征求党内外群众意见
						var paramters = {
							processId : '0303',
							id : id,
							massesOpinion16 : massesOpinion16
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0303-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// --------------------------------------预备党员转正-支委会审查------------------------------------
		// 保存“预备党员转正-支委会审查 ”信息
		saveOrUpdate0304Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0304-dialog #partymemberId")
								.val();// 党员ID
						var branchOpinion17 = $("#proposer-0304-dialog #branchOpinion17")
								.val();// 支委会意见
						var discussDate17 = $("#proposer-0304-dialog #discussDate17")
								.val();// 讨论日期
						var secretary17 = $("#proposer-0304-dialog #secretary17")
								.val();// 党支部书记
						var paramters = {
							processId : '0304',
							id : id,
							branchOpinion17 : branchOpinion17,
							discussDate17 : discussDate17,
							secretary17 : secretary17
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0304-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// --------------------------------------预备党员转正-支部大会讨论------------------------------------
		// 保存“预备党员转正-支部大会讨论 ”信息
		saveOrUpdate0305Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0305-dialog #partymemberId")
								.val();// 党员ID
						var branchMeetingResult18 = $("#proposer-0305-dialog #branchMeetingResult18")
								.val();// 支部大会讨论结果
						var officialDate18 = $("#proposer-0305-dialog #officialDate18")
								.val();// 转正日期
						var officialInfo18 = $("#proposer-0305-dialog #officialInfo18")
								.val();// 转正情况
						var paramters = {
							processId : '0305',
							id : id,
							branchMeetingResult18 : branchMeetingResult18,
							officialDate18 : officialDate18,
							officialInfo18 : officialInfo18
						}
						Ajax.call({
							url : 'org/saveOrUpdatePartymemberDevelopmentStep',
							p : {
								paramters : "{'data':"
										+ JSON.stringify(paramters, "data")
										+ "}"
							},
							f : function(data) {
								if (data.success) {
									Notify.success(data.msg);
									$("#proposer-0305-dialog").modal('toggle');
									DevelopmentProcedure
											.nextStepDevelopmentProcedureDialog(
													id, data.nextProcessId);
									DevelopmentProcedure.initTable(null,
											data.nextProcessId);
								}
							}
						});
					}
				}
			});
		},
		// --------------------------------------预备党员转正-上级党委审批------------------------------------
		// 保存“预备党员转正-上级党委审批 ”信息
		saveOrUpdate0306Info : function() {
			bootbox.confirm({
				size : 'small',
				message : "确认保存？",
				callback : function(result) {
					if (result) {
						var id = $("#proposer-0306-dialog #partymemberId")
								.val();// 党员ID
						var superiorAuditOrg19 = $("#proposer-0306-dialog #superiorAuditOrg19")
								.val();// 上级审批组织
						var auditResult19 = $("#proposer-0306-dialog #auditResult19")
								.val();// 审批结果
						var paramters = {
							processId : '0306',
							id : id,
							superiorAuditOrg19 : superiorAuditOrg19,
							auditResult19 : auditResult19
						}
						Ajax.call({
									url : 'org/saveOrUpdatePartymemberDevelopmentStep',
									p : {
										paramters : "{'data':"
												+ JSON.stringify(paramters,
														"data") + "}"
									},
									f : function(data) {
										if (data.success) {
											Notify.success(data.msg);
											$("#proposer-0306-dialog")
													.modal('toggle');
											DevelopmentProcedure.initTable(
													null, data.nextProcessId);
										}
									}
								});
					}
				}
			});
		}
	}
	return t;
}();