var setUserFollower = function (user_id) {
    $.ajax({
        method: "GET",
        url: "/UserFollowerController",
        data: {user_id: user_id},
        success: function () {
            $.ajax({
                url: 'Author',
                method: 'GET',
                data: {authorId: user_id},
                success: function (res) {
                    putContent(res);
                }
            });
            alert("Now you follow this author");
        }
    });
};

var unFollow = function (userId) {
    $.ajax({
        url: '/UserFollowerController',
        method: 'GET',
        data: {user_id: userId, action: 'unfollow'},
        success: function () {
            $.ajax({
                url: 'Author',
                method: 'GET',
                data: {authorId: userId},
                success: function (res) {
                    putContent(res);
                }
            });
        }
    })
    ;
};