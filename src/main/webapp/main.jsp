<%@ page language="java" contentType="text/html; charset=EUC-KR"
         pageEncoding="EUC-KR"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Insert title here</title>
    <script
            src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA25s854jG9aLWJ3878CaiMNShBCN5WinE&sensor=false&libraries=places"></script>
    <script>
        function initAutocomplete() {
            const map = new google.maps.Map(document.getElementById("map-canvas"), {
                center: { lat: -33.8688, lng: 151.2195 },
                zoom: 4,
                mapTypeId: "roadmap",
            });

            const input = document.getElementById("pac-input");
            const searchBox = new google.maps.places.SearchBox(input);

            map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

            map.addListener("bounds_changed", () => {
                searchBox.setBounds(map.getBounds());
            });

            let markers = [];

            searchBox.addListener("places_changed", () => {
                const places = searchBox.getPlaces();

                if (places.length == 0) {
                    return;
                }

                markers.forEach((marker) => {
                    marker.setMap(null);
                });
                markers = [];

                const bounds = new google.maps.LatLngBounds();

                places.forEach((place) => {
                    if (!place.geometry || !place.geometry.location) {
                        console.log("Returned place contains no geometry");
                        return;
                    }

                    const icon = {
                        url: place.icon,
                        size: new google.maps.Size(71, 71),
                        origin: new google.maps.Point(0, 0),
                        anchor: new google.maps.Point(17, 34),
                        scaledSize: new google.maps.Size(25, 25),
                    };

                    markers.push(
                        new google.maps.Marker({
                            map,
                            icon,
                            title: place.name,
                            position: place.geometry.location,
                        })
                    );
                    if (place.geometry.viewport) {
                        bounds.union(place.geometry.viewport);
                    } else {
                        bounds.extend(place.geometry.location);
                    }
                });
                map.fitBounds(bounds);
            });

            let infoWindow = new google.maps.InfoWindow({
                content: "Choose location",
                position: new google.maps.LatLng(-34.397, 150.644),
            });

            infoWindow.open(map);

            map.addListener("click", (mapsMouseEvent) => {
                infoWindow.close();

                infoWindow = new google.maps.InfoWindow({
                    position: mapsMouseEvent.latLng,
                });
                infoWindow.setContent(
                    document.getElementById("lat").value = mapsMouseEvent.latLng.lat(),
                    document.getElementById("lon").value = mapsMouseEvent.latLng.lng()
                );
                infoWindow.open(map);
            });
        }

        google.maps.event.addDomListener(window, 'load', initAutocomplete);
    </script>
</head>
<body>
<input
        id="pac-input"
        class="controls"
        type="text"
        placeholder="Search Box"
/>
<div id="map-canvas" style="height:600px; width:900px"></div>

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
</body>
</html>