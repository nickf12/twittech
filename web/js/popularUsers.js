$(document).ready(function () {

});

const author = function (authorId) {
    $.ajax({
        url: 'Author',
        method: 'GET',
        data: {authorId: authorId},
        success: function (res) {
            putContent(res);
        }
    });
};

const getPopularUsers = function(last) {
    $.ajax({
        url: 'column',
        method: 'GET',
        data: {last: last, content: 'popularUsers'},
        success: function(res) {
            $('#leftcolumn').html(res);
        }
    });
};