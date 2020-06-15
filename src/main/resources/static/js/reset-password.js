$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);
    
    $("#reset-password__form").submit(function (e) {
        if ($("#reset-password__form__user-id").attr("value") !== "") {
            e.submit();
        }
        e.preventDefault();
    });
});