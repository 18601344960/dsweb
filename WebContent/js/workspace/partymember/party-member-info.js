/**
 * @description 党员管理-党员信息维护
 * @author zhaozijing
 * @since 2015-07-14
 */
var PartyMemberInfo = function() {
	var t = {
		mainContainer : '',
		init : function(mainContainer) {
			t.mainContainer = mainContainer;
			t.initView();
			t.initEvent();
		},
		initView : function() {
		},
		initEvent : function() {
			$(document).on("click", "#ccpartyId", function() {
				t.initSubjectTree();
				var ccpartyIdObj = $("#ccpartyId");
				var ccpartyIdOffset = $("#ccpartyId").offset();
				$("#ccpartyMenuContent").css({}).slideDown("fast");
				$("body").bind("mousedown", t.onCcpartyBodyDown);
			});
		},
		//鼠标失去焦点
		onCcpartyBodyDown : function(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "ccpartyMenuContent" || $(event.target).parents("#ccpartyMenuContent").length>0)) {
				$("#ccpartyMenuContent").fadeOut("fast");
				$("body").unbind("mousedown", t.onBodyDown);
			}
		},
		//根据会员ID加载党员信息
		loadPartyMemberInfo : function(userId, pId) {
			if(userId==null || userId==''){
			    /*obj = {
			            pId : pId
			    }
				$("#right-partymember-info").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-partyinfo.html', obj));
				$("#partymember-table #ccpartyId").attr("ids",PartyMember.ccpartyId);
				$("#partymember-table #ccpartyId").val(PartyMember.ccpartyName);*/
			}else{
				Ajax.call({
					url : 'org/loadPartyMemberInfoByUserId',
					p : {
						userId : userId
					},
					f : function(data) {
						if (data && data.item) {
							PartyMember.userId = data.item.id;
							data.item.globalUserType=Global.userType;
							data.item.success = data.success;
							data.item.joinCurrentTime=Global.getDate(data.item.joinCurrentTime);
							data.item.passTime=Global.getDate(data.item.passTime);
							data.item.auditTime=Global.getDate(data.item.auditTime);
							data.item.applyTime=Global.getDate(data.item.applyTime);
							data.item.activistTime=Global.getDate(data.item.activistTime);
							data.item.targetTime=Global.getDate(data.item.targetTime);
							$("#right-partymember-info").html(nunjucks.render(Global.appName + '/tpl/workspace/partymember/party-member-partyinfo.html', data.item));
							$("#partymember-type-select").val(data.item.type);
							$("input[type=radio][name='partymember-repre'][value=" + data.item.isDelegate + "]").attr("checked", 'checked');
							$("input[type=radio][name='partymember-worker'][value=" + data.item.isManager + "]").attr("checked", 'checked');
							$("input[type=radio][name='partymember-harder'][value=" + data.item.isDestitution + "]").attr("checked", 'checked');
							$("input[type=radio][name='partymember-isMobileMember'][value=" + data.item.isMobileMember + "]").attr("checked", 'checked');
							$("#partymember-jointype-select").val(data.item.joinType);
							//$("#partymember-live-select").val(data.item.joinActivity);
						} else {
							Notify.error("加载党员信息失败");
						}
					}
				});
			}
		},
		// 党组织树
        initSubjectTree : function() {
            var setting = {
                check : {
                    enable : true,
                    chkStyle : "radio",
                    radioType : "all"
                },
                view : {
                    dblClickExpand : false
                },
                data : {
                    simpleData : {
                        enable : true
                    }
                },
                callback : {
                    onClick : t.treeClick,
                    onCheck : t.treeCheck
                }
            };
            Ajax.call({
                url : 'org/getTreeCCPartyAndLowerLevel',
                p : {
                    ccpartyId : Global.ccpartyId
                },
                async:false,
                f : function(data) {
                    if (data && data.items) {
                        var treeObj = $.fn.zTree.init($("#ccparty-tree"), setting, data.items);
                        var nodes = treeObj.transformToArray (treeObj.getNodes());
                        for(var i=0;i<nodes.length;i++){
                            if('Z000104'!=nodes[i].type){
                                //党支部，radio禁选
                                treeObj.setChkDisabled(nodes[i], 'disabled');
                            }
                        }
                    } else {
                        Notify.error("获取组织树失败");
                    }
                }
            });
        },
		treeClick : function(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("ccparty-tree");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		},
		treeCheck : function(e, treeId, treeNode) {
			var ccpartyIdObj = $("#partymember-table #ccpartyId");
			ccpartyIdObj.val(treeNode.name);
			ccpartyIdObj.attr("ids", treeNode.id);
		},
		//党员信息校验
		checkPartymemberInput:function(partymember_paramter){
			partymember_paramter.id=$("#partymember-id").val(); // 党员ID
			partymember_paramter.type = $("#partymember-type-select").val(); // 党员类型
			//partymember_paramter.isDelegate = $("input[name='partymember-repre']:checked").val();
			//partymember_paramter.isManager = $("input[name='partymember-worker']:checked").val();
			//partymember_paramter.isDestitution = $("input[name='partymember-harder']:checked").val();
			partymember_paramter.joinTime = $("#partymember-joinTime").val();
			partymember_paramter.joinCurrentTime = $("#partymember-joinCurrentTime").val();
			partymember_paramter.joinType = $("#partymember-jointype-select").val();
			partymember_paramter.ccpartyId = $("#partymember-table #ccpartyId").attr("ids");
			partymember_paramter.introducer = $("#partymember-introducer").val();
			partymember_paramter.passTime = $("#partymember-passTime").val();
			partymember_paramter.auditTime = $("#partymember-auditTime").val();
			//partymember_paramter.isMobileMember = $("input[name='partymember-isMobileMember']:checked").val();
			var fee = $("#partymember-fee").val();
			if(!CheckInputUtils.isEmpty(fee)){
			    if (!CheckInputUtils.moneyCheck(fee)) {
			        $("#partymember-fee").focus();
			        return;
			    }
			}
			partymember_paramter.fee = fee;
			//partymember_paramter.joinActivity = $("#partymember-live-select").val();
			partymember_paramter.applyTime = $("#partymember-applyTime").val();
			partymember_paramter.activistTime = $("#partymember-activistTime").val();
			partymember_paramter.targetTime = $("#partymember-targetTime").val();
			partymember_paramter.trainer = $("#partymember-trainer").val();
			return partymember_paramter;
		},
		//保存或修改党员信息和用户信息统一处理
		updateOrSavePartymemberAndUser:function(){
			var paramters = [];
			var user_paramter = {};
			var partymember_paramter = {};
			//step1、用户信息验证
			user_paramter = PartyMemberBaseInfo.checkUserInput(user_paramter);
			if(user_paramter==null){
				return;
			}
			paramters.push(user_paramter);
			//step2、党员信息验证
			partymember_paramter=PartyMemberInfo.checkPartymemberInput(partymember_paramter);
			if(partymember_paramter==null){
				return;
			}
			paramters.push(partymember_paramter);
			//step3、合并成json串提交后台处理
			Ajax.call({
				url : 'org/saveOrUpdatePartymemberAndUser',
				p : {
				    ccpartyId : PartyMember.ccpartyId,
					paramters : "{'data':" + JSON.stringify(paramters, "data") + "}"
				},
				f : function(data) {
					if(data && data.success){
						Notify.success(data.msg);
						//左侧党员树、用户信息、党员信息重新加载
						if(user_paramter.id==null || user_paramter.id==''){
							PartyMember.userId=user_paramter.idNumber;
						}
						PartyMember.userId=user_paramter.id;
						PartyMember.getPartymembers(PartyMember.ccpartyId);
						PartyMemberBaseInfo.loadUserInfo(PartyMember.userId);
						PartyMemberInfo.loadPartyMemberInfo(PartyMember.userId);
					}else{
						Notify.error(data.msg);
					}
				}
			});
		},
		//假删除党员
		fakeDeletePartymember:function(id){
			bootbox.confirm({
			     size : 'small',
			     message : "确认删除该党员？",
			     callback : function(result) {
			          if (result) {
			        	  Ajax.call({
			  				url : "org/fakeDeletePartymember",
			  				p : {
			  					id:id
			  				},
			  				f : function(data) {
			  					if (data.success) {
			  						Notify.success(data.msg);
			  						PartyMember.getPartymembers(PartyMember.ccpartyId);
			  						$("#right-user-info").html('<p style="text-align: center;">请选择左侧党员</p>');
			  						$("#right-partymember-info").html('<p style="text-align: center;">请选择左侧党员</p>');
			  					} else {
			  						Notify.error(data.msg);
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