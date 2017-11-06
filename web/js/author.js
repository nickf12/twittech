const showRemoveDialog = function(userId) {
    var dialog = document.createElement('dialog');
    dialog.open = true;
    dialog.id = 'removeDialog';
    dialog.innerHTML = '<div class="column">Do you also whish remove its tweet?' +
        '<div class="row"><button onclick="removeUser(' + userId + ', true)">Yes</button>' +
        '<button onclick="removeUser(' + userId + ', false)">No</button>' +
        '<button onclick="closeRmDialog()">Close</button></div></div>';
    $('body').append(dialog);
    dialog.show();
};

const closeRmDialog = function(dialog) {
    document.getElementById('removeDialog').remove();
};

const removeUser = function(userId, removeTweets) {
    $.ajax({
        url: '/Author',
        method: 'GET',
        data: {userId: userId, removeTweets: removeTweets},
        success: function() {
            alert("The user's password has been changed!");
            location.assign('/');
        }
    });
};

$(document).ready(function () {
    var msgs = $('#authorMessages');
    var loader = $('<img class="loader" src="res/icons/loader.gif">');

    $.ajax({
        url: '/Author',
        method: 'GET',
        data: {authorId: ${authorId}, next: 0},
        success: function (res) {
            var tweets = JSON.parse(res);
            tweets.forEach(function (tweet) {
                var el = createTweet(tweet.id, tweet.message, tweet.username, tweet.image, tweet.userId, tweet.date);
                msgs.append(el);
            });
            msgs.append(loader);
        }
    });

    ws.send(JSON.stringify({is: ${authorId}, from: session.id}));

    $('#faux').on('scroll', function (event) {
        if (this.scrollTop >= this.scrollHeight - this.clientHeight) {
            var last = document.getElementsByClassName('message').length;
            $.ajax({
                url: '/Author',
                method: 'GET',
                data: {authorId: ${authorId}, next: last},
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
