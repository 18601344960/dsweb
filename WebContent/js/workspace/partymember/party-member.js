/**
 * @description 党员管理
 * @author zhaozijing
 * @since 2015-07-06
 */
var PartyMember=function(){
	var t = {
			mainContainer:'',
			ccpartyId:Global.ccpartyId,
			currentCcpartyId : '',
			ccpartyName:'',
			userId:'',
			userName:'',
			init:function(mainContainer){
				t.mainContainer = mainContainer;
				t.initView();
				t.initEvent();
				PartyMemberBaseInfo.init(t.mainContainer);
				PartyMemberInfo.init(t.mainContainer);
				CcpartyLeader.init("#partyMemberContent");
				PartyGroupMember.init("#group-member-tab");
			},
			initView:function(){
			},
			initEvent:function(){
			    $(document).on("show.bs.tab", '#members-manager-tab a[data-toggle="tab"]', function(e) {
	                var tabId = $(e.target).attr('href');
	                if (tabId == "#group-member-tab") {
	                    PartyGroupMember.render(t.currentCcpartyId);
	                }else if(tabId == "#export-member-tab"){
	                    PartyMemberExport.render(t.currentCcpartyId);
	                }else if (tabId == "#election-tab") {
	                    CcpartyLeader.showElectionInfo('branch-election-info');
	                }
	            });
		    },
		    render : function() {
		    	$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member.html'));
		    	//加载左边内容
		    	t.initCcpartySubjectTree();	//加载组织树
		    	t.getPartymembers(t.ccpartyId);
		    	var zTree = $.fn.zTree.getZTreeObj("ccparty-trees");
		    	var nodes = zTree.getSelectedNodes();
		    	if (nodes.length != 0) {
	                t.ccpartyId = nodes[0].id;
	                if (nodes[0].type == 'Z000101' || nodes[0].type == 'Z000102') {
	                    // 党组或党委
	                    CcpartyLeader.render();
	                } else {
	                    $("#partymember-left").hide();
	                    $("#partyMemberContent").css("margin-left", "0px");
	                    // 其他党支部
	                    $("#partyMemberContent").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-info.html'));
	                    // 加载党员
	                    t.getPartymembers(nodes[0].id);
	                    t.currentCcpartyId = nodes[0].id;
	                    $("#right-user-info").html('<p style="text-align: center;">请选择左侧党员</p>');
	                    $("#right-partymember-info").html('<p style="text-align: center;">请选择左侧党员</p>');
	                }
	            }
		    	$("#right-partymember-info").html('<p style="text-align: center;">请选择左侧党员</p>');
			},
			//组织树获取
			initCcpartySubjectTree : function() {
				ComTree.initTree({
	                divContainer : "#ccparty-trees",
	                url : 'org/getCcpartyTreeByParentId',
	                async : false, // false同步 true异步
	                selectedFirstNode : true,
	                lowerAsyncEnable : true,// 下级数据方式 true异步
	                p : {
	                    parentId : Global.ccpartyId,
	                    ccpartyId : Global.ccpartyId
	                },
	                onClick : t.ccpartyClick
	            });
			},
			//根据组织获取党员列表
			getPartymembers : function(ccpartyId) {
				Ajax.call({
					url : 'uam/getUsersByCcparty',
					p : {
						ccpartyId : ccpartyId
					},
					f : function(data) {
						if (data && data.items) {
							data.random=Math.random();
							$("#partymember-center").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/load-partymember.html',data));
							$("#"+PartyMember.userId).attr("class","partymembers_click");
						}else{
							Notify.error("加载人员列表失败");
						}
					}
				});
			},
			//组织树点击事件
			ccpartyClick : function(e, treeId, treeNode) {
				t.ccpartyId = treeNode.id;
	            t.ccpartyName = treeNode.name;
	            t.userId = "";
	            t.userName = "";
	            if (treeNode.type == 'Z000101' || treeNode.type == 'Z000102') {
	                // 党组或党委
	                CcpartyLeader.render();
	            } else {
	                // 其他党支部
	                $("#partyMemberContent").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-info.html',Global));
	                $("#partymember-manager-tab").find("li").removeClass();
	                $("#partymember-manager-tab").find("li:first").addClass('active');

	                // 根据党组织ID去加载党员列表
	                t.getPartymembers(treeNode.id);
	                t.currentCcpartyId = treeNode.id
	                $("#right-user-info").html('<p style="text-align: center;">请选择左侧党员</p>');
	                $("#right-partymember-info").html('<p style="text-align: center;">请选择左侧党员</p>');
	            }
			},
			mouseOve:function($this){
				if($this.className!="partymembers_click"){
					$this.className="partymembers_select";
				}
			},
			mouseOut:function($this){
				if($this.className!='partymembers_click'){
					$this.className="partymembers";
				}
			},
			onClick:function($this,partymemberId,partymemberName){
				t.userId=partymemberId;
				t.userId=partymemberName;
				$(".chats li").removeClass("partymembers_click");
				$(".chats li").addClass("partymembers");
				$this.className="partymembers_click";
		    	//加载用户信息和党员信息
				t.loadUserAndPartymemberInfo(partymemberId);
				$("#right-user-info-tab").addClass("active");
				$("#right-partymember-info-tab").removeClass("active");
			},
			//加载用户信息和党员信息
			loadUserAndPartymemberInfo:function(id){
				PartyMemberBaseInfo.loadUserInfo(id);
				//tab恢复
				$("#baseinfoTab").attr("class","active");
				$("#partymemberInfoTab").attr("class","");
				$("#right-user-info").attr("class","tab-pane active partymember_content");
				$("#right-partymember-info").attr("class","tab-pane partymember_content");
			}
	}
	return t;
}();