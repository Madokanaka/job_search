<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container mt-4">
        <h2 class="mb-4"><@spring.message "chat.title"/> ${employer.name} ${employer.surname}</h2>

        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">${employer.name} ${employer.surname}</h5>
                <p class="card-text"><strong><@spring.message "chat.employer.email"/>: </strong>${employer.email}</p>
                <p class="card-text"><strong><@spring.message "chat.employer.phone"/>: </strong>${employer.phoneNumber!'-'}</p>
                <p class="card-text"><strong><@spring.message "chat.employer.accountType"/>: </strong>${employer.accountType}</p>
            </div>
        </div>

        <div class="card">
            <div class="card-header bg-light">
                <h5 class="mb-0"><@spring.message "chat.conversation"/></h5>
            </div>
            <div class="card-body" style="max-height: 400px; overflow-y: auto;" id="chatMessages">
                <#if messages?? && messages?has_content>
                    <#list messages as message>
                        <div class="mb-2">
                            <#if message.sender.id == candidate.id>
                                <div class="text-end">
                                    <span class="badge bg-primary">
                                        ${message.content} <small>(${message.timestamp.toLocalDate()})</small>
                                    </span>
                                </div>
                            <#else>
                                <div class="text-start">
                                    <span class="badge bg-secondary">
                                        ${message.content} <small>(${message.timestamp.toLocalDate()})</small>
                                    </span>
                                </div>
                            </#if>
                        </div>
                    </#list>
                </#if>
            </div>
            <div class="card-footer">
                <div class="input-group">
                    <input type="text" class="form-control" id="messageInput" placeholder="<@spring.message 'chat.message.placeholder'/>">
                    <button class="btn btn-primary" onclick="sendMessage()"><@spring.message "chat.send.button"/></button>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/sockjs-client/1.5.1/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.4/stomp.min.js"></script>
    <script>
        const candidateId = ${candidate.id};
        const chatRoomId = ${chatRoomId};
    </script>
    <script src="/static/js/chat.js"></script>
</@main.layout>