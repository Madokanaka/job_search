<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="text-center">
        <h2><@spring.message "verify.token.title"/></h2>
        <#if error??>
            <b class="text-danger">${error}</b>
        </#if>
        <#if message??>
            <b class="text-warning">${message}</b>
        </#if>
        <#if token??>
            <div class="mt-3">
                <p><@spring.message "verify.token.instruction"/></p>
                <p><strong><@spring.message "verify.token.label"/>: </strong>${token}</p>
            </div>
        </#if>
    </div>

    <div class="py-5 h-100">
        <div class="row d-flex align-items-center justify-content-center h-100">
            <div class="col-md-7 col-lg-5 col-xl-5">
                <form action="/auth/verify_token" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>
                    <div class="mb-3">
                        <label class="form-label" for="tokenInput"><@spring.message "verify.token.label"/></label>
                        <input type="text" id="tokenInput" name="token" class="form-control"
                               placeholder="<@spring.message 'verify.token.placeholder'/>" autofocus/>
                    </div>
                    <p class="text-center">
                        <button type="submit"
                                class="btn btn-primary mt-3"
                                aria-label="<@spring.message 'verify.token.submit.aria'/>">
                            <@spring.message "verify.token.submit"/>
                        </button>
                    </p>
                </form>
            </div>
        </div>
    </div>
</@main.layout>