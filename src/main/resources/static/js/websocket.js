// ✅ WebSocket 연결 설정
const socket = new SockJS('/ws'); // WebSocket 엔드포인트
const stompClient = Stomp.over(socket);

// ✅ 현재 열려있는 채팅방 ID를 추적하는 변수
let currentChatRoomId = null;

let chatRoomIdMap = {}; // teacherId -> chatRoomId 매핑 저장

// ✅ WebSocket 연결 및 이벤트 핸들링
stompClient.connect({}, function () {

    // ✅ 메시지 구독 (채팅방 전체 메시지 수신)
    stompClient.subscribe('/topic/chatRoom', function (message) {
        const msg = JSON.parse(message.body);

        // 안 읽은 메시지 개수 갱신
        fetchUnreadMessagesByChatRoom();

        //채팅방이 열려있지 않아도, teacherList 에서 LastMessage 업데이트
        updateLastMessage(msg.chatRoomId, msg.senderName, msg.chatMessage);

        if(!currentChatRoomId || currentChatRoomId !== msg.chatRoomId) {
            fetchUnreadMessageCount();    //새로운 메시지가 오면 숫자 갱신
        }

        appendMessageToChatBox(msg.senderId, msg.senderName, msg.chatMessage);

        if(isOpen === true) {
            // ✅ 현재 보고 있는 채팅방과 새 메시지의 채팅방이 같다면 `is_read` 업데이트
            if (currentChatRoomId === msg.chatRoomId) {
                markMessagesAsRead(currentChatRoomId, $("#loggedInUserId").val());
            } else {
                updateUnreadMessageBadge(); // 다른 채팅방이면 알림 배지 업데이트
            }
        }
    });
}, function (error) {
    console.error("🚨 WebSocket 연결 실패! 자동 재연결 시도 중...");
    setTimeout(connectWebSocket, 5000); // 5초 후 재연결 시도
});

// ✅ WebSocket 재연결 함수
function connectWebSocket() {
    stompClient.connect({}, function () {
        console.log("🔗 WebSocket 재연결 성공!");
    }, function (error) {
        console.error("🚨 WebSocket 재연결 실패:", error);
    });
}

// ✅ 메시지 전송 함수 (WebSocket 사용)
function sendMessage(chatRoomId, senderId, chatMessage) {
    if (!stompClient.connected) {
        console.error("🚨 WebSocket이 연결되지 않았음! 메시지 전송 불가.");
        return;
    }

    const message = {
        chatRoomId: chatRoomId,
        senderId: senderId,
        chatMessage: chatMessage
    };

    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
}


$(document).ready(function () {
    fetchChatRoomIds();

    fetchUnreadMessageCount();


    // ✅ 채팅 모달이 열리면 안 읽은 메시지를 읽음 처리
    $('#chatModal').on('shown.bs.modal', function () {
        let chatRoomId = $("#chatRoomId").val();
        let userId = $("#loggedInUserId").val();

        currentChatRoomId = chatRoomId; // 현재 열려 있는 채팅방 ID 저장
        markMessagesAsRead(chatRoomId, userId);
    });

    // ✅ 전송 버튼 클릭 이벤트
    $("#sendMessageBtn").click(function () {
        let message = $("#chatInput").val().trim();
        if (message === "") return;

        let chatRoomId = $("#chatRoomId").val();
        let senderId = $("#loggedInUserId").val();

        sendMessage(chatRoomId, senderId, message);

        $("#chatInput").val(""); // 입력창 초기화
    });

    // ✅ Enter 키로 메시지 보내기 , Shift + Enter 누르면 줄 바꿈
    $("#chatInput").keypress(function (event) {
        if(event.key === "Enter") {
            if(event.shiftKey) {
                event.preventDefault();  //기본 Enter 동작 방지용
                let chatInput = $(this);
                chatInput.val(chatInput.val() + "\n");    //줄바꿈
            } else {
                event.preventDefault();
                $("#sendMessageBtn").click();
            }
        }
    });
});

// ✅ 안 읽은 메시지를 읽음 처리하는 함수
function markMessagesAsRead(chatRoomId, userId) {
    if (!chatRoomId) {
        console.error("🚨 chatRoomId가 설정되지 않음!");
        return;
    }

    $.ajax({
        url: "/chat/markAsRead",
        type: "POST",
        data: { chatRoomId: chatRoomId, userId: userId },
        beforeSend : function(xhr)
        {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            fetchUnreadMessageCount(); //전체 안읽은 메시지 개수 다시 가져오기
        },
        error: function (xhr, status, error) {
            console.error("❌ 메시지 읽음 처리 실패:", error);
        }
    });
}


// ✅ 새로운 메시지를 채팅창에 추가
function appendMessageToChatBox(senderId, senderName, message) {
    const chatBox = document.getElementById("chatBox");
    const messageElement = document.createElement("div");
    messageElement.classList.add("chat-message");

    let loggedInUserId = $("#loggedInUserId").val();
    if (senderId === loggedInUserId) {
        messageElement.classList.add("my-message");
    } else {
        messageElement.classList.add("other-message");
    }

    messageElement.innerHTML = `
        <div class="message-content">
            <span class="sender-name">${senderName}</span>
            <p class="message-text">${message}</p>
        </div>
    `;

    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight; // 최신 메시지로 스크롤 이동
}

// ✅ 안 읽은 채팅 개수 불러오는 함수 (AJAX 사용)
function fetchUnreadMessageCount() {
    $.ajax({
        url: "/chat/unreadMessagesByChatRoom",
        type: "GET",
        success: function (response) {
            updateUnreadIndicators(response);
            updateUnreadMessageBadge(response);
        },
        error: function () {
            console.error("🚨 안 읽은 메시지 개수 불러오기 실패");
        }
    });
}

// ✅ 읽지 않은 메시지 개수를 업데이트하는 함수
function updateUnreadMessageBadge(response) {
    const badgeElement = $("#chatNotificationBadge");

    let totalUnreadCount = Object.values(response).reduce((sum, count) => sum + count, 0); // 전체 개수 합산

    if (badgeElement.length) {
        if (totalUnreadCount > 0) {
            badgeElement.text(totalUnreadCount);
            badgeElement.css("display", "inline-block"); // 배지 표시
        } else {
            badgeElement.css("display", "none"); // 배지 숨김
        }
    }
}

function updateLastMessage(chatRoomId, senderName, message) {
    let lastMessage = message.length > 10 ? message.substring(0, 10) + "..." : message;

    // ✅ 현재 채팅 선택 모달창이 열려 있는 경우에만 업데이트
    if ($("#chatTeacherListModal").hasClass("show")) {
        $("#teacherList .list-group-item").each(function () {
            let teacherId = $(this).find("button.teacherChat").attr("id");  // 버튼에서 teacherId 가져오기

            if (teacherId) {
                let chatRoom = chatRoomIdMap[teacherId];  // teacherId -> chatRoomId 매핑된 객체
                if (chatRoom === chatRoomId) {
                    $(this).find(".last-message").text(lastMessage);  // lastMessage 업데이트
                }
            }
        });
    }
}

function fetchChatRoomIds() {
    $.ajax({
        url: "/chat/getChatRoomIds",
        type: "GET",
        success: function (response) {
            chatRoomIdMap = response;
        },
        error: function () {
            console.error("🚨 채팅방 ID 매핑 가져오기 실패!");
        }
    });
}

function fetchUnreadMessagesByChatRoom() {
    $.ajax({
        url: "/chat/unreadMessagesByChatRoom",
        type: "GET",
        success: function (response) {
            updateUnreadIndicators(response);
        },
        error: function () {
            console.error("🚨 안 읽은 메시지 개수 가져오기 실패!");
        }
    });
}

function updateUnreadIndicators(unreadCounts) {

    $("#teacherList .list-group-item").each(function () {
        let teacherId = $(this).find("button.teacherChat").attr("id");
        let chatRoomId = chatRoomIdMap[teacherId];

        if (chatRoomId && unreadCounts[chatRoomId] > 0) {
            if (!$(this).find(".unread-indicator").length) {
                $(this).find(".fw-bold").after('<span class="unread-indicator" style="color: red; font-size: 1rem;">🔴</span>');
            }
        } else {
            $(this).find(".unread-indicator").remove();
        }
    });
}