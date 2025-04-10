<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Активные резюме</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="mb-0">Активные резюме</h1>
        <span class="badge bg-primary fs-6">
                <#if resumes??>${resumes?filter(r -> r.isActive)?size}<#else>0</#if> найдено
            </span>
    </div>

    <#if resumes??>
        <#assign activeResumes = resumes?filter(r -> r.isActive)>

        <#if activeResumes?size gt 0>
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                <#list activeResumes as resume>
                    <div class="col">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title mb-3">${resume.name!''}</h5>

                                <div class="d-flex align-items-center mb-2">
                                        <span class="badge bg-success text-white me-2">
                                            <#if resume.salary??>
                                                ${resume.salary?string["0"]} ₽
                                            <#else>
                                                З/П не указана
                                            </#if>
                                        </span>
                                    <span class="badge bg-info text-white">
                                            Категория: ${resume.categoryId!''}
                                        </span>
                                </div>

                                <div class="mt-auto pt-3">
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <button class="btn btn-outline-primary btn-sm me-md-2">
                                            <i class="bi bi-eye"></i> Просмотр
                                        </button>
                                        <button class="btn btn-primary btn-sm">
                                            <i class="bi bi-telephone"></i> Контакты
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        <#else>
            <div class="alert alert-info mt-4">
                <i class="bi bi-info-circle"></i> Активных резюме не найдено
            </div>
        </#if>
    <#else>
        <div class="alert alert-warning">
            <i class="bi bi-exclamation-triangle"></i> Данные о резюме временно недоступны
        </div>
    </#if>
</div>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>