<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="d-flex min-vh-100 align-items-center justify-content-center">
        <div class="row justify-content-center w-100">
            <div class="col-md-6 col-lg-4">
                <div class="card shadow-sm">
                    <div class="card-body p-4">
                        <h2 class="card-title text-center mb-4"><@spring.message "Login.title"/></h2>

                        <form id="login-form" action="/auth/login" method="post">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                                       value="${(_csrf.token)!'csrf-token'}"/>
                            </#if>

                            <div class="mb-3">
                                <label for="email" class="form-label"><@spring.message "Login.email.label"/></label>
                                <input type="email" class="form-control" id="email" name="username" required>
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label"><@spring.message "Login.password.label"/></label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>

                            <#if error??>
                                <div class="alert alert-danger mb-4">
                                    <@spring.message "Login.error.invalidCredentials"/>
                                </div>
                            </#if>

                            <button type="submit" class="btn btn-primary w-100 mb-3"><@spring.message "Login.button.submit"/></button>
                            <a href="/auth/register" class="btn btn-secondary w-100 mb-3"><@spring.message "Login.button.register"/></a>
                            <a href="/auth/forgot_password" class="btn btn-success w-100 mb-3"><@spring.message "Login.button.forgotPassword"/></a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>
