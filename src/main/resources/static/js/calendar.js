document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    // 숨겨진 input에서 googleCalendarId 값 가져오기
    var googleCalendarInfo = document.getElementById('googleCalendarId').value;
    console.log(googleCalendarInfo);  // 값이 잘 출력되는지 확인
    let popup = document.querySelector('dialog');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        customButtons: {
            myCustomButton: {
                text: "일정 추가하기",
                click: function () {
                    openPopup(); // 팝업 열기 함수 호출
                }
            }
        },
        headerToolbar: {
            left: 'myCustomButton',
            center: 'title'
        },
        initialView: 'dayGridMonth',
        googleCalendarApiKey: 'AIzaSyBXFz8vCh6SbLBDmddYajLubFWJMGs2DRM',
        events:
            {
                googleCalendarId: googleCalendarInfo
            },
        eventSources:
            {
                googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
                backgroundColor: 'red',
                textColor: 'white' // 추가로 글자 색상 지정 가능
            },
        dayCellDidMount: function (info) {
            const date = new Date(info.date); // 날짜 가져오기
            const day = date.getDay(); // 요일 가져오기 (0: 일요일, 6: 토요일)

            // 날짜 번호 요소 가져오기
            const dayNumberElement = info.el.querySelector('.fc-daygrid-day-number');

            // 요일에 따라 색상 적용
            if (day === 0) {
                dayNumberElement.style.color = 'red'; // 일요일은 빨간색
            } else if (day === 6) {
                dayNumberElement.style.color = 'blue'; // 토요일은 파란색
            } else {
                dayNumberElement.style.color = 'black'; // 평일은 검정색
            }
        },
        eventClick: function (info) {
            info.jsEvent.preventDefault();
            currentEvent = info.event;
            console.log(info.event);
            renderEventDetail(info.event);
            popup.setAttribute('open', 'open');
        }
    });

    calendar.render();

    function openPopup() {
        console.log('===> 🚀 팝업 열림!!');
        popup.innerHTML = `
            <form id="eventForm">
                <label>제목:</label><input type="text" id="summary" required><br><br>
                <label>시작 날짜:</label><input type="datetime-local" id="startDate" required><br><br>
                <label>종료 날짜:</label><input type="datetime-local" id="endDate" required><br><br>
                <label>설명:</label><textarea id="description"></textarea><br><br>
                <div><strong>위치:</strong> <input id="location" type="text"></div>
                <button type="button" id="submitBtn">전송</button>
                <button type="button" id="cancelBtn">취소</button>
            </form>
        `;
        popup.showModal();

        console.log('===> 🚀 팝업 내의 함수 호출 열림!!');

        popup.addEventListener('click', function (event) {
            if (event.target.id === 'submitBtn') {
                submitEvent();
            } else if (event.target.id === 'cancelBtn') {
                popup.close();
            }
        });
    }

    function submitEvent() {
        const item = {
            summary: document.getElementById('summary').value,
            startDate: new Date(document.getElementById('startDate').value).toISOString(),
            endDate: new Date(document.getElementById('endDate').value).toISOString(),
            description: document.getElementById('description').value,
            location: document.getElementById('location') ? document.getElementById('location').value : '',
            timeZone: "Asia/Seoul"
        };

        $.ajax({
            type: 'POST',
            url: '/calendar/insert',  // 요청을 보낼 URL
            beforeSend: function (xhr) {  // 데이터 전송 전에 헤더에 CSRF 토큰 설정
                xhr.setRequestHeader(header, token);
            },
            data: JSON.stringify(item),  // 보낼 데이터 (JSON 문자열로 변환)
            dataType: 'json',  // 응답 데이터 타입
            contentType: 'application/json',  // 전송할 데이터의 타입 (JSON 형식)
            success: function (response) {
                console.log('==> response : ', response);

                if (response && response.success) {
                    alert('일정이 추가되었습니다.');
                } else {
                    alert('일정 추가 실패: ' + (response.error || '알 수 없는 오류'));
                }

                popup.close();
            },
            error: function (xhr, status, error) {
                console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
                console.log('에러 메시지:', error);  // 에러 메시지
                alert('일정 추가 중 오류가 발생했습니다.');
                return;
                if (xhr.status === 401) {
                    console.log('401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                    getNewToken();
                    submitEvent();

                } else {
                    console.error('다른 오류 발생:', xhr.responseText);
                    popup.close();
                }
            }
        });
    }

    function renderEventDetail(event) {
        const eventDescription = event.extendedProps.description || '설명 없음';
        const eventStart = event.start.toISOString().slice(0, 16);
        const eventEnd = event.end ? event.end.toISOString().slice(0, 16) : '';
        const eventLocation = event.extendedProps.location || '위치 정보 없음';

        // 팝업에 숨겨진 input 추가
        popup.querySelector('div').innerHTML = `
        <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100%;">${event.title}</h3>
        
        <div><strong>설명:</strong> ${eventDescription}</div>
        <div><strong>시작 시간:</strong> ${eventStart}</div>
        <div><strong>종료 시간:</strong> ${eventEnd}</div>
        <div><strong>위치:</strong> ${eventLocation}</div>
        <button id="editEvent">수정</button>
        <button id="deleteEvent">삭제</button>
        <button id="closePopup">닫기</button>
    `;

        document.getElementById('editEvent').addEventListener('click', () => enableEditing(event));
        document.getElementById('deleteEvent').addEventListener('click', deleteEvent);
        document.getElementById('closePopup').addEventListener('click', closePopup);
    }

    function enableEditing(event) {
        const eventDescription = event.extendedProps.description || '설명 없음';
        const eventStart = event.start.toISOString().slice(0, 16);
        const eventEnd = event.end ? event.end.toISOString().slice(0, 16) : '';
        const eventLocation = event.extendedProps.location || '위치 정보 없음';
        const googleEventId = event.id; // event.id를 calendar_event_id로 사용

        console.log(event.extendedProps);
        console.log(googleEventId);

        popup.querySelector('div').innerHTML = `
        <h3><input id="eventTitle" type="text" value="${event.title}" /></h3>
        <input type="hidden" id="googleEventId" value="${googleEventId}">
        <div><strong>설명:</strong> <input id="eventDescription" type="text" value="${eventDescription}" /></div>
        <div><strong>시작 시간:</strong> <input id="eventStart" type="datetime-local" value="${eventStart}" /></div>
        <div><strong>종료 시간:</strong> <input id="eventEnd" type="datetime-local" value="${eventEnd}" /></div>
        <div><strong>위치:</strong> <input id="eventLocation" type="text" value="${eventLocation}" /></div>
        <button id="saveEvent">저장</button>
        <button id="cancelEdit">취소</button>
    `;

        document.getElementById('saveEvent').addEventListener('click', () => saveEvent(event));
        document.getElementById('cancelEdit').addEventListener('click', () => renderEventDetail(event));
    }


    function saveEvent() {
        const updatedEvent = {
            googleEventId: document.getElementById('googleEventId').value,
            eventTitle: document.getElementById('eventTitle').value,
            eventDescription: document.getElementById('eventDescription').value,
            eventLocation: document.getElementById('eventLocation').value,
            eventStart: document.getElementById('eventStart').value,
            eventEnd: document.getElementById('eventEnd').value,
            timeZone: "Asia/Seoul"
        };

        $.ajax({
            type: 'POST',
            url: '/calendar/update',  // 서버의 일정 수정 API URL
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token); // CSRF 토큰 설정
            },
            data: JSON.stringify(updatedEvent),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                if (response && response.message) {
                    alert('일정이 수정되었습니다.');
                } else {
                    alert('일정 수정 실패: ' + (response.error || '알 수 없는 오류'));
                }
                popup.close();
            },
            error: function (xhr, status, error) {
                console.log('서버 오류 발생:', xhr.status);
                console.log('에러 메시지:', error);
                alert('일정 수정 중 오류가 발생했습니다.');
                return;
                if (xhr.status === 401) {
                    console.log('401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                    getNewToken();
                    saveEvent();  // 토큰 갱신 후 재요청
                } else {
                    console.error('다른 오류 발생:', xhr.responseText);
                }
                popup.close();
            }
        });
    }

    function deleteEvent() {
        if (confirm('이 이벤트를 삭제하시겠습니까?')) {
            $.ajax({
                url: '/calendar/delete',  // URL은 그대로 두고
                type: 'POST',  // 요청 방식을 POST로 변경
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token); // CSRF 토큰 설정
                },
                data: { eventId: currentEvent.id },  // eventId를 데이터로 전송
                success: function(data) {
                    if (data.success) {  // 서버에서 success가 true일 때
                        alert('이벤트 삭제 성공');
                        currentEvent.remove();
                        closePopup();
                    } else {
                        alert('이벤트 삭제 실패: ' + data.message);  // 실패 메시지 표시
                    }
                },
                error: function(xhr, status, error) {
                    alert('서버와의 통신 오류');
                    if (xhr.status === 401) {
                        console.log('401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                        getNewToken();
                        deleteEvent();  // 토큰 갱신 후 재요청
                    } else {
                        console.error('다른 오류 발생:', xhr.responseText);
                    }
                    popup.close();
                }
            });
        }
    }

    function closePopup() {
        popup.removeAttribute('open');
    }

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

    // 화면에서 호출해보기 위해 작성한 테스트 소스 START
    $(document).on('click', '#getNewToken', () => {
        getNewToken();
    })
    // 화면에서 호출해보기 위해 작성한 테스트 소스 END

    // DB에 갖고 있던 RT로 새로운 AT를 발급하는 백엔드 API를 호출하는 함수 START
    function getNewToken() {
        $.ajax({
            type: 'GET',
            url: '/auth/google/refreshToken',
            success: function (response) {
                const {success, message, data} = response;
                const {message: customMessage} = data;

                console.log('==> success : ', success);
                console.log('==> message : ', message);
                console.log('==> customMessage : ', customMessage);

                if (success) {
                    alert('토큰이 갱신되었습니다.');
                } else {
                    alert('토큰 갱신 중 오류가 발생했습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.log('토큰 갱신 중 오류 발생');
                console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
                console.log('응답 내용:', xhr.responseText);  // 서버에서 반환된 응답 내용
                console.log('에러 메시지:', error);  // 에러 메시지
                alert('토큰 갱신 중 오류가 발생했습니다.');
            }
        })
    }

})

$(document).ready(function () {

    // DB에 갖고 있던 RT로 새로운 AT를 발급하는 백엔드 API를 호출하는 함수 END
})