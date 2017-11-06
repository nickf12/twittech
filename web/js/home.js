$(document).ready(function () {
    var msgContainer = $('#messagesContainer');
    var loader = $('<img src="res/icons/loader.gif">');
    $.ajax({
        url: '/home',
        method: 'GET',
        data: {next: 0},
        success: function (res) {
            var newTweets = JSON.parse(res);
            newTweets.forEach(function (tweet) {
                var el = createTweet(tweet.id, tweet.message, tweet.username, tweet.image, tweet.userId, tweet.date);
                msgContainer.append(el);
            });
            msgContainer.append(loader);
        }
    });

    $('#messagesContainer').on('scroll', function (event) {
        if (this.scrollTop >= this.scrollHeight - this.clientHeight) {
            var container = $(this);
            var last = document.getElementsByClassName('message').length;
            $.ajax({
                url: '/home',
                method: 'GET',
                data: {next: last},
                success: function (res) {
                    var newTweets = JSON.parse(res);
                    newTweets.forEach(function (tweet) {
                        var el = createTweet(tweet.id, tweet.message, tweet.username, tweet.image, tweet.userId, tweet.date);
                        container.append(el);
                    });
                    msgContainer.append(loader);
                }
            });
        }
    });

    $('#newTweet button').on('click', function () {
        sendTweet($("#newTweet textarea").val());
    });
});

$('#leftcolumn').load('column', {content: 'popularUsers'});