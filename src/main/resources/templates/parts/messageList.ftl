<#include "security.ftl">

<div class="card-columns" id="message-list">
    <#list messages as message>
        <div class="card my-3" data-id="${message.id}">
            <#if message.filename??>
                <img src="/img/${message.filename}" class="card-img-top"/>
            </#if>
            <div class="m-2">

                <span>${message.text}</span><br/>
                <#--                отображение тегов-->
                <i>#${message.tag}</i>

            </div>
            <div class="card-footer text-muted">
                <#--                переход на конал пользователя-->
                <a href="/user-messages/${message.author.id}">${message.authorName} </a>
                <#--                проверка кому пренадлежит сообщение-->
                <#if message.author.id == currentUserId>
                <#--                    редактировать собственные сообщения по ид-->
<#--                    гиперсылка и идентификатор сообщения-->
                    <a class="btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                        Edit
                    </a>

                </#if>
            </div>
        </div>
    <#else>
        No message
    </#list>
</div>
