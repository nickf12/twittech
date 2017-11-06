var showContent = function (content) {
    $.ajax({
        method: 'GET',
        url: content,
        success: function (res) {
            $("#content").html(res);
        }
    });
};

const putContent = function(content) {
  $('#content').html(content);
};

const putLeftNav = function(content) {
    $('#leftcolumn').html(content);
};