$(document).ready(function () {

    $("#requests").removeClass("invisible");

    $(".requests__pending__entry").click(function () {
        let id = $(this).attr("id");
        $("#"+id+"modal").modal({show:true});
    })

    $(".update").click(
        function () {
            $.post("/tom/update-holiday-request", {
                id: $(this).attr("id").replace('acc', '').replace('dec', ''),
                act: $(this).attr("id").slice(0, 3)
            }, function () {
                document.location = '/tom/pending-holiday-requests';
            })
        })
});