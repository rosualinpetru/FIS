$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);

    $("#tl").prop('disabled', true);
    $('#department').change(
        function () {
            if($(this).children("option:selected").val() === "") {
                $('#tl').html('<option value=""> --- Select a department first  ---</option>');
                $("#tl").prop('disabled', true);
            }
            else{
            $("#tl").prop('disabled', true);
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
                $('#tl').html(html);
                $("#tl").prop('disabled', false);
            });
            }
        });

});