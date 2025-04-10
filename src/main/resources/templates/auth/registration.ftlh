<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <h2 class="my-5">Регистрация</h2>
    <form action="/auth/register" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="name">Имя</label>
            <input type="text" class="form-control" id="name" name="name" required>
            <#if error_name??>
                <div class="text-danger">${error_name}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="surname">Фамилия</label>
            <input type="text" class="form-control" id="surname" name="surname" required>
            <#if error_surname??>
                <div class="text-danger">${error_surname}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="surname">Возраст</label>
            <input type="text" class="form-control" id="surname" name="age" required>
            <#if error_age??>
                <div class="text-danger">${error_age}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
            <#if error_email??>
                <div class="text-danger">${error_email}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="phoneNumber">Телефон</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" required>
            <#if error_phoneNumber??>
                <div class="text-danger">${error_phoneNumber}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" name="password" required>
            <#if error_password??>
                <div class="text-danger">${error_password}</div>
            </#if>
        </div>
        <div class="form-group">
            <label for="accountType">Тип аккаунта</label>
            <select class="form-control" id="accountType" name="accountType" required>
                <option value="employer">Employer</option>
                <option value="applicant">Applicant</option>
            </select>
            <#if error_accountType??>
                <div class="text-danger">${error_accountType}</div>
            </#if>
        </div>
        <div>
            <#if error??>
                <div class="text-danger">${error}</div>
            </#if>
        </div>
        <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
    </form>
</div>
</body>
</html>
