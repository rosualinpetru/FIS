$(document).ready(function () {
    $("#reset-password__form").submit(function (e) {
        if ($("#reset-password__form__user-id").attr("value") !== "") {
            e.submit();
        }
    });
});