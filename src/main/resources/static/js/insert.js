/**
 * 
 */
$(function() {
	if($("#mailAddressExists").text() == "このメールアドレスは既に登録されています"){
		$("#mailAddress").css("class", "error-input");
	}
});