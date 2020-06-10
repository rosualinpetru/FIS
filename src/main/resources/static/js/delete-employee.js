$(document).ready(function () {

    $("#delete-employee__employee-id").prop('disabled', true);
    $('#delete-employee__department').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#delete-employee__employee-id').html('<option value=""> --- Select a department first  ---</option>');
                $("#delete-employee__employee-id").prop('disabled', true);
            }
            else{
                $("#delete-employee__employee-id").prop('disabled', true);
                $.getJSON("/update-delete-employee-form", {
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
                    $('#delete-employee__employee-id').html(html);
                    $("#delete-employee__employee-id").prop('disabled', false);
                });
            }
        });

});