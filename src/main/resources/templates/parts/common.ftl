<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Sweater</title>
    </head>
    <body>
    <#nested>
    </body>
    </html>
</#macro>

<#macro navbar>
    <div>Hello, user</div>
    <a href="/main">main</a>
    <a href="/login">login</a>
    <a href="/registration">registration</a>
    <a href="/user">user</a>
    <hr/>
</#macro>
