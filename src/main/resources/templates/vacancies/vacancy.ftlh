<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container py-4">
        <div class="card shadow-sm border-0">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h3 class="mb-0">${vacancy.name}</h3>
                <span class="badge bg-light text-dark">
                    <#if vacancy.isActive>Активна<#else>Неактивна</#if>
                </span>
            </div>
            <div class="card-body">
                <div class="row g-4">
                    <div class="col-lg-8">
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Описание вакансии</h5>
                            <p class="text-muted">${vacancy.description!'Нет описания'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Категория</h5>
                            <p>${vacancy.categoryName!'Не указана'}</p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Зарплата</h5>
                            <p class="fw-bold text-success fs-5">
                                ${vacancy.salary?string.currency}
                            </p>
                        </section>
                        <section class="mb-4">
                            <h5 class="fw-bold text-primary">Требуемый опыт</h5>
                            <p>${vacancy.expFrom} - ${vacancy.expTo} лет</p>
                        </section>
                    </div>
                    <div class="col-lg-4">
                        <div class="card h-100 border-light shadow-sm">
                            <div class="card-body">
                                <h6 class="card-title fw-bold text-primary">Детали</h6>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Создано:</span>
                                        <span>${vacancy.createdDate.toLocalDate()}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between">
                                        <span>Обновлено:</span>
                                        <span>${vacancy.updateTime.toLocalDate()}</span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>