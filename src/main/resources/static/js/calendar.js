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
                <div><strong><i class="ri ri-calendar-check-fill"></i></strong><input type="text" id="summary" placeholder="Add title" required></div>
                <div><strong><i class="ri ri-alarm-line"></i></strong><input type="datetime-local" id="startDate" required>
                     <strong><i class="ri ri-alarm-fill"></i></strong><input type="datetime-local" id="endDate" required></div></div>
                <div><label><i class="ri ri-edit-box-line"></i></label><textarea id="description" placeholder="Add description"></textarea></div>
                <div><strong><i class="ri ri-map-pin-line"></i></strong> <input id="location" type="text" placeholder="Add location"></div>
                <div style="text-align: right; margin-top: 10px;">
                <button type="button" id="submitBtn" class="btn btn-primary btn-sm">추가<i class="bi bi-pencil"></i></button>
                <button type="button" id="cancelBtn" class="btn btn-secondary btn-sm">취소<i class="bi bi-backspace"></i></button>
                </div>
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
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
                console.log('에러 메시지:', error);  // 에러 메시지

                alert('일정 추가 중 오류가 발생했습니다.');

                if (xhr.status === 401) {  // 401 체크
                    console.log(xhr.status + ' : 401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                    getNewToken().then(() => {
                        submitEvent(); // 토큰 갱신 후 재요청
                    }).catch((err) => {
                        console.log('토큰 갱신 실패:', err);
                        popup.close();
                    });
                }  else {
                    console.error('다른 오류 발생:', xhr.responseText);
                }

                popup.close();
            }
        });
    }

    function renderEventDetail(event) {
        const eventDescription = event.extendedProps.description || '설명 없음';
        const eventStart = event.start.toISOString().slice(0, 16).replace('T', ' ');
        const eventEnd = event.end ? event.end.toISOString().slice(0, 16).replace('T', ' ') : '';
        const eventLocation = event.extendedProps.location || '위치 정보 없음';

        // 팝업에 상세 정보 표시
        popup.innerHTML = `
        <h3 style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis; max-width: 100%;"><i class="ri ri-calendar-check-fill"></i>${event.title}</h3>
        <div><strong><i class="ri ri-alarm-line"></i></strong> ${eventStart} ~ ${eventEnd}</div>
        <div><strong><i class="ri ri-edit-box-line"></i></strong> ${eventDescription}</div>
        <div><strong><i class="ri ri-map-pin-line"></i></strong> ${eventLocation}</div>
        <div style="text-align: right; margin-top: 10px;">
            <button id="editEvent" class="btn btn-success btn-sm">수정<i class="bi bi-pencil"></i></button>
            <button id="deleteEvent" class="btn btn-danger btn-sm">삭제<i class="bi bi-trash"></i></button>
            <button id="closePopup" class="btn btn-secondary btn-sm">닫기<i class="bi bi-backspace"></i></button>
        </div>
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
        const googleEventId = event.id;

        // 팝업을 수정 모드로 변경
        popup.innerHTML = `
        <div><input id="eventTitle" type="text" style="font-size: 30px; font-weight: bold;"
                                                value="${event.title}" /></div>
        <input type="hidden" id="googleEventId" value="${googleEventId}">
        <div><strong><i class="ri ri-edit-box-line"></i></strong> <input id="eventDescription" type="text" value="${eventDescription}" /></div>
        <div><strong><i class="ri ri-alarm-line"></i></strong> <input id="eventStart" type="datetime-local" value="${eventStart}" /></div>
        <div><strong><i class="ri ri-alarm-fill"></i></strong> <input id="eventEnd" type="datetime-local" value="${eventEnd}" /></div>
        <div><strong><i class="ri ri-map-pin-line"></i></strong> <input id="eventLocation" type="text" value="${eventLocation}" /></div>
        <div style="text-align: right; margin-top: 10px;">
            <button id="saveEvent" class="btn btn-primary btn-sm" >저장<i class="bi bi-pencil"></i></button>
            <button id="cancelEdit" class="btn btn-secondary btn-sm">취소<i class="bi bi-backspace"></i></button>
        </div>
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
                    window.location.reload();
                } else {
                    alert('일정 수정 실패: ' + (response.error || '알 수 없는 오류'));
                }
                popup.close();
            },
            error: function (xhr, status, error) {
                console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
                console.log('에러 메시지:', error);  // 에러 메시지

                alert('일정 수정 중 오류가 발생했습니다.');

                if (xhr.status === 401) {  // 401 체크
                    console.log(xhr.status + ' : 401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                    getNewToken().then(() => {
                        saveEvent(); // 토큰 갱신 후 재요청
                    }).catch((err) => {
                        console.log('토큰 갱신 실패:', err);
                        popup.close();
                    });
                }  else {
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
                        window.location.reload();
                    } else {
                        alert('이벤트 삭제 실패: ' + data.message);  // 실패 메시지 표시
                    }
                },
                error: function (xhr, status, error) {
                    console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
                    console.log('에러 메시지:', error);  // 에러 메시지

                    alert('일정 삭제 중 오류가 발생했습니다.');

                    if (xhr.status === 401) {  // 401 체크
                        console.log(xhr.status + ' : 401 Unauthorized 오류 발생. 토큰 갱신 시도.');
                        getNewToken().then(() => {
                            deleteEvent(); // 토큰 갱신 후 재요청
                        }).catch((err) => {
                            console.log('토큰 갱신 실패:', err);
                            popup.close();
                        });
                    }  else {
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

    // DB에 갖고 있던 RT로 새로운 AT를 발급하는 백엔드 API를 호출하는 함수 START
    async function getNewToken() {
        try {
            const response = await $.ajax({
                type: 'GET',
                url: '/auth/google/refreshToken',
            });

            const { success, message, data } = response;
            const { message: customMessage } = data;

            console.log('==> success : ', success);
            console.log('==> message : ', message);
            console.log('==> customMessage : ', customMessage);

            if (success) {
                alert('토큰이 갱신되었습니다.');
            } else {
                alert('토큰 갱신 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.log('토큰 갱신 중 오류 발생');
            console.log('에러 메시지:', error);  // 에러 메시지
            alert('토큰 갱신 중 오류가 발생했습니다.');
            throw error; // 오류가 발생한 경우 예외를 던짐
        }
    }

})

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

    // 화면에서 호출해보기 위해 작성한 테스트 소스 START
    $(document).on('click', '#getNewToken', () => {
        getNewToken();
    })
    // 화면에서 호출해보기 위해 작성한 테스트 소스 END

    // DB에 갖고 있던 RT로 새로운 AT를 발급하는 백엔드 API를 호출하는 함수 END
})