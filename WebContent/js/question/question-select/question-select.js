/**
 * @description 题库选择
 * @author 赵子靖
 * @since 2017-03-11
 */
var QuestionSelect = function() {
    var t = {
        mainContainer : '',
        init : function(mainContainer) {
            t.mainContainer = mainContainer;
            t.initView();
            t.initEvent();
        },
        initView : function() {
            t.render();
        },
        initEvent : function() {
        },
        render : function() {
        	Global.initLoader(t.mainContainer);
//        	$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/question/question-select/select.html'));
        	$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/question/question-select/sequential-question.html'));
        },
        //开始练习
        beginExercise:function(type){
        	Global.initLoader(t.mainContainer);
        	if(Constants.EXERCISE_TYPE_1==type){
        		//顺序练习
        		$(t.mainContainer).html(nunjucks.render(Global.appName + '/tpl/question/question-select/sequential-question.html'));
        	}else if(Constants.EXERCISE_TYPE_1=='2'){
        		//随机练习
        	}
        }
    }
    return t;
}();