// WebSocket 연결 설정
const socket = new SockJS('/ws'); // WebSocket 엔드포인트
const stompClient = Stomp.over(socket);

// WebSocket 연결
stompClient.connect({}, function () {

    // 메시지 구독
    stompClient.subscribe('/topic/chatRoom', function (message) {
        const msg = JSON.parse(message.body);
        console.log(msg.senderName + ": " + msg.chatMessage);

        appendMessageToChatBox(msg.senderId, msg.senderName, msg.chatMessage);
    });
});


$(document).ready(function () {
    $("#sendMessageBtn").click(function () {
        let message = $("#chatInput").val().trim();
        if (message === "") return;

        let chatRoomId = $("#chatRoomId").val();
        let senderId = $("#loggedInUserId").val();

        //WebSocket을 사용하여 메시지 전송
        sendMessage(chatRoomId, senderId, message);

        //채팅창에 메시지 바로 추가
        $("#chatInput").val(""); // 입력창 초기화
    });

    // 메시지 전송 함수 (WebSocket 사용)
    function sendMessage(chatRoomId, senderId, chatMessage) {

        const message = {
            chatRoomId: chatRoomId,
            senderId: senderId,
            chatMessage: chatMessage
        };

        stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
    }

    // Enter 키로 메시지 보내기
    $("#chatInput").keypress(function (event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            $("#sendMessageBtn").click();
        }
    });
});
function fetchChatHistory(chatRoomId) {
    $.ajax({
        url: '/chat/recent?id='+chatRoomId,
        type: "GET",
        dataType: "json",
        success: function(data) {
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

function appendMessageToChatBox(senderId, senderName, message) {
    const chatBox = document.getElementById("chatBox");

    const messageElement = document.createElement("div");
    messageElement.classList.add("chat-message");

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
