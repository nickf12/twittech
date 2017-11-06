$(document).ready(function () {
    //Submit process
    $('#registerForm').on('submit', function (event) {
        event.preventDefault();
        $.ajax({
            method: 'post',
            url: '/RegisterController',
            data: $(this).serializeArray(),
            success: function(res) {
                $('#content').html(res);
            }
        });
    });

    //Validations
    var name = $('#registerForm input[type="text"]')[0];
    var submit = $('#registerForm input[type="submit"]')[0];
    var email = $('#registerForm input[type="email"]')[0];
    var pass = $('#registerForm input[type="password"]');   //both password fields
    var input = $('#registerForm input');   //all fields
    input.on('input', function () {
        if(!name.checkValidity()) {
            $(name).css('border', '2px red solid');
        } else {
            $(name).css('border', '');
        }
        if (pass[1].checkValidity() && pass[0].checkValidity() && email.checkValidity() && name.checkValidity()) {
            submit.disabled = false;
        } else {
            submit.disabled = true;
        }
    });
    pass.on('input', function () {
        if ($(pass[0]).val() !== $(pass[1]).val()) {
            pass.css('border', 'solid #FF5555 1px');
            submit.disabled = true;
            $('#passMessage').text("The passwords don't match");
        } else {
            pass.css('border', '');
            $('#passMessage').text("");
        }
    });
});