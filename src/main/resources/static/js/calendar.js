$(document).ready(function () {
    $(document).on('click', '#joinGCalBtn', () => {
        openPopup();
    })

    function openPopup() {
        const popupWidth = 500;
        const popupHeight = 600;
        const left = (window.innerWidth - popupWidth) / 2;
        const top = (window.innerHeight - popupHeight) / 2;

        const popup = window.open(
            '/auth/oauth2/authorize', // OAuth 요청을 처리하는 URL
            'Google Calendar Auth',
            `width=${popupWidth},height=${popupHeight},top=${top},left=${left},scrollbars=yes`
        );

        // 팝업 창에서 부모로 데이터 전달
        const interval = setInterval(() => {
            if (popup.closed) {
                clearInterval(interval);
                console.log('팝업 닫힘');
            }
        }, 500);
    }
})

// document.addEventListener('DOMContentLoaded', function () {
// var calendarEl = document.getElementById('calendar');
// let popup = document.querySelector('dialog');
// var calendar = new FullCalendar.Calendar(calendarEl, {
//     initialView: 'dayGridMonth',
//     googleCalendarApiKey: 'AIzaSyBXFz8vCh6SbLBDmddYajLubFWJMGs2DRM',
//     events:
//         {
//             googleCalendarId: 'f42bfe4a0e07e48e56920950dbe4b49dd6a91d6c265b0af89a28cf6ab17f22bd@group.calendar.google.com',
//         },
//     eventSources:
//         {
//             googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
//             backgroundColor: 'red',
//             textColor: 'white' // 추가로 글자 색상 지정 가능
//         },
//     dayCellDidMount: function (info) {
//         const date = new Date(info.date); // 날짜 가져오기
//         const day = date.getDay(); // 요일 가져오기 (0: 일요일, 6: 토요일)
//
//         // 날짜 번호 요소 가져오기
//         const dayNumberElement = info.el.querySelector('.fc-daygrid-day-number');
//
//         // 요일에 따라 색상 적용
//         if (day === 0) {
//             dayNumberElement.style.color = 'red'; // 일요일은 빨간색
//         } else if (day === 6) {
//             dayNumberElement.style.color = 'blue'; // 토요일은 파란색
//         } else {
//             dayNumberElement.style.color = 'black'; // 평일은 검정색
//         }
//     },
//     eventClick: function (info) {
//         info.jsEvent.preventDefault(); // 브라우저 기본 동작 방지
//         const eventDescription = info.event.extendedProps.description || '설명 없음';
//         const eventStart = info.event.start.toLocaleString(); // 시작 시간
//         const eventEnd = info.event.end ? info.event.end.toLocaleString() : '없음'; // 종료 시간
//         const eventLocation = info.event.extendedProps.location || '위치 정보 없음'; // 위치
//         console.log(info.event.extendedProps);
//         popup.querySelector('div').innerHTML = `
//                 <h3>${info.event.title}</h3>
//                 <div><strong>설명:</strong> ${eventDescription}</div>
//                 <div><strong>시작 시간:</strong> ${eventStart}</div>
//                 <div><strong>종료 시간:</strong> ${eventEnd}</div>
//                 <div><strong>위치:</strong> ${eventLocation}</div>
//             `;
//         popup.setAttribute('open', 'open');
//     },
// });
//
// calendar.render();
//
// // 팝업 닫기 버튼 이벤트
// popup.querySelector('button').addEventListener('click', () => {
//     popup.removeAttribute('open');
// });
// });