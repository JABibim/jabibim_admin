// ✅ WebSocket 연결 설정
const socket = new SockJS('/ws'); // WebSocket 엔드포인트
const stompClient = Stomp.over(socket);

// ✅ 현재 열려있는 채팅방 ID를 추적하는 변수
let currentChatRoomId = null;

// ✅ WebSocket 연결 및 이벤트 핸들링
stompClient.connect({}, function () {

    // ✅ 메시지 구독 (채팅방 전체 메시지 수신)
    stompClient.subscribe('/topic/chatRoom', function (message) {
        const msg = JSON.parse(message.body);
        currentChatRoomId = msg.chatRoomId;
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

// ✅ 전송 버튼 클릭 이벤트
$(document).ready(function () {
    $("#sendMessageBtn").click(function () {
        let message = $("#chatInput").val().trim();
        if (message === "") return;

        let chatRoomId = $("#chatRoomId").val();
        let senderId = $("#loggedInUserId").val();

        sendMessage(chatRoomId, senderId, message);

        $("#chatInput").val(""); // 입력창 초기화

        // ✅ 채팅 모달이 열리면 안 읽은 메시지를 읽음 처리
        $('#chatModal').on('shown.bs.modal', function () {
            let chatRoomId = $("#chatRoomId").val();
            let userId = $("#loggedInUserId").val();

            currentChatRoomId = chatRoomId; // 현재 열려 있는 채팅방 ID 저장
            markMessagesAsRead(chatRoomId, userId);
        });

        $(document).on('hide.bs.modal', '#chatModal', function () {
            isOpen = false;
        });

    });

    // ✅ Enter 키로 메시지 보내기
    $("#chatInput").keypress(function (event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            $("#sendMessageBtn").click();
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
            $("#chatNotificationBadge").hide();
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

// ✅ 안 읽은 메시지 개수 가져와서 업데이트하는 함수
function updateUnreadMessageBadge() {
    let userId = $("#loggedInUserId").val();

    if (!userId) {
        console.error("🚨 userId 값이 없음! AJAX 요청을 중단합니다.");
        return;
    }

    $.ajax({
        url: "/chat/unreadCount",
        type: "GET",
        data: { userId: userId },
        success: function (count) {
            if (count > 0) {
                $("#chatNotificationBadge").text(count).show();
            } else {
                $("#chatNotificationBadge").hide();
            }
        },
        error: function (xhr, status, error) {
            console.error("❌ AJAX 요청 실패:", error);
        }
    });
}
