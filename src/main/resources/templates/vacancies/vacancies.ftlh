<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Активные вакансии</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <h1 class="mb-4">Активные вакансии</h1>

    <div class="row g-3">
        <#list vacancies as vacancy>
            <#if vacancy.isActive>
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100 shadow-sm">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${vacancy.name}</h5>

                            <div class="d-flex align-items-center mb-2">
                                <span class="badge bg-success me-2">
                                    <#if vacancy.salary??>
                                        ${vacancy.salary?string["0"]} ₽
                                    <#else>
                                        З/П не указана
                                    </#if>
                                </span>
                            </div>

                            <div class="mb-2">
                                <span class="text-muted">Опыт: </span>
                                <#if vacancy.expFrom?? && vacancy.expTo??>
                                    ${vacancy.expFrom} - ${vacancy.expTo} лет
                                <#elseif vacancy.expFrom??>
                                    от ${vacancy.expFrom} лет
                                <#elseif vacancy.expTo??>
                                    до ${vacancy.expTo} лет
                                <#else>
                                    Без опыта
                                </#if>
                            </div>

                            <#if vacancy.description??>
                                <p class="card-text flex-grow-1">${vacancy.description}</p>
                            </#if>

                            <#if vacancy.categoryId??>
                                <div class="mb-2">
                                    <span class="badge bg-info">Категория: ${vacancy.categoryId}</span>
                                </div>
                            </#if>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-3">
                                <button class="btn btn-outline-primary me-md-2">Откликнуться</button>
                            </div>
                        </div>
                    </div>
                </div>
            </#if>
        </#list>
    </div>

    <#if vacancies?filter(v -> v.isActive)?size == 0>
        <div class="alert alert-info mt-4" role="alert">
            На данный момент активных вакансий нет.
        </div>
    </#if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>