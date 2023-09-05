function funcHandler(event)
{
		var button = event.target
		var dataTitle = button.dataset.title
		var dataTarget = button.dataset.target
		var urlz = button.dataset.url
		var serverz = button.dataset.server
		$(dataTarget).on('show.bs.modal',function(){
			$.get(urlz, function (data) {
					try{
					    $(serverz).html(data);
					}catch(r)
					{
                        console.log('aman aman')
					}finally
					{
                        $(dataTarget).find('.modal-title').text(dataTitle)
					}
//					$(dataTarget).find('.modal-body').html(data);
			});
		})

}