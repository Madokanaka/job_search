<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="d-flex align-items-center justify-content-center min-vh-100">
        <div class="col-7">
            <h2 class="text-center mb-4"><@spring.message "register.title"/></h2>
            <form action="/auth/register" method="post" enctype="multipart/form-data">
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                           value="${(_csrf.token)!'csrf-token'}"/>
                </#if>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example1cg"><@spring.message "User.name.label"/></label>
                    <@spring.formInput "userDto.name" 'id="form3Example1cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example2cg"><@spring.message "User.surname.label"/></label>
                    <@spring.formInput "userDto.surname" 'id="form3Example2cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example3cg"><@spring.message "User.age.label"/></label>
                    <@spring.formInput "userDto.age" 'id="form3Example3cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example4cg"><@spring.message "User.email.label"/></label>
                    <@spring.formInput "userDto.email" 'id="form3Example4cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example5cg"><@spring.message "User.phone.label"/></label>
                    <@spring.formInput "userDto.phoneNumber" 'id="form3Example5cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example6cg"><@spring.message "User.password.label"/></label>
                    <@spring.formInput "userDto.password" 'id="form3Example6cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example7cg"><@spring.message "User.accountType.label"/></label>
                    <select class="form-control" id="form3Example7cg" name="accountType" required>
                        <option value="employer"><@spring.message "User.accountType.employer"/></option>
                        <option value="applicant"><@spring.message "User.accountType.applicant"/></option>
                    </select>
                    <#if error_accountType??>
                        <div class="text-danger mb-3">${error_accountType}</div>
                    </#if>
                </div>
                <#if error??>
                    <div class="text-danger mb-3">${error}</div>
                </#if>
                <button type="submit" class="btn btn-primary w-100"><@spring.message "register.submit"/></button>
            </form>
        </div>
    </div>
</@main.layout>
