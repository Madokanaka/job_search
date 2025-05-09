<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="text-center">
        <h2><@spring.message "message.title"/></h2>
        <p class="text-warning">${message}</p>
    </div>
</@main.layout>
