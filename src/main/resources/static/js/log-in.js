$(document).ready(function () {
    $("#log-in__auth-form__submit").click(function () {
        let userField = $("#log-in__auth-form__username");
        let passHolderField = $("#log-in__auth-form__password--holder");
        if (userField.val() !== "" && passHolderField.val() !== "") {
            $.get("/tom/get-salt", {username: userField.val()}, function (data) {
                if (data === "") {
                    window.location = "/tom/log-in?error";
                }
                $("#log-in__auth-form__password").val(passHolderField.val() + data);
                $("#log-in__auth-form").submit();
            });
        }
    });

    $("#log-in__auth-form__reset-link").click(function () {
        $("#log-in__reset-pass-form").removeClass("d-none");
        $("#log-in__auth-form").addClass("d-none")
    });

    $("#log-in__auth-form__auth-link").click(function () {
        $("#log-in__reset-pass-form").addClass("d-none");
        $("#log-in__auth-form").removeClass("d-none");
    });
});