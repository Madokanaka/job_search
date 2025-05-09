<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="text-center">
        <h2><@spring.message "reset.password.title"/></h2>
        <#if error??>
            <b class="text-danger">${error}</b>
        </#if>
    </div>

    <div class="py-5 h-100">
        <div class="row d-flex align-items-center justify-content-center h-100">
            <div class="col-md-7 col-lg-5 col-xl-5">
                <form action="/auth/reset_password" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                               value="${(_csrf.token)!'csrf-token'}"/>
                    </#if>
                    <input type="hidden" name="token" value="${(resetPasswordDto.token)!token!''}">
                    <div class="mb-3">
                        <label class="form-label" for="passwordInput"><@spring.message "reset.password.label"/></label>
                        <@spring.formInput "resetPasswordDto.password" 'id="passwordInput" class="form-control" placeholder="' + springMacroRequestContext.getMessage('reset.password.placeholder') + '" required autofocus' />
                        <@spring.showErrors "<br>" "text-danger" />
                    </div>
                    <p class="text-center">
                        <button type="submit"
                                class="btn btn-primary mt-3"
                                aria-label="<@spring.message "reset.password.submit.aria"/>">
                            <@spring.message "reset.password.submit"/>
                        </button>
                    </p>
                </form>
            </div>
        </div>
    </div>
</@main.layout>