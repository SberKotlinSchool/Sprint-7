<#import "commons/csrf_token.ftlh" as t />

<!DOCTYPE html>
<html lang="ru">

<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0">
  <!-- icon -->
  <link rel="shortcut icon" href="../static/svg/logo/free-icon-font-address-book.svg" type="image/png">

  <title>Вход</title>
  <link rel="stylesheet" href="../static/css/styles.css">
  <script src="../static/js/commons.js" defer></script>
</head>

<body>
<div class="background">
  <div class="modal">
    <div class="modal_container">

      <h4>Вход</h4>
      <form method="post" action="/login">
        <@t.token/>
        <div class="flex-container column">
          <div class="text-input login-input">
            <div class="flex-container column">
              <div class="input-title">
                Логин
              </div>
              <input type="text" name="username" placeholder="" required autofocus autocomplete="off">
            </div>
          </div>
        </div>
        <div class="flex-container column">
          <div class="text-input login-input">
            <div class="flex-container column">
              <div class="input-title">
                Пароль
              </div>
              <input type="password" name="password" placeholder="" required autocomplete="off">
            </div>
          </div>
        </div>
        <br>
        <div class="flex-container column">
          <div class="text-input login-input">
            <div class="flex-container column">
              <div class="input-title">
              </div>
            </div>
          </div>
        </div>
        <br>
        <div class="flex-container button-row margin-button">
          <input type="submit" id="submitButton" class="interface-color green-button text-button" value="Войти"/>
        </div>
      </form>

    </div>
  </div>
</div>

</body>

</html>

