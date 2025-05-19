let stompClient = null;

function connect() {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/chat/' + conversationId, function(message) {
            const messageDto = JSON.parse(message.body);
            showMessage(messageDto);
        });
    }, function(error) {
        console.error('Connection error: ' + error);
    });
}

function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const content = messageInput.value.trim();
    if (content && stompClient && stompClient.connected) {
        const messageDto = {
            senderId: candidateId,
            receiverId: employerId,
            content: content,
            timestamp: new Date().toISOString()
        };
        stompClient.send("/app/chat.send", {}, JSON.stringify(messageDto));
        messageInput.value = '';
    } else {
        console.error('Cannot send message: STOMP client is not connected');
    }
}

function showMessage(messageDto) {
    const chatMessages = document.getElementById('chatMessages');
    const div = document.createElement('div');
    div.className = `mb-2 ${messageDto.senderId === candidateId ? 'text-end' : 'text-start'}`;
    div.innerHTML = `
        <span class="badge ${messageDto.senderId === candidateId ? 'bg-primary' : 'bg-secondary'}">
            ${messageDto.content} <small>(${new Date(messageDto.timestamp).toLocaleString()})</small>
        </span>
    `;
    chatMessages.appendChild(div);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

document.addEventListener('DOMContentLoaded', connect);