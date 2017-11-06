var createTweet = function (tweetId, message, from, fromImage, fromId, date) {

    var author = document.createElement('div');
    author.className = 'author';
    var avatar = document.createElement('img');
    avatar.className = 'avatar';
    avatar.src = fromImage;
    var name = document.createElement('span');
    $(name).text(from);
    var publishDate = document.createElement('span');
    publishDate.className = "date";
    $(publishDate).text(date);
    $(author).append([avatar, name, publishDate]);
    $(author).on('click', function () {
        $.ajax({
            url: 'Author',
            method: 'GET',
            data: {authorId: fromId},
            success: function (res) {
                putContent(res);
            }
        });
    });

    var content = document.createElement('div');
    content.className = 'content';
    $(content).text(message);

    var actions = document.createElement('div');
    actions.className = 'actions';
    $(actions).append(
        '<button class="retweet" disabled onclick="retweet(' + tweetId + ')">' + "<%@include file="/res/icons_formated/retweet.svg"%>" + '</button>' +
        '<button class="favorite" disabled onclick="setFavorite(' + tweetId + ')">' + "<%@include file="/res/icons_formated/flag.svg"%>" +'</button>' +
        '<button class="comment" disabled onclick="commentTweet(' + tweetId + ')">' + "<%@include file="/res/icons_formated/edit.svg"%>" +'</button>'
    );

    if(${not empty sessionScope.user}){
        $(actions).find('button').attr('disabled', false);
    }

    if (${sessionScope.user.admin == true}) {
        $(actions).append('<button onclick="removeTweet(' + tweetId + ')" href="/remove"><img src="res/icons/close.svg"></button>');
    }

    var tweet = document.createElement('div');
    tweet.id = 'tweet_' + tweetId;
    tweet.className = 'message';
    $(tweet).append([author, content, actions]);

    return tweet;
};

<c:if test="${not empty sessionScope.user}">

const sendTweet = function(message) {
        $.ajax({
            url: "tweet",
            method: "GET",
            data: {userId: ${sessionScope.user.userId}, message: message, action: "add"},
            success: function() {
                alert("Tweet added");
                location.reload(true);
            }
        });
};

const commentTweet = function (tweetId) {
    $.ajax({
        url: "tweet",
        method: "GET",
        data: {tweetId:tweetId,  action: "getComment"},
        success: function(res) {
            var comments = JSON.parse(res);
            var c = "";
            comments.forEach(function(comment){
                c += '<div class="comment">'+
                    '<span><img class="avatar" src="'+comment.image+'"/>'+ comment.username +'</span>' +
                    '<div class="commentmessage">'+ comment.comment +
                    '</div><div class="date">' + comment.date + '</div></div>';
            });
            var dialog = document.createElement('dialog');
            dialog.className = 'commentDialog';
            dialog.id = tweetId;
            dialog.innerHTML = '<div class="column"><textarea></textarea>' + c +
                '<div class="row"><button onclick="sendComment(' + tweetId + ')">Comment</button>' +
                '<button onclick="closeDialog(' + tweetId + ')">Close</button></div></div>';
            $('body').append($(dialog));
            dialog.show();
            //location.reload(true);
        }
    });

};
const sendComment = function (tweetId) {
    var msg = $('#' + tweetId + ' textarea').val();
    $.ajax({
        url: 'tweet',
        method: 'GET',
        data: {tweetId: tweetId, comment: msg, action: 'comment'},
        success: function () {
            $('#' + tweetId).remove();
        }
    });

};
const setFavorite = function (tweetId) {
    $.ajax({
        url: 'tweet',
        method: 'GET',
        data: {tweetId: tweetId, action: 'favorite'},
        success: function () {
            console.log('favorited');
        }
    });
};
const retweet = function (tweetId) {
    $.ajax({
        url: 'tweet',
        method: 'GET',
        data: {tweetId: tweetId, action: 'retweet'},
        success: function () {
            console.log('retweeted');
        }
    });
};
const removeTweet = function (tweetId) {
    var dialog = document.createElement('dialog');
    dialog.id = tweetId;
    dialog.innerHTML = '<div>Are you sure to remove this tweet?</div>' +
        '<div class="row"><button onclick="sendRemove(' + tweetId + ')">yes</button>' +
        '<button onclick="closeDialog(' + tweetId + ')">no</button></div>';
    $('body').append($(dialog));
    dialog.showModal();
};
const sendRemove = function (tweetId) {
    $.ajax({
        url: 'tweet',
        method: 'GET',
        data: {tweetId: tweetId, action: 'remove'},
        success: function () {
            console.log('tweet removed');
            closeDialog(tweetId);
            $('#tweet_' + tweetId).css('background-color', 'rgba(255, 28, 0, 0.50)');
            setTimeout(function () {
                $('#tweet_' + tweetId).remove();
            }, 2000);
        }
    });
};

const closeDialog = function (tweetId) {
    $('#' + tweetId).remove();
};

</c:if>



