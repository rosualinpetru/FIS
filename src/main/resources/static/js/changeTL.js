
$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);

    $("#emplID").prop('disabled', true);
    $("#TLID").prop('disabled', true);
    $('#department').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#emplID').html('<option value=""> --- Select a department first  ---</option>');
                $("#emplID").prop('disabled', true);
                $('#TLID').html('<option value=""> --- Select a department first  ---</option>');
                $("#TLID").prop('disabled', true);
            }
            else{
                $("#emplID").prop('disabled', true);
                $("#TLID").prop('disabled', true);
                $.getJSON("/updateSignUpForm", {
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
                    $('#emplID').html(html);
                    $("#emplID").prop('disabled', false);
                    $('#TLID').html(html);
                    $("#TLID").prop('disabled', false);
                });
            }
        });

});

