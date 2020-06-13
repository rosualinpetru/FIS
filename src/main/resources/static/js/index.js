$(document).ready(function () {
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