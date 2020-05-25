document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: ['interaction', 'dayGrid'],
        header: {
            left: 'prev,next',
            center: 'today',
            right: 'title'
        },
        navLinks: true, // can click day/week names to navigate views
        eventLimit: true, // allow "more" link when too many events
        displayEventTime:false,
        height: "parent",
        events: {
            url: '/loadMyTeamsSchedule',
            failure: function () {
                document.getElementById('script-warning').style.display = 'block'
            }
        }
    });

    calendar.render();
});