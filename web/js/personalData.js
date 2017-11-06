$('#navigation').load('MenuController');
$(document).ready(function () {
    $('#personalDataForm').on('submit', function (event) {
        event.preventDefault();
        var data = {action: "edit"};
        $.each($(this).serializeArray(), function (i, field) {
            if(field.value)
                data[field.name] = field.value;
        });
        var file = this[6].files[0];
        if(file) {
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function (event) {
                data['image'] = event.target.result;
                sendPersonalData(data);
            };
        } else {
            sendPersonalData(data);
        }

    });
    $('#personalDataForm input[name="age"]')[0].oninput = function() {
        if(!this.checkValidity()) {
            $(this).css('border', '2px red solid');
        } else {
            $(this).css('border', '');
        }
    };
});

var sendPersonalData = function(data) {
    $.ajax({
        method: 'POST',
        url: '/PersonalData',
        data: data,
        success: function (res) {
            $('#content').html(res);
        }
    });
};