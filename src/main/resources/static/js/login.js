<!-- core logic -->
$(function () {
    $("#submit").click(function () {
        var username = $("#user").val();
        var password = $("#password").val();
        if (username.trim() == "" || password.trim() == "") {
            alert("Username and password cannot be empty");
            return;
        }
        //execute asynchronous ajax http request
        $.ajax({
            url: "/user/login",
            data: {"username": username, "password": password},
            type: "POST",
            dataType: "json",
            success: function (data) {
                console.log(data);
                if (data.status == 0) {
                    alert("login successfully");
                    window.location.href = "list.html";
                } else {
                    alert("username or password incorrect!");
                    $("#user").val("");
                    $("#password").val("");
                }
            }
        });
    });
});