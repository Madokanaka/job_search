<#import "spring.ftl" as spring>
<#macro layout>
    <!doctype html>
    <html lang="${.lang}">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
                crossorigin="anonymous"></script>
        <script src="../static/scripts/main.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <title><@spring.message "title"/></title>
    </head>
    <body>

    <style>
        .navbar {
            top: 0;
        }
    </style>

    <#assign known = SPRING_SECURITY_CONTEXT?? >
    <#if known>
        <#assign
        userPr = SPRING_SECURITY_CONTEXT.authentication.principal
        name = userPr.getUsername()
        >
    </#if>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-5">
        <div class="container-fluid">
            <a class="navbar-brand" href="/vacancies"><@spring.message "navbar.brand"/></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="<@spring.message "navbar.toggle"/>">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/vacancies"><@spring.message "navbar.vacancies"/></a>
                    </li>
                    <#if userPr??>
                        <#if userPr.authorities?seq_contains("APPLICANT")>
                            <li class="nav-item">
                                <a class="nav-link" href="/companies"><@spring.message "navbar.companies"/></a>
                            </li>
                        <#elseif userPr.authorities?seq_contains("EMPLOYER")>
                            <li class="nav-item">
                                <a class="nav-link" href="/resumes"><@spring.message "navbar.resumes"/></a>
                            </li>
                        </#if>
                    </#if>
                    <li class="nav-item">
                        <div class="dropdown">
                            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1"
                                    data-bs-toggle="dropdown" aria-expanded="false">
                                <@spring.message "layout.nav.lang.exchange"/>
                            </button>
                            <ul class="dropdown-menu z-3" aria-labelledby="dropdownMenuButton1">
                                <li>
                                    <a class="dropdown-item" href="?lang=en">
                                        <img src="https://flagcdn.com/w320/gb.png" alt="English" class="me-2" style="width: 20px; height: 14px;">
                                        <@spring.message "layout.nav.lang_en"/>
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="?lang=ru">
                                        <img src="https://flagcdn.com/w320/ru.png" alt="Русский" class="me-2" style="width: 20px; height: 14px;">
                                        <@spring.message "layout.nav.lang_ru"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>

                </ul>


                <#if userPr??>
                    <a href="/profile" class="text-light m-2 m-sm-0">
                        <@spring.message "navbar.greeting"/> ${name}:
                    </a>
                    <#if _csrf??>
                        <form action="/logout" method="post">
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                            <button type="submit" class="btn btn-outline-info m-2"
                                    id="logout"><@spring.message "navbar.logout"/></button>
                        </form>
                    <#else>
                        <a href="/logout" class="btn btn-outline-info m-2"
                           id="logout"><@spring.message "navbar.logout"/></a>
                    </#if>
                <#else>
                    <a href="/auth/login"
                       class="btn btn-outline-info m-2"><@spring.message "navbar.login"/></a>
                    <a href="/auth/register"
                       class="btn btn-outline-info m-2"><@spring.message "navbar.register"/></a>
                </#if>
            </div>
        </div>
    </nav>

    <div class="container min-vh-100 align-items-center justify-content-center mt-5">
        <#nested spring>
    </div>

    </body>
    </html>
</#macro>