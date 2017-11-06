$(document).ready(function () {
    $('#navigation').load('MenuController');
    ws.send(JSON.stringify({me: userID, ws: session.id}));

    var msgs = $('#profileMessages');
    var loader = $('<img class="loader" src="res/icons/loader.gif">');
    $.ajax({
        url: '/Author',
        method: 'GET',
        data: {authorId: ${sessionScope.user.userId}, next: 0},
        success: function (res) {
            var tweets = JSON.parse(res);
            tweets.forEach(function (tweet) {
                var el = createTweet(tweet.id, tweet.message, tweet.username, tweet.image, tweet.userId, tweet.date);
                msgs.append(el);
            });
            msgs.append(loader);
        }
    });

    $('#faux').on('scroll', function (event) {
        if (this.scrollTop >= this.scrollHeight - this.clientHeight) {
            var last = document.getElementsByClassName('message').length;
            $.ajax({
                url: '/Author',
                method: 'GET',
                data: {authorId: ${sessionScope.user.userId}, next: last},
                success: function (res) {
                    var tweets = JSON.parse(res);
                    tweets.forEach(function (tweet) {
                        var el = createTweet(tweet.id, tweet.message, tweet.username, tweet.image, tweet.userId, tweet.date);
                        msgs.append(el);
                    });
                    msgs.append(loader);
                }
            });
        }
    });
});