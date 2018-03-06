$(function () {
    $.ajax({
        url: '/msg/latestSend',
        dataType: 'json',
        success: function (oResult) {
            if (oResult.code == 0) {
                var latest = $('.latest-send');
                latest.append('最近发送 ');
                var list = oResult.list;
                for (var o in list) {
                    var json = eval("(" + list[o] + ")");
                    latest.append('<a href="javascript:void(0);" class="latest" data-name="' + json.name + '">' +
                        '<img id="' + json.id + '" src="' + json.headUrl + '" title="' + json.name + '">' +
                        '</a>');
                }
            } else {
                $('.latest-send').html("");
            }
        }
    });
    var flag = false;
    $("#content").on("focus", function () {
        $.ajax({
            url: '/valname',
            method: 'post',
            data: {username: $('#username').val()},
            success: function (oResult) {
                var code = oResult.code;
                if (code == 1) {
                    $("#msg").html(oResult.msg);
                } else {
                    $("#msg").html("");
                    flag = true;
                }
            }
        });
    });

    var timeoutId;
    var index = -1;
    var confirm = false;
    $("#username").keyup(function (event) {
        //key up and key down
        if (event.keyCode == 38 || event.keyCode == 40) {
            var lis = $('.drop-menu li');
            var len = lis.length;
            if (len == 0)
                return;
            lis = $(lis);
            //up key
            if (event.keyCode == 38) {
                index = (index - 1) % len;
            } else {
                //down key
                index = (index + 1) % len;
            }
            $(lis).removeClass('hover');
            $(lis.get(index)).addClass('hover');
            return;
        }
        //key enter
        if (event.keyCode == 13) {
            if (index < 0)
                return;
            var lis = $('.drop-menu li');
            $(lis.get(index)).click();
            return;
        }
        confirm = false;
        var value = this.value;
        if (value == '') {
            $('.drop-menu').html("");
            $('.drop-menu').hide();
            return;
        }
        clearTimeout(timeoutId);
        timeoutId = setTimeout(function () {
            $.ajax({
                url: '/fuzzyqueryname',
                method: 'GET',
                dataType: 'json',
                data: {username: value},
                success: function (oResult) {
                    if (oResult.code == 0) {
                        index = -1;
                        $(".drop-menu").html("");
                        $(".drop-menu").css('display', 'block');
                        var list = oResult.list;
                        list = eval("(" + list + ")");
                        for (var o in list) {
                            $('.drop-menu').append('<li data-name="' + list[o].name + '"><img class="list-head" src="' + list[o].headUrl + '" alt="' + list[o].name + '">'
                                + ' <span class="list-name" title="' + list[o].name + '">' + list[o].name + '</span></li>');
                        }
                    } else {
                        $(".drop-menu").html("");
                        $(".drop-menu").hide();
                    }
                }
            })
        }, 200);
    });
    $('#username').on('focus', function () {
        if (confirm == false)
            $('#username').keyup();
    });

    $(".btn-send").bind("click", function () {
        var content = $("#content");
        var username = $("#username");
        content.focus();
        if (flag == false) {
            return;
        }
        $.ajax({
            url: '/msg/send',
            method: 'post',
            data: {username: username.val(), content: content.val()},
            success: function (oResult) {
                var code = oResult.code;
                if (code == 1) {
                    $("#msg").html(oResult.msg);
                } else {
                    $("#msg").html("");
                    $('#myModal').modal('hide');
                    window.location.reload();
                }
            }
        });
    });

    $(".sns-action-del").bind("click", function () {
        var this_ = $(this);
        $('#delModal').modal('show');
        $(".btn-del").bind("click", function () {
            $.ajax({
                url: '/msg/delcvst',
                method: 'post',
                data: {conversationId: this_.attr("data-id")},
                success: function (oResult) {
                    var code = oResult.code;
                    if (code == 1) {
                        alert(oResult.msg);
                    } else {
                        $('#delModal').modal('hide');
                        window.location.reload();
                    }
                }
            })
        })
    });
    $('.latest-send').on('click', '.latest', function () {
        $("#msg").html("");
        $("#username").val($(this).attr("data-name"));
    });
    $('.drop-menu').on('click', 'li', function () {
        confirm = true;
        $(".drop-menu").hide();
        $("#username").val($(this).attr("data-name"));
    });
});