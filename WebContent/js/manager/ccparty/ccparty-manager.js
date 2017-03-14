/**
 * @description 党组织管理
 * @author zhaozijing
 * @since 2015-08-13
 */
var CcpartyManager = function() {
	var t = {
		mainContainer : '',
		type:'0',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
			CcpartyLeader.init();
			CcpartyLeaderBackup.init();
		},
		initView : function() {
		},
		initEvent : function() {
			//行政组织树监听事件
			$(document).on("click", "#ccpartyOrganizationId", function() {
				$("#organizationMenuContent").slideDown("fast");
				$("body").bind("mousedown", t.onOrganizationBodyDown);
			});
			//党组织树监听事件
			$(document).on("click", "#parentCcpartyId", function() {
				$("#ccpartyMenuContent").slideDown("fast");
				$("body").bind("mousedown", t.onCcpartyBodyDown);
			});
			
			$(document).on("click", "#add-ccparty-btn", function() {
				t.addCcparty();
			});
			$(document).on("click", "#delete-ccparty-btn", function() {
				t.deleteCcparty();
			});
		},
		render : function() {
			//获取党组织详细信息
			Ajax.call({
				url : "org/getCcpartyById",
				p : {
					ccpartyId:CcpartyNavigation.ccpartyId
				},
				f : function(data) {
					if (data && data.item) {
					    data.Global=Global;
						$("#ccparty-right").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-manager.html',data));
						data.item.flag="edit";
						if(data.parent==null){
							data.item.parentCcpartyId='root';
							data.item.parentCcpartyName='root';
						}else{
							data.item.parentCcpartyId=data.parent.id;
							data.item.parentCcpartyName=data.parent.name;
						}
						$("#ccparty-manager").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-info-manager.html',data));
						t.initCcpartySubjectTree();
						$("#isTip").val(data.item.isTip);
						$("#status").val(data.item.status);
						t.loadCcpartyType(data.item.type);
						$("#ccparty-buttons").show();
						$("#ccparty-group-table").bootstrapTable({
			                queryParams : function(params) {
			                    $.extend(params, {
			                        ccpartyId : CcpartyNavigation.ccpartyId,
			                    })
			                    return params;
			                }
			            });
					}else{
						Notify.error("获取党组织详情信息异常");
					}
				}
			});
		},
		//加载党组织类型
		loadCcpartyType:function(currentType){
			$("#type option").remove();
			Ajax.call({
				url : "sys/getCodeListByParentId",
				p : {
					parentId:'Z0001'
				},
				f : function(data) {
					if (data && data.rows) {
						for(var i=0;i<data.rows.length;i++){
							if(currentType==data.rows[i].id){
								$("#type").append( "<option selected='selected' value='"+data.rows[i].id+ "'>"+data.rows[i].name+"</option>" );
							}else{
								$("#type").append( "<option value='"+data.rows[i].id+ "'>"+data.rows[i].name+"</option>" );
							}
						}
					}
				}
			});
		},
		//新增党组织事件
		addCcparty:function(){
			var data = new Object();
			data.item =new Object();
			data.item.parentCcpartyId=CcpartyNavigation.ccpartyId;
			data.item.parentCcpartyName=CcpartyNavigation.ccpartyName;
			data.item.flag='add';
			data.Global=Global;
			data.item.sequence=10000;
			$("#ccparty-right").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-info-manager.html',data));
			if(CcpartyNavigation.ccpartyId=='000'){
				$("#parentId").val("");
				$("#parentIdSpan").text("");
			}
			t.initCcpartySubjectTree();
			t.loadCcpartyType();
			$("#tips").show();
		},
		//党组织树
		initCcpartySubjectTree : function() {
			ComTree.initTree({
				divContainer : "#ccparty-tree",
				url : 'org/getTreeCCPartyAndLowerLevel',
				p : {
					ccpartyId : Global.ccpartyId
				},
				onClick : t.ccpartyClickCallBack
			});
		},
		ccpartyClickCallBack:function(e, treeId, treeNode) {
			$("#parentCcpartyId").attr("ids",treeNode.id);
			$("#parentCcpartyId").val(treeNode.name);
		},
		//鼠标失去焦点
		onCcpartyBodyDown : function(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#ccpartyMenuContent").length>0)) {
				$("#ccpartyMenuContent").fadeOut("fast");
				$("body").unbind("mousedown", t.onBodyDown);
			}
		},
		//删除
		deleteCcparty:function(){
			bootbox.confirm({
			     size : 'small',
			     message : "确认删除"+$("#ccpartyName").val()+"？",
			     callback : function(result) {
			          if (result) {
			        	  Ajax.call({
			  				url : "org/delCCParty",
			  				p : {
			  					ccpartyId:$("#ccpartyId").val()
			  				},
			  				f : function(data) {
			  					if (data.success) {
			  						Notify.success(data.msg);
			  						$("#ccparty-right").html("");
			  						CcpartyNavigation.initCcpartySubjectTree();
			  					} else {
			  						Notify.error(data.msg);
			  					}
			  				}
			  			});
			          }
			     }
			});
		},
		//验证ID是否占用
		checkCcpartyId:function(myselfId){
			if(myselfId==null || myselfId==''){
				Notify.notice("请输入党组织ID");
				$("#myselfId").focus();
			}else{
				var parentId = $("#parentId").val();
				var checkId = parentId+myselfId;
				Ajax.call({
					url : "org/checkCcpartyId",
					p : {
						checkId:checkId
					},
					f : function(data) {
						if (data.success) {
							Notify.notice(data.msg);
							$("#myselfId").focus();
						}
					}
				});
			}
			
		},
		//将届满日期加3年
		addThreeYears:function(documentTime){
			if(documentTime!=null && documentTime!=''){
				expirationTime = documentTime.substring(0,4)*1+3;
				$("#expirationTime").val(expirationTime+documentTime.substring(4,documentTime.length));
			}
		},
		//保存党组织
		saveOrUpdateCcparty:function(){
			var id = $("#ccpartyId").val();	 			//ID
			var name = CheckInputUtils.splitSpaceForStrAround($("#ccpartyName").val()); 	// 组织名称
			var type = $("#type").val();				//类型
			var parentCcpartyId = $("#parentCcpartyId").attr("ids");
			var documentTime = $("#documentTime").val(); // 换届日期
			var expirationTime = $("#expirationTime").val(); 	// 届满日期
			var isTip = $("#isTip").val();						//是否换届提醒
			var status = $("#status").val();					//状态
			var sequence = $("#sequence").val();	//序号
			if(CheckInputUtils.isEmpty(id)){
				//设置ID
				id = $("#parentId").val()+$("#myselfId").val();
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入组织名称");
				$("#ccpartyName").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(parentCcpartyId)) {
				Notify.notice("请选择上级党组织");
				$("#parentCcpartyId").focus();
				return;
			}
			Ajax.call({
				url : "org/saveOrUpdateCcparty",
				p : {
					id:id,
					sequence:sequence,
					name:name,
					type:type,
					parentCcpartyId:parentCcpartyId,
					documentTime:documentTime,
					expirationTime:expirationTime,
					isTip:isTip,
					status:status
				},
				f : function(data) {
					if (data.success) {
						Notify.success(data.msg);
						
						$("#ccparty-right").html(nunjucks.render(Global.appName + '/tpl/manager/ccparty/ccparty-manager.html'));
						CcpartyNavigation.ccpartyId=id;
						//左边组织树重新加载
						CcpartyNavigation.initCcpartySubjectTree();
						var treeObj = $.fn.zTree.getZTreeObj("ccparty-navigation-tree" );
						var treenode = treeObj.getNodeByParam("id" , id, null);
						treeObj.expandNode(treenode, true, false);
						treeObj.selectNode(treenode);
						t.render();
					} else {
						Notify.error(data.msg);
					}
				}
			});
		},
		// 跳转组织电子活动证
        openCcpartyCard : function() {
            MyCcpartyCard.initParamter();
            MyCcpartyCard.getCcpartyCardDialog(CcpartyNavigation.ccpartyId);
//            CcpartyCard.init(CcpartyNavigation.ccpartyId);
//            var config = {
//                getYearForSelectOption : Global.getYearForSelectOption
//            };
//            $("#dialogs").html(nunjucks.render(Global.appName + '/tpl/workspace/ccparty-card/dialogs/ccparty-card-dialog.html',config));
//            CcpartyCard.loadCcpartyCard();
//            $("#ccparty-card-dialog").modal({});
        }
		
	}
	return t;
}();