<html>
    <head></head>
    <body>
        <h1>Hi!</h1>
        <br/>
        ${message}
        <form action="/auth" method="POST">
            Email
            <br/>
            <input type="text" name="email" id="email"/>
            <br/>
            Password
            <br/>
            <input type="password" name="pw" id="pw"/>
            <br/>
            <input type="submit"/>
        </form>
    </body>
</html>