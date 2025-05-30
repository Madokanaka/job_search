<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <h2><@spring.message "ForgotPassword.title"/></h2>

    <#if error??>
        <b class="text-danger text-center">${error}</b>
    </#if>


    <div class="py-5 h-100">
        <div class="row d-flex align-items-center justify-content-center h-100">
            <div class="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
                <form action="/auth/forgot_password" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>

                    <div class="form-outline border border-secondary rounded p-3">
                        <div>
                            <p><@spring.message "ForgotPassword.instruction"/></p>
                        </div>

                        <div>
                            <p>
                                <input type="email" name="email" class="form-control"
                                       placeholder="<@spring.message 'ForgotPassword.emailPlaceholder'/>" required autofocus/>
                            </p>

                            <p class="text-center">
                                <input type="submit" value="<@spring.message 'ForgotPassword.sendButton'/>" class="btn btn-primary" />
                            </p>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@main.layout>
