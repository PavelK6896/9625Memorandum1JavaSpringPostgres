<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#--    текущий контекст взятые с сервера переменые-->
<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    currentUserId = user.getId()
    >
<#else>
    <#assign
    name = "sesion no"
    isAdmin = false
    currentUserId = -1
    >
</#if>