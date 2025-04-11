<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование профиля</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container py-4">
    <h1 class="mb-4">Редактировать профиль</h1>

    <form action="/profile/edit" method="POST">
        <input type="hidden" name="_method" value="PUT">

        <div class="mb-3">
            <label for="name" class="form-label">Имя</label>
            <input type="text" class="form-control" id="name" name="name" value="${user.name}" required>
            <#if error_name??>
                <div class="text-danger">${error_name}</div>
            </#if>
        </div>

        <div class="mb-3">
            <label for="surname" class="form-label">Фамилия</label>
            <input type="text" class="form-control" id="surname" name="surname" value="${user.surname}" required>
            <#if error_surname??>
                <div class="text-danger">${error_surname}</div>
            </#if>
        </div>

        <div class="mb-3">
            <label for="age" class="form-label">Возраст</label>
            <input type="number" class="form-control" id="age" name="age" value="${user.age}" required min="18"
                   max="100">
            <#if error_age??>
                <div class="text-danger">${error_age}</div>
            </#if>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
            <#if error_email??>
                <div class="text-danger">${error_email}</div>
            </#if>
        </div>

        <div class="mb-3">
            <label for="phoneNumber" class="form-label">Телефон</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" value="${user.phoneNumber}"
                   required>
            <#if error_phoneNumber??>
                <div class="text-danger">${error_phoneNumber}</div>
            </#if>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Пароль</label>
            <input type="password" class="form-control" id="password" name="password" required>
            <#if error_password??>
                <div class="text-danger">${error_password}</div>
            </#if>
        </div>

        <#if error??>
            <div class="text-danger mb-3">${error}</div>
        </#if>

        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
    </form>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
