<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <@c.navbar></@c.navbar>
    Add new user
    ${message}

    <@l.login "/registration" />

</@c.page>
