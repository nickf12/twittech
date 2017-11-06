<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="css/personalData.css">
<script src="js/personalData.js"></script>

<form method="post" enctype=multipart/form-data id="personalDataForm">
    <label>Age</label>
    <input name="age" type="text" pattern="[0-9]+$" value="${sessionScope.personalData.getAge()}"/>

    <label>Gender</label>
    <select name="gender">
        <option selected>${sessionScope.personalData.getGender()}</option>
        <option value="male">Male</option>
        <option value="female">Female</option>
        <option value="other">Other</option>
    </select>
    <label>Language</label>
    <input name="language" type="text" value="${sessionScope.personalData.getLanguage()}"/>
    <label>Job</label>
    <input name="job" type="text" value="${sessionScope.personalData.getJob()}"/>
    <label>Company</label>
    <input name="company" type="text" value="${sessionScope.personalData.getCompany()}"/>
    <label>Description</label>
    <textarea name="description">${sessionScope.personalData.getDescription()}</textarea>
    <label>Image</label>
    <input name="image" type="file" accept="image/*"/>
    <div class="formFooter column">
        <input type="submit" value="Save"/>
        <input type="submit" value="Later" onclick="showContent('/Profile')"/>
    </div>
</form>



