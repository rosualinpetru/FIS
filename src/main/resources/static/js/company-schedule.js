$(document).ready(function () {
    $("#company-schedule").removeClass("invisible");

    $("#company-schedule__employee-id").prop('disabled', true);
    $('#company-schedule__department').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#company-schedule__employee-id').html('<option value=""> --- Select a department first  ---</option>');
                $("#company-schedule__employee-id").prop('disabled', true);
            }
            else{
                $("#company-schedule__employee-id").prop('disabled', true);
                $.getJSON("/tom/update-company-schedule-form", {
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
                    $('#company-schedule__employee-id').html(html);
                    $("#company-schedule__employee-id").prop('disabled', false);
                });
            }
        });

});