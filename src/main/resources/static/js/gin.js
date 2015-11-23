function ginOnLoad(){
	disableCardButtons()
	$(function() {
	    $( "#playerCards" ).sortable();
	    $( "#playerCards" ).disableSelection();
	});
}

function disableCardButtons() {
	$("button[name='card']").click(function(){
		return false;
	})
}