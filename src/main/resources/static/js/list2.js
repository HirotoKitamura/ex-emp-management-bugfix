/**
 * 
 */
$(function() {
	$("#prev").on("click", function(){
		$("button").prop("disabled", true);
		$("#page").val(parseInt($("#page").val(),10)-1);
		$("#pageForm").submit();
	})
	$("#next").on("click", function(){
		$("button").prop("disabled", true);
		$("#page").val(parseInt($("#page").val(),10)+1);
		$("#pageForm").submit();
	})
});