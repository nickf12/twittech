<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" session="false" %>

<link rel="stylesheet" href="css/navigation.css">
<script type="text/javascript">
    $(document).ready(function () {
        $(".menu").click(function (event) {
            if($(this).val() == 'home') {
                location.assign('/home');
            }
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
        <td><a class="menu" id="Profile" href=#>
            <div class="nav-item">
                <%@include file="res/icons/profile.svg"%>
                <span>Profile</span>
            </div>
        </a></td>
        <td><a class="menu" id="LogoutController" href=#>
            <div class="nav-item">
                <%@include file="res/icons/logout.svg"%>
                <span>Logout</span>
            </div>
        </a></td>
    </tr>
</table>


