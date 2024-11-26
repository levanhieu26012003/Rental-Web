import React, { useState, useRef, useCallback, useEffect } from 'react';
import { GoogleMap, useLoadScript, Marker, Circle, Autocomplete } from '@react-google-maps/api';
import { gql, useQuery } from '@apollo/client';
import { Box, TextField, Button } from '@mui/material';

const libraries = ["places"];
const mapContainerStyle = {
  width: "100%",
  height: "400px",
};

const center = {
  lat: 10.762622,
  lng: 106.660172,
};

const GET_MOTELS_BY_RADIUS = gql`
  query getAllMotels($lat: Float!, $lng: Float!, $radius: Float!) {
    getAllMotels(lat: $lat, lng: $lng, radius: $radius) {
      motels {
        id
        title
        lng
        lat
        address
        price
        area
        numberTenant
        wards
        district
        province
        status
        createdDate
      }
      totalCount
    }
  }
`;

const RentalMapWithRadius = () => {
  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries,
  });

  const [location, setLocation] = useState({ lat: center.lat, lng: center.lng });
  const [radius, setRadius] = useState(0);
  const [circleCenter, setCircleCenter] = useState(center);
  const [address, setAddress] = useState("");
  const [motelsData, setMotelsData] = useState(null);
  const [currentIndex, setCurrentIndex] = useState(0);
 
  const { data, refetch } = useQuery(GET_MOTELS_BY_RADIUS, {
    variables: {
      lat: location.lat,
      lng: location.lng,
      radius: radius / 1000,
    },
    skip: true,
  });

  const mapRef = useRef();
  const autocompleteRef = useRef();

  const onMapLoad = useCallback((map) => {
    mapRef.current = map;
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      setCurrentIndex(1);
    }, 1000);
    return () => clearTimeout(timer);
  }, [data, circleCenter])

  const handlePlaceSearch = async () => {
    if (circleCenter) {
      setLocation(circleCenter);
      const response = await refetch({
        lat: circleCenter.lat,
        lng: circleCenter.lng,
        radius: radius / 1000,
      });
      setMotelsData(response.data);
    }
  };

  const handleRadiusChange = (e) => {
    setRadius(Number(e.target.value));
  };

  const onPlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place && place.geometry) {
      const { lat, lng } = place.geometry.location;
      setCircleCenter({ lat: lat(), lng: lng() });
      setAddress(place.formatted_address); // Set the selected address to input
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
      <TextField
        label="Enter radius in meters"
        variant="outlined"
        fullWidth
        type="number"
        value={radius}
        onChange={handleRadiusChange}
        style={{ marginTop: 10 }}
      />
      <Button variant="contained" color="primary" onClick={handlePlaceSearch} style={{ marginTop: 10 }}>
        Search in this area
      </Button>

      <GoogleMap
        mapContainerStyle={mapContainerStyle}
        zoom={12}
        center={circleCenter}
        onLoad={onMapLoad}
      >
        <Circle
          center={circleCenter}
          key={currentIndex + 1} // Dynamic key based on position and radius
          radius={radius}
          options={{ fillColor: "#AA0000" }}
        />
        {motelsData && motelsData.getAllMotels.motels.map((motel, index) => (
          <Marker
            key={index}
            position={{ lat: motel.lat, lng: motel.lng }}
            title={motel.title}
          />
        ))}
      </GoogleMap>
    </Box>
  );
};

export default RentalMapWithRadius;
