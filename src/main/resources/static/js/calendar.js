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
            info.jsEvent.preventDefault(); // 브라우저 기본 동작 방지
            const eventDescription = info.event.extendedProps.description || '설명 없음';
            const eventStart = info.event.start.toLocaleString(); // 시작 시간
            const eventEnd = info.event.end ? info.event.end.toLocaleString() : '없음'; // 종료 시간
            const eventLocation = info.event.extendedProps.location || '위치 정보 없음'; // 위치
            console.log(info.event.extendedProps);
            popup.querySelector('div').innerHTML = `
                    <h3>${info.event.title}</h3>
                    <div><strong>설명:</strong> ${eventDescription}</div>
                    <div><strong>시작 시간:</strong> ${eventStart}</div>
                    <div><strong>종료 시간:</strong> ${eventEnd}</div>
                    <div><strong>위치:</strong> ${eventLocation}</div>
                `;
            popup.setAttribute('open', 'open');
        },
    });

    calendar.render();

    // 팝업 닫기 버튼 이벤트
    popup.querySelector('button').addEventListener('click', () => {
        popup.removeAttribute('open');
    });

    function openPopup() {
        console.log('===> 🚀 팝업 열림!!');
        popup.innerHTML = `
            <form id="eventForm">
                <label>제목:</label><input type="text" id="summary" required><br><br>
                <label>시작 날짜:</label><input type="datetime-local" id="startDate" required><br><br>
                <label>종료 날짜:</label><input type="datetime-local" id="endDate" required><br><br>
                <label>설명:</label><textarea id="description"></textarea><br><br>
                <button type="button" id="submitBtn">전송</button>
                <button type="button" id="cancelBtn">취소</button>
            </form>
        `;
        popup.showModal();

        console.log('===> 🚀 팝업 내의 함수 호출 열림!!');
        document.getElementById('submitBtn').onclick = submitEvent;
        document.getElementById('cancelBtn').onclick = () => popup.close();
    }

    function submitEvent() {
        const item = {
            summary: document.getElementById('summary').value,
            startDate: new Date(document.getElementById('startDate').value).toISOString(),
            endDate: new Date(document.getElementById('endDate').value).toISOString(),
            description: document.getElementById('description').value,
            timeZone: 'UTC'
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

                // if (success) {
                //     alert('일정이 추가되었습니다..');
                // } else {
                //     alert('일정 추가 실패');
                // }

                popup.close();
            },
            error: function (err) {
                console.log('error : ', err);
            }
            // error: function (xhr, status, error) {
            //     console.log('일정 추가 중 오류 발생');
            //     console.log('서버 오류 발생:', xhr.status);  // HTTP 상태 코드
            //     console.log('응답 내용:', xhr.responseText);  // 서버에서 반환된 응답 내용
            //     console.log('에러 메시지:', error);  // 에러 메시지
            //     alert('일정 추가 중 오류가 발생했습니다.');
            //
            //     if ( xhr.status === 401 ) {
            //         window.location.href = '/dashboard';
            //     }
            //
            //     popup.close();  // 팝업 닫기
            // }
        });
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

    // DB에 갖고 있던 RT로 새로운 AT를 발급하는 백엔드 API를 호출하는 함수 END
})
