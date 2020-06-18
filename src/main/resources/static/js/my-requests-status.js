$(document).ready(function () {

    $(".requests__details").click(function () {
        let id = $(this).attr("id");
        $("#"+id+"modal").modal({show:true});
    })

    $("#requests__status").change(
        function () {
            if($(this).val()!=="")
                window.location = "/tom/my-requests-status?status="+$(this).val();
        })
});