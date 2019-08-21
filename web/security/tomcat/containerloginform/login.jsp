<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Home page</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.css">
</head>
<body>

<section class="ui container">

    <h2>Login form</h2>

    <form class="ui form" name="loginForm" method="POST" action="j_security_check">
        <div class="field">
            <label for="userNameId">User name: </label>
            <input id="userNameId" type="text" name="j_username" size="20">
        </div>

        <div class="field">
            <label for="passId">Password: </label>
            <input id="passId" type="password" size="20" name="j_password">
        </div>

        <p>
            <button class="ui button" type="submit">Submit</button>
        </p>
    </form>

</section>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.js"></script>
</body>
</html>
