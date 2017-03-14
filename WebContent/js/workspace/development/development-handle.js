/**
 * @description 新增党员类型处理JS
 * @author 赵子靖
 * @since 2015-12-03
 */
var DevelopmentHandle = function() {
	var t = {
		mainContainer : '',
		proposer : 0,// 申请人
		activist : 1,// 积极分子
		developmentObject : 2,// 发展对象
		probationaryPartyMember : 3,// 预备党员
		partyMember : 4,// 正式党员
		paramters : {},
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initEvent();
			t.initParamters();
		},
		initEvent : function() {
		},
		// 初始化参数
		initParamters : function() {
			t.paramters
			{
				id = ''; // 登录账号
				name = ''; // 姓名
				gender = ''; // 性别
				birthDay = '';// 出生日期
				userNationId = '';// 民族
				userBirthPlaceId = '';// 籍贯
				mobile = '';// 电话
				idNumber = '';// 身份证
				address = '';// 地址
				educationId = '';// 学历
				degreeId = '';// 学位
				applyTime = '';// 申请入党日期
				ccparty = '';// 所属党组织
				type = 0;// 申请人
				phaseCode = 0;// 发展阶段
				activistTime : '';// 列为积极分子日期
				trainer : '';// 培养联系人
				targeTime : '';// 列为发展对象日期
				isDelegate : '';// 是否困难党员
				joinActivity : '';// 参加组织生活情况
				introducer : '';// 入党介绍人
				passTime : '';// 支部大会通过日期
				auditTime : '';// 上级组织批准日期
				fee : '';// 缴纳党费
				joinCurrentTime : '';// 进入当前支部日期
				joinType : '';// 进入当前支部类型
			}
		},
		// 验证用户登录账号是否可用
		checkLoginNo : function(loginNo) {
			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#loginNo").focus();
				return;
			} else if (CheckInputUtils.checkIsMobilePhone(loginNo)) {
				Notify.notice("请不要使用手机号作为登录账号。");
				$("#loginNo").focus();
				return;
			} else {
				Ajax.call({
							url : 'uam/checkUserLoginNo',
							p : {
								loginNo : loginNo
							},
							f : function(data) {
								if (data.success == false) {
									Notify.notice(data.msg);
									$("#loginNo").focus();
									return;
								}
							}
						});
			}
		},
		// 更换上传头像
		replaceHeadImg : function() {
			$("#maintainDialogs")
					.html(nunjucks
							.render(Global.appName
									+ '/tpl/workspace/dialogs/development/replace-head-dialog.html'));
			$("#replace-head-dialog").modal({});
		},
		// 保存头像
		saveHeadImg : function() {
			var headImg = $("#headImg").val();
			// 对头像进行验证
			var suffix = headImg
					.substring(headImg.indexOf("."), headImg.length);
			if (!CheckInputUtils.checkIsImage(suffix)) {
				Notify
						.notice("请选择正确的图片，格式后缀名应以'.gif', '.jpg', '.jpeg', '.png'结尾，大小在500KB之内！");
				return;
			}
			// 使用ajaxfileupload异步上传文件
			$.ajaxFileUpload({
						url : 'uam/replaceHeadImg', // 用于文件上传的服务器端请求地址
						data : {
							userId : $("#userId").val()
						}, // 此参数非常严谨，写错一个引号都不行
						secureuri : false, // 是否需要安全协议，一般设置为false
						fileElementId : 'headImg', // 文件上传域的ID
						dataType : 'json', // 返回值类型 一般设置为json
						success : function(data, status) // 服务器成功响应处理函数
						{
							Notify.info(data.msg);
							// 将image href连接更换
							$("#headImgId").attr(
									"src",
									"uam/getHeadImg?userId="
											+ $("#userId").val() + "&timestam="
											+ new Date().getTime());
							$("#replace-head-dialog").modal('toggle');
							$("#head_" + PartyMember.userId).attr(
									"src",
									"uam/getHeadImg?userId="
											+ $("#userId").val() + "&timestam="
											+ new Date().getTime());
						},
						error : function(data, status, e) // 服务器响应失败处理函数
						{
							Notify.error(data.msg);
						}
					})// end ajaxfileupload

		},
		// ------------------------------------------申请人----------------------------------------------------------
		// 增加申请人
		addProposer : function() {
			var config = {
				dialogType : 'add',
				ccpartyId : Global.ccpartyId,
				ccpartyName : Global.ccpartyName,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/proposer-dialogs.html',
									config));
			$("#proposer-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
		},
		// 编辑申请人
		editProposer : function(data) {
			var config = {
				dialogType : 'edit',
				user : data.item,
				ccpartyId : data.ids,
				ccpartyName : data.names,
				partyMember : data.partyMember,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/proposer-dialogs.html',
									config));
			$("#proposer-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
			$("#proposer-dialog #userNationName").val(Global
					.getCodeName('A0121.' + data.item.nation));
			$("#proposer-dialog #userNationId").val(data.item.nation);
			$("#proposer-dialog #userBirthPlaceName").val(Global
					.getCodeName('A0114.' + data.item.birthPlace));
			$("#proposer-dialog #userBirthPlaceId").val(data.item.birthPlace);
			$("#proposer-dialog #educationName").val(Global
					.getCodeName('A0405.' + data.item.education));
			$("#proposer-dialog #educationId").val(data.item.education);
			$("#proposer-dialog #degreeName").val(Global.getCodeName('A0440.'
					+ data.item.degree));
			$("#proposer-dialog #degreeId").val(data.item.degree);
			$("#proposer-dialog input[type=radio][name='gender'][value="
					+ data.item.gender + "]").attr("checked", 'checked');

			PartyMemberRewardsResume.setRewardsInputsBestInfo();
			PartyMemberResume.setResumeInputsBestInfo();
		},
		// 新增或保存申请人信息
		saveOrUpdateProposer : function() {
			var id = $("#userId").val();// ID
			var loginNo = $("#loginNo").val();// 登录账号
			var name = $("#proposer-dialog #name").val();// 姓名
			var gender = $("#proposer-dialog input[name='gender']:checked")
					.val(); // 性别
			var birthDay = $("#proposer-dialog #birthDay").val();// 出生日期
			var userNationId = $("#proposer-dialog #userNationId").val(); // 民族
			var userBirthPlaceId = $("#proposer-dialog #userBirthPlaceId")
					.val(); // 籍贯
			var mobile = $("#proposer-dialog #mobile").val(); // 电话
			var idNumber = $("#proposer-dialog #idNumber").val(); // 身份证号
			var address = $("#proposer-dialog #address").val();// 地址
			var educationId = $("#proposer-dialog #educationId").val();// 学历
			var degreeId = $("#proposer-dialog #degreeId").val();// 学位
			var applyTime = $("#proposer-dialog #applyTime").val();// 申请日期
			var ccparty = $("#proposer-dialog #ccparty").attr("ids");// 所属党组织
			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#proposer-dialog #loginNo").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入姓名。");
				$("#proposer-dialog #name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(birthDay)) {
				Notify.notice("请输入出生日期。");
				$("#proposer-dialog #birthDay").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(userNationId)) {
				Notify.notice("请选填民族。");
				$("#proposer-dialog #userNationName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(idNumber)) {
				Notify.notice("请输入身份证号。");
				$("#proposer-dialog #idNumber").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(applyTime)) {
				Notify.notice("请输入申请入党日期。");
				$("#proposer-dialog #applyTime").focus();
				return;
			}
			t.paramters.id = id;
			t.paramters.loginNo = loginNo;
			t.paramters.name = name;
			t.paramters.gender = gender;
			t.paramters.birthDay = birthDay;
			t.paramters.userNationId = userNationId;
			t.paramters.userBirthPlaceId = userBirthPlaceId;
			t.paramters.mobile = mobile;
			t.paramters.idNumber = idNumber;
			t.paramters.address = address;
			t.paramters.educationId = educationId;
			t.paramters.degreeId = degreeId;
			t.paramters.applyTime = applyTime;
			t.paramters.ccparty = ccparty;
			t.paramters.type = 0;// 申请人
			t.paramters.phaseCode = t.proposer;
			Ajax.call({
						url : 'org/saveOrUpdatePartymemberForDevelopmentPlan',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(t.paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.notice(data.msg);
								$("#proposer-dialog").modal('toggle');
								DevelopmentProcedure.initTable(
										t.paramters.type, null)
							}
						}
					});
			t.initParamters();
		},
		// ------------------------------------------积极分子----------------------------------------------------------
		// 增加积极分子
		addActivist : function() {
			var config = {
				dialogType : 'add',
				ccpartyId : Global.ccpartyId,
				ccpartyName : Global.ccpartyName,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/activist-dialogs.html',
									config));
			$("#activist-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
		},
		// 编辑积极分子
		editActivist : function(data) {
			var config = {
				dialogType : 'edit',
				user : data.item,
				ccpartyId : data.ids,
				ccpartyName : data.names,
				partyMember : data.partyMember,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/activist-dialogs.html',
									config));
			$("#activist-dialog").modal({});

			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
			$("#activist-dialog #userNationName").val(Global
					.getCodeName('A0121.' + data.item.nation));
			$("#activist-dialog #userNationId").val(data.item.nation);
			$("#activist-dialog #userBirthPlaceName").val(Global
					.getCodeName('A0114.' + data.item.birthPlace));
			$("#activist-dialog #userBirthPlaceId").val(data.item.birthPlace);
			$("#activist-dialog #educationName").val(Global
					.getCodeName('A0405.' + data.item.education));
			$("#activist-dialog #educationId").val(data.item.education);
			$("#activist-dialog #degreeName").val(Global.getCodeName('A0440.'
					+ data.item.degree));
			$("#activist-dialog #degreeId").val(data.item.degree);
			$("#activist-dialog input[type=radio][name='gender'][value="
					+ data.item.gender + "]").attr("checked", 'checked');

			PartyMemberRewardsResume.setRewardsInputsBestInfo();
			PartyMemberResume.setResumeInputsBestInfo();
		},
		// 新增或保存积极分子信息
		saveOrUpdateActivist : function() {
			var id = $("#userId").val();// ID
			var loginNo = $("#loginNo").val();// 登录账号
			var name = $("#activist-dialog #name").val();// 姓名
			var gender = $("#activist-dialog input[name='gender']:checked")
					.val(); // 性别
			var birthDay = $("#activist-dialog #birthDay").val();// 出生日期
			var userNationId = $("#activist-dialog #userNationId").val(); // 民族
			var userBirthPlaceId = $("#activist-dialog #userBirthPlaceId")
					.val(); // 籍贯
			var mobile = $("#activist-dialog #mobile").val(); // 电话
			var idNumber = $("#activist-dialog #idNumber").val(); // 身份证号
			var address = $("#activist-dialog #address").val();// 地址
			var educationId = $("#activist-dialog #educationId").val();// 学历
			var degreeId = $("#activist-dialog #degreeId").val();// 学位
			var applyTime = $("#activist-dialog #applyTime").val();// 申请日期
			var ccparty = $("#activist-dialog #ccparty").attr("ids");// 所属党组织
			var activistTime = $("#activist-dialog #activistTime").val();// 列为发展对象日期
			var trainer = $("#activist-dialog #trainer").val();// 培养联系人
			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#loginNo").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入姓名。");
				$("#activist-dialog #name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(birthDay)) {
				Notify.notice("请输入出生日期。");
				$("#proposer-dialog #birthDay").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(userNationId)) {
				Notify.notice("请选填民族。");
				$("#activist-dialog #userNationName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(idNumber)) {
				Notify.notice("请输入身份证号。");
				$("#activist-dialog #idNumber").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(applyTime)) {
				Notify.notice("请输入申请入党日期。");
				$("#activist-dialog #applyTime").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(activistTime)) {
				Notify.notice("请输入列为积极分子日期。");
				$("#activist-dialog #activistTime").focus();
				return;
			}
			t.paramters.id = id;
			t.paramters.loginNo = loginNo;
			t.paramters.name = name;
			t.paramters.gender = gender;
			t.paramters.birthDay = birthDay;
			t.paramters.userNationId = userNationId;
			t.paramters.userBirthPlaceId = userBirthPlaceId;
			t.paramters.mobile = mobile;
			t.paramters.idNumber = idNumber;
			t.paramters.address = address;
			t.paramters.educationId = educationId;
			t.paramters.degreeId = degreeId;
			t.paramters.applyTime = applyTime;
			t.paramters.ccparty = ccparty;
			t.paramters.type = 1;// 积极分子
			t.paramters.phaseCode = t.activist;
			t.paramters.activistTime = activistTime;
			t.paramters.trainer = trainer;
			Ajax.call({
						url : 'org/saveOrUpdatePartymemberForDevelopmentPlan',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(t.paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.notice(data.msg);
								$("#activist-dialog").modal('toggle');
								DevelopmentProcedure.initTable(
										t.paramters.type, null)
							}
						}
					});
			t.initParamters();
		},
		// ------------------------------------------发展对象----------------------------------------------------------
		// 增加发展对象
		addDevelopmentObject : function() {
			var config = {
				dialogType : 'add',
				ccpartyId : Global.ccpartyId,
				ccpartyName : Global.ccpartyName,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/development-object-dialogs.html',
									config));
			$("#development-object-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
		},
		// 编辑发展对象
		editDevelopmentObject : function(data) {
			var config = {
				dialogType : 'edit',
				user : data.item,
				ccpartyId : data.ids,
				ccpartyName : data.names,
				partyMember : data.partyMember,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/development-object-dialogs.html',
									config));
			$("#development-object-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯

			$("#development-object-dialog #userNationName").val(Global
					.getCodeName('A0121.' + data.item.nation));
			$("#development-object-dialog #userNationId").val(data.item.nation);
			$("#development-object-dialog #userBirthPlaceName").val(Global
					.getCodeName('A0114.' + data.item.birthPlace));
			$("#development-object-dialog #userBirthPlaceId")
					.val(data.item.birthPlace);
			$("#development-object-dialog #educationName").val(Global
					.getCodeName('A0405.' + data.item.education));
			$("#development-object-dialog #educationId")
					.val(data.item.education);
			$("#development-object-dialog #degreeName").val(Global
					.getCodeName('A0440.' + data.item.degree));
			$("#development-object-dialog #degreeId").val(data.item.degree);
			$("#development-object-dialog input[type=radio][name='gender'][value="
					+ data.item.gender + "]").attr("checked", 'checked');

			PartyMemberRewardsResume.setRewardsInputsBestInfo();
			PartyMemberResume.setResumeInputsBestInfo();
		},
		// 新增或保存发展对象信息
		saveOrUpdateDevelopmentObject : function() {
			var id = $("#userId").val();// ID
			var loginNo = $("#loginNo").val();// 登录账号
			var name = $("#development-object-dialog #name").val();// 姓名
			var gender = $("#development-object-dialog input[name='gender']:checked")
					.val(); // 性别
			var birthDay = $("#development-object-dialog #birthDay").val();// 出生日期
			var userNationId = $("#development-object-dialog #userNationId")
					.val(); // 民族
			var userBirthPlaceId = $("#development-object-dialog #userBirthPlaceId")
					.val(); // 籍贯
			var mobile = $("#development-object-dialog #mobile").val(); // 电话
			var idNumber = $("#development-object-dialog #idNumber").val(); // 身份证号
			var address = $("#development-object-dialog #address").val();// 地址
			var educationId = $("#development-object-dialog #educationId")
					.val();// 学历
			var degreeId = $("#development-object-dialog #degreeId").val();// 学位
			var applyTime = $("#development-object-dialog #applyTime").val();// 申请日期
			var ccparty = $("#development-object-dialog #ccparty").attr("ids");// 所属党组织
			var activistTime = $("#development-object-dialog #activistTime")
					.val();// 列为积极分子日期
			var trainer = $("#development-object-dialog #trainer").val();// 培养联系人
			var targetTime = $("#development-object-dialog #targetTime").val();// 列为发展对象日期
			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#loginNo").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入姓名。");
				$("#development-object-dialog #name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(birthDay)) {
				Notify.notice("请输入出生日期。");
				$("#development-object-dialog #birthDay").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(userNationId)) {
				Notify.notice("请选填民族。");
				$("#development-object-dialog #userNationName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(idNumber)) {
				Notify.notice("请输入身份证号。");
				$("#development-object-dialog #idNumber").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(applyTime)) {
				Notify.notice("请输入申请入党日期。");
				$("#development-object-dialog #applyTime").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(activistTime)) {
				Notify.notice("请输入列为积极分子对象日期。");
				$("#development-object-dialog #activistTime").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(targetTime)) {
				Notify.notice("请输入发展对象日期。");
				$("#development-object-dialog #targetTime").focus();
				return;
			}
			t.paramters.id = id;
			t.paramters.loginNo = loginNo;
			t.paramters.name = name;
			t.paramters.gender = gender;
			t.paramters.birthDay = birthDay;
			t.paramters.userNationId = userNationId;
			t.paramters.userBirthPlaceId = userBirthPlaceId;
			t.paramters.mobile = mobile;
			t.paramters.idNumber = idNumber;
			t.paramters.address = address;
			t.paramters.educationId = educationId;
			t.paramters.degreeId = degreeId;
			t.paramters.applyTime = applyTime;
			t.paramters.ccparty = ccparty;
			t.paramters.type = 2;// 发展对象
			t.paramters.phaseCode = t.developmentObject;
			t.paramters.activistTime = activistTime;
			t.paramters.trainer = trainer;
			t.paramters.targetTime = targetTime;
			Ajax.call({
						url : 'org/saveOrUpdatePartymemberForDevelopmentPlan',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(t.paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.notice(data.msg);
								$("#development-object-dialog").modal('toggle');
								DevelopmentProcedure.initTable(
										t.paramters.type, null);
							}
						}
					});
			t.initParamters();
		},
		// ------------------------------------------预备党员----------------------------------------------------------
		// 增加预备党员
		addProbationaryPartyMember : function() {
			var config = {
				dialogType : 'add',
				ccpartyId : Global.ccpartyId,
				ccpartyName : Global.ccpartyName,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/probationary-partyMember-dialogs.html',
									config));
			$("#probationary-partyMember-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
		},
		// 编辑预备党员
		editProbationaryPartyMember : function(data) {
			var config = {
				dialogType : 'edit',
				user : data.item,
				ccpartyId : data.ids,
				ccpartyName : data.names,
				partyMember : data.partyMember,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/probationary-partyMember-dialogs.html',
									config));
			$("#probationary-partyMember-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯

			$("#probationary-partyMember-dialog #userNationName").val(Global
					.getCodeName('A0121.' + data.item.nation));
			$("#probationary-partyMember-dialog #userNationId")
					.val(data.item.nation);
			$("#probationary-partyMember-dialog #userBirthPlaceName")
					.val(Global.getCodeName('A0114.' + data.item.birthPlace));
			$("#probationary-partyMember-dialog #userBirthPlaceId")
					.val(data.item.birthPlace);
			$("#probationary-partyMember-dialog #educationName").val(Global
					.getCodeName('A0405.' + data.item.education));
			$("#probationary-partyMember-dialog #educationId")
					.val(data.item.education);
			$("#probationary-partyMember-dialog #degreeName").val(Global
					.getCodeName('A0440.' + data.item.degree));
			$("#probationary-partyMember-dialog #degreeId")
					.val(data.item.degree);
			$("#probationary-partyMember-dialog input[type=radio][name='gender'][value="
					+ data.item.gender + "]").attr("checked", 'checked');

			PartyMemberRewardsResume.setRewardsInputsBestInfo();
			PartyMemberResume.setResumeInputsBestInfo();
		},
		// 新增或保存预备党员
		saveOrUpdateProbationaryPartyMember : function() {
			var id = $("#userId").val();// 登录账号/ID
			var loginNo = $("#loginNo").val();// 登录账号
			var name = $("#probationary-partyMember-dialog #name").val();// 姓名
			var gender = $("#probationary-partyMember-dialog input[name='gender']:checked")
					.val(); // 性别
			var birthDay = $("#probationary-partyMember-dialog #birthDay")
					.val();// 出生日期
			var userNationId = $("#probationary-partyMember-dialog #userNationId")
					.val(); // 民族
			var userBirthPlaceId = $("#probationary-partyMember-dialog #userBirthPlaceId")
					.val(); // 籍贯
			var mobile = $("#probationary-partyMember-dialog #mobile").val(); // 电话
			var idNumber = $("#probationary-partyMember-dialog #idNumber")
					.val(); // 身份证号
			var address = $("#probationary-partyMember-dialog #address").val();// 地址
			var educationId = $("#probationary-partyMember-dialog #educationId")
					.val();// 学历
			var degreeId = $("#probationary-partyMember-dialog #degreeId")
					.val();// 学位
			var applyTime = $("#probationary-partyMember-dialog #applyTime")
					.val();// 申请日期
			var ccparty = $("#probationary-partyMember-dialog #ccparty")
					.attr("ids");// 所属党组织
			var isDelegate = $("#probationary-partyMember-dialog input[name='isDelegate']:checked")
					.val();// 是否困难党员
			var joinActivity = $("#probationary-partyMember-dialog #joinActivity")
					.val();// 参加组织生活情况
			var introducer = $("#probationary-partyMember-dialog #introducer")
					.val();// 入党介绍人
			var passTime = $("#probationary-partyMember-dialog #passTime")
					.val();// 支部大会通过日期
			var auditTime = $("#probationary-partyMember-dialog #auditTime")
					.val();// 上级组织批准日期
			var fee = $("#probationary-partyMember-dialog #fee").val();// 缴纳党费
			var joinCurrentTime = $("#probationary-partyMember-dialog #joinCurrentTime")
					.val();// 加入当前组织日期
			var joinType = $("#probationary-partyMember-dialog #joinType")
					.val();// 加入组织类型

			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#loginNo").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入姓名。");
				$("#probationary-partyMember-dialog #name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(birthDay)) {
				Notify.notice("请输入出生日期。");
				$("#probationary-partyMember-dialog #birthDay").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(userNationId)) {
				Notify.notice("请选填民族。");
				$("#probationary-partyMember-dialog #userNationName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(idNumber)) {
				Notify.notice("请输入身份证号。");
				$("#probationary-partyMember-dialog #idNumber").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(passTime)) {
				Notify.notice("请输入支部大会通过日期。");
				$("#probationary-partyMember-dialog #passTime").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(auditTime)) {
				Notify.notice("请输入上级组织批准日期。");
				$("#probationary-partyMember-dialog #auditTime").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(joinCurrentTime)) {
				Notify.notice("请输入进入当前党支部日期。");
				$("#probationary-partyMember-dialog #joinCurrentTime").focus();
				return;
			}
			t.paramters.id = id;
			t.paramters.loginNo = loginNo;
			t.paramters.name = name;
			t.paramters.gender = gender;
			t.paramters.birthDay = birthDay;
			t.paramters.userNationId = userNationId;
			t.paramters.userBirthPlaceId = userBirthPlaceId;
			t.paramters.mobile = mobile;
			t.paramters.idNumber = idNumber;
			t.paramters.address = address;
			t.paramters.educationId = educationId;
			t.paramters.degreeId = degreeId;
			t.paramters.applyTime = applyTime;
			t.paramters.ccparty = ccparty;
			t.paramters.type = 3;// 预备党员
			t.paramters.phaseCode = t.probationaryPartyMember;
			t.paramters.isDelegate = isDelegate;
			t.paramters.joinActivity = joinActivity;
			t.paramters.introducer = introducer;
			t.paramters.passTime = passTime;
			t.paramters.auditTime = auditTime;
			t.paramters.fee = fee;
			t.paramters.joinCurrentTime = joinCurrentTime;
			t.paramters.joinType = joinType;
			Ajax.call({
						url : 'org/saveOrUpdatePartymemberForDevelopmentPlan',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(t.paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.notice(data.msg);
								$("#probationary-partyMember-dialog")
										.modal('toggle');
								DevelopmentProcedure.initTable(
										t.paramters.type, null);
							}
						}
					});
			t.initParamters();
		},
		// ------------------------------------------正式党员----------------------------------------------------------
		// 增加党员
		addPartyMember : function() {
			var config = {
				dialogType : 'add',
				ccpartyId : Global.ccpartyId,
				ccpartyName : Global.ccpartyName,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/partyMember-dialogs.html',
									config));
			$("#partyMember-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
		},
		// 编辑正式党员
		editPartyMember : function(data) {
			var config = {
				dialogType : 'edit',
				user : data.item,
				ccpartyId : data.ids,
				ccpartyName : data.names,
				partyMember : data.partyMember,
				random : new Date().getTime()
			}
			$.extend(config, Global);
			$("#dialogs")
					.html(nunjucks
							.render(
									Global.appName
											+ '/tpl/workspace/dialogs/development/partyMember-dialogs.html',
									config));
			$("#partyMember-dialog").modal({});
			SelectTree.initSelectTree("#userNation-tree",
					'sys/getCodeTreeByParentId', "A0121"); // 民族
			SelectTree.initSelectTree("#userBirthPlace-tree",
					'sys/getCodeTreeByParentId', "A0114"); // 籍贯
			$("#partyMember-dialog #userNationName").val(Global
					.getCodeName('A0121.' + data.item.nation));
			$("#partyMember-dialog #userNationId").val(data.item.nation);
			$("#partyMember-dialog #userBirthPlaceName").val(Global
					.getCodeName('A0114.' + data.item.birthPlace));
			$("#partyMember-dialog #userBirthPlaceId")
					.val(data.item.birthPlace);
			$("#partyMember-dialog #educationName").val(Global
					.getCodeName('A0405.' + data.item.education));
			$("#partyMember-dialog #educationId").val(data.item.education);
			$("#partyMember-dialog #degreeName").val(Global
					.getCodeName('A0440.' + data.item.degree));
			$("#partyMember-dialog #degreeId").val(data.item.degree);
			$("#partyMember-dialog input[type=radio][name='gender'][value="
					+ data.item.gender + "]").attr("checked", 'checked');

			PartyMemberRewardsResume.setRewardsInputsBestInfo();
			PartyMemberResume.setResumeInputsBestInfo();
		},
		// 新增或保存正式党员
		saveOrUpdatePartyMember : function() {
			var id = $("#userId").val();// 登录账号/ID
			var loginNo = $("#loginNo").val();// 登录账号
			var name = $("#partyMember-dialog #name").val();// 姓名
			var gender = $("#partyMember-dialog input[name='gender']:checked")
					.val(); // 性别
			var birthDay = $("#partyMember-dialog #birthDay").val();// 出生日期
			var userNationId = $("#partyMember-dialog #userNationId").val(); // 民族
			var userBirthPlaceId = $("#partyMember-dialog #userBirthPlaceId")
					.val(); // 籍贯
			var mobile = $("#partyMember-dialog #mobile").val(); // 电话
			var idNumber = $("#partyMember-dialog #idNumber").val(); // 身份证号
			var address = $("#partyMember-dialog #address").val();// 地址
			var educationId = $("#partyMember-dialog #educationId").val();// 学历
			var degreeId = $("#partyMember-dialog #degreeId").val();// 学位
			var applyTime = $("#partyMember-dialog #applyTime").val();// 申请日期
			var ccparty = $("#partyMember-dialog #ccparty").attr("ids");// 所属党组织
			var isDelegate = $("#partyMember-dialog input[name='isDelegate']:checked")
					.val();// 是否困难党员
			var joinActivity = $("#partyMember-dialog #joinActivity").val();// 参加组织生活情况
			var fee = $("#partyMember-dialog #fee").val();// 缴纳党费
			var joinCurrentTime = $("#partyMember-dialog #joinCurrentTime")
					.val();// 加入当前组织日期
			var joinType = $("#partyMember-dialog #joinType").val();// 加入组织类型

			if (CheckInputUtils.isEmpty(loginNo)) {
				Notify.notice("请输入登录账号。");
				$("#loginNo").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入姓名。");
				$("#partyMember-dialog #name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(birthDay)) {
				Notify.notice("请输入出生日期。");
				$("#partyMember-dialog #birthDay").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(userNationId)) {
				Notify.notice("请选填民族。");
				$("#partyMember-dialog #userNationName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(idNumber)) {
				Notify.notice("请输入身份证号。");
				$("#partyMember-dialog #idNumber").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(joinCurrentTime)) {
				Notify.notice("请输入进入当前党支部日期。");
				$("#partyMember-dialog #joinCurrentTime").focus();
				return;
			}
			t.paramters.id = id;
			t.paramters.loginNo = loginNo;
			t.paramters.name = name;
			t.paramters.gender = gender;
			t.paramters.birthDay = birthDay;
			t.paramters.userNationId = userNationId;
			t.paramters.userBirthPlaceId = userBirthPlaceId;
			t.paramters.mobile = mobile;
			t.paramters.idNumber = idNumber;
			t.paramters.address = address;
			t.paramters.educationId = educationId;
			t.paramters.degreeId = degreeId;
			t.paramters.applyTime = applyTime;
			t.paramters.ccparty = ccparty;
			t.paramters.type = 4;// 正式党员
			t.paramters.phaseCode = t.partyMember;
			t.paramters.isDelegate = isDelegate;
			t.paramters.joinActivity = joinActivity;
			t.paramters.fee = fee;
			t.paramters.joinCurrentTime = joinCurrentTime;
			t.paramters.joinType = joinType;
			Ajax.call({
						url : 'org/saveOrUpdatePartymemberForDevelopmentPlan',
						p : {
							paramters : "{'data':"
									+ JSON.stringify(t.paramters, "data") + "}"
						},
						f : function(data) {
							if (data.success) {
								Notify.notice(data.msg);
								$("#partyMember-dialog").modal('toggle');
								DevelopmentProcedure.initTable(
										t.paramters.type, null);
							}
						}
					});
			t.initParamters();
		}
	}
	return t;
}();