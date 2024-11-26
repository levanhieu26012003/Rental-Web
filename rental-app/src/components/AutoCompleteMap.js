import React, { useState, useRef, useCallback } from 'react';
import { GoogleMap, useLoadScript, Autocomplete, Marker } from '@react-google-maps/api';
import { Box, TextField, Button } from '@mui/material';

const libraries = ["places"];
const mapContainerStyle = {
  width: "100%",
  height: "400px",
};

// Default center (Ho Chi Minh City)
const center = {
  lat: 10.762622,
  lng: 106.660172,
};

const RentalMapWithAutocomplete = () => {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY, // Thay bằng API key của bạn
    libraries,
  });

  const [location, setLocation] = useState(center);
  const [address, setAddress] = useState("");
  const autocompleteRef = useRef();

  const onPlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place && place.geometry) {
      const { lat, lng } = place.geometry.location;
      setLocation({ lat: lat(), lng: lng() });
    }
  };

  const onLoad = (autocomplete) => {
    autocompleteRef.current = autocomplete;
  };

  if (loadError) return "Error loading maps";
  if (!isLoaded) return "Loading maps";

  return (
    <Box>
      <Autocomplete
        onLoad={onLoad}
        onPlaceChanged={onPlaceChanged}
      >
        <TextField
          label="Enter a location"
          variant="outlined"
          fullWidth
          value={address}
          onChange={(e) => setAddress(e.target.value)}
        />
      </Autocomplete>

      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        zoom={12}
        center={location}
      >
        <AdvancedMarkerElement  position={location} />
      </GoogleMap>
    </Box>
  );
};

export default RentalMapWithAutocomplete;
