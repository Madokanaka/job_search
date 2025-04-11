<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="d-flex align-items-center justify-content-center min-vh-100">
        <div class="col-7">
            <h2 class="text-center mb-4">Регистрация</h2>
            <form action="/auth/register" method="post" enctype="multipart/form-data">
                <div class="form-group mb-3">
                    <label for="name">Имя</label>
                    <input type="text" class="form-control" id="name" name="name" required>
                    <#if error_name??>
                        <div class="text-danger">${error_name}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="surname">Фамилия</label>
                    <input type="text" class="form-control" id="surname" name="surname" required>
                    <#if error_surname??>
                        <div class="text-danger">${error_surname}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="age">Возраст</label>
                    <input type="text" class="form-control" id="age" name="age" required>
                    <#if error_age??>
                        <div class="text-danger">${error_age}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                    <#if error_email??>
                        <div class="text-danger">${error_email}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="phoneNumber">Телефон</label>
                    <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required>
                    <#if error_phoneNumber??>
                        <div class="text-danger">${error_phoneNumber}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="password">Пароль</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <#if error_password??>
                        <div class="text-danger">${error_password}</div>
                    </#if>
                </div>
                <div class="form-group mb-3">
                    <label for="accountType">Тип аккаунта</label>
                    <select class="form-control" id="accountType" name="accountType" required>
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
