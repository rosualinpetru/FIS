document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('requests__calendar');

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
            url: '/team-schedule',
            failure: function () {
                document.getElementById('script-warning').style.display = 'block'
            }
        }
    });

    calendar.render();
});