<#import "commons/csrf_token.ftlh" as t />
<#import "commons/admin_page.ftlh" as a />

<@a.admin_page 'Админка'>

  <div class="search-buttons-block flex-container row">
    <form method="post" action="/user/admin_panel/filter">
        <@t.token />
      <div class="search-block flex-container row">
          <#if filter?? && filter?has_content>
            <input type="text" name="filter" autocomplete="off" placeholder="Поиск по ФИО/логину..." class="search-input" value="${filter}"/>
          <#else>
            <input type="text" name="filter" autocomplete="off"  placeholder="Поиск по ФИО/логину..." class="search-input"/>
          </#if>

        <div class="submit_wrapper">
          <input type="submit" id="submitButton" hidden value="send"/>
          <label for="submitButton">
										<span class="flex-container interface-color">
											<div class="button yellow-button margin-button">
												<img src="${baseUrl}/static/svg/search/free-icon-font-search-3917132.svg" width="20px" height="16px">
											</div>
										</span>
          </label>
        </div>
          <#if filter?? && filter?has_content>
            <div class="flex-container button-row">
              <a href="/user/admin_panel" class="interface-color">
									<span class="flex-container">
										<div class="button red-button margin-button">
											<img src="${baseUrl}/static/svg/cross/free-icon-font-cross-3917188.svg" width="20px" height="16px">
										</div>
									</span>
              </a>
            </div>
          </#if>
      </div>
    </form>
    <div class="search-space"></div>
  </div>

  <div>
    <table>
      <tr class>
        <th>Логин</th>
        <th>ФИО</th>
        <th>Бан</th>
      </tr>

        <#if users?has_content>
            <#list users as user>
              <tr>
                <td>${user.login}</td>
                <td>${user.name}</td>
                <td>
                    <#if user.isAccountNonLocked()>
                      <a href="/user/admin_panel/user/${user.id}/ban" class="interface-color" target="_self" onclick="confirmBan(event)">
                          <span class="flex-container">
                            <div class="button red-button">
                              <img src="${baseUrl}/static/svg/delete_user/free-icon-font-remove-user-3914336.svg" width="20px" height="16px">
                            </div>
                          </span>
                      </a>
                    <#else >
                      <a href="/user/admin_panel/user/${user.id}/delete_ban" class="interface-color" target="_self" onclick="confirmDeleteBan(event)">
                        <span class="flex-container">
                          <div class="button green-button">
                            <img src="${baseUrl}/static/svg/add_user/free-icon-font-user-add-3917698.svg" width="20px" height="16px">
                          </div>
                        </span>
                      </a>
                    </#if>
                </td>
              </tr>
            </#list>
        </#if>
    </table>
  </div>
  </div>

</@a.admin_page>



