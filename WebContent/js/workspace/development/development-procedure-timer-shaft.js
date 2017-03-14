/**
 * @description 党员发展时间轴
 * @author 赵子靖
 * @since 2016-07-16
 */
var TimerShaft = function() {
    var t = {
        mainContainer : '',
        id : '',
        init : function(mainContainer,id) {
            t.mainContainer = mainContainer;
            t.id = id;
            t.render();
        },
        initEvent : function() {
            $(function(){
                $('label').click(function(){
                    $('.event_year>li').removeClass('current');
                    $(this).parent('li').addClass('current');
                    var year = $(this).attr('for');
                    $('#'+year).parent().prevAll('div').slideUp(800);
                    $('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
                });
            });
        },
        render : function() {
            Global.initLoader(t.mainContainer);
            Ajax.call({
                url : 'org/getPartymemberDevelopmentProcedureByUser',
                p : {
                    userId:t.id
                },
                f : function(data) {
                    if (data.item) {
                        $.extend(data, Global);
                        $(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/workspace/development/timer-shaft.html', data));
                        t.initEvent();
                    }
                }
            });
        }
    }
    return t;
}();