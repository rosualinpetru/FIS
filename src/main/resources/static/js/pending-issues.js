$(document).ready(function () {
    $("#issue-requests").removeClass("invisible");

    $(".issue__pending__entry").click(function () {
        let id = $(this).attr("id");
        $("#" + id + "modal").modal({show: true});
    })

    $(".update").click(
        function () {
            $.post("/delete-issue", {
                issueId: $(this).attr("id").replace('issueId', ''),
            }, function () {
                document.location = '/pending-issues';
            })
        })
});