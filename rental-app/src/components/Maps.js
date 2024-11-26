import React, { useCallback, useRef, useEffect, useState } from "react";
import { GoogleMap, Marker, useLoadScript } from "@react-google-maps/api";

const libraries = ["places"];
const mapContainerStyle = {
  width: "100%",
  height: "400px",
};

const initialCenter = {
  lat: 10.762622,
  lng: 106.660172, // Tọa độ của Thành phố Hồ Chí Minh
};

const options = {
  disableDefaultUI: true,
  zoomControl: true,
};

export default function MapComponent({ markers1 }) {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY, 
        libraries,
  });
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedPosition, setSelectedPosition] = useState(initialCenter); // Vị trí marker

  useEffect (()=>{
    if (markers1[0] ){
      setSelectedPosition({ lat:markers1[0].lat,lng: markers1[0].lng })
      if (mapRef.current) {
        mapRef.current.panTo(selectedPosition);  // Di chuyển bản đồ tới vị trí mới
      }
    }
    const timer = setTimeout(() => {
      setCurrentIndex(1); 
    }, 1);
    return () => clearTimeout(timer); 
  },[markers1])

  const mapRef = useRef();

  const onMapLoad = useCallback((map) => {
    mapRef.current = map;
  }, []);

  if (loadError) return "Lỗi khi tải bản đồ";
  if (!isLoaded) return "Đang tải bản đồ...";

  return (
    <div>
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        zoom={15}
        center={selectedPosition}
        options={options}
        onLoad={onMapLoad}
      >
        {/* Hiển thị markers từ props */}
        {markers1 && markers1.map((marker, index) => (
          <Marker 
            key={`${currentIndex} + ${index}`}
            position={{ lat: marker.lat, lng: marker.lng }} 
          />
        ))}
     
      </GoogleMap>
    </div>
  );
}
