$(document).ready(function () {

    $(".requests__details").click(function () {
        let id = $(this).attr("id");
        $("#"+id+"modal").modal({show:true});
    })

    $("#feedback__department").change(
        function () {
            if($(this).val()!=="")
                window.location = "/tom/pending-holiday-requests-hr?departmentId="+$(this).val();
        })
});