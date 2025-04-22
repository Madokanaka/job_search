<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Компании</h1>
        </div>

        <#if employers?? && employers?size gt 0>
            <div class="row">
                <#list employers as employer>
                    <div class="col-12 col-md-6 col-lg-4 mb-4">
                        <div class="card h-100 shadow-sm">
                            <a href="/profile/${employer.id}" class="card-body d-block text-decoration-none text-dark">
                                <div class="d-flex flex-column align-items-center">
                                    <div class="mb-3">
                                        <#if employer.avatar??>
                                            <img src="${employer.avatar}" alt="Avatar" class="img-fluid rounded-circle" style="width: 120px; height: 120px;">
                                        <#else>
                                            <img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png" alt="Default Avatar" class="img-fluid rounded-circle" style="width: 120px; height: 120px;">
                                        </#if>
                                    </div>
                                    <h3 class="card-title mb-2">${employer.name} ${employer.surname}</h3>
                                    <div class="d-flex flex-wrap justify-content-center gap-3 mb-3">
                                        <span class="badge bg-primary fs-6">Тип: ${employer.accountType}</span>
                                        <span class="text-muted">Возраст: ${employer.age}</span>
                                    </div>
                                    <div class="card-text">
                                        <p>Email: ${employer.email}</p>
                                        <p>Телефон: ${employer.phoneNumber}</p>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </div>
                </#list>
            </div>

            <div class="d-flex justify-content-center mt-4">
                <nav aria-label="Pagination">
                    <ul class="pagination">
                        <#if (currentPage > 0)>
                            <li class="page-item">
                                <a class="page-link" href="/companies?page=${currentPage - 1}&size=6">«</a>
                            </li>
                        </#if>

                        <#list 0..(totalPages - 1) as i>
                            <li class="page-item <#if i == currentPage>active</#if>">
                                <a class="page-link" href="/companies?page=${i}&size=6">${i + 1}</a>
                            </li>
                        </#list>

                        <#if (currentPage?? && ((currentPage + 1) < totalPages))>
                            <li class="page-item">
                                <a class="page-link" href="/companies?page=${currentPage + 1}&size=6">»</a>
                            </li>
                        </#if>
                    </ul>
                </nav>
            </div>

        <#else>
            <div class="alert alert-info" role="alert">
                На данный момент активных компаний нет.
            </div>
        </#if>
    </div>
</@main.layout>
