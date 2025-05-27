<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="container py-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0"><@spring.message "chats.title"/></h3>
            </div>
            <div class="card-body">
                <#if chats?has_content>
                    <div class="list-group">
                        <#list chats as chat>
                            <a href="/chat/${chat.id}" class="list-group-item list-group-item-action">
                                <div class="d-flex w-100 justify-content-between">
                                    <h5 class="mb-1">${chat.otherUserName}</h5>
                                    <small>${chat.createdAt.toLocalDate()}</small>
                                </div>
                                <p class="mb-1">${chat.otherUserEmail}</p>
                            </a>
                        </#list>
                    </div>
                <#else>
                    <p class="text-muted"><@spring.message "chats.empty"/></p>
                </#if>
            </div>
        </div>
    </div>
</@main.layout>