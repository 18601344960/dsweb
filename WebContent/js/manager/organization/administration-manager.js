/**
 * @description 行政组织管理
 * @author zhaozijing
 * @since 2015-08-17
 */
var AdministrationManager = function() {
	var t = {
		mainContainer : '',
		type:'0',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
			Department.init();
		},
		initView : function() {
		},
		initEvent : function() {
			//标签事件监听
			$(document).on("show.bs.tab", '#administration-tabs a[data-toggle="tab"]', function(e) {
				var tabId = $(e.target).attr('href');
				if (tabId == "#administration-manager") {
					t.render();
				} else if (tabId == "#department-list") {
					Department.render();
				}
			});
			//行政组织树监听事件
			$(document).on("click", "#parentOrganizationId", function() {
				$("#treeTrId").show();
				$("#organizationMenuContent").slideDown("fast");
				$("body").bind("mousedown", t.onOrganizationBodyDown);
			});
			
			$(document).on("click", "#add-administration-btn", function() {
				t.addAdministration();
			});
			$(document).on("click", "#delete-administration-btn", function() {
				t.deleteOrganization();
			});
		},
		render : function() {
			//获取组织详细信息
			Ajax.call({
				url : "org/getOrganizationById",
				p : {
					organizationId:AdministrationNavigation.administrationId
				},
				f : function(data) {
					if (data && data.item) {
						data.item.flag="edit";
						if(data.parent==null){
							data.item.parentOrganizationId='root';
							data.item.parentOrganizationName='root';
						}else{
							data.item.parentOrganizationId=data.parent.id;
							data.item.parentOrganizationName=data.parent.name;
						}
						$("#administration-manager").html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-info-manager.html',data.item));
						t.initOrganizationSubjectTree();
						$("#status").val(data.item.status);
					}else{
						Notify.error("获取行政组织详情信息异常");
					}
				}
			});
		},
		//新增行政组织事件
		addAdministration:function(){
			var data = new Object();
			data.parentOrganizationId=AdministrationNavigation.administrationId;
			data.parentOrganizationName=AdministrationNavigation.administrationName;
			data.flag='add';
			data.sequence=10000;
			$("#administration-right").html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-info-manager.html',data));
			if(AdministrationNavigation.administrationId=='000'){
				$("#parentId").val("");
				$("#parentIdSpan").text("");
			}
			t.initOrganizationSubjectTree();
			$("#administration-buttons").hide();
			$("#tips").show();
		},
		//验证ID是否占用
		checkOrganizationId:function(myselfId){
			if(myselfId==null || myselfId==''){
				Notify.notice("请输入单位ID");
				$("#myselfId").focus();
			}else{
				var parentId = $("#parentId").val();
				var checkId = parentId+myselfId;
				Ajax.call({
					url : "org/checkOrganizationId",
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
		//删除
		deleteOrganization:function(){
			bootbox.confirm({
			     size : 'small',
			     message : "确认删除"+$("#name").val()+"？",
			     callback : function(result) {
			          if (result) {
			        	  Ajax.call({
			  				url : "org/delOrganization",
			  				p : {
			  					id:$("#organizationId").val()
			  				},
			  				f : function(data) {
			  					if (data.success) {
			  						Notify.success(data.msg);
			  						$("#administration-right").html("");
			  						AdministrationNavigation.initAdministrationSubjectTree();
			  					} else {
			  						Notify.error(data.msg);
			  					}
			  				}
			  			});
			          }
			     }
			});
		},
		//行政组织树
		initOrganizationSubjectTree : function() {
			ComTree.initTree({
				divContainer : "#organization-tree",
				url : 'org/getCurrentOrganizationAndSunsToTreeView',
				p : {
					organizationId : Global.organizationId
				},
				onClick : t.organizationClickCallBack
			});
		},
		organizationClickCallBack:function(e, treeId, treeNode) {
			$("#parentOrganizationId").attr("ids",treeNode.id);
			$("#parentOrganizationId").val(treeNode.name);
			$("#treeTrId").hide();
		},
		//鼠标失去焦点
		onOrganizationBodyDown : function(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "organizationMenuContent" || $(event.target).parents("#organizationMenuContent").length>0)) {
				$("#organizationMenuContent").fadeOut("fast");
				$("body").unbind("mousedown", t.onBodyDown);
			}
		},
		//保存党组织
		saveOrUpdateAdministration:function(){
			var id = $("#organizationId").val();	 			//ID
			var name = CheckInputUtils.splitSpaceForStrAround($("#name").val()); 	// 组织名称
			var representative = $("#representative").val();						//法定代表人
			var parentOrganizationId = $("#parentOrganizationId").attr("ids");
			var address = $("#address").val();					 //单位地址
			var status = $("#status").val();					//状态
			var sequence = $("#sequence").val();	//序号
			if(CheckInputUtils.isEmpty(id)){
				//设置ID
				id = $("#parentId").val()+$("#myselfId").val();
			}
			if (CheckInputUtils.isEmpty(name)) {
				Notify.notice("请输入组织名称");
				$("#name").focus();
				return;
			}
			if (CheckInputUtils.isEmpty(parentOrganizationId)) {
				Notify.notice("请选择上级组织");
				$("#parentOrganizationId").focus();
				return;
			}
			Ajax.call({
				url : "org/saveOrUpdateOrganization",
				p : {
					id:id,
					sequence:sequence,
					name:name,
					representative:representative,
					parentId:parentOrganizationId,
					address:address,
					status:status
				},
				f : function(data) {
					if (data.success) {
						Notify.success(data.msg);
						AdministrationNavigation.administrationId=id;
						$("#administration-right").html(nunjucks.render(Global.appName + '/tpl/manager/organization/administration-manager.html'));
						//左边组织树重新加载
						AdministrationNavigation.initAdministrationSubjectTree();
						var treeObj = $.fn.zTree.getZTreeObj("administration-navigation-tree" );
						var treenode = treeObj.getNodeByParam("id" , id, null);
						treeObj.expandNode(treenode, true, false);
						treeObj.selectNode(treenode);
						t.render();
					} else {
						Notify.error(data.msg);
					}
				}
			});
		}
		
	}
	return t;
}();