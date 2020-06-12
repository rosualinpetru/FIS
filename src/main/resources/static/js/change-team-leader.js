
$(document).ready(function () {
    $("#change-team-leader__form__employee-id").prop('disabled', true);
    $("#change-team-leader__form__team-leader-id").prop('disabled', true);
    $('#change-team-leader__form__department').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#change-team-leader__form__employee-id').html('<option value=""> --- Select a department first  ---</option>');
                $("#change-team-leader__form__employee-id").prop('disabled', true);
                $('#change-team-leader__form__team-leader-id').html('<option value=""> --- Select a department first  ---</option>');
                $("#change-team-leader__form__team-leader-id").prop('disabled', true);
            }
            else{
                $("#change-team-leader__form__employee-id").prop('disabled', true);
                $("#change-team-leader__form__team-leader-id").prop('disabled', true);

                $.getJSON("/tom/update-change-team-leader-form", {
                    departmentId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    let html = '<option value=""> --- None --- </option>';
                    const len = data.length;
                    for (let i = 0; i < len; i++) {
                        html += '<option value="' + data[i].value0 + '">'
                            + data[i].value1 + '</option>';
                    }
                    html += '</option>';
                    $('#change-team-leader__form__employee-id').html(html);
                    $("#change-team-leader__form__employee-id").prop('disabled', false);
                });

                $.getJSON("/tom/update-change-team-leader-form-without-me", {
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
                    $('#change-team-leader__form__team-leader-id').html(html);
                    $("#change-team-leader__form__team-leader-id").prop('disabled', false);
                });
            }
        });

});

