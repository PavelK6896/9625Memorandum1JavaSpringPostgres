<#import "parts/common.ftl" as c>
<#--страница подписок-->
<@c.page>
<h3>${userChannel.username}</h3>
<#--    //подписчики или подписки-->
<div>${type}</div>
<ul class="list-group">
<#--    цикол -->
    <#list users as user>
        <li class="list-group-item">
            <a href="/user-messages/${user.id}">${user.getUsername()}</a>
        </li>
    </#list>
</ul>
</@c.page>
