<#import "parts/common.ftl" as c>

<@c.page>

    <div>list of user</div>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th>изменить роль</th>
            <th>id</th>
            <th>email</th>
            <th>password</th>
            <th>active</th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit - ${user.id}</a></td>
                <td>${user.id}</td>
                <td>${user.email?if_exists}</td>
                <td>${user.password?if_exists}</td>
                <td>${(user.active?string)?if_exists}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>

