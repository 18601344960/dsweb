/**
 * @description 转出党员历史
 * @author 赵子靖
 * @since 2016-08-23
 */
var PartyMemberExport = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initEvent();
        },
        initEvent : function() {
        },
        render : function() {
            $("#export-member-table").bootstrapTable('destroy');
            $("#export-member-table").bootstrapTable({
                queryParams : function(params) {
                    $.extend(params, {
                        ccpartyId : PartyMember.ccpartyId
                    })
                    return params;
                }
            });
        },
        //彻底删除党员
        realDeletePartymember:function(id){
            bootbox.confirm({
                size : 'small',
                message : "确认永久移除该党员？",
                callback : function(result) {
                     if (result) {
                         Ajax.call({
                           url : "org/realDeletePartymember",
                           p : {
                               id:id
                           },
                           f : function(data) {
                               if (data.success) {
                                   Notify.success(data.msg);
                                   t.render();
                               } else {
                                   Notify.error(data.msg);
                               }
                           }
                       });
                     }
                }
           });
        },
        //恢复
        recoverPartyMember:function(id){
            bootbox.confirm({
                size : 'small',
                message : "确认恢复该党员？",
                callback : function(result) {
                     if (result) {
                         Ajax.call({
                           url : "org/recoverPartyMember",
                           p : {
                               id:id
                           },
                           f : function(data) {
                               if (data.success) {
                                   Notify.success(data.msg);
                                   t.render();
                               } else {
                                   Notify.error(data.msg);
                               }
                           }
                       });
                     }
                }
           });
        },
        gender:function(value,row){
            if(value){
                return Global.getCodeName('A0107.'+value);
            }
        },
        memberType:function(value,row){
            if(value){
                return Global.getEnumName('obt_party_member.type.'+value.type);
            }
        },
        joinCurrentTime:function(value,row){
            if(value){
                return Global.getDate(value.joinCurrentTime);
            }
        },
        memberStatus:function(value,row){
            if(value){
                return Global.getEnumName('obt_party_member.status.'+value.status);
            }
        },
        updateTime:function(value,row){
            if(value){
                return value.updateTime;
            }
        },
        updateUser:function(value,row){
            if(value && value.user){
                return value.user.name;
            }
        },
        //操作
        operator:function(value,row){
            var html = "";
            html += '<a href="javascript:PartyMemberExport.recoverPartyMember(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="fa fa-undo"></i>恢复</a>';
            html += '<a href="javascript:PartyMemberExport.realDeletePartymember(\'' + row.id + '\')" class="btn btn-default btn-xs"><i class="fa fa-times"></i>移除</a>'
            return html;
        }
    }
    return t;
}();