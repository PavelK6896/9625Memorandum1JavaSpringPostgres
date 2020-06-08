<#import "parts/common.ftl" as c>

<@c.page>

    <div>list of user</div>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th>w</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit - ${user.id}</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>

