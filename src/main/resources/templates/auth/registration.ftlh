<#import "../layout.ftlh" as main>
<@main.layout ; spring>
    <div class="d-flex align-items-center justify-content-center min-vh-100">
        <div class="col-7">
            <h2 class="text-center mb-4">Регистрация</h2>
            <form action="/auth/register" method="post" enctype="multipart/form-data">
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf-param-name'}"
                           value="${(_csrf.token)!'csrf-token'}"/>
                </#if>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example1cg">Имя</label>
                    <@spring.formInput "userDto.name" 'id="form3Example1cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example2cg">Фамилия</label>
                    <@spring.formInput "userDto.surname" 'id="form3Example2cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example3cg">Возраст</label>
                    <@spring.formInput "userDto.age" 'id="form3Example3cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example4cg">Email</label>
                    <@spring.formInput "userDto.email" 'id="form3Example4cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example5cg">Телефон</label>
                    <@spring.formInput "userDto.phoneNumber" 'id="form3Example5cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example6cg">Пароль</label>
                    <@spring.formInput "userDto.password" 'id="form3Example6cg" class="form-control"' />
                    <@spring.showErrors "<br>" "error text-danger" />
                </div>
                <div class="form-group mb-3">
                    <label class="form-label" for="form3Example7cg">Тип аккаунта</label>
                    <select class="form-control" id="form3Example7cg" name="accountType" required>
                        <option value="employer">Employer</option>
                        <option value="applicant">Applicant</option>
                    </select>
                    <#if error_accountType??>
                        <div class="text-danger mb-3">${error_accountType}</div>
                    </#if>
                </div>
                <#if error??>
                    <div class="text-danger mb-3">${error}</div>
                </#if>
                <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
            </form>
        </div>
    </div>
</@main.layout>
