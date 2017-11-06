$(document).ready(function(){
    $("#loginForm").on("submit", function(event) {
        console.log('login start');
        event.preventDefault();
        $.ajax({
            method: "post",
            url: "/LoginController",
            data: $(this).serializeArray(),
            success: function(res) {
                $('#content').html(res);
            }
        });
    })
});