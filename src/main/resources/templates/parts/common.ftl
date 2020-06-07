<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Memorandum</title>
        <link rel="stylesheet" href="/static/style.css">
    </head>
    <body>
    <#nested>
    </body>
    </html>
</#macro>

<#macro navbar>
    <div>Hello, user /${name?if_exists}</div>
    <a href="/main">main</a>
    <a href="/login">login</a>
    <a href="/registration">registration</a>
    <a href="/user">user</a>
    <hr/>
</#macro>
