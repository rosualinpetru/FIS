$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);

    $("#reset-link").click(function () {
        $("#reset-pass-form").show();
        $("#login-form").hide();
    });

    $("#back-login").click(function () {
        $("#reset-pass-form").hide();
        $("#login-form").show();
    });
});