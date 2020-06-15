$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);
    
    let type = $("#request-holiday__form__request-type")
    let fileGroup = $("#request-holiday__form__file-group");
    let file = $("#request-holiday__form__file");

    if (type.val() === 'Med') {
        fileGroup.removeClass("d-none");
        file.attr("required");
    }
    else {
        fileGroup.addClass("d-none");
        file.removeAttr("required");
    }

    type.change(
        function () {
            if ($(this).val() === 'Med') {
                fileGroup.removeClass("d-none");
                file.attr("required");
            }
            else {
                fileGroup.addClass("d-none");
                file.removeAttr("required");
            }
        });

    file.change(function(){
        let fileName = $(this).val();
        $(this).next('.custom-file-label').html(fileName);
    })
});