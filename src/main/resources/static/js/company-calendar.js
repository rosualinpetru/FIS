document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById("company-schedule__calendar");

    document.getElementById("company-schedule__employee-id").addEventListener("change",function () {
        calendarEl.innerHTML = "";
        const e = document.getElementById("company-schedule__employee-id");
        const calendar = new FullCalendar.Calendar(calendarEl, {
            plugins: ['interaction', 'dayGrid'],
            header: {
                left: 'prev,next',
                center: 'today',
                right: 'title'
            },
            navLinks: true,
            eventLimit: true,
            displayEventTime: false,
            height: "parent",
            events: {
                url: "/tom/company-calendar?id="+ e.options[e.selectedIndex].value,
                failure: function () {
                    document.getElementById('script-warning').style.display = 'block'
                }
            }
        });
        calendar.render();
    })
});