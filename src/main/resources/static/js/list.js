/**
 * 
 */
$(function() {
	$("#search").on("keyup", function() {
		$.ajax({
			url: "http://localhost:8080/employee/suggest",
			dataType: "json",
			data: { 
				partOfName: $("#search").val()
			},
			async: true
		}).done(function(data) {
			console.dir(JSON.stringify(data));
			$("#search").autocomplete({
			      source: data.names
			});
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			// 検索失敗時には、その旨をダイアログ表示
			alert("エラー");
			console.log("XMLHttpRequest : " + XMLHttpRequest.status);
			console.log("textStatus     : " + textStatus);
			console.log("errorThrown    : " + errorThrown.message);
		}).always(function(data) {
		});
	})
  } );