<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>

<link rel="stylesheet" href="css/navigation.css">
<script type="text/javascript">
    $(document).ready(function () {
        $(".menu").click(function (event) {
            $('#content').load('ContentController', {content: $(this).attr('id')});
        });
    });
</script>

<table>
    <tr>
        <td><a class="menu" id="home" href=#>
            <div class="nav-item">
                <%@include file="res/icons/home.svg"%>
                <span>Home</span>
            </div>
        </a></td>
        <td><a class="menu" id="RegisterController" href=#>
            <div class="nav-item signup">
                <%@include file="res/icons/register.svg"%>
                <span>Registration</span>
            </div>
        </a></td>
        <td><a class="menu" id="LoginController" href=#>
            <div class="nav-item">
                <div class="rotated">
                    <%@include file="res/icons/key.svg"%>
                </div>
                <span>Login</span>
            </div>
        </a></td>
    </tr>
</table>	