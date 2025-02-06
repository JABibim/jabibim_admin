// WebSocket 연결 설정
const socket = new SockJS('/ws'); // WebSocket 엔드포인트
const stompClient = Stomp.over(socket);

// WebSocket 연결
stompClient.connect({}, function () {
    console.log("Connected to WebSocket");

    // 메시지 구독
    stompClient.subscribe('/topic/chatRoom', function (message) {
        const msg = JSON.parse(message.body);
        console.log(msg.senderName + ": " + msg.chatMessage);

        // 채팅 메시지를 채팅 창에 추가
        $('#chatBox').append('<div>' + msg.senderName + ': ' + msg.chatMessage + '</div>');
    });
});

// 메시지 전송 함수
function sendMessage(chatRoomId, senderId, chatMessage) {
    const message = {
        chatRoomId: chatRoomId,
        senderId: senderId,
        chatMessage: chatMessage
    };
    stompClient.send("/app/chat/sendMessage", {}, JSON.stringify({
       chatRoomId : chatRoomId,
        senderId : senderId,
        chatMessage : chatMessage
    }));
}

// 버튼 클릭 이벤트 등록
$(document).ready(function () {
    $('#sendMessageBtn').click(function () {
        const chatRoomId = $('#chatRoomId').val();
        const senderId = $('#loggedInUserId').val();
        const chatMessage = $('#chatInput').val(); // 입력된 메시지 가져오기
        console.log("chatRoomID======", chatRoomId);
        console.log("senderId======",senderId);
        console.log("chatMessage======",chatMessage);

        if (!chatRoomId || !senderId) {
            alert('채팅방이 열리지 않거나 사용자 정보가 없습니다..');
            return;
        }

        if (chatMessage.trim() !== "") {
            sendMessage(chatRoomId, senderId, chatMessage); // 메시지 전송
            $('#chatInput').val(''); // 입력창 초기화
        } else {
            alert("메시지를 입력하세요.");
        }
    });
});

function fetchChatHistory(chatRoomId) {
    console.log("📌 fetchChatHistory 실행됨, chatRoomId:", chatRoomId);

    $.ajax({
        url: '/chat/recent?id='+chatRoomId,
        type: "GET",
        dataType: "json",
        success: function(data) {
            console.log("🟢 서버 응답 데이터:", data);
            displayChatHistory(data);
        },
        error: function(xhr, status, error) {
            console.error("🔴 채팅 내역 불러오기 실패:", status, error);
        }
    });
}


function displayChatHistory(chatHistory) {
    const chatBox = document.getElementById("chatBox");
    chatBox.innerHTML = "";  //기존 채팅 내역 초기화

    chatHistory.each(msg => {
        const messageElement = document.createElement("div");
        messageElement.textContent = `${msg.senderName}: ${msg.chatMessage}`;
        chatBox.appendChild(messageElement);
    });
}
