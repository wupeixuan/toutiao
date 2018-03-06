$(function () {

    $(".sns-action-del").bind("click", function () {
        var this_ = $(this);
        $('#delModal').modal('show');
        $(".btn-del").bind("click", function () {
            $.ajax({
                url: '/msg/delete',
                method: 'post',
                data: {msgId:this_.attr("data-id")},
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
    })
})