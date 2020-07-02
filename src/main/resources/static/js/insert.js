/**
 * 
 */
$(function() {
	$("#getAddress").on("click", function() {
		$.ajax({
			url: "https://zipcoda.net/api",
			dataType: "jsonp",
			data: { 
				zipcode: $("#zipCode").val().replace("-","")
			},
			async: true
		}).done(function(data) {
			console.dir(JSON.stringify(data));
			$("#address").val(data.items[0].pref+data.items[0].address);
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			// 検索失敗時には、その旨をダイアログ表示
			alert("番号に該当する住所は存在しません。");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		}).always(function(data) {
		});
	})
});