$(document).ready(function () {
    $("#log-in__auth-form__reset-link").click(function () {
        $("#log-in__reset-pass-form").removeClass("d-none");
        $("#log-in__auth-form").addClass("d-none")
    });

    $("#log-in__auth-form__auth-link").click(function () {
        $("#log-in__reset-pass-form").addClass("d-none");
        $("#log-in__auth-form").removeClass("d-none");
    });
});