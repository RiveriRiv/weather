<%@ page import="com.weather.forecast.data.WeatherForecast" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
         pageEncoding="EUC-KR"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
</head>
<body>

<input id="refresh" type="button" value="Choose new location" onclick="window.location='/weather'">

<form:form action="/weather/place" method="get" modelAttribute="place">
  <table>
  <tr>
    <td><form:label path="lat">Lat: </form:label></td>
    <td><form:input id="lat" type="text" path="lat"/></td>
  </tr>
  <tr>
    <td><form:label path="lon">Lon: </form:label></td>
    <td><form:input id="lon" type="text" path="lon"/></td>
  </tr>
  <tr>
    <td><input type="submit" id="submit" value="submit"/></td>
  </tr>
</table>
</form:form>

  <div>${dailyWeatherForecast}</div>

  <script>
    auto = setInterval(function() {
      if (document.getElementById("lat").value && document.getElementById("lat").value) {
        document.getElementById("submit").click();
      }
    }, 10000);
  </script>
</body>
</body>
</html>