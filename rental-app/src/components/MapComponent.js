import React, { useState, useCallback, useRef, useEffect } from "react";
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

export default function MapComponent() {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY, // Đặt API key của bạn ở đây
    libraries,
  });

  const [selectedPosition, setSelectedPosition] = useState(initialCenter); // Vị trí marker
  const mapRef = useRef();
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect (()=>{
    const interval = setInterval(() => {
      setCurrentIndex(1); 
    }, 3000);

    return () => clearInterval(interval); 
  },[])

  const onMapClick = useCallback((event) => {
    const lat = event.latLng.lat();
    const lng = event.latLng.lng();
    
    setSelectedPosition({ lat, lng });
    
  }, []);

  const onMapLoad = useCallback((map) => {
    mapRef.current = map;
  }, []);

  if (loadError) return "Lỗi khi tải bản đồ";
  if (!isLoaded) return "Đang tải bản đồ...";

  return (
    <div>
      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        zoom={12}
        center={selectedPosition} // Đặt center là vị trí hiện tại của marker
        options={options}
        onLoad={onMapLoad}
        onClick={onMapClick} // Xử lý sự kiện click
      >
        {/* Marker ở vị trí đã chọn */}
        <Marker key={`${currentIndex}`}
          position={selectedPosition} // Marker di chuyển theo vị trí đã chọn
        />
      </GoogleMap>
    </div>
  );
}
