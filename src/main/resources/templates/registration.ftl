<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div>Hello, user</div>
    <a href="/main">main</a>
    <a href="/login">login</a>
    <a href="/registration">registration</a>
    <hr/>
    Add new user
    ${message}

    <@l.login "/registration" />

</@c.page>
