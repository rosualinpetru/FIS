$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);

    $("#log-in__auth-form__reset-link").click(function () {
        $("#log-in__reset-pass-form").removeClass("d-none");
        $("#log-in__auth-form").addClass("d-none")
    });

    $("#log-in__auth-form__auth-link").click(function () {
        $("#log-in__reset-pass-form").addClass("d-none");
        $("#log-in__auth-form").removeClass("d-none");
    });
});