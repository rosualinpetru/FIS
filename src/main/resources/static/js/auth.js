$(document).ready(function () {
    $("#reset-link").click(function () {
        $("#reset-pass-form").show();
        $("#login-form").hide();
    })

    $("#back-login").click(function () {
        $("#reset-pass-form").hide();
        $("#login-form").show();
    })
});