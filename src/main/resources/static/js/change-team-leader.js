
$(document).ready(function () {
    let teamLeader = $('#change-team-leader__form__team-leader-id');
    let employee = $("#change-team-leader__form__employee-id");
    let html = '';
    employee.prop('disabled', true);
    teamLeader.prop('disabled', true);
    $('#change-team-leader__form__department').change(
        function () {
            if ($(this).children("option:selected").val() === "") {
                employee.html('<option value=""> --- Select a department first  ---</option>');
                employee.prop('disabled', true);
                teamLeader.html('<option value=""> --- Select a department first  ---</option>');
                teamLeader.prop('disabled', true);
            } else {
                employee.prop('disabled', true);
                teamLeader.prop('disabled', true);

                $.getJSON("/tom/update-change-team-leader-form", {
                    departmentId: $(this).val(),
                    ajax: 'true'
                }, function (data) {
                    html = '<option value=""> --- None --- </option>';
                    const len = data.length;
                    for (let i = 0; i < len; i++) {
                        html += '<option value="' + data[i].value0 + '">'
                            + data[i].value1 + '</option>';
                    }
                    html += '</option>';
                    employee.html(html);
                    teamLeader.html(html);
                    teamLeader.prop('disabled', false);
                    employee.prop('disabled', false);
                });
            }
        });
    employee.change(function () {
        teamLeader.html(html);
        $("#change-team-leader__form__team-leader-id option[value=" + $(this).val() + "]").remove();
    })
});

