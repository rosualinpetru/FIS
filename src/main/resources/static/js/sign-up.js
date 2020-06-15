$(document).ready(function () {
    $("#sign-up__form__team-leader-field").prop('disabled', true);
    $('#sign-up__form__department-field').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#sign-up__form__team-leader-field').html('<option value=""> --- Select a department first  ---</option>');
                $("#sign-up__form__team-leader-field").prop('disabled', true);
            }
            else{
            $("#sign-up__form__team-leader-field").prop('disabled', true);
            $.getJSON("/tom/update-sign-up-form", {
                departmentId: $(this).val(),
                ajax: 'true'
            }, function (data) {
                let html = '<option value="0"> --- None --- </option>';
                const len = data.length;
                for (let i = 0; i < len; i++) {
                    html += '<option value="' + data[i].value0 + '">'
                        + data[i].value1 + '</option>';
                }
                html += '</option>';
                $('#sign-up__form__team-leader-field').html(html);
                $("#sign-up__form__team-leader-field").prop('disabled', false);
            });
            }
        });
});