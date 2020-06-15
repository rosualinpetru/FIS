$(document).ready(function () {

    $(".alert:not(:empty)").removeClass("d-none");

    setTimeout(function () {
        $(".alert").fadeOut();
    }, 3000);
    
    $(".index__menu-toggler").click(function () {
        let doc = $("html");
        doc.css("overflow", "hidden");
        let alert = $(".index__alert");
        alert.toggleClass("h-25");
        alert.toggleClass("index__alert--up");
        $(".index__content").toggleClass("index__content--up");
        setTimeout(function () {
            doc.css("overflow", "visible");
        }, 500);
    })
});